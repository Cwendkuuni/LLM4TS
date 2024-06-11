import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;

public class ProcessCommonJSModulesTest {

 @Test
 public void testProcessCommonJSModules() {
 CompilerOptions options = new CompilerOptions();
 CompilerInput input = new CompilerInput("test.js", "test.js", null);
 ProcessCommonJSModules processor = new ProcessCommonJSModules(
 new Compiler(options), "");

 // Given a simple CommonJS module
 input.setContent("/** @suppress {extraRequire } */\n" +
 "require('module1');\n" +
 "function main() {\n" +
 "  console.log('Hello World');\n" +
 "}\n");
 processor.process(input, null);

 // Then the generated code should not have require calls
 Node root = input.getRootNode();
 assertFalse(root.hasChild( IR.call(IR.getprop(IR.name("require"), null), null) ));

 // And should have the correct goog.provide statement
 Node provideStatement = root.getFirstChild();
 assertEquals("goog.provide('test');", provideStatement.toString());

 // Given a CommonJS module with exports
input.setContent("/** @suppress {extraRequire } */\n" +
 "var module1 = {\n" +
 "  exports: function() {\n" +
 "    return {'foo': 'bar'};\n" +
 "  }\n" +
 "};\n" +
 "require('module1').exports;\n" +
 "function main() {\n" +
 "  console.log(foo);\n" +
 "}\n");
 processor.process(input, null);

 // Then the generated code should have the correct assignment of exports
 Node exports = root.getChildAtIndex(1);
 Node foo = exports.getChildAtIndex(0);
 assertEquals("'bar'", foo.toString());

 // Given a CommonJS module with dependencies
 input.setContent("/** @suppress {extraRequire } */\n" +
 "require('dep1');\n" +
 "require('dep2');\n" +
 "function main() {\n" +
 "  console.log('Hello World');\n" +
 "}\n");
 processor.process(input, null);

 // Then the generated code should have the correct goog.require statements
 Node requires = root.getLastChild();
 assertEquals("goog.require('dep1');\n" +
 "goog.require('dep2');\n", requires.toString());
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidModuleName() {
 CompilerOptions options = new CompilerOptions();
 CompilerInput input = new CompilerInput("test.js", "test.js", null);
 ProcessCommonJSModules processor = new ProcessCommonJSModules(
 new Compiler(options), "");

 input.setContent("/** @suppress {extraRequire } */\n" +
 "require('module'");
 processor.process(input, null);
 }

 @Test
 public void testRelativeModuleRequire() {
 CompilerOptions options = new CompilerOptions();
 CompilerInput input = new CompilerInput("test.js", "test.js", null);
 ProcessCommonJSModules processor = new ProcessCommonJSModules(
 new Compiler(options), "");

 input.setContent("/** @suppress {extraRequire } */\n" +
 "require('./module1');\n" +
 "function main() {\n" +
 "  console.log('Hello World');\n" +
 "}\n");
 processor.process(input, null);

 Node requireCall = root.getFirstChild();
 String moduleName = requireCall.getChildAtIndex(1).getString();
 String expectedModuleName = "test" + MODULE_SLASH + "module1";
 assertEquals(expectedModuleName, moduleName);
 }
}