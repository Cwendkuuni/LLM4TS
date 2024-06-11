import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.RemoveConstantExpressions;
import com.google.javascript.jscomp.RemoveConstantRValuesCallback;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.NodeFactory;

import java.util.Arrays;
import java.util.List;

public class RemoveConstantExpressionsTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private CompilerPassManager passManager;

  @org.junit.Before
  public void setUp() throws Exception {
    compiler = new TestCompiler();
    nodeFactory = compiler.getFactory();
    passManager = new CompilerPassManager(compiler);
  }

  @org.junit.Test
  public void testRemoveConstantExpressionWithNoSideEffects() {
    String inputCode = "1 + foo() + bar()";
    Node rootNode = testAndGetRootNode(inputCode);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(passManager);
    pass.process(null, rootNode);
    Node expectedRoot = nodeFactory.createText("foo();bar()");

    List<Node> actual = getPostOrderTraversalList(rootNode);
    List<Node> expected = getPostOrderTraversalList(expectedRoot);

    assertEquals(expected.size(), actual.size());
    for (int i = 0; i < expected.size(); i++) {
      Node actualNode = actual.get(i);
      Node expectedNode = expected.get(i);
      assertEquals(expectedNode.getType(), actualNode.getType());
      if (expectedNode.getType() == EXPR_STATEMENT) {
        assertEquals(expectedNode.getFirstChild().toString(),
            actualNode.getFirstChild().toString());
      }
    }
  }

  @org.junit.Test
  public void testRemoveConstantExpressionWithSideEffects() {
    String inputCode = "document.write(1 + foo() + bar())";
    Node rootNode = testAndGetRootNode(inputCode);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(passManager);
    pass.process(null, rootNode);
    Node expectedRoot = nodeFactory.createCall("document.write", Arrays.asList(
        nodeFactory.createCall("foo"),
        nodeFactory.createCall("bar")
    ));

    List<Node> actual = getPostOrderTraversalList(rootNode);
    List<Node> expected = getPostOrderTraversalList(expectedRoot);

    assertEquals(expected.size(), actual.size());
    for (int i = 0; i < expected.size(); i++) {
      Node actualNode = actual.get(i);
      Node expectedNode = expected.get(i);
      assertEquals(expectedNode.getType(), actualNode.getType());
      if (expectedNode.getType() == CALL_EXPRESSION) {
        assertEquals(expectedNode.getFirstChild().toString(),
            actualNode.getFirstChild().toString());
      }
    }
  }

  @org.junit.Test
  public void testRemoveConstantExpressionWithSideEffectsAndNested() {
    String inputCode = "document.write(1 + foo() + bar()) ++ x";
    Node rootNode = testAndGetRootNode(inputCode);

    RemoveConstantExpressions pass = new RemoveConstantExpressions(passManager);
    pass.process(null, rootNode);
    Node expectedRoot = nodeFactory.createCall("document.write", Arrays.asList(
        nodeFactory.createCall("foo"),
        nodeFactory.createCall("bar")
    ));
    Node expectedX = nodeFactory.createIdentifier("x");

    List<Node> actual = getPostOrderTraversalList(rootNode);
    List<Node> expected = Arrays.asList(
        expectedRoot,
        expectedX
    );

    assertEquals(expected.size(), actual.size());
    for (int i = 0; i < expected.size(); i++) {
      Node actualNode = actual.get(i);
      Node expectedNode = expected.get(i);
      assertEquals(expectedNode.getType(), actualNode.getType());
      if (expectedNode.getType() == CALL_EXPRESSION) {
        assertEquals(expectedNode.getFirstChild().toString(),
            actualNode.getFirstChild().toString());
      }
      if (expectedNode.getType() == IDENTIFIER) {
        assertEquals("x", expectedNode.toString());
      }
    }
  }

  private Node testAndGetRootNode(String code) {
    TestSourceFile testSourceFile = new TestSourceFile(code, "test.js");
    Node rootNode = compiler.parse(testSourceFile, null, null);
    passManager.runPasses(Arrays.asList(passManager.getPass(RemoveConstantExpressions.class)), rootNode);
    return rootNode;
  }

  private List<Node> getPostOrderTraversalList(Node node) {
    List<Node> list = Lists.newArrayList();
    AbstractPostOrderCallback cb = new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal traversal, Node node, Node parent) {
        list.add(node);
      }
    };
    traverse(null, node, cb);
    return list;
  }
}