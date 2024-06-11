package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckLevel.Warning;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class CheckGlobalThisTest {
  private static final String FUNCTION_NAME = "myFunction";
  private static final String PROPERTY_NAME = "myProperty";
  private static final String PROTOTYPE_NAME = "prototype";

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeFactory nodeFactory;
  private NodeTraversal traversal;

  @Before
  public void setup() {
    nodeFactory = new NodeFactory();
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, Warning);
    traversal = new NodeTraversal(new CompilerOptions(), checker);
  }

  @Test
  public void shouldNotTraverseNonGlobalContexts() {
    Node functionNode = createFunctionNode();
    Node rootNode = createScriptNode();
    Node parentNode = createBlockNode();
    Node grandParentNode = createScriptNode();

    rootNode.addChild(grandParentNode);
    grandParentNode.addChild(parentNode);
    parentNode.addChild(functionNode);

    when(compiler.getOptions()).thenReturn(new CompilerOptions());

    traversal.traverse(rootNode, null);

    assertNull(checker.assignLhsChild);
  }

  @Test
  public void shouldTraverseFunctionsInGlobalContext() {
    Node functionNode = createFunctionNode();
    Node rootNode = createScriptNode();

    rootNode.addChild(functionNode);

    when(compiler.getOptions()).thenReturn(new CompilerOptions());

    traversal.traverse(rootNode, null);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    // Check that an error was reported for the function.
    when(compiler.report(eq(functionNode), eq(levelCaptor.capture()), eq(diagnosticTypeCaptor.capture())))
        .thenReturn(null);

    traversal.traverse(rootNode, null);

    assertThat(diagnosticTypeCaptor.getValue()).isEqualTo(CheckGlobalThis.GLOBAL_THIS);
  }

  @Test
  public void shouldReportGlobalThisOnLeftSideOfAssignment() {
    Node assignNode = createAssignmentNode("this", new Node(Token.NUM, null, "5"));
    Node rootNode = createScriptNode();
    Node parentNode = createBlockNode();

    rootNode.addChild(parentNode);
    parentNode.addChild(assignNode);

    when(compiler.getOptions()).thenReturn(new CompilerOptions());

    traversal.traverse(rootNode, null);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    // Check that an error was reported for the assignment node.
    when(compiler.report(eq(assignNode), eq(levelCaptor.capture()), eq(diagnosticTypeCaptor.capture())))
        .thenReturn(null);

    traversal.traverse(rootNode, null);

    assertThat(diagnosticTypeCaptor.getValue()).isEqualTo(CheckGlobalThis.GLOBAL_THIS);
  }

  @Test
  public void shouldReportGlobalThisOnLeftSideOfPropertyAccess() {
    Node getNode = createGetNode("this", PROPERTY_NAME);
    Node rootNode = createScriptNode();
    Node parentNode = createBlockNode();

    rootNode.addChild(parentNode);
    parentNode.addChild(getNode);

    when(compiler.getOptions()).thenReturn(new CompilerOptions());

    traversal.traverse(rootNode, null);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    // Check that an error was reported for the get node.
    when(compiler.report(eq(getNode), eq(levelCaptor.capture()), eq(diagnosticTypeCaptor.capture())))
        .thenReturn(null);

    traversal.traverse(rootNode, null);

    assertThat(diagnosticTypeCaptor.getValue()).isEqualTo(CheckGlobalThis.GLOBAL_THIS);
  }

  @Test
  public void shouldNotReportLocalThisOnLeftSideOfAssignment() {
    Node functionNode = createFunctionNode();
    Node assignNode = createAssignmentNode("this", new Node(Token.NAME, null, FUNCTION_NAME));
    Node rootNode = createScriptNode();

    rootNode.addChild(functionNode);
    functionNode.addChild(assignNode);

    when(compiler.getOptions()).thenReturn(new CompilerOptions());

    traversal.traverse(rootNode, null);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    // Check that no error was reported for the assignment node.
    when(compiler.report(eq(assignNode), eq(levelCaptor.capture()), eq(diagnosticTypeCaptor.capture())))
        .thenReturn(null);

    traversal.traverse(rootNode, null);

    assertNull(checker.assignLhsChild);
  }

  @Test
  public void shouldNotReportLocalThisOnLeftSideOfPropertyAccess() {
    Node functionNode = createFunctionNode();
    Node getNode = createGetNode("this", PROPERTY_NAME);
    Node rootNode = createScriptNode();

    rootNode.addChild(functionNode);
    functionNode.addChild(getNode);

    when(compiler.getOptions()).thenReturn(new CompilerOptions());

    traversal.traverse(rootNode, null);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    // Check that no error was reported for the get node.
    when(compiler.report(eq(getNode), eq(levelCaptor.capture()), eq(diagnosticTypeCaptor.capture())))
        .thenReturn(null);

    traversal.traverse(rootNode, null);

    assertNull(checker.assignLhsChild);
  }

  @Test
  public void shouldReportGlobalThisOnPrototype() {
    Node functionNode = createFunctionNode();
    Node prototypeNode = createObjectLiteralNode();
    Node getPrototypeNode = createGetNode("this", PROTOTYPE_NAME);
    Node rootNode = createScriptNode();

    rootNode.addChild(functionNode);
    functionNode.addChild(prototypeNode);
    prototypeNode.addChild(getPrototypeNode);

    when(compiler.getOptions()).thenReturn(new CompilerOptions());

    traversal.traverse(rootNode, null);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    // Check that no error was reported for the get node on the prototype.
    when(compiler.report(eq(getPrototypeNode), eq(levelCaptor.capture()), eq(diagnosticTypeCaptor.capture())))
        .thenReturn(null);

    traversal.traverse(rootNode, null);

    assertNull(checker.assignLhsChild);
  }

  @Test
  public void shouldReportGlobalThisOnSubProperty() {
    Node functionNode = createFunctionNode();
    Node prototypeNode = createObjectLiteralNode();
    Node getPrototypeNode = createGetNode("this", PROTOTYPE_NAME);
    Node getSubPropertyNode = createGetNode(getPrototypeNode, PROPERTY_NAME);
    Node rootNode = createScriptNode();

    rootNode.addChild(functionNode);
    functionNode.addChild(prototypeNode);
    prototypeNode.addChild(getPrototypeNode);
    getPrototypeNode.addChild(getSubPropertyNode);

    when(compiler.getOptions()).thenReturn(new CompilerOptions());

    traversal.traverse(rootNode, null);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    // Check that an error was reported for the sub-property get node.
    when(compiler.report(eq(getSubPropertyNode), eq(levelCaptor.capture()), eq(diagnosticTypeCaptor.capture())))
        .thenReturn(null);

    traversal.traverse(rootNode, null);

    assertThat(diagnosticTypeCaptor.getValue()).isEqualTo(CheckGlobalThis.GLOBAL_THIS);
  }

  /** Creates a script node. */
  private Node createScriptNode() {
    return new Node(Token.SCRIPT, null, null);
  }

  /** Creates a block node. */
  private Node createBlockNode() {
    return new Node(Token.BLOCK, null, null);
  }

  /** Creates a function node. */
  private Node createFunctionNode() {
    return new Node(Token.FUNCTION, null, null);
  }

  /** Creates an assignment node. */
  private Node createAssignmentNode(String value, Node rhs) {
    return new Node(Token.ASSIGN, new Node(Token.NAME, null, FUNCTION_NAME), rhs);
  }

  /** Creates a get node. */
  private Node createGetNode(Node obj, String propertyName) {
    return new Node(Token.GETPROP, obj, new Node(Token.NAME, null, propertyName));
  }

  /** Creates an object literal node. */
  private Node createObjectLiteralNode() {
    return new Node(Token.OBJECT, null, null);
  }
}