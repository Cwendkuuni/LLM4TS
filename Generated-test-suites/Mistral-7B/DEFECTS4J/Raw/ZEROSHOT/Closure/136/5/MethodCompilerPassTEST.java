import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TraversalRootsCallback;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
public class MethodCompilerPassTest {

  @Mock
  private AbstractCompiler compiler;

  @InjectMocks
  private MethodCompilerPass testee;

  @Test
  public void testMethodCompilerPass_externsAndJs_traverseRoots() {
    // GIVEN
    Node externs = Mockito.mock(Node.class);
    Node root = Mockito.mock(Node.class);

    // WHEN
    testee.process(externs, root);

    // THEN
    Mockito.verify(compiler).traverseRoots(
        Mockito.eq(compiler),
        Mockito.eq(Lists.newArrayList(externs, root)),
        Mockito.eq(testee.new GatherSignatures()));
  }

  @Test
  public void testMethodCompilerPass_externs_getExternMethods() {
    // GIVEN
    Node externs = Mockito.mock(Node.class);
    Node externMethodsNameNode = Mockito.mock(Node.class);
    String externMethodsName = "externMethods";
    String externMethodsSourceName = "externs.js";

    Mockito.when(externs.getFirstChild()).thenReturn(externMethodsNameNode);
    Mockito.when(externMethodsNameNode.getType()).thenReturn(Token.STRING);
    Mockito.when(externMethodsNameNode.getString()).thenReturn(externMethodsName);

    // WHEN
    testee.process(externs, null);

    // THEN
    Mockito.verify(testee.getSignatureStore()).addSignature(
        Mockito.eq(externMethodsName),
        Mockito.eq(Node.NULL),
        Mockito.eq(externMethodsSourceName));
  }

  @Test
  public void testMethodCompilerPass_externs_getExternMethodsWithoutSignatures() {
    // GIVEN
    Node externs = Mockito.mock(Node.class);
    Node externMethodsNameNode = Mockito.mock(Node.class);
    String externMethodsName = "externMethods";

    Mockito.when(externs.getFirstChild()).thenReturn(externMethodsNameNode);
    Mockito.when(externMethodsNameNode.getType()).thenReturn(Token.STRING);
    Mockito.when(externMethodsNameNode.getString()).thenReturn(externMethodsName);

    // WHEN
    testee.process(externs, null);

    // THEN
    Mockito.verify(testee.getSignatureStore()).removeSignature(
        Mockito.eq(externMethodsName));
    Mockito.verify(testee.externMethodsWithoutSignatures).add(externMethodsName);
  }

  @Test
  public void testMethodCompilerPass_externs_addExternMethod() {
    // GIVEN
    Node externs = Mockito.mock(Node.class);
    Node functionNode = Mockito.mock(Node.class);
    String functionName = "functionName";
    String sourceFile = "externs.js";

    Mockito.when(externs.getFirstChild()).thenReturn(Node.NULL);
    Mockito.when(externs.getNext()).thenReturn(functionNode);
    Mockito.when(functionNode.getType()).thenReturn(Token.FUNCTION);

    // WHEN
    testee.process(externs, null);

    // THEN
    Mockito.verify(testee.getSignatureStore()).addSignature(
        Mockito.eq(functionName),
        Mockito.eq(functionNode),
        Mockito.eq(sourceFile));
    Mockito.verify(testee.methodDefinitions).put(Mockito.eq(functionName), Mockito.eq(functionNode));
  }

  @Test
  public void testMethodCompilerPass_externs_addExternMethodWithoutSignature() {
    // GIVEN
    Node externs = Mockito.mock(Node.class);
    Node functionNameNode = Mockito.mock(Node.class);
    String functionName = "functionName";

    Mockito.when(externs.getFirstChild()).thenReturn(Node.NULL);
    Mockito.when(externs.getNext()).thenReturn(functionNameNode);
    Mockito.when(functionNameNode.getType()).thenReturn(Token.STRING);
    Mockito.when(functionNameNode.getString()).thenReturn(functionName);

    // WHEN
    testee.process(externs, null);

    // THEN
    Mockito.verifyNoMoreInteractions(testee.getSignatureStore());
  }

  @Test
  public void testMethodCompilerPass_js_getActingCallback() {
    // GIVEN
    TraversalRootsCallback actingCallback = Mockito.mock(TraversalRootsCallback.class);

    // WHEN
    Callback callback = testee.getActingCallback();

    // THEN
    assertEquals(actingCallback.getClass(), callback.getClass());
  }

  @Test
  public void testMethodCompilerPass_js_getSignatureStore_reset() {
    // GIVEN
    SignatureStore signatureStore = Mockito.mock(SignatureStore.class);

    // WHEN
    testee.getSignatureStore().reset();

    // THEN
    Mockito.verify(signatureStore).reset();
  }

  @Test
  public void testMethodCompilerPass_js_getSignatureStore_addSignature() {
    // GIVEN
    SignatureStore signatureStore = Mockito.mock(SignatureStore.class);
    String functionName = "functionName";
    Node functionNode = Mockito.mock(Node.class);
    String sourceFile = "sourceFile.js";

    Mockito.when(signatureStore.addSignature(Mockito.eq(functionName), Mockito.eq(functionNode), Mockito.eq(sourceFile))).thenReturn(null);

    // WHEN
    testee.getSignatureStore().addSignature(functionName, functionNode, sourceFile);

    // THEN
    Mockito.verify(signatureStore).addSignature(Mockito.eq(functionName), Mockito.eq(functionNode), Mockito.eq(sourceFile));
  }

