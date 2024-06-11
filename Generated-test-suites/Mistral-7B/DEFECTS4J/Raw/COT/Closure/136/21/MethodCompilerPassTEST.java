package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Test class for MethodCompilerPass
 */
public class MethodCompilerPassTest {
  private static final String SOURCE_CODE =
      "function Foo() {} " +
      "Foo.prototype.bar = function() {} " +
      "Foo.prototype.qux = function(x) {} " +
      "function main() { } ";

  private static final String EXTERN_SOURCE_CODE =
      "Foo.bar = function() {} " +
      "Foo.qux = function(y) {} ";

  @SuppressWarnings("unchecked")
  private SignatureStore signatureStore = new SignatureStore() {
    private Map<String, Node> signatures = Sets.newHashMap();

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

    public Node getSignature(String functionName) {
      return signatures.get(functionName);
    }
  };

  @SuppressWarnings("unchecked")
  private MethodCompilerPass compilerPass = new MethodCompilerPass(new TestCompiler()) {
    @Override
    SignatureStore getSignatureStore() {
      return signatureStore;
    }

    @Override
    Callback getActingCallback() {
      // no-op
      return new Callback() {};
    }
  };

  @org.junit.Test
  public void testProcessExterns() {
    Node externs = TestCompiler.parse(EXTERN_SOURCE_CODE);
    compilerPass.process(externs, null);

    assertEquals(2, compilerPass.externMethods.size());
    assertTrue(compilerPass.externMethods.containsAll(Sets.newHashSet("Foo.bar", "Foo.qux")));
    assertFalse(compilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(compilerPass.externMethodsWithoutSignatures.contains("Foo.bar"));
    assertNull(compilerPass.getSignature("Foo.nonExistentMethod"));
  }

  @org.junit.Test
  public void testProcessSourceCode() {
    Node sourceCodeNode = TestCompiler.parse(SOURCE_CODE);
    compilerPass.process(null, sourceCodeNode);

    assertEquals(2, compilerPass.methodDefinitions.size());
    assertEquals(Foo.class.getMethod("bar"), compilerPass.methodDefinitions.get("Foo.bar"));
    assertEquals(Foo.class.getMethod("qux"), compilerPass.methodDefinitions.get("Foo.qux"));
    assertNull(compilerPass.methodDefinitions.get("Foo.nonExistentMethod"));
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testGetPropertyOfUndefinedFunction() {
    Node externs = TestCompiler.parse("Foo.nonExistentMethod = function() {}");
    compilerPass.process(externs, null);
    compilerPass.getSignature("Foo.nonExistentMethod");
  }

  @org.junit.Test
  public void testGetPrototypeOfFunction() {
    Node sourceCodeNode = TestCompiler.parse(
        "function Foo() {} " +
        "Foo.prototype = { getBar: function() {} }");
    compilerPass.process(null, sourceCodeNode);
    Node prototype = compilerPass.getSignature("Foo.prototype.getBar");
    assertSame(Foo.prototype.getBar, prototype);
  }

  @org.junit.Test
  public void testGetFunctionFromObjectLiteral() {
    Node sourceCodeNode = TestCompiler.parse(
        "function Foo() {" +
        "  this.bar = function() {" +
        "  }" +
        "}" +
        "var obj = { qux: function(x) {} }");
    compilerPass.process(null, sourceCodeNode);
    Node barFunction = compilerPass.getSignature("Foo.bar");
    Node quxFunction = compilerPass.getSignature("obj.qux");
    assertNull(barFunction);
    assertSame(obj.qux, quxFunction);
  }
}