import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {

  @SuppressWarnings("unchecked")
  private static final Class<?> TEST_CLASS = ScopedAliases.class;

  @SuppressWarnings("unchecked")
  private static final HotSwapCompilerPass<Node, Node> TEST_INSTANCE =
      (HotSwapCompilerPass<Node, Node>) reflectionGetInstance( TEST_CLASS );

  private static Object reflectionGetInstance(Class<?> clazz) {
    try {
      Constructor<?> constructor = clazz.getDeclaredConstructor(
          AbstractCompiler.class, PreprocessorSymbolTable.class,
          AliasTransformationHandler.class);
      constructor.setAccessible(true);
      return constructor.newInstance(mockAbstractCompiler(), mockPreprocessorSymbolTable(), mockAliasTransformationHandler());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static AbstractCompiler mockAbstractCompiler() {
    return mock(AbstractCompiler.class);
  }

  private static PreprocessorSymbolTable mockPreprocessorSymbolTable() {
    return mock(PreprocessorSymbolTable.class);
  }

  private static AliasTransformationHandler mockAliasTransformationHandler() {
    return mock(AliasTransformationHandler.class);
  }

  private static void testProcess(Node externs, Node root) {
    doTest("process", () -> TEST_INSTANCE.process(externs, root));
  }

  private static void testHotSwapScript(Node root, Node originalRoot) {
    doTest("hotSwapScript", () -> TEST_INSTANCE.hotSwapScript(root, originalRoot));
  }

  private static void doTest(String methodName, Runnable testMethod) {
    System.out.println("Testing " + methodName);
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALL);
    AbstractCompiler compiler = mockCompiler(options);

    Node root = new Node(Token.START_FILE, "test.js", 1, 1);
    root.setString("goog.scope(function() { \n" +
        "  var dom = goog.dom; \n" +
        "  var DIV = dom.TagName.DIV; \n" +
        "  dom.createElement(DIV); \n" +
        "});");

    testMethod.run();

    assertFalse("No errors expected", TEST_INSTANCE.hasErrors());
    assertTrue("Alias definition order is correct",
        TEST_INSTANCE.getAliasDefinitionsInOrder().size() == 2);
    assertTrue("Scope calls are correct", TEST_INSTANCE.getScopeCalls().size() == 1);
  }

  @Test
  public void testProcess_NoAliases() {
    Node externs = new Node(Token.START_FILE, "test.js", 1, 1);
    Node root = new Node(Token.START_FILE, "test.js", 1, 1);
    testProcess(externs, root);
  }

  @Test
  public void testProcess_SimpleAliases() {
    Node externs = new Node(Token.START_FILE, "test.js", 1, 1);
    Node root = new Node(Token.START_FILE, "test.js", 1, 1);
    root.setString("goog.scope(function() { \n" +
        "  var x = goog.x; \n" +
        "  var y = goog.y; \n" +
        "});");
    testProcess(externs, root);
  }

  @Test
  public void testProcess_MultipleScopes() {
    Node externs = new Node(Token.START_FILE, "test.js", 1, 1);
    Node root = new Node(Token.START_FILE, "test.js", 1, 1);
    root.setString("goog.scope(function() { \n" +
        "  var x = goog.x; \n" +
        "}); \n" +
        "goog.scope(function() { \n" +
        "  var y = goog.y; \n" +
        "});");
    testProcess(externs, root);
  }

  @Test
  public void testHotSwapScript_NoAliases() {
    Node root = new Node(Token.START_FILE, "test.js", 1, 1);
    Node originalRoot = new Node(Token.START_FILE, "test.js", 1, 1);
    testHotSwapScript(root, originalRoot);
  }

  @Test
  public void testHotSwapScript_SimpleAliases() {
    Node root = new Node(Token.START_FILE, "test.js", 1, 1);
    root.setString("goog.scope(function() { \n" +
        "  var x = goog.x; \n" +
        "  var y = goog.y; \n" +
        "});");
    Node originalRoot = new Node(Token.START_FILE, "test.js", 1, 1);
    originalRoot.setString("goog.scope(function() { \n" +
        "  var x = 1; \n" +
        "  var y = 2; \n" +
        "});");
    testHotSwapScript(root, originalRoot);
  }

  @Test
  public void testHotSwapScript_MultipleAliases() {
    Node root = new Node(Token.START_FILE, "test.js", 1, 1);
    root.setString("goog.scope(function() { \n" +
        "  var x = goog.x; \n" +
        "  var y = goog.y; \n" +
        "}); \n" +
        "goog.scope(function() { \n" +
        "  var z = goog.z; \n" +
        "});");
    Node originalRoot = new Node(Token.START_FILE, "test.js", 1, 1);
    originalRoot.setString("goog.scope(function() { \n" +
        "  var x = 1; \n" +
        "  var y = 2; \n" +
        "}); \n" +
        "goog.scope(function() { \n" +
        "  var z = 3; \n" +
        "});");
    testHotSwapScript(root, originalRoot);
  }

  @Test
  public void testProcess_InvalidScopeCall() {
    Node externs = new Node(Token.START_FILE, "test.js", 1, 1);
    Node root = new Node(Token.START_FILE, "test.js", 1, 1);
    root.setString("goog.scope(function() { \n" +
        "  goog.scope(); \n" +
        "});");
    testProcess(externs, root);

    List<DiagnosticType> errors = TEST_INSTANCE.getDiagnostics();
    assertEquals("One error expected", 1, errors.size());
    assertEquals("goog.scope() must be alone in a single statement.",
        JSC_GOOG_SCOPE_USED_IMPROPERLY, errors.get(0));
  }

  @Test
  public void testProcess_InvalidFunctionBody() {
    Node externs = new Node(Token.START_FILE, "test.js", 1, 1);
    Node root = new Node(Token.START_FILE, "test.js", 1, 1);
    root.setString("goog.scope(function() { \n" +
        "  var x = goog.x; \n" +
        "  return; \n" +
        "  var y = goog.y; \n" +
        "});");
    testProcess(externs, root);

    List<DiagnosticType> errors = TEST_INSTANCE.getDiagnostics();
    assertEquals("No errors expected", 0, errors.size());
  }

  @Test
  public void testProcess_InvalidFunctionBodyWithThrow() {
    Node externs = new Node(Token.START_FILE, "test.js", 1, 1);
    Node root = new Node(Token.START_FILE, "test.js", 1, 1);
    root.setString("goog.scope(function() { \n" +
        "  var x = goog.x; \n" +
        "  throw new Error(); \n" +
        "  var y = goog.y; \n" +
        "});");
    testProcess(externs, root);

    List<DiagnosticType> errors = TEST_INSTANCE.getDiagnostics();
    assertEquals("One error expected", 1, errors.size());
    assertEquals("The body of a goog.scope function cannot use 'throw'.",
        JSC_GOOG_SCOPE_USES_THROW, errors.get(0));
  }

  @Test
  public void testProcess_InvalidFunctionBodyWithThis() {
    Node externs = new Node(Token.START_FILE, "test.js", 1, 1);
    Node root = new Node(Token.START_FILE, "test.js", 1, 1);
    root.setString("goog.scope(function() { \n" +
        "  this.x = goog.x; \n" +
        "  var y = goog.y; \n" +
        "});");
    testProcess(externs, root);

    List<DiagnosticType> errors = TEST_INSTANCE.getDiagnostics();
    assertEquals("One error expected", 1, errors.size());
    assertEquals("The body of a goog.scope function cannot reference 'this'.",
        JSC_GOOG_SCOPE_REFERENCES_THIS, errors.get(0));
  }

  @Test
  public void testProcess_InvalidFunctionBodyWithReturn() {
    Node externs = new Node(Token.START_FILE, "test.js", 1, 1);
    Node root = new Node(Token.START_FILE, "test.js", 1, 1);
    root.setString("goog.scope(function() { \n" +
        "  return goog.x; \n" +
        "  var y = goog.y; \n" +
        "});");
    testProcess(externs, root);

    List<DiagnosticType> errors = TEST_INSTANCE.getDiagnostics();
    assertEquals("One error expected", 1, errors.size());
    assertEquals("The body of a goog.scope function cannot use 'return'.",
        JSC_GOOG_SCOPE_USES_RETURN, errors.get(0));
  }

  @Test
  public void testProcess_AliasRedefinition() {
    Node externs = new Node(Token.START_FILE, "test.js", 1, 1);
    Node root = new Node(Token.START_FILE, "test.js", 1, 1);
    root.setString("goog.scope(function() { \n" +
        "  var x = goog.x; \n" +
        "  var x = goog.x; \n" +
        "});");
    testProcess(externs, root);

    List<DiagnosticType> errors = TEST_INSTANCE.getDiagnostics();
    assertEquals("One error expected", 1, errors.size());
    assertEquals("The alias x is assigned a value more than once.",
        JSC_GOOG_SCOPE_ALIAS_REDEFINED, errors.get(0));
  }

  @Test
  public void testProcess_NonAliasLocalVariable() {
    Node externs = new Node(Token.START_FILE, "test.js", 1, 1);
    Node root = new Node(Token.START_FILE, "test.js", 1, 1);
    root.setString("goog.scope(function() { \n" +
        "  var x = 1; \n" +
        "});");
    testProcess(externs, root);

    List<DiagnosticType> errors = TEST_INSTANCE.getDiagnostics();
    assertEquals("One error expected", 1, errors.size());
    assertEquals("The local variable x is in a goog.scope and is not an alias.",
        JSC_GOOG_SCOPE_NON_ALIAS_LOCAL, errors.get(0));
  }
}