  @Test(expected = IllegalStateException.class)
  public void testMethodCompilerPass_js_getSignatureStore_addSignature_illegalStateException() {
    // GIVEN
    SignatureStore signatureStore = Mockito.mock(SignatureStore.class);
    String functionName = "functionName";
    Node functionNode = Mockito.mock(Node.class);
    String sourceFile = "sourceFile.js";

    Mockito.when(signatureStore.addSignature(Mockito.eq(functionName), Mockito.eq(functionNode), Mockito.eq(sourceFile))).thenThrow(new IllegalStateException());

    // WHEN
    testee.getSignatureStore().addSignature(functionName, functionNode, sourceFile);

    // THEN
    // IllegalStateException is expected to be thrown
  }

  @Test
  public void testMethodCompilerPass_js_getSignatureStore_removeSignature() {
    // GIVEN
    SignatureStore signatureStore = Mockito.mock(SignatureStore.class);
    String functionName = "functionName";

    // WHEN
    testee.getSignatureStore().removeSignature(functionName);

    // THEN
    Mockito.verify(signatureStore).removeSignature(Mockito.eq(functionName));
  }

  @Test
  public void testMethodCompilerPass_js_methodDefinitions_put() {
    // GIVEN
    String functionName = "functionName";
    Node functionNode = Mockito.mock(Node.class);

    // WHEN
    testee.methodDefinitions.put(functionName, functionNode);

    // THEN
    Multimap<String, Node> methodDefinitions = testee.methodDefinitions;
    Node expectedFunctionNode = functionNode;

    assertSame(expectedFunctionNode, methodDefinitions.get(functionName).get(0));
  }

  @Test
  public void testMethodCompilerPass_js_methodDefinitions_get() {
    // GIVEN
    String functionName = "functionName";
    Node functionNode = Mockito.mock(Node.class);

    // WHEN
    testee.methodDefinitions.put(functionName, functionNode);

    // THEN
    Node expectedFunctionNode = functionNode;

    assertSame(expectedFunctionNode, testee.methodDefinitions.get(functionName).get(0));
  }

  @Test
  public void testMethodCompilerPass_js_methodDefinitions_isEmpty() {
    // GIVEN

    // WHEN
    boolean isEmpty = testee.methodDefinitions.isEmpty();

    // THEN
    assertTrue(isEmpty);
  }

  @Test
  public void testMethodCompilerPass_js_methodDefinitions_containsKey() {
    // GIVEN
    String functionName = "functionName";
    Node functionNode = Mockito.mock(Node.class);

    testee.methodDefinitions.put(functionName, functionNode);

    // WHEN
    boolean containsKey = testee.methodDefinitions.containsKey(functionName);

    // THEN
    assertTrue(containsKey);
  }

  @Test
  public void testMethodCompilerPass_js_methodDefinitions_containsValue() {
    // GIVEN
    String functionName = "functionName";
    Node functionNode = Mockito.mock(Node.class);

    testee.methodDefinitions.put(functionName, functionNode);

    // WHEN
    boolean containsValue = testee.methodDefinitions.containsValue(functionNode);

    // THEN
    assertTrue(containsValue);
  }

  @Test
  public void testMethodCompilerPass_js_externMethods_isEmpty() {
    // GIVEN

    // WHEN
    boolean isEmpty = testee.externMethods.isEmpty();

    // THEN
    assertTrue(isEmpty);
  }

  @Test
  public void testMethodCompilerPass_js_externMethods_contains() {
    // GIVEN
    String functionName = "functionName";

    testee.externMethods.add(functionName);

    // WHEN
    boolean contains = testee.externMethods.contains(functionName);

    // THEN
    assertTrue(contains);
  }

  @Test
  public void testMethodCompilerPass_js_externMethodsWithoutSignatures_isEmpty() {
    // GIVEN

    // WHEN
    boolean isEmpty = testee.externMethodsWithoutSignatures.isEmpty();

    // THEN
    assertTrue(isEmpty);
  }

  @Test
  public void testMethodCompilerPass_js_externMethodsWithoutSignatures_contains() {
    // GIVEN
    String functionName = "functionName";

    testee.externMethodsWithoutSignatures.add(functionName);

    // WHEN
    boolean contains = testee.externMethodsWithoutSignatures.contains(functionName);

    // THEN
    assertTrue(contains);
  }

  @Test
  public void testMethodCompilerPass_js_nonMethodProperties_isEmpty() {
    // GIVEN

    // WHEN
    boolean isEmpty = testee.nonMethodProperties.isEmpty();

    // THEN
    assertTrue(isEmpty);
  }

  @Test
  public void testMethodCompilerPass_js_nonMethodProperties_contains() {
    // GIVEN
    String propertyName = "propertyName";

    testee.nonMethodProperties.add(propertyName);

    // WHEN
    boolean contains = testee.nonMethodProperties.contains(propertyName);

    // THEN
    assertTrue(contains);
  }
}