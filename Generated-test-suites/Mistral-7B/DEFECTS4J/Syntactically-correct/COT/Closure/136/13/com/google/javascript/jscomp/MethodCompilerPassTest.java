package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.testing.TestCompiler;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

public class MethodCompilerPassTest {

  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @org.junit.Before
  public void setUp() {
    TestCompiler compiler = new TestCompiler();
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      Callback getActingCallback() {
        // No-op acting callback for testing
        return new Callback() {
          public void process(Node node, Node parent) {}
        };
      }

      @Override
      SignatureStore getSignatureStore() {
        if (signatureStore == null) {
          signatureStore = new TestSignatureStore();
        }
        return signatureStore;
      }
    };
    signatureStore = new TestSignatureStore();
  }

  @org.junit.Test
  public void testMethodCompilerPass_NoExterns() {
    Node root = TestHelper.parseStringToAst("function foo() {}");
    methodCompilerPass.process(null, root);
    assertEmpty(methodCompilerPass.externMethods);
    assertEmpty(methodCompilerPass.externMethodsWithoutSignatures);
    assertEmpty(methodCompilerPass.nonMethodProperties);
    assertEmpty(methodCompilerPass.methodDefinitions.asMap().entrySet());
  }

  @org.junit.Test
  public void testMethodCompilerPass_ExternWithSignature() {
    Node externs = TestHelper.parseStringToAst("function bar(arg1, arg2) {}");
    Node root = TestHelper.parseStringToAst("function foo() {}");
    methodCompilerPass.process(externs, root);

    assertEquals("bar", methodCompilerPass.externMethods.iterator().next());
    assertFalse(methodCompilerPass.externMethodsWithoutSignatures.contains("bar"));
    assertEmpty(methodCompilerPass.nonMethodProperties);
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertSame(externs.getFirstChild(), methodCompilerPass.methodDefinitions.get("bar"));
  }

  @org.junit.Test
  public void testMethodCompilerPass_ExternWithoutSignature() {
    Node externs = TestHelper.parseStringToAst("function qux() {}");
    Node root = TestHelper.parseStringToAst("function foo() {}");
    methodCompilerPass.process(externs, root);

    assertEquals("qux", methodCompilerPass.externMethodsWithoutSignatures.iterator().next());
    assertTrue(methodCompilerPass.externMethods.isEmpty());
    assertFalse(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertEmpty(methodCompilerPass.nonMethodProperties);
    assertEmpty(methodCompilerPass.methodDefinitions.asMap().entrySet());
  }

  @org.junit.Test
  public void testMethodCompilerPass_ExternsAndMethods() {
    Node externs = TestHelper.parseStringToAst(
        "function bar(arg1, arg2) {}" +
        "function qux(arg1) {}");
    Node root = TestHelper.parseStringToAst("function foo() {}");
    methodCompilerPass.process(externs, root);

    assertEquals(2, methodCompilerPass.externMethods.size());
    assertFalse(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertEmpty(methodCompilerPass.nonMethodProperties);
    assertEquals(2, methodCompilerPass.methodDefinitions.size());

    List<String> externMethods = Lists.newArrayList(methodCompilerPass.externMethods);
    List<String> methodDefinitions = Lists.newArrayList(methodCompilerPass.methodDefinitions.keySet());

    assertEquals("bar", externMethods.get(0));
    assertEquals("qux", externMethods.get(1));
    assertFalse(methodDefinitions.contains("qux"));
    assertTrue(methodDefinitions.contains("bar"));
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testMethodCompilerPass_UndefinedFunction() {
    Node externs = TestHelper.parseStringToAst("function undefinedFunction() {}");
    Node root = TestHelper.parseStringToAst("function foo() {}");
    methodCompilerPass.process(externs, root);
  }

  @org.junit.Test
  public void testMethodCompilerPass_Prototype() {
    Node externs = TestHelper.parseStringToAst(
        "function Foo() {}" +
        "Foo.prototype.getBar = function(arg1) {}");
    Node root = TestHelper.parseStringToAst("function foo() {}");
    methodCompilerPass.process(externs, root);

    assertEquals(1, methodCompilerPass.externMethods.size());
    assertFalse(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertEmpty(methodCompilerPass.nonMethodProperties);
    assertEquals(2, methodCompilerPass.methodDefinitions.size());

    List<String> externMethods = Lists.newArrayList(methodCompilerPass.externMethods);
    List<String> methodDefinitions = Lists.newArrayList(methodCompilerPass.methodDefinitions.keySet());

    assertEquals("Foo", externMethods.get(0));
    assertFalse(methodDefinitions.contains("getBar"));
    assertTrue(methodCompilerPass.methodDefinitions.containsKey("getBar"));
  }

  private static class TestSignatureStore implements SignatureStore {

    private Multimap<String, Node> signatureMap = HashMultimap.create();

    @Override
    public void reset() {
      signatureMap.clear();
    }

    @Override
    public void addSignature(String functionName, Node functionNode, String sourceFile) {
      signatureMap.put(functionName, functionNode);
    }

    @Override
    public void removeSignature(String functionName) {
      signatureMap.remove(functionName);
    }

    public Node getSignature(String functionName) {
      return signatureMap.get(functionName).get(0);
    }

    public int getNumberOfSignatures(String functionName) {
      return signatureMap.get(functionName).size();
    }

    public boolean hasSignature(String functionName) {
      return signatureMap.containsKey(functionName);
    }
  }

  private static class TestHelper {
    static Node parseStringToAst(String code) {
      return RhinoTestUtils.parseStringToNode(code);
    }
  }

  private static class RhinoTestUtils {
    static Node parseStringToNode(String cs) {
      RhinoCommonUtils.initRhino();
      Context context = Context.enter();
      try {
        Script script = new Script();
        script.setSourceReader(new StringReader(cs));
        context.evaluateString(script, null, 1, null);
        return (Node) context.getReturnValue();
      } finally {
        Context.exit();
      }
    }
  }
}