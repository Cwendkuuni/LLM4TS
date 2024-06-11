package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.mockitobserver.MockitoObserver;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(MockitoJUnit.MockitoJUnitRunner.class)
public class MethodCompilerPassTest {

  private MethodCompilerPass methodCompilerPass;
  private Compiler compiler;
  private SignatureStore signatureStore;
  private Multimap<String, Node> methodDefinitions;
  private Set<String> externMethods;
  private Set<String> externMethodsWithoutSignatures;
  private Set<String> nonMethodProperties;

  @Before
  public void setUp() {
    compiler = Mockito.mock(Compiler.class);
    signatureStore = Mockito.mock(SignatureStore.class);
    methodDefinitions = HashMultimap.create();
    externMethods = Sets.newHashSet();
    externMethodsWithoutSignatures = Sets.newHashSet();
    nonMethodProperties = Sets.newHashSet();
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      Callback getActingCallback() {
        return Mockito.mock(Callback.class);
      }

      @Override
      SignatureStore getSignatureStore() {
        return signatureStore;
      }
    };
  }

  @Test
  public void testAddSignature() {
    String functionName = "testFunction";
    Node functionNode = Mockito.mock(Node.class);
    String sourceFile = "testSourceFile";

    methodCompilerPass.addSignature(functionName, functionNode, sourceFile);

    Mockito.verify(signatureStore).addSignature(functionName, functionNode, sourceFile);
    Mockito.verify(methodDefinitions).put(functionName, functionNode);
    assertEquals(functionNode, methodDefinitions.get(functionName).get(0));
  }

  @Test
  public void testRemoveSignature() {
    String functionName = "testFunction";

    methodCompilerPass.removeSignature(functionName);

    Mockito.verify(signatureStore).removeSignature(functionName);
    assertEquals(0, methodDefinitions.get(functionName).size());
  }

  @Test
  public void testAddPossibleSignature() {
    String functionName = "testFunction";
    Node functionNode = Mockito.mock(Node.class);
    NodeTraversal nodeTraversal = Mockito.mock(NodeTraversal.class);

    methodCompilerPass.addPossibleSignature(functionName, functionNode, nodeTraversal);

    Mockito.verify(signatureStore, Mockito.never()).addSignature(functionName, functionNode, anyString());
    Mockito.verify(nonMethodProperties).add(functionName);
  }

  @Test
  public void testProcessWithExternsAndRoot() {
    String externFunctionName = "externFunction";
    Node externFunctionNode = Mockito.mock(Node.class);
    String externSourceFile = "externSourceFile";

    Mockito.when(compiler.getSourceFile(externFunctionNode)).thenReturn(externSourceFile);

    methodCompilerPass.process(createNode("{}"), createNode("function test() {}"));

    Mockito.verify(signatureStore).reset();
    Mockito.verify(externMethods).add(externFunctionName);
    Mockito.verify(externMethodsWithoutSignatures).add(externFunctionName);
    Mockito.verifyZeroInteractions(signatureStore);
    Mockito.verifyZeroInteractions(methodDefinitions);

    Mockito.when(compiler.getSourceFile(any(Node.class)))
        .thenReturn(Mockito.anyString());

    methodCompilerPass.process(createNode("{}"), createNode("function test() {}"));

    Mockito.verify(signatureStore).addSignature(externFunctionName, externFunctionNode, externSourceFile);
    Mockito.verify(methodDefinitions).put(externFunctionName, externFunctionNode);
  }

  @Test
  public void testGatherSignatures() {
    String functionName = "testFunction";
    Node functionNode = Mockito.mock(Node.class);
    Node sourceNode = Mockito.mock(Node.class);

    Mockito.when(functionNode.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(sourceNode.getType()).thenReturn(Token.OBJECTLIT);

    methodCompilerPass.process(createNode("{}"), createNode("function test() {}"));

    Mockito.verify(signatureStore, Mockito.never()).addSignature(functionName, functionNode, anyString());
    Mockito.verifyZeroInteractions(methodDefinitions);

    Node functionCallNode = createNode("function test() {}");
    functionCallNode.setParent(sourceNode);

    methodCompilerPass.process(createNode("{}"), createNode("function test() {}"));

    Mockito.verify(signatureStore).addSignature(functionName, functionNode, anyString());
    Mockito.verify(methodDefinitions).put(functionName, functionNode);
  }

  @Test
  public void testGetExternMethods() {
    String externFunctionName = "externFunction";

    methodCompilerPass.process(createNode("{}"), createNode("{}"));

    Mockito.verifyZeroInteractions(signatureStore);
    Mockito.verifyZeroInteractions(externMethods);
    Mockito.verifyZeroInteractions(externMethodsWithoutSignatures);

    Node externFunctionNode = Mockito.mock(Node.class);

    methodCompilerPass.process(createNode("{foo: function test() {}}"), createNode("{}"));

    Mockito.verify(externMethods).add(externFunctionName);
    Mockito.verifyZeroInteractions(externMethodsWithoutSignatures);

    Mockito.when(compiler.getSourceFile(externFunctionNode)).thenReturn("externSourceFile");

    methodCompilerPass.process(createNode("{foo: function test() {}}"), createNode("{}"));

    Mockito.verify(signatureStore).addSignature(externFunctionName, externFunctionNode, "externSourceFile");
    Mockito.verify(methodDefinitions).put(externFunctionName, externFunctionNode);
  }

  @Test
  public void testNonMethodProperties() {
    String nonMethodPropertyName = "testProperty";

    methodCompilerPass.process(createNode("{}"), createNode("function test() {}"));

    Mockito.verifyZeroInteractions(nonMethodProperties);

    Node propertyNode = createNode("propertyName: 'testProperty'");
    propertyNode.setParent(createNode("function test() {}"));

    methodCompilerPass.process(createNode("{}"), createNode("function test() {}"));

    Mockito.verify(nonMethodProperties).add(nonMethodPropertyName);
  }

  private Node createNode(String nodeString) {
    return new Node() {
      @Override
      public String toString() {
        return nodeString;
      }
    };
  }
}