package org.apache.commons.cli.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.CompilationLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.testing.TestCompiler;
import com.google.javascript.rhino.NodeFactory;
import org.apache.commons.cli.MethodCompilerPass;
import org.apache.commons.cli.MethodCompilerPass.GetExternMethods;
import org.apache.commons.cli.MethodCompilerPass.GatherSignatures;

public class MethodCompilerPassTest {
  private static final String TEST_SOURCE = "test.js";

  @Test
  public void testExternMethods() {
    CompilerOptions options = new CompilerOptions(CompilationLevel.ADVANCED);

    SignatureStore signatureStore = new SignatureStore() {
      private Multimap<String, Node> signatures = HashMultimap.create();

      @Override
      public void reset() {}

      @Override
      public void addSignature(String functionName, Node functionNode, String sourceFile) {
        signatures.put(functionName, functionNode);
      }

      @Override
      public void removeSignature(String functionName) {}

      public Node getSignature(String functionName) {
        return signatures.get(functionName).get(0);
      }
    };

    MethodCompilerPass pass = new MethodCompilerPass(new TestCompiler(options)) {
      @Override
      abstract Callback getActingCallback() {
        return new Callback() {
          @Override
          public void call(Node node, NodeTraversal t) {}
        };
      }

      @Override
      abstract SignatureStore getSignatureStore() {
        return signatureStore;
      }
    };

    Node sourceNode = NodeFactory.createScope(NodeFactory.newTextNode("(function() { }))"));
    Node externsNode = NodeFactory.createScope(NodeFactory.newTextNode("function foo() {};"));

    pass.process(externsNode, sourceNode);

    Node fooSignature = pass.getSignature("foo");
    assertThat(fooSignature).isNotNull();
  }

  @Test
  public void testNonMethodProperties() {
    CompilerOptions options = new CompilerOptions(CompilationLevel.ADVANCED);

    MethodCompilerPass pass = new MethodCompilerPass(new TestCompiler(options)) {
      @Override
      abstract Callback getActingCallback() {
        return new Callback() {
          @Override
          public void call(Node node, NodeTraversal t) {}
        };
      }

      @Override
      abstract SignatureStore getSignatureStore() {
        return null;
      }
    };

    Node sourceNode = NodeFactory.createScope(NodeFactory.newTextNode("var foo = 1; var bar = function() {};"));
    pass.process(null, sourceNode);

    Set<String> nonMethodProperties = pass.nonMethodProperties;
    assertThat(nonMethodProperties).containsExactly("foo");
  }

  @Test
  public void testGatherSignatures() {
    CompilerOptions options = new CompilerOptions(CompilationLevel.ADVANCED);

    SignatureStore signatureStore = new SignatureStore() {
      private Multimap<String, Node> signatures = HashMultimap.create();

      @Override
      public void reset() {}

      @Override
      public void addSignature(String functionName, Node functionNode, String sourceFile) {
        signatures.put(functionName, functionNode);
      }

      @Override
      public void removeSignature(String functionName) {}

      public Node getSignature(String functionName) {
        return signatures.get(functionName).get(0);
      }
    };

    MethodCompilerPass pass = new MethodCompilerPass(new TestCompiler(options)) {
      @Override
      abstract Callback getActingCallback() {
        return new Callback() {
          @Override
          public void call(Node node, NodeTraversal t) {
            if (node.getType() == Token.FUNCTION) {
              String functionName = node.getFirstChild().getString();
              Node function = node;
              signatureStore.addSignature(functionName, function, TEST_SOURCE);
            }
          }
        };
      }

      @Override
      abstract SignatureStore getSignatureStore() {
        return signatureStore;
      }
    };

    Node sourceNode = NodeFactory.createScope(
        NodeFactory.newTextNode("function Foo() { this.bar = function() {}; }"));
    pass.process(null, sourceNode);

    Node barSignature = pass.getSignature("Foo.bar");
    assertThat(barSignature).isNotNull();
  }

  @Test
  public void testGetExternMethods() {
    CompilerOptions options = new CompilerOptions(CompilationLevel.ADVANCED);

    MethodCompilerPass pass = new MethodCompilerPass(new TestCompiler(options)) {
      @Override
      abstract Callback getActingCallback() {
        return new Callback() {
          @Override
          public void call(Node node, NodeTraversal t) {}
        };
      }

      @Override
      abstract SignatureStore getSignatureStore() {
        return null;
      }

      @Override
      Class<? extends AbstractPostOrderCallback> getGetExternMethodsCallbackClass() {
        return GetExternMethods.class;
      }
    };

    Node externsNode = NodeFactory.createScope(NodeFactory.newTextNode("function foo() {};"));
    Node sourceNode = NodeFactory.createScope(NodeFactory.newTextNode(""));
    pass.process(externsNode, sourceNode);

    Node fooNode = pass.methodDefinitions.get("foo").get(0);
    Node fooSignature = pass.getSignature("foo");
    assertThat(fooNode).isEqualTo(fooSignature);
  }
}

class TestCompiler extends Compiler {
  public TestCompiler(CompilerOptions options) {
    super(options);
  }

  @Override
  public Node compileModule(Node node, Node parent, CompilationLevel level) {
    return null;
  }
}

class SignatureStore {}