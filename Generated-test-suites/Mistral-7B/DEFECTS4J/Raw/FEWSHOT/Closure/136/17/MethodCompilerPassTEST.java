package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.test.checkers.TypeChecker;
import org.junit.Before;
import org.junit.Test;

public class MethodCompilerPassTest {
  private Compiler compiler;
  private CheckerFactory checkerFactory;
  private SignatureStore signatureStore;

  @Before
  public void setUp() {
    compiler = new Compiler();
    checkerFactory = new CheckerFactory();
    signatureStore = new SignatureStore() {
      @Override
      public void reset() {}

      @Override
      public void addSignature(String functionName, Node functionNode, String sourceFile) {
        System.out.println("Added signature for function: " + functionName);
      }

      @Override
      public void removeSignature(String functionName) {}
    };
  }

  @Test
  public void testMethodCompilerPass() {
    String sourceCode =
        "function Foo() {}\n"
            + "Foo.prototype.bar = function() {}\n"
            + "Foo.prototype = {'getBar': function() {}}";

    Node root = compiler.parseTree(checkerFactory, sourceCode, null);

    MethodCompilerPass pass = new MethodCompilerPass(compiler) {
      @Override
      abstract Callback getActingCallback() {
        return null;
      }

      @Override
      abstract SignatureStore getSignatureStore() {
        return signatureStore;
      }
    };

    pass.process(null, root);

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    Node prototype = traversal.findFirstDescendantOfType(root, Token.OBJECTLIT);

    List<String> expectedMethodNames = Lists.newArrayList("bar", "getBar");

    for (Node methodNameNode : prototype.getChildren()) {
      if (methodNameNode.getType() == Token.STRING) {
        String methodName = methodNameNode.getString();
        Node methodDefinition = methodDefinitions.get(methodName);

        assertThat(expectedMethodNames).contains(methodName);
        assertThat(methodDefinition).isNotNull();
      }
    }

    assertNull(signatureStore.getSignature("nonExistentMethod"));
  }
}

class SignatureStoreTest implements MethodCompilerPass.SignatureStore {}