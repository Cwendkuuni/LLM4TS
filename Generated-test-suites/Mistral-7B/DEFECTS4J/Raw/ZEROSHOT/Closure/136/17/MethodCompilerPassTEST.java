import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Annotations;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Map;
import java.util.Set;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
public class MethodCompilerPassTest {

  private AbstractCompiler compiler;
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @Before
  public void setUp() {
    Annotations.initMocks(this);
    CompilerOptions options = new CompilerOptions();
    compiler = Mockito.spy(new AbstractCompiler() {});
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      abstract Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }

      @Override
      abstract SignatureStore getSignatureStore() {
        return signatureStore;
      }
    };
    signatureStore = Mockito.mock(SignatureStore.class);
  }

  @After
  public void tearDown() {
    PowerMockito.resetStatic();
  }

  @Test
  public void testExternMethodsAreCollected() {
    String externsSource = "Foo.bar = function() {}; Foo.baz = function(x) {};";
    Node externs = PowerMockito.mock(Node.class);
    PowerMockito.when(externs.getText()).thenReturn(externsSource);

    methodCompilerPass.process(externs, null);

    Set<String> collectedExternMethods = methodCompilerPass.externMethods;
    Set<String> expectedExternMethods = Sets.newHashSet("bar", "baz");
    assertEquals(expectedExternMethods, collectedExternMethods);
  }

  @Test
  public void testNonMethodPropertiesAreCollected() {
    String source = "Foo.property = 42;";
    Node root = PowerMockito.mock(Node.class);
    PowerMockito.when(root.getText()).thenReturn(source);

    methodCompilerPass.process(null, root);

    Set<String> collectedNonMethodProperties = methodCompilerPass.nonMethodProperties;
    Set<String> expectedNonMethodProperties = Sets.newHashSet("property");
    assertEquals(expectedNonMethodProperties, collectedNonMethodProperties);
  }

  @Test
  public void testMethodsWithSignaturesAreStored() {
    String source = "Foo.bar = function(x, y) {};";
    Node root = PowerMockito.mock(Node.class);
    PowerMockito.when(root.getText()).thenReturn(source);

    methodCompilerPass.process(null, root);

    SignatureStore signatureStore = methodCompilerPass.getSignatureStore();
    Map<String, Node> expectedMethodDefinitions = Map.of(
        "bar", Mockito.mock(Node.class)
    );
    Multimap<String, Node> actualMethodDefinitions = methodCompilerPass.methodDefinitions;
    assertEquals(expectedMethodDefinitions, actualMethodDefinitions.asMap());
  }

  @Test
  public void testMethodsWithoutSignaturesAreIgnored() {
    String source = "Foo.bar = function() {};";
    Node root = PowerMockito.mock(Node.class);
    PowerMockito.when(root.getText()).thenReturn(source);

    methodCompilerPass.process(null, root);

    Set<String> collectedExternMethodsWithoutSignatures = methodCompilerPass.externMethodsWithoutSignatures;
    Set<String> expectedExternMethodsWithoutSignatures = Sets.newHashSet("bar");
    assertEquals(expectedExternMethodsWithoutSignatures, collectedExternMethodsWithoutSignatures);
  }

  @Test
  public void testStaticMethodsAreCollected() {
    String source = "Foo.bar = function() {};";
    Node root = PowerMockito.mock(Node.class);
    PowerMockito.when(root.getText()).thenReturn(source);

    methodCompilerPass.process(null, root);

    Set<String> collectedExternMethods = methodCompilerPass.externMethods;
    Set<String> expectedExternMethods = Sets.newHashSet("bar");
    assertEquals(expectedExternMethods, collectedExternMethods);
  }

  @Test
  public void testPrototypeMethodsAreCollected() {
    String source = "Foo.prototype.bar = function() {};";
    Node root = PowerMockito.mock(Node.class);
    PowerMockito.when(root.getText()).thenReturn(source);

    methodCompilerPass.process(null, root);

    Set<String> collectedExternMethods = methodCompilerPass.externMethods;
    Set<String> expectedExternMethods = Sets.newHashSet("bar");
    assertEquals(expectedExternMethods, collectedExternMethods);
  }

  @Test
  public void testPrototypeMethodsAreProcessedCorrectly() {
    String source = "Foo.prototype = { getBar: function() {} };";
    Node root = PowerMockito.mock(Node.class);
    PowerMockito.when(root.getText()).thenReturn(source);

    methodCompilerPass.process(null, root);

    SignatureStore signatureStore = methodCompilerPass.getSignatureStore();
    Multimap<String, Node> actualMethodDefinitions = methodCompilerPass.methodDefinitions;
    Node expectedMethodDefinition = Mockito.mock(Node.class);
    PowerMockito.when(expectedMethodDefinition.getText()).thenReturn("function() {}");
    PowerMockito.when(signatureStore.get(anyString())).thenReturn(expectedMethodDefinition);
    assertEquals(1, actualMethodDefinitions.size());
    Node actualMethodDefinition = actualMethodDefinitions.get("getBar").get(0);
    assertSame(expectedMethodDefinition, actualMethodDefinition);
  }
}