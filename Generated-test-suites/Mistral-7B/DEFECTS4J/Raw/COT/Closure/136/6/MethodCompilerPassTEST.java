import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
public class MethodCompilerPassTest {
  @Mock private AbstractCompiler compiler;
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStoreMock;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return signatureStoreMock;
      }

      @Override
      Callback getActingCallback() {
        // This is not important for the test
        return null;
      }
    };
    signatureStoreMock = Mockito.mock(SignatureStore.class);
  }

  @Test
  public void testConstructor() {
    assertNotNull(methodCompilerPass);
  }

  @Test
  public void testProcessNullExternsAndRoot() {
    methodCompilerPass.process(null, null);
    verifyNoMoreInteractions(compiler, signatureStoreMock);
  }

  @Test
  public void testProcessExterns() {
    String sourceName = "test.js";

    Node externs = createNode("{\n" +
        "  Foo: {\n" +
        "    getBar: function() {}\n" +
        "  },\n" +
        "  Bar: function() {}\n" +
        "}", Token.OBJECTLIT);

    methodCompilerPass.process(externs, null);

    verify(signatureStoreMock).addSignature("Foo.getBar", anyNode(), sourceName);
    verifyZeroInteractions(compiler);

    Set<String> externMethods = methodCompilerPass.externMethods;
    Set<String> externMethodsWithoutSignatures =
        methodCompilerPass.externMethodsWithoutSignatures;
    assertEquals(2, externMethods.size());
    assertEquals(1, externMethodsWithoutSignatures.size());
    assertTrue(externMethods.contains("Foo.getBar"));
    assertTrue(externMethods.contains("Bar"));
    assertTrue(externMethodsWithoutSignatures.contains("Bar"));
  }

  @Test
  public void testProcessRoot() {
    String sourceName = "test.js";

    Node root = createNode("function Foo() {}\n" +
        "function Foo.getBar() {}\n" +
        "function Foo.prototype.getBaz() {}", Token.PROGRAM);

    methodCompilerPass.process(null, root);

    verifyNoMoreInteractions(compiler, signatureStoreMock);

    Set<String> externMethods = methodCompilerPass.externMethods;
    Set<String> externMethodsWithoutSignatures =
        methodCompilerPass.externMethodsWithoutSignatures;
    Set<String> nonMethodProperties = methodCompilerPass.nonMethodProperties;
    assertEquals(3, externMethods.size());
    assertEquals(0, externMethodsWithoutSignatures.size());
    assertEquals(1, nonMethodProperties.size());
    assertTrue(externMethods.contains("Foo"));
    assertTrue(externMethods.contains("Foo.getBar"));
    assertTrue(externMethods.contains("Foo.prototype.getBaz"));
    assertTrue(nonMethodProperties.contains("prototype"));
  }

  @Test
  public void testProcessExternsAndRoot() {
    String sourceName = "test.js";

    Node externs = createNode("{\n" +
        "  Foo: {\n" +
        "    getBar: function() {}\n" +
        "  },\n" +
        "  Bar: function() {}\n" +
        "}", Token.OBJECTLIT);

    Node root = createNode("function Foo() {}\n" +
        "function Foo.getBar() {}\n" +
        "function Foo.prototype.getBaz() {}", Token.PROGRAM);

    methodCompilerPass.process(externs, root);

    verify(signatureStoreMock).addSignature("Foo.getBar", anyNode(), sourceName);
    verifyZeroInteractions(compiler);

    Set<String> externMethods = methodCompilerPass.externMethods;
    Set<String> externMethodsWithoutSignatures =
        methodCompilerPass.externMethodsWithoutSignatures;
    Set<String> nonMethodProperties = methodCompilerPass.nonMethodProperties;
    assertEquals(3, externMethods.size());
    assertEquals(1, externMethodsWithoutSignatures.size());
    assertEquals(1, nonMethodProperties.size());
    assertTrue(externMethods.contains("Foo.getBar"));
    assertTrue(externMethods.contains("Foo"));
    assertTrue(externMethods.contains("Bar"));
    assertTrue(externMethodsWithoutSignatures.contains("Bar"));
    assertTrue(nonMethodProperties.contains("prototype"));
  }

  @Test
  public void testAddPossibleSignature() {
    String fnSourceName = "test.js";

    methodCompilerPass.addPossibleSignature("getBar", createNode("function getBar() {}", Token.FUNCTION), null);

    verify(signatureStoreMock).addSignature("getBar", anyNode(), fnSourceName);
  }

  @Test
  public void testAddPossibleSignatureWithParentFunction() {
    String fnSourceName = "test.js";

    Node parentFunction = createNode("function Foo() {}", Token.FUNCTION);
    methodCompilerPass.addPossibleSignature("getBar", createNode("function getBar() {}", Token.FUNCTION), parentFunction);

    verify(signatureStoreMock).addSignature("Foo.getBar", parentFunction, fnSourceName);
  }

  @Test
  public void testAddPossibleSignatureWithParentGetProp() {
    String fnSourceName = "test.js";

    Node parentGetProp = createNode(
        "function Foo() {}\n" +
        "Foo.prototype.getBar = function() {}", Token.PROGRAM);
    methodCompilerPass.addPossibleSignature("getBar", createNode("function getBar() {}", Token.FUNCTION), parentGetProp);

    verify(signatureStoreMock).addSignature("Foo.prototype.getBar", anyNode(), fnSourceName);
  }

  @Test
  public void testRemoveSignature() {
    String functionName = "getBar";
    String sourceFile = "test.js";
    Node functionNode = createNode("function getBar() {}", Token.FUNCTION);

    methodCompilerPass.addSignature(functionName, functionNode, createNode("", Token.EMPTY));
    methodCompilerPass.removeSignature(functionName);

    verify(signatureStoreMock).removeSignature(functionName);
  }

  @Test
  public void testGetMethodDefinitions() {
    String sourceName = "test.js";

    Node root = createNode("function Foo() {}\n" +
        "function Foo.getBar() {}\n" +
        "function Foo.prototype.getBaz() {}", Token.PROGRAM);

    methodCompilerPass.process(null, root);

    Multimap<String, Node> methodDefinitions = methodCompilerPass.methodDefinitions;

    Node getBar = methodDefinitions.get("Foo.getBar").get(0);
    Node getBaz = methodDefinitions.get("Foo.prototype.getBaz").get(0);

    Node expectedGetBar = createNode("function getBar() {}", Token.FUNCTION);
    Node expectedGetBaz = createNode("function Foo.prototype.getBaz() {}", Token.FUNCTION);

    assertEquals(2, methodDefinitions.size());
    assertEquals(expectedGetBar, getBar);
    assertEquals(expectedGetBaz, getBaz);
  }

  private Node createNode(String code, int nodeType) {
    RhinoCommon rhinoCommon = new RhinoCommon();
    return rhinoCommon.parseWithErrors(code, code, null).getFirstChild();
  }

  private void verifyZeroInteractions(Mockito mock) {
    Mockito.verifyNoInteractions(mock);
  }

  private void verify(SignatureStore signatureStore, int times) {
    Mockito.verify(signatureStore, times(times)).addSignature(anyString(), anyNode(), anyString());
  }

  private void verify(SignatureStore signatureStore, Node functionNode, int times) {
    Mockito.verify(signatureStore, times(times)).addSignature(anyString(), functionNode, anyString());
  }

  private void verify(SignatureStore signatureStore, String functionName, int times) {
    Mockito.verify(signatureStore, times(times)).removeSignature(functionName);
  }

  private void verifyNoMoreInteractions(SignatureStore signatureStore) {
    Mockito.verifyNoMoreInteractions(signatureStore);
  }

  private void verifyNoMoreInteractions(AbstractCompiler compiler) {
    Mockito.verifyNoMoreInteractions(compiler);
  }

  private void verifyNoMoreInteractions(SignatureStore signatureStore, AbstractCompiler compiler) {
    Mockito.verifyNoMoreInteractions(signatureStore, compiler);
  }

  private void verify(SignatureStore signatureStore, int times, String functionName) {
    Mockito.verify(signatureStore, times(times)).addSignature(functionName, anyNode(), anyString());
  }
}