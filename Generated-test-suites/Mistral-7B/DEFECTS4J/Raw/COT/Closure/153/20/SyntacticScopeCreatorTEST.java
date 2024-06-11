import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Token;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;

public class SyntacticScopeCreatorTest {

 private SyntacticScopeCreator creator;
 private AbstractCompiler compiler;
 private Scope scope;

 @Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 creator = new SyntacticScopeCreator(compiler);
 scope = new Scope(null, null);
 }

 @Test
 public void testCreateScope() {
 Scope result = creator.createScope(Node.newNode(Token.FUNCTION), null);
 assertNotNull(result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateScopeNullParent() {
 SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
 Scope result = creator.createScope(Node.newNode(Token.FUNCTION), null);
 }

 @Test
 public void testScanRootFunction() {
 Node functionNode = Node.newNode(Token.FUNCTION);
 Node fnNameNode = Node.newNode(Token.IDENTIFIER, "test");
 Node argumentsNode = Node.newNode(Token.LP);
 Node bodyNode = Node.newNode(Token.IDENTIFIER, "testBody");

 creator.scanRoot(functionNode, null);

 ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
 ArgumentCaptor<Scope> scopeCaptor = ArgumentCaptor.forClass(Scope.class);
 ArgumentCaptor<JSType> typeCaptor = ArgumentCaptor.forClass(JSType.class);
 ArgumentCaptor<Node> inputCaptor = ArgumentCaptor.forClass(Node.class);

 creator.redeclarationHandler.onRedeclaration(
     scopeCaptor.capture(), nameCaptor.capture(),
     nodeCaptor.capture(), null, null, null);
 assertNotNull(scopeCaptor.getValue());
 assertNotNull(scopeCaptor.getValue().getParent());
 assertNotNull(nameCaptor.getValue());
 assertNotNull(nodeCaptor.getValue());
 assertNull(typeCaptor.getValue());
 assertNull(inputCaptor.getValue());

 creator.declareVar(nameCaptor.getValue(), nodeCaptor.getValue(),
     functionNode, null, null, null);

 String test = nameCaptor.getValue();
 Node testNode = nodeCaptor.getValue();
 Scope parentScope = scopeCaptor.getValue().getParent();

 assertNotNull(test);
 assertNotNull(testNode);
 assertNull(parentScope);

 when(compiler.getInput(test)).thenReturn(Node.newTextNode(""));
 creator.scanVars(bodyNode, functionNode);

 Var testVar = scope.getVar(test);
 assertNotNull(testVar);
 assertSame(testNode, testVar.getNode());
 }

 @Test
 public void testScanRootGlobal() {
 Node globalNode = Node.newNode(Token.SCRIPT);
 creator.scanRoot(globalNode, null);
 }

 @Test
 public void testScanVarsFunctionExpression() {
 Node functionNode = Node.newNode(Token.FUNCTION,
     Node.newNode("test", Node.newNode(Token.LP)),
     Node.newNode(Token.RP));
 Node globalNode = Node.newNode(Token.SCRIPT);

 creator.scanVars(functionNode, globalNode);
 }

 @Test
 public void testScanVarsStatementBlock() {
 Node statementBlockNode = Node.newNode(Token.BLOCK_START, null);
 Node ifStatementNode = Node.newNode(Token.IF,
     Node.newNode(Token.LP),
     Node.newNode(Token.NAME, "test"),
     Node.newNode(Token.RP),
     Node.newNode(Token.BLOCK_START, null));
 Node varNode1 = Node.newNode(Token.VAR,
     Node.newNode(Token.NAME, "test1"),
     Node.newNode(Token.ASSIGN,
         Node.newNode(Token.NAME, "test1"),
         Node.newNode(Token.NUMBER, 1)));
 Node varNode2 = Node.newNode(Token.VAR,
     Node.newNode(Token.NAME, "test2"),
     Node.newNode(Token.ASSIGN,
         Node.newNode(Token.NAME, "test2"),
         Node.newNode(Token.NUMBER, 2)));
 statementBlockNode.appendChild(ifStatementNode);
 ifStatementNode.appendChild(varNode1);
 ifStatementNode.appendChild(varNode2);

 Node globalNode = Node.newNode(Token.SCRIPT);
 creator.scanVars(statementBlockNode, globalNode);
 }

 @Test
 public void testScanVarsControlStructure() {
 Node ifStatementNode = Node.newNode(Token.IF,
     Node.newNode(Token.LP),
     Node.newNode(Token.NAME, "test"),
     Node.newNode(Token.RP),
     Node.newNode(Token.BLOCK_START, null));
 Node varNode = Node.newNode(Token.VAR,
     Node.newNode(Token.NAME, "test"),
     Node.newNode(Token.ASSIGN,
         Node.newNode(Token.NAME, "test"),
         Node.newNode(Token.NUMBER, 1)));
 ifStatementNode.appendChild(varNode);

 Node globalNode = Node.newNode(Token.SCRIPT);
 creator.scanVars(ifStatementNode, globalNode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testScanVarsNullParent() {
 Node statementBlockNode = Node.newNode(Token.BLOCK_START, null);

 creator.scanVars(statementBlockNode, null);
 }

 @Test
 public void testDeclareVarDuplicateGlobal() {
 Node declarationNode1 = Node.newNode(Token.VAR,
     Node.newNode(Token.NAME, "test"),
     Node.newNode(Token.ASSIGN,
         Node.newNode(Token.NAME, "test"),
         Node.newNode(Token.NUMBER, 1)));
 Node declarationNode2 = Node.newNode(Token.VAR,
     Node.newNode(Token.NAME, "test"),
     Node.newNode(Token.ASSIGN,
         Node.newNode(Token.NAME, "test"),
         Node.newNode(Token.NUMBER, 2)));
 Node globalNode = Node.newNode(Token.SCRIPT);

 creator.declareVar("test", declarationNode1, null, null, null, null);
 creator.declareVar("test", declarationNode2, null, null, null, null);
 }

 @Test
 public void testDeclareVarDuplicateLocal() {
 Node functionNode = Node.newNode(Token.FUNCTION,
     Node.newNode("test"),
     Node.newNode(Token.LP),
     Node.newNode(Token.RP));
 Node declarationNode1 = Node.newNode(Token.VAR,
     Node.newNode(Token.NAME, "test"),
     Node.newNode(Token.ASSIGN,
         Node.newNode(Token.NAME, "test"),
         Node.newNode(Token.NUMBER, 1)));
 Node declarationNode2 = Node.newNode(Token.VAR,
     Node.newNode(Token.NAME, "test"),
     Node.newNode(Token.ASSIGN,
         Node.newNode(Token.NAME, "test"),
         Node.newNode(Token.NUMBER, 2)));
 Node bodyNode = Node.newNode(Token.BLOCK_START, null);

 creator.declareVar("test", declarationNode1, functionNode, null, null, null);
 creator.declareVar("test", declarationNode2, bodyNode, functionNode, null, null);
 }

 @Test
 public void testDeclareVarDuplicateCatch() {
 Node functionNode = Node.newNode(Token.FUNCTION,
     Node.newNode("test"),
     Node.newNode(Token.LP),
     Node.newNode(Token.RP));
 Node catchNode = Node.newNode(Token.CATCH,
     Node.newNode(Token.NAME, "test"),
     Node.newNode(Token.BLOCK_START, null));
 Node declarationNode1 = Node.newNode(Token.VAR,
     Node.newNode(Token.NAME, "test"),
     Node.newNode(Token.ASSIGN,
         Node.newNode(Token.NAME, "test"),
         Node.newNode(Token.NUMBER, 1)));
 Node declarationNode2 = Node.newNode(Token.VAR,
     Node.newNode(Token.NAME, "test"),
     Node.newNode(Token.ASSIGN,
         Node.newNode(Token.NAME, "test"),
         Node.newNode(Token.NUMBER, 2)));
 Node bodyNode = Node.newNode(Token.BLOCK_START, null);

 creator.declareVar("test", declarationNode1, catchNode, functionNode, null, null);
 creator.declareVar("test", declarationNode2, bodyNode, functionNode, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeclareVarNullParent() {
 Node declarationNode = Node.newNode(Token.VAR,
     Node.newNode(Token.NAME, "test"),
     Node.newNode(Token.ASSIGN,
         Node.newNode(Token.NAME, "test"),
         Node.newNode(Token.NUMBER, 1)));

 creator.declareVar("test", declarationNode, null, null, null, null);
 }

 @Test
 public void testDeclareVarRedefinition() {
 Node functionNode = Node.newNode(Token.FUNCTION,
     Node.newNode("test"),
     Node.newNode(Token.LP),
     Node.newNode(Token.RP));
 Node declarationNode1 = Node.newNode(Token.VAR,
     Node.newNode(Token.NAME, "test"),
     Node.newNode(Token.ASSIGN,
         Node.newNode(Token.NAME, "test"),
         Node.newNode(Token.NUMBER, 1)));
 Node declarationNode2 = Node.newNode(Token.VAR,
     Node.newNode(Token.NAME, "test"),
     Node.newNode(Token.ASSIGN,
         Node.newNode(Token.NAME, "test"),
         Node.newNode(Token.NUMBER, 2)));
 Node bodyNode = Node.newNode(Token.BLOCK_START, null);

 creator.declareVar("test", declarationNode1, functionNode, null, null, null);
 creator.declareVar("test", declarationNode2, bodyNode, functionNode, null, null);

 JSError error = creator.compiler.getErrors().get(0);
 DiagnosticType errorType = error.getType();
 String errorMessage = error.getMessage();

 assertEquals(VAR_MULTIPLY_DECLARED_ERROR, errorType);
 assertEquals("test first declared in test", errorMessage);
 }

 @Test
 public void testDeclareVarRedefinitionGlobal() {
 Node declarationNode1 = Node.newNode(Token.VAR,
     Node.newNode(Token.NAME, "test"),
     Node.newNode(Token.ASSIGN,
         Node.newNode(Token.NAME, "test"),
         Node.newNode(Token.NUMBER, 1)));
 Node declarationNode2 = Node.newNode(Token.VAR,
     Node.newNode(Token.NAME, "test"),
     Node.newNode(Token.ASSIGN,
         Node.newNode(Token.NAME, "test"),
         Node.newNode(Token.NUMBER, 2)));
 Node globalNode = Node.newNode(Token.SCRIPT);

 creator.declareVar("test", declarationNode1, null, null, null, null);
 creator.declareVar("test", declarationNode2, null, null, null, null);

 JSError error = creator.compiler.getErrors().get(0);
 DiagnosticType errorType = error.getType();
 String errorMessage = error.getMessage();

 assertEquals(VAR_MULTIPLY_DECLARED_ERROR, errorType);
 assertEquals("test first declared in test", errorMessage);
 }

 @Test
 public void testDeclareVarRedefinitionLocal() {
 Node functionNode = Node.newNode(Token.FUNCTION,
     Node.newNode("test"),
     Node.newNode(Token.LP),
     Node.newNode(Token.RP));
 Node declarationNode1 = Node.newNode(Token.VAR,
     Node.newNode(Token.NAME, "test"),
     Node.newNode(Token.ASSIGN,
         Node.newNode(Token.NAME, "test"),
         Node.newNode(Token.NUMBER, 1)));
 Node declarationNode2 = Node.newNode(Token.VAR,
     Node.newNode(Token.NAME, "test"),
     Node.newNode(Token.ASSIGN,
         Node.newNode(Token.NAME, "test"),
         Node.newNode(Token.NUMBER, 2)));
 Node bodyNode = Node.newNode(Token.BLOCK_START, null);

 creator.declareVar("test", declarationNode1, functionNode, null, null, null);
 creator.declareVar("test", declarationNode2, bodyNode, functionNode, null, null);

 JSError error = creator.compiler.getErrors().get(0);
 DiagnosticType errorType = error.getType();
 String errorMessage = error.getMessage();

 assertEquals(VAR_MULTIPLY_DECLARED_ERROR, errorType);
 assertEquals("test first declared in test", errorMessage);
 }

 @Test
 public void testDeclareVarRedefinitionCatch() {
 Node functionNode = Node.newNode(Token.FUNCTION,
     Node.newNode("test"),
     Node.newNode(Token.LP),
     Node.newNode(Token.RP));
 Node catchNode = Node.newNode(Token.CATCH,
     Node.newNode(Token.NAME, "test"),
     Node.newNode(Token.BLOCK_START, null));
 Node declarationNode1 = Node.newNode(Token.VAR,
     Node.newNode(Token.NAME, "test"),
     Node.newNode(Token.ASSIGN,
         Node.newNode(Token.NAME, "test"),
         Node.newNode(Token.NUMBER, 1)));
 Node declarationNode2 = Node.newNode(Token.VAR,
     Node.newNode(Token.NAME, "test"),
     Node.newNode(Token.ASSIGN,
         Node.newNode(Token.NAME, "test"),
         Node.newNode(Token.NUMBER, 2)));
 Node bodyNode = Node.newNode(Token.BLOCK_START, null);

 creator.declareVar("test", declarationNode1, catchNode, functionNode, null, null);
 creator.declareVar("test", declarationNode2, bodyNode, functionNode, null, null);

 JSError error = creator.compiler.getErrors().get(0);
 DiagnosticType errorType = error.getType();
 String errorMessage = error.getMessage();

 assertEquals(VAR_MULTIPLY_DECLARED_ERROR, errorType);
 assertEquals("test first declared in test", errorMessage);
 }

 @Test
 public void testDeclareVarArgument() {
 Node functionNode = Node.newNode(Token.FUNCTION,
     Node.newNode("test"),
     Node.newNode(Token.LP),
     Node.newNode(Token.RP));
 Node declarationNode = Node.newNode(Token.VAR,
     Node.newNode(Token.NAME, "arguments"),
     Node.newNode(Token.ASSIGN,
         Node.newNode(Token.NAME, "arguments"),
         Node.newNode(Token.IDENTIFIER, "testArguments")));
 Node bodyNode = Node.newNode(Token.BLOCK_START, null);

 creator.declareVar("arguments", declarationNode, functionNode, null, null, null);
 creator.declareVar("arguments", declarationNode, bodyNode, functionNode, null, null);

 JSError error = creator.compiler.getErrors().get(0);
 DiagnosticType errorType = error.getType();
 String errorMessage = error.getMessage();

 assertEquals(VAR_ARGUMENTS_SHADOWED_ERROR, errorType);
 assertEquals("Shadowing \"arguments\" is not allowed", errorMessage);
 }
}