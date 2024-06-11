import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeFactory;

public class ProcessCommonJSModulesTest {

 private Compiler compiler;
 private NodeFactory nodeFactory;
 private CompilerOptions options;

 @Before
 public void setup() {
 compiler = new Compiler();
 options = new CompilerOptions();
 nodeFactory = compiler.getFactory();
 }

 @Test
 public void testSimpleCommonJSModule() {
 String sourceCode = "var m = require('module1');";
 String expected = "var m = module1;";

 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
 Node root = parser.parse(nodeFactory, sourceCode, "test.js", 0);
 processor.process(nodeFactory.Null(), root);

 assertEquals(expected, root.toString());
 }

 @Test
 public void testCommonJSModuleWithDependencies() {
 String sourceCode = "var m1 = require('module1');\n" +
 "var m2 = require('module2');\n" +
 "module.exports = function() {\n" +
 "  return m1.foo() + m2.bar();\n" +
 "};";
 String expected = "var m1 = module1;\n" +
 "var m2 = module2;\n" +
 "function() {\n" +
 "  return m1.foo() + m2.bar();\n" +
 "}";

 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
 Node root = parser.parse(nodeFactory, sourceCode, "test.js", 0);
 processor.process(nodeFactory.Null(), root);

 assertEquals(expected, root.toString());
 }

 @Test
 public void testCommonJSModuleWithModuleExports() {
 String sourceCode = "var m = require('module');\n" +
 "module.exports = m;\n";
 String expected = "var m = module;\n" +
 "module.exports = module;\n";

 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
 Node root = parser.parse(nodeFactory, sourceCode, "test.js", 0);
 processor.process(nodeFactory.Null(), root);

 assertEquals(expected, root.toString());
 }

 @Test
 public void testCommonJSModuleWithProvide() {
 String sourceCode = "var m = require('module');\n" +
 "goog.provide('testModule');\n" +
 "testModule.foo = function() {\n" +
 "  return 'test';\n" +
 "};\n" +
 "module.exports = m;\n";
 String expected = "var m = module;\n" +
 "goog.provide('testModule');\n" +
 "testModule.foo = function() {\n" +
 "  return 'test';\n" +
 "};\n" +
 "module.exports = module;\n";

 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", true);
 Node root = parser.parse(nodeFactory, sourceCode, "test.js", 0);
 processor.process(nodeFactory.Null(), root);

 assertEquals(expected, root.toString());
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidURI() {
 String sourceCode = "var m = require('invalid:/URI');\n";

 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
 Node root = parser.parse(nodeFactory, sourceCode, "test.js", 0);
 processor.process(nodeFactory.Null(), root);
 }

 @Test(expected = RuntimeException.class)
 public void testMissingRequireCall() {
 String sourceCode = "var m = module;\n";

 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
 Node root = parser.parse(nodeFactory, sourceCode, "test.js", 0);
 processor.process(nodeFactory.Null(), root);
 }

 @Test(expected = RuntimeException.class)
 public void testMissingModuleName() {
 String sourceCode = "var m = require();\n";

 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
 Node root = parser.parse(nodeFactory, sourceCode, "test.js", 0);
 processor.process(nodeFactory.Null(), root);
 }

 @Test
 public void testEmptyFile() {
 String sourceCode = "";
 String expected = "";

 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
 Node root = parser.parse(nodeFactory, sourceCode, "test.js", 0);
 processor.process(nodeFactory.Null(), root);

 assertEquals(expected, root.toString());
 }
}