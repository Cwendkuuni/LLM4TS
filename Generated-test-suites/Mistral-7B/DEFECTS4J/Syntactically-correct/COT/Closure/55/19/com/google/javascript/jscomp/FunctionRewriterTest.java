import static org.junit.Assert.*;
import static com.google.javascript.jscomp.FunctionRewriter.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class FunctionRewriterTest {
  private Compiler compiler;
  private CompilerPassManager passManager;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    nodeFactory = new NodeFactory();
    compiler = new Compiler();
    passManager = new CompilerPassManager(compiler);
  }

  @Test
  public void testProcess_NoReduction() {
    Node root = nodeFactory.parse("function a() {}");
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    passManager.runPasses(ImmutableList.of(functionRewriter), root);
    assertEquals("function a() {}", compiler.toSource(root));
  }

  @Test
  public void testProcess_IdentityFunction() {
    Node root = nodeFactory.parse("function a(x) {return x}");
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    passManager.runPasses(ImmutableList.of(functionRewriter), root);

    String expectedSource = "function a(x)" +
        "{" +
        "  return JSCompiler_identityFn(x)" +
        "}";
    assertEquals(expectedSource, compiler.toSource(root));
  }

  @Test
  public void testProcess_EmptyFunction() {
    Node root = nodeFactory.parse("function a() {}");
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    passManager.runPasses(ImmutableList.of(functionRewriter), root);

    String expectedSource = "function a() {}" +
        "function JSCompiler_emptyFn() {" +
        "  return function() {}" +
        "}" +
        "JSCompiler_emptyFn = JSCompiler_emptyFn || JSCompiler_emptyFn;";
    assertEquals(expectedSource, compiler.toSource(root));
  }

  @Test(expected = IllegalStateException.class)
  public void testProcess_InvalidFunction() {
    Node root = nodeFactory.parse("function a() {2 + 2}");
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    passManager.runPasses(ImmutableList.of(functionRewriter), root);
  }

  @Test
  public void testProcess_MultipleFunctions() {
    Node root = nodeFactory.parse("" +
        "function a() {}" +
        "function b() {}" +
        "function c() {}");
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    passManager.runPasses(ImmutableList.of(functionRewriter), root);

    String expectedSource = "function a() {}" +
        "function b() {}" +
        "function c() {}" +
        "function JSCompiler_emptyFn() {" +
        "  return function() {}" +
        "}" +
        "JSCompiler_emptyFn = JSCompiler_emptyFn || JSCompiler_emptyFn;";
    assertEquals(expectedSource, compiler.toSource(root));
  }

  @Test
  public void testParseHelperCode_EmptyFunction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node helperCode = functionRewriter.parseHelperCode(EmptyFunctionReducer.class);
    assertNotNull(helperCode);
  }

  @Test
  public void testParseHelperCode_IdentityFunction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node helperCode = functionRewriter.parseHelperCode(IdentityReducer.class);
    assertNotNull(helperCode);
  }
}