import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {
  
  private CheckGlobalThis checkGlobalThis;
  private AbstractCompiler compiler;
  private CheckLevel level;
  private NodeTraversal traversal;
  private Node node;
  private Node parent;
  
  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    level = CheckLevel.WARNING;
    checkGlobalThis = new CheckGlobalThis(compiler, level);
    traversal = new NodeTraversal();
    node = new Node(Token.THIS);
    parent = new Node(Token.GETPROP);
  }
  
  @Test
  public void testShouldTraverse_WithFunctionAsConstructor_ShouldReturnFalse() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setConstructor(true);
    functionNode.setJSDocInfo(jsDocInfo);
    NodeTraversal t = new NodeTraversal();
    assertFalse("shouldTraverse() should return false when given a function that is a constructor", checkGlobalThis.shouldTraverse(t, functionNode, null));
  }
  
  @Test
  public void testShouldTraverse_WithFunctionAnnotatedWithThis_ShouldReturnFalse() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.recordThisType();
    functionNode.setJSDocInfo(jsDocInfo);
    NodeTraversal t = new NodeTraversal();
    assertFalse("shouldTraverse() should return false when given a function annotated with @this", checkGlobalThis.shouldTraverse(t, functionNode, null));
  }
  
  @Test
  public void testShouldTraverse_WithFunctionAnnotatedWithOverride_ShouldReturnFalse() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setOverride();
    functionNode.setJSDocInfo(jsDocInfo);
    NodeTraversal t = new NodeTraversal();
    assertFalse("shouldTraverse() should return false when given a function annotated with @override", checkGlobalThis.shouldTraverse(t, functionNode, null));
  }
  
  @Test
  public void testShouldTraverse_WithFunctionNotInBlockScriptNameOrAssign_ShouldReturnFalse() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parentBlock = new Node(Token.BLOCK);
    parentBlock.addChildToFront(functionNode);
    NodeTraversal t = new NodeTraversal();
    assertFalse("shouldTraverse() should return false when given a function that is not in a block, script, name, or assign", checkGlobalThis.shouldTraverse(t, functionNode, parentBlock));
  }
  
  @Test
  public void testVisit_WithThisNode_ShouldReportGlobalThis() {
    NodeTraversal t = new NodeTraversal();
    checkGlobalThis.visit(t, node, parent);
    assertTrue("visit() should report a global 'this' object", t.hasErrors());
  }
  
  @Test
  public void testVisit_WithAssignLhsChildNode_ShouldNotReportGlobalThis() {
    checkGlobalThis.visit(traversal, node, parent);
    assertFalse("visit() should not report a global 'this' object when it is the left side of an assignment", traversal.hasErrors());
  }
  
  @Test
  public void testVisit_WithNodeNotAssignLhsChild_ShouldNotDoAnything() {
    Node lhsNode = new Node(Token.NAME, "x");
    Node rhsNode = new Node(Token.NULL);
    Node assignNode = new Node(Token.ASSIGN, lhsNode, rhsNode);
    parent.addChildToFront(assignNode);
    checkGlobalThis.visit(traversal, lhsNode, assignNode);
    assertFalse("visit() should not do anything when the node is not the left side of an assignment", traversal.hasErrors());
  }
}