import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MethodCompilerPassTest {
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStoreSpy;

  @Before
  public void setUp() {
    AbstractCompiler compilerSpy = new AbstractCompiler() {};
    methodCompilerPass = new MethodCompilerPass(compilerSpy) {
      @Override
      SignatureStore getSignatureStore() {
        return signatureStoreSpy;
      }

      @Override
      Callback getActingCallback() {
        // empty implementation
        return new Callback() {};
      }
    };
    signatureStoreSpy = new SignatureStore() {
      Multimap<String, Node> signatures = HashMultimap.create();

      @Override
      public void reset() {
      }

      @Override
      public void addSignature(String functionName, Node functionNode, String sourceFile) {
        signatures.put(functionName, functionNode);
      }

      @Override
      public void removeSignature(String functionName) {
      }
    };
  }

  // Test all public methods of MethodCompilerPass

  @Test
  public void testProcess() {
    // Given
    Node externs = Node.newNode(Token.BLOCK, Lists.newArrayList());
    Node root = Node.newNode(Token.BLOCK, Lists.newArrayList());

    // When
    methodCompilerPass.process(externs, root);

    // Then
    assertEquals(0, methodCompilerPass.externMethods.size());
    assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());
    assertSame(methodCompilerPass.methodDefinitions, signatureStoreSpy.signatures);
  }

  // Test edge cases and exception handling scenarios

  @Test(expected = IllegalStateException.class)
  public void testAddSignatureWithUndefinedFunction() {
    // Given
    methodCompilerPass.getSignatureStore().removeSignature("foo");
    methodCompilerPass.addSignature("foo", null, null);

    // Then exception is thrown
  }

  @Test
  public void testExternMethodWithSignature() {
    // Given
    Node externs = Node.newNode(
        Token.BLOCK,
        Lists.newArrayList(
            Node.newNode(Token.ASSIGN, Lists.newArrayList(
                Node.newNode(Token.GETPROP, Lists.newArrayList(
                    Node.newNode(Token.STRING, "Foo"),
                    Node.newNode(Token.STRING, "prototype")
                )),
                Node.newNode(Token.OBJECTLIT, Lists.newArrayList(
                    Node.newNode(Token.STRING, "bar"),
                    Node.newNode(Token.FUNCTION, Lists.newArrayList(
                        Node.newNode(Token.VOID, Lists.newArrayList())
                    ))
                ))
            ))
        )
    );

    // When
    methodCompilerPass.process(externs, Node.newNode(Token.BLOCK, Lists.newArrayList()));

    // Then
    assertTrue(methodCompilerPass.externMethods.contains("Foo.prototype.bar"));
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertSame(methodCompilerPass.methodDefinitions.get("Foo.prototype.bar"),
        signatureStoreSpy.signatures.get("Foo.prototype.bar"));
  }

  @Test
  public void testExternMethodWithoutSignature() {
    // Given
    Node externs = Node.newNode(
        Token.BLOCK,
        Lists.newArrayList(
            Node.newNode(Token.ASSIGN, Lists.newArrayList(
                Node.newNode(Token.GETPROP, Lists.newArrayList(
                    Node.newNode(Token.STRING, "Foo"),
                    Node.newNode(Token.STRING, "prototype")
                )),
                Node.newNode(Token.STRING, "bar")
            ))
        )
    );

    // When
    methodCompilerPass.process(externs, Node.newNode(Token.BLOCK, Lists.newArrayList()));

    // Then
    assertFalse(methodCompilerPass.externMethods.contains("Foo.prototype.bar"));
    assertEquals(0, methodCompilerPass.methodDefinitions.size());
  }

  @Test
  public void testNonMethodProperty() {
    // Given
    Node externs = Node.newNode(
        Token.BLOCK,
        Lists.newArrayList(
            Node.newNode(Token.ASSIGN, Lists.newArrayList(
                Node.newNode(Token.GETPROP, Lists.newArrayList(
                    Node.newNode(Token.STRING, "Foo"),
                    Node.newNode(Token.STRING, "prototype")
                )),
                Node.newNode(Token.STRING, "length")
            ))
        )
    );

    // When
    methodCompilerPass.process(externs, Node.newNode(Token.BLOCK, Lists.newArrayList()));

    // Then
    assertFalse(methodCompilerPass.externMethods.contains("Foo.prototype.length"));
    assertEquals(0, methodCompilerPass.methodDefinitions.size());
  }
}