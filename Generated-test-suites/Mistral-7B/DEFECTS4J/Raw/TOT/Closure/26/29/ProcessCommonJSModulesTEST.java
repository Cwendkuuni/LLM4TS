import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceName;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    String inputCode = "// Input code.\n" +
        "function main() {\n" +
        "  require('module1');\n" +
        "  require('module2');\n" +
        "}\n" +
        "\n" +
        "// module1.js\n" +
        "var module1 = {\n" +
        "  exports: function () {\n" +
        "    return 'Hello from module1';\n" +
        "  }\n" +
        "};\n";

    String expectedOutputCode = "// Input code.\n" +
        "// (function () {\n" +
        "//   'use strict';\n" +
        "//   goog.require('module1');\n" +
        "//   goog.require('module2');\n" +
        "//   var module1 = module1$module$exports;\n" +
        "//   var module2 = module2$module$exports;\n" +
        "//   function main() {\n" +
        "//     goog.provide('main');\n" +
        "//     main.module$exports = function () {\n" +
        "//       return module1.exports() + ' ' + module2.exports();\n" +
        "//     };\n" +
        "//   }\n" +
        "// })();\n" +
        "\n" +
        "// module1.js -> module1$module$exports.js\n" +
        "var module1$module$exports = {\n" +
        "  exports: function () {\n" +
        "    return 'Hello from module1';\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "// module2.js -> module2$module$exports.js\n" +
        "var module2$module$exports = {\n" +
        "};\n";

    Compiler compiler = new Compiler();
    compiler.addPass(new ProcessCommonJSModules(compiler, "."));
    Node root = NodeFactory.parseTree(inputCode, new SourceName("test.js"));
    compiler.process(null, root);
    assertEquals(expectedOutputCode, compiler.getProgramText());
  }

  @Test
  public void testProcessCommonJSModulesWithDependenciesReporting() {
    String inputCode = "// Input code.\n" +
        "function main() {\n" +
        "  require('module1');\n" +
        "  require('module2');\n" +
        "}\n" +
        "\n" +
        "// module1.js\n" +
        "var module1 = {\n" +
        "  exports: function () {\n" +
        "    return 'Hello from module1';\n" +
        "  }\n" +
        "};\n";

    String expectedOutputCode = "// Input code.\n" +
    "// (function () {\n" +
    "//   'use strict';\n" +
    "//   goog.require('module1');\n" +
    "//   goog.require('module2');\n" +
    "//   var module1 = module1$module$exports;\n" +
    "//   var module2 = module2$module$exports;\n" +
    "//   function main() {\n" +
    "//     goog.provide('main');\n" +
    "//     main.module$exports = function () {\n" +
    "//       return module1.exports() + ' ' + module2.exports();\n" +
    "//     };\n" +
    "//   }\n" +
    "// })();\n" +
    "\n" +
    "// module1.js -> module1$module$exports.js\n" +
    "var module1$module$exports = {\n" +
    "  exports: function () {\n" +
    "    return 'Hello from module1';\n" +
    "  }\n" +
    "};\n" +
    "\n" +
    "// module2.js -> module2$module$exports.js\n" +
    "var module2$module$exports = {\n" +
    "};\n" +
    "\n" +
    "// Output: ['module1', 'module2']\n";

    Compiler compiler = new Compiler();
    compiler.addPass(new ProcessCommonJSModules(compiler, ".", true));
    Node root = NodeFactory.parseTree(inputCode, new SourceName("test.js"));
    compiler.process(null, root);
    assertEquals(expectedOutputCode.split("\n"), compiler.getProgramText().split("\n"));
    assertEquals(Sets.newHashSet("module1", "module2"), compiler.getInput()
        .getRequires());
  }

  @Test
  public void testProcessCommonJSModulesWithRelativeRequire() {
    String inputCode = "// Input code.\n" +
        "function main() {\n" +
        "  require('./module1');\n" +
        "  require('module2');\n" +
        "}\n" +
        "\n" +
        "// module1.js\n" +
        "var module1 = {\n" +
        "  exports: function () {\n" +
        "    return 'Hello from module1';\n" +
        "  }\n" +
        "};\n";

    String currentFileName = "test.js";
    String requiredFileName = "module1.js";
    String expectedOutputCode = "// Input code.\n" +
        "// (function () {\n" +
        "//   'use strict';\n" +
        "//   goog.require('./module1');\n" +
        "//   goog.require('module2');\n" +
        "//   var module1 = module1$module$exports;\n" +
        "//   var module2 = module2$module$exports;\n" +
        "//   function main() {\n" +
        "//     goog.provide('main');\n" +
        "//     main.module$exports = function () {\n" +
        "//       return module1.exports() + ' ' + module2.exports();\n" +
        "//     };\n" +
        "//   }\n" +
        "// })();\n" +
        "\n" +
        "// module1.js -> module1$module$exports.js\n" +
        "var module1$module$exports = {\n" +
        "  exports: function () {\n" +
        "    return 'Hello from module1';\n" +
        "  }\n" +
        "};\n";

    Compiler compiler = new Compiler();
    compiler.addPass(new ProcessCommonJSModules(compiler, ".", true));
    Node root = NodeFactory.parseTree(inputCode, new SourceName(currentFileName));
    compiler.process(null, root);
    assertEquals(expectedOutputCode.split("\n"), compiler.getProgramText().split("\n"));
  }

  @Test
  public void testProcessCommonJSModulesWithModuleExports() {
    String inputCode = "// Input code.\n" +
        "function main() {\n" +
        "  require('module1').exports.func();\n" +
        "}\n" +
        "\n" +
        "// module1.js\n" +
        "module.exports = {\n" +
        "  func: function () {\n" +
        "    console.log('Hello from module1');\n" +
        "  }\n" +
        "};\n";

    String expectedOutputCode = "// Input code.\n" +
        "// (function () {\n" +
        "//   'use strict';\n" +
        "//   goog.require('module1');\n" +
        "//   var module1 = module1$module$exports;\n" +
        "//   function main() {\n" +
        "//     goog.provide('main');\n" +
        "//     main.module$exports = function () {\n" +
        "//       module1.func();\n" +
        "//     };\n" +
        "//   }\n" +
        "// })();\n" +
        "\n" +
        "// module1.js -> module1$module$exports.js\n" +
        "var module1$module$exports = {\n" +
        "  func: function () {\n" +
        "    console.log('Hello from module1');\n" +
        "  }\n" +
        "};\n";

    Compiler compiler = new Compiler();
    compiler.addPass(new ProcessCommonJSModules(compiler, ".", true));
    Node root = NodeFactory.parseTree(inputCode, new SourceName("test.js"));
    compiler.process(null, root);
    assertEquals(expectedOutputCode.split("\n"), compiler.getProgramText().split("\n"));
  }

  @Test
  public void testProcessCommonJSModulesWithCircularDep() {
    String inputCode = "// Input code.\n" +
        "function main() {\n" +
        "  require('module1');\n" +
        "  require('module2');\n" +
        "}\n" +
        "\n" +
        "// module1.js\n" +
        "var module1 = {\n" +
        "  exports: function () {\n" +
        "    require('./module2');\n" +
        "    return 'Hello from module1';\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "// module2.js\n" +
        "var module2 = {\n" +
        "  exports: function () {\n" +
        "    require('./module1');\n" +
        "    return 'Hello from module2';\n" +
        "  }\n" +
        "};\n";

    String expectedErrorMessage = "Circular dependency between 'module1' and 'module2'.";

    Compiler compiler = new Compiler();
    compiler.addPass(new ProcessCommonJSModules(compiler, ".", true));
    Node root = NodeFactory.parseTree(inputCode, new SourceName("test.js"));
    try {
      compiler.process(null, root);
      fail("Expected an error for circular dependencies.");
    } catch (AssertionError e) {
      assertEquals(expectedErrorMessage, e.getMessage());
    }
  }
}