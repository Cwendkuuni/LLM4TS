import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeTree.*;

import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws Exception {
    String input = "var a = require('./a.js');\n" +
        "exports.b = function() {\n" +
        "  console.log(a);\n" +
        "};\n" +
        "require('./b.js');\n" +
        "exports.c = function() {\n" +
        "  console.log('c');\n" +
        "};";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    SourceFile source = Parser.parseJavaScriptSource(input, "test.js");
    Node root = source.getTree();

    CompilerPass pass = new ProcessCommonJSModules(compiler, "", false);
    pass.process(null, root);

    NodeTraversal traversal = NodeTraversal.create(compiler, root, null);
    traversal.traverse();

    Node expectedOutput = NodeFactory.createScript(
        "goog.provide('test');\n" +
        "test.exports = function() {\n" +
        "  var a$a = require('./a.js');\n" +
        "  this.b = function() {\n" +
        "    console.log(a$a);\n" +
        "  };\n" +
        "  require('./b.js');\n" +
        "  this.c = function() {\n" +
        "    console.log('c');\n" +
        "  };\n" +
        "};\n" +
        "goog.require('test.exports');");

    Node actualOutput = NodeTraversal.getChildAtIndex(root, 0);
    assertEquals(expectedOutput.toString(), actualOutput.toString());

    // Check if 'require' calls have been replaced with direct references.
    Set<String> requiredModules = Sets.newHashSet();
    Node currentNode = NodeTraversal.getChildAtIndex(root, 1);
    Node processCommonJsModulesCallback = NodeTraversal.getChildAtIndex(currentNode, 0);
    Node processCommonJsModules = NodeTraversal.getChildAtIndex(processCommonJsModulesCallback, 0);

    NodeTraversal requireTraversal = NodeTraversal.create(compiler, processCommonJsModules, null);
    requireTraversal.traverse(new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && n.getChildCount() == 2 &&
            "require".equals(n.getFirstChild().getQualifiedName())) {
          String requiredModule = toModuleName(n.getChildAtIndex(1).getString(),
              normalizeSourceName(t.getSourceName()));
          requiredModules.add(requiredModule);
        }
      }
    });

    assertEquals(Sets.newHashSet("a.js", "b.js"), requiredModules);
  }

  private String normalizeSourceName(String filename) {
    if (filename.indexOf("") == 0) {
      filename = filename.substring(filename.indexOf("") + filename.length());
    }
    return filename;
  }

  private String toModuleName(String requiredFilename, String currentFilename) {
    requiredFilename = requiredFilename.replaceAll("\\.js$", "");
    currentFilename = currentFilename.replaceAll("\\.js$", "");

    if (requiredFilename.startsWith("." + File.separator) ||
        requiredFilename.startsWith(".." + File.separator)) {
      try {
        requiredFilename = (new URI(currentFilename)).resolve(new URI(requiredFilename))
            .toString();
      } catch (URISyntaxException e) {
        throw new RuntimeException(e);
      }
    }
    return MODULE_NAME_PREFIX +
        requiredFilename.replaceAll("^\\." + Pattern.quote(File.separator), "")
            .replaceAll(Pattern.quote(File.separator), MODULE_NAME_SEPARATOR)
            .replaceAll("\\.js$", "").replaceAll("-", "_");
  }

  private static final String MODULE_NAME_SEPARATOR = "$";
  private static final String MODULE_NAME_PREFIX = "module$";
}