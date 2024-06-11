import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class SyntacticScopeCreatorTest {
  
  private static AbstractCompiler compiler;
  private static SyntacticScopeCreator scopeCreator;
  
  @BeforeClass
  public static void setUp() {
    compiler = new AbstractCompiler();
    scopeCreator = new SyntacticScopeCreator(compiler);
  }
  
  @Test
  public void testCreateScope_FunctionDeclaration() {
    Node rootNode = new Node(Token.FUNCTION);
    rootNode.putProp(Node.SOURCENAME_PROP, "testFile.js");
    
    Node fnNameNode = new Node(Token.STRING);
    fnNameNode.setString("foo");
    
    Node argsNode = new Node(Token.LP);
    Node argNode = new Node(Token.NAME);
    argNode.setString("x");
    argsNode.addChildToBack(argNode);
    
    Node bodyNode = new Node(Token.BLOCK);
    
    rootNode.addChildToFront(fnNameNode);
    rootNode.addChildToBack(argsNode);
    rootNode.addChildToBack(bodyNode);
    
    Scope scope = scopeCreator.createScope(rootNode, null);
    
    assertNotNull(scope);
    assertNotNull(scope.getRootNode());
    assertNotNull(scope.getParent());
    assertEquals("testFile.js", scope.getScopeRootSourceName());
  }
  
  @Test
  public void testCreateScope_FunctionExpression() {
    Node rootNode = new Node(Token.FUNCTION);
    rootNode.putProp(Node.SOURCENAME_PROP, "testFile.js");
    
    Node fnNameNode = new Node(Token.EMPTY);
    
    Node argsNode = new Node(Token.LP);
    Node argNode = new Node(Token.NAME);
    argNode.setString("x");
    argsNode.addChildToBack(argNode);
    
    Node bodyNode = new Node(Token.BLOCK);
    
    rootNode.addChildToFront(fnNameNode);
    rootNode.addChildToBack(argsNode);
    rootNode.addChildToBack(bodyNode);
    
    Scope scope = scopeCreator.createScope(rootNode, null);
    
    assertNotNull(scope);
    assertNotNull(scope.getRootNode());
    assertNotNull(scope.getParent());
    assertEquals("testFile.js", scope.getScopeRootSourceName());
  }
  
  @Test
  public void testCreateScope_GlobalBlock() {
    Node rootNode = new Node(Token.SCRIPT);
    rootNode.putProp(Node.SOURCENAME_PROP, "testFile.js");
    
    Scope scope = scopeCreator.createScope(rootNode, null);
    
    assertNotNull(scope);
    assertNotNull(scope.getRootNode());
    assertNull(scope.getParent());
    assertEquals("testFile.js", scope.getScopeRootSourceName());
  }
  
  @Test
  public void testScanVars_VarDeclaration_WithInitializers() {
    Node rootNode = new Node(Token.VAR);
    Node child1 = new Node(Token.NAME);
    child1.setString("x");
    rootNode.addChildToFront(child1);
    Node child2 = new Node(Token.NAME);
    child2.setString("y");
    rootNode.addChildToBack(child2);
    
    Scope scope = new Scope();
    scopeCreator.scanVars(rootNode, null);
    
    assertTrue(scope.isDeclared("x", false));
    assertTrue(scope.isDeclared("y", false));
    assertFalse(scope.isDeclared("z", false));
  }
  
  @Test
  public void testScanVars_FunctionDeclaration_WithFunctionName() {
    Node rootNode = new Node(Token.FUNCTION);
    Node fnNameNode = new Node(Token.STRING);
    fnNameNode.setString("foo");
    rootNode.addChildToFront(fnNameNode);
    
    Scope scope = new Scope();
    scopeCreator.scanVars(rootNode, null);
    
    assertTrue(scope.isDeclared("foo", false));
  }
  
  @Test
  public void testScanVars_FunctionDeclaration_NoFunctionName() {
    Node rootNode = new Node(Token.FUNCTION);
    Node fnNameNode = new Node(Token.EMPTY);
    rootNode.addChildToFront(fnNameNode);
    
    Scope scope = new Scope();
    scopeCreator.scanVars(rootNode, null);
    
    assertFalse(scope.isDeclared("", false));
  }
  
  @Test
  public void testScanVars_CatchBlock() {
    Node rootNode = new Node(Token.CATCH);
    Node varNode = new Node(Token.NAME);
    varNode.setString("e");
    rootNode.addChildToFront(varNode);
    Node blockNode = new Node(Token.BLOCK);
    rootNode.addChildToBack(blockNode);
    
    Scope scope = new Scope();
    scopeCreator.scanVars(rootNode, null);
    
    assertTrue(scope.isDeclared("e", false));
  }
  
  @Test
  public void testScanVars_GlobalBlock() {
    Node rootNode = new Node(Token.SCRIPT);
    
    Scope scope = new Scope();
    scopeCreator.scanVars(rootNode, null);
    
    assertFalse(scope.isDeclared("x", false));
  }
  
  @Test
  public void testDeclareVar_NewDeclaration() {
    Scope scope = new Scope();
    Node n = new Node(Token.NAME);
    n.setString("x");
    
    scopeCreator.declareVar("x", n, null, null, null, null);
    
    assertTrue(scope.isDeclared("x", false));
  }
  
  @Test
  public void testDeclareVar_DuplicateDeclaration_NoSuppression() {
    Scope scope = new Scope();
    Node n1 = new Node(Token.NAME);
    n1.setString("x");
    
    scopeCreator.declareVar("x", n1, null, null, null, null);
    
    Node n2 = new Node(Token.NAME);
    n2.setString("x");
    try {
      scopeCreator.declareVar("x", n2, null, null, null, null);
      fail("Expected JSError");
    } catch (JSError e) {
      assertEquals("JSC_VAR_MULTIPLY_DECLARED_ERROR", e.getType().key);
    }
  }
  
  @Test
  public void testDeclareVar_DuplicateDeclaration_WithSuppression() {
    Scope scope = new Scope();
    Node n1 = new Node(Token.NAME);
    n1.setString("x");
    
    scopeCreator.declareVar("x", n1, null, null, null, null);
    
    Node n2 = new Node(Token.NAME);
    n2.setString("x");
    JSDocInfo info = new JSDocInfo();
    info.addSuppression("duplicate");
    n2.setJSDocInfo(info);
    
    scopeCreator.declareVar("x", n2, null, null, null, null);
    
    assertTrue(scope.isDeclared("x", false));
  }
  
  @Test
  public void testDeclareVar_ArgumentsVariable_Shadowing() {
    Scope scope = new Scope();
    Node n = new Node(Token.NAME);
    n.setString("arguments");
    
    try {
      scopeCreator.declareVar("arguments", n, null, null, null, null);
      fail("Expected JSError");
    } catch (JSError e) {
      assertEquals("JSC_VAR_ARGUMENTS_SHADOWED_ERROR", e.getType().key);
    }
  }
  
  @Test
  public void testDefaultRedeclarationHandler_ArgumentsVariable_Shadowing() {
    Scope scope = new Scope();
    Node nameNode = new Node(Token.NAME);
    nameNode.setString("arguments");
    Node n = new Node(Token.STRING);
    
    try {
      scopeCreator.new DefaultRedeclarationHandler()
          .onRedeclaration(scope, "arguments", n, null, null, nameNode);
      fail("Expected JSError");
    } catch (JSError e) {
      assertEquals("JSC_VAR_ARGUMENTS_SHADOWED_ERROR", e.getType().key);
    }
  }
  
  @Test
  public void testDefaultRedeclarationHandler_MultiplyDeclared_Error() {
    Scope scope = new Scope();
    Node nameNode = new Node(Token.NAME);
    nameNode.setString("x");
    Node n1 = new Node(Token.STRING);
    
    try {
      scopeCreator.new DefaultRedeclarationHandler()
          .onRedeclaration(scope, "x", n1, null, null, nameNode);
      fail("Expected JSError");
    } catch (JSError e) {
      assertEquals("JSC_VAR_MULTIPLY_DECLARED_ERROR", e.getType().key);
    }
  }
}