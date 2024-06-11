import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InvocationOnMock;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

public class RuntimeTypeCheckTest {

  private AbstractCompiler compiler;
  private CompilerInput input;
  private FunctionType functionType;
  private Node functionNode;
  private ObjectType objectType;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    input = mock(CompilerInput.class);
    when(compiler.getInput(anyString())).thenReturn(input);

    functionType = new FunctionType(
        Token.ANY, ImmutableList.of(), Token.VOID, null);
    functionNode = NodeUtil.createFunctionNode("testFunction", functionType);

    objectType = new ObjectType("TestObject", null, null);
  }

  @Test
  public void testAddMarkers() throws IOException {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    Node root = Node.newBlock(null, null);
    root.addChildToFront(functionNode);
    runtimeTypeCheck.process(null, root);

    TreeSet<String> markerNames = new TreeSet<>(Comparator.comparing());
    Node markersNode = findMarkersNode(functionNode);

    for (Node markerNode = markersNode.getFirstChild();
         markerNode != null; markerNode = markerNode.getNext()) {
      markerNames.add(markerNode.getFirstChild().getString());
    }

    assertEquals(Sets.newHashSet(
        "instance_of__TestObject", "implements__TestObject"), markerNames);
  }

  @Test
  public void testAddChecks() throws IOException {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    Node root = Node.newBlock(null, null);
    root.addChildToFront(functionNode);
    runtimeTypeCheck.process(null, root);

    Node checkTypeCallNode = findCheckTypeCallNode(functionNode);
    assertEquals(checkTypeCallNode, isCheckerCall("classChecker", functionType,
        checkTypeCallNode));

    FunctionType newFunctionType = new FunctionType(
        Token.ANY, ImmutableList.of(new ObjectType("TestParam", null, null)),
        Token.VOID, null);
    functionType.setJSType(newFunctionType);
    runtimeTypeCheck.process(null, root);

    checkTypeCallNode = findCheckTypeCallNode(functionNode);
    assertEquals(checkTypeCallNode, isCheckerCall("valueChecker",
        newFunctionType.getParameters().get(0).getJSType(), checkTypeCallNode));
  }

  @Test
  public void testAddBoilerplateCode() throws IOException {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    Node root = Node.newBlock(null, null);
    root.addChildToFront(functionNode);
    runtimeTypeCheck.process(null, root);
    runtimeTypeCheck.addBoilerplateCode();

    Node boilerplateCodeNode = findBoilerplateCodeNode(root);
    assertEquals(boilerplateCodeNode.getString(),
        getBoilerplateCode(compiler, null).getString());
  }

  @Test
  public void testAddBoilerplateCodeWithLogFunction() throws IOException {
    String logFunction = "function(warning, expr) {\n" +
        "  console.log('Warning: " +
        "Type check failed: " +
        "expected " +
        "but got ' + expr + '\n');\n" +
        "}";

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);
    Node root = Node.newBlock(null, null);
    root.addChildToFront(functionNode);
    runtimeTypeCheck.process(null, root);
    runtimeTypeCheck.addBoilerplateCode();

    Node boilerplateCodeNode = findBoilerplateCodeNode(root);
    assertEquals(boilerplateCodeNode.getString(),
        getBoilerplateCode(compiler, logFunction).getString());
  }

  @Test
  public void testAddChecksForReturnType() throws IOException {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    Node root = Node.newBlock(null, null);
    root.addChildToFront(functionNode);
    runtimeTypeCheck.process(null, root);

    Node checkTypeCallNode = findCheckTypeCallNode(functionNode);
    assertEquals(checkTypeCallNode, isCheckerCall("classChecker", functionType,
        checkTypeCallNode));

    FunctionType newFunctionType = new FunctionType(
        Token.ANY, ImmutableList.of(), new ObjectType("TestReturnType", null, null), null);
    functionType.setJSType(newFunctionType);
    runtimeTypeCheck.process(null, root);

    checkTypeCallNode = findCheckTypeCallNode(functionNode);
    assertEquals(checkTypeCallNode, isCheckerCall("valueChecker",
        newFunctionType.getReturnType(), checkTypeCallNode));
  }

  private Node findBoilerplateCodeNode(Node root) {
    for (Node node = root; node != null; node = node.getParent()) {
      if (node.getType() == Token.ASSIGN &&
          node.getFirstChild().getType() == Token.GETELEM &&
          node.getFirstChild().getFirstChild().getType() == Token.IDENT &&
          node.getFirstChild().getFirstChild().getString().equals("jscomp_runtimeTypeCheck")) {
        return node.getFirstChild().getNext();
      }
    }
    throw new IllegalStateException();
  }

  private Node findCheckTypeCallNode(Node node) {
    for (Node child = node.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.getType() == Token.CALL &&
          child.getFirstChild().getType() == Token.IDENT &&
          child.getFirstChild().getString().equals("checkType")) {
        return child;
      }
    }
    throw new IllegalStateException();
  }

  private Node findMarkersNode(Node node) {
    for (Node child = node.getParent(); child != null;
         child = child.getParent()) {
      if (child.getType() == Token.GETPROP &&
          child.getFirstChild().getType() == Token.IDENT &&
          child.getFirstChild().getString().equals("prototype")) {
        return child.getNext();
      }
    }
    throw new IllegalStateException();
  }

  private Node isCheckerCall(String checkerName, JSType expectedType, Node checkTypeCallNode) {
    Node checkerNode = checkTypeCallNode.getFirstChild().getNext();
    assertEquals(checkerNode.getType(), Token.CALL);
    assertEquals(checkerNode.getFirstChild().getType(), Token.IDENT);
    assertEquals(checkerNode.getFirstChild().getString(), checkerName);

    Node arrayNode = checkTypeCallNode.getLastChild();
    for (Node alternateCheckerNode = arrayNode.getFirstChild();
         alternateCheckerNode != null;
         alternateCheckerNode = alternateCheckerNode.getNext()) {
      Node alternateTypeNode = alternateCheckerNode.getFirstChild();
      assertEquals(alternateTypeNode.getType(), Token.CALL);
      assertEquals(alternateTypeNode.getFirstChild().getType(), Token.IDENT);
      JSType alternateType = alternateTypeNode.getFirstChild().getString()
          .equals("valueChecker") ?
          expectedType :
          ((ObjectType) new ObjectType(alternateTypeNode.getFirstChild().getString(), null, null))
              .getJSType();
      assertEquals(alternateType, expectedType);
    }

    return checkTypeCallNode;
  }

  private static Node getBoilerplateCode(AbstractCompiler compiler,
      @Nullable String logFunction) throws IOException {
    return RuntimeTypeCheck.getBoilerplateCode(compiler, logFunction);
  }
}