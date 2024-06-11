package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.TraversalUtils;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

public class MethodCompilerPassTest {
  private Node root;
  private Node externs;
  private MethodCompilerPass pass;
  private SignatureStore signatureStore;
  private Compiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setup() {
    compiler = new Compiler();
    nodeFactory = compiler.getFactory();
    compiler.init();
    compiler.setOptions(new CompilerOptions());

    root = nodeFactory.createProgram();
    externs = nodeFactory.createObject();

    pass = new TestMethodCompilerPass(compiler);
    signatureStore = new TestSignatureStore();
    pass.setSignatureStore(signatureStore);
  }

  @Test
  public void testEmpty() {
    pass.process(externs, root);
    assertFalse(pass.externMethods.isEmpty());
    assertFalse(pass.externMethodsWithoutSignatures.isEmpty());
    assertFalse(pass.methodDefinitions.isEmpty());
    assertNull(signatureStore.getSignatures().get(0));
  }

  @Test
  public void testExternMethodsWithSignatures() {
    Node functionNode = createFunctionNode("testFunction");
    Node functionNameNode = nodeFactory.createName("testFunction");
    Node functionAssignmentNode = nodeFactory.createAssign(functionNameNode, functionNode);
    externs.appendChild(functionAssignmentNode);

    pass.process(externs, root);
    assertFalse(pass.externMethods.isEmpty());
    assertFalse(pass.externMethodsWithoutSignatures.isEmpty());
    assertFalse(pass.methodDefinitions.isEmpty());
    List<Node> signatures = signatureStore.getSignatures();
    assertSame(functionNode, signatures.get(0));
  }

  @Test
  public void testExternMethodsWithoutSignatures() {
    Node methodNameNode = nodeFactory.createName("testMethod");
    Node methodAssignmentNode = nodeFactory.createAssign(methodNameNode, createFunctionNode("testMethod"));
    externs.appendChild(methodAssignmentNode);

    pass.process(externs, root);
    assertFalse(pass.externMethods.isEmpty());
    assertFalse(pass.externMethodsWithoutSignatures.isEmpty());
    assertFalse(pass.methodDefinitions.isEmpty());
    assertNull(signatureStore.getSignatures().get(0));
  }

  @Test
  public void testMethodDefinition() {
    Node functionNode = createFunctionNode("testFunction");
    Node functionNameNode = nodeFactory.createName("testFunction");
    Node functionAssignmentNode = nodeFactory.createAssign(functionNameNode, functionNode);
    root.appendChild(functionAssignmentNode);

    Node getPropNode = nodeFactory.createGetProp(nodeFactory.createName("Foo"), nodeFactory.createName("prototype"));
    Node getPropPrototypeNameNode = getPropNode.getFirstChild();
    Node getPropPrototypeFunctionNode = TraversalUtils.getFirstChildOfType(getPropNode, Token.FUNCTION);
    Node setPrototypeNode = nodeFactory.createAssign(getPropPrototypeNameNode, functionNode);
    root.appendChild(setPrototypeNode);

    pass.process(externs, root);
    assertFalse(pass.externMethods.isEmpty());
    assertFalse(pass.externMethodsWithoutSignatures.isEmpty());
    assertFalse(pass.methodDefinitions.isEmpty());
    List<Node> signatures = signatureStore.getSignatures();
    assertSame(functionNode, signatures.get(0));
  }

  @Test(expected = IllegalStateException.class)
  public void testUndefinedFunction() {
    Node undefinedFunctionNameNode = nodeFactory.createName("undefinedFunction");
    Node undefinedFunctionAssignmentNode = nodeFactory.createAssign(undefinedFunctionNameNode, createFunctionNode("undefinedFunction"));
    externs.appendChild(undefinedFunctionAssignmentNode);

    pass.process(externs, root);
    assertTrue(pass.externMethods.contains("undefinedFunction"));
    assertTrue(pass.externMethodsWithoutSignatures.contains("undefinedFunction"));
    assertFalse(pass.methodDefinitions.isEmpty());
  }

  @Test
  public void testNonMethodProperties() {
    Node propertyNameNode = nodeFactory.createName("testProperty");
    Node propertyAssignmentNode = nodeFactory.createAssign(propertyNameNode, createNumberNode(1));
    root.appendChild(propertyAssignmentNode);

    pass.process(externs, root);
    assertTrue(pass.externMethods.isEmpty());
    assertTrue(pass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(pass.nonMethodProperties.contains("testProperty"));
  }

  private Node createFunctionNode(String name) {
    Node function = nodeFactory.createFunction(name, Lists.<Node>newArrayList(), null);
    Node functionNameNode = nodeFactory.createName(name);
    Node functionAssignmentNode = nodeFactory.createAssign(functionNameNode, function);
    return function;
  }

  private Node createNumberNode(int value) {
    return nodeFactory.createNumberLiteral(value);
  }

  private static class TestSignatureStore implements MethodCompilerPass.SignatureStore {
    private List<Node> signatures = Lists.newArrayList();

    @Override
    public void reset() {
      signatures.clear();
    }

    @Override
    public void addSignature(String functionName, Node functionNode, String sourceFile) {
      signatures.add(functionNode);
    }

    @Override
    public void removeSignature(String functionName) {
      // not implemented
    }

    List<Node> getSignatures() {
      return signatures;
    }
  }

  private static class TestMethodCompilerPass extends MethodCompilerPass {
    TestMethodCompilerPass(Compiler compiler) {
      super(compiler);
    }

    @Override
    abstract Callback getActingCallback();

    @Override
    abstract SignatureStore getSignatureStore();

    @Override
    Callback getActingCallback() {
      return new TestActingCallback();
    }

    @Override
    SignatureStore getSignatureStore() {
      return signatureStore;
    }

    private static class TestActingCallback extends AbstractPostOrderCallback {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        // not implemented
      }
    }
  }
}