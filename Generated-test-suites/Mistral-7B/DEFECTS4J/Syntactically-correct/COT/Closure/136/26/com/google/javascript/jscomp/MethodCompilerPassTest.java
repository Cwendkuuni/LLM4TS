import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.CallbackType.SKIP;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.test.TestSourceFile;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MethodCompilerPassTest {
  

  private Compiler compiler;
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @Before
  public void setUp() {
    compiler = new Compiler();
    methodCompilerPass = new MethodCompilerPass(compiler);
    signatureStore = new SignatureStore() {
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

      public Map<String, Node> getSignatures() {
        return signatures;
      }
    };
    methodCompilerPass.setSignatureStore(signatureStore);
  }

  /**
   * Test for the basic functionality of the process method.
   */
  @Test
  public void testBasicFunctionality() {
    Node externs = TestSourceFile.getExternsNode("testExterns.js");
    Node root = TestSourceFile.getRootNode("testSource.js");

    methodCompilerPass.process(externs, root);

    List<Map.Entry<String, Node>> signatures = signatureStore.getSignatures().entrySet();
    assertEquals(2, signatures.size());

    Map.Entry<String, Node> firstEntry = signatures.get(0);
    assertEquals("foo", firstEntry.getKey());
    assertEquals(
        "function foo(x) {...}",
        TestSourceFile.getNodeAsString(firstEntry.getValue()));

    Map.Entry<String, Node> secondEntry = signatures.get(1);
    assertEquals("bar", secondEntry.getKey());
    assertEquals(
        "function bar() {...}",
        TestSourceFile.getNodeAsString(secondEntry.getValue()));
  }

  /**
   * Test for the handling of externs with no signatures.
   */
  @Test
  public void testHandlingOfExternsWithoutSignatures() {
    Node externs = TestSourceFile.getExternsNode("testExternsWithNoSignatures.js");
    Node root = TestSourceFile.getRootNode("testSource.js");

    methodCompilerPass.process(externs, root);

    List<String> externMethods = methodCompilerPass.externMethods;
    assertEquals(1, externMethods.size());
    assertEquals("qux", externMethods.get(0));

    Set<String> nonMethodProperties = methodCompilerPass.nonMethodProperties;
    assertEquals(1, nonMethodProperties.size());
    assertEquals("quux", nonMethodProperties.iterator().next());
  }

  /**
   * Test for handling method definitions with exceptions.
   */
  @Test(expected = IllegalStateException.class)
  public void testHandlingOfMethodDefinitionsWithExceptions() {
    Node externs = TestSourceFile.getExternsNode("testExternsWithExceptions.js");
    Node root = TestSourceFile.getRootNode("testSource.js");

    methodCompilerPass.process(externs, root);
  }

  /**
   * Test for handling method definitions without functions.
   */
  @Test
  public void testHandlingOfMethodDefinitionsWithoutFunctions() {
    Node externs = TestSourceFile.getExternsNode("testExternsWithNoFunctions.js");
    Node root = TestSourceFile.getRootNode("testSource.js");

    methodCompilerPass.process(externs, root);

    List<String> externMethods = methodCompilerPass.externMethods;
    assertEquals(1, externMethods.size());
    assertEquals("quux", externMethods.get(0));

    Set<String> nonMethodProperties = methodCompilerPass.nonMethodProperties;
    assertEquals(1, nonMethodProperties.size());
    assertEquals("quux", nonMethodProperties.iterator().next());

    List<String> methodDefinitions = Lists.newArrayList(
        methodCompilerPass.methodDefinitions.get(Sets.newHashSet("quux")));
    assertEquals(0, methodDefinitions.size());
  }

  /**
   * Test for handling multiple method definitions with the same name.
   */
  @Test
  public void testHandlingOfMultipleMethodDefinitionsWithTheSameName() {
    Node externs = TestSourceFile.getExternsNode("testExternsWithMultipleDefinitions.js");
    Node root = TestSourceFile.getRootNode("testSource.js");

    methodCompilerPass.process(externs, root);

    List<Map.Entry<String, Node>> signatures = signatureStore.getSignatures().entrySet();
    assertEquals(2, signatures.size());

    Map.Entry<String, Node> firstEntry = signatures.get(0);
    assertEquals("qux", firstEntry.getKey());
    assertEquals(
        "function qux() {...}",
        TestSourceFile.getNodeAsString(firstEntry.getValue()));

    Map.Entry<String, Node> secondEntry = signatures.get(1);
    assertEquals("qux", secondEntry.getKey());
    assertEquals(
        "function qux(x) {...}",
        TestSourceFile.getNodeAsString(secondEntry.getValue()));
  }

  
}