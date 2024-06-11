package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableMultimap;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.test.testtype.AbstractCompilerForTesting;

import org.junit.Test;

public class MethodCompilerPassTest extends AbstractCompilerForTesting {

  private static class DummySignatureStore implements MethodCompilerPass.SignatureStore {
    private final ImmutableSet<String> signatures = ImmutableSet.of();

    @Override
    public void reset() {}

    @Override
    public void addSignature(String functionName, Node functionNode, String sourceFile) {
      signatures.add(functionName);
    }

    @Override
    public void removeSignature(String functionName) {}

    public Set<String> getSignatures() {
      return signatures;
    }

    @Override
    public String toString() {
      return "DummySignatureStore{" + signatures + "}";
    }
  }

  private static class DummyMethodCompilerPass extends MethodCompilerPass {
    @Override
    public abstract Callback getActingCallback();

    @Override
    public abstract SignatureStore getSignatureStore();

    public DummyMethodCompilerPass(AbstractCompiler compiler) {
      super(compiler);
    }

    @Override
    public Callback getActingCallback() {
      throw new UnsupportedOperationException();
    }

    @Override
    public SignatureStore getSignatureStore() {
      return new DummySignatureStore();
    }
  }

  @Test
  public void testMethodCompilerPass() {
    AbstractCompiler compiler = newAbstractCompiler();
    MethodCompilerPass passthrough = new DummyMethodCompilerPass(compiler);

    Node root = parseJs("function foo() {}", null);
    passthrough.process(null, root);

    SignatureStore signatureStore = passthrough.getSignatureStore();

    assertThat(signatureStore.getSignatures()).containsExactly("foo");

    passthrough = new DummyMethodCompilerPass(compiler) {
      @Override
      Callback getActingCallback() {
        // No-op
        return new AbstractPostOrderCallback() {};
      }
    };

    root = parseJs("function foo(a, b) {}", null);
    passthrough.process(null, root);

    signatureStore = passthrough.getSignatureStore();

    assertThat(signatureStore.getSignatures()).containsExactly("foo");
    Node foo = findNode("foo", root);
    passthrough.addPossibleSignature("foo", foo, null);
    assertThat(passthrough.methodDefinitions.get("foo")).isEqualTo(foo);
  }

  @Test
  public void testGetExternMethods() {
    AbstractCompiler compiler = newAbstractCompiler();
    MethodCompilerPass passthrough = new DummyMethodCompilerPass(compiler) {
      @Override
      Callback getActingCallback() {
        // No-op
        return new AbstractPostOrderCallback() {};
      }
    };

    Node externs = parseJs("Foo.prototype.getBar = function() {};\n"
        + "Foo.prototype.getBaz = getQuux;", null);
    passthrough.process(externs, null);

    SignatureStore signatureStore = passthrough.getSignatureStore();
    Multimap<String, Node> methodDefinitions = passthrough.methodDefinitions;

    Set<String> externMethods = passthrough.externMethods;
    Set<String> nonMethodProperties = passthrough.nonMethodProperties;

    assertThat(externMethods).containsExactly("getBar", "getBaz");
    assertThat(nonMethodProperties).isEmpty();

    Node bar = findNode("getBar", externs);
    Node baz = findNode("getBaz", externs);
    Node quux = findNode("getQuux", externs);

    Node functionBar = methodDefinitions.get("getBar").get(0);
    Node functionBaz = methodDefinitions.get("getBaz").get(0);

    assertThat(functionBar).isSameInstanceAs(bar);
    assertThat(functionBaz).isSameInstanceAs(quux);
  }
}