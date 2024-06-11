import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.JSType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.JSDocInfo;

public class SyntacticScopeCreatorTest {

  @SuppressWarnings("unchecked")
  private static final ScopeCreator SYNTACTIC_SCOPE_CREATOR =
      new SyntacticScopeCreator(new NoOpCompiler());

  private static final AbstractCompiler NO_OP_COMPILER = new NoOpCompiler();

  @SuppressWarnings("unchecked")
  private static final ScopeCreator SYNTAX_SCOPE_CREATOR_WITH_HANDLER =
      new SyntacticScopeCreator(
          NO_OP_COMPILER, new StrictRedeclarationHandler());

  private TestSourceFile sourceFile;
  private Node parseTree;

  @Before
  public void setUp() {
    sourceFile = new TestSourceFile("TestFile.js");
  }

  @BeforeMethod
  public void setupForTest() {
    parseTree = sourceFile.parse();
  }

  // Basic test cases for each method -----------------------------------------

  @Test
  public void testCreateScope() {
    Scope parentScope = SYNTAX_SCOPE_CREATOR.createScope(parseTree, null);
    assertNotNull(parentScope);
  }

  @Test
  public void testCreateScopeWithParent() {
    Scope parentScope = new Scope(null, parseTree);
    Scope childScope = SYNTAX_SCOPE_CREATOR.createScope(parseTree, parentScope);
    assertNotNull(childScope);
    assertEquals(parentScope, childScope.getParent());
  }

  @Test
  public void testScanRoot() {
    Scope globalScope = SYNTAX_SCOPE_CREATOR.createScope(parseTree, null);
    Node functionNode = parseTree.getFirstChild();
    Scope functionScope = SYNTAX_SCOPE_CREATOR.createScope(functionNode, globalScope);
    assertEquals(globalScope, functionScope.getParent());
    // Function name is not declared in the outer scope, so it is declared here
    assertTrue(functionScope.containsVar("funcName"));
  }

  @Test
  public void testScanVars() {
    Node varNodeList = parseTree.getFirstChild().getNext();
    Scope parentScope = new Scope(null, parseTree);
    Scope childScope = SYNTAX_SCOPE_CREATOR.createScope(varNodeList, parentScope);
    assertEquals(parentScope, childScope.getParent());

    for (Node varNode = varNodeList; varNode != null; varNode = varNode.getNext()) {
      assertTrue(childScope.containsVar(varNode.getString()));
    }
  }

  // Test for edge cases and exception handling scenarios -----------------------

