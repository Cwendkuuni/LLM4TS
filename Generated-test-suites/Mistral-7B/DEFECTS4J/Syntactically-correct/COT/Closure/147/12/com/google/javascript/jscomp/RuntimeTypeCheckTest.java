import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

/**
 * Tests for {@link RuntimeTypeCheck} class.
 */
public class RuntimeTypeCheckTest {

  private AbstractCompiler compiler;
  private RuntimeTypeCheck runtimeTypeCheck;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
  }

  @Test
  public void process_normalCase() {
    // Given
    Node rootNode = createSampleCodeNode();

    // When
    runtimeTypeCheck.process(null, rootNode);

    // Then
    verifyAddCheckerNodesForParameters(rootNode);
    verifyAddCheckerNodesForReturnValue(rootNode);
  }

  @Test
  public void process_classWithInterfaceImplementation() {
    // Given
    Node rootNode = createSampleCodeNodeWithClassAndInterface();

    // When
    runtimeTypeCheck.process(null, rootNode);

    // Then
    verifyAddCheckerNodesForParameters(rootNode);
    verifyAddMarkerNodesForInterfaceImplementation(rootNode);
  }

  @Test(expected = AssertionError.class)
  public void process_unknownType() {
    // Given
    Node rootNode = createSampleCodeNodeWithUnknownType();

    // When
    runtimeTypeCheck.process(null, rootNode);

    // Then
    // This test should fail because of the unknown type.
  }

  @Test(expected = AssertionError.class)
  public void process_nullType() {
    // Given
    Node rootNode = createSampleCodeNodeWithNullType();

    // When
    runtimeTypeCheck.process(null, rootNode);

    // Then
    // This test should fail because of the null type.
  }

  @Test
  public void addBoilerplateCode_noLogFunction() {
    // Given
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    // When
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    Node js = runtimeTypeCheck.addBoilerplateCode(compiler, null);

    // Then
    assertThat(js).isNotNull();
  }

  @Test
  public void addBoilerplateCode_withLogFunction() {
    // Given
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    String logFunction = "logFunction";

    // When
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);
    Node js = runtimeTypeCheck.addBoilerplateCode(compiler, logFunction);

    // Then
    assertThat(js).isNotNull();

    ArgumentCaptor<String> logFunctionArgCaptor = ArgumentCaptor.forClass(String.class);
    Node logFunctionNode = NodeUtil.newQualifiedNameNode("logFunction", -1, -1);
    when(compiler.getNodeForCodeInsertion(null)).thenReturn(logFunctionNode);
    runtimeTypeCheck.addBoilerplateCode(compiler, logFunction);
    verify(compiler).addNodeBefore(logFunctionNode, js);
    verify(compiler).addNodeAfter(logFunctionNode, js);
  }

  private Node createSampleCodeNode() {
    return NodeUtil.parseJsFile("sample_code.js");
  }

  private Node createSampleCodeNodeWithClassAndInterface() {
    return NodeUtil.parseJsFile("sample_code_with_class_and_interface.js");
  }

  private Node createSampleCodeNodeWithUnknownType() {
    return NodeUtil.parseJsFile("sample_code_with_unknown_type.js");
  }

  private Node createSampleCodeNodeWithNullType() {
    return NodeUtil.parseJsFile("sample_code_with_null_type.js");
  }

  private void verifyAddCheckerNodesForParameters(Node rootNode) {
    FunctionType functionType = (FunctionType) rootNode.getFirstChild().getJSType();
    for (int i = 0; i < functionType.getParameters().size(); i++) {
      Node parameterNode =
          (Node) Iterables.get(rootNode.getFirstChild().getChildren(), i + 1);
      Node checkerNode = findCheckerNodeForParameter(parameterNode);
      assertThat(checkerNode).isNotNull();
    }
  }

  private void verifyAddCheckerNodesForReturnValue(Node rootNode) {
    FunctionType functionType = (FunctionType) rootNode.getFirstChild().getJSType();
    Node returnNode = rootNode.getLastChild();
    Node checkerNode = findCheckerNodeForReturnValue(returnNode);
    assertThat(checkerNode).isNotNull();
  }

  private void verifyAddMarkerNodesForInterfaceImplementation(Node rootNode) {
    FunctionType functionType = (FunctionType) rootNode.getFirstChild().getJSType();
    String className = NodeUtil.getFunctionName(functionType.getSource());
    ObjectType interfaceType = new ObjectType();
    interfaceType.setReferenceName("IInterface");
    TreeSet<ObjectType> stuff = Sets.newTreeSet(RuntimeTypeCheck.ALPHA);
    stuff.add(interfaceType);
    for (Node markerNode : findMarkerNodesForInterfaceImplementation(rootNode)) {
      ObjectType markerObjectType = markerNode.getJSType();
      assertThat(markerObjectType).isInstanceOf(ObjectType.class);
      ObjectType markerInterfaceType = ((ObjectType) markerObjectType).getInterface();
      assertThat(markerInterfaceType).isEqualTo(interfaceType);
      assertThat(markerNode.getParent().getParent().getChildren())
          .contains(markerNode);
      assertThat(markerNode.getParent().getParent().getLastChild())
          .isEqualTo(functionType.getConstructor());
      assertThat(markerNode.getParent())
          .isInstanceOf(Node.class)
          .satisfies(n -> n.getType() == Token.GETPROP)
          .satisfies(n -> NodeUtil.getQualifiedName(n).equals("prototype"));
    }
  }

  private Node findCheckerNodeForParameter(Node parameterNode) {
    Node checkerNode = null;
    for (Node child : parameterNode.getChildren()) {
      if (child.getType() == Token.CALL &&
          "checkType".equals(NodeUtil.getFunctionName(child.getFirstChild()))) {
        checkerNode = child;
        break;
      }
    }
    return checkerNode;
  }

  private Node findCheckerNodeForReturnValue(Node returnNode) {
    Node checkerNode = null;
    for (Node child : returnNode.getChildren()) {
      if (child.getType() == Token.CALL &&
          "checkType".equals(NodeUtil.getFunctionName(child.getFirstChild()))) {
        checkerNode = child;
        break;
      }
    }
    return checkerNode;
  }

  private Iterable<Node> findMarkerNodesForInterfaceImplementation(Node rootNode) {
    TreeSet<Node> markerNodes = Sets.newTreeSet();
    NodeTraversal.traverse(compiler, rootNode, new AddMarkerFinders(compiler));
    return markerNodes;
  }

  private static class AddMarkerFinders
      extends NodeTraversal.AbstractPostOrderCallback {

    private final AbstractCompiler compiler;

    private AddMarkerFinders(AbstractCompiler compiler) {
      this.compiler = compiler;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (NodeUtil.isFunction(n)) {
        visitFunction(t, n);
      }
    }

    private void visitFunction(NodeTraversal t, Node n) {
      FunctionType funType = (FunctionType) n.getJSType();
      if (!funType.isConstructor()) {
        return;
      }
      Node markerNode = findMarkerNode(n);
      if (markerNode != null) {
        compiler.addMarkerNodeForTesting(markerNode);
      }
    }

    private Node findMarkerNode(Node n) {
      Node markerNode = null;
      for (Node child : n.getChildren()) {
        if (child.getType() == Token.GETPROP &&
            child.getFirstChild().getType() == Token.GETPROP &&
            "prototype".equals(child.getFirstChild().getLastChild().getTokenValue())) {
          String markerName = NodeUtil.getQualifiedName(child.getLastChild())
              .split("__")[1];
          if ("instance_of__"
              .equals(markerName) || markerName.startsWith("implements__")) {
            markerNode = child;
            break;
          }
        }
      }
      return markerNode;
    }
  }
}