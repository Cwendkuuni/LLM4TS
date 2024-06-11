import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;

public class VarCheckTest {

  private VarCheck varCheck;

  @Before
  public void setUp() {
    varCheck = new VarCheck();
  }

  @Test
  public void testProcess_withUndefinedVarError() {
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    Node varNode = new Node(Token.VAR, new Node(Token.NAME, "x"));
    Node exprStmtNode = new Node(Token.EXPR_RESULT, new Node(Token.NAME, "y"));
    Node scriptNode = new Node(Token.SCRIPT, varNode, exprStmtNode);
    root.addChildToBack(scriptNode);

    Compiler compiler = new Compiler();
    compiler.initOptions(new CompilerOptions());
    varCheck.process(externs, root);

    JSError[] errors = compiler.getErrors();
    assertEquals(1, errors.length);
    assertEquals(VarCheck.UNDEFINED_VAR_ERROR, errors[0].getType());
    assertEquals("y", errors[0].getDescription());
  }

  @Test
  public void testProcess_withViolatedModuleDepError() {
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    Node varNode1 = new Node(Token.VAR, new Node(Token.NAME, "x1"));
    Node varNode2 = new Node(Token.VAR, new Node(Token.NAME, "x2"));
    Node varNode3 = new Node(Token.VAR, new Node(Token.NAME, "x3"));
    Node scriptNode1 = new Node(Token.SCRIPT, varNode1, varNode2);
    Node scriptNode2 = new Node(Token.SCRIPT, varNode3);
    root.addChildToBack(scriptNode1);
    root.addChildToBack(scriptNode2);

    Compiler compiler = new Compiler();
    JSModuleGraph moduleGraph = new JSModuleGraph();
    JSModule module1 = new JSModule("module1");
    JSModule module2 = new JSModule("module2");
    moduleGraph.addAllEdges(module1, Lists.newArrayList(module2));
    compiler.setModuleGraph(moduleGraph);
    compiler.initOptions(new CompilerOptions());
    varCheck.process(externs, root);

    JSError[] errors = compiler.getErrors();
    assertEquals(1, errors.length);
    assertEquals(VarCheck.VIOLATED_MODULE_DEP_ERROR, errors[0].getType());
    assertEquals("module2", errors[0].getDescription());
  }

  @Test
  public void testProcess_withMissingModuleDepError() {
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    Node varNode1 = new Node(Token.VAR, new Node(Token.NAME, "x1"));
    Node varNode2 = new Node(Token.VAR, new Node(Token.NAME, "x2"));
    Node varNode3 = new Node(Token.VAR, new Node(Token.NAME, "x3"));
    Node scriptNode1 = new Node(Token.SCRIPT, varNode1, varNode2);
    Node scriptNode2 = new Node(Token.SCRIPT, varNode3);
    root.addChildToBack(scriptNode1);
    root.addChildToBack(scriptNode2);

    Compiler compiler = new Compiler();
    JSModuleGraph moduleGraph = new JSModuleGraph();
    JSModule module1 = new JSModule("module1");
    JSModule module2 = new JSModule("module2");
    compiler.setModuleGraph(moduleGraph);
    compiler.initOptions(new CompilerOptions());
    varCheck.process(externs, root);

    JSError[] warnings = compiler.getWarnings();
    assertEquals(1, warnings.length);
    assertEquals(VarCheck.MISSING_MODULE_DEP_ERROR, warnings[0].getType());
    assertEquals("module1", warnings[0].getDescription());
  }

  @Test
  public void testProcess_withStrictModuleDepError() {
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    Node varNode1 = new Node(Token.VAR, new Node(Token.NAME, "x1"));
    Node varNode2 = new Node(Token.VAR, new Node(Token.NAME, "x2"));
    Node varNode3 = new Node(Token.VAR, new Node(Token.NAME, "x3"));
    Node scriptNode1 = new Node(Token.SCRIPT, varNode1, varNode2);
    Node scriptNode2 = new Node(Token.SCRIPT, varNode3);
    root.addChildToBack(scriptNode1);
    root.addChildToBack(scriptNode2);

    Compiler compiler = new Compiler();
    JSModuleGraph moduleGraph = new JSModuleGraph();
    JSModule module1 = new JSModule("module1");
    JSModule module2 = new JSModule("module2");
    compiler.setModuleGraph(moduleGraph);
    compiler.initOptions(new CompilerOptions());
    varCheck.process(externs, root);

    JSError[] disabled = compiler.getDisabledWarnings();
    assertEquals(1, disabled.length);
    assertEquals(VarCheck.STRICT_MODULE_DEP_ERROR, disabled[0].getType());
    assertEquals("module2", disabled[0].getDescription());
  }

  @Test
  public void testProcess_withNameReferenceInExternsError() {
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    Node varNode1 = new Node(Token.VAR, new Node(Token.NAME, "foo"));
    Node varNode2 = new Node(Token.VAR, new Node(Token.NAME, "bar"));
    Node scriptNode = new Node(Token.SCRIPT, varNode1, varNode2);
    root.addChildToBack(scriptNode);

    Compiler compiler = new Compiler();
    compiler.initOptions(new CompilerOptions());
    varCheck.process(externs, root);

    JSError[] warnings = compiler.getWarnings();
    assertEquals(1, warnings.length);
    assertEquals(VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR, warnings[0].getType());
    assertEquals("foo", warnings[0].getDescription());
  }

  @Test
  public void testProcess_withUndefinedExternVarError() {
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    Node varNode = new Node(Token.VAR, new Node(Token.GETPROP,
        new Node(Token.NAME, "window"), new Node(Token.STRING, "foo")));
    Node scriptNode = new Node(Token.SCRIPT, varNode);
    root.addChildToBack(scriptNode);

    Compiler compiler = new Compiler();
    compiler.initOptions(new CompilerOptions());
    varCheck.strictExternCheck = true;
    varCheck.process(externs, root);

    JSError[] warnings = compiler.getWarnings();
    assertEquals(1, warnings.length);
    assertEquals(VarCheck.UNDEFINED_EXTERN_VAR_ERROR, warnings[0].getType());
    assertEquals("foo", warnings[0].getDescription());
  }

  @Test
  public void testProcess_withInvalidFunctionDeclarationError() {
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    Node varNode = new Node(Token.VAR, new Node(Token.FUNCTION));
    Node scriptNode = new Node(Token.SCRIPT, varNode);
    root.addChildToBack(scriptNode);

    Compiler compiler = new Compiler();
    compiler.initOptions(new CompilerOptions());
    varCheck.process(externs, root);

    JSError[] errors = compiler.getErrors();
    assertEquals(1, errors.length);
    assertEquals(VarCheck.INVALID_FUNCTION_DECL, errors[0].getType());
  }

  @Test(expected = IllegalStateException.class)
  public void testVisit_withUnexpectedVariable() {
    NodeTraversal t = new NodeTraversal(compiler, null);
    Node n = new Node(Token.NAME, "foo");
    Node parent = new Node(Token.SCRIPT);

    varCheck.visit(t, n, parent);
  }
}