import static com.google.javascript.jscomp.FunctionRewriter.SAVINGS_THRESHOLD;
import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;

public class FunctionRewriterTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    nodeFactory = compiler.getNodeFactory();
  }

  @Test
  public void testEmptyFunctionReducer() {
    String code = "function a() {}";
    Node root = compiler.parse(code);
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expectedHelperCode = nodeFactory.parseSyntheticCode(
        "JSCompiler_emptyFn:helper", FunctionRewriter.EmptyFunctionReducer.HELPER_SOURCE);
    Node actualHelperCode = findHelperCode(root, FunctionRewriter.EmptyFunctionReducer.class);

    assertNotNull(actualHelperCode);
    assertEquals(expectedHelperCode.getChildCount(), actualHelperCode.getChildCount());
    for (int i = 0; i < expectedHelperCode.getChildCount(); i++) {
      assertEquals(expectedHelperCode.getChild(i), actualHelperCode.getChild(i));
    }

    Node expectedRoot = nodeFactory.parse(
        "function a() {emptynode();} function emptynode() {}");
    Node actualRoot = findRootAfterProcessing(root, functionRewriter);

    assertEquals(expectedRoot.getChildCount(), actualRoot.getChildCount());
    for (int i = 0; i < expectedRoot.getChildCount(); i++) {
      assertEquals(expectedRoot.getChild(i), actualRoot.getChild(i));
    }
  }

  @Test
  public void testIdentityReducer() {
    String code = "function a(x) { return x; }";
    Node root = compiler.parse(code);
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expectedHelperCode = nodeFactory.parseSyntheticCode(
        "JSCompiler_identityFn:helper", FunctionRewriter.IdentityReducer.HELPER_SOURCE);
    Node actualHelperCode = findHelperCode(root, FunctionRewriter.IdentityReducer.class);

    assertNotNull(actualHelperCode);
    assertEquals(expectedHelperCode.getChildCount(), actualHelperCode.getChildCount());
    for (int i = 0; i < expectedHelperCode.getChildCount(); i++) {
      assertEquals(expectedHelperCode.getChild(i), actualHelperCode.getChild(i));
    }

    Node expectedRoot = nodeFactory.parse(
        "function a(x) { return identityFn(x); } function identityFn(x) { return x; }");
    Node actualRoot = findRootAfterProcessing(root, functionRewriter);

    assertEquals(expectedRoot.getChildCount(), actualRoot.getChildCount());
    for (int i = 0; i < expectedRoot.getChildCount(); i++) {
      assertEquals(expectedRoot.getChild(i), actualRoot.getChild(i));
    }
  }

  @Test
  public void testReturnConstantReducer() {
    String code = "function a() { return 10; }";
    Node root = compiler.parse(code);
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expectedHelperCode = nodeFactory.parseSyntheticCode(
        "JSCompiler_returnArg:helper", FunctionRewriter.ReturnConstantReducer.HELPER_SOURCE);
    Node actualHelperCode = findHelperCode(root, FunctionRewriter.ReturnConstantReducer.class);

    assertNotNull(actualHelperCode);
    assertEquals(expectedHelperCode.getChildCount(), actualHelperCode.getChildCount());
    for (int i = 0; i < expectedHelperCode.getChildCount(); i++) {
      assertEquals(expectedHelperCode.getChild(i), actualHelperCode.getChild(i));
    }

    Node expectedRoot = nodeFactory.parse(
        "function a() { return 10; } function returnconst(constValue) { return constValue; }");
    Node actualRoot = findRootAfterProcessing(root, functionRewriter);

    assertEquals(expectedRoot.getChildCount(), actualRoot.getChildCount());
    for (int i = 0; i < expectedRoot.getChildCount(); i++) {
      assertEquals(expectedRoot.getChild(i), actualRoot.getChild(i));
    }
  }

  @Test
  public void testGetterReducer() {
    String code = "function a() { return this.a; }";
    Node root = compiler.parse(code);
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expectedHelperCode = nodeFactory.parseSyntheticCode(
        "JSCompiler_get:helper", FunctionRewriter.GetterReducer.HELPER_SOURCE);
    Node actualHelperCode = findHelperCode(root, FunctionRewriter.GetterReducer.class);

    assertNotNull(actualHelperCode);
    assertEquals(expectedHelperCode.getChildCount(), actualHelperCode.getChildCount());
    for (int i = 0; i < expectedHelperCode.getChildCount(); i++) {
      assertEquals(expectedHelperCode.getChild(i), actualHelperCode.getChild(i));
    }

    Node expectedRoot = nodeFactory.parse(
        "function a() { return getter(\"a\"); } function getter(propertyName) { return this[propertyName]; }");
    Node actualRoot = findRootAfterProcessing(root, functionRewriter);

    assertEquals(expectedRoot.getChildCount(), actualRoot.getChildCount());
    for (int i = 0; i < expectedRoot.getChildCount(); i++) {
      assertEquals(expectedRoot.getChild(i), actualRoot.getChild(i));
    }
  }

  @Test
  public void testSetterReducer() {
    String code = "function a(x) { this.a = x; }";
    Node root = compiler.parse(code);
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node expectedHelperCode = nodeFactory.parseSyntheticCode(
        "JSCompiler_set:helper", FunctionRewriter.SetterReducer.HELPER_SOURCE);
    Node actualHelperCode = findHelperCode(root, FunctionRewriter.SetterReducer.class);

    assertNotNull(actualHelperCode);
    assertEquals(expectedHelperCode.getChildCount(), actualHelperCode.getChildCount());
    for (int i = 0; i < expectedHelperCode.getChildCount(); i++) {
      assertEquals(expectedHelperCode.getChild(i), actualHelperCode.getChild(i));
    }

    Node expectedRoot = nodeFactory.parse(
        "function a(x) { getter(\"a\"), setter(\"a\", x); } function getter(propertyName) { return this[propertyName]; } function setter(propertyName, value) { this[propertyName] = value; }");
    Node actualRoot = findRootAfterProcessing(root, functionRewriter);

    assertEquals(expectedRoot.getChildCount(), actualRoot.getChildCount());
    for (int i = 0; i < expectedRoot.getChildCount(); i++) {
      assertEquals(expectedRoot.getChild(i), actualRoot.getChild(i));
    }
  }

  @Test
  public void testSavingsThreshold() {
    String code = "function a() { return this.a; } function b() { return this.b; }";
    Node root = compiler.parse(code);
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node aGetter = findNodeByName(root, "a");
    Node bGetter = findNodeByName(root, "b");

    int savings = functionRewriter.estimatedSavings(aGetter) +
        functionRewriter.estimatedSavings(bGetter);

    assertFalse(savings > SAVINGS_THRESHOLD);

    Node emptyFunctionA = nodeFactory.parse("function a() {}");
    Node emptyFunctionB = nodeFactory.parse("function b() {}");
    Node combinedCode = nodeFactory.parse(
        "function a() {} " +
        "function b() {} " +
        "function a() { return a(); } " +
        "function b() { return b(); }");

    FunctionRewriter functionRewriterWithThreshold = new FunctionRewriter(compiler);
    Node rootWithThreshold = new FunctionRewriter(compiler) {
      @Override
      public int estimatedSavings(Node node) {
        return super.estimatedSavings(node) + SAVINGS_THRESHOLD;
      }
    }.process(null, root);

    Node aGetterWithThreshold = findNodeByName(rootWithThreshold, "a");
    Node bGetterWithThreshold = findNodeByName(rootWithThreshold, "b");

    savings = functionRewriterWithThreshold.estimatedSavings(aGetterWithThreshold) +
        functionRewriterWithThreshold.estimatedSavings(bGetterWithThreshold);

    assertTrue(savings > 0);

    Node combinedCodeWithRewrittenFunctions = nodeFactory.parse(
        "function a() {} " +
        "function b() {} " +
        "function a() { return emptyFn(); } " +
        "function b() { return emptyFn(); } " +
        "function emptyFn() {}");

    Node rootWithRewrittenFunctions = functionRewriterWithThreshold.process(null, combinedCode).getChild(0);

    Node aGetterWithRewrittenFunctions = findNodeByName(rootWithRewrittenFunctions, "a");
    Node bGetterWithRewrittenFunctions = findNodeByName(rootWithRewrittenFunctions, "b");

    savings = functionRewriterWithThreshold.estimatedSavings(aGetterWithRewrittenFunctions) +
        functionRewriterWithThreshold.estimatedSavings(bGetterWithRewrittenFunctions);

    assertTrue(savings > 0);
  }

  private Node findNodeByName(Node parent, String name) {
    for (int i = 0; i < parent.getChildCount(); i++) {
      Node node = parent.getChild(i);
      if (NodeUtil.isName(node) && node.getString().equals(name)) {
        return node;
      }
    }
    return null;
  }

  private Node findHelperCode(Node root, Class<? extends Reducer> reducerClass) {
    for (int i = 0; i < root.getChildCount(); i++) {
      Node node = root.getChild(i);
      if (NodeUtil.isFunctionDeclaration(node) &&
          reducerClass.isInstance(FunctionRewriter.getReducerFromClass(node))) {
        return node.getLastChild();
      }
    }
    return null;
  }

  private Node findRootAfterProcessing(Node root, CompilerPass pass) {
    Node newRoot = pass.process(null, root);
    return newRoot.getChildren().isEmpty() ? newRoot : newRoot.getChildren().get(0);
  }
}