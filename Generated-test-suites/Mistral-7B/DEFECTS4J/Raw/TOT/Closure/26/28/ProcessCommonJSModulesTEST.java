import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, "", false);

    String inputCode = "var module = {\n" +
        "  exports: {\n" +
        "    foo: function() {\n" +
        "      console.log('Hello from module A!');\n" +
        "    }\n" +
        "  },\n" +
        "  require: function(moduleName) {\n" +
        "    if (moduleName === 'moduleB') {\n" +
        "      return {\n" +
        "        exports: {\n" +
        "          bar: function() {\n" +
        "            console.log('Hello from module B!');\n" +
        "          }\n" +
        "        }\n" +
        "      };\n" +
        "    } else {\n" +
        "      throw new Error('Module not found: ' + moduleName);\n" +
        "    }\n" +
        "  }\n" +
        "};\n" +
        "goog.provide('moduleA');\n" +
        "goog.require('moduleB');\n" +
        "moduleA.exports.foo();\n";

    Node input = NodeFactory.parseTree(inputCode);
    Node root = NodeFactory.createRootNode(input);
    p.process(null, root);

    NodeTraversal nt = new NodeTraversal(compiler, root);
    Node module = nt.getModule();
    assertSame(inputCode.substring(0, inputCode.lastIndexOf("goog.provide('moduleA')")),
        nt.getSourceText());

    Node moduleA = nt.getChildByName("moduleA", module.getFirstChild());
    Node foo = moduleA.getFirstChild().getFirstChild();
    Node requireCall = foo.getChildAtIndex(1);
    Node moduleB = requireCall.getChildAtIndex(1);
    Node bar = moduleB.getFirstChild().getFirstChild();

    assertEquals("Hello from module B!", bar.getCallExpression().getFunction().getBody()
        .getLastChild().getString());
    assertNull(requireCall.getChildAtIndex(0).getQualifiedName());
    assertEquals("moduleB", requireCall.getChildAtIndex(1).getString());

    Node exports = module.getFirstChild();
    Node exportsModuleA = exports.getChildByName("moduleA");
    Node fooModuleA = exportsModuleA.getFirstChild();
    Node exportsModuleB = exports.getChildByName("moduleB");
    Node barModuleB = exportsModuleB.getFirstChild();

    assertEquals("moduleB", exportsModuleB.getChildAtIndex(0).getString());
    assertEquals("exports", exportsModuleB.getChildAtIndex(1).getString());
    assertEquals("bar", exportsModuleB.getChildAtIndex(2).getQualifiedName());

    assertEquals("moduleA", exportsModuleA.getChildAtIndex(0).getString());
    assertEquals("exports", exportsModuleA.getChildAtIndex(1).getString());
    assertEquals("foo", exportsModuleA.getChildAtIndex(2).getQualifiedName());

    assertSame(foo, fooModuleA);
    assertSame(bar, barModuleB);
  }
}