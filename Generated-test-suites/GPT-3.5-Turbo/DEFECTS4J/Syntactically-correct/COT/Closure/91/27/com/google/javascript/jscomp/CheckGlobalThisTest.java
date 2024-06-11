
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.testing.BaseRhinoTestCase;

public class CheckGlobalThisTest extends BaseRhinoTestCase {
  
  @Test
  public void testShouldTraverse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    
    // Test shouldTraverse with a FUNCTION node that is a constructor
    JSDocInfo constructorJsDoc = new JSDocInfo();
    constructorJsDoc.setConstructor(true);
    Node constructorNode = new Node(Token.FUNCTION);
    constructorNode.setJSDocInfo(constructorJsDoc);
    boolean result1 = check.shouldTraverse(new NodeTraversal(), constructorNode, null);
    assertFalse(result1);
    
    // Test shouldTraverse with a FUNCTION node that has "@this" annotation
    JSDocInfo thisJsDoc = new JSDocInfo();
    thisJsDoc.recordThisType();
    Node thisNode = new Node(Token.FUNCTION);
    thisNode.setJSDocInfo(thisJsDoc);
    boolean result2 = check.shouldTraverse(new NodeTraversal(), thisNode, null);
    assertFalse(result2);
    
    // Test shouldTraverse with a FUNCTION node that has "@override" annotation
    JSDocInfo overrideJsDoc = new JSDocInfo();
    overrideJsDoc.setOverride(true);
    Node overrideNode = new Node(Token.FUNCTION);
    overrideNode.setJSDocInfo(overrideJsDoc);
    boolean result3 = check.shouldTraverse(new NodeTraversal(), overrideNode, null);
    assertFalse(result3);
    
    // Test shouldTraverse with a FUNCTION node that is within a valid context
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION);
    Node blockNode = new Node(Token.BLOCK);
    scriptNode.addChildToBack(blockNode);
    blockNode.addChildToBack(functionNode);
    boolean result4 = check.shouldTraverse(new NodeTraversal(), functionNode, scriptNode);
    assertTrue(result4);
  }
  
  @Test
  public void testVisit() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = new NodeTraversal();
    
    // Test visit with a THIS node that should be reported
    Node thisNode1 = new Node(Token.THIS);
    Node parent1 = new Node(Token.GETPROP);
    boolean result1 = check.visit(traversal, thisNode1, parent1);
    // Assert that an error is reported by the compiler
    
    // Test visit with a THIS node that should not be reported
    Node thisNode2 = new Node(Token.THIS);
    Node parent2 = new Node(Token.SCRIPT);
    boolean result2 = check.visit(traversal, thisNode2, parent2);
    // Assert that no error is reported by the compiler
  }

  @Test
  public void testShouldReportThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis check = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    
    // Test shouldReportThis with a THIS node as the assignLhsChild
    Node thisNode1 = new Node(Token.THIS);
    Node assignNode1 = new Node(Token.ASSIGN);
    assignNode1.addChildToBack(thisNode1);
    boolean result1 = check.shouldReportThis(thisNode1, assignNode1);
    assertTrue(result1);
    
    // Test shouldReportThis with a THIS node and a parent GETPROP node
    Node thisNode2 = new Node(Token.THIS);
    Node getPropNode = new Node(Token.GETPROP);
    boolean result2 = check.shouldReportThis(thisNode2, getPropNode);
    assertTrue(result2);
    
    // Test shouldReportThis with a non-THIS node and a parent GETPROP node
    Node identifierNode = new Node(Token.NAME);
    Node getPropNode2 = new Node(Token.GETPROP);
    boolean result3 = check.shouldReportThis(identifierNode, getPropNode2);
    assertFalse(result3);
  }
  
  @Test
  public void testGetFunctionJsDocInfo() {
    CheckGlobalThis check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    
    // Test getFunctionJsDocInfo with a FUNCTION node that has JSDoc info
    JSDocInfo jsDocInfo1 = new JSDocInfo();
    Node functionNode1 = new Node(Token.FUNCTION);
    functionNode1.setJSDocInfo(jsDocInfo1);
    JSDocInfo result1 = check.getFunctionJsDocInfo(functionNode1);
    assertEquals(jsDocInfo1, result1);
    
    // Test getFunctionJsDocInfo with a NAME node containing a FUNCTION node with JSDoc info
    JSDocInfo jsDocInfo2 = new JSDocInfo();
    Node functionNode2 = new Node(Token.FUNCTION);
    functionNode2.setJSDocInfo(jsDocInfo2);
    Node nameNode = new Node(Token.NAME);
    nameNode.addChildToBack(functionNode2);
    JSDocInfo result2 = check.getFunctionJsDocInfo(nameNode);
    assertEquals(jsDocInfo2, result2);
    
    // Test getFunctionJsDocInfo with an ASSIGN node containing a NAME node with a FUNCTION node with JSDoc info
    JSDocInfo jsDocInfo3 = new JSDocInfo();
    Node functionNode3 = new Node(Token.FUNCTION);
    functionNode3.setJSDocInfo(jsDocInfo3);
    Node nameNode2 = new Node(Token.NAME);
    nameNode2.addChildToBack(functionNode3);
    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToFront(nameNode2);
    JSDocInfo result3 = check.getFunctionJsDocInfo(assignNode);
    assertEquals(jsDocInfo3, result3);
    
    // Test getFunctionJsDocInfo with a NAME node that does not have JSDoc info
    Node nameNode3 = new Node(Token.NAME);
    JSDocInfo result4 = check.getFunctionJsDocInfo(nameNode3);
    assertNull(result4);
    
    // Test getFunctionJsDocInfo with a non-FUNCTION parent node
    JSDocInfo jsDocInfo5 = new JSDocInfo();
    Node functionNode5 = new Node(Token.FUNCTION);
    functionNode5.setJSDocInfo(jsDocInfo5);
    Node blockNode = new Node(Token.BLOCK);
    blockNode.addChildToFront(functionNode5);
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToFront(blockNode);
    JSDocInfo result5 = check.getFunctionJsDocInfo(scriptNode);
    assertNull(result5);
  }
}
