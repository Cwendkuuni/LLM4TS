package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MethodCompilerPassTest {

  @Test
  public void testMethodCompilerPass_BasicFunctionDeclaration() {
    MethodCompilerPass passthrough = new MethodCompilerPass(null) {
      @Override
      abstract SignatureStore getSignatureStore();

      @Override
      abstract Callback getActingCallback();

      @Override
      SignatureStore getSignatureStore() {
        throw new UnsupportedOperationException();
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }

      @Override
      void process(Node externs, Node root) {
        // No-op
      }
    };

    Node externs = Node.newObjectLiteral();
    externs.addChild(Node.newProperty("foo", Node.newFunction(null, null, true, null, null)));

    passthrough.process(externs, Node.newObjectLiteral());

    Set<String> expectedExternMethods = Sets.newHashSet("foo");
    Set<String> actualExternMethods = passthrough.externMethods;

    assertEquals(expectedExternMethods, actualExternMethods);
  }

  @Test
  public void testMethodCompilerPass_FunctionDeclarationWithInvalidSignature_Ignores() {
    MethodCompilerPass passthrough = new MethodCompilerPass(null) {
      @Override
      abstract SignatureStore getSignatureStore();

      @Override
      abstract Callback getActingCallback();

      @Override
      SignatureStore getSignatureStore() {
        throw new UnsupportedOperationException();
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }

      @Override
      void process(Node externs, Node root) {
        // No-op
      }
    };

    Node externs = Node.newObjectLiteral();
    externs.addChild(Node.newProperty("foo", Node.newFunction(null, null, true, null, "invalidSignature")));

    passthrough.process(externs, Node.newObjectLiteral());

    Set<String> expectedExternMethodsIgnored = Sets.newHashSet("foo");
    Set<String> actualExternMethods = passthrough.externMethods;

    assertEquals(expectedExternMethodsIgnored, actualExternMethods);
  }

  @Test
  public void testMethodCompilerPass_FunctionDeclarationWithValidSignature() {
    MethodCompilerPass passthrough = new MethodCompilerPass(null) {
      @Override
      abstract SignatureStore getSignatureStore();

      @Override
      abstract Callback getActingCallback();

      @Override
      SignatureStore getSignatureStore() {
        throw new UnsupportedOperationException();
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }

      @Override
      void process(Node externs, Node root) {
        // No-op
      }
    };

    Node externs = Node.newObjectLiteral();
    externs.addChild(Node.newProperty("foo", Node.newFunction(null, null, true, null, null)));

    Node root = Node.newProgram(Node.newList(), Node.newList());

    passthrough.process(externs, root);

    Set<String> expectedExternMethods = Sets.newHashSet("foo");
    Set<String> actualExternMethods = passthrough.externMethods;

    assertEquals(expectedExternMethods, actualExternMethods);

    Node fooFunction = passthrough.methodDefinitions.get("foo").getValue();
    assertEquals(3, fooFunction.getNumChildren());

    assertFalse(fooFunction.getChild(0).isFunction());
    assertEquals("function", fooFunction.getChild(1).getString());
    assertFalse(fooFunction.getChild(2).isFunction());
  }

  @Test
  public void testMethodCompilerPass_PropertyDeclaration() {
    MethodCompilerPass passthrough = new MethodCompilerPass(null) {
      @Override
      abstract SignatureStore getSignatureStore();

      @Override
      abstract Callback getActingCallback();

      @Override
      SignatureStore getSignatureStore() {
        throw new UnsupportedOperationException();
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }

      @Override
      void process(Node externs, Node root) {
        // No-op
      }
    };

    Node externs = Node.newObjectLiteral();
    externs.addChild(Node.newProperty("foo", Node.newString("bar")));

    passthrough.process(externs, Node.newObjectLiteral());

    Set<String> expectedNonMethodProperties = Sets.newHashSet("foo");
    Set<String> actualNonMethodProperties = passthrough.nonMethodProperties;

    assertEquals(expectedNonMethodProperties, actualNonMethodProperties);

    assertFalse(passthrough.externMethods.isEmpty());
  }

  @Test
  public void testMethodCompilerPass_PropertyDeclarationWithMethodName_Ignores() {
    MethodCompilerPass passthrough = new MethodCompilerPass(null) {
      @Override
      abstract SignatureStore getSignatureStore();

      @Override
      abstract Callback getActingCallback();

      @Override
      SignatureStore getSignatureStore() {
        throw new UnsupportedOperationException();
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }

      @Override
      void process(Node externs, Node root) {
        // No-op
      }
    };

    Node externs = Node.newObjectLiteral();
    externs.addChild(Node.newProperty("foo", Node.newFunction(null, null, true, null, null)));

    passthrough.process(externs, Node.newObjectLiteral());

    assertEquals(0, passthrough.nonMethodProperties.size());
  }

  @Test
  public void testMethodCompilerPass_MultipleDeclarations() {
    MethodCompilerPass passthrough = new MethodCompilerPass(null) {
      @Override
      abstract SignatureStore getSignatureStore();

      @Override
      abstract Callback getActingCallback();

      @Override
      SignatureStore getSignatureStore() {
        throw new UnsupportedOperationException();
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }

      @Override
      void process(Node externs, Node root) {
        // No-op
      }
    };

    Node externs = Node.newObjectLiteral();
    externs.addChild(Node.newProperty("foo", Node.newFunction(null, null, true, null, null)));
    externs.addChild(Node.newProperty("bar", Node.newFunction(null, null, true, null, null)));

    passthrough.process(externs, Node.newObjectLiteral());

    Set<String> expectedExternMethods = Sets.newHashSet("foo", "bar");
    Set<String> actualExternMethods = passthrough.externMethods;

    assertEquals(expectedExternMethods, actualExternMethods);
  }

  @Test
  public void testMethodCompilerPass_MultipleDeclarationsWithInvalidSignature_Ignores() {
    MethodCompilerPass passthrough = new MethodCompilerPass(null) {
      @Override
      abstract SignatureStore getSignatureStore();

      @Override
      abstract Callback getActingCallback();

      @Override
      SignatureStore getSignatureStore() {
        throw new UnsupportedOperationException();
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }

      @Override
      void process(Node externs, Node root) {
        // No-op
      }
    };

    Node externs = Node.newObjectLiteral();
    externs.addChild(Node.newProperty("foo", Node.newFunction(null, null, true, null, "invalidSignature")));
    externs.addChild(Node.newProperty("bar", Node.newFunction(null, null, true, null, null)));

    passthrough.process(externs, Node.newObjectLiteral());

    Set<String> expectedExternMethodsIgnored = Sets.newHashSet("foo");
    Set<String> actualExternMethods = passthrough.externMethods;

    assertEquals(expectedExternMethodsIgnored, actualExternMethods);
    assertEquals(1, passthrough.nonMethodProperties.size());
  }

  @Test
  public void testMethodCompilerPass_PrototypeDeclaration() {
    MethodCompilerPass passthrough = new MethodCompilerPass(null) {
      @Override
      abstract SignatureStore getSignatureStore();

      @Override
      abstract Callback getActingCallback();

      @Override
      SignatureStore getSignatureStore() {
        throw new UnsupportedOperationException();
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }

      @Override
      void process(Node externs, Node root) {
        // No-op
      }
    };

    Node externs = Node.newObjectLiteral();
    externs.addChild(Node.newProperty("foo", Node.newObjectLiteral()));
    externs.getLastChild().addChild(Node.newProperty("prototype", Node.newObjectLiteral()));
    externs.getLastChild().getLastChild().addChild(Node.newProperty("getBar", Node.newFunction(null, null, true, null, null)));

    passthrough.process(externs, Node.newObjectLiteral());

    Set<String> expectedExternMethods = Sets.newHashSet("getBar");
    Set<String> actualExternMethods = passthrough.externMethods;

    assertEquals(expectedExternMethods, actualExternMethods);
  }

  @Test
  public void testMethodCompilerPass_PrototypeDeclaration_IgnoresInvalidSignature() {
    MethodCompilerPass passthrough = new MethodCompilerPass(null) {
      @Override
      abstract SignatureStore getSignatureStore();

      @Override
      abstract Callback getActingCallback();

      @Override
      SignatureStore getSignatureStore() {
        throw new UnsupportedOperationException();
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }

      @Override
      void process(Node externs, Node root) {
        // No-op
      }
    };

    Node externs = Node.newObjectLiteral();
    externs.addChild(Node.newProperty("foo", Node.newObjectLiteral()));
    externs.getLastChild().addChild(Node.newProperty("prototype", Node.newObjectLiteral()));
    externs.getLastChild().getLastChild().addChild(Node.newProperty("getBar", Node.newFunction(null, null, true, null, "invalidSignature")));

    passthrough.process(externs, Node.newObjectLiteral());

    Set<String> expectedExternMethodsIgnored = Sets.newHashSet();
    Set<String> actualExternMethods = passthrough.externMethods;

    assertEquals(expectedExternMethodsIgnored, actualExternMethods);
    assertEquals(0, passthrough.nonMethodProperties.size());
  }

  @Test
  public void testMethodCompilerPass_PrototypeDeclarationWithMethodName_Ignores() {
    MethodCompilerPass passthrough = new MethodCompilerPass(null) {
      @Override
      abstract SignatureStore getSignatureStore();

      @Override
      abstract Callback getActingCallback();

      @Override
      SignatureStore getSignatureStore() {
        throw new UnsupportedOperationException();
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }

      @Override
      void process(Node externs, Node root) {
        // No-op
      }
    };

    Node externs = Node.newObjectLiteral();
    externs.addChild(Node.newProperty("foo", Node.newObjectLiteral()));
    externs.getLastChild().addChild(Node.newProperty("prototype", Node.newObjectLiteral()));
    externs.getLastChild().getLastChild().addChild(Node.newProperty("prototype", Node.newFunction(null, null, true, null, null)));

    passthrough.process(externs, Node.newObjectLiteral());

    assertEquals(0, passthrough.nonMethodProperties.size());
  }

  @Test
  public void testMethodCompilerPass_PrototypeDeclarationWithValidSignature() {
    MethodCompilerPass passthrough = new MethodCompilerPass(null) {
      @Override
      abstract SignatureStore getSignatureStore();

      @Override
      abstract Callback getActingCallback();

      @Override
      SignatureStore getSignatureStore() {
        throw new UnsupportedOperationException();
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }

      @Override
      void process(Node externs, Node root) {
        // No-op
      }
    };

    Node externs = Node.newObjectLiteral();
    externs.addChild(Node.newProperty("foo", Node.newObjectLiteral()));
    externs.getLastChild().addChild(Node.newProperty("prototype", Node.newObjectLiteral()));
    externs.getLastChild().getLastChild().addChild(Node.newProperty("getBar", Node.newFunction(
        null, Node.newList(Node.newSymbol("arg1")), true, null, null
    )));

    passthrough.process(externs, Node.newObjectLiteral());

    Set<String> expectedExternMethods = Sets.newHashSet("getBar");
    Set<String> actualExternMethods = passthrough.externMethods;

    assertEquals(expectedExternMethods, actualExternMethods);
  }
}