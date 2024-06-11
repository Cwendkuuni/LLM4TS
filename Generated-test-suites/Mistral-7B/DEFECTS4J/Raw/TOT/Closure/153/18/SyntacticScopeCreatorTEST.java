import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.TestBase;
import com.google.javascript.rhino.JSDocInfo;

import static com.google.common.truth.Truth.assertThat;

public class SyntacticScopeCreatorTest extends TestBase {

  private AbstractCompiler compiler;
  private ScopeCreator syntacticScopeCreator;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    compiler = new AbstractCompiler();
    syntacticScopeCreator = new SyntacticScopeCreator(compiler);
  }

  @org.junit.Test
  public void testEmptyFunction() {
    Node root = js.parse("function foo() {}");
    Scope scope = syntacticScopeCreator.createScope(root, null);

    assertEmpty(scope.getVars());
  }

  @org.junit.Test
  public void testFunctionWithArguments() {
    Node root = js.parse("function foo(a, b) {}");
    Scope scope = syntacticScopeCreator.createScope(root, null);

    Scope.Var aVar = scope.getVar("a");
    Scope.Var bVar = scope.getVar("b");

    assertThat(aVar).isNotNull();
    assertThat(bVar).isNotNull();
  }

  @org.junit.Test
  public void testFunctionWithArgumentsAndBody() {
    Node root = js.parse("function foo(a, b) {" +
        " var c = 1; " +
        "}");
    Scope scope = syntacticScopeCreator.createScope(root, null);

    Scope.Var aVar = scope.getVar("a");
    Scope.Var bVar = scope.getVar("b");
    Scope.Var cVar = scope.getVar("c");

    assertThat(aVar).isNotNull();
    assertThat(bVar).isNotNull();
    assertThat(cVar).isNotNull();
  }

  @org.junit.Test
  public void testFunctionWithArgumentsAndBodyAndReturnType() {
    Node root = js.parse("function foo(a, b): number {" +
        " var c = 1; " +
        " return 0; " +
        "}");
    Scope scope = syntacticScopeCreator.createScope(root, null);

    Scope.Var aVar = scope.getVar("a");
    Scope.Var bVar = scope.getVar("b");
    Scope.Var cVar = scope.getVar("c");

    assertThat(aVar).isNotNull();
    assertThat(bVar).isNotNull();
    assertThat(cVar).isNotNull();

    JSDocInfo jsDocInfo = root.getJSDocInfo();
    assertThat(jsDocInfo).isNotNull();
    assertThat(jsDocInfo.getReturnType()).isInstanceOf(JSType.number());
  }

  @org.junit.Test
  public void testFunctionWithNameAndEmptyBody() {
    Node root = js.parse("function foo() {}");
    Scope scope = syntacticScopeCreator.createScope(root, null);

    Scope.Var fooVar = scope.getVar("foo");

    assertThat(fooVar).isNotNull();
  }

  @org.junit.Test
  public void testFunctionWithNameAndBody() {
    Node root = js.parse("function foo() {" +
        " var a = 1; " +
        "}");
    Scope scope = syntacticScopeCreator.createScope(root, null);

    Scope.Var fooVar = scope.getVar("foo");
    Scope.Var aVar = scope.getVar("a");

    assertThat(fooVar).isNotNull();
    assertThat(aVar).isNotNull();
  }

  @org.junit.Test
  public void testFunctionWithNameAndBodyAndReturnType() {
    Node root = js.parse("function foo(): number {" +
        " var a = 1; " +
        " return 0; " +
        "}");
    Scope scope = syntacticScopeCreator.createScope(root, null);

    Scope.Var fooVar = scope.getVar("foo");
    Scope.Var aVar = scope.getVar("a");

    assertThat(fooVar).isNotNull();
    assertThat(aVar).isNotNull();

    JSDocInfo jsDocInfo = root.getJSDocInfo();
    assertThat(jsDocInfo).isNotNull();
    assertThat(jsDocInfo.getReturnType()).isInstanceOf(JSType.number());
  }

  @org.junit.Test
  public void testFunctionWithNameAndArgumentsAndBody() {
    Node root = js.parse("function foo(a, b) {" +
        " var c = 1; " +
        "}");
    Scope scope = syntacticScopeCreator.createScope(root, null);

    Scope.Var fooVar = scope.getVar("foo");
    Scope.Var aVar = scope.getVar("a");
    Scope.Var bVar = scope.getVar("b");
    Scope.Var cVar = scope.getVar("c");

    assertThat(fooVar).isNotNull();
    assertThat(aVar).isNotNull();
    assertThat(bVar).isNotNull();
    assertThat(cVar).isNotNull();
  }

  @org.junit.Test
  public void testFunctionWithNameAndArgumentsAndBodyAndReturnType() {
    Node root = js.parse("function foo(a: number, b: string): number {" +
        " var c = 1; " +
        " return 0; " +
        "}");
    Scope scope = syntacticScopeCreator.createScope(root, null);

    Scope.Var fooVar = scope.getVar("foo");
    Scope.Var aVar = scope.getVar("a");
    Scope.Var bVar = scope.getVar("b");
    Scope.Var cVar = scope.getVar("c");

    assertThat(fooVar).isNotNull();
    assertThat(aVar).isNotNull();
    assertThat(bVar).isNotNull();
    assertThat(cVar).isNotNull();

    JSDocInfo jsDocInfo = root.getJSDocInfo();
    assertThat(jsDocInfo).isNotNull();
    assertThat(jsDocInfo.getParams()).hasSize(2);
    assertThat(jsDocInfo.getReturnType()).isInstanceOf(JSType.number());
  }

  @org.junit.Test
  public void testVariableDeclaration() {
    Node root = js.parse("var a = 1, b = 2, c = 3;");
    Scope scope = syntacticScopeCreator.createScope(root, null);

    Scope.Var aVar = scope.getVar("a");
    Scope.Var bVar = scope.getVar("b");
    Scope.Var cVar = scope.getVar("c");

    assertThat(aVar).isNotNull();
    assertThat(bVar).isNotNull();
    assertThat(cVar).isNotNull();
  }

  @org.junit.Test
  public void testVariableDeclarationWithType() {
    Node root = js.parse("var a: number = 1, b: string = 2, c: boolean = true;");
    Scope scope = syntacticScopeCreator.createScope(root, null);

    Scope.Var aVar = scope.getVar("a");
    Scope.Var bVar = scope.getVar("b");
    Scope.Var cVar = scope.getVar("c");

    assertThat(aVar).isNotNull();
    assertThat(bVar).isNotNull();
    assertThat(cVar).isNotNull();

    JSDocInfo jsDocInfo = root.getJSDocInfo();
    assertThat(jsDocInfo).isNotNull();
    assertThat(jsDocInfo.getVars()).hasSize(3);
    assertThat(jsDocInfo.getVars().get(0).getType()).isInstanceOf(JSType.number());
    assertThat(jsDocInfo.getVars().get(1).getType()).isInstanceOf(JSType.string());
    assertThat(jsDocInfo.getVars().get(2).getType()).isInstanceOf(JSType.booleanType());
  }

  @org.junit.Test
  public void testVariableDeclarationWithSameName() {
    Node root = js.parse("var a = 1; var a = 2;");
    Scope scope = syntacticScopeCreator.createScope(root, null);

    Scope.Var aVar = scope.getVar("a");

    assertThat(aVar).isNotNull();

    DiagnosticType diagnosticType = scope.getDiagnostic(0);
    assertThat(diagnosticType).isNotNull();
    assertThat(diagnosticType).isInstanceOf(VAR_MULTIPLY_DECLARED_ERROR);
  }

  @org.junit.Test
  public void testVariableDeclarationWithSameNameInNestedScope() {
    Node root = js.parse("function foo() {" +
        " var a = 1; " +
        " var a = 2; " +
        "}");
    Scope scope = syntacticScopeCreator.createScope(root, null);

    Scope innerScope = scope.getLocalScope();
    Scope.Var aVar1 = innerScope.getVar("a");
    Scope.Var aVar2 = innerScope.getVar("a");

    assertThat(aVar1).isNotNull();
    assertThat(aVar2).isNotNull();

    DiagnosticType diagnosticType = innerScope.getDiagnostic(0);
    assertThat(diagnosticType).isNotNull();
    assertThat(diagnosticType).isInstanceOf(VAR_MULTIPLY_DECLARED_ERROR);
  }

  @org.junit.Test
  public void testVariableDeclarationWithArgumentsName() {
    Node root = js.parse("function foo(a) {" +
        " var a = 1; " +
        "}");
    Scope scope = syntacticScopeCreator.createScope(root, null);

    Scope.Var aVar = scope.getVar("a");

    assertThat(aVar).isNotNull();

    DiagnosticType diagnosticType = scope.getDiagnostic(0);
    assertThat(diagnosticType).isNotNull();
    assertThat(diagnosticType).isInstanceOf(VAR_ARGUMENTS_SHADOWED_ERROR);
  }

  @org.junit.Test
  public void testVariableDeclarationWithArgumentsNameInNestedScope() {
    Node root = js.parse("function foo(a) {" +
        " function bar() {" +
        " var a = 1; " +
        " }" +
        "}");
    Scope scope = syntacticScopeCreator.createScope(root, null);

    Scope innerScope = scope.getFunctionScope("bar");
    Scope.Var aVar = innerScope.getVar("a");

    assertThat(aVar).isNotNull();

    DiagnosticType diagnosticType = innerScope.getDiagnostic(0);
    assertThat(diagnosticType).isNotNull();
    assertThat(diagnosticType).isInstanceOf(VAR_ARGUMENTS_SHADOWED_ERROR);
  }
}