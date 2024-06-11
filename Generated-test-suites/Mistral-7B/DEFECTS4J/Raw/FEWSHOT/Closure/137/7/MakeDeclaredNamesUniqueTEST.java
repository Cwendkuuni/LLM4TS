package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp. traversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp. traversal.NodeTraversal;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Tests for {@link MakeDeclaredNamesUnique} class.
 */
public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testSimpleRenaming() {
    String jsCode = "function foo() { var x = 1; var x = 2; }";

    Node node = parse(jsCode);

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal traversal = new NodeTraversal(node);
    traversal.setScopedCallback(renamer);
    traversal.traverse();

    Node fooFunction = node.getFirstChild();
    Node xDeclaration1 = fooFunction.getFirstChild().getNext();
    Node xDeclaration2 = xDeclaration1.getNext();

    assertEquals("x1", xDeclaration1.getString());
    assertEquals("x2", xDeclaration2.getString());
  }

  @Test
  public void testContextualRenaming() {
    String jsCode = "function foo() { function bar() {} }";

    Node node = parse(jsCode);

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal traversal = new NodeTraversal(node);
    traversal.setScopedCallback(renamer);
    traversal.traverse();

    Node fooFunction = node.getFirstChild();
    Node barFunction = fooFunction.getLastChild();

    assertEquals("bar1", barFunction.getString());
  }

  @Test
  public void testGlobalRenaming() {
    String jsCode = "var x = 1, y = 2, x = 3;";

    Node node = parse(jsCode);

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal traversal = new NodeTraversal(node);
    traversal.setScopedCallback(renamer);
    traversal.traverse();

    List<Node> declarations = Lists.newArrayList();
    Node currentNode = node;
    while (currentNode != null) {
      if (NodeUtil.isVarDeclaration(currentNode)) {
        declarations.add(currentNode);
      }
      currentNode = currentNode.getPrevious();
    }

    Map<String, Integer> nameCount = Maps.newHashMap();
    for (Node declaration : declarations) {
      String name = declaration.getString();
      int count = nameCount.getOrDefault(name, 0);
      nameCount.put(name, count + 1);
    }

    assertEquals(Arrays.asList("x1", "y", "x2"), getNames(nameCount.keySet()));
  }

  @Test
  public void testNestedRenaming() {
    String jsCode = "function foo() { function bar() { var x = 1; } }";

    Node node = parse(jsCode);

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal traversal = new NodeTraversal(node);
    traversal.setScopedCallback(renamer);
    traversal.traverse();

    Node fooFunction = node.getFirstChild();
    Node barFunction = fooFunction.getLastChild();
    Node xDeclaration = barFunction.getFirstChild().getNext();

    assertEquals("x1", xDeclaration.getString());
  }

  @Test
  public void testRecursiveFunctionRenaming() {
    String jsCode = "function foo() { function foo() {} }";

    Node node = parse(jsCode);

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal traversal = new NodeTraversal(node);
    traversal.setScopedCallback(renamer);
    traversal.traverse();

    Node outerFooFunction = node.getFirstChild();
    Node innerFooFunction = outerFooFunction.getLastChild();

    assertEquals("foo1", innerFooFunction.getString());
  }

  @Test
  public void testCatchBlockRenaming() {
    String jsCode = "try { foo() } catch (e) { var x = 1; }";

    Node node = parse(jsCode);

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal traversal = new NodeTraversal(node);
    traversal.setScopedCallback(renamer);
    traversal.traverse();

    Node catchBlock = node.getLastChild();
    Node xDeclaration = catchBlock.getLastChild();

    assertEquals("x1", xDeclaration.getString());
  }

  @Test
  public void testObjectPropertyRenaming() {
    String jsCode = "var obj = { x: 1 }; obj.x = 2;";

    Node node = parse(jsCode);

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal traversal = new NodeTraversal(node);
    traversal.setScopedCallback(renamer);
    traversal.traverse();

    Node objNode = node.getFirstChild();
    Node xDeclaration1 = objNode.getFirstChild();
    Node xDeclaration2 = xDeclaration1.getNext();

    assertEquals("x1", xDeclaration1.getString());
    assertEquals("x2", xDeclaration2.getString());
  }

  @Test
  public void testObjectPropertyNotRenaming() {
    String jsCode = "var obj = { x: 1 }; obj.y = 2;";

    Node node = parse(jsCode);

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal traversal = new NodeTraversal(node);
    traversal.setScopedCallback(renamer);
    traversal.traverse();

    Node objNode = node.getFirstChild();
    Node yDeclaration = objNode.getLastChild();

    assertEquals("y", yDeclaration.getString());
  }

  @Test
  public void testGlobalPropertyNotRenaming() {
    String jsCode = "var x = 1; window.x = 2;";

    Node node = parse(jsCode);

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal traversal = new NodeTraversal(node);
    traversal.setScopedCallback(renamer);
    traversal.traverse();

    Node xDeclaration1 = node.getFirstChild();
    Node xDeclaration2 = node.getLastChild();

    assertEquals("x", xDeclaration1.getString());
    assertEquals("x", xDeclaration2.getString());
  }

  private Node parse(String jsCode) {
    TokenStream tokenStream = new TokenStream(new TokenStreamFactory());
    tokenStream.reset(jsCode.toCharArray());
    Node rootNode = tokenStream.parseProgram();

    return rootNode;
  }

  private List<String> getNames(Iterable<String> names) {
    ImmutableSet<String> nameSet = ImmutableSet.copyOf(names);
    return ImmutableList.copyOf(nameSet.toArray(new String[0]));
  }
}