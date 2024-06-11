import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.TestSourceFile;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static com.google.common.truth.Truth.assertThat;

@Category(IntegrationTests.class)
public class SyntacticScopeCreatorTest {

  private TestSourceFile testSourceFile;
  private AbstractCompiler compiler;
  private ScopeCreator syntacticScopeCreator;

  @Before
  public void setup() {
    testSourceFile = new TestSourceFile();
    compiler = new Compiler(testSourceFile);
    syntacticScopeCreator = new SyntacticScopeCreator(compiler);
  }

  @Test
  public void testSimpleVariableDeclaration() {
    String sourceCode = "function test() {" +
        "  var x = 1; " +
        "  var y; " +
        "  var z = 2; " +
        "}";

    Node ast = testSourceFile.compile(sourceCode);
    Scope rootScope = syntacticScopeCreator.createScope(ast, null);

    Scope testScope = rootScope.getChildScope();

    assertThat(testScope.getVariable("x")).isNotNull();
    assertThat(testScope.getVariable("x").getValue()).isNotNull();
    assertThat(testScope.getVariable("y")).isNotNull();
    assertThat(testScope.getVariable("y").getValue()).isNull();
    assertThat(testScope.getVariable("z")).isNotNull();
    assertThat(testScope.getVariable("z").getValue()).isNotNull();
  }

  @Test
  public void testFunctionDeclaration() {
    String sourceCode = "function test() {" +
        "  function foo() {} " +
        "}";

    Node ast = testSourceFile.compile(sourceCode);
    Scope rootScope = syntacticScopeCreator.createScope(ast, null);

    Scope testScope = rootScope.getChildScope();
    Scope fooScope = testScope.getChildScope();

    assertThat(fooScope.getName()).isEqualTo("foo");
    assertThat(fooScope.getParentScope()).isEqualTo(testScope);
  }

  @Test
  public void testDuplicateVariableDeclaration() {
    String sourceCode = "function test() {" +
        "  var x = 1; " +
        "  var x = 2; " +
        "}";

    Node ast = testSourceFile.compile(sourceCode);
    Scope rootScope = syntacticScopeCreator.createScope(ast, null);

    try {
      rootScope.getChildScope();
      fail("Expected JSC_VAR_MULTIPLY_DECLARED_ERROR");
    } catch (RuntimeException e) {
      DiagnosticType diagnosticType = DiagnosticType.fromCode(e.getCause().getMessage());
      assertThat(diagnosticType).isEqualTo(SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR);
    }
  }

  @Test
  public void testArgumentsVariableDeclaration() {
    String sourceCode = "function test(x) {" +
        "  var arguments = []; " +
        "  arguments.push(x); " +
        "}";

    Node ast = testSourceFile.compile(sourceCode);
    Scope rootScope = syntacticScopeCreator.createScope(ast, null);

    Scope testScope = rootScope.getChildScope();

    try {
      syntacticScopeCreator.declareVar("arguments", null, testScope, null, null, ast);
      fail("Expected JSC_VAR_ARGUMENTS_SHADOWED_ERROR");
    } catch (RuntimeException e) {
      DiagnosticType diagnosticType = DiagnosticType.fromCode(e.getCause().getMessage());
      assertThat(diagnosticType).isEqualTo(SyntacticScopeCreator.VAR_ARGUMENTS_SHADOWED_ERROR);
    }
  }
}