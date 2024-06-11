package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CheckGlobalThisTest {

  @Mock
  private AbstractCompiler compiler;

  @InjectMocks
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testShouldTraverseFunctionWithNoParent() {
    Node functionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of(), Token.BLOCK, null, null);
    Node parent = null;

    when(checker.shouldTraverse(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(functionNode), ArgumentMatchers.eq(parent)))
        .thenReturn(true);

    // Act
    boolean result = checker.shouldTraverse(mock(NodeTraversal.class), functionNode, parent);

    // Assert
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverseFunctionWithParentThatIsAssignment() {
    Node assignmentNode = Node.newAssignment(Token.ASSIGN, null, Node.newName("a"), Node.newName("this"), null);
    Node functionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of(), Token.BLOCK, null, null);

    Node parent = assignmentNode;

    when(checker.shouldTraverse(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(functionNode), ArgumentMatchers.eq(parent)))
        .thenReturn(true);

    // Act
    boolean result = checker.shouldTraverse(mock(NodeTraversal.class), functionNode, parent);

    // Assert
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverseFunctionWithParentThatIsFunction() {
    Node parentFunctionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of(), Token.BLOCK, null, null);
    Node functionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of(), Token.BLOCK, null, null);

    when(checker.shouldTraverse(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(functionNode), ArgumentMatchers.eq(parentFunctionNode)))
        .thenReturn(true);

    // Act
    boolean result = checker.shouldTraverse(mock(NodeTraversal.class), functionNode, parentFunctionNode);

    // Assert
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverseFunctionWithParentThatIsObjectLiteral() {
    Node objectLiteralNode = Node.newObjectLiteral(Token.OBJECT, null, null, null);
    Node functionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of(), Token.BLOCK, null, null);

    when(checker.shouldTraverse(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(functionNode), ArgumentMatchers.eq(objectLiteralNode)))
        .thenReturn(true);

    // Act
    boolean result = checker.shouldTraverse(mock(NodeTraversal.class), functionNode, objectLiteralNode);

    // Assert
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverseFunctionWithParentThatIsPrototypeProperty() {
    Node prototypePropertyNode =
        Node.newGetProp(Token.GETPROP, null, Node.newName("prototype"), null, null);
    Node functionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of(), Token.BLOCK, null, null);

    when(checker.shouldTraverse(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(functionNode), ArgumentMatchers.eq(prototypePropertyNode)))
        .thenReturn(false);

    // Act
    boolean result = checker.shouldTraverse(mock(NodeTraversal.class), functionNode, prototypePropertyNode);

    // Assert
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverseFunctionWithParentThatIsPrototypeSubProperty() {
    Node prototypeSubPropertyNode =
        Node.newGetProp(Token.GETPROP, null, Node.newName("prototype"), Node.newName("subProperty"), null);
    Node functionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of(), Token.BLOCK, null, null);

    when(checker.shouldTraverse(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(functionNode), ArgumentMatchers.eq(prototypeSubPropertyNode)))
        .thenReturn(false);

    // Act
    boolean result = checker.shouldTraverse(mock(NodeTraversal.class), functionNode, prototypeSubPropertyNode);

    // Assert
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverseFunctionWithParentThatIsVariableDeclaration() {
    Node variableDeclarationNode = Node.newVar("a", null, Node.newFunction(Token.FUNCTION, null, ImmutableList.of(), Token.BLOCK, null, null), null, null);
    Node functionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of(), Token.BLOCK, null, null);

    when(checker.shouldTraverse(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(functionNode), ArgumentMatchers.eq(variableDeclarationNode)))
        .thenReturn(true);

    // Act
    boolean result = checker.shouldTraverse(mock(NodeTraversal.class), functionNode, variableDeclarationNode);

    // Assert
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverseFunctionWithParentThatIsVariableDeclarationWithFunctionValue() {
    Node variableDeclarationNode =
        Node.newVar("a", null, null, Node.newFunction(Token.FUNCTION, null, ImmutableList.of(), Token.BLOCK, null, null), null, null);
    Node functionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of(), Token.BLOCK, null, null);

    when(checker.shouldTraverse(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(functionNode), ArgumentMatchers.eq(variableDeclarationNode)))
        .thenReturn(true);

    // Act
    boolean result = checker.shouldTraverse(mock(NodeTraversal.class), functionNode, variableDeclarationNode);

    // Assert
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assignmentNode = Node.newAssignment(Token.ASSIGN, null, Node.newName("a"), Node.newThis(), null);

    when(checker.shouldReportThis(ArgumentMatchers.eq(assignmentNode), ArgumentMatchers.eq(null)))
        .thenReturn(true);

    // Act
    boolean result = checker.shouldReportThis(mock(NodeTraversal.class), null, assignmentNode);

    // Assert
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node assignmentNode = Node.newAssignment(Token.ASSIGN, null, Node.newName("a"), Node.newName("this"), null);

    when(checker.shouldReportThis(ArgumentMatchers.eq(Node.newName("this")), ArgumentMatchers.eq(assignmentNode)))
        .thenReturn(false);

    // Act
    boolean result = checker.shouldReportThis(mock(NodeTraversal.class), assignmentNode, null);

    // Assert
    assertThat(result).isFalse();
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    Node propertyAccessNode = Node.newGetProp(Token.GETPROP, null, Node.newName("a"), Node.newName("this"), null);

    when(checker.shouldReportThis(ArgumentMatchers.eq(propertyAccessNode), ArgumentMatchers.eq(null)))
        .thenReturn(true);

    // Act
    boolean result = checker.shouldReportThis(mock(NodeTraversal.class), null, propertyAccessNode);

    // Assert
    assertThat(result).isTrue();
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfPropertyAccess() {
    Node propertyAccessNode = Node.newGetProp(Token.GETPROP, null, Node.newName("a"), Node.newName("this"), null);

    when(checker.shouldReportThis(ArgumentMatchers.eq(Node.newName("this")), ArgumentMatchers.eq(propertyAccessNode)))
        .thenReturn(false);

    // Act
    boolean result = checker.shouldReportThis(mock(NodeTraversal.class), propertyAccessNode, null);

    // Assert
    assertThat(result).isFalse();
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    Node functionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of(), Token.BLOCK, null, null);

    JSDocInfo result = checker.getFunctionJsDocInfo(functionNode);

    // Assert
    assertThat(result).isNull();
  }

  @Test
  public void testGetFunctionJsDocInfoWithConstructorAnnotation() {
    Node functionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of(), null, null, null);
    Node constructorAnnotationNode = Node.newAnnotation(Token.ANNOTATION, null, "constructor", null);
    Node constructorAnnotationValueNode = Node.newName("MyConstructor");
    Node constructorAnnotationValueListNode = Node.newList(Token.LIST, null, ImmutableList.of(constructorAnnotationValueNode));

    Node functionAnnotationNode = Node.newMember(Token.MEMBER, null, "constructor", constructorAnnotationNode, constructorAnnotationValueListNode, null);
    functionNode.setFirstChild(functionAnnotationNode);

    JSDocInfo result = checker.getFunctionJsDocInfo(functionNode);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getAnnotations()).containsExactly(constructorAnnotationNode);
  }

  @Test
  public void testGetFunctionJsDocInfoWithThisAnnotation() {
    Node functionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of(), Token.BLOCK, null, null);
    Node thisAnnotationNode = Node.newAnnotation(Token.ANNOTATION, null, "this", null);

    Node functionAnnotationNode = Node.newMember(Token.MEMBER, null, "this", thisAnnotationNode, null, null);
    functionNode.setFirstChild(functionAnnotationNode);

    JSDocInfo result = checker.getFunctionJsDocInfo(functionNode);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getThisType()).isEqualTo("this");
  }

  @Test
  public void testGetFunctionJsDocInfoWithNoAnnotation() {
    Node functionNode = Node.newFunction(Token.FUNCTION, null, ImmutableList.of(), Token.BLOCK, null, null);

    JSDocInfo result = checker.getFunctionJsDocInfo(functionNode);

    // Assert
    assertThat(result).isNull();
  }

  @Test
  public void testVisit() {
    Node assignmentNode = Node.newAssignment(Token.ASSIGN, null, Node.newName("a"), Node.newName("this"), null);

    when(checker.shouldReportThis(ArgumentMatchers.eq(assignmentNode), ArgumentMatchers.eq(null)))
        .thenReturn(true);

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);

    CheckLevel level = CheckLevel.WARNING;
    DiagnosticType diagnosticType = DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object");

    checker.visit(nodeTraversal, null, assignmentNode);

    // Assert
    when(compiler.report(ArgumentMatchers.any(NodeTraversal.class), ArgumentMatchers.eq(level), ArgumentMatchers.eq(diagnosticType)))
        .thenCallRealMethod();

    verify(compiler).report(nodeTraversal, level, diagnosticType);
  }
}