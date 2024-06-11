package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeTraversal;

@RunWith(MockitoJUnitRunner.class)
public class InlineObjectLiteralsTest {

  private AbstractCompiler compiler;
  private InlineObjectLiterals inlineObjectLiterals;

  @Before
  public void setup() {
    compiler = Mockito.mock(AbstractCompiler.class);
    inlineObjectLiterals = new InlineObjectLiterals(compiler, Mockito.mock(Supplier.class));
  }

  @Test
  public void testProcess_EmptyCode_NoChange() {
    Node externs = Node.NULL;
    Node root = Node.NULL;

    inlineObjectLiterals.process(externs, root);

    // No change expected
    assertTrue(NodeUtil.treeEquivalent(externs, Node.NULL));
    assertTrue(NodeUtil.treeEquivalent(root, Node.NULL));
  }

  @Test
  public void testProcess_SimpleObjectLiterals_InlineObject() {
    Node externs = Node.objectLit(
        Node.PROPERTY,
        Node.STRING_LITERAL("name"),
        Node.OBJECT_LITERAL(
            Node.PROPERTY,
            Node.STRING_LITERAL("age"),
            Node.INT_LITERAL(25)
        )
    );

    Node root = Node.variable("obj").assign(Node.NAME("obj"), Node.NAME("name"), Node.NAME("age"), Node.INT_LITERAL(25));

    inlineObjectLiterals.process(externs, root);

    ImmutableList<Node> expected = ImmutableList.of(
        Node.VAR_DECLARATION(Node.VAR("age"), Node.NODE(), Node.INT_LITERAL(25)),
        Node.VAR_DECLARATION(Node.VAR("name"), Node.NODE(), Node.STRING_LITERAL("name")),
        Node.VAR_DECLARATION(Node.VAR("obj"), Node.NODE(), Node.NULL),
        Node.ASSIGN(Node.NAME("obj"), Node.NAME("name"), Node.NAME("age"), Node.INT_LITERAL(25))
    );

    ImmutableList<Node> actual = NodeTraversal.deepTraverse(root).collect(NodeCollectingVisitor.getInstance());

    assertEquals(expected, actual);
  }

  @Test
  public void testProcess_ObjectLiteralsWithReusedKeys_InlineObject() {
    Node externs = Node.objectLit(
        Node.PROPERTY,
        Node.STRING_LITERAL("name1"),
        Node.OBJECT_LITERAL(
            Node.PROPERTY,
            Node.STRING_LITERAL("age"),
            Node.OBJECT_LITERAL(
                Node.PROPERTY,
                Node.STRING_LITERAL("name1"),
                Node.INT_LITERAL(1)
            )
        )
    );

    Node root = Node.variable("obj1").assign(Node.NAME("obj1"), Node.NAME("name1"), Node.NAME("age"), Node.objectLit(
        Node.PROPERTY,
        Node.STRING_LITERAL("name1"),
        Node.INT_LITERAL(1)
    ));

    inlineObjectLiterals.process(externs, root);

    ImmutableList<Node> expected = ImmutableList.of(
        Node.VAR_DECLARATION(Node.VAR("age"), Node.NODE(), Node.INT_LITERAL(1)),
        Node.VAR_DECLARATION(Node.VAR("name1"), Node.NODE(), Node.STRING_LITERAL("name1")),
        Node.VAR_DECLARATION(Node.VAR("obj1"), Node.NODE(), Node.NULL),
        Node.ASSIGN(Node.NAME("obj1"), Node.NAME("name1"), Node.NAME("age"), Node.INT_LITERAL(1))
    );

    ImmutableList<Node> actual = NodeTraversal.deepTraverse(root).collect(NodeCollectingVisitor.getInstance());

    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcess_InvalidObjectLiterals_ThrowsException() {
    Node externs = Node.objectLit(Node.INT_LITERAL(1));
    Node root = Node.NULL;

    inlineObjectLiterals.process(externs, root);

    fail("Should throw an exception, but did not.");
  }

  @Test
  public void testProcess_ObjectLiteralsWithSameKeysInDifferentScopes_InlineObject() {
    Node externs1 = Node.objectLit(
        Node.PROPERTY,
        Node.STRING_LITERAL("name1"),
        Node.OBJECT_LITERAL(
            Node.PROPERTY,
            Node.STRING_LITERAL("age"),
            Node.OBJECT_LITERAL(
                Node.PROPERTY,
                Node.STRING_LITERAL("name2"),
                Node.INT_LITERAL(2)
            )
        )
    );

    Node root1 = Node.variable("obj1").assign(Node.NAME("obj1"), Node.NAME("name1"), Node.NAME("age"), Node.objectLit(
        Node.PROPERTY,
        Node.STRING_LITERAL("name2"),
        Node.INT_LITERAL(2)
    ));

    Node externs2 = Node.objectLiterals(
        Node.objectLit(
            Node.PROPERTY,
            Node.STRING_LITERAL("name1"),
            Node.OBJECT_LITERAL(
                Node.PROPERTY,
                Node.STRING_LITERAL("age"),
                Node.INT_LITERAL(3)
            )
        )
    );

    Node root2 = Node.variable("obj2").assign(Node.NAME("obj2"), Node.NAME("name1"), Node.NAME("age"), Node.INT_LITERAL(3));

    inlineObjectLiterals.process(externs1, root1);
    inlineObjectLiterals.process(externs2, root2);

    ImmutableList<Node> expected1 = ImmutableList.of(
        Node.VAR_DECLARATION(Node.VAR("age"), Node.NODE(), Node.INT_LITERAL(2)),
        Node.VAR_DECLARATION(Node.VAR("name1"), Node.NODE(), Node.STRING_LITERAL("name1")),
        Node.VAR_DECLARATION(Node.VAR("obj1"), Node.NODE(), Node.NULL),
        Node.ASSIGN(Node.NAME("obj1"), Node.NAME("name1"), Node.NAME("age"), Node.INT_LITERAL(2))
    );

    ImmutableList<Node> expected2 = ImmutableList.of(
        Node.VAR_DECLARATION(Node.VAR("age"), Node.NODE(), Node.INT_LITERAL(3)),
        Node.VAR_DECLARATION(Node.VAR("name1"), Node.NODE(), Node.STRING_LITERAL("name1")),
        Node.VAR_DECLARATION(Node.VAR("obj2"), Node.NODE(), Node.NULL),
        Node.ASSIGN(Node.NAME("obj2"), Node.NAME("name1"), Node.NAME("age"), Node.INT_LITERAL(3))
    );

    ImmutableList<Node> actual1 = NodeTraversal.deepTraverse(root1).collect(NodeCollectingVisitor.getInstance());
    ImmutableList<Node> actual2 = NodeTraversal.deepTraverse(root2).collect(NodeCollectingVisitor.getInstance());

    assertEquals(expected1, actual1);
    assertEquals(expected2, actual2);
  }
}