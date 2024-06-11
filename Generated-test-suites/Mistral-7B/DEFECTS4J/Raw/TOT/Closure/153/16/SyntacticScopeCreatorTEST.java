import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RedeclarationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Scope.ScopeType;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.jscomp.test_utils.CompileTestRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SyntacticScopeCreatorTest {

  private static final String SOURCE =
      "function foo() {" +
      "  var x = 1; " +
      "  var y = 1; " +
      "  var z = 1; " +
      "  function bar() {} " +
      "  var x = 2; " +
      "  function baz() {} " +
      "  try {" +
      "    catch (e) {} " +
      "  } " +
      "  var x = 3; " +
      "  var arguments = 1; " +
      "  }";

  @Test
  public void testSyntaxScopeCreation() throws IOException {
    AbstractCompiler compiler = new TestCompiler();
    RedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler();
    ScopeCreator creator = new SyntacticScopeCreator(compiler, redeclarationHandler);

    Scope scope = creator.createScope(createNodeFromString(SOURCE), null);

    List<Scope.Var> vars = new ArrayList<>();
    vars.add(scope.getVar("x"));
    vars.add(scope.getVar("y"));
    vars.add(scope.getVar("z"));
    vars.add(scope.getVar("arguments"));

    assertEquals(ScopeType.LOCAL, scope.getType());
    assertEquals(4, scope.getVariables().size());
    assertEquals("foo", scope.getName());
    assertEquals(ImmutableList.copyOf(vars), scope.getVariables());
  }

  @Test
  public void testGlobalScopeCreation() throws IOException {
    AbstractCompiler compiler = new TestCompiler();
    RedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler();
    ScopeCreator creator = new SyntacticScopeCreator(compiler, redeclarationHandler);

    Scope scope = creator.createScope(createNodeFromString(""), null);

    List<Scope.Var> vars = new ArrayList<>();
    vars.add(scope.getVar("x"));
    vars.add(scope.getVar("y"));
    vars.add(scope.getVar("z"));
    vars.add(scope.getVar("arguments"));

    assertEquals(ScopeType.GLOBAL, scope.getType());
    assertEquals(4, scope.getVariables().size());
    assertEquals(ImmutableList.copyOf(vars), scope.getVariables());
  }

  @Test
  public void testRedeclaration() throws IOException {
    AbstractCompiler compiler = new TestCompiler();
    RedeclarationHandler redeclarationHandler = new RedeclarationErrorHandler();
    ScopeCreator creator = new SyntheticScopeCreator(compiler, redeclarationHandler);

    Scope scope = creator.createScope(createNodeFromString(SOURCE), null);

    List<Scope.Var> vars = new ArrayList<>();
    vars.add(scope.getVar("x"));
    vars.add(scope.getVar("y"));
    vars.add(scope.getVar("z"));
    vars.add(scope.getVar("arguments"));

    assertEquals(ScopeType.LOCAL, scope.getType());
    assertEquals(4, scope.getVariables().size());
    assertEquals(ImmutableList.copyOf(vars), scope.getVariables());

    String errorMessage = "";
    try {
      scope = creator.createScope(createNodeFromString(SOURCE), scope);
    } catch (RuntimeException e) {
      errorMessage = e.getMessage();
    }

    assertTrue(errorMessage.contains("JSC_VAR_MULTIPLY_DECLARED_ERROR"));
    assertEquals(5, scope.getVariables().size());
    assertEquals(ImmutableList.copyOf(vars), scope.getVariables());
  }

  @Test
  public void testArgumentsShadowing() throws IOException {
    AbstractCompiler compiler = new TestCompiler();
    RedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler();
    ScopeCreator creator = new SyntacticScopeCreator(compiler, redeclarationHandler);

    Scope scope = creator.createScope(createNodeFromString(SOURCE), null);

    List<Scope.Var> vars = new ArrayList<>();
    vars.addAll(scope.getVariables());
    vars.removeIf(v -> v.getName().equals("arguments"));

    assertEquals(3, scope.getVariables().size());
    assertEquals(ImmutableList.copyOf(vars), scope.getVariables());

    String errorMessage = "";
    try {
      creator.createScope(
          createNodeFromString("function foo() {" +
                               "  var arguments = 1; " +
                               "}", null), scope);
    } catch (RuntimeException e) {
      errorMessage = e.getMessage();
    }

    assertTrue(errorMessage.contains("JSC_VAR_ARGUMENTS_SHADOWED_ERROR"));
    assertEquals(3, scope.getVariables().size());
  }

  @Test
  public void testFunctionDeclaration() throws IOException {
    AbstractCompiler compiler = new TestCompiler();
    RedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler();
    ScopeCreator creator = new SyntacticScopeCreator(compiler, redeclarationHandler);

    Scope parentScope = creator.createScope(createNodeFromString(""), null);
    Scope functionScope = creator.createScope(
        createNodeFromString("function foo() {"), parentScope);

    List<Scope.Var> functionVars = new ArrayList<>();
    functionVars.addAll(functionScope.getVariables());

    assertEquals(1, functionScope.getVariables().size());
    assertEquals(functionVars.get(0).getName(), "foo");

    Scope bodyScope = creator.createScope(
        createNodeFromString("var x = 1;"), functionScope);

    List<Scope.Var> bodyVars = new ArrayList<>();
    bodyVars.addAll(bodyScope.getVariables());

    assertEquals(1, bodyScope.getVariables().size());
    assertEquals(bodyVars.get(0).getName(), "x");

    Scope returnScope = creator.createScope(
        createNodeFromString("return 1;"), bodyScope);

    assertEquals(0, returnScope.getVariables().size());
  }

  @Test
  public void testCatchVariableDeclaration() throws IOException {
    AbstractCompiler compiler = new TestCompiler();
    RedeclarationHandler redeclarationHandler = new DefaultRedeclarationHandler();
    ScopeCreator creator = new SyntacticScopeCreator(compiler, redeclarationHandler);

    Scope parentScope = creator.createScope(createNodeFromString(""), null);
    Scope functionScope = creator.createScope(
        createNodeFromString("function foo() {" +
                             "  try {" +
                             "    catch (x) {} " +
                             "  } "), parentScope);

    List<Scope.Var> functionVars = new ArrayList<>();
    functionVars.addAll(functionScope.getVariables());

    assertEquals(1, functionScope.getVariables().size());
    assertEquals(functionVars.get(0).getName(), "x");

    Scope bodyScope = creator.createScope(
        createNodeFromString(""), functionScope);

    assertEquals(0, bodyScope.getVariables().size());
  }

  private static Node createNodeFromString(String string) {
    try {
      return compiler.parseTree(string, null);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static class RedeclarationErrorHandler implements RedeclarationHandler {

    @Override
    public void onRedeclaration(
        Scope s, String name,
        Node n, Node parent, Node gramps, Node nodeWithLineNumber) {

      fail("Should never reach here.");
    }
  }

  @BeforeClass
  public static void setupCompiler() {
    CompileTestRunner.setupGlobalCompiler();
  }

  @AfterClass
  public static void teardownCompiler() {
    CompileTestRunner.tearDownGlobalCompiler();
  }
}