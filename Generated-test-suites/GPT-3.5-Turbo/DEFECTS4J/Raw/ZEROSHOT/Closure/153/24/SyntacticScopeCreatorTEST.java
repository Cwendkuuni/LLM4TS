import com.google.common.base.Preconditions;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;
import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {
  private SyntacticScopeCreator scopeCreator;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    scopeCreator = new SyntacticScopeCreator(compiler);
  }

  @Test
  public void testCreateScope_GlobalBlock() {
    Node root = new Node(Token.SCRIPT);
    Scope scope = scopeCreator.createScope(root, null);
    assertNotNull(scope);
    assertNull(scope.getParent());
    assertNull(scope.getParentScope());
    assertEquals(0, scope.getDepth());
  }

  @Test
  public void testCreateScope_FunctionDeclaration() {
    Node root = new Node(Token.FUNCTION);
    Scope parent = new Scope(null, null);
    Scope scope = scopeCreator.createScope(root, parent);
    assertNotNull(scope);
    assertNull(scope.getParent());
    assertEquals(parent, scope.getParentScope());
    assertEquals(parent.getDepth() + 1, scope.getDepth());
  }

  @Test
  public void testScanVars_VarDeclaration() {
    Node root = new Node(Token.SCRIPT);
    Node varNode = new Node(Token.VAR);
    varNode.addChildToBack(new Node(Token.NAME, "x"));
    varNode.addChildToBack(new Node(Token.NAME, "y"));
    root.addChildToBack(varNode);
    Scope scope = new Scope(null, null);
    scopeCreator.scanVars(root, null);
    assertTrue(scope.isDeclared("x", false));
    assertTrue(scope.isDeclared("y", false));
  }

  @Test
  public void testScanVars_FunctionDeclaration() {
    Node root = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToBack(new Node(Token.NAME, "foo"));
    root.addChildToBack(functionNode);
    Scope scope = new Scope(null, null);
    scopeCreator.scanVars(root, null);
    assertTrue(scope.isDeclared("foo", false));
  }

  @Test
  public void testScanVars_CatchDeclaration() {
    Node root = new Node(Token.SCRIPT);
    Node catchNode = new Node(Token.CATCH);
    catchNode.addChildToBack(new Node(Token.NAME, "e"));
    root.addChildToBack(catchNode);
    Scope scope = new Scope(null, null);
    scopeCreator.scanVars(root, null);
    assertTrue(scope.isDeclared("e", false));
  }

  @Test
  public void testScanVars_ControlStructure() {
    Node root = new Node(Token.SCRIPT);
    Node ifNode = new Node(Token.IF);
    Node nameNode = new Node(Token.NAME, "x");
    ifNode.addChildToBack(nameNode);
    root.addChildToBack(ifNode);
    Scope scope = new Scope(null, null);
    scopeCreator.scanVars(root, null);
    assertTrue(scope.isDeclared("x", false));
  }

  @Test
  public void testDeclareVar_NewVariable() {
    Scope scope = new Scope(null, null);
    Node varNode = new Node(Token.NAME, "x");
    Node parentNode = new Node(Token.VAR);
    Node grampsNode = new Node(Token.SCRIPT);
    JSType declaredType = null;
    Node nodeWithLineNumber = new Node(Token.NAME, "x");
    scopeCreator.declareVar("x", varNode, parentNode, grampsNode, declaredType, nodeWithLineNumber);
    assertTrue(scope.isDeclared("x", false));
  }

  @Test
  public void testDeclareVar_RedeclaredVariable() {
    Scope scope = new Scope(null, null);
    Node varNode = new Node(Token.NAME, "x");
    Node parentNode = new Node(Token.VAR);
    Node grampsNode = new Node(Token.SCRIPT);
    JSType declaredType = null;
    Node nodeWithLineNumber = new Node(Token.NAME, "x");

    // Declare x one time
    scopeCreator.declareVar("x", varNode, parentNode, grampsNode, declaredType, nodeWithLineNumber);

    // Try to declare x again
    scopeCreator.declareVar("x", varNode, parentNode, grampsNode, declaredType, nodeWithLineNumber);
    // Check if the error is reported correctly
    assertNull(scope.getParent());
    assertEquals(parent, scope.getParentScope());
    assertEquals(parent.getDepth() + 1, scope.getDepth());
  }

  @Test
  public void testDeclareVar_RedeclarationWithSuppressions() {
    Scope scope = new Scope(null, null);
    Node varNode = new Node(Token.NAME, "x");
    Node parentNode = new Node(Token.VAR);
    Node grampsNode = new Node(Token.SCRIPT);
    JSType declaredType = null;
    Node nodeWithLineNumber = new Node(Token.NAME, "x");

    // Declare x one time
    scopeCreator.declareVar("x", varNode, parentNode, grampsNode, declaredType, nodeWithLineNumber);

    // Try to declare x again with suppression
    JSDocInfo.Builder builder = new JSDocInfo.Builder();
    builder.addSuppression("duplicate");
    varNode.setJSDocInfo(builder.build());

    scopeCreator.declareVar("x", varNode, parentNode, grampsNode, declaredType, nodeWithLineNumber);

    assertNull(scope.getParent());
    assertEquals(parent, scope.getParentScope());
    assertEquals(parent.getDepth() + 1, scope.getDepth());
  }

  @Test
  public void testDeclareVar_ReduplicateCatchVariable() {
    Scope scope = new Scope(null, null);
    Node varNode = new Node(Token.NAME, "x");
    Node parentNode = new Node(Token.VAR);
    Node grampsNode = new Node(Token.SCRIPT);
    JSType declaredType = null;
    Node nodeWithLineNumber = new Node(Token.NAME, "x");

    // Declare x once
    scopeCreator.declareVar("x", varNode, parentNode, grampsNode, declaredType, nodeWithLineNumber);

    // Declare x in a catch block
    Scope parent = new Scope(null, null);
    Node catchNode = new Node(Token.CATCH);
    Node catchVarNode = new Node(Token.NAME, "x");
    Node catchBlockNode = new Node(Token.BLOCK);
    catchNode.addChildToBack(catchVarNode);
    catchNode.addChildToBack(catchBlockNode);
    parentNode.addChildToBack(catchNode);

    scopeCreator.scanVars(parentNode, null);
    scopeCreator.scanVars(catchBlockNode, catchNode);

    // Check if no error is reported
    assertNull(scope.getParent());
    assertEquals(parent, scope.getParentScope());
    assertEquals(parent.getDepth() + 1, scope.getDepth());
  }

  @Test
  public void testDeclareVar_GlobalArgumentsVariable() {
    Scope scope = new Scope(null, null);
    Node varNode = new Node(Token.NAME, "arguments");
    Node parentNode = new Node(Token.VAR);
    Node grampsNode = new Node(Token.SCRIPT);
    JSType declaredType = null;
    Node nodeWithLineNumber = new Node(Token.NAME, "arguments");

    // Declare arguments at global scope
    scopeCreator.declareVar("arguments", varNode, parentNode, grampsNode, declaredType, nodeWithLineNumber);

    // Try to declare arguments within a function
    parentNode = new Node(Token.FUNCTION);
    Scope parent = new Scope(null, null);
    scopeCreator.scanRoot(parentNode, parent);

    scopeCreator.declareVar("arguments", varNode, parentNode, grampsNode, declaredType, nodeWithLineNumber);
    // Check if the error is reported correctly
    assertNull(scope.getParent());
    assertEquals(parent, scope.getParentScope());
    assertEquals(parent.getDepth() + 1, scope.getDepth());
  }

  @Test
  public void testDeclareVar_FunctionDeclaration() {
    Scope scope = new Scope(null, null);
    Node varNode = new Node(Token.NAME, "foo");
    Node parentNode = new Node(Token.FUNCTION);
    Node grampsNode = new Node(Token.SCRIPT);
    JSType declaredType = null;
    Node nodeWithLineNumber = new Node(Token.NAME, "foo");
    scopeCreator.declareVar("foo", varNode, parentNode, grampsNode, declaredType, nodeWithLineNumber);
    assertTrue(scope.isDeclared("foo", false));
  }
}