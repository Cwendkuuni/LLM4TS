package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

public class RuntimeTypeCheckTest {

  private AbstractCompiler compiler;
  private CompilerPassManager passManager;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    passManager = mock(CompilerPassManager.class);
    when(compiler.getInput("test.js")).thenReturn(new CompilerInput("test.js", ""));
    when(compiler.getNodeForCodeInsertion(null)).thenReturn(new Node(Token.NULL));
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());
    MockitoAnnotations.initMocks(this);
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    passManager.addPass(runtimeTypeCheck);
  }

  @Test
  public void testAddMarkers() throws IOException {
    Node root = NodeUtil.parseJsFile("test.js", "return function Test() {}");
    when(compiler.getInput("test.js")).thenReturn(new CompilerInput("test.js", ""));

    passManager.process(root, null);

    TreeSet<String> markerNames = new TreeSet<String>(Comparator.comparing());
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    Node prototype = traversal.findNode(NodeUtil.newQualifiedNameNode("Test"), null);
    for (Node child = prototype.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.getType() == Token.GETPROP && child.getFirstChild().equals(
          Node.newString("prototype"))) {
        for (Node grandChild = child.getLastChild();
             grandChild != null;
             grandChild = grandChild.getNext()) {
          markerNames.add(grandChild.getFirstChild().toString());
        }
      }
    }

    assertEquals(ImmutableList.of("instance_of__Test"), markerNames);
  }

  @Test
  public void testAddChecks() throws IOException {
    Node root = NodeUtil.parseJsFile("test.js", "function Test(a: number, b: string) { return a + b; }");
    when(compiler.getInput("test.js")).thenReturn(new CompilerInput("test.js", ""));

    passManager.process(root, null);

    Node functionNode = NodeUtil.findFunctionDeclaration(root, "Test");
    Node bodyNode = functionNode.getLastChild();

    Node checkNode = null;
    for (Node child = bodyNode.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.getType() == Token.CALL &&
          child.getFirstChild().equals(jsCode("checkType"))) {
        checkNode = child;
        break;
      }
    }

    assertEquals(checkNode, bodyNode.getLastChild());

    Node paramA = NodeUtil.getChildByUniqueName(functionNode, "a");
    Node paramB = NodeUtil.getChildByUniqueName(functionNode, "b");

    Node checkA = null;
    for (Node grandChild = checkNode.getFirstChild(); grandChild != null;
         grandChild = grandChild.getNext()) {
      if (grandChild.getType() == Token.CALL &&
          ((Node)grandChild.getFirstChild()).equals(valueCheckerNode("number"))) {
        checkA = grandChild;
        break;
      }
    }

    assertEquals(checkA, paramA);

    Node checkB = null;
    for (Node grandChild = checkNode.getLastChild(); grandChild != null;
         grandChild = grandChild.getPrev()) {
      if (grandChild.getType() == Token.CALL &&
          ((Node)grandChild.getFirstChild()).equals(valueCheckerNode("string"))) {
        checkB = grandChild;
        break;
      }
    }

    assertEquals(checkB, paramB);
  }

  private Node valueCheckerNode(String type) {
    return NodeUtil.newQualifiedNameNode("valueChecker", -1, -1)
        .getFirstChild()
        .cloneTree()
        .setJSType(new JSType[] {new JSType() {}})
        .setFirstChild(new Node(Token.STRING, type));
  }

  private Node jsCode(String prop) {
    return NodeUtil.newQualifiedNameNode("jscomp.typecheck." + prop, -1, -1);
  }
}