import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;

import java.util.Set;

public class ProcessCommonJSModulesTest {

 private Compiler compiler;
 private CompilerOptions options;

 @Before
 public void setup() {
 compiler = new Compiler();
 options = new CompilerOptions();
 }

 @Test
 public void testProcessCommonJSModules() {
 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
 Node root = new Node("function() {}");
 processor.process(null, root);
 // Perform additional assertions here
 }

 @Test
 public void testGuessCJSModuleName() {
 assertEquals("module$myModule", guessCJSModuleName("myModule.js"));
 assertEquals("module$my.Module", guessCJSModuleName("my.Module.js"));
 assertEquals("module$myModule", guessCJSModuleName("./myModule.js"));
 assertEquals("module$myModule", guessCJSModuleName("../myModule.js"));
 }

 @Test
 public void testToModuleName() {
 assertEquals("module$myModule", toModuleName("myModule.js"));
 assertEquals("module$my.Module", toModuleName("my.Module.js"));
 assertEquals("module$myModule", toModuleName("./myModule.js"));
 assertEquals("module$myModule", toModuleName("../myModule.js"));
 assertEquals("module$myModule", toModuleName("./my/Module.js"));
 assertEquals("module$myModule", toModuleName("../my/Module.js"));
 }

 @Test
 public void testNormalizeSourceName() {
 assertEquals("myModule", normalizeSourceName("./myModule.js"));
 assertEquals("myModule", normalizeSourceName("../myModule.js"));
 assertEquals("myModule", normalizeSourceName("./myModule.jsx"));
 assertEquals("myModule", normalizeSourceName("./myModule.ts"));
 assertEquals("myModule", normalizeSourceName("myModule.js"));
 }

 @Test
 public void testProcessCommonJsModulesCallback() {
 Node root = new Node("function processCommonJSModulesCallback() {" +
     "require('./myModule');" +
     "var myModule = require('./myModule');" +
     "goog.require('myModule');" +
     "goog.provide('myModule');" +
     "myModule.exports = {};" +
     "module.exports = myModule.exports;" +
     "}");

 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
 Node result = new Node("function processCommonJSModulesCallback() {" +
     "require('./myModule$module$exports');" +
     "goog.provide('myModule');" +
     "myModule = myModule || {};" +
     "myModule.module$exports = myModule;" +
     "}");

 NodeTraversal traversal = new NodeTraversal(root, options, compiler);
 processor.process(null, root);
 NodeTraversal.traverse(compiler, root, new CallbackVerifier(result));
 }

 @Test(expected = RuntimeException.class)
 public void testURISyntaxException() {
 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
 Node root = new Node("function processURI() {" +
     "require('invalid://URI')" +
     "}");
 processor.process(null, root);
 fail("Expected URISyntaxException but test did not throw exception.");
 }

 @Test
 public void testSuffixVarsCallback() {
 Node root = new Node("var myVar = 42;");
 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "mySuffix", false);
 NodeTraversal traversal = new NodeTraversal(root, options, compiler);
 processor.process(null, root);
 assertEquals("myVar$$mySuffix", root.getFirstChild().getString());
 }
}

 class CallbackVerifier extends AbstractPostOrderCallback {
 private final Node expected;

 CallbackVerifier(Node expected) {
 this.expected = expected.cloneTree();
 }

 @Override
 public void visit(NodeTraversal t, Node n, Node parent) {
 if (!n.equals(expected)) {
 fail("Expected:\n" + expected + "\nReceived:\n" + n);
 }
 }
}