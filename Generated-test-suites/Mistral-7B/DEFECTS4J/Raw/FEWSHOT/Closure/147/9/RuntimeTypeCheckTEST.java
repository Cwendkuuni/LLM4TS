package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RuntimeTypeCheck.AddChecks;
import com.google.javascript.jscomp.RuntimeTypeCheck.AddMarkers;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;

/**
 * Tests for the RuntimeTypeCheck class.
 */
public class RuntimeTypeCheckTest {
  private AbstractCompiler compiler;
  private NodeTraversal nodeTraversal;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    compiler = mock(AbstractCompiler.class);
    nodeTraversal = new NodeTraversal(compiler);
  }

  @Test
  public void testAddMarkers() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    FunctionType functionType = new FunctionType(new Node("foo"), null, null);
    Node functionNode = new Node(Token.FUNCTION, null, null);
    functionNode.setJSType(functionType);
    Node classNode = NodeUtil.newQualifiedNameNode("Foo", -1, -1);

    Node addMarkers = new AddMarkers(compiler);
    Node nodeToInsertAfter = functionNode;
    Node markerNode = addMarkers.addMarker(functionType, nodeToInsertAfter, null);
    nodeToInsertAfter = addMarkerNode;
    markerNode = addMarkers.addMarker(functionType, nodeToInsertAfter, new ObjectType("I"));

    assertEquals("instance_of__Foo", markerNode.getFirstChild().getString());
    assertEquals("implements__I", markerNode.getNext().getString());
  }

  @Test
  public void testAddChecks() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    FunctionType functionType = new FunctionType(new Node("foo"), null, null);
    Node functionNode = new Node(Token.FUNCTION, null, null);
    functionNode.setJSType(functionType);

    AddChecks addChecks = new AddChecks(compiler);
    Node markersNode = new Node(Token.BLOCK);
    Node paramNode = new Node(Token.GETPROP, new Node(Token.THIS), "params");
    Node paramNameNode = new Node(Token.IDENT, "paramName");

    Node checkNode = createCheckTypeCallNode(functionType.getParameters().get(0).getJSType(), paramNameNode);
    markersNode.addChildToBack(checkNode);
    nodeTraversal.traverse(compiler, functionNode, addChecks);

    List<Node> children = Arrays.asList(functionNode.getLastChild().getChildren());
    assertEquals(1, children.size());
    assertEquals(markersNode, children.get(0));
  }

  private Node createCheckTypeCallNode(JSType jstype, Node expr) {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    return runtimeTypeCheck.createCheckTypeCallNode(jstype, expr);
  }
}