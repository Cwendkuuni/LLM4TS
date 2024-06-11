import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {

  @Mock
  private AbstractCompiler compiler;
  @Mock
  private Checker checker;
  @Mock
  private CompilerOptions options;
  @Mock
  private DiagnosticType diagnosticType;
  @Mock
  private NodeFactory nodeFactory;
  @Mock
  private Parser parser;

  private CheckGlobalThis checkerUnderTest;

  @Before
  public void setUp() {
    when(compiler.getOptions()).thenReturn(options);
    when(options.getCheckGlobalThis()).thenReturn(WARNING);

    checkerUnderTest = new CheckGlobalThis(compiler, WARNING);
  }

  @Test
  public void shouldReportUseOfThisOnLeftSideOfAssignment() {
    Node assignmentNode = nodeFactory.createNode(Token.ASSIGN, null, null, null, null);
    Node leftHandSide = nodeFactory.createNode(Token.THIS, null, null, null, null);
    assignmentNode.setFirstChild(leftHandSide);

    Node tree = parser.parseJavaScript("function foo() { this = 1; }", null, nodeFactory);

    NodeTraversal traversal = new NodeTraversal(tree, checkerUnderTest, new NodeFactory(), new Checker.Phase());
    traversal.traverse(tree);

    verify(compiler).report(argThat(d -> d.getCode() == diagnosticType.getCode() &&
        d.getMessage().contains("JSC_USED_GLOBAL_THIS") &&
        d.getSourceName().equals("foo")))
        .atLeastOnce();
  }

  @Test
  public void shouldNotReportUseOfThisOnRightSideOfAssignment() {
    Node assignmentNode = nodeFactory.createNode(Token.ASSIGN, null, null, null, null);
    Node leftHandSide = nodeFactory.createNode(Token.NAME, "a", null, null, null);
    Node rightHandSide = nodeFactory.createNode(Token.THIS, null, null, null, null);
    assignmentNode.setFirstChild(leftHandSide);
    assignmentNode.setNext(rightHandSide);

    Node tree = parser.parseJavaScript("function foo() { a = this; }", null, nodeFactory);

    NodeTraversal traversal = new NodeTraversal(tree, checkerUnderTest, new NodeFactory(), new Checker.Phase());
    traversal.traverse(tree);

    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void shouldReportUseOfThisInPropertyAccess() {
    Node getPropertyNode = nodeFactory.createNode(Token.GETPROP, null, null, null, null);
    Node thisNode = nodeFactory.createNode(Token.THIS, null, null, null, null);
    getPropertyNode.setFirstChild(thisNode);

    Node assignmentNode = nodeFactory.createNode(Token.ASSIGN, null, null, null, null);
    Node leftHandSide = nodeFactory.createNode(Token.NAME, "a", null, null, null);
    assignmentNode.setFirstChild(leftHandSide);
    assignmentNode.setNext(getPropertyNode);

    Node tree = parser.parseJavaScript("function foo() { a.this = 1; }", null, nodeFactory);

    NodeTraversal traversal = new NodeTraversal(tree, checkerUnderTest, new NodeFactory(), new Checker.Phase());
    traversal.traverse(tree);

    verify(compiler).report(argThat(d -> d.getCode() == diagnosticType.getCode() &&
        d.getMessage().contains("JSC_USED_GLOBAL_THIS") &&
        d.getSourceName().equals("foo")))
        .atLeastOnce();
  }

  @Test
  public void shouldNotReportUseOfThisInPropertyAccessOnPrototype() {
    Node getPropertyNode = nodeFactory.createNode(Token.GETPROP, null, null, null, null);
    Node prototypeNode = nodeFactory.createNode(Token.GETPROP, null, null, null, null);
    prototypeNode.setLastChild(new Node(Token.THIS, null, null, null, null));
    getPropertyNode.setFirstChild(prototypeNode);

    Node assignmentNode = nodeFactory.createNode(Token.ASSIGN, null, null, null, null);
    Node leftHandSide = nodeFactory.createNode(Token.NAME, "a", null, null, null);
    assignmentNode.setFirstChild(leftHandSide);
    assignmentNode.setNext(getPropertyNode);

    Node tree = parser.parseJavaScript("function foo() { a.prototype.this = 1; }", null, nodeFactory);

    NodeTraversal traversal = new NodeTraversal(tree, checkerUnderTest, new NodeFactory(), new Checker.Phase());
    traversal.traverse(tree);

    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void shouldReportUseOfThisInFunctionCall() {
    Node functionCallNode = nodeFactory.createNode(Token.CALL, null, null, null, null);
    Node functionNode = nodeFactory.createNode(Token.FUNCTION, null, null, null, null);
    Node thisNode = nodeFactory.createNode(Token.THIS, null, null, null, null);
    functionNode.setFirstChild(thisNode);
    functionCallNode.setFirstChild(functionNode);

    Node assignmentNode = nodeFactory.createNode(Token.ASSIGN, null, null, null, null);
    Node leftHandSide = nodeFactory.createNode(Token.NAME, "a", null, null, null);
    assignmentNode.setFirstChild(leftHandSide);
    assignmentNode.setNext(functionCallNode);

    Node tree = parser.parseJavaScript("function foo() { a(this); }", null, nodeFactory);

    NodeTraversal traversal = new NodeTraversal(tree, checkerUnderTest, new NodeFactory(), new Checker.Phase());
    traversal.traverse(tree);

    verify(compiler).report(argThat(d -> d.getCode() == diagnosticType.getCode() &&
        d.getMessage().contains("JSC_USED_GLOBAL_THIS") &&
        d.getSourceName().equals("foo")))
        .atLeastOnce();
  }

  @Test
  public void shouldNotReportUseOfThisInConstructor() {
    Node constructorNode = nodeFactory.createNode(Token.FUNCTION, null, null, null, null);
    constructorNode.setIsConstructor(true);
    Node thisNode = nodeFactory.createNode(Token.THIS, null, null, null, null);
    constructorNode.setFirstChild(thisNode);

    Node tree = parser.parseJavaScript("function Foo() { this.x = 1; }", null, nodeFactory);

    NodeTraversal traversal = new NodeTraversal(tree, checkerUnderTest, new NodeFactory(), new Checker.Phase());
    traversal.traverse(tree);

    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void shouldReportUseOfThisInPrototypeMethod() {
    Node constructorNode = nodeFactory.createNode(Token.FUNCTION, null, null, null, null);
    constructorNode.setIsConstructor(true);

    Node prototypeNode = nodeFactory.createNode(Token.GETPROP, null, null, null, null);
    prototypeNode.setLastChild(new Node(Token.THIS, null, null, null, null));
    constructorNode.setFirstChild(prototypeNode);

    Node getterNode = nodeFactory.createNode(Token.FUNCTION, null, null, null, null);
    getterNode.setIsGetter(true);
    getterNode.setNext(new Node(Token.THIS, null, null, null, null));

    Node assignmentNode = nodeFactory.createNode(Token.ASSIGN, null, null, null, null);
    Node leftHandSide = nodeFactory.createNode(Token.NAME, "a", null, null, null);
    assignmentNode.setFirstChild(leftHandSide);
    assignmentNode.setNext(getterNode);

    Node tree = parser.parseJavaScript("function Foo() { this.prototype.get x() { this.x = 1; } }", null, nodeFactory);

    NodeTraversal traversal = new NodeTraversal(tree, checkerUnderTest, new NodeFactory(), new Checker.Phase());
    traversal.traverse(tree);

    verifyNoMoreInteractions(compiler);

    Node prototype = tree.getFirstChild();
    Node method = prototype.getFirstChild();

    checkerUnderTest = new CheckGlobalThis(compiler, ERROR);

    traversal = new NodeTraversal(tree, checkerUnderTest, new NodeFactory(), new Checker.Phase());
    traversal.traverse(tree);

    verify(compiler).report(argThat(d -> d.getCode() == diagnosticType.getCode() &&
        d.getMessage().contains("JSC_USED_GLOBAL_THIS") &&
        d.getSourceName().equals("Foo.get x")))
        .atLeastOnce();
  }

  @Test
  public void shouldReportUseOfThisInPrototypeMethodOnNonPrototypeProperty() {
    Node constructorNode = nodeFactory.createNode(Token.FUNCTION, null, null, null, null);
    constructorNode.setIsConstructor(true);

    Node prototypeNode = nodeFactory.createNode(Token.GETPROP, null, null, null, null);
    prototypeNode.setLastChild(new Node(Token.THIS, null, null, null, null));
    constructorNode.setFirstChild(prototypeNode);

    Node getterNode = nodeFactory.createNode(Token.FUNCTION, null, null, null, null);
    getterNode.setIsGetter(true);
    getterNode.setNext(new Node(Token.THIS, null, null, null, null));

    Node assignmentNode = nodeFactory.createNode(Token.ASSIGN, null, null, null, null);
    Node leftHandSide = nodeFactory.createNode(Token.NAME, "a", null, null, null);
    assignmentNode.setFirstChild(leftHandSide);
    assignmentNode.setNext(getterNode);

    Node tree = parser.parseJavaScript("function Foo() { this.prototype.x = function() { this.x = 1; } }", null, nodeFactory);

    NodeTraversal traversal = new NodeTraversal(tree, checkerUnderTest, new NodeFactory(), new Checker.Phase());
    traversal.traverse(tree);

    verify(compiler).report(argThat(d -> d.getCode() == diagnosticType.getCode() &&
        d.getMessage().contains("JSC_USED_GLOBAL_THIS") &&
        d.getSourceName().equals("Foo.x")))
        .atLeastOnce();
  }

  @Test
  public void shouldNotReportUseOfThisInFunctionAnnotatedWithAtThis() {
    Node functionNode = nodeFactory.createNode(Token.FUNCTION, null, null, null, null);
    Node thisAnnotationNode = nodeFactory.createNode(Token.ANNOTATION, null, null, null, null);
    thisAnnotationNode.setLastChild(new Node(Token.STRING_LITERAL, "@this", null, null, null));
    functionNode.addChildToEnd(thisAnnotationNode);

    Node thisNode = nodeFactory.createNode(Token.THIS, null, null, null, null);
    functionNode.setFirstChild(thisNode);

    Node tree = parser.parseJavaScript("function @this Foo() { this.x = 1; }", null, nodeFactory);

    NodeTraversal traversal = new NodeTraversal(tree, checkerUnderTest, new NodeFactory(), new Checker.Phase());
    traversal.traverse(tree);

    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void shouldNotReportUseOfThisInFunctionWhereNoLogicalPlaceToPutAtThisAnnotation() {
    Node functionNode = nodeFactory.createNode(Token.FUNCTION, null, null, null, null);

    Node assignmentNode = nodeFactory.createNode(Token.ASSIGN, null, null, null, null);
    Node leftHandSide = nodeFactory.createNode(Token.NAME, "a", null, null, null);
    assignmentNode.setFirstChild(leftHandSide);
    assignmentNode.setNext(functionNode);

    Node thisNode = nodeFactory.createNode(Token.THIS, null, null, null, null);
    functionNode.setFirstChild(thisNode);

    Node tree = parser.parseJavaScript("var a = function() { this.x = 1; }", null, nodeFactory);

    NodeTraversal traversal = new NodeTraversal(tree, checkerUnderTest, new NodeFactory(), new Checker.Phase());
    traversal.traverse(tree);

    verifyNoMoreInteractions(compiler);
  }
}