  @Test(expected = IllegalArgumentException.class)
  public void testCreateScopeWithNullCompiler() {
    new SyntacticScopeCreator(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateScopeWithNullParentScope() {
    new SyntacticScopeCreator(NO_OP_COMPILER, null).createScope(parseTree, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateScopeWithNullParseTree() {
    new SyntacticScopeCreator(NO_OP_COMPILER, null).createScope(null, null);
  }

  @Test
  public void testRedeclarationHandler() {
    String varName = "testVariable";
    Node varNode = new Node(Token.VAR, "var", null);
    varNode.setLineNumber(1);
    Node parentNode = new Node(Token.BLOCK, null, null);
    parentNode.setLineNumber(1);
    Node declarationNode = new Node(Token.NAME, varName, null);
    declarationNode.setLineNumber(1);
    declarationNode.setParent(parentNode);
    Node redeclarationNode = new Node(Token.VAR, "var", null);
    redeclarationNode.setLineNumber(2);
    redeclarationNode.setParent(parentNode);
    Node grampsNode = new Node(Token.BLOCK, null, null);
    grampsNode.setLineNumber(1);
    Node nodeWithLineNumber = new Node(Token.BLOCK, null, null);
    nodeWithLineNumber.setLineNumber(2);

    Scope parentScope = new Scope(null, null);
    Scope childScope = new Scope(null, null);
    SyntacticScopeCreator.DefaultRedeclarationHandler handler = new SyntacticScopeCreator.DefaultRedeclarationHandler();

    handler.onRedeclaration(parentScope, varName, varNode, parentNode, grampsNode, nodeWithLineNumber);

    // Test for duplicate variables in the global scope
    Scope globalScope = new Scope(null, null);
    parentScope = globalScope;
    childScope = new Scope(null, null);
    handler.onRedeclaration(parentScope, varName, declarationNode, parentNode, grampsNode, nodeWithLineNumber);
    handler.onRedeclaration(childScope, varName, redeclarationNode, parentNode, grampsNode, nodeWithLineNumber);

    assertFalse(globalScope.isDeclared(varName));

    // Test for redeclaration of function argument
    Node functionNode = new Node(Token.FUNCTION, null, null);
    Node argsNode = functionNode.getFirstChild();
    declarationNode = argsNode.getNext();
    declarationNode.setString(varName);
    redeclarationNode = argsNode.getNext();
    redeclarationNode.setType(Token.VAR);
    redeclarationNode.setString(varName);

    parentScope = new Scope(null, null);
    childScope = new Scope(null, null);

    handler.onRedeclaration(parentScope, varName, declarationNode, functionNode, null, nodeWithLineNumber);
    handler.onRedeclaration(childScope, varName, redeclarationNode, functionNode, null, nodeWithLineNumber);

    assertFalse(childScope.isDeclared(varName));

    // Test for redeclaration of function name
    declarationNode.setType(Token.NAME);
    declarationNode.setString("funcName");
    redeclarationNode.setType(Token.NAME);
    redeclarationNode.setString("funcName");

    parentScope = new Scope(null, null);
    childScope = new Scope(null, null);

    handler.onRedeclaration(parentScope, "funcName", declarationNode, functionNode, null, nodeWithLineNumber);
    handler.onRedeclaration(childScope, "funcName", redeclarationNode, functionNode, null, nodeWithLineNumber);

    assertFalse(childScope.isDeclared("funcName"));

    // Test for redeclaration of global variable with JSDoc
    Node jsDocNode = new Node(Token.COMMENT, null, null);
    jsDocNode.setLineNumber(1);
    jsDocNode.setString("/** @type {number} */");

    declarationNode.setParent(jsDocNode);
    redeclarationNode.setParent(jsDocNode);

    parentScope = new Scope(null, null);
    childScope = new Scope(null, null);

    handler.onRedeclaration(parentScope, varName, declarationNode, parentNode, grampsNode, nodeWithLineNumber);
    handler.onRedeclaration(childScope, varName, redeclarationNode, parentNode, grampsNode, nodeWithLineNumber);

    JSDocInfo jsDocInfo = declarationNode.getJSDocInfo();
    assertTrue(jsDocInfo != null);
    assertFalse(childScope.isDeclared(varName));
  }

  // Test for duplicate variables in function scope
  @Test
  public void testDuplicateVariablesInFunctionScope() {
    String varName = "testVariable";
    Node varNode = new Node(Token.VAR, "var", null);
    varNode.setLineNumber(1);
    Node parentNode = new Node(Token.BLOCK, null, null);
    parentNode.setLineNumber(1);
    Node declarationNode = new Node(Token.NAME, varName, null);
    declarationNode.setLineNumber(1);
    declarationNode.setParent(parentNode);
    Node redeclarationNode = new Node(Token.VAR, "var", null);
    redeclarationNode.setLineNumber(2);
    redeclarationNode.setParent(parentNode);
    Node functionNode = new Node(Token.FUNCTION, null, null);
    Node functionScope = new Scope(functionNode, parentNode);
    Scope parentScope = new Scope(null, null);

    SyntacticScopeCreator.DefaultRedeclarationHandler handler = new SyntacticScopeCreator.DefaultRedeclarationHandler();

    handler.onRedeclaration(parentScope, varName, declarationNode, parentNode, null, nodeWithLineNumber);
    handler.onRedeclaration(functionScope, varName, redeclarationNode, parentNode, null, nodeWithLineNumber);

    assertTrue(functionScope.isDeclared(varName));
  }

  // Test for redeclaration of function argument
  @Test
  public void testReddeclarationOfFunctionArgument() {
    String argName = "testArg";
    Node varNode = new Node(Token.VAR, "var", null);
    varNode.setLineNumber(1);
    Node parentNode = new Node(Token.BLOCK, null, null);
    parentNode.setLineNumber(1);
    Node declarationNode = new Node(Token.NAME, argName, null);
    declarationNode.setLineNumber(1);
    declarationNode.setParent(parentNode);
    Node redeclarationNode = new Node(Token.VAR, "var", null);
    redeclarationNode.setLineNumber(2);
    redeclarationNode.setParent(parentNode);
    Node argsNode = new Node(Token.LP, null, null);
    Node functionNode = new Node(Token.FUNCTION, null, argsNode);
    Node functionScope = new Scope(functionNode, parentNode);
    Scope parentScope = new Scope(null, null);

    SyntacticScopeCreator.DefaultRedeclarationHandler handler = new SyntacticScopeCreator.DefaultRedeclarationHandler();

    handler.onRedeclaration(parentScope, argName, declarationNode, parentNode, null, nodeWithLineNumber);
    handler.onRedeclaration(functionScope, argName, redeclarationNode, parentNode, null, nodeWithLineNumber);

    assertTrue(functionScope.isDeclared(argName));
  }

  // Test for redeclaration of function name
  @Test
  public void testReddeclarationOfFunctionName() {
    String funcName = "testFunc";
    Node jsDocNode = new Node(Token.COMMENT, null, null);
    jsDocNode.setLineNumber(1);
    Node jsDocInfo = new JSFunctionType(null, null);
    jsDocNode.setJSDocInfo(jsDocInfo);
    Node declarationNode = new Node(Token.NAME, funcName, null);
    declarationNode.setLineNumber(1);
    declarationNode.setParent(jsDocNode);
    Node redeclarationNode = new Node(Token.NAME, funcName, null);
    redeclarationNode.setLineNumber(2);
    redeclarationNode.setParent(jsDocNode);
    Node functionNode = new Node(Token.FUNCTION, null, null);
    Node functionScope = new Scope(functionNode, null);
    Scope parentScope = new Scope(null, null);

    SyntacticScopeCreator.DefaultRedeclarationHandler handler = new SyntacticScopeCreator.DefaultRedeclarationHandler();

    handler.onRedeclaration(parentScope, funcName, declarationNode, functionNode, null, nodeWithLineNumber);
    handler.onRedeclaration(functionScope, funcName, redeclarationNode, functionNode, null, nodeWithLineNumber);

    assertTrue(functionScope.isDeclared(funcName));
  }

  // Test for redeclaration of global variable with JSDoc
  @Test
  public void testReddeclarationOfGlobalVariableWithJSDoc() {
    String varName = "testVariable";
    Node varNode = new Node(Token.VAR, "var", null);
    varNode.setLineNumber(1);
    Node parentNode = new Node(Token.BLOCK, null, null);
    parentNode.setLineNumber(1);
    Node declarationNode = new Node(Token.NAME, varName, null);
    declarationNode.setLineNumber(1);
    declarationNode.setParent(parentNode);
    Node jsDocNode = new Node(Token.COMMENT, null, null);
    jsDocNode.setLineNumber(1);
    Node jsDocInfo = new JSNumberType(null, null);
    jsDocNode.setJSDocInfo(jsDocInfo);
    declarationNode.setParent(jsDocNode);
    Node redeclarationNode = new Node(Token.VAR, "var", null);
    redeclarationNode.setLineNumber(2);
    redeclarationNode.setParent(parentNode);
    Node redeclarationJsDocNode = new Node(Token.COMMENT, null, null);
    redeclarationJsDocNode.setLineNumber(2);
    Node redeclarationJsDocInfo = new JSNumberType(null, null);
    redeclarationJsDocNode.setJSDocInfo(redeclarationJsDocInfo);
    redeclarationNode.setParent(redeclarationJsDocNode);
    Node grampsNode = new Node(Token.BLOCK, null, null);
    grampsNode.setLineNumber(1);
    Node nodeWithLineNumber = new Node(Token.BLOCK, null, null);
    nodeWithLineNumber.setLineNumber(2);

    Scope parentScope = new Scope(null, null);
    Scope childScope = new Scope(null, null);

    SyntacticScopeCreator.DefaultRedeclarationHandler handler = new SyntacticScopeCreator.DefaultRedeclarationHandler();

    handler.onRedeclaration(parentScope, varName, declarationNode, parentNode, grampsNode, nodeWithLineNumber);
    handler.onRedeclaration(childScope, varName, redeclarationNode, parentNode, grampsNode, nodeWithLineNumber);

    JSDocInfo jsDocInfo = declarationNode.getJSDocInfo();
    JSDocInfo redeclarationJsDocInfo = redeclarationNode.getJSDocInfo();
    assertTrue(jsDocInfo != null && redeclarationJsDocInfo != null);
    assertEquals(jsDocInfo.getType(), redeclarationJsDocInfo.getType());
    assertTrue(childScope.isDeclared(varName));
  }

  @AfterMethod
  public void tearDown() {
    sourceFile.dispose();
  }
}

class NoOpCompiler implements AbstractCompiler {
  @Override
  public void report(DiagnosticType diagnosticType, Node node, String message,
                    Object... args) {
  }

  @Override
  public Input getInput(String sourceName) {
    return null;
  }

  @Override
  public void addSourceFile(String fileName) {
  }
}

class StrictRedeclarationHandler implements SyntacticScopeCreator.RedeclarationHandler {
  @Override
  public void onRedeclaration(
      Scope s, String name, Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
    throw new RuntimeException("Reddeclaration is not allowed.");
  }
}