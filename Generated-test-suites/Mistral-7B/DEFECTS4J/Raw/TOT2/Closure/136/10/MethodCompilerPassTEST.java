import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.NodeTraversal;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MethodCompilerPassTest {
  private AbstractCompiler compiler;
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @Before
  public void setup() {
    compiler = new Compiler();
    methodCompilerPass = new MethodCompilerPass(compiler) {
      SignatureStore mySignatureStore = new SignatureStore() {
        private Multimap<String, Node> signatures = HashMultimap.create();

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
          return signatures.get(functionName).get(0);
        }
      };

      @Override
      public SignatureStore getSignatureStore() {
        return mySignatureStore;
      }

      @Override
      Callback getActingCallback() {
        return new TestingCallback();
      }
    };
  }

  @Test
  public void testSimpleMethod() {
    String sourceCode = "function foo() {};";
    Node rootNode = RhinoParser.parseStringToNode(sourceCode);
    methodCompilerPass.process(null, rootNode);

    Node functionNode = methodCompilerPass.getSignatureStore().getSignature("foo");
    assertEquals(Token.FUNCTION, functionNode.getType());
  }

  @Test(expected = IllegalStateException.class)
  public void testUndefinedMethodInExterns() {
    String sourceCode = "{}";
    Node rootNode = RhinoParser.parseStringToNode(sourceCode);
    methodCompilerPass.process(Node.toTree(Lists.newArrayList(
        Node.newString("externs", "function foo();")
    )), rootNode);

    methodCompilerPass.getSignatureStore().removeSignature("foo");
    methodCompilerPass.getActingCallback().process(
        NodeTraversal.create(compiler), null, null);
  }

  @Test
  public void testMethodWithArguments() {
    String sourceCode = "function foo(a, b) {};";
    Node rootNode = RhinoParser.parseStringToNode(sourceCode);
    methodCompilerPass.process(null, rootNode);

    Node functionNode = methodCompilerPass.getSignatureStore().getSignature("foo");
    assertEquals(Token.FUNCTION, functionNode.getType());
    assertEquals(2, functionNode.getNumChildren());
    assertEquals("a", functionNode.getChild(0).getString());
    assertEquals("b", functionNode.getChild(1).getString());
  }

  @Test
  public void testMethodWithPrototype() {
    String sourceCode = "function Foo() {};\nFoo.prototype.bar = function() {};";
    Node rootNode = RhinoParser.parseStringToNode(sourceCode);
    methodCompilerPass.process(null, rootNode);

    Node prototypeNode = methodCompilerPass.methodDefinitions.get("Foo.prototype");
    Node prototypeFunctionNode = prototypeNode.getFirstChild();
    Node barMethodNode = prototypeFunctionNode.getFirstChild();

    Node functionNode = methodCompilerPass.getSignatureStore().getSignature("Foo.prototype.bar");
    assertEquals(Token.FUNCTION, functionNode.getType());
  }

  @Test(expected = IllegalStateException.class)
  public void testNonMethodProperty() {
    String sourceCode = "function Foo() { this.bar = 42; }";
    Node rootNode = RhinoParser.parseStringToNode(sourceCode);
    methodCompilerPass.process(null, rootNode);

    methodCompilerPass.getActingCallback().process(
        NodeTraversal.create(compiler), null, null);
  }

  @Test
  public void testExternMethodWithoutSignature() {
    String sourceCode = "{}";
    Node rootNode = RhinoParser.parseStringToNode(sourceCode);
    methodCompilerPass.process(
        Node.toTree(Lists.newArrayList(
            Node.newString("externs", "function foo();")
        )), rootNode);

    assertEquals(1, methodCompilerPass.externMethodsWithoutSignatures.size());
    assertEquals("foo", methodCompilerPass.externMethodsWithoutSignatures.iterator().next());
  }

  @Test(expected = IllegalStateException.class)
  public void testMultipleMethodsWithSameName() {
    String sourceCode = "function foo() {};\nfunction foo() {};";
    Node rootNode = RhinoParser.parseStringToNode(sourceCode);
    methodCompilerPass.process(null, rootNode);

    methodCompilerPass.getActingCallback().process(
        NodeTraversal.create(compiler), null, null);
  }

  @Test
  private class TestingCallback extends AbstractPostOrderCallback {
    @Override
    public void process(NodeTraversal t, Node n, Node parent) {}
  }
}