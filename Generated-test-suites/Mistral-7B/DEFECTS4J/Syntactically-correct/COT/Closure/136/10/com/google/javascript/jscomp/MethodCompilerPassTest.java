import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Token.*;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.MethodCompilerPass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class MethodCompilerPassTest {
  private Compiler compiler;
  private SignatureStore signatureStoreMock;
  private MethodCompilerPass methodCompilerPass;

  @Before
  public void setup() {
    compiler = Mockito.mock(Compiler.class);
    signatureStoreMock = Mockito.mock(SignatureStore.class);
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return signatureStoreMock;
      }

      @Override
      Callback getActingCallback() {
        return new TestCallback();
      }
    };
  }

  @Test
  public void testProcess_ExternsWithMethods_AddsSignaturesToSignatureStore() {
    // Given
    String source = "Foo.prototype.getBar = function() { return 1; }";
    Node externs = Node.jsSrc(source);
    Node root = Node.objectLiteral();

    // When
    methodCompilerPass.process(externs, root);

    // Then
    String functionName = "getBar";
    String functionSourceName = "Foo";
    Node functionNode = Node.function("getBar", null);

    Mockito.verify(signatureStoreMock).addSignature(functionName, functionNode, functionSourceName);
  }

  @Test
  public void testProcess_ExternsWithFunctions_AddsFunctionsToMethodDefinitions() {
    // Given
    String source = "Foo.getBar = function() { return 1; }";
    Node externs = Node.jsSrc(source);
    Node root = Node.objectLiteral();

    // When
    methodCompilerPass.process(externs, root);

    // Then
    String functionName = "getBar";
    Node functionNode = Node.function("getBar", null);

    assertEquals(functionNode, methodCompilerPass.methodDefinitions.get(functionName));
  }

  @Test
  public void testProcess_RootWithMethods_AddsSignaturesToSignatureStore() {
    // Given
    String source = "function Foo() { this.getBar = function() { return 1; } }";
    Node root = Node.parse(source);

    // When
    methodCompilerPass.process(null, root);

    // Then
    String functionName = "getBar";
    String functionSourceName = "Foo";
    Node functionNode = Node.function("getBar", null);

    Mockito.verify(signatureStoreMock).addSignature(functionName, functionNode, functionSourceName);
  }

  @Test
  public void testProcess_RootWithMethods_AddsFunctionsToMethodDefinitions() {
    // Given
    String source = "function Foo() { this.getBar = function() { return 1; } }";
    Node root = Node.parse(source);

    // When
    methodCompilerPass.process(null, root);

    // Then
    String functionName = "getBar";
    Node functionNode = Node.function("getBar", null);

    assertEquals(functionNode, methodCompilerPass.methodDefinitions.get(functionName));
  }

  @Test(expected = IllegalStateException.class)
  public void testProcess_ExternsWithUndefinedFunction_IgnoresFunction() {
    // Given
    String source = "Foo.getBar";
    Node externs = Node.jsSrc(source);
    Node root = Node.objectLiteral();

    // When
    methodCompilerPass.process(externs, root);

    // Then - an exception should be thrown because getBar is not defined with a signature
  }

  @Test
  public void testProcess_RootWithPrototype_AddsFunctionsToMethodDefinitions() {
    // Given
    String source = "function Foo() { this.prototype.getBar = function() { return 1; } }";
    Node root = Node.parse(source);

    // When
    methodCompilerPass.process(null, root);

    // Then
    String functionName = "getBar";
    Node functionNode = Node.function("getBar", null);

    assertEquals(functionNode, methodCompilerPass.methodDefinitions.get(functionName));
  }

  // Add more tests for edge cases and exceptions as needed
}

class TestCallback implements Callback {
  @Override
  public void process(NodeTraversal t, Node n, Node parent) {}
}