package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

public class MethodCompilerPassTest {

  private static class DummySignatureStore implements SignatureStore {
    Multimap<String, Node> signatures = HashMultimap.create();

    @Override
    public void reset() {
      signatures.clear();
    }

    @Override
    public void addSignature(String functionName, Node functionNode, String sourceFile) {
      signatures.put(functionName, functionNode);
    }

    @Override
    public void removeSignature(String functionName) {
      signatures.remove(functionName);
    }
  }

  private MethodCompilerPass testee;
  private Node root;
  private SignatureStore signatureStore;

  @SuppressWarnings("unchecked")
  @Before
  public void before() {
    Compiler compiler = mock(Compiler.class);
    CompilerOptions options = new CompilerOptions();
    testee = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        if (signatureStore == null) {
          signatureStore = new DummySignatureStore();
        }
        return signatureStore;
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }
    };

    root = Node.objectLiteral();
  }

  @Test
  public void testEmptyCode() {
    testee.process(null, root);
    assertEquals(0, testee.methodDefinitions.size());
  }

  @Test
  public void testFunctionDeclaration() {
    Node function = Node.function("test", new Node[0], null);
    root.addChild(function);

    testee.process(null, root);
    assertEquals(1, testee.methodDefinitions.size());
    assertNotNull(testee.methodDefinitions.get("test"));
  }

  @Test
  public void testFunctionExpression() {
    Node function = Node.name("test");
    function.setType(Token.FUNCTION);
    function.addChild(Node.objectLiteral());
    root.addChild(function);

    testee.process(null, root);
    assertEquals(1, testee.methodDefinitions.size());
    assertNotNull(testee.methodDefinitions.get("test"));
  }

  @Test
  public void testFunctionDeclarationWithParameters() {
    Node function = Node.function("test", new Node[] { Node.name("arg1") }, null);
    root.addChild(function);

    testee.process(null, root);
    assertEquals(1, testee.methodDefinitions.size());
    Node methodNode = testee.methodDefinitions.get("test");
    assertEquals(1, methodNode.getNumChildren());
    Node parameters = methodNode.getFirstChild();
    assertEquals(1, parameters.getNumChildren());
    assertEquals("arg1", parameters.getLastChild().getString());
  }

  @Test
  public void testFunctionExpressionWithParameters() {
    Node function = Node.name("test");
    function.setType(Token.FUNCTION);
    Node parameters = Node.arrayLiteral();
    Node arg1 = Node.name("arg1");
    parameters.addChild(arg1);
    function.addChild(parameters);
    function.addChild(Node.objectLiteral());
    root.addChild(function);

    testee.process(null, root);
    assertEquals(1, testee.methodDefinitions.size());
    Node methodNode = testee.methodDefinitions.get("test");
    assertEquals(1, methodNode.getNumChildren());
    Node parametersNode = methodNode.getFirstChild();
    assertEquals(1, parametersNode.getNumChildren());
    assertEquals("arg1", parametersNode.getLastChild().getString());
  }

  @Test(expected = IllegalStateException.class)
  public void testUndefinedFunction() {
    Node function = Node.name("foo");
    root.addChild(function);
    testee.process(null, root);
  }

  @Test
  public void testGetExternMethods() {
    Node externs = Node.objectLiteral();
    Node function1 = Node.function("test1", new Node[0], null);
    Node method1 = Node.getProp(Node.name("Foo"), Node.stringLiteral("test1"));
    method1.setType(Token.GETPROP);
    method1.addChild(function1);
    externs.addChild(method1);

    Node function2 = Node.function("test2", new Node[0], null);
    Node method2 = Node.name("test2");
    method2.setType(Token.NAME);
    Node assign = Node.assign(Node.name("Foo"), method2);
    assign.setType(Token.ASSIGN);
    assign.addChild(method2);
    assign.addChild(function2);
    externs.addChild(assign);

    testee.process(externs, root);
    assertEquals(2, testee.methodDefinitions.size());
    assertEquals("test1", testee.methodDefinitions.keys().toArray()[0]);
    assertEquals("test2", testee.methodDefinitions.keys().toArray()[1]);
  }

  @Test
  public void testGatherSignatures() {
    Node function = Node.function("test", new Node[0], null);
    root.addChild(function);

    testee.process(null, root);
    assertEquals(1, testee.methodDefinitions.size());
    assertNotNull(testee.methodDefinitions.get("test"));

    Node prototype = Node.objectLiteral();
    prototype.addChild(Node.name("prototype"));
    root.addChild(prototype);

    Node prototypeFunction = Node.function("prototypeFunction", new Node[0], null);
    prototype.addChild(prototypeFunction);

    testee.process(null, root);
    assertEquals(1, testee.methodDefinitions.size());
    assertNotNull(testee.methodDefinitions.get("test"));

    Node prototypePrototype = Node.getProp(prototype, Node.stringLiteral("prototype"));
    prototypePrototype.setType(Token.GETPROP);
    prototypePrototype.addChild(Node.name("prototypeFunction"));
    prototypePrototype.addChild(function);
    root.addChild(prototypePrototype);

    testee.process(null, root);
    assertEquals(2, testee.methodDefinitions.size());
    assertNotNull(testee.methodDefinitions.get("test"));
    assertNotNull(testee.methodDefinitions.get("prototypeFunction"));
  }
}