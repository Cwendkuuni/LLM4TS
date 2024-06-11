import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.ast.AbstractNode;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class MethodCompilerPassTest {
  private Compiler compiler;
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @Before
  public void setup() {
    compiler = new Compiler();
    methodCompilerPass = new MyMethodCompilerPass(compiler);
    signatureStore = new SignatureStore() {
      private Multimap<String, Node> storedSignatures = HashMultimap.create();

      @Override
      public void reset() {
      }

      @Override
      public void addSignature(String functionName, Node functionNode, String sourceFile) {
        storedSignatures.put(functionName, functionNode);
      }

      @Override
      public void removeSignature(String functionName) {
        storedSignatures.remove(functionName);
      }

      public Node getStoredSignature(String functionName) {
        return storedSignatures.get(functionName).get(0);
      }
    };
    methodCompilerPass.setSignatureStore(signatureStore);
  }

  @Test
  public void testSimpleMethod() {
    String sourceCode = "function Foo() {}";
    Node externs = null;
    Node root = Node.newJsonText(sourceCode);

    methodCompilerPass.process(externs, root);

    Node functionNode = methodCompilerPass.getStoredSignature("Foo");
    assertSame("Expected function node for Foo", Node.FUNCTION, functionNode.getType());
  }

  @Test
  public void testMethodWithArguments() {
    String sourceCode = "function Foo(a, b) {}";
    Node externs = null;
    Node root = Node.newJsonText(sourceCode);

    methodCompilerPass.process(externs, root);

    Node functionNode = methodCompilerPass.getStoredSignature("Foo");
    AbstractNode function = AbstractNode.getFunctionFromNode(functionNode);
    assertEquals("Expected function with two arguments", 2, function.getNumParams());
  }

  @Test
  public void testMethodWithoutSignatureInExterns() {
    String sourceCode = "function Foo() {}";
    Node externs = Node.newJsonText("{ Foo: function() {} }");
    Node root = Node.newJsonText(sourceCode);

    methodCompilerPass.process(externs, root);

    Node functionNode = methodCompilerPass.getStoredSignature("Foo");
    assertNull("Expected null for undeclared method without signature");
  }

  @Test
  public void testMethodWithSignatureInExterns() {
    String sourceCode = "function Foo(a, b) {}";
    Node externs = Node.newJsonText("{ Foo: function(a, b) {} }");
    Node root = Node.newJsonText(sourceCode);

    methodCompilerPass.process(externs, root);

    Node functionNode = methodCompilerPass.getStoredSignature("Foo");
    AbstractNode function = AbstractNode.getFunctionFromNode(functionNode);
    assertEquals("Expected function with two arguments", 2, function.getNumParams());
  }

  @Test
  public void testPropertyThatIsNotAMethod() {
    String sourceCode = "function Foo() {" +
        "this.bar = 42;" +
        "}";
    Node externs = null;
    Node root = Node.newJsonText(sourceCode);

    methodCompilerPass.process(externs, root);

    assertFalse("Expected false for property that is not a method",
        methodCompilerPass.externMethods.contains("bar")
    );
  }

  @Test
  public void testMethodWithPrototype() {
    String sourceCode = "function Foo() {}" +
        "Foo.prototype.getBar = function() { return this.foo; }";
    Node externs = null;
    Node root = Node.newJsonText(sourceCode);

    methodCompilerPass.process(externs, root);

    Node protoNode = methodCompilerPass.getStoredSignature("getBar");
    AbstractNode prototype = AbstractNode.getFunctionFromNode(protoNode);
    assertEquals("Expected function with this reference", 0, prototype.getNumParams());
  }

  @Test
  public void testMethodsWithSameNameButDifferentSignatures() {
    String sourceCode1 = "function Foo(a) {}";
    String sourceCode2 = "function Foo(b) {}";
    Node externs = Node.newJsonText("{ Foo: [] }");
    Node root1 = Node.newJsonText(sourceCode1);
    Node root2 = Node.newJsonText(sourceCode2);

    methodCompilerPass.process(externs, root1);
    methodCompilerPass.process(externs, root2);

    Node firstFunctionNode = methodCompilerPass.getStoredSignature("Foo");
    AbstractNode firstFunction = AbstractNode.getFunctionFromNode(firstFunctionNode);
    assertEquals("Expected function with one argument for the first method", 1, firstFunction.getNumParams());

    Node secondFunctionNode = methodCompilerPass.getStoredSignature("Foo");
    AbstractNode secondFunction = AbstractNode.getFunctionFromNode(secondFunctionNode);
    assertEquals("Expected function with one argument for the second method", 1, secondFunction.getNumParams());
    assertFalse("Expected false for methods with different signatures to be equal", firstFunctionNode.equals(secondFunctionNode));
  }

  @Test
  public void testMethodWithInvalidSignature() {
    String sourceCode = "function Foo() {}";
    Node externs = Node.newJsonText("{ Foo: function(a, b, c) {} }");
    Node root = Node.newJsonText(sourceCode);

    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.VERBOSE);

    methodCompilerPass.process(externs, root);
    assertEquals("Expected warning for method with invalid signature", 1, compiler.getNumWarnings());
  }

  @Test
  public void testMethodWithMissingParentheses() {
    String sourceCode = "function Foo bar() {}";
    Node externs = null;
    Node root = Node.newJsonText(sourceCode);

    methodCompilerPass.process(externs, root);

    assertNull("Expected null for method with missing parentheses");
  }

  @Test
  public void testMethodWithMissingName() {
    String sourceCode = "function() {}";
    Node externs = null;
    Node root = Node.newJsonText(sourceCode);

    methodCompilerPass.process(externs, root);

    assertNull("Expected null for method without name");
  }

  @Test
  public void testMethodWithEmptyBody() {
    String sourceCode = "function Foo() {}";
    Node externs = null;
    Node root = Node.newJsonText(sourceCode);

    methodCompilerPass.process(externs, root);

    Node functionNode = methodCompilerPass.getStoredSignature("Foo");
    assertSame("Expected empty function for method with empty body", Node.EMPTY, functionNode);
  }
}

class MyMethodCompilerPass extends MethodCompilerPass {
  MyMethodCompilerPass(Compiler compiler) {
    super(compiler);
  }

  @Override
  Callback getActingCallback() {
    return new DoNothingCallback();
  }

  static class DoNothingCallback extends AbstractPostOrderCallback {
    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      // No-op
    }
  }

  @Override
  SignatureStore getSignatureStore() {
    return signatureStore;
  }
}