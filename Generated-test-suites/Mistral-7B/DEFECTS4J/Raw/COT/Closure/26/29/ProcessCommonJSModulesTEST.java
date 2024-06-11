import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import java.io.File;
import java.net.URI;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  @SuppressWarnings("unchecked")
  private static CompilerInput createInput(String source) {
    CompilerPass processor = new ProcessCommonJSModules(new NoOpCompiler(), "");
    CompilerInput input = new CompilerInput(new File("test.js"), source);
    processor.process(input.getExterns(), input.getRootNode());
    return input;
  }

  @Test
  public void testGuessCJSModuleName() {
    String filename = "src/modules/module1.js";
    String expected = "module1";
    assertEquals(expected, guessCJSModuleName(filename));
  }

  @Test
  public void testToModuleName() {
    String expected = "module$module1";
    assertEquals(expected, toModuleName("module1"));

    expected = "module$src.modules.module1";
    assertEquals(expected, toModuleName("src/modules/module1"));

    expected = "module$./module2";
    assertEquals(expected, toModuleName("./module2"));

    expected = "module$../module3";
    assertEquals(expected, toModuleName("../module3"));
  }

  @Test
  public void testNormalizeSourceName() {
    String filename = "src/modules/module1.js";
    String expected = "modules/module1";
    assertEquals(expected, normalizeSourceName(filename));
  }

  @Test
  public void testProcessCommonJsModulesCallbackVisitRequireCall() {
    String requiredFilename = "modules/module1.js";
    String currentFilename = "test.js";
    String expectedModuleName = "module1";
    String expectedModuleRef = "module$$module1";

    CompilerInput input = createInput(
        "require('" + requiredFilename + "');" +
        "function test() {" +
        "  // test code" +
        "}");

    NodeTraversal traversal = new NodeTraversal(new NoOpCompiler(), input.getRootNode());
    NodeTraversal.traverse(new ProcessCommonJSModules(new NoOpCompiler(), "", true), input.getRootNode(), new ProcessCommonJsModulesCallback() {
      @Override
      public void visitRequireCall(NodeTraversal t, Node require, Node parent) {
        assertEquals(expectedModuleName, toModuleName(require.getChildAtIndex(1).getString(), currentFilename));
        assertEquals(expectedModuleRef, require.getChildAtIndex(1).getString());
        assertEquals(expectedModuleName, parent.getChildAtIndex(0).getString());
      }
    });
  }

  @Test
  public void testProcessCommonJsModulesCallbackVisitScript() {
    String filename = "src/modules/module1.js";
    String expectedModuleName = "module1";

    CompilerInput input = createInput(
        "// " + filename + "\n" +
        "goog.provide('" + expectedModuleName + "');\n" +
        "// module code");

    assertEquals(expectedModuleName, guessCJSModuleName(filename));
    JSModule module = ((ProcessCommonJSModules) input.getPasses().get(0)).getModule();
    assertEquals(expectedModuleName, module.getName());

    // Check that the module is added to the input
    Set<String> requires = input.getRequireMap().keySet();
    assertTrue(requires.contains(expectedModuleName));
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJsModulesCallbackVisitRequireCallWithInvalidURI() {
    String requiredFilename = "modules/module1.js";
    String currentFilename = "test.js";
    String filename = "test.js";

    CompilerInput input = createInput(
        "require('" + requiredFilename + "');" +
        "function test() {" +
        "  // test code" +
        "}");

    NodeTraversal traversal = new NodeTraversal(new NoOpCompiler(), input.getRootNode());
    new ProcessCommonJsModulesCallback() {
      @Override
      public void visitRequireCall(NodeTraversal t, Node require, Node parent) {
        try {
          String moduleName = toModuleName(require.getChildAtIndex(1).getString(), currentFilename);
          fail("Expected URISyntaxException");
        } catch (URISyntaxException e) {
          // Expected
        }
      }
    }.visitRequireCall(traversal, require, parent);
  }

  private static class NoOpCompiler implements AbstractCompiler {

    @Override
    public void process(CompilerInput input) {
    }
  }
}