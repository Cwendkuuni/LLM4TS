import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.NodeFactory;

public class ProcessCommonJSModulesTest {

 private AbstractCompiler compiler;
 private CompilerOptions options;
 private NodeFactory nodeFactory;

 @BeforeClass
 public static void setupClass() {
 // Initialize JsCompiler
 CompilerOptions options = new CompilerOptions();
 Compiler compiler = new Compiler(options);
 }

 @Before
 public void setUp() {
 compiler = new Compiler(options);
 nodeFactory = compiler.getFactory();
 }

 @After
 public void tearDown() {
 compiler = null; // Allow garbage collection
 }

 @Test
 public void testProcessCommonJSModules() {
 String input = "// A CommonJS module\n" +
 "var exports = {\n" +
 " add: function(x, y) {\n" +
 " return x + y;\n" +
 " },\n" +
 " multiply: function(x, y) {\n" +
 " return x * y;\n" +
 " }\n" +
 "};\n" +
 "\n" +
 "exports.add(1, 2); // expected output: 3\n" +
 "exports.multiply(2, 3); // expected output: 6";

 String output = "// rewritten code\n" +
 "goog.provide('module$A');\n" +
 "goog.require('goog.string');\n" +
 "\n" +
 "var module$A = function() {};\n" +
 "module$A.prototype.add = function(x, y) {\n" +
 " return x + y;\n" +
 "};\n" +
 "module$A.prototype.multiply = function(x, y) {\n" +
 " return x * y;\n" +
 "};\n" +
 "\n" +
 "goog.exportProperty(module$A, 'add', module$A.add);\n" +
 "goog.exportProperty(module$A, 'multiply', module$A.multiply);\n" +
 "\n" +
 "module$A.add = module$A.add.bind(module$A);\n" +
 "module$A.multiply = module$A.multiply.bind(module$A);\n" +
 "module$A.module$exports = module$A;\n" +
 "exports = module$A;\n" +
 "\n" +
 "goog.require('module$A');\n" +
 "goog.provide('exports');\n" +
 "exports.add = module$A.add;\n" +
 "exports.multiply = module$A.multiply;\n" +
 "\n" +
 "module$A.add(1, 2); // expected output: 3\n" +
 "module$A.multiply(2, 3); // expected output: 6";

 CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler,
 "", false);

 Node inputNode = nodeFactory.parse(input);
 Node resultNode = processCommonJSModules.process(null, inputNode);
 String result = nodeFactory.printToString(resultNode);
 assertEquals(output, result);
 }

 @Test
 public void testProcessCommonJSModulesWithDependencies() {
 String input = "// A CommonJS module\n" +
 "var exports = {\n" +
 " add: function(x, y) {\n" +
 " return x + y;\n" +
 " },\n" +
 " multiply: function(x, y) {\n" +
 " return x * y;\n" +
 " }\n" +
 "};\n" +
 "\n" +
 "require('moduleB'); // requires moduleB\n" +
 "\n" +
 "exports.add(1, 2); // expected output: 3\n" +
 "exports.multiply(2, 3); // expected output: 6";

 String output = "// rewritten code\n" +
 "goog.provide('module$A');\n" +
 "goog.require('goog.string');\n" +
 "goog.require('module$B');\n" +
 "\n" +
 "var module$A = function() {};\n" +
 "module$A.prototype.add = function(x, y) {\n" +
 " return x + y;\n" +
 "};\n" +
 "module$A.prototype.multiply = function(x, y) {\n" +
 " return x * y;\n" +
 "};\n" +
 "\n" +
 "goog.exportProperty(module$A, 'add', module$A.add);\n" +
 "goog.exportProperty(module$A, 'multiply', module$A.multiply);\n" +
 "\n" +
 "module$A.add = module$A.add.bind(module$A);\n" +
 "module$A.multiply = module$A.multiply.bind(module$A);\n" +
 "module$A.module$exports = module$A;\n" +
 "exports = module$A;\n" +
 "\n" +
 "goog.require('module$B');\n" +
 "goog.provide('exports');\n" +
 "exports.add = module$A.add;\n" +
 "exports.multiply = module$A.multiply;\n" +
 "\n" +
 "module$A.add(1, 2); // expected output: 3\n" +
 "module$A.multiply(2, 3); // expected output: 6";

 CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler,
 "", true);

 Node inputNode = nodeFactory.parse(input);
 Node resultNode = processCommonJSModules.process(null, inputNode);
 String result = nodeFactory.printToString(resultNode);
 assertEquals(output, result);
 }

 @Test
 public void testProcessCommonJSModulesWithInvalidRequireCall() {
 String input = "// A CommonJS module\n" +
 "var exports = {\n" +
 " add: function(x, y) {\n" +
 " return x + y;\n" +
 " },\n" +
 " multiply: function(x, y) {\n" +
 " return x * y;\n" +
 " }\n" +
 "};\n" +
 "\n" +
 "require('invalidModule'); // requires invalidModule which does not exist\n" +
 "\n" +
 "exports.add(1, 2); // expected output: 3\n" +
 "exports.multiply(2, 3); // expected output: 6";

 String output = "// rewritten code\n" +
 "goog.provide('module$A');\n" +
 "goog.require('goog.string');\n" +
 "\n" +
 "var module$A = function() {};\n" +
 "module$A.prototype.add = function(x, y) {\n" +
 " return x + y;\n" +
 "};\n" +
 "module$A.prototype.multiply = function(x, y) {\n" +
 " return x * y;\n" +
 "};\n" +
 "\n" +
 "goog.exportProperty(module$A, 'add', module$A.add);\n" +
 "goog.exportProperty(module$A, 'multiply', module$A.multiply);\n" +
 "\n" +
 "module$A.add = module$A.add.bind(module$A);\n" +
 "module$A.multiply = module$A.multiply.bind(module$A);\n" +
 "module$A.module$exports = module$A;\n" +
 "exports = module$A;\n" +
 "\n" +
 "goog.require('goog.string');\n" +
 "goog.provide('exports');\n" +
 "exports.add = module$A.add;\n" +
 "exports.multiply = module$A.multiply;\n" +
 "\n" +
 "module$A.add(1, 2); // expected output: 3\n" +
 "module$A.multiply(2, 3); // expected output: 6";

 CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler,
 "", true);

 Node inputNode = nodeFactory.parse(input);
 Node resultNode = processCommonJSModules.process(null, inputNode);
 String result = nodeFactory.printToString(resultNode);
 assertEquals(output, result);
 }
}