import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.compiler.CompilerOptions;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    CheckGlobalThis checker = createChecker();
    NodeTraversal traversal = createTraversal(checker);
    AbstractCompiler compiler = createCompiler();
    CompilerOptions options = new CompilerOptions(null);
    checker.init(compiler, options, null);

    Node functionNode = createFunctionNode();
    Node functionParentNode = createParentFunctionNode();
    Node functionGrandparentNode = createGrandparentNode();

    traversal.traverse(functionNode, null);
    assertFalse(checker.shouldReportThis(functionNode, functionParentNode));

    functionNode.setParent(functionParentNode);
    functionParentNode.setParent(functionGrandparentNode);
    traversal.traverse(functionNode, null);
    assertTrue(checker.shouldReportThis(functionNode, functionParentNode));

    Node assignNode = createAssignNode();
    Node assignLeftChildNode = createAssignLeftChildNode();
    Node assignParentNode = createAssignParentNode();

    assignLeftChildNode.setParent(assignParentNode);
    assignParentNode.setParent(functionParentNode);
    assignParentNode.setNext(functionNode);

    traversal.traverse(assignLeftChildNode, null);
    assertFalse(checker.shouldReportThis(assignLeftChildNode, assignParentNode));

    traversal.traverse(assignParentNode, null);
    assertTrue(checker.shouldReportThis(assignLeftChildNode, assignParentNode));

    Node constructorNode = createConstructorNode();
    Node constructorParentNode = createConstructorParentNode();
    Node constructorGrandparentNode = createGrandparentNode();

    constructorParentNode.setParent(constructorGrandparentNode);
    constructorParentNode.setNext(functionParentNode);
    constructorNode.setParent(constructorParentNode);

    traversal.traverse(constructorNode, null);
    assertFalse(checker.shouldReportThis(constructorNode, constructorParentNode));

    Node thisNode = createThisNode();
    Node thisParentNode = createFunctionParentNode();

    thisParentNode.setParent(functionParentNode);
    thisNode.setParent(thisParentNode);

    traversal.traverse(thisNode, null);
    assertTrue(checker.shouldReportThis(thisNode, thisParentNode));

    Node prototypePropertyNode = createPrototypePropertyNode();
    Node prototypePropertyParentNode = createPrototypePropertyParentNode();
    Node prototypePropertyGrandparentNode = createGrandparentNode();

    prototypePropertyParentNode.setParent(prototypePropertyGrandparentNode);
    prototypePropertyParentNode.setNext(functionParentNode);
    prototypePropertyNode.setParent(prototypePropertyParentNode);

    traversal.traverse(prototypePropertyNode, null);
    assertFalse(checker.shouldReportThis(prototypePropertyNode, prototypePropertyParentNode));

    Node prototypeSubPropertyNode = createPrototypeSubPropertyNode();
    Node prototypeSubPropertyParentNode = createPrototypeSubPropertyParentNode();
    Node prototypeSubPropertyGrandparentNode = createGrandparentNode();

    prototypeSubPropertyParentNode.setParent(prototypeSubPropertyGrandparentNode);
    prototypeSubPropertyParentNode.setNext(prototypePropertyParentNode);
    prototypeSubPropertyNode.setParent(prototypeSubPropertyParentNode);

    traversal.traverse(prototypeSubPropertyNode, null);
    assertFalse(checker.shouldReportThis(prototypeSubPropertyNode, prototypeSubPropertyParentNode));
  }

  private CheckGlobalThis createChecker() {
    return new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
  }

  private NodeTraversal createTraversal(Callback callback) {
    return new NodeTraversal(callback);
  }

  private AbstractCompiler createCompiler() {
    return mock(AbstractCompiler.class);
  }

  private Node createFunctionNode() {
    return new Node(Token.FUNCTION, null, null, null);
  }

  private Node createParentFunctionNode() {
    return new Node(Token.NAME, "parentFunction", null, null);
  }

  private Node createGrandparentNode() {
    return new Node(Token.NAME, "grandparentNode", null, null);
  }

  private Node createAssignNode() {
    return new Node(Token.ASSIGN, null, null, null);
  }

  private Node createAssignLeftChildNode() {
    return new Node(Token.THIS, null, null, null);
  }

  private Node createAssignParentNode() {
    return new Node(Token.ASSIGN, null, null, null);
  }

  private Node createConstructorNode() {
    JSDocInfo jsDocInfo = new JSDocInfo(null, null, "constructor", null, null, null);
    return new Node(Token.FUNCTION, jsDocInfo, null, null);
  }

  private Node createConstructorParentNode() {
    return new Node(Token.NAME, "constructorParentNode", null, null);
  }

  private Node createThisNode() {
    return new Node(Token.THIS, null, null, null);
  }

  private Node createFunctionParentNode() {
    return new Node(Token.NAME, "functionParentNode", null, null);
  }

  private Node createPrototypePropertyNode() {
    return new Node(Token.GETPROP, new Node(Token.NAME, "prototype"), null, null);
  }

  private Node createPrototypePropertyParentNode() {
    return new Node(Token.NAME, "prototypePropertyParentNode", null, null);
  }

  private Node createPrototypeSubPropertyNode() {
    return new Node(Token.GETPROP, new Node(Token.NAME, "prototypeSubProperty"), null, null);
  }

  private Node createPrototypeSubPropertyParentNode() {
    return new Node(Token.NAME, "prototypeSubPropertyParentNode", null, null);
  }
}