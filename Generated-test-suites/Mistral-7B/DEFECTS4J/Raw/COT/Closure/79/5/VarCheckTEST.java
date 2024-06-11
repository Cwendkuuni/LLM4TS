import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static com.google.common.collect.Sets.newHashSet;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createVariable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TestingCompiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.tree.Tree;

@RunWith(MockitoJUnitRunner.class)
public class VarCheckTest {

  @Mock
  private Compiler compiler;

  @Mock
  private NodeTraversal nodeTraversal;

  @Mock
  private Diagnostics diagnostics;

  private VarCheck varCheck;
  private TestingCompiler compilerUnderTest;
  private CompilerInput input;
  private CompilerInput externsInput;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    CompilerOptions options = new CompilerOptions();
    compilerUnderTest = new TestingCompiler(options);
    compilerUnderTest.setDiagnostics(diagnostics);

    varCheck = new VarCheck(compilerUnderTest);
    input = new CompilerInput("test.js", "");
    externsInput = new CompilerInput("externs.js", "");
  }

  @Test
  public void testCheckEmptyFile() {
    Node root = Node.nullLiteral();
    traverse(compilerUnderTest, root, varCheck);
    assertFalse(diagnostics.hasErrors());
    assertFalse(diagnostics.hasWarnings());
  }

  @Test
  public void testCheckDeclaredVariable() {
    String varName = "testVar";
    Node root = createName(Token.NAME, varName);
    Scope scope = new Scope();
    scope.declare(varName, root, null, null);
    CompilerInput input = new CompilerInput("test.js", "", scope);

    traverse(compilerUnderTest, input.getAstRoot(compilerUnderTest), varCheck);
    assertFalse(diagnostics.hasErrors());
    assertFalse(diagnostics.hasWarnings());
  }

  @Test
  public void testCheckUndefinedVariable() {
    String varName = "testVar";
    Node root = createName(Token.NAME, varName);
    CompilerInput input = new CompilerInput("test.js", "", null);

    traverse(compilerUnderTest, input.getAstRoot(compilerUnderTest), varCheck);
    assertFalse(diagnostics.hasErrors());
    assertFalse(diagnostics.hasWarnings());

    Scope scope = new Scope();
    DiagnosticType diagnosticType = UNDEFINED_VAR_ERROR;
    assertDiagnostic(diagnosticType, root, varCheck, diagnosticType.getMessage(varName));
  }

  @Test
  public void testCheckUndefinedVariableInFunction() {
    String varName = "testVar";
    Node function = createName(Token.FUNCTION, "");
    Node body = Node.nullLiteral();
    Node root = new Node(Token.LIST, Lists.newArrayList(function));
    function.addChildToBack(new Node(Token.VAR, varName));
    function.addChildToBack(createName(Token.LP, ""));
    function.addChildToBack(body);
    function.addChildToBack(new Node(Token.RP, ""));

    Scope scope = new Scope();
    CompilerInput input = new CompilerInput("test.js", "", scope);
    traverse(compilerUnderTest, input.getAstRoot(compilerUnderTest), varCheck);

    assertFalse(diagnostics.hasErrors());
    assertFalse(diagnostics.hasWarnings());

    assertDiagnostic(UNDEFINED_VAR_ERROR, root.getFirstChild(), varCheck,
        diagnosticType.getMessage(varName));
  }

  @Test
  public void testCheckUndefinedVariableInExterns() {
    String varName = "testVar";
    Node root = createName(Token.NAME, varName);

    Node externs = createName(Token.NAME, "externs");
    Node externsRoot = createVariable(Token.VAR, varName, Node.UNDEFINED, null, null);
    externs.addChildToBack(externsRoot);

    CompilerInput input = new CompilerInput("test.js", "", null);
    CompilerInput externsInput = new CompilerInput("externs.js", "", null);
    externsInput.addAstRoot(externsRoot);

    traverse(compilerUnderTest, input.getAstRoot(compilerUnderTest), varCheck);

    assertFalse(diagnostics.hasErrors());
    assertFalse(diagnostics.hasWarnings());

    assertDiagnostic(UNDEFINED_EXTERN_VAR_ERROR, root, varCheck,
        diagnosticType.getMessage(varName));
  }

  @Test
  public void testCheckMissingModuleDependency() {
    String depName = "depModule";
    String varName = "testVar";

    Node depModule = createName(Token.NAME, depName);
    Node root = createName(Token.NAME, varName);

    CompilerInput depInput = new CompilerInput("depModule.js", "", null);
    CompilerInput input = new CompilerInput("test.js", "", null);

    CompilerInput depExternsInput = new CompilerInput("depExterns.js", "", null);
    CompilerInput externsInput = new CompilerInput("externs.js", "", null);

    Node depExternsRoot = createVariable(Token.VAR, depName, Node.UNDEFINED, null, null);
    depExternsInput.addAstRoot(depExternsRoot);

    Node externsRoot = createVariable(Token.VAR, "SyntheticVarsDeclar", Node.UNDEFINED, null, null);
    externsInput.addAstRoot(externsRoot);

    depModule.addChildToBack(depExternsRoot);
    root.addChildToBack(createName(Token.GETPROP, ""));
    root.getLastChild().addChildToBack(root);

    traversals(compilerUnderTest, depInput.getAstRoot(compilerUnderTest), nodeTraversal, varCheck);
    traversals(compilerUnderTest, input.getAstRoot(compilerUnderTest), nodeTraversal, varCheck);

    assertFalse(diagnostics.hasErrors());
    assertFalse(diagnostics.hasWarnings());

    assertDiagnostic(MISSING_MODULE_DEP_ERROR, root, varCheck,
        diagnosticType.getMessage(depName, "test.js", varName));
  }

  @Test
  public void testCheckModuleDependencyInWrongOrder() {
    String depName = "depModule";
    String varName = "testVar";

    Node depModule = createName(Token.NAME, depName);
    Node root = createName(Token.NAME, varName);

    CompilerInput depInput = new CompilerInput("depModule.js", "", null);
    CompilerInput input = new CompilerInput("test.js", "", null);

    CompilerInput depExternsInput = new CompilerInput("depExterns.js", "", null);
    CompilerInput externsInput = new CompilerInput("externs.js", "", null);

    Node depExternsRoot = createVariable(Token.VAR, depName, Node.UNDEFINED, null, null);
    depExternsInput.addAstRoot(depExternsRoot);

    Node externsRoot = createVariable(Token.VAR, "SyntheticVarsDeclar", Node.UNDEFINED, null, null);
    externsInput.addAstRoot(externsRoot);

    depModule.addChildToBack(depExternsRoot);
    root.addChildToBack(createName(Token.GETPROP, ""));
    root.getLastChild().addChildToBack(root);

    traversals(compilerUnderTest, input.getAstRoot(compilerUnderTest), nodeTraversal, varCheck);
    traversals(compilerUnderTest, depInput.getAstRoot(compilerUnderTest), nodeTraversal, varCheck);

    assertFalse(diagnostics.hasErrors());
    assertFalse(diagnostics.hasWarnings());

    assertDiagnostic(MISSING_MODULE_DEP_ERROR, root, varCheck,
        diagnosticType.getMessage(depName, "test.js", varName));
  }

  @Test
  public void testCheckStrictModuleDependency() {
    String depName = "depModule";
    String varName = "testVar";

    Node depModule = createName(Token.NAME, depName);
    Node root = createName(Token.NAME, varName);

    CompilerInput depInput = new CompilerInput("depModule.js", "", null);
    CompilerInput input = new CompilerInput("test.js", "", null);

    Node depExternsRoot = createVariable(Token.VAR, depName, Node.UNDEFINED, null, null);
    depExternsInput = new CompilerInput("depExterns.js", "", null);
    depExternsInput.addAstRoot(depExternsRoot);

    Node externsRoot = createVariable(Token.VAR, "SyntheticVarsDeclar", Node.UNDEFINED, null, null);
    externsInput = new CompilerInput("externs.js", "", null);
    externsInput.addAstRoot(externsRoot);

    depModule.addChildToBack(depExternsRoot);
    root.addChildToBack(createName(Token.GETPROP, ""));
    root.getLastChild().addChildToBack(root);

    varCheck = new VarCheck(compilerUnderTest, true);
    traversals(compilerUnderTest, depInput.getAstRoot(compilerUnderTest), nodeTraversal, varCheck);
    traversals(compilerUnderTest, input.getAstRoot(compilerUnderTest), nodeTraversal, varCheck);

    assertFalse(diagnostics.hasErrors());
    assertFalse(diagnostics.hasWarnings());

    assertDiagnostic(STRICT_MODULE_DEP_ERROR, root, varCheck,
        diagnosticType.getMessage(depName, "test.js", varName));
  }

  @Test
  public void testCheckDuplicateVariableDeclaration() {
    String varName = "testVar";

    Node root = createVariable(Token.VAR, varName, Node.UNDEFINED, null, null);
    root.addChildToBack(root);

    CompilerInput input = new CompilerInput("test.js", "", null);
    traversals(compilerUnderTest, input.getAstRoot(compilerUnderTest), nodeTraversal, varCheck);

    assertFalse(diagnostics.hasErrors());
    assertFalse(diagnostics.hasWarnings());

    assertNull(compilerUnderTest.getErrorList().getErrorCount());
  }

  @Test
  public void testCheckDuplicateVariableDeclarationInFunction() {
    String varName = "testVar";

    Node function = createName(Token.FUNCTION, "");
    Node body = Node.nullLiteral();
    Node root = new Node(Token.LIST, Lists.newArrayList(function));
    Node varDeclaration = createVariable(Token.VAR, varName, Node.UNDEFINED, null, null);
    Node secondVarDeclaration = createVariable(Token.VAR, varName, Node.UNDEFINED, null, null);
    function.addChildToBack(new Node(Token.VAR, "")); // function name
    function.addChildToBack(createName(Token.LP, ""));
    function.addChildToBack(body);
    function.addChildToBack(new Node(Token.RP, ""));
    function.addChildToBack(varDeclaration);
    function.addChildToBack(secondVarDeclaration);

    CompilerInput input = new CompilerInput("test.js", "", null);
    traversals(compilerUnderTest, input.getAstRoot(compilerUnderTest), nodeTraversal, varCheck);

    assertFalse(diagnostics.hasErrors());
    assertFalse(diagnostics.hasWarnings());

    assertDiagnostic(INVALID_FUNCTION_DECL, function, varCheck,
        "Syntax error: function declaration must have a name");
  }

  private void traversals(Compiler compiler, Node node, NodeTraversal nodeTraversal,
      VarCheck varCheck) {
    compiler.init();
    compiler.process(input, externsInput);
    compiler.addPass(varCheck);
    traverse(compiler, node, nodeTraversal);
  }

  private void assertDiagnostic(DiagnosticType diagnosticType, Node node, VarCheck varCheck,
      String expectedMessage) {
    Diagnostic diagnostic = diagnostics.getFirst(diagnosticType);
    assertNotNull(diagnostic);
    assertEquals(expectedMessage, diagnostic.getMessage(node));
  }
}