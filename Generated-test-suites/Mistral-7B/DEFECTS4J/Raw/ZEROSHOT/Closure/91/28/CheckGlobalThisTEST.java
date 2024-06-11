import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.powermock.PowerMock.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest({CheckGlobalThis.class})
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseFunctionWithoutJsDoc() {
    Node functionNode = newNode("function foo() {}");
    Node parentNode = newNode("var bar = function foo() {}");
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);

    when(nodeTraversal.getParent()).thenReturn(parentNode);
    when(nodeTraversal.makeError(any(Node.class), any(CheckLevel.class), any(DiagnosticType.class)))
        .thenReturn(newNode(""));

    assertThat(checker.shouldTraverse(nodeTraversal, functionNode, nodeTraversal.getParent()))
        .isTrue();
  }

  @Test
  public void testShouldNotTraverseFunctionWithJsDocConstructor() {
    Node functionNode = newNode("function Foo() {}");
    Node parentNode = newNode("class Foo {}");
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);

    when(nodeTraversal.getParent()).thenReturn(parentNode);

    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setIsConstructor(true);
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    assertThat(checker.shouldTraverse(nodeTraversal, functionNode, nodeTraversal.getParent()))
        .isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithJsDocInterface() {
    Node functionNode = newNode("function Foo() {}");
    Node parentNode = newNode("interface Foo {}");
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);

    when(nodeTraversal.getParent()).thenReturn(parentNode);

    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setIsInterface(true);
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    assertThat(checker.shouldTraverse(nodeTraversal, functionNode, nodeTraversal.getParent()))
        .isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithJsDocThis() {
    Node functionNode = newNode("function Foo() { this.bar = 1; }");
    Node parentNode = newNode("function Foo() {}");
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);

    when(nodeTraversal.getParent()).thenReturn(parentNode);

    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setHasThisType(true);
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    assertThat(checker.shouldTraverse(nodeTraversal, functionNode, nodeTraversal.getParent()))
        .isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoPlausibleJsDoc() {
    Node functionNode = newNode("function foo() {}");
    Node parentNode = newNode("var bar = function foo() {}");
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);

    when(nodeTraversal.getParent()).thenReturn(parentNode);
    when(nodeTraversal.makeError(any(Node.class), any(CheckLevel.class), any(DiagnosticType.class)))
        .thenReturn(newNode(""));

    assertThat(checker.shouldTraverse(nodeTraversal, functionNode, nodeTraversal.getParent()))
        .isTrue();
  }

  @Test
  public void testShouldNotTraverseFunctionGettingLentToPrototype() {
    Node functionNode = newNode("function foo() {}");
    Node parentNode = newNode("function bar() { Object.defineProperty(this, 'foo', { get: function() { return foo; } }); }");
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);

    when(nodeTraversal.getParent()).thenReturn(parentNode);

    assertThat(checker.shouldTraverse(nodeTraversal, functionNode, nodeTraversal.getParent()))
        .isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionGettingLentToPrototypeDeep() {
    Node functionNode = newNode("function foo() {}");
    Node parentNode = newNode("function bar() { Object.defineProperty(this, 'baz', { get: function() { return this.foo; } }); }");
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);

    when(nodeTraversal.getParent()).thenReturn(parentNode);

    assertThat(checker.shouldTraverse(nodeTraversal, functionNode, nodeTraversal.getParent()))
        .isFalse();
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assignNode = newNode("var a = this;");
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);

    assertThat(checker.shouldReportThis(nodeTraversal, assignNode.getFirstChild(), assignNode))
        .isTrue();
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    Node assignNode = newNode("var a = {get x() { return this; }};");
    Node propertyAccessNode = newNode("a.x");
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);

    assertThat(checker.shouldReportThis(nodeTraversal, propertyAccessNode, assignNode))
        .isTrue();
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node assignNode = newNode("var a = this;");
    Node rhsNode = newNode("1");
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);

    when(nodeTraversal.makeError(any(Node.class), any(CheckLevel.class), any(DiagnosticType.class)))
        .thenReturn(newNode(""));

    assertThat(checker.shouldReportThis(nodeTraversal, rhsNode, assignNode))
        .isFalse();
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignmentDeep() {
    Node assignNode = newNode("var a = {get x() { return this; }};");
    Node rhsNode = newNode("{ get y() { return this; } }");
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);

    when(nodeTraversal.makeError(any(Node.class), any(CheckLevel.class), any(DiagnosticType.class)))
        .thenReturn(newNode(""));

    assertThat(checker.shouldReportThis(nodeTraversal, rhsNode, assignNode))
        .isFalse();
  }

  private Node newNode(String nodeString) {
    return new Node(new Token(Token.TOKEN_UNKNOWN, 0, 0), null, null, nodeString);
  }

  private boolean isGet(Node node) {
    return NodeUtil.isGet(node);
  }
}