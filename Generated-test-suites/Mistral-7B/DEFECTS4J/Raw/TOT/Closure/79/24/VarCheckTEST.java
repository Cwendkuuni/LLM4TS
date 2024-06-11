import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VarCheckTest {

  private AbstractCompiler compiler;
  private CompilerPassManager passManager;
  private CompilerInput input;
  private Node root;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    passManager = new CompilerPassManager(compiler);
    input = NodeFactory.createCompilerInput("test.js", "test.js");
    root = NodeFactory.createRoot();
  }

  @Test
  public void testUndefinedVariable() {
    passManager.addPass(new VarCheck(compiler));
    passManager.process(input, root);

    Node varNode = NodeFactory.createName("undefinedVar");
    Node functionNode = NodeFactory.createFunctionDeclaration("test", null, null, null,
        null, null, Lists.newArrayList(varNode), null, null, null);
    root.addChild(functionNode);

    passManager.process(input, root);

    assertDiagnostics(input, UNDEFINED_VAR_ERROR, "undefinedVar");
  }

  @Test
  public void testGlobalVariable() {
    passManager.addPass(new VarCheck(compiler));
    passManager.process(input, root);

    Node globalVarNode = NodeFactory.createName("globalVar");
    root.addChild(globalVarNode);

    passManager.process(input, root);

    // No diagnostics expected.
    assertEquals(0, input.getDiagnostics().size());
  }

  @Test
  public void testFunctionVariable() {
    passManager.addPass(new VarCheck(compiler));
    passManager.process(input, root);

    Node functionNode = NodeFactory.createFunctionDeclaration("test", null, null, null,
        null, null, null, null, null, null);
    Node varNode = NodeFactory.createName("functionVar");
    functionNode.addChild(varNode);
    root.addChild(functionNode);

    passManager.process(input, root);

    // No diagnostics expected.
    assertEquals(0, input.getDiagnostics().size());
  }

  @Test
  public void testModuleDependency() {
    passManager.addPass(new VarCheck(compiler));
    passManager.process(input, root);

    JSModule moduleA = new JSModule("moduleA");
    JSModule moduleB = new JSModule("moduleB");
    compiler.addModule(moduleA);
    compiler.addModule(moduleB);

    CompilerInput inputA = NodeFactory.createCompilerInput("moduleA.js", "moduleA.js");
    CompilerInput inputB = NodeFactory.createCompilerInput("moduleB.js", "moduleB.js");

    inputA.setModule(moduleA);
    inputB.setModule(moduleB);

    input.setExtern(true);
    input.addDependencies(moduleB.getName());

    Node varNodeA = NodeFactory.createName("varA");
    Node varNodeB = NodeFactory.createName("varB");
    root.addChild(varNodeA);
    root.addChild(varNodeB);

    passManager.process(input, root);

    // No diagnostics expected.
    assertEquals(0, input.getDiagnostics().size());

    // Now swap the order of the modules in the input.
    input.setDependencies(Lists.newArrayList(moduleB.getName(), moduleA.getName()));

    passManager.process(input, root);

    // ViolatedModuleDepError expected.
    assertDiagnostics(input, VIOLATED_MODULE_DEP_ERROR, "moduleA", "moduleB", "varB");
  }

  @Test
  public void testMissingModuleDependency() {
    passManager.addPass(new VarCheck(compiler));
    passManager.process(input, root);

    JSModule moduleA = new JSModule("moduleA");
    JSModule moduleB = new JSModule("moduleB");
    compiler.addModule(moduleA);
    compiler.addModule(moduleB);

    CompilerInput inputA = NodeFactory.createCompilerInput("moduleA.js", "moduleA.js");
    CompilerInput inputB = NodeFactory.createCompilerInput("moduleB.js", "moduleB.js");

    inputA.setModule(moduleA);
    inputB.setModule(moduleB);

    input.setExtern(true);
    input.addDependencies(moduleB.getName());

    Node varNodeB = NodeFactory.createName("varB");
    root.addChild(varNodeB);

    passManager.process(input, root);

    // MissingModuleDepError expected.
    assertDiagnostics(input, MISSING_MODULE_DEP_ERROR, "moduleA", "moduleB", "varB");
  }

  @Test
  public void testStrictModuleDependency() {
    passManager.addPass(new VarCheck(compiler, true));
    passManager.process(input, root);

    JSModule moduleA = new JSModule("moduleA");
    JSModule moduleB = new JSModule("moduleB");
    compiler.addModule(moduleA);
    compiler.addModule(moduleB);

    CompilerInput inputA = NodeFactory.createCompilerInput("moduleA.js", "moduleA.js");
    CompilerInput inputB = NodeFactory.createCompilerInput("moduleB.js", "moduleB.js");

    inputA.setModule(moduleA);
    inputB.setModule(moduleB);

    input.setExtern(true);
    input.addDependencies(moduleB.getName());

    Node varNodeB = NodeFactory.createName("varB");
    root.addChild(varNodeB);

    passManager.process(input, root);

    // StrictModuleDepError expected.
    assertDiagnostics(input, STRICT_MODULE_DEP_ERROR, "moduleA", "moduleB", "varB");
  }

  @Test
  public void testNameReferenceInExterns() {
    passManager.addPass(new VarCheck(compiler));
    passManager.process(input, root);

    Node externsNode = NodeFactory.createRoot();
    Node nameNode = NodeFactory.createName("nameRef");
    externsNode.addChild(nameNode);

    Node functionNode = NodeFactory.createFunctionDeclaration("test", null, null, null,
        null, null, null, null, null, null);
    root.addChild(functionNode);

    externsNode.addChild(root);

    passManager.process(input, root);

    // NameReferenceInExternsWarning expected.
    assertDiagnostics(input, NAME_REFERENCE_IN_EXTERNS_ERROR, "nameRef");
  }

  @Test
  public void testInvalidFunctionDeclaration() {
    passManager.addPass(new VarCheck(compiler));
    passManager.process(input, root);

    Node functionNode = NodeFactory.createRoot();
    root.addChild(functionNode);

    passManager.process(input, root);

    // InvalidFunctionDecl error expected.
    assertDiagnostics(input, INVALID_FUNCTION_DECL, "");
  }

  @After
  public void tearDown() {
    compiler = null;
    passManager = null;
    input = null;
    root = null;
  }

  private void assertDiagnostics(CompilerInput input, DiagnosticType expectedType,
                                 String expectedMessage, Object... args) {
    DiagnosticType actualType = input.getDiagnostics().get(0).getType();
    assertEquals(expectedType, actualType);

    String actualMessage = input.getDiagnostics().get(0).getMessage();
    String formattedMessage = String.format(expectedMessage, args);
    assertEquals(formattedMessage, actualMessage);
  }

  private void assertDiagnostics(CompilerInput input, DiagnosticType expectedType,
                                 String expectedMessage, String expectedModule1,
                                 String expectedModule2, String expectedVar) {
    DiagnosticType actualType = input.getDiagnostics().get(0).getType();
    assertEquals(expectedType, actualType);

    String actualMessage = input.getDiagnostics().get(0).getMessage();
    String formattedMessage = String.format(expectedMessage,
        expectedModule1, expectedModule2, expectedVar);
    assertEquals(formattedMessage, actualMessage);
  }

  private enum DiagnosticType {
    UNDEFINED_VAR_ERROR("JSC_UNDEFINED_VARIABLE",
        "variable {0} is undefined"),
    VIOLATED_MODULE_DEP_ERROR("JSC_VIOLATED_MODULE_DEPENDENCY",
        "module {0} cannot reference {2}, defined in " +
        "module {1}, since {1} loads after {0}"),
    MISSING_MODULE_DEP_ERROR("JSC_MISSING_MODULE_DEPENDENCY",
        "missing module dependency; module {0} should depend " +
        "on module {1} because it references {2}"),
    STRICT_MODULE_DEPENDENCY_ERROR("JSC_STRICT_MODULE_DEPENDENCY",
        "module {0} cannot reference {2}, defined in " +
        "module {1}"),
    NAME_REFERENCE_IN_EXTERNS("JSC_NAME_REFERENCE_IN_EXTERNS",
        "accessing name {0} in externs has no effect"),
    UNDEFINED_EXTERN_VAR_ERROR("JSC_UNDEFINED_EXTERN_VAR_ERROR",
        "name {0} is not undefined in the externs."),
    INVALID_FUNCTION_DECL("JSC_INVALID_FUNCTION_DECL",
        "Syntax error: function declaration must have a name"),
    DISABLED("JSC_STRICT_MODULE_DEPENDENCY", "");

    private final String type;
    private final String message;

    DiagnosticType(String type, String message) {
      this.type = type;
      this.message = message;
    }

    DiagnosticType(String type) {
      this.type = type;
      this.message = "";
    }

    public String getType() {
      return type;
    }

    public String getMessage() {
      return message;
    }
  }
}