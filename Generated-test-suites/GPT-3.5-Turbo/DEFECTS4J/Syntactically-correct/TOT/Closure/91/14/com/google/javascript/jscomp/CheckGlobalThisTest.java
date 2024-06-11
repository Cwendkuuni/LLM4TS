import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void shouldReportThisWhenLeftSideOfAssignment() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    // Assert
    assert(result);
  }
  
  @Test
  public void shouldReportThisWithPropertyAccess() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    // Assert
    assert(result);
  }
  
  @Test
  public void shouldNotReportThisWhenNotLeftSideOfAssignmentOrPropertyAccess() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.STRING);
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    // Assert
    assert(!result);
  }
  
  @Test
  public void shouldNotTraverseFunctionWithConstructorAnnotation() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assert(!result);
  }
  
  @Test
  public void shouldNotTraverseFunctionWithThisAnnotation() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assert(!result);
  }
  
  @Test
  public void shouldNotTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.GT);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assert(!result);
  }
  
  @Test
  public void shouldNotTraverseFunctionWithPrototypeMethod() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.GT);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setPrototypable(true);
    n.setJSDocInfo(jsDoc);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assert(!result);
  }
  
  @Test
  public void shouldTraverseFunctionWithLogicalPlaceForThisAnnotation() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assert(result);
  }
  
  @Test
  public void shouldTraverseFunctionWithLentToPrototype() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToFront(new Node(Token.GETPROP));
    n.setParent(lhs);
    parent.addChildToFront(lhs);
    parent.addChildToFront(new Node());
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assert(!result);
  }
  
  @Test
  public void shouldVisitNodeAndReportGlobalThisWarning() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.SCRIPT);
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    assert(compiler.getWarnings().size() > 0);
  }
  
  @Test
  public void shouldSetAssignLhsChildToNullAfterVisit() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.SCRIPT);
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    assert(checkGlobalThis.assignLhsChild == null);
  }
  
  @Test
  public void shouldNotReportGlobalThisInAssignmentToVariable() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    n.addChildToFront(new Node(Token.NAME));
    parent.addChildToFront(n);
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    assert(compiler.getWarnings().size() == 0);
  }
  
  @Test
  public void shouldNotReportGlobalThisInAssignmentToObject() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    n.addChildToFront(new Node(Token.STRING));
    parent.addChildToFront(n);
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    assert(compiler.getWarnings().size() == 0);
  }
  
  @Test
  public void shouldNotReportGlobalThisInAssignmentToSubpropertyOfPrototype() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    n.addChildToFront(new Node(Token.GETPROP, new Node(Token.NAME), new Node(Token.STRING, "prototype")));
    parent.addChildToFront(n);
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    assert(compiler.getWarnings().size() == 0);
  }
  
  @Test
  public void shouldNotReportGlobalThisInAssignmentToSubpropertyOfNestedPrototype() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node grandparent = new Node(Token.NAME);
    Node parent = new Node(Token.GETPROP, grandparent, new Node(Token.STRING, "prototype"));
    n.addChildToFront(parent);
    parent.addChildToFront(n);
    grandparent.addChildToFront(parent);
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    assert(compiler.getWarnings().size() == 0);
  }

  @Test
  public void shouldReportGlobalThisInAssignmentToPrototypeProperty() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = new CheckLevel();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP, new Node(Token.NAME), new Node(Token.STRING, "prototype"));
    Node rhs = new Node(Token.NAME);
    parent.addChildToFront(lhs);
    parent.addChildToBack(rhs);
    lhs.addChildToFront(n);

    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    assert(compiler.getWarnings().size() > 0);
  }
}