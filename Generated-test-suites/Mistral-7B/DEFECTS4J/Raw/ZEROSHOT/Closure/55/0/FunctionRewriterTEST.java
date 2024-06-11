import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Token;

import java.util.List;

public class FunctionRewriterTest {

  private AbstractCompiler compiler;
  private FunctionRewriter functionRewriter;

  @org.junit.Before
  public void setUp() {
    compiler = new Compiler();
    functionRewriter = new FunctionRewriter(compiler);
  }

  @org.junit.Test
  public void testEmptyFunctionReducer() {
    Node root = createNode("function() {}");
    functionRewriter.process(null, root);
    Node expected = createNode("emptyFn()");
    Node actual = root.getFirstChild();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testIdentityFunctionReducer() {
    Node root = createNode("function(x) { return x; }");
    functionRewriter.process(null, root);
    Node expected = createNode("getter(\"x\")");
    Node actual = root.getFirstChild();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testReturnConstantReducer() {
    Node root = createNode("function() { return 10; }");
    functionRewriter.process(null, root);
    Node expected = createNode("returnconst(10)");;
    Node actual = root.getFirstChild();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testGetterReducer() {
    Node root = createNode("function() { return this.a; }");
    functionRewriter.process(null, root);
    Node expected = createNode("getter(\"a\")");
    Node actual = root.getFirstChild();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testSetterReducer() {
    Node root = createNode("function(value) { this.a = value; }");
    functionRewriter.process(null, root);
    Node expected = createNode("getter(\"a\")");
    Node actual = root.getFirstChild();
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testMultipleReducers() {
    Node root = createNode("function() { return this.a; } function() { return this.b; }");
    functionRewriter.process(null, root);
    Node expected = createNode("getter(\"a\")", "getter(\"b\")");
    Node actual = root.getFirstChild();
    List<Node> actualChildren = actual.getChildren();
    assertEquals(expected.getChildren().size(), actualChildren.size());
    for (int i = 0; i < expected.getChildren().size(); i++) {
      Node expectedChild = expected.getChild(i);
      Node actualChild = actualChildren.get(i);
      assertEquals(expectedChild, actualChild);
    }
  }

  @org.junit.Test
  public void testNoReduction() {
    Node root = createNode("function() {}");
    functionRewriter.process(null, root);
    Node actual = root.getFirstChild();
    assertTrue(actual.getType() == FUNCTION);
  }

  @org.junit.Test
  public void testNotFunctionExpression() {
    Node root = createNode("var a = function() {}");
    functionRewriter.process(null, root);
    Node actual = root.getFirstChild();
    assertTrue(actual.getType() != FUNCTION);
  }

  private Node createNode(String jsCode) {
    return compiler.parseAndCompileToAST(jsCode, null).getRootNode();
  }

  private static class Compiler extends AbstractCompiler {
    @Override
    public void reportCodeChange() {
      // Do nothing.
    }
  }
}