package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.util.NodeUtil;

import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

  @Test
  public void testIsInlinableObject() {
    Node externs = Node.objectLiteral();
    Node root = Node.objectLiteral();

    InlineObjectLiterals pass = new InlineObjectLiterals(new AbstractCompiler() {}, () -> "test");
    pass.process(externs, root);

    List<Reference> refs = Lists.newArrayList(
        new Reference(Node.newIdentifier("o"), new Node(Token.NAME, "o"), null),
        new Reference(Node.newIdentifier("o1"), new Node(Token.NAME, "o1"), null),
        new Reference(Node.newIdentifier("o2"), new Node(Token.NAME, "o2"), null),
        new Reference(Node.newIdentifier("o3"), new Node(Token.NAME, "o3"), null),
        new Reference(Node.newIdentifier("o4"), new Node(Token.NAME, "o4"), null),
        new Reference(Node.newIdentifier("o5"), new Node(Token.NAME, "o5"), null)
    );

    // Test cases for inlinable objects
    assertTrue(InlineObjectLiterals.isInlinableObject(refs));

    // Test cases for non-inlinable objects with no assignment or no object literal
    List<Reference> refs1 = Lists.newArrayList(
        new Reference(Node.newIdentifier("x"), new Node(Token.NAME, "x"), null),
        new Reference(Node.newIdentifier("y"), new Node(Token.NAME, "y"), null)
    );
    assertFalse(InlineObjectLiterals.isInlinableObject(refs1));

    List<Reference> refs2 = Lists.newArrayList(
        new Reference(Node.newIdentifier("x"), new Node(Token.NAME, "x"), Node.newIdentifier("y")),
        new Reference(Node.newIdentifier("y"), new Node(Token.NAME, "y"), null)
    );
    assertFalse(InlineObjectLiterals.isInlinableObject(refs2));

    List<Reference> refs3 = Lists.newArrayList(
        new Reference(Node.newIdentifier("x"), new Node(Token.NAME, "x"), null),
        new Reference(Node.newIdentifier("y"), new Node(Token.NAME, "y"), null),
        new Reference(Node.newIdentifier("z"), new Node(Token.NAME, "z"), null)
    );
    new Reference(Node.newIdentifier("z"), new Node(Token.NAME, "z"), Node.newObjectLiteral());
    assertFalse(InlineObjectLiterals.isInlinableObject(refs3));

    // Test cases for non-inlinable objects with assignment but no object literal
    List<Reference> refs4 = Lists.newArrayList(
        new Reference(Node.newIdentifier("x"), new Node(Token.NAME, "x"), null),
        new Reference(Node.newIdentifier("y"), new Node(Token.NAME, "y"), null),
        new Reference(Node.newIdentifier("y"), new Node(Token.NAME, "y"), Node.newNumberLiteral(1))
    );
    assertFalse(InlineObjectLiterals.isInlinableObject(refs4));

    // Test cases for non-inlinable objects with object literal but no assignment
    List<Reference> refs5 = Lists.newArrayList(
        new Reference(Node.newIdentifier("x"), new Node(Token.NAME, "x"), Node.newObjectLiteral()),
        new Reference(Node.newIdentifier("y"), new Node(Token.NAME, "y"), null)
    );
    assertFalse(InlineObjectLiterals.isInlinableObject(refs5));

    // Test cases for non-inlinable objects with assignment and object literal
    List<Reference> refs6 = Lists.newArrayList(
        new Reference(Node.newIdentifier("x"), new Node(Token.NAME, "x"), null),
        new Reference(Node.newIdentifier("y"), new Node(Token.NAME, "y"), null),
        new Reference(Node.newIdentifier("x"), new Node(Token.NAME, "x"), Node.newObjectLiteral()),
        new Reference(Node.newIdentifier("y"), new Node(Token.NAME, "y"), Node.newNumberLiteral(1))
    );
    assertFalse(InlineObjectLiterals.isInlinableObject(refs6));

    // Test cases for self-referential objects
    List<Reference> refs7 = Lists.newArrayList(
        new Reference(Node.newIdentifier("x"), new Node(Token.NAME, "x"), null),
        new Reference(Node.newIdentifier("y"), new Node(Token.NAME, "y"), null),
        new Reference(Node.newIdentifier("x"), new Node(Token.NAME, "x"), Node.newIdentifier("y")),
        new Reference(Node.newIdentifier("y"), new Node(Token.NAME, "y"), Node.newIdentifier("x"))
    );
    assertFalse(InlineObjectLiterals.isInlinableObject(refs7));
  }

  @Test
  public void testSplitObject() {
    Node externs = Node.objectLiteral();
    Node root = Node.objectLiteral();

    InlineObjectLiterals pass = new InlineObjectLiterals(new AbstractCompiler() {}, () -> "test");
    pass.process(externs, root);

    Scope scope = new Scope();
    Var v = scope.declareVar("o", Node.newObjectLiteral());

    Reference declaration = new Reference(Node.newIdentifier("o"), new Node(Token.NAME, "o"), null);
    Reference init = new Reference(Node.newIdentifier("o"), new Node(Token.NAME, "o"), null);
    ReferenceCollection referenceInfo = new ReferenceCollection();
    referenceInfo.setInitializingDeclaration(init);
    referenceInfo.setReferences(Lists.newArrayList(declaration, init));

    Map<String, String> varmap = Maps.newHashMap();
    varmap.put("key1", "o_key1_0");
    varmap.put("key2", "o_key2_1");
    varmap.put("key3", "o_key3_2");

    Map<String, Node> initvals = Maps.newHashMap();
    fillInitialValues(init, initvals);

    replaceAssignmentExpression(v, declaration, varmap);

    Node expected = Node.newBlock(
        Node.newVar("o_key1_0", Node.newUndefinedNode(null)),
        Node.newVar("o_key2_1", Node.newUndefinedNode(null)),
        Node.newVar("o_key3_2", Node.newUndefinedNode(null)),
        Node.newList(Node.newIdent("true"))
    );

    Node actual = v.getScope().getRootNode();
    assertTrue(NodeUtil.treeEquals(expected, actual));
  }
}