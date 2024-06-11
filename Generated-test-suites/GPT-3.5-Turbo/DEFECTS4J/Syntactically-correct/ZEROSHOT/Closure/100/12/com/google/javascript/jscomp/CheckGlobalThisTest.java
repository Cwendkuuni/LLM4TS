import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  private CheckGlobalThis checkGlobalThis;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverse_FunctionIsConstructor() {
    // Create a function node with constructor annotation
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo constructorAnnotation = new JSDocInfo();
    constructorAnnotation.setConstructor(true);
    functionNode.setJSDocInfo(constructorAnnotation);

    // Create a dummy NodeTraversal and parent node
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node parent = new Node(Token.ROOT);
    
    // Test shouldTraverse method
    assertFalse(checkGlobalThis.shouldTraverse(t, functionNode, parent));
  }

  @Test
  public void testShouldTraverse_FunctionHasThisAnnotation() {
    // Create a function node with @this annotation
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo thisAnnotation = new JSDocInfo();
    thisAnnotation.recordType(new JSTypeRegistry(), 
        new FunctionTypeBuilder(new JSTypeRegistry()).buildFunctionType());
    functionNode.setJSDocInfo(thisAnnotation);

    // Create a dummy NodeTraversal and parent node
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node parent = new Node(Token.ROOT);
    
    // Test shouldTraverse method
    assertFalse(checkGlobalThis.shouldTraverse(t, functionNode, parent));
  }

  @Test
  public void testShouldTraverse_FunctionIsNestedAssignment() {
    // Create a function node
    Node functionNode = new Node(Token.FUNCTION);

    // Create a dummy NodeTraversal and parent node
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(new Node(Token.NAME, "a"));
    parent.addChildToBack(new Node(Token.EMPTY));

    // Add the assignLhsChild
    checkGlobalThis.visit(t, parent.getFirstChild(), parent);
    
    // Test shouldTraverse method
    assertFalse(checkGlobalThis.shouldTraverse(t, functionNode, parent));
  }

  @Test
  public void testShouldTraverse_RightSideIsAssignmentToPrototypeProperty() {
    // Create a left side property access node
    Node prototypePropertyAccess = new Node(Token.GETPROP);
    prototypePropertyAccess.addChildToFront(new Node(Token.NAME, "a"));
    prototypePropertyAccess.addChildToBack(new Node(Token.STRING, "prototype"));

    // Create a right side assignment
    Node assignment = new Node(Token.ASSIGN);
    assignment.addChildToFront(prototypePropertyAccess);
    assignment.addChildToBack(new Node(Token.NAME, "b"));

    // Create a dummy NodeTraversal and parent node
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(assignment);
    
    // Test shouldTraverse method
    assertFalse(checkGlobalThis.shouldTraverse(t, assignment.getFirstChild(), parent));
  }

  @Test
  public void testShouldTraverse_RightSideIsAssignmentToSubproperty() {
    // Create a left side property access node
    Node subpropertyAccess = new Node(Token.GETPROP);
    subpropertyAccess.addChildToFront(new Node(Token.NAME, "a"));
    subpropertyAccess.addChildToBack(new Node(Token.STRING, "prototype.x"));

    // Create a right side assignment
    Node assignment = new Node(Token.ASSIGN);
    assignment.addChildToFront(subpropertyAccess);
    assignment.addChildToBack(new Node(Token.NAME, "b"));

    // Create a dummy NodeTraversal and parent node
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(assignment);
    
    // Test shouldTraverse method
    assertFalse(checkGlobalThis.shouldTraverse(t, assignment.getFirstChild(), parent));
  }

  @Test
  public void testShouldTraverse_LeftSideIsPrototypeMethod() {
    // Create a left side prototype method node
    Node prototypeMethod = new Node(Token.FUNCTION);
    Node prototypeMethodParent = new Node(Token.GETPROP);
    prototypeMethodParent.addChildToFront(new Node(Token.NAME, "a"));
    prototypeMethodParent.addChildToBack(new Node(Token.STRING, "prototype"));
    prototypeMethodParent.addChildToBack(prototypeMethod);
    
    // Create a dummy NodeTraversal and parent node
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(prototypeMethodParent);
    parent.addChildToBack(new Node(Token.NAME, "b"));

    // Add the assignLhsChild
    checkGlobalThis.visit(t, parent.getFirstChild(), parent);
    
    // Test shouldTraverse method
    assertFalse(checkGlobalThis.shouldTraverse(t, prototypeMethodParent, parent));
  }

  @Test
  public void testShouldTraverse_AllOtherCases() {
    // Create a function node
    Node functionNode = new Node(Token.FUNCTION);

    // Create a dummy NodeTraversal and parent node
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node parent = new Node(Token.ROOT);

    // Test shouldTraverse method
    assertTrue(checkGlobalThis.shouldTraverse(t, functionNode, parent));
  }

  @Test
  public void testVisit_ShouldReportThis() {
    // Create a THIS node and a dummy parent node
    Node thisNode = new Node(Token.THIS);
    Node parent = new Node(Token.ROOT);

    // Create a dummy NodeTraversal
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);

    // Test visit method
    checkGlobalThis.visit(t, thisNode, parent);
    assertTrue(t.hasReportedErrors());
  }

  @Test
  public void testVisit_ShouldNotReportThis() {
    // Create a non-THIS node and a dummy parent node
    Node nonThisNode = new Node(Token.NAME, "x");
    Node parent = new Node(Token.ROOT);

    // Create a dummy NodeTraversal
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);

    // Test visit method
    checkGlobalThis.visit(t, nonThisNode, parent);
    assertFalse(t.hasReportedErrors());
  }
  
  @Test
  public void testVisit_AssignLhsChildNotNull() {
    // Create a THIS node and a dummy parent node
    Node thisNode = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);

    // Create a dummy NodeTraversal
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);

    // Add the assignLhsChild
    checkGlobalThis.visit(t, parent.getFirstChild(), parent);

    // Test visit method
    checkGlobalThis.visit(t, thisNode, parent);
    assertTrue(t.hasReportedErrors());
    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionHasJSDocInfo() {
    // Create a function node with JSDocInfo
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    functionNode.setJSDocInfo(jsDocInfo);

    // Test getFunctionJsDocInfo method
    assertEquals(jsDocInfo, checkGlobalThis.getFunctionJsDocInfo(functionNode));
  }

  @Test
  public void testGetFunctionJsDocInfo_ParentIsNameWithJSDocInfo() {
    // Create a function node without JSDocInfo
    Node functionNode = new Node(Token.FUNCTION);

    // Create a parent node
    Node parent = new Node(Token.NAME);
    JSDocInfo parentJsDocInfo = new JSDocInfo();
    parent.setJSDocInfo(parentJsDocInfo);

    // Set the function node as parent's child
    parent.addChildToFront(functionNode);

    // Test getFunctionJsDocInfo method
    assertEquals(parentJsDocInfo, checkGlobalThis.getFunctionJsDocInfo(functionNode));
  }

  @Test
  public void testGetFunctionJsDocInfo_ParentIsAssignWithJSDocInfo() {
    // Create a function node without JSDocInfo
    Node functionNode = new Node(Token.FUNCTION);

    // Create a parent node
    Node parent = new Node(Token.ASSIGN);
    JSDocInfo parentJsDocInfo = new JSDocInfo();
    parent.setJSDocInfo(parentJsDocInfo);

    // Set the function node as parent's second child
    parent.addChildToBack(functionNode);

    // Test getFunctionJsDocInfo method
    assertEquals(parentJsDocInfo, checkGlobalThis.getFunctionJsDocInfo(functionNode));
  }

  @Test
  public void testGetFunctionJsDocInfo_ParentIsVarWithNameWithJSDocInfo() {
    // Create a function node without JSDocInfo
    Node functionNode = new Node(Token.FUNCTION);

    // Create a parent node
    Node varNode = new Node(Token.VAR);
    Node parent = new Node(Token.NAME);
    JSDocInfo parentJsDocInfo = new JSDocInfo();
    parent.setJSDocInfo(parentJsDocInfo);

    // Set the var node as parent's only child
    parent.addChildToFront(varNode);
    varNode.addChildToBack(functionNode);
    
    // Test getFunctionJsDocInfo method
    assertEquals(parentJsDocInfo, checkGlobalThis.getFunctionJsDocInfo(functionNode));
  }

  @Test
  public void testGetFunctionJsDocInfo_DefaultCase() {
    // Create a function node without JSDocInfo
    Node functionNode = new Node(Token.FUNCTION);

    // Create a parent node
    Node parent = new Node(Token.ROOT);

    // Test getFunctionJsDocInfo method
    assertNull(checkGlobalThis.getFunctionJsDocInfo(functionNode));
  }
}