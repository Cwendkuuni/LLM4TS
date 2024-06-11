import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.rhino.Token;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MethodCompilerPass.class})
public class MethodCompilerPassTest extends CompilerPassTestBase {

  @Test
  public void testMethodCompilerPass() {
    // Create a mock of the AbstractCompiler class
    AbstractCompiler compilerMock = Mockito.mock(AbstractCompiler.class);

    // Set up the MethodCompilerPass instance with the mock AbstractCompiler
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compilerMock);

    // Test the process method with an empty externs and root
    Node externs = null;
    Node root = null;
    methodCompilerPass.process(externs, root);
    assertEquals(0, methodCompilerPass.externMethods.size());
    assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());
    assertEquals(0, methodCompilerPass.nonMethodProperties.size());
    assertEquals(0, methodCompilerPass.methodDefinitions.size());

    // Test the process method with an externs file
    externs = createMockExternsNode();
    root = createMockRootNode();
    methodCompilerPass.process(externs, root);
    assertEquals(3, methodCompilerPass.externMethods.size());
    assertEquals(1, methodCompilerPass.externMethodsWithoutSignatures.size());
    assertEquals(1, methodCompilerPass.nonMethodProperties.size());
    assertEquals(3, methodCompilerPass.methodDefinitions.size());

    // Test for edge cases and exceptions
    testMethodCompilerPassEdgeCases(methodCompilerPass, compilerMock);
  }

  /**
   * Helper method to test edge cases and exceptions for MethodCompilerPass
   *
   * @param methodCompilerPass - the instance of MethodCompilerPass under test
   * @param compilerMock - the mock AbstractCompiler
   */
  private void testMethodCompilerPassEdgeCases(
      MethodCompilerPass methodCompilerPass, AbstractCompiler compilerMock) {

    // Test null input
    methodCompilerPass.process(null, root);
    assertNull(methodCompilerPass);

    methodCompilerPass.process(externs, null);
    assertNull(methodCompilerPass);

    // Test empty methodDefinitions
    SignatureStore signatureStoreMock = Mockito.mock(SignatureStore.class);
    PowerMockito.spy(methodCompilerPass);
    PowerMockito.doReturn(signatureStoreMock).when(methodCompilerPass).getSignatureStore();
    methodCompilerPass.methodDefinitions.clear();
    methodCompilerPass.process(externs, root);
    assertEquals(0, methodCompilerPass.methodDefinitions.size());

    // Test getActingCallback and getSignatureStore are not abstract
    try {
      new MethodCompilerPass(compilerMock) {
        @Override
        Callback getActingCallback() {
          return null;
        }

        @Override
        SignatureStore getSignatureStore() {
          return null;
        }
      };
      fail("Should not be able to instantiate subclass without getActingCallback and getSignatureStore methods");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  /**
   * Helper method to create a mock externs Node
   *
   * @return Node - the mock externs Node
   */
  private Node createMockExternsNode() {
    return createMockNode("{}");
  }

  /**
   * Helper method to create a mock root Node
   *
   * @return Node - the mock root Node
   */
  private Node createMockRootNode() {
    Node root = createMockNode("function main() {}");
    return root;
  }

  /**
   * Helper method to create a mock Node
   *
   * @param nodeContent - the content of the Node
   * @return Node - the mock Node
   */
  private Node createMockNode(String nodeContent) {
    Node node = Mockito.mock(Node.class);
    PowerMockito.when(node.toString()).thenReturn(nodeContent);
    PowerMockito.when(node.getType()).thenReturn(Token.PROGRAM);
    return node;
  }
}