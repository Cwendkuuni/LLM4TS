import static org.junit.Assert.*;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

  @Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "");
    String filename = "./test.js";
    String expected = "module$test";
    String actual = pcm.guessCJSModuleName(filename);
    assertEquals(expected, actual);
  }

  @Test
  public void testToModuleName() {
    String filename = "test.js";
    String expected = "module$test";
    String actual = ProcessCommonJSModules.toModuleName(filename);
    assertEquals(expected, actual);
  }

  @Test
  public void testToModuleNameWithCurrentFilename() {
    String requiredFilename = "./sub/test.js";
    String currentFilename = "main.js";
    String expected = "module$sub$test";
    String actual = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals(expected, actual);
  }

  @Test
  public void testProcessNullCompiler() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "");
    assertThrows(NullPointerException.class, () -> {
      pcm.process(null, null);
    });
  }

  @Test
  public void testProcessNonNullCompiler() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(new AbstractCompiler(), "");
    Node externs = new Node(Token.EXPR_RESULT);
    Node root = new Node(Token.SCRIPT);
    assertDoesNotThrow(() -> {
      pcm.process(externs, root);
    });
  }

  @Test
  public void testGetCurrentScriptNode() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "");
    Node script = new Node(Token.SCRIPT);
    Node parent = new Node(Token.EXPR_RESULT, script);
    Node result = pcm.getCurrentScriptNode(parent);
    assertEquals(script, result);
  }

  @Test
  public void testSuffixVarsCallback() {
    ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "");
    Node node = new Node(Token.NAME, "varName");
    NodeTraversal traversal = new NodeTraversal(pcm.compiler, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        // do nothing
      }
    });
    pcm.new SuffixVarsCallback("suffix").visit(traversal, node, null);
    assertEquals("varName$$suffix", node.getString());
  }
}