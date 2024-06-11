import org.junit.Test;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.SyntacticScopeCreator;
import com.google.javascript.rhino.Node;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SyntacticScopeCreatorTest {

 private ScopeCreator sc;
 private Scope rootScope;

 @Test
 public void testSyntacticScopeCreator() {
 sc = new SyntacticScopeCreator(new NoOpCompiler());
 rootScope = sc.createScope(createFunctionNode("test"), null);
 }

 @Test
 public void testDeclaringVariables() {
 Node funcNode = createFunctionNode("testFunction() {}");
 Scope scope = sc.createScope(funcNode, null);
 String[] expectedVariableNames = {"x", "y", "z"};
 String[] actualVariableNames = new String[scope.size()];
 int i = 0;
 for (Scope.Var var : scope.getVariables()) {
 actualVariableNames[i++] = var.getName();
 }
 assertEquals(expectedVariableNames.length, scope.size());
 for (int j = 0; j < expectedVariableNames.length; j++) {
 assertEquals(expectedVariableNames[j], actualVariableNames[j]);
 }
 }

 @Test
 public void testDeclaringFunction() {
 Node funcNode = createFunctionNode("function add(x, y) { return x + y; }");
 Scope scope = sc.createScope(funcNode, null);
 String expectedFunctionName = "add";
 String actualFunctionName = "";
 for (Scope.Var var : scope.getVariables()) {
 if (var.getName().equals(expectedFunctionName)) {
 actualFunctionName = var.getType().getName();
 break;
 }
 }
 assertEquals("function", actualFunctionName);
 }

 @Test
 public void testDeclaringGlobalVariables() {
 Node funcNode = createFunctionNode("function test() { var x; }");
 Scope scope = sc.createScope(funcNode, null);
 assertNull(scope.getVariable("x"));
 }

 @Test
 public void testDeclaringGlobalVariablesWithValues() {
 Node funcNode = createFunctionNode("function test() { var x = 5; }");
 Scope scope = sc.createScope(funcNode, null);
 Scope.Var var = scope.getVariable("x");
 assertEquals(5, var.getValue().getInt());
 }

 @Test
 public void testDeclaringGlobalVariableWithSameNameInFunction() {
 Node funcNode = createFunctionNode("function test() { var x; } function test() { var x; }");
 Scope scope = sc.createScope(funcNode, null);
 Scope.Var var1 = scope.getVariable("x");
 Scope.Var var2 = scope.getVariable("x");
 assertNotSame(var1, var2);
 }

 @Test
 public void testDeclaringGlobalVariableWithSameNameInGlobal() {
 Node funcNode1 = createFunctionNode("function test() { }");
 Node funcNode2 = createFunctionNode("function test() { var x; }");
 Scope scope1 = sc.createScope(funcNode1, null);
 Scope scope2 = sc.createScope(funcNode2, null);
 Scope.Var var1 = scope1.getVariable("x");
 Scope.Var var2 = scope2.getVariable("x");
 assertNotSame(var1, var2);
 }

 @Test
 public void testDeclaringArgumentsVariable() {
 Node funcNode = createFunctionNode("function test(x, y) {}");
 Scope scope = sc.createScope(funcNode, null);
 String expectedArgumentName = "arguments";
 String actualArgumentName = "";
 for (Scope.Var var : scope.getVariables()) {
 if (var.getName().equals(expectedArgumentName)) {
 actualArgumentName = var.getType().getName();
 break;
 }
 }
 assertEquals("arguments", actualArgumentName);
 }

 @Test
 public void testDeclaringArgumentsVariableInFunction() {
 Node funcNode = createFunctionNode("function test(x, y) { var arguments; }");
 Scope scope = sc.createScope(funcNode, null);
 assertNull(scope.getVariable("arguments"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeclaringArgumentsVariableTwice() {
 Node funcNode = createFunctionNode("function test(x, y) { var arguments; } function test(x, y) { var arguments; }");
 sc.createScope(funcNode, null);
 }

 @Test
 public void testDeclaringArgumentsVariableInGlobal() {
 Node funcNode1 = createFunctionNode("function test() {}");
 Node funcNode2 = createFunctionNode("function test() { var arguments; }");
 Scope scope1 = sc.createScope(funcNode1, null);
 Scope scope2 = sc.createScope(funcNode2, null);
 assertNull(scope1.getVariable("arguments"));
 assertNull(scope2.getVariable("arguments"));
 }

 private Node createFunctionNode(String code) {
 return NodePrinter.print(code).getFirstChild();
 }

}