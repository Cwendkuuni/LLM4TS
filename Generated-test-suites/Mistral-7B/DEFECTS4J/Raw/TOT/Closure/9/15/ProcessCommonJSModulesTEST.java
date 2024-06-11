import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceName;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws Exception {
    String input = "// CommonJS module\n" +
        "var exports = {\n" +
        "  foo: function() {\n" +
        "    return 'common';\n" +
        "  }\n" +
        "};\n" +
        "module.exports = {\n" +
        "  bar: function() {\n" +
        "    return 'common-module';\n" +
        "  }\n" +
        "};\n";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    Node root = new NodeFactory().createScriptNode(input, SourceName.get("test.js"));

    CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler, "");
    processCommonJSModules.process(null, root);

    Node output = root.getFirstChild();

    Node expectedOutput = new NodeFactory().createScriptNode(
        "goog.provide('test.common');\n" +
        "test.common.foo = function() {\n" +
        "  return 'common';\n" +
        "};\n" +
        "goog.provide('test.common-module');\n" +
        "test.common-module.bar = function() {\n" +
        "  return 'common-module';\n" +
        "};\n",
        SourceName.get("test.js")
    );

    NodeTraversal traversal = new NodeTraversal(compiler, output, null);
    Node actualOutput = traversal.getRoot();

    assertEquals(expectedOutput.getTreeValue(), actualOutput.getTreeValue());
    assertNotNull(compiler.getDiagnostics().getErrors().isEmpty());
  }
}