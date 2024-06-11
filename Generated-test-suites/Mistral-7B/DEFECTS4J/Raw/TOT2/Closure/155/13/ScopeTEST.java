import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

 @Test
 public void testCreateScope() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 assertEquals(0, scope.getDepth());
 assertNull(scope.getParent());
 assertNotNull(scope.getTypeOfThis());
 }

 @Test
 public void testCreateFunctionScope() {
 Node functionNode = new Node(Token.FUNCTION, new Node("functionName"), new Node[0], new Node[0]);
 Scope scope = new Scope(new Scope(null, new Node(Token.BLOCK)), functionNode);
 assertEquals(1, scope.getDepth());
 assertNotNull(scope.getParent());
 assertNotNull(scope.getTypeOfThis());
 }

 @Test
 public void testDeclareVariable() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 String variableName = "testVariable";
 Node variableNode = new Node(Token.VAR, new Node(variableName), new Node[0]);
 JSType variableType = JSType.getNull();
 CompilerInput input = new CompilerInput("testFile.js");
 Var variable = scope.declare(variableName, variableNode, variableType, input);
 assertEquals(variableName, variable.getName());
 assertEquals(variableType, variable.getType());
 assertEquals(0, variable.getIndex());
 }

 @Test
 public void testDeclareFunction() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 String functionName = "testFunction";
 Node functionNode = new Node(Token.FUNCTION, new Node(functionName), new Node[0], new Node[0]);
 JSType thisType = new ObjectType("Function");
 Scope functionScope = new Scope(scope, functionNode);
 Var function = functionScope.declare(functionName, functionNode, thisType, null);
 assertEquals(functionName, function.getName());
 assertEquals(thisType, function.getType());
 assertEquals(-1, function.getIndex());
 }

 @Test
 public void testResolveVariableType() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 String variableName = "testVariable";
 Node variableNode = new Node(Token.VAR, new Node(variableName), new Node[0]);
 JSType initialValueType = JSType.getInt();
 Node initialValueNode = new Node(Token.NUMBER, new Node[0], initialValueType.toNode());
 Node assignNode = new Node(Token.ASSIGN, variableNode, initialValueNode);
 Scope scopeWithInitialValue = new Scope(scope, assignNode);
 Var variable = scopeWithInitialValue.getVar(variableName);
 JSType expectedType = JSType.getInt();
 variable.resolveType(new ErrorReporter() {
 @Override
 public void warn(String message, int lineNumber, int lineOffset) {
 // ignored
 }

 @Override
 public void error(String message, int lineNumber, int lineOffset) {
 fail("Unexpected error: " + message);
 }
 });
 assertEquals(expectedType, variable.getType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetVariableTypeAfterDeclaration() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 String variableName = "testVariable";
 Node variableNode = new Node(Token.VAR, new Node(variableName), new Node[0]);
 JSType initialValueType = JSType.getInt();
 Node initialValueNode = new Node(Token.NUMBER, new Node[0], initialValueType.toNode());
 Node assignNode = new Node(Token.ASSIGN, variableNode, initialValueNode);
 Scope scopeWithInitialValue = new Scope(scope, assignNode);
 Var variable = scopeWithInitialValue.getVar(variableName);
 variable.setType(JSType.getBoolean());
 }

 @Test
 public void testGetVariableByIndex() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 String variable1Name = "testVariable1";
 String variable2Name = "testVariable2";
 Node variable1Node = new Node(Token.VAR, new Node(variable1Name), new Node[0]);
 Node variable2Node = new Node(Token.VAR, new Node(variable2Name), new Node[0]);
 JSType variable1Type = JSType.getInt();
 JSType variable2Type = JSType.getBoolean();
 CompilerInput input1 = new CompilerInput("testFile1.js");
 CompilerInput input2 = new CompilerInput("testFile2.js");
 Var variable1 = scope.declare(variable1Name, variable1Node, variable1Type, input1);
 Var variable2 = scope.declare(variable2Name, variable2Node, variable2Type, input2);
 assertEquals(variable1, scope.getVar(0));
 assertEquals(variable2, scope.getVar(1));
 }

 @Test
 public void testGetVariableByName() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 String variable1Name = "testVariable1";
 String variable2Name = "testVariable2";
 Node variable1Node = new Node(Token.VAR, new Node(variable1Name), new Node[0]);
 Node variable2Node = new Node(Token.VAR, new Node(variable2Name), new Node[0]);
 JSType variable1Type = JSType.getInt();
 JSType variable2Type = JSType.getBoolean();
 CompilerInput input1 = new CompilerInput("testFile1.js");
 CompilerInput input2 = new CompilerInput("testFile2.js");
 Var variable1 = scope.declare(variable1Name, variable1Node, variable1Type, input1);
 Var variable2 = scope.declare(variable2Name, variable2Node, variable2Type, input2);
 assertEquals(variable1, scope.getVar(variable1Name));
 assertEquals(variable2, scope.getVar(variable2Name));
 }

 @Test
 public void testGetVariableByParent() {
 Scope parentScope = new Scope(null, new Node(Token.BLOCK));
 String variableName = "testVariable";
 Node variableNode = new Node(Token.VAR, new Node(variableName), new Node[0]);
 JSType variableType = JSType.getInt();
 Var variable = parentScope.declare(variableName, variableNode, variableType, null);
 Scope childScope = new Scope(parentScope, new Node(Token.BLOCK));
 assertEquals(variable, childScope.getVar(variableName));
 }

 @Test
 public void testGetVariableByGlobalScope() {
 String variableName = "testVariable";
 Scope globalScope = new Scope(null, new Node(Token.BLOCK));
 Node variableNode = new Node(Token.VAR, new Node(variableName), new Node[0]);
 JSType variableType = JSType.getInt();
 Var variable = globalScope.declare(variableName, variableNode, variableType, null);
 Scope localScope = new Scope(globalScope, new Node(Token.BLOCK));
 assertEquals(variable, localScope.getVar(variableName));
 }

 @Test
 public void testUndelecareVariable() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 String variableName = "testVariable";
 Node variableNode = new Node(Token.VAR, new Node(variableName), new Node[0]);
 JSType variableType = JSType.getInt();
 CompilerInput input = new CompilerInput("testFile.js");
 Var variable = scope.declare(variableName, variableNode, variableType, input);
 scope.undeclare(variable);
 assertNull(scope.getVar(variableName));
 }
}