import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AstFactory;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.Token;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;
  private AbstractCompiler compiler;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = new NodeTraversal(compiler, checkGlobalThis);
  }

  @Test
  public void testShouldTraverse_FunctionConstructorWithThisAnnotation_false() {
    Node node = createFunctionNodeWithThisAnnotation();
    assertFalse(checkGlobalThis.shouldTraverse(traversal, node, node.getParent()));
  }

  @Test
  public void testShouldTraverse_FunctionConstructorWithInterfaceAnnotation_false() {
    Node node = createFunctionNodeWithInterfaceAnnotation();
    assertFalse(checkGlobalThis.shouldTraverse(traversal, node, node.getParent()));
  }

  @Test
  public void testShouldTraverse_FunctionConstructorWithThisTypeAnnotation_false() {
    Node node = createFunctionNodeWithThisTypeAnnotation();
    assertFalse(checkGlobalThis.shouldTraverse(traversal, node, node.getParent()));
  }

  @Test
  public void testShouldTraverse_FunctionConstructorWithOverrideAnnotation_false() {
    Node node = createFunctionNodeWithOverrideAnnotation();
    assertFalse(checkGlobalThis.shouldTraverse(traversal, node, node.getParent()));
  }

  @Test
  public void testShouldTraverse_FunctionConstructor_noParent_false() {
    Node node = createFunctionNode();
    assertFalse(checkGlobalThis.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldTraverse_FunctionConstructor_nonGlobalContext_false() {
    Node node = createFunctionNode();
    Node parent = new Node(Token.BLOCK);
    assertFalse(checkGlobalThis.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testVisit_This_shouldReportThis_true() {
    Node node = createThisNode();
    Node parent = new Node(Token.ACCESS);
    assertTrue(checkGlobalThis.shouldReportThis(node, parent));
  }

  @Test
  public void testVisit_This_shouldReportThis_false() {
    Node node = createThisNode();
    Node parent = null;
    assertFalse(checkGlobalThis.shouldReportThis(node, parent));
  }

  @Test
  public void testVisit_AssignLhsChild_assignLhsChildCleared() {
    Node node = createThisNode();
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.visit(traversal, node, parent);
    assertNull(checkGlobalThis.getAssignLhsChild());
  }

  @Test
  public void testVisit_AssignLhsChild_assignLhsChildNotCleared() {
    Node node = createThisNode();
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.setAssignLhsChild(node);
    checkGlobalThis.visit(traversal, node, parent);
    assertNotNull(checkGlobalThis.getAssignLhsChild());
  }

  private Node createFunctionNodeWithThisAnnotation() {
    Node parentNode = createParentNode();
    parentNode.putProp(Node.JSDOC_INFO_PROP, new JSDocInfo());
    Node node = createFunctionNode();
    parentNode.addChildToFront(node);
    return node;
  }

  private Node createFunctionNodeWithInterfaceAnnotation() {
    Node parentNode = createParentNode();
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.recordInterface();
    parentNode.putProp(Node.JSDOC_INFO_PROP, jsDocInfo);
    Node node = createFunctionNode();
    parentNode.addChildToFront(node);
    return node;
  }

  private Node createFunctionNodeWithThisTypeAnnotation() {
    Node parentNode = createParentNode();
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.recordThisType(new Node(Token.THIS));
    parentNode.putProp(Node.JSDOC_INFO_PROP, jsDocInfo);
    Node node = createFunctionNode();
    parentNode.addChildToFront(node);
    return node;
  }

  private Node createFunctionNodeWithOverrideAnnotation() {
    Node parentNode = createParentNode();
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.recordOverride();
    parentNode.putProp(Node.JSDOC_INFO_PROP, jsDocInfo);
    Node node = createFunctionNode();
    parentNode.addChildToFront(node);
    return node;
  }

  private Node createFunctionNode() {
    return new Node(Token.FUNCTION);
  }

  private Node createThisNode() {
    return new Node(Token.THIS);
  }

  private Node createParentNode() {
    return new Node(Token.SCRIPT);
  }
}