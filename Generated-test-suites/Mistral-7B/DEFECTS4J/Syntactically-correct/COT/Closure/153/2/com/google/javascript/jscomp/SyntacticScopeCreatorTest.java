import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RedeclarationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.eurasian.JSError;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;

public class SyntacticScopeCreatorTest {

 private AbstractCompiler compiler;
 private SyntacticScopeCreator syntacticScopeCreator;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 syntacticScopeCreator = new SyntacticScopeCreator(compiler);
 }

 @Test
 public void testCreateScope() {
 Scope expectedScope = new Scope(null, null);
 Scope actualScope = syntacticScopeCreator.createScope(new Node(), null);
 assertEquals(expectedScope, actualScope);
 }

 @Test
 public void testCreateScopeWithParent() {
 Scope parentScope = new Scope(null, null);
 Scope expectedScope = new Scope(parentScope, null);
 Scope actualScope = syntacticScopeCreator.createScope(new Node(), parentScope);
 assertEquals(expectedScope, actualScope);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateScopeWithNullParent() {
 syntacticScopeCreator.createScope(new Node(), null);
 }

 @Test
 public void testScanRootFunction() {
 Node rootNode = new Node("function() {}");
 Scope actualScope = syntacticScopeCreator.createScope(rootNode, null);
 String expectedSourceName = "function() {}";
 assertEquals(expectedSourceName, actualScope.getSourceName());

 Var functionVar = actualScope.getVar("function");
 assertNull(functionVar);

 Var argumentsVar = actualScope.getVar("arguments");
 assertNull(argumentsVar);
}

 @Test
 public void testScanRootFunctionWithFunctionName() {
 Node rootNode = new Node("function foo() {}");
 Scope actualScope = syntacticScopeCreator.createScope(rootNode, null);
 String expectedSourceName = "function foo() {}";
 assertEquals(expectedSourceName, actualScope.getSourceName());

 Var functionVar = actualScope.getVar("foo");
 assertNotNull(functionVar);
 assertEquals("foo", functionVar.getName());
 assertNull(functionVar.getType());
}

 @Test
 public void testScanRootFunctionWithArguments() {
 Node rootNode = new Node("function foo(x, y) {}");
 Scope actualScope = syntacticScopeCreator.createScope(rootNode, null);
 String expectedSourceName = "function foo(x, y) {}";
 assertEquals(expectedSourceName, actualScope.getSourceName());

 Var argumentsVar = actualScope.getVar("arguments");
 assertNotNull(argumentsVar);
 assertEquals("arguments", argumentsVar.getName());
 assertNull(argumentsVar.getType());

 Var xVar = actualScope.getVar("x");
 assertNotNull(xVar);
 assertEquals("x", xVar.getName());
 assertNull(xVar.getType());

 Var yVar = actualScope.getVar("y");
 assertNotNull(yVar);
 assertEquals("y", yVar.getName());
 assertNull(yVar.getType());
}

 @Test
 public void testScanRootFunctionWithArgumentsAndFunctionName() {
 Node rootNode = new Node("function foo(x, y) { function bar() {} }");
 Scope actualScope = syntacticScopeCreator.createScope(rootNode, null);
 String expectedSourceName = "function foo(x, y) { function bar() {} }";
 assertEquals(expectedSourceName, actualScope.getSourceName());

 Var argumentsVar = actualScope.getVar("arguments");
 assertNotNull(argumentsVar);
 assertEquals("arguments", argumentsVar.getName());
 assertNull(argumentsVar.getType());

 Var xVar = actualScope.getVar("x");
 assertNotNull(xVar);
 assertEquals("x", xVar.getName());
 assertNull(xVar.getType());

 Var yVar = actualScope.getVar("y");
 assertNotNull(yVar);
 assertEquals("y", yVar.getName());
 assertNull(yVar.getType());

 Var functionBarVar = actualScope.getVar("bar");
 assertNotNull(functionBarVar);
 assertEquals("bar", functionBarVar.getName());
 assertNull(functionBarVar.getType());
}

 @Test
 public void testScanRootScript() {
 Node rootNode = new Node("var x = 1;");
 Scope actualScope = syntacticScopeCreator.createScope(rootNode, null);
 String expectedSourceName = "var x = 1;";
 assertEquals(expectedSourceName, actualScope.getSourceName());

 Var xVar = actualScope.getVar("x");
 assertNotNull(xVar);
 assertEquals("x", xVar.getName());
 assertEquals(JSType.NUMBER, xVar.getType());
}

 @Test
 public void testScanVarsFunction() {
 Node rootNode = new Node("function foo() { var x = 1; }");
 Scope parentScope = new Scope(null, null);
 Scope actualScope = syntacticScopeCreator.createScope(rootNode, parentScope);
 String expectedSourceName = "function foo() { var x = 1; }";
 assertEquals(expectedSourceName, actualScope.getSourceName());

 Var xVar = actualScope.getVar("x");
 assertNotNull(xVar);
 assertEquals("x", xVar.getName());
 assertEquals(JSType.NUMBER, xVar.getType());
}

 @Test
 public void testScanVarsFunctionWithNamedFunction() {
 Node rootNode = new Node("function foo() { function bar() {} }");
 Scope parentScope = new Scope(null, null);
 Scope actualScope = syntacticScopeCreator.createScope(rootNode, parentScope);
 String expectedSourceName = "function foo() { function bar() {} }";
 assertEquals(expectedSourceName, actualScope.getSourceName());

 Var barVar = actualScope.getVar("bar");
 assertNotNull(barVar);
 assertEquals("bar", barVar.getName());
 assertNull(barVar.getType());
}

 @Test
 public void testScanVarsVars() {
 Node rootNode = new Node("var x = 1, y = 2, z = 3;");
 Scope actualScope = syntacticScopeCreator.createScope(rootNode, null);
 String expectedSourceName = "var x = 1, y = 2, z = 3;";
 assertEquals(expectedSourceName, actualScope.getSourceName());

 Var xVar = actualScope.getVar("x");
 assertNotNull(xVar);
 assertEquals("x", xVar.getName());
 assertEquals(JSType.NUMBER, xVar.getType());

 Var yVar = actualScope.getVar("y");
 assertNotNull(yVar);
 assertEquals("y", yVar.getName());
 assertEquals(JSType.NUMBER, yVar.getType());

 Var zVar = actualScope.getVar("z");
 assertNotNull(zVar);
 assertEquals("z", zVar.getName());
 assertEquals(JSType.NUMBER, zVar.getType());
}

 @Test
 public void testScanVarsCatchBlock() {
 Node rootNode = new Node("try { /* empty */ } catch (e) { var x = 1; }");
 Scope actualScope = syntacticScopeCreator.createScope(rootNode, null);
 String expectedSourceName = "try { /* empty */ } catch (e) { var x = 1; }";
 assertEquals(expectedSourceName, actualScope.getSourceName());

 Var xVar = actualScope.getVar("x");
 assertNotNull(xVar);
 assertEquals("x", xVar.getName());
 assertEquals(JSType.NUMBER, xVar.getType());
}

 @Test
 public void testScanVarsControlStructure() {
 Node rootNode = new Node("if (true) { var x = 1; }");
 Scope actualScope = syntacticScopeCreator.createScope(rootNode, null);
 String expectedSourceName = "if (true) { var x = 1; }";
 assertEquals(expectedSourceName, actualScope.getSourceName());

 Var xVar = actualScope.getVar("x");
 assertNotNull(xVar);
 assertEquals("x", xVar.getName());
 assertEquals(JSType.NUMBER, xVar.getType());
}

 @Test
 public void testScanVarsStatementBlock() {
 Node rootNode = new Node("{ var x = 1; }");
 Scope actualScope = syntacticScopeCreator.createScope(rootNode, null);
 String expectedSourceName = "{ var x = 1; }";
 assertEquals(expectedSourceName, actualScope.getSourceName());

 Var xVar = actualScope.getVar("x");
 assertNotNull(xVar);
 assertEquals("x", xVar.getName());
 assertEquals(JSType.NUMBER, xVar.getType());
}

 @Test
 public void testRedeclarationHandlerDefault() {
 // Given
 Scope parentScope = new Scope(null, null);
 SyntacticScopeCreator syntacticScopeCreator = new SyntacticScopeCreator(compiler);

 // When
 String sourceName = "function foo() { var x = 1; var x = 2; }";
 Node rootNode = new Node(sourceName);
 Scope actualScope = syntacticScopeCreator.createScope(rootNode, parentScope);

 // Then
 String expectedErrorMessage = "Variable x first declared in ";
 assertEquals(expectedErrorMessage, actualScope.getErrorMessages().get(0).getMessage());
}

 @Test
 public void testRedeclarationHandlerWithSuppressions() {
 // Given
 JSDocInfo jsdocInfo = new JSDocInfo();
 jsdocInfo.addSuppression("duplicate");
 Node rootNode = new Node("/** @suppress {duplicate} */ function foo() { var x = 1; var x = 2; }");
 SyntacticScopeCreator syntacticScopeCreator = new SyntacticScopeCreator(compiler);

 // When
 Scope actualScope = syntacticScopeCreator.createScope(rootNode, null);

 // Then
 assertTrue(actualScope.getErrorMessages().isEmpty());
}

 @Test
 public void testRedefinitionOfArgumentsVariable() {
 // Given
 String sourceName = "function foo() { var arguments = 1; }";
 Node rootNode = new Node(sourceName);
 SyntacticScopeCreator syntacticScopeCreator = new SyntacticScopeCreator(compiler);

 // When
 Scope actualScope = syntacticScopeCreator.createScope(rootNode, null);

 // Then
 DiagnosticType expectedDiagnosticType = VAR_ARGUMENTS_SHADOWED_ERROR;
 JSError expectedError = new JSError(0, 0, expectedDiagnosticType);
 assertEquals(expectedDiagnosticType, actualScope.getErrorMessages().get(0).getType());
 assertEquals(expectedError, actualScope.getErrorMessages().get(0).getError());
}
}