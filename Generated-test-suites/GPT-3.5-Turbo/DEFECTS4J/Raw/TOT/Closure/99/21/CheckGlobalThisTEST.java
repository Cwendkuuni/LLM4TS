import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CheckGlobalThisTest {
  
  @Test
  public void shouldReportThisForAssignmentToPrototypeProperty() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(Mockito.mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal t = Mockito.mock(NodeTraversal.class);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(new Node(Token.NAME, "prototype"));
    parent.addChildToBack(new Node(Token.NAME, "property"));
    
    checkGlobalThis.visit(t, n, parent);
    
    assertEquals(1, t.getErrors().size());
    assertEquals(CheckGlobalThis.GLOBAL_THIS, t.getErrors().get(0).getType());
  }
  
  @Test
  public void shouldReportThisForAssignmentToPrototypeSubproperty() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(Mockito.mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal t = Mockito.mock(NodeTraversal.class);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(new Node(Token.NAME, "prototype.property"));
    parent.addChildToBack(new Node(Token.NAME, "subproperty"));
    
    checkGlobalThis.visit(t, n, parent);
    
    assertEquals(1, t.getErrors().size());
    assertEquals(CheckGlobalThis.GLOBAL_THIS, t.getErrors().get(0).getType());
  }
  
  @Test
  public void shouldReportThisForAssignmentToVariable() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(Mockito.mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal t = Mockito.mock(NodeTraversal.class);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    
    checkGlobalThis.visit(t, n, parent);
    
    assertEquals(1, t.getErrors().size());
    assertEquals(CheckGlobalThis.GLOBAL_THIS, t.getErrors().get(0).getType());
  }
  
  @Test
  public void shouldNotReportThisForAssignmentInPrototypeMethod() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(Mockito.mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal t = Mockito.mock(NodeTraversal.class);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    parent.setJSDocInfo(jsDoc);
    
    checkGlobalThis.visit(t, n, parent);
    
    assertEquals(0, t.getErrors().size());
  }
  
  @Test
  public void shouldNotReportThisForAssignmentInConstructorFunction() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(Mockito.mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal t = Mockito.mock(NodeTraversal.class);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordConstructor();
    parent.setJSDocInfo(jsDoc);
    
    checkGlobalThis.visit(t, n, parent);
    
    assertEquals(0, t.getErrors().size());
  }
  
  @Test
  public void shouldNotReportThisForAssignmentInThisFunction() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(Mockito.mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal t = Mockito.mock(NodeTraversal.class);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThis();
    parent.setJSDocInfo(jsDoc);
    
    checkGlobalThis.visit(t, n, parent);
    
    assertEquals(0, t.getErrors().size());
  }
  
  @Test
  public void shouldNotReportThisForAssignmentInNestedFunctionWithoutLogicalPlaceForThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(Mockito.mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal t = Mockito.mock(NodeTraversal.class);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.FUNCTION);
    Node grandParent = new Node(Token.FUNCTION);
    parent.addChildToFront(grandParent);
    JSDocInfo jsDoc = new JSDocInfo();
    grandParent.setJSDocInfo(jsDoc);
    
    checkGlobalThis.visit(t, n, parent);
    
    assertEquals(0, t.getErrors().size());
  }
  
  @Test
  public void shouldNotReportThisForAssignmentToObject() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(Mockito.mock(AbstractCompiler.class), CheckLevel.WARNING);
    NodeTraversal t = Mockito.mock(NodeTraversal.class);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToFront(new Node(Token.NAME, "a"));
    lhs.addChildToBack(new Node(Token.NAME, "property"));
    parent.addChildToFront(lhs);
    parent.addChildToBack(new Node(Token.NAME, "value"));
    
    checkGlobalThis.visit(t, n, parent);
    
    assertEquals(0, t.getErrors().size());
  }

}