import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.ProcessCommonJSModules.JSModule;

public class ProcessCommonJSModulesTest {

 @Test
 public void testProcessCommonJSModules() {
 CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
 CompilerInput input = new CompilerInput(
 "input.js",
 "function main() {\n" +
 "require('module1');\n" +
 "require('module2');\n" +
 "}\n" +
 "// module1.js\n" +
 "function module1() {\n" +
 "  exports.hello = 'hello';\n" +
 "  exports.world = 'world';\n" +
 "}\n" +
 "// module2.js\n" +
 "function module2() {\n" +
 "  exports.greeting = function() {\n" +
 "    return 'Hello, ' + exports.hello + '!';\n" +
 "  };\n" +
 "}\n"
 );
 ProcessCommonJSModules processor = new ProcessCommonJSModules(
 new AbstractCompiler() {},
 "."
 );
 processor.process(input, null);

 JSModule module1 = processor.getModule();
 JSModule module2 = module1.getModule("module2");
 Node output = module1.getCode();

 // Check that require calls have been replaced with direct references
 assertNotNull(output.getFirstChild());
 Node main = output.getFirstChild();
 Node requireCall1 = main.getChildAtIndex(1);
 assertFalse(requireCall1.isCall());
 assertEquals("module1", requireCall1.getString());
 Node requireCall2 = main.getChildAtIndex(2);
 assertFalse(requireCall2.isCall());
 assertEquals("module2", requireCall2.getString());

 // Check that goog.provide and goog.require have been emitted
 Node provide = null;
 Node require = null;
 for (Node child : output) {
 if (child.isCall() && child.getChildCount() == 2 &&
 child.getChildAtIndex(0).isName() &&
 child.getChildAtIndex(0).getString().equals("goog.provide")) {
 provide = child;
 break;
 }
 if (child.isCall() && child.getChildCount() == 2 &&
 child.getChildAtIndex(0).isName() &&
 child.getChildAtIndex(0).getString().equals("goog.require")) {
 require = child;
 break;
 }
 }
 assertNotNull(provide);
 assertNotNull(require);
 assertEquals(2, require.getChildAtIndex(1).getChildCount());
 assertTrue(require.getChildAtIndex(1).isArray());
 assertTrue(require.getChildAtIndex(1).getChild(0).isString());
 assertTrue(require.getChildAtIndex(1).getChild(1).isString());

 // Check that module exports have been renamed to moduleName.module$exports
 assertNotNull(module1.getModule("module1"));
 assertNotNull(module2.getModule("module1"));
 Node module1Exports = module1.getModule("module1").getExport();
 Node module2Exports = module2.getExport();
 assertEquals("hello", module1Exports.getChildAtIndex(0).getString());
 assertEquals("world", module1Exports.getChildAtIndex(1).getString());
 assertEquals("function() {\n" +
 "  return 'Hello, " + module1.getName() + "!';\n" +
 "}", module2Exports.toString());

 // Check that global variables have been suffixed with the module name
 Node globalVariable1 = output.getFirstChild().getChildAtIndex(0);
 assertEquals("main$$module1", globalVariable1.getString());
 Node globalVariable2 = output.getFirstChild().getChildAtIndex(1);
 assertEquals("require$$module1", globalVariable2.getString());
 Node globalVariable3 = output.getFirstChild().getChildAtIndex(2);
 assertEquals("module1$$module2", globalVariable3.getString());

 // Check that require calls in module2 work
 assertEquals("Hello, module1", module2.getFunction("greeting").call(null));
 }
}