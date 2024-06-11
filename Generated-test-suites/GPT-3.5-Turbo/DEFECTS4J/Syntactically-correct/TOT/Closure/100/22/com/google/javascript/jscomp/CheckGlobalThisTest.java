import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {
  
  // Test case for shouldTraverse method
  @Test
  public void testShouldTraverse() {
    NodeTraversal t = new NodeTraversal();
    Node n1 = createFunctionNode();
    Node parent1 = createParentNode();
    assertFalse(CheckGlobalThis.shouldTraverse(t, n1, parent1));
    
    Node n2 = createFunctionNodeWithConstructorAnnotation();
    Node parent2 = createParentNode();
    assertFalse(CheckGlobalThis.shouldTraverse(t, n2, parent2));
    
    Node n3 = createFunctionNodeWithThisAnnotation();
    Node parent3 = createParentNode();
    assertFalse(CheckGlobalThis.shouldTraverse(t, n3, parent3));
    
    Node n4 = createFunctionNodeWithNestedFunction();
    Node parent4 = createParentNode();
    assertTrue(CheckGlobalThis.shouldTraverse(t, n4, parent4));
    
    Node n5 = createAssignmentNodeWithPrototypeProperty();
    Node parent5 = createParentNode();
    assertFalse(CheckGlobalThis.shouldTraverse(t, n5, parent5));
    
    Node n6 = createAssignmentNodeWithPrototypeSubproperty();
    Node parent6 = createParentNode();
    assertFalse(CheckGlobalThis.shouldTraverse(t, n6, parent6));
    
    Node n7 = createAssignmentNodeWithNonPrototypeProperty();
    Node parent7 = createParentNode();
    assertTrue(CheckGlobalThis.shouldTraverse(t, n7, parent7));
    
    Node n8 = createOtherNode();
    Node parent8 = createParentNode();
    assertTrue(CheckGlobalThis.shouldTraverse(t, n8, parent8));
  }
  
  // Test case for shouldReportThis method
  @Test
  public void testShouldReportThis() {
    Node n1 = createThisNode();
    Node parent1 = createParentNode();
    Node assignLhsChild1 = createAssignLhsChildNode();
    assertTrue(CheckGlobalThis.shouldReportThis(n1, parent1));
    
    Node n2 = createThisNode();
    Node parent2 = createParentNode();
    assertFalse(CheckGlobalThis.shouldReportThis(n2, parent2));
  }
  
  // Test case for visit method
  @Test
  public void testVisit() {
    NodeTraversal t = new NodeTraversal();
    Node n1 = createThisNode();
    Node parent1 = createParentNode();
    Node assignLhsChild1 = createAssignLhsChildNode();
    CheckGlobalThis.visit(t, n1, parent1);
    assertEquals(null, assignLhsChild1);
    
    Node n2 = createOtherNode();
    Node parent2 = createParentNode();
    Node assignLhsChild2 = createAssignLhsChildNode();
    CheckGlobalThis.visit(t, n2, parent2);
    assertEquals(assignLhsChild2, assignLhsChild1);
  }
  
  // Helper methods for creating nodes
  private Node createFunctionNode() {
    // Create function node without annotation
    Node functionNode = new Node(Token.FUNCTION);
    return functionNode;
  }
  
  private Node createFunctionNodeWithConstructorAnnotation() {
    // Create function node with constructor annotation
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordConstructor();
    functionNode.setJSDocInfo(jsDoc);
    return functionNode;
  }
  
  private Node createFunctionNodeWithThisAnnotation() {
    // Create function node with this annotation
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    functionNode.setJSDocInfo(jsDoc);
    return functionNode;
  }
  
  private Node createFunctionNodeWithNestedFunction() {
    // Create function node with nested function
    Node functionNode = new Node(Token.FUNCTION);
    Node nestedFunction = new Node(Token.FUNCTION);
    functionNode.addChildToFront(nestedFunction);
    return functionNode;
  }
  
  private Node createAssignmentNodeWithPrototypeProperty() {
    // Create assignment node with prototype property
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToFront(new Node(Token.NAME, "obj"));
    lhs.addChildToBack(new Node(Token.STRING, "prototype"));
    Node rhs = new Node(Token.NUMBER);
    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToFront(lhs);
    assignNode.addChildToBack(rhs);
    return assignNode;
  }
  
  private Node createAssignmentNodeWithPrototypeSubproperty() {
    // Create assignment node with prototype subproperty
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToFront(new Node(Token.NAME, "obj"));
    lhs.addChildToBack(new Node(Token.STRING, "prototype.property"));
    Node rhs = new Node(Token.NULL);
    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToFront(lhs);
    assignNode.addChildToBack(rhs);
    return assignNode;
  }
  
  private Node createAssignmentNodeWithNonPrototypeProperty() {
    // Create assignment node with non-prototype property
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToFront(new Node(Token.NAME, "obj"));
    lhs.addChildToBack(new Node(Token.STRING, "property"));
    Node rhs = new Node(Token.STRING, "value");
    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToFront(lhs);
    assignNode.addChildToBack(rhs);
    return assignNode;
  }
  
  private Node createOtherNode() {
    // Create other node
    Node node = new Node(Token.ADD);
    return node;
  }
  
  private Node createThisNode() {
    // Create this node
    Node node = new Node(Token.THIS);
    return node;
  }
  
  private Node createAssignLhsChildNode() {
    // Create assignLhsChild node
    Node node = new Node(Token.NAME, "lhsChild");
    return node;
  }
  
  private Node createParentNode() {
    // Create parent node
    Node node = new Node(Token.ASSIGN);
    return node;
  }
}