package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckLevel level;
  private Callback callback;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    level = CheckLevel.WARNING;
    callback = new CheckGlobalThis(compiler, level);
    traversal = NodeTraversal.create(callback);
  }

  @Test
  public void testShouldTraverseGlobalFunction() {
    Node globalFunctionNode = Node.newFunction("f", new int[0], null);
    Node parentNode = Node.newBlock();
    Node rootNode = Node.newScript();
    rootNode.addChildToEnd(parentNode);
    parentNode.addChildToEnd(globalFunctionNode);

    boolean result = callback.shouldTraverse(traversal, globalFunctionNode, parentNode);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node constructorFunctionNode = Node.newFunction("constructor", new int[0], null);
    Node parentNode = Node.newBlock();
    Node rootNode = Node.newScript();
    rootNode.addChildToEnd(parentNode);
    parentNode.addChildToEnd(constructorFunctionNode);
    constructorFunctionNode.setJSDocInfo(JSDocInfo.constructors());

    boolean result = callback.shouldTraverse(traversal, constructorFunctionNode, parentNode);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node functionWithThisAnnotationNode = Node.newFunction("f", new int[0], null);
    Node parentNode = Node.newBlock();
    Node rootNode = Node.newScript();
    rootNode.addChildToEnd(parentNode);
    parentNode.addChildToEnd(functionWithThisAnnotationNode);
    functionWithThisAnnotationNode.setJSDocInfo(createJsDocInfoWithThisAnnotation());

    boolean result = callback.shouldTraverse(traversal, functionWithThisAnnotationNode, parentNode);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoParent() {
    Node functionNode = Node.newFunction("f", new int[0], null);

    boolean result = callback.shouldTraverse(traversal, functionNode, null);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    Node functionNode = Node.newFunction("f", new int[0], null);
    Node parentNode = Node.newBlock();

    boolean result = callback.shouldTraverse(traversal, functionNode, parentNode);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotTraverseFunctionLentToPrototype() {
    Node functionLentToPrototypeNode = Node.newFunction("prototypeFunction", new int[0], null);
    Node prototypeNode = Node.newObjectLiteral();
    prototypeNode.setProperty("prototype", Node.newObjectLiteral());
    prototypeNode.getProperty("prototype").setProperty("method", functionLentToPrototypeNode);

    Node parentNode = Node.newBlock();
    Node rootNode = Node.newScript();
    rootNode.addChildToEnd(parentNode);
    parentNode.addChildToEnd(prototypeNode);

    boolean result = callback.shouldTraverse(traversal, functionLentToPrototypeNode, parentNode);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldReportThisKeywordOnLeftSideOfAssignment() {
    Node assignNode = Node.newAssign(Node.newThis(), Node.newString("foo"));
    Node parentNode = Node.newBlock();
    Node rootNode = Node.newScript();
    rootNode.addChildToEnd(parentNode);
    parentNode.addChildToEnd(assignNode);

    Node targetNode = assignNode.getLeftHandSide();
    boolean result = callback.shouldReportThis(assignNode, parentNode);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldReportThisKeywordOnPropertyAccess() {
    Node getNode = Node.newGetProperty(Node.newThis(), "foo");
    Node parentNode = Node.newBlock();
    Node rootNode = Node.newScript();
    rootNode.addChildToEnd(parentNode);
    parentNode.addChildToEnd(getNode);

    boolean result = callback.shouldReportThis(getNode, parentNode);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotReportThisKeywordNotOnLeftSideOfAssignment() {
    Node assignNode = Node.newAssign(Node.newString("foo"), Node.newThis());
    Node parentNode = Node.newBlock();
    Node rootNode = Node.newScript();
    rootNode.addChildToEnd(parentNode);
    parentNode.addChildToEnd(assignNode);

    boolean result = callback.shouldReportThis(assignNode, parentNode);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotReportThisKeywordNotOnPropertyAccess() {
    Node getNode = Node.newGetProperty(Node.newString("foo"), Node.newThis());
    Node parentNode = Node.newBlock();
    Node rootNode = Node.newScript();
    rootNode.addChildToEnd(parentNode);
    parentNode.addChildToEnd(getNode);

    boolean result = callback.shouldReportThis(getNode, parentNode);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotReportThisKeywordInsidePrototypeMethod() {
    Node functionInsidePrototypeMethodNode = Node.newFunction("prototypeMethod", new int[0], null);
    Node prototypeNode = Node.newObjectLiteral();
    prototypeNode.setProperty("prototype", Node.newObjectLiteral());
    prototypeNode.getProperty("prototype").setProperty("method", functionInsidePrototypeMethodNode);
    Node parentNode = Node.newGetProperty(Node.newThis(), "prototype");
    Node rootNode = Node.newScript();
    rootNode.addChildToEnd(parentNode);
    parentNode.addChildToEnd(prototypeNode);

    boolean result = callback.shouldReportThis(functionInsidePrototypeMethodNode, parentNode);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotReportThisKeywordInsideFunctionAnnotatedWithConstructor() {
    Node functionAnnotatedWithConstructorNode = Node.newFunction("constructorFunction", new int[0], null);
    Node functionAnnotatedWithConstructorNodeWithJsDocInfo = createFunctionNodeWithJsDocInfo("constructorFunction", functionAnnotatedWithConstructorNode);
    Node parentNode = Node.newBlock();
    Node rootNode = Node.newScript();
    rootNode.addChildToEnd(parentNode);
    parentNode.addChildToEnd(functionAnnotatedWithConstructorNodeWithJsDocInfo);

    boolean result = callback.shouldReportThis(functionAnnotatedWithConstructorNodeWithJsDocInfo, parentNode);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldNotReportThisKeywordInsideFunctionAnnotatedWithThis() {
    Node functionAnnotatedWithThisNode = Node.newFunction("thisFunction", new int[0], null);
    Node functionAnnotatedWithThisNodeWithJsDocInfo = createFunctionNodeWithJsDocInfo("thisFunction", functionAnnotatedWithThisNode);
    Node parentNode = Node.newBlock();
    Node rootNode = Node.newScript();
    rootNode.addChildToEnd(parentNode);
    parentNode.addChildToEnd(functionAnnotatedWithThisNodeWithJsDocInfo);

    boolean result = callback.shouldReportThis(functionAnnotatedWithThisNodeWithJsDocInfo, parentNode);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldReportThisKeywordAtTopLevel() {
    Node topLevelThisNode = Node.newThis();
    Node parentNode = Node.newBlock();
    Node rootNode = Node.newScript();
    rootNode.addChildToEnd(parentNode);
    parentNode.addChildToEnd(topLevelThisNode);

    boolean result = callback.shouldReportThis(topLevelThisNode, parentNode);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldReportThisKeywordInsideFunctionWithoutParent() {
    Node functionWithoutParentNode = Node.newFunction("functionWithoutParent", new int[0], null);
    Node rootNode = Node.newScript();

    boolean result = callback.shouldReportThis(functionWithoutParentNode, null);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldReportThisKeywordInsideVariableDeclaration() {
    Node variableDeclarationNode = Node.newAssign(Node.newVariable("thisVariable"), Node.newThis());
    Node parentNode = Node.newBlock();
    Node rootNode = Node.newScript();
    rootNode.addChildToEnd(parentNode);
    parentNode.addChildToEnd(variableDeclarationNode);

    boolean result = callback.shouldReportThis(variableDeclarationNode, parentNode);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotReportThisKeywordInsideVariableDeclarationWithParent() {
    Node variableDeclarationNode = Node.newAssign(Node.newVariable("thisVariable"), Node.newThis());
    Node parentNode = Node.newBlock();
    Node rootNode = Node.newScript();
    rootNode.addChildToEnd(parentNode);
    Node variableDeclarationWithParentNode = Node.newAssign(parentNode, variableDeclarationNode);
    parentNode.addChildToEnd(variableDeclarationWithParentNode);

    boolean result = callback.shouldReportThis(variableDeclarationNode, parentNode);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldReportThisKeywordInsideFunctionWithLhsChild() {
    Node assignNode = Node.newAssign(Node.newThis(), Node.newString("foo"));
    Node functionNode = Node.newFunction("functionWithLhsChild", new int[0], null);
    Node functionNodeWithLhsChild = Node.newAssign(functionNode, assignNode);
    Node parentNode = Node.newBlock();
    Node rootNode = Node.newScript();
    rootNode.addChildToEnd(parentNode);
    parentNode.addChildToEnd(functionNodeWithLhsChild);

    boolean result = callback.shouldReportThis(assignNode, parentNode);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotReportThisKeywordInsideFunctionWithLhsChildButNotLeftSideOfAssignment() {
    Node assignNode = Node.newAssign(Node.newString("foo"), Node.newThis());
    Node functionNode = Node.newFunction("functionWithLhsChild", new int[0], null);
    Node functionNodeWithLhsChild = Node.newAssign(functionNode, assignNode);
    Node parentNode = Node.newBlock();
    Node rootNode = Node.newScript();
    rootNode.addChildToEnd(parentNode);
    parentNode.addChildToEnd(functionNodeWithLhsChild);

    boolean result = callback.shouldReportThis(assignNode, parentNode);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldReportThisKeywordInsideFunctionWithLhsChildButNotPropertyAccess() {
    Node assignNode = Node.newAssign(Node.newThis(), Node.newString("foo"));
    Node functionNode = Node.newFunction("functionWithLhsChild", new int[0], null);
    Node functionNodeWithLhsChild = Node.newAssign(functionNode, assignNode);
    Node parentNode = Node.newBlock();
    Node rootNode = Node.newScript();
    rootNode.addChildToEnd(parentNode);
    parentNode.addChildToEnd(functionNodeWithLhsChild);

    boolean result = callback.shouldReportThis(functionNode, parentNode);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldReportThisKeywordInsideFunctionWithNoLhsChild() {
    Node functionNode = Node.newFunction("functionWithNoLhsChild", new int[0], null);
    Node parentNode = Node.newBlock();
    Node rootNode = Node.newScript();
    rootNode.addChildToEnd(parentNode);
    parentNode.addChildToEnd(functionNode);

    boolean result = callback.shouldReportThis(functionNode, parentNode);
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldReportThisKeywordInsideFunctionWithNoLhsChildButNotLeftSideOfAssignment() {
    Node functionNode = Node.newFunction("functionWithNoLhsChild", new int[0], null);
    Node parentNode = Node.newBlock();
    Node rootNode = Node.newScript();
    rootNode.addChildToEnd(parentNode);
    parentNode.addChildToEnd(functionNode);

    boolean result = callback.shouldReportThis(functionNode, parentNode);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldReportThisKeywordInsideFunctionWithNoLhsChildButNotPropertyAccess() {
    Node functionNode = Node.newFunction("functionWithNoLhsChild", new int[0], null);
    Node parentNode = Node.newBlock();
    Node rootNode = Node.newScript();
    rootNode.addChildToEnd(parentNode);
    parentNode.addChildToEnd(functionNode);

    boolean result = callback.shouldReportThis(functionNode, parentNode);
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldReportGlobalThisWarning() {
    Node globalThisNode = Node.newThis();
    Node parentNode = Node.newBlock();
    Node rootNode = Node.newScript();
    rootNode.addChildToEnd(parentNode);
    parentNode.addChildToEnd(globalThisNode);

    callback.visit(traversal, globalThisNode, parentNode);
    DiagnosticType expectedDiagnosticType = DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object");
    assertThat(compiler.getDiagnostics()).containsExactly(expectedDiagnosticType);
  }

  private JSDocInfo createJsDocInfoWithThisAnnotation() {
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setThisType(new JSDocType(new String[]{"this"}));
    return jsDocInfo;
  }

  private Node createFunctionNodeWithJsDocInfo(String name, Node functionNode) {
    functionNode.setJSDocInfo(createJsDocInfoWithThisAnnotation());
    functionNode.setName(name);
    return functionNode;
  }
}