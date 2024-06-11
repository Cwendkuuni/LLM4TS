import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class MustBeReachingVariableDefTest {

 @Test
 public void testMustBeReachingVariableDef() {
 // Given
 Scope jsScope = new Scope();
 AbstractCompiler compiler = new AbstractCompiler();
 ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Node(), new Node());
 MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

 // When
 Node rootNode = new Node(Token.BLOCK);
 Node assignment1 = new Node(Token.ASSIGN, rootNode, new Node(Token.NAME, "a"),
 new Node(Token.NUM, 5));
 Node assignment2 = new Node(Token.ASSIGN, rootNode, new Node(Token.NAME, "a"),
 new Node(Token.NAME, "b"));
 Node functionCall = new Node(Token.CALL, rootNode, new Node(Token.NAME, "func"),
 new Node(Token.NUM, 1));
 Node returnStatement = new Node(Token.RETURN, rootNode, new Node(Token.NAME, "a"));
 rootNode.addChild(assignment1);
 rootNode.addChild(assignment2);
 rootNode.addChild(functionCall);
 rootNode.addChild(returnStatement);
 analysis.flowThrough(rootNode, analysis.createInitialEstimateLattice());

 // Then
 MustDef expectedResult = new MustDef();
 expectedResult.reachingDef.put(jsScope.getVar("a"), new Definition(assignment1));
 expectedResult.reachingDef.put(jsScope.getVar("b"), new Definition(assignment2));
 MustDef actualResult = analysis.getResult();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testGetDef() {
 // Given
 Scope jsScope = new Scope();
 AbstractCompiler compiler = new AbstractCompiler();
 ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Node(), new Node());
 MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
 Node rootNode = new Node(Token.BLOCK);
 Node assignment1 = new Node(Token.ASSIGN, rootNode, new Node(Token.NAME, "a"),
 new Node(Token.NUM, 5));
 Node assignment2 = new Node(Token.ASSIGN, rootNode, new Node(Token.NAME, "a"),
 new Node(Token.NAME, "b"));
 Node functionCall = new Node(Token.CALL, rootNode, new Node(Token.NAME, "func"),
 new Node(Token.NUM, 1));
 Node returnStatement = new Node(Token.RETURN, rootNode, new Node(Token.NAME, "a"));
 rootNode.addChild(assignment1);
 rootNode.addChild(assignment2);
 rootNode.addChild(functionCall);
 rootNode.addChild(returnStatement);
 analysis.flowThrough(rootNode, analysis.createInitialEstimateLattice());

 // When
 String variableName = "a";
 Node useNode = assignment2;
 Node expectedDefNode = assignment1;
 Node actualDefNode = analysis.getDef(variableName, useNode);

 // Then
 assertEquals(expectedDefNode, actualDefNode);
 }

 @Test
 public void testDoesNotGetDefForNonLocalVariable() {
 // Given
 Scope jsScope = new Scope();
 AbstractCompiler compiler = new AbstractCompiler();
 ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Node(), new Node());
 MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
 Node rootNode = new Node(Token.BLOCK);
 Node assignment1 = new Node(Token.ASSIGN, rootNode, new Node(Token.NAME, "a"),
 new Node(Token.NUM, 5));
 Node assignment2 = new Node(Token.ASSIGN, rootNode, new Node(Token.NAME, "a"),
 new Node(Token.NAME, "b"));
 Node functionCall = new Node(Token.CALL, rootNode, new Node(Token.NAME, "func"),
 new Node(Token.NUM, 1));
 Node returnStatement = new Node(Token.RETURN, rootNode, new Node(Token.NAME, "a"));
 rootNode.addChild(assignment1);
 rootNode.addChild(assignment2);
 rootNode.addChild(functionCall);
 rootNode.addChild(returnStatement);
 analysis.flowThrough(rootNode, analysis.createInitialEstimateLattice());

 // When
 String variableName = "b";
 Node useNode = assignment2;

 // Then
 assertNull(analysis.getDef(variableName, useNode));
 }

 @Test
 public void testDoesNotGetDefForFunctionParameter() {
 // Given
 Scope jsScope = new Scope();
 AbstractCompiler compiler = new AbstractCompiler();
 ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Node(), new Node());
 MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
 Node rootNode = new Node(Token.BLOCK);
 Node function = new Node(Token.FUNCTION, rootNode, new Node(Token.NAME, "func"),
 new Node(Token.BLOCK));
 Node parameter = new Node(Token.VAR, function, new Node(Token.NAME, "x"));
 Node assignment1 = new Node(Token.ASSIGN, function, new Node(Token.NAME, "a"),
 new Node(Token.NUM, 5));
 rootNode.addChild(function);
 function.addChild(parameter);
 function.addChild(assignment1);
 analysis.flowThrough(rootNode, analysis.createInitialEstimateLattice());

 // When
 String variableName = "x";
 Node useNode = assignment1;

 // Then
 assertNull(analysis.getDef(variableName, useNode));
 }

 @Test
 public void testDoesNotGetDefForEscapedVariable() {
 // Given
 Scope jsScope = new Scope();
 AbstractCompiler compiler = new AbstractCompiler();
 ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Node(), new Node());
 MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
 Node rootNode = new Node(Token.BLOCK);
 Node assignment1 = new Node(Token.ASSIGN, rootNode, new Node(Token.NAME, "a"),
 new Node(Token.NUM, 5));
 Node assignment2 = new Node(Token.ASSIGN, rootNode, new Node(Token.NAME, "a"),
 new Node(Token.NAME, "b"));
 Node functionCall = new Node(Token.CALL, rootNode, new Node(Token.NAME, "func"),
 new Node(Token.NUM, 1));
 Node returnStatement = new Node(Token.RETURN, rootNode, new Node(Token.NAME, "a"));
 rootNode.addChild(assignment1);
 rootNode.addChild(assignment2);
 rootNode.addChild(functionCall);
 rootNode.addChild(returnStatement);
 jsScope.putVar("a", new Var(rootNode, new Node(Token.NAME, "a")));
 jsScope.getVar("a").setFlag("escaped", true);
 analysis.flowThrough(rootNode, analysis.createInitialEstimateLattice());

 // When
 String variableName = "a";
 Node useNode = assignment2;

 // Then
 assertNull(analysis.getDef(variableName, useNode));
 }

 @Test
 public void testDependsOnOuterScopeVariable() {
 // Given
 Scope jsScope = new Scope();
 AbstractCompiler compiler = new AbstractCompiler();
 ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Node(), new Node());
 MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
 Node rootNode = new Node(Token.BLOCK);
 Node outerScopeVariableDeclaration = new Node(Token.VAR, rootNode, new Node(Token.NAME, "outerScopeVariable"));
 Node assignment1 = new Node(Token.ASSIGN, rootNode, new Node(Token.NAME, "a"),
 new Node(Token.NUM, 5));
 Node assignment2 = new Node(Token.ASSIGN, rootNode, new Node(Token.NAME, "a"),
 new Node(Token.NAME, "outerScopeVariable"));
 Node functionCall = new Node(Token.CALL, rootNode, new Node(Token.NAME, "func"),
 new Node(Token.NUM, 1));
 Node returnStatement = new Node(Token.RETURN, rootNode, new Node(Token.NAME, "a"));
 rootNode.addChild(outerScopeVariableDeclaration);
 rootNode.addChild(assignment1);
 rootNode.addChild(assignment2);
 rootNode.addChild(functionCall);
 rootNode.addChild(returnStatement);
 analysis.flowThrough(rootNode, analysis.createInitialEstimateLattice());

 // When
 String variableName = "a";
 Node useNode = assignment2;

 // Then
 assertTrue(analysis.dependsOnOuterScopeVars(variableName, useNode));
 }

 @Test
 public void testDoesNotDependOnOuterScopeVariable() {
 // Given
 Scope jsScope = new Scope();
 AbstractCompiler compiler = new AbstractCompiler();
 ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Node(), new Node());
 MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
 Node rootNode = new Node(Token.BLOCK);
 Node assignment1 = new Node(Token.ASSIGN, rootNode, new Node(Token.NAME, "a"),
 new Node(Token.NUM, 5));
 Node assignment2 = new Node(Token.ASSIGN, rootNode, new Node(Token.NAME, "a"),
 new Node(Token.NUM, 6));
 Node functionCall = new Node(Token.CALL, rootNode, new Node(Token.NAME, "func"),
 new Node(Token.NUM, 1));
 Node returnStatement = new Node(Token.RETURN, rootNode, new Node(Token.NAME, "a"));
 rootNode.addChild(assignment1);
 rootNode.addChild(assignment2);
 rootNode.addChild(functionCall);
 rootNode.addChild(returnStatement);
 analysis.flowThrough(rootNode, analysis.createInitialEstimateLattice());

 // When
 String variableName = "a";
 Node useNode = assignment2;

 // Then
 assertFalse(analysis.dependsOnOuterScopeVars(variableName, useNode));
 }
}