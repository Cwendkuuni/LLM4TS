import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThisConstructor() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    assertNotNull(checkGlobalThis);
  }

  @Test
  public void testShouldTraverseFunctionWithConstructorAndThisAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    Node functionNode = new Node(Token.FUNCTION, new int[0], new int[0]);
    Node constructorAnnotationNode = new Node(Token.ANNOTATION, 0, new int[0]);
    Node thisAnnotationNode = new Node(Token.ANNOTATION, 1, new int[0]);
    functionNode.addChildToStatementList(constructorAnnotationNode);
    constructorAnnotationNode.addChildToStatementList(thisAnnotationNode);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal nodeTraversal = new NodeTraversal(functionNode, checkGlobalThis, null);
    checkGlobalThis.visit(nodeTraversal, functionNode, null);
    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null));
  }

  @Test(expected = NullPointerException.class)
  public void testNullPointerExceptionWhenParentIsNull() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    Node functionNode = new Node(Token.FUNCTION, new int[0], new int[0]);
    Node parentNode = null;
    NodeTraversal nodeTraversal = new NodeTraversal(functionNode, checkGlobalThis, parentNode);
    checkGlobalThis.visit(nodeTraversal, functionNode, parentNode);
  }

  @Test
  public void testVisitMultipleOccurrencesOfThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    Node functionNode = new Node(Token.FUNCTION, new int[0], new int[0]);
    Node thisNode1 = new Node(Token.THIS, 0, new int[0]);
    Node thisNode2 = new Node(Token.THIS, 1, new int[0]);
    functionNode.addChildToStatementList(thisNode1);
    functionNode.addChildToStatementList(thisNode2);
    NodeTraversal nodeTraversal = new NodeTraversal(functionNode, checkGlobalThis, null);
    checkGlobalThis.visit(nodeTraversal, functionNode, null);
    assertEquals(2, checkGlobalThis.getNumGlobalThisWarnings());
  }

  @Test(expected = NullPointerException.class)
  public void testNullPointerExceptionWhenNodeIsNull() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    Node node = null;
    checkGlobalThis.shouldReportThis(new NodeTraversal(node, null, null), node, null);
  }

  @Test(expected = NullPointerException.class)
  public void testNullPointerExceptionWhenParentIsNull() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    Node node = new Node(Token.FUNCTION, new int[0], new int[0]);
    checkGlobalThis.shouldReportThis(new NodeTraversal(node, null, null), node, null);
  }

  @Test
  public void testShouldReportThisInPrototypeMethod() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    Node functionNode = new Node(Token.FUNCTION, new int[0], new int[0]);
    Node prototypePropertyAccessNode = new Node(Token.GETPROP, 0, new int[0]);
    prototypePropertyAccessNode.setLastChild(new Node(Token.STRING, "prototype", new int[0]));
    Node thisNode = new Node(Token.THIS, 0, new int[0]);
    functionNode.addChildToStatementList(prototypePropertyAccessNode);
    functionNode.addChildToStatementList(thisNode);
    NodeTraversal nodeTraversal = new NodeTraversal(functionNode, checkGlobalThis, null);
    checkGlobalThis.visit(nodeTraversal, functionNode, null);
    assertFalse(checkGlobalThis.shouldReportThis(nodeTraversal.getCurrentNode(), null));
  }

  @Test
  public void testGetFunctionJsDocInfoFunctionWithJsDocInfo() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    Node functionNode = new Node(Token.FUNCTION, new int[0], new int[0]);
    Node jsDocInfoNode = new Node(Token.ANNOTATION, 0, new int[0]);
    Node jsDocInfoTagNode = new Node(Token.TAG, 0, new int[0]);
    jsDocInfoTagNode.setLastChild(new Node(Token.STRING, "constructor", new int[0]));
    jsDocInfoNode.addChildToStatementList(jsDocInfoTagNode);
    functionNode.addChildToStatementList(jsDocInfoNode);
    Node jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(functionNode);
    assertTrue(jsDocInfo instanceof JSDocInfo);
  }

  @Test(expected = NullPointerException.class)
  public void testNullPointerExceptionWhenNodeHasNoJsDocInfo() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    Node functionNode = new Node(Token.FUNCTION, new int[0], new int[0]);
    Node jsDocInfoNode = null;
    Node jsDocInfoTagNode = new Node(Token.TAG, 0, new int[0]);
    jsDocInfoTagNode.setLastChild(new Node(Token.STRING, "constructor", new int[0]));
    jsDocInfoNode = new Node(Token.ANNOTATION, 0, new int[0]);
    jsDocInfoNode.addChildToStatementList(jsDocInfoTagNode);
    functionNode.addChildToStatementList(jsDocInfoNode);
    NodeTraversal nodeTraversal = new NodeTraversal(functionNode, checkGlobalThis, null);
    checkGlobalThis.getFunctionJsDocInfo(functionNode);
  }

  @Test
  public void testGetFunctionJsDocInfoFunctionWithoutJsDocInfo() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    Node functionNode = new Node(Token.FUNCTION, new int[0], new int[0]);
    Node jsDocInfoNode = null;
    Node jsDocInfoTagNode = new Node(Token.TAG, 0, new int[0]);
    jsDocInfoTagNode.setLastChild(new Node(Token.STRING, "constructor", new int[0]));
    jsDocInfoNode = new Node(Token.ANNOTATION, 0, new int[0]);
    jsDocInfoNode.addChildToStatementList(jsDocInfoTagNode);
    functionNode.addChildToStatementList(jsDocInfoNode);
    NodeTraversal nodeTraversal = new NodeTraversal(functionNode, checkGlobalThis, null);
    Node currentNode = nodeTraversal.getCurrentNode();
    Node jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(currentNode);
    assertNull(jsDocInfo);
  }

  public int getNumGlobalThisWarnings() {
    return ((CheckGlobalThis) getInstance()).getNumGlobalThisWarnings();
  }
}

class CheckGlobalThis {
  private int numGlobalThisWarnings = 0;

  public int getNumGlobalThisWarnings() {
    return numGlobalThisWarnings;
  }
}