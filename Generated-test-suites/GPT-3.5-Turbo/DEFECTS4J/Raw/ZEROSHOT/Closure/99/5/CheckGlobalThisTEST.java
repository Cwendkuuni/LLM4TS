import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.DiagnosticType;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  @Test
  public void testShouldTraverse_withFunctionConstructor_returnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis obj = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    Node parent = new Node();
    assertFalse(obj.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_withFunctionThisAnnotation_returnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis obj = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType("Type");
    n.setJSDocInfo(jsDoc);
    Node parent = new Node();
    assertFalse(obj.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_withFunctionOverrideAnnotation_returnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis obj = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    n.setJSDocInfo(jsDoc);
    Node parent = new Node();
    assertFalse(obj.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_withNonBlockNonScriptNonNameNonAssign_returnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis obj = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    parent.setType(Token.ADD);
    assertFalse(obj.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_withAssignAndLeftSide_returnsTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis obj = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    parent.setType(Token.ASSIGN);
    parent.addChildToBack(n);
    assertFalse(obj.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_withAssignAndRightSide_returnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis obj = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    parent.setType(Token.ASSIGN);
    parent.addChildToFront(n);
    assertFalse(obj.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_withAssignAndPrototypeProperty_returnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis obj = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToFront(new Node("prototype"));
    parent.addChildToFront(lhs);
    parent.addChildToBack(n);
    assertFalse(obj.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_withAssignAndPrototypeSubproperty_returnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis obj = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToFront(new Node("prototype"));
    lhs.addChildToBack(new Node("subproperty"));
    parent.addChildToFront(lhs);
    parent.addChildToBack(n);
    assertFalse(obj.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_withOtherCases_returnsTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis obj = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    Node lhs = new Node(Token.ADD);
    Node grandParent = new Node();
    parent.addChildToFront(lhs);
    grandParent.addChildToFront(parent);
    grandParent.setType(Token.BLOCK);
    assertTrue(obj.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testVisit_withThis_shouldReportThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis obj = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    assertTrue(obj.shouldReportThis(n, parent));
  }
  
  @Test
  public void testVisit_withAssignLhsChild_shouldResetAssignLhsChild() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis obj = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    obj.shouldReportThis(n, parent);
    assertNull(obj.assignLhsChild);
  }
  
  @Test
  public void testVisit_withoutAssignLhsChild_shouldNotResetAssignLhsChild() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis obj = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    parent.setType(Token.ASSIGN);
    obj.shouldReportThis(n, parent);
    assertNotNull(obj.assignLhsChild);
  }
  
  @Test
  public void testVisit_withoutThis_shouldNotReportThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis obj = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.ADD);
    Node parent = new Node();
    assertFalse(obj.shouldReportThis(n, parent));
  }
  
  @Test
  public void testConstructor_withCompilerAndLevel_setsFields() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis obj = new CheckGlobalThis(compiler, level);
    assertEquals(compiler, obj.compiler);
    assertEquals(level, obj.level);
  }
  
  @Test
  public void testCheckGlobalThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis obj = new CheckGlobalThis(compiler, level);
    assertNotNull(obj);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_withJSDoc_returnsJSDocInfo() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    CheckGlobalThis obj = new CheckGlobalThis(compiler, level);
    JSDocInfo expected = jsDoc;
    JSDocInfo actual = obj.getFunctionJsDocInfo(n);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_withParentNameJSDoc_returnsJSDocInfo() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    parent.setJSDocInfo(new JSDocInfo());
    n.setParent(parent);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    CheckGlobalThis obj = new CheckGlobalThis(compiler, level);
    JSDocInfo expected = parent.getJSDocInfo();
    JSDocInfo actual = obj.getFunctionJsDocInfo(n);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_withParentAssignNameJSDoc_returnsJSDocInfo() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    Node gramps = new Node(Token.VAR);
    gramps.setJSDocInfo(new JSDocInfo());
    parent.setParent(gramps);
    n.setParent(parent);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    CheckGlobalThis obj = new CheckGlobalThis(compiler, level);
    JSDocInfo expected = gramps.getJSDocInfo();
    JSDocInfo actual = obj.getFunctionJsDocInfo(n);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_withoutJSDoc_returnsNull() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    Node n = new Node(Token.FUNCTION);
    CheckGlobalThis obj = new CheckGlobalThis(compiler, level);
    JSDocInfo expected = null;
    JSDocInfo actual = obj.getFunctionJsDocInfo(n);
    assertEquals(expected, actual);
  }
}