import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceName;
import static com.google.javascript.jscomp.JSError.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class VarCheckTest {

  private Compiler compiler;
  private CompilerPassManager passManager;
  private CompilerOptions options;
  private NodeFactory nodeFactory;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    compiler = Mockito.mock(Compiler.class);
    passManager = Mockito.mock(CompilerPassManager.class);
    options = new CompilerOptions();
    nodeFactory = new NodeFactory();

    Mockito.when(compiler.getErrorLevel(Mockito.any(JSError.class)))
        .thenReturn(CheckLevel.ERROR);

    Mockito.when(compiler.newCompilerInput("test.js"))
        .thenReturn(new CompilerInput("test.js"));

    Mockito.when(compiler.newCompilerInput("externs.js"))
        .thenReturn(new CompilerInput("externs.js"));

    Mockito.when(compiler.newExternInput("{SyntheticVarsDecl}"))
        .thenReturn(new CompilerInput("{SyntheticVarsDecl}"));

    Mockito.when(compiler.getModuleGraph()).thenReturn(new JSModuleGraph());

    Mockito.when(compiler.newErrorReporter())
        .thenReturn(Mockito.mock(ErrorReporter.class));

    Mockito.when(compiler.getSourceName("test.js"))
        .thenReturn(new SourceName("test.js"));

    Mockito.when(compiler.getSourceName("externs.js"))
        .thenReturn(new SourceName("externs.js"));

    Mockito.when(compiler.newExternScope()).thenReturn(new Scope());

    Mockito.when(compiler.newGlobalScope()).thenReturn(new Scope());

    Mockito.when(compiler.newRootNode()).thenReturn(nodeFactory.createProgram());

    Mockito.when(compiler.newScope()).thenReturn(new Scope());

    Mockito.when(compiler.newRootNodeTraversal(compiler.getRootNode()))
        .thenReturn(new NodeTraversal(compiler, null, null, null, null));

    Mockito.when(compiler.getInput())
        .thenReturn(compiler.newCompilerInput("test.js"));

    Mockito.when(compiler.getInput("test.js"))
        .thenReturn(compiler.getInput());

    Mockito.when(compiler.getInput("externs.js"))
        .thenReturn(compiler.newCompilerInput("externs.js"));

    Mockito.when(compiler.getInput("{SyntheticVarsDecl}"))
        .thenReturn(compiler.newExternInput("{SyntheticVarsDecl}"));

    Mockito.when(compiler.newAstRoot(compiler.getInput()))
        .thenCallRealMethod();

    Mockito.when(compiler.newAstRoot(compiler.newCompilerInput("")))
        .thenReturn(nodeFactory.createProgram());

    Mockito.when(compiler.addPass(Mockito.any(CompilerPass.class)))
        .then(invocation -> passManager.addPass(invocation.getArgument(0)));

    Mockito.when(compiler.runPasses())
        .thenCallRealMethod();

    Mockito.when(passManager.runPasses())
        .thenCallRealMethod();

    Mockito.when(compiler.getOptions()).thenReturn(options);

    options.setWarningsAsErrors(true);
  }

  @Test
  public void testNoUndefinedVariables() {
    String input =
        "var a = 1;\n" +
        "var b = 2;\n" +
        "function foo() {\n" +
        "  var c = 3;\n" +
        "}\n";

    Node root = nodeFactory.parse(input);
    VarCheck varCheck = new VarCheck(compiler, false);

    NodeTraversal traversal = new NodeTraversal(compiler, null, root, null, null);
    varCheck.process(null, root);

    assertEquals(0, traversal.getDiagnostics().size());
  }

  @Test
  public void testUndefinedVariable() {
    String input = "var a = 1;\nvar b;";

    Node root = nodeFactory.parse(input);
    VarCheck varCheck = new VarCheck(compiler, false);

    NodeTraversal traversal = new NodeTraversal(compiler, null, root, null, null);
    varCheck.process(null, root);

    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(UNDEFINED_VAR_ERROR, traversal.getDiagnostics().get(0).getType());
    assertEquals("b is undefined", traversal.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testUndefinedVariableInFunction() {
    String input = "function foo() {\nvar b;";

    Node root = nodeFactory.parse(input);
    VarCheck varCheck = new VarCheck(compiler, false);

    NodeTraversal traversal = new NodeTraversal(compiler, null, root, null, null);
    varCheck.process(null, root);

    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(UNDEFINED_VAR_ERROR, traversal.getDiagnostics().get(0).getType());
    assertEquals("b is undefined", traversal.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testUndefinedVariableInExterns() {
    String input =
        "extern function foo() {\n" +
        "  var b; // should be reported as undefined\n" +
        "}" +
        "var a = 1;\n";

    Node root = nodeFactory.parse(input);
    VarCheck varCheck = new VarCheck(compiler, false);

    NodeTraversal traversal = new NodeTraversal(compiler, null, root, null, null);
    varCheck.process(null, root);

    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(UNDEFINED_EXTERN_VAR_ERROR, traversal.getDiagnostics().get(0).getType());
    assertEquals("b is not undefined in the externs.", traversal.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testModuleDependency() {
    String inputA = "// module a.js\nvar a = 1;\n";
    String inputB = "// module b.js\nvar b = require(\"a.js\").a;\n";

    Node rootA = nodeFactory.parse(inputA);
    Node rootB = nodeFactory.parse(inputB);

    CompilerInput inputA = compiler.newCompilerInput("a.js");
    CompilerInput inputB = compiler.newCompilerInput("b.js");

    JSModule moduleA = new JSModule("a.js");
    JSModule moduleB = new JSModule("b.js");

    JSModuleGraph moduleGraph = new JSModuleGraph();
    moduleGraph.addModule(moduleA);
    moduleGraph.addModule(moduleB);
    moduleGraph.addDependency(moduleB, moduleA);

    Mockito.when(compiler.getModuleGraph()).thenReturn(moduleGraph);

    VarCheck varCheck = new VarCheck(compiler, false);

    NodeTraversal traversalB = new NodeTraversal(compiler, null, rootB, null, null);
    varCheck.process(inputB, rootB);

    assertEquals(1, traversalB.getDiagnostics().size());
    assertEquals(VIOLATED_MODULE_DEP_ERROR, traversalB.getDiagnostics().get(0).getType());
    assertEquals("module b.js cannot reference a, defined in module a.js, since a loads after b", traversalB.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testModuleDependencyStrict() {
    String inputA = "// module a.js\nvar a = 1;\n";
    String inputB = "// module b.js\nvar b = require(\"a.js\").a;\n";

    Node rootA = nodeFactory.parse(inputA);
    Node rootB = nodeFactory.parse(inputB);

    CompilerInput inputA = compiler.newCompilerInput("a.js");
    CompilerInput inputB = compiler.newCompilerInput("b.js");

    JSModule moduleA = new JSModule("a.js");
    JSModule moduleB = new JSModule("b.js");

    JSModuleGraph moduleGraph = new JSModuleGraph();
    moduleGraph.addModule(moduleA);
    moduleGraph.addModule(moduleB);
    moduleGraph.addDependency(moduleB, moduleA);

    Mockito.when(compiler.getModuleGraph()).thenReturn(moduleGraph);
    Mockito.when(compiler.getErrorLevel(STRICT_MODULE_DEP_ERROR)).thenReturn(CheckLevel.DISABLED);

    VarCheck varCheck = new VarCheck(compiler, false);

    NodeTraversal traversalB = new NodeTraversal(compiler, null, rootB, null, null);
    varCheck.process(inputB, rootB);

    assertEquals(0, traversalB.getDiagnostics().size());
  }

  @Test
  public void testMissingModuleDependency() {
    String input = "// module a.js\nvar a = require(\"b.js\").b;\n";

    Node root = nodeFactory.parse(input);
    VarCheck varCheck = new VarCheck(compiler, false);

    NodeTraversal traversal = new NodeTraversal(compiler, null, root, null, null);
    varCheck.process(null, root);

    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(MISSING_MODULE_DEP_ERROR, traversal.getDiagnostics().get(0).getType());
    assertEquals("missing module dependency; module a.js should depend on module b.js because it references b", traversal.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testMissingModuleDependencyStrict() {
    String input = "// module a.js\nvar a = require(\"b.js\").b;\n";

    Node root = nodeFactory.parse(input);
    VarCheck varCheck = new VarCheck(compiler, false);

    Mockito.when(compiler.getErrorLevel(MISSING_MODULE_DEP_ERROR)).thenReturn(CheckLevel.WARNING);

    NodeTraversal traversal = new NodeTraversal(compiler, null, root, null, null);
    varCheck.process(null, root);

    assertEquals(0, traversal.getDiagnostics().size());
  }

  @Test
  public void testModuleDependencyWithGlobal() {
    String inputA = "// module a.js\nvar a = 1;\n";
    String inputB = "// module b.js\nvar b = require(\"a.js\").a;\n" +
        "var c = a;\n";

    Node rootA = nodeFactory.parse(inputA);
    Node rootB = nodeFactory.parse(inputB);

    CompilerInput inputA = compiler.newCompilerInput("a.js");
    CompilerInput inputB = compiler.newCompilerInput("b.js");

    JSModule moduleA = new JSModule("a.js");
    JSModule moduleB = new JSModule("b.js");

    JSModuleGraph moduleGraph = new JSModuleGraph();
    moduleGraph.addModule(moduleA);
    moduleGraph.addModule(moduleB);
    moduleGraph.addDependency(moduleB, moduleA);

    Mockito.when(compiler.getModuleGraph()).thenReturn(moduleGraph);

    VarCheck varCheck = new VarCheck(compiler, false);

    NodeTraversal traversalB = new NodeTraversal(compiler, null, rootB, null, null);
    varCheck.process(inputB, rootB);

    assertEquals(0, traversalB.getDiagnostics().size());
  }

  @Test
  public void testFunctionDeclaration() {
    String input = "var a = function foo() {};";

    Node root = nodeFactory.parse(input);
    VarCheck varCheck = new VarCheck(compiler, false);

    NodeTraversal traversal = new NodeTraversal(compiler, null, root, null, null);
    varCheck.process(null, root);

    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(INVALID_FUNCTION_DECL, traversal.getDiagnostics().get(0).getType());
    assertEquals("Syntax error: function declaration must have a name", traversal.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testDuplicateDeclaration() {
    String input = "var a = 1;\nvar a = 2;\n";

    Node root = nodeFactory.parse(input);
    VarCheck varCheck = new VarCheck(compiler, false);

    NodeTraversal traversal = new NodeTraversal(compiler, null, root, null, null);
    varCheck.process(null, root);

    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(UNDEFINED_VAR_ERROR, traversal.getDiagnostics().get(0).getType());
    assertEquals("a is undefined", traversal.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testDuplicateDeclarationInFunction() {
    String input = "function foo() {\nvar a = 1;\nvar a = 2;\n}";

    Node root = nodeFactory.parse(input);
    VarCheck varCheck = new VarCheck(compiler, false);

    NodeTraversal traversal = new NodeTraversal(compiler, null, root, null, null);
    varCheck.process(null, root);

    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(UNDEFINED_VAR_ERROR, traversal.getDiagnostics().get(0).getType());
    assertEquals("a is undefined", traversal.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testDuplicateDeclarationInExterns() {
    String input =
        "extern function foo() {\n" +
        "  var a = 1;\n" +
        "  var a = 2;\n" +
        "}" +
        "var b = 1;\n";

    Node root = nodeFactory.parse(input);
    VarCheck varCheck = new VarCheck(compiler, false);

    NodeTraversal traversal = new NodeTraversal(compiler, null, root, null, null);
    varCheck.process(null, root);

    assertEquals(2, traversal.getDiagnostics().size());
    assertEquals(UNDEFINED_EXTERN_VAR_ERROR, traversal.getDiagnostics().get(1).getType());
    assertEquals("name a is not undefined in the externs.", traversal.getDiagnostics().get(1).getMessage());
  }
}