import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.isFunction;
import static com.google.javascript.jscomp.NodeUtil.isFunctionDeclaration;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import java.util.Set;

public class VarCheckTest {

  private static class TestCompiler extends AbstractCompiler {

    public TestCompiler() {
      super(null, null, null, null, null, null, null, null, null);
    }

    @Override
    public Diagnostics check(CompilerInput input) throws CompilerException {
      return new Diagnostics();
    }
  }

  @org.junit.Before
  public void setUp() {
    compiler = new TestCompiler();
  }

  private AbstractCompiler compiler;

  @org.junit.Test
  public void testSimpleVar() {
    CompilerInput input = new CompilerInput("test.js", "test.js", null, null);
    Node root = new Node(Token.PROGRAM, null);
    Node decl = new Node(Token.VAR, new Node(Token.NAME, "x"));
    root.addChildToBack(decl);

    VarCheck varCheck = new VarCheck(compiler);
    varCheck.process(null, root);
    assertFalse("Variable should be declared", varCheck.varsToDeclareInExterns.isEmpty());
    assertEquals("Unexpected number of variables to declare", 1, varCheck.varsToDeclareInExterns.size());
    assertEquals("Unexpected variable name", "x", varCheck.varsToDeclareInExterns.iterator().next());
  }

  @org.junit.Test
  public void testFunctionVar() {
    CompilerInput input = new CompilerInput("test.js", "test.js", null, null);
    Node root = new Node(Token.PROGRAM, null);
    Node function = new Node(Token.FUNCTION, new Node(Token.NAME, "foo"), null, null, null, null);
    Node decl = new Node(Token.VAR, new Node(Token.NAME, "x"));
    Node body = new Node(Token.BLOCK, null);
    Node assign = new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.NUM, 0));
    body.addChildToBack(assign);
    function.addChildToBack(new Node(Token.VAR, new Node(Token.NAME, "x")));
    function.addChildToBack(new Node(Token.BLOCK, body));
    root.addChildToBack(new Node(Token.FUNCTION, new Node(Token.NAME, "foo"), null, null, null, null, null, function));

    VarCheck varCheck = new VarCheck(compiler, true);
    varCheck.process(null, root);
    assertFalse("Variable should be declared", varCheck.varsToDeclareInExterns.isEmpty());
    assertEquals("Unexpected number of variables to declare", 1, varCheck.varsToDeclareInExterns.size());
    assertEquals("Unexpected variable name", "x", varCheck.varsToDeclareInExterns.iterator().next());
  }

  @org.junit.Test
  public void testDuplicateVar() {
    CompilerInput input = new CompilerInput("test.js", "test.js", null, null);
    Node root = new Node(Token.PROGRAM, null);
    Node decl1 = new Node(Token.VAR, new Node(Token.NAME, "x"));
    Node decl2 = new Node(Token.VAR, new Node(Token.NAME, "x"));
    root.addChildToBack(decl1);
    root.addChildToBack(decl2);

    VarCheck varCheck = new VarCheck(compiler);
    varCheck.process(null, root);
    assertFalse("Variable should be declared", varCheck.varsToDeclareInExterns.isEmpty());
    assertEquals("Unexpected number of variables to declare", 1, varCheck.varsToDeclareInExterns.size());
    assertEquals("Unexpected variable name", "x", varCheck.varsToDeclareInExterns.iterator().next());
  }

  @org.junit.Test
  public void testUndefinedVar() {
    CompilerInput input = new CompilerInput("test.js", "test.js", null, null);
    Node root = new Node(Token.PROGRAM, null);
    Node decl = new Node(Token.VAR, new Node(Token.NAME, "x"));
    root.addChildToBack(decl);

    VarCheck varCheck = new VarCheck(compiler);
    varCheck.process(null, root);
    if (compiler.getErrorLevel(UNDEFINED_VAR_ERROR) == CheckLevel.ERROR) {
      // The test passes if an error is reported.
      return;
    }

    fail("An error should have been reported");
  }

  @org.junit.Test
  public void testFunctionWithEmptyName() {
    CompilerInput input = new CompilerInput("test.js", "test.js", null, null);
    Node root = new Node(Token.PROGRAM, null);
    Node function = new Node(Token.FUNCTION, null, null, null, null, null, null, null);
    root.addChildToBack(function);

    VarCheck varCheck = new VarCheck(compiler);
    varCheck.process(null, root);
    assertTrue("Function name should be empty", NodeUtil.isFunction(function));
    assertTrue("Function should have no name declaration", function.getFirstChild() == null);
    VarCheck.INVALID_FUNCTION_DECL.expect(1);
  }

  @org.junit.Test
  public void testUndefinedVarInFunction() {
    CompilerInput input = new CompilerInput("test.js", "test.js", null, null);
    Node root = new Node(Token.PROGRAM, null);
    Node function = new Node(Token.FUNCTION, new Node(Token.NAME, "foo"), null, null, null, null, null, null);
    Node decl = new Node(Token.VAR, new Node(Token.NAME, "x"));
    Node body = new Node(Token.BLOCK, null);
    Node assign = new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.NUM, 0));
    body.addChildToBack(assign);
    function.addChildToBack(new Node(Token.VAR, new Node(Token.NAME, "x")));
    function.addChildToBack(new Node(Token.BLOCK, body));
    root.addChildToBack(function);

    VarCheck varCheck = new VarCheck(compiler);
    varCheck.process(null, root);
    if (compiler.getErrorLevel(UNDEFINED_VAR_ERROR) == CheckLevel.ERROR) {
      // The test passes if an error is reported.
      return;
    }

    fail("An error should have been reported");
  }

  @org.junit.Test
  public void testModuleDependency() {
    CompilerInput inputA = new CompilerInput("moduleA.js", "moduleA.js", null, null);
    Node rootA = new Node(Token.PROGRAM, null);
    Node decl = new Node(Token.VAR, new Node(Token.NAME, "x"));
    rootA.addChildToBack(decl);

    CompilerInput inputB = new CompilerInput("moduleB.js", "moduleB.js", null, null);
    Node rootB = new Node(Token.PROGRAM, null);
    Node function = new Node(Token.FUNCTION, new Node(Token.NAME, "foo"), null, null, null, null, null, null);
    Node assign = new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.NUM, 0));
    Node body = new Node(Token.BLOCK, null);
    body.addChildToBack(assign);
    function.addChildToBack(new Node(Token.VAR, new Node(Token.NAME, "x")));
    function.addChildToBack(new Node(Token.BLOCK, body));
    rootB.addChildToBack(function);

    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(inputA.getAstRoot(compiler), inputB.getAstRoot(compiler)), varCheck);
    if (compiler.getErrorLevel(VarCheck.VIOLATED_MODULE_DEP_ERROR) == CheckLevel.ERROR) {
      // The test passes if an error is reported.
      return;
    }

    fail("An error should have been reported");
  }

  @org.junit.Test
  public void testModuleDependencyWarning() {
    CompilerInput inputA = new CompilerInput("moduleA.js", "moduleA.js", null, null);
    Node rootA = new Node(Token.PROGRAM, null);
    Node decl = new Node(Token.VAR, new Node(Token.NAME, "x"));
    rootA.addChildToBack(decl);

    CompilerInput inputB = new CompilerInput("moduleB.js", "moduleB.js", null, null);
    Node rootB = new Node(Token.PROGRAM, null);
    Node function = new Node(Token.FUNCTION, new Node(Token.NAME, "foo"), null, null, null, null, null, null);
    Node assign = new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.NUM, 0));
    Node body = new Node(Token.BLOCK, null);
    body.addChildToBack(assign);
    function.addChildToBack(new Node(Token.VAR, new Node(Token.NAME, "x")));
    function.addChildToBack(new Node(Token.BLOCK, body));
    rootB.addChildToBack(function);

    VarCheck varCheck = new VarCheck(compiler, true);
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(inputA.getAstRoot(compiler), inputB.getAstRoot(compiler)), varCheck);
    // No error should be reported

    Scope scopeA = ((Scope) rootA.getFirstChild()).getGlobalScope();
    Scope scopeB = ((Scope) rootB.getFirstChild()).getGlobalScope();
    assertEquals("Incorrect number of dependencies", 1, scopeB.getDepMap().size());
    assertEquals("Unexpected dependency", "moduleA.js", scopeB.getDepMap().keySet().iterator().next());
  }

  @org.junit.Test
  public void testMissingModuleDependency() {
    CompilerInput inputA = new CompilerInput("moduleA.js", "moduleA.js", null, null);
    Node rootA = new Node(Token.PROGRAM, null);
    Node decl = new Node(Token.VAR, new Node(Token.NAME, "x"));
    rootA.addChildToBack(decl);

    CompilerInput inputB = new CompilerInput("moduleB.js", "moduleB.js", null, null);
    Node rootB = new Node(Token.PROGRAM, null);
    Node function = new Node(Token.FUNCTION, new Node(Token.NAME, "foo"), null, null, null, null, null, null);
    Node assign = new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.NUM, 0));
    Node body = new Node(Token.BLOCK, null);
    body.addChildToBack(assign);
    function.addChildToBack(new Node(Token.VAR, new Node(Token.NAME, "x")));
    function.addChildToBack(new Node(Token.BLOCK, body));
    rootB.addChildToBack(function);

    VarCheck varCheck = new VarCheck(compiler);
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(inputB.getAstRoot(compiler), rootA), varCheck);
    if (compiler.getErrorLevel(VarCheck.MISSING_MODULE_DEP_ERROR) == CheckLevel.WARNING) {
      // The test passes if a warning is reported.
      return;
    }

    fail("A warning should have been reported");
  }

  @org.junit.Test
  public void testStrictModuleDependency() {
    CompilerInput inputA = new CompilerInput("moduleA.js", "moduleA.js", null, null);
    Node rootA = new Node(Token.PROGRAM, null);
    Node decl = new Node(Token.VAR, new Node(Token.NAME, "x"));
    rootA.addChildToBack(decl);

    CompilerInput inputB = new CompilerInput("moduleB.js", "moduleB.js", null, null);
    Node rootB = new Node(Token.PROGRAM, null);
    Node function = new Node(Token.FUNCTION, new Node(Token.NAME, "foo"), null, null, null, null, null, null);
    Node assign = new Node(Token.ASSIGN, null, new Node(Token.NAME, "x"), new Node(Token.NUM, 0));
    Node body = new Node(Token.BLOCK, null);
    body.addChildToBack(assign);
    function.addChildToBack(new Node(Token.VAR, new Node(Token.NAME, "x")));
    function.addChildToBack(new Node(Token.BLOCK, body));
    rootB.addChildToBack(function);

    VarCheck varCheck = new VarCheck(compiler, true, true);
    NodeTraversal.traverseRoots(compiler, Lists.newArrayList(inputB.getAstRoot(compiler), rootA), varCheck);
    // No error should be reported

    Scope scopeB = ((Scope) rootB.getFirstChild()).getGlobalScope();
    assertFalse("Unexpected dependency", scopeB.getDepMap().containsKey("moduleA.js"));
  }

  @org.junit.Test
  public void testNameReferenceInExterns() {
    CompilerInput input = new CompilerInput("test.js", "test.js", null, null);
    Node root = new Node(Token.PROGRAM, null);
    Node decl = new Node(Token.VAR, new Node(Token.NAME, "x"));
    root.addChildToBack(decl);

    VarCheck varCheck = new VarCheck(compiler);
    varCheck.process(input, root);
    if (compiler.getErrorLevel(VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR) == CheckLevel.WARNING) {
      // The test passes if a warning is reported.
      return;
    }

    fail("A warning should have been reported");
  }

  @org.junit.Test
  public void testUndefinedExternVar() {
    CompilerInput input = new CompilerInput("test.js", "test.js", null, null);
    Node root = new Node(Token.PROGRAM, null);
    Node decl = new Node(Token.VAR, new Node(Token.NAME, "x"));
    root.addChildToBack(decl);

    VarCheck varCheck = new VarCheck(compiler);
    varCheck.process(input, root);
    if (compiler.getErrorLevel(VarCheck.UNDEFINED_EXTERN_VAR_ERROR) == CheckLevel.WARNING) {
      // The test passes if a warning is reported.
      return;
    }

    fail("A warning should have been reported");
  }

  @org.junit.Test
  public void testInvalidFunctionDeclaration() {
    CompilerInput input = new CompilerInput("test.js", "test.js", null, null);
    Node root = new Node(Token.PROGRAM, null);
    Node function = new Node(Token.FUNCTION, null, null, null, null, null, null, null);
    root.addChildToBack(function);

    VarCheck varCheck = new VarCheck(compiler);
    varCheck.process(input, root);
    VarCheck.INVALID_FUNCTION_DECL.expect(1);
  }
}