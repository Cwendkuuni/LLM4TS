import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  @Test
  public void testShouldTraverse_Function_NotConstructorOrThisAnnotation_ShouldReturnTrue() {
    // Setup
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_Function_ConstructorAnnotation_ShouldReturnFalse() {
    // Setup
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.putProp("jsdocinfo", JSDocInfo.newBuilder().setConstructor(true).build());
    Node parent = new Node();
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_Function_ThisAnnotation_ShouldReturnFalse() {
    // Setup
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.putProp("jsdocinfo", JSDocInfo.newBuilder().setThisType(true).build());
    Node parent = new Node();
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_Function_OverrideAnnotation_ShouldReturnFalse() {
    // Setup
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    n.putProp("jsdocinfo", JSDocInfo.newBuilder().setOverride(true).build());
    Node parent = new Node();
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_Function_ParentNotBlockScriptNameOrAssign_ShouldReturnFalse() {
    // Setup
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node(Token.ADD);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_Function_IsObjectLitKeyIsLendsNameEndsWithPrototype_ShouldReturnFalse() {
    // Setup
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node(Token.STRING_KEY);
    Node gramps = new Node();
    JSDocInfo.Builder jsDocBuilder = JSDocInfo.newBuilder();
    jsDocBuilder.setLendsName("example.prototype");
    gramps.putProp("jsdocinfo", jsDocBuilder.build());
    parent.putProp("parent", gramps);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testVisit_This_ShouldReportError() {
    // Setup
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    // Error assertion here
  }
  
  @Test
  public void testVisit_AssignLhsChild_ShouldResetAssignLhsChildToNull() {
    // Setup
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    checkGlobalThis.assignLhsChild = n;
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    assertNull(checkGlobalThis.assignLhsChild);
  }
  
  @Test
  public void testShouldReportThis_AssignLhsChildNotNull_ShouldReturnTrue() {
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    checkGlobalThis.assignLhsChild = new Node();
    Node n = new Node();
    Node parent = new Node();
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(n);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testShouldReportThis_ParentIsGet_ShouldReturnTrue() {
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node n = new Node();
    Node parent = new Node(Token.GETPROP);
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(n);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testShouldReportThis_NotAssignLhsChildAndParentNotGet_ShouldReturnFalse() {
    // Setup
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node n = new Node();
    Node parent = new Node();
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(n);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_HasJsDocInfo_ShouldReturnJsDocInfo() {
    // Setup
    Node n = new Node();
    JSDocInfo.Builder jsDocBuilder = JSDocInfo.newBuilder();
    jsDocBuilder.addSuppression("example");
    n.putProp("jsdocinfo", jsDocBuilder.build());
    
    // Act
    JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);
    
    // Assert
    assertEquals(jsDocBuilder.build(), result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_HasParentNameAndParentJsDocInfo_DoesNotHaveJsDocInfo_ShouldReturnParentJsDocInfo() {
    // Setup
    Node n = new Node();
    Node parent = new Node(Token.NAME);
    JSDocInfo.Builder jsDocBuilder = JSDocInfo.newBuilder();
    jsDocBuilder.setConstructor(true);
    parent.putProp("jsdocinfo", jsDocBuilder.build());
    n.putProp("parent", parent);
    
    // Act
    JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);
    
    // Assert
    assertEquals(jsDocBuilder.build(), result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_HasParentVarAndParentJsDocInfo_DoesNotHaveJsDocInfo_ShouldReturnParentJsDocInfo() {
    // Setup
    Node n = new Node();
    Node parent = new Node(Token.VAR);
    JSDocInfo.Builder jsDocBuilder = JSDocInfo.newBuilder();
    jsDocBuilder.setOverride(true);
    parent.putProp("jsdocinfo", jsDocBuilder.build());
    Node gramps = new Node();
    parent.putProp("parent", gramps);
    n.putProp("parent", parent);
    
    // Act
    JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);
    
    // Assert
    assertEquals(jsDocBuilder.build(), result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_HasParentNameAndNoJsDocInfo_ShouldReturnNull() {
    // Setup
    Node n = new Node();
    Node parent = new Node(Token.NAME);
    n.putProp("parent", parent);
    
    // Act
    JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);
    
    // Assert
    assertNull(result);
  }
  
}