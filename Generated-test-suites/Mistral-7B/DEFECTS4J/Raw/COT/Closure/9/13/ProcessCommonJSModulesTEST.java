import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Processor;
import com.google.javascript.jscomp.ProcessorOptions;
import com.google.javascript.jscomp.ProcessCommonJSModules;

public class ProcessCommonJSModulesTest {

 @Test
 public void testProcessCommonJSModules() throws Exception {
 // Given
 Processor processor = new Processor(new ProcessorOptions());
 CompilerInput input = new CompilerInput("test.js", null, null);
 ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(
 processor, ".", false);
 Node root = new Node("({})");

 // When
 processor.process(input, root);
 ProcessCommonJSModulesCallback callback = new ProcessCommonJSModulesCallback();
 NodeTraversal.traverse(processor, root, callback);

 // Then
 JSModule jsModule = processCommonJSModules.getModule();
 assertNotNull(jsModule);
 assertEquals("test", jsModule.getName());

 // Given
 String requiredFilename = "required.js";
 String currentFilename = "test.js";
 Node requireCall = new Node("require('" + requiredFilename + "')");
 Node moduleRef = new Node("module$required");
 Node script = new Node("// test script");
 Node moduleExportsProp = new Node("module.exports");
 Node exports = new Node("{foo: 'bar'}");

 // When
 callback.visitRequireCall(null, requireCall, script);
 callback.visitScript(null, script);
 callback.visitModuleExports(moduleExportsProp);

 // Then
 assertEquals("module$required", moduleRef.getString());
 assertEquals("module$exports", moduleExportsProp.getChildAtIndex(1).getString());
 assertEquals("{foo: 'bar'}", jsModule.getModuleExports().toString());
 }

 @Test
 public void testProcessCommonJSModulesWithDependencies() throws Exception {
 // Given
 Processor processor = new Processor(new ProcessorOptions());
 CompilerInput input = new CompilerInput("test.js", null, null);
 ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(
 processor, ".", true);
 Node root = new Node("(function() {\n" +
 "  require('dependency');\n" +
 "  var foo = 'bar';\n" +
 "})()");

 // When
 processor.process(input, root);
 ProcessCommonJSModulesCallback callback = new ProcessCommonJSModulesCallback();
 NodeTraversal.traverse(processor, root, callback);

 // Then
 assertTrue(input.containsRequire("dependency"));
 }

 @Test(expected = RuntimeException.class)
 public void testURISyntaxException() throws Exception {
 // Given
 Processor processor = new Processor(new ProcessorOptions());
 CompilerInput input = new CompilerInput("test.js", null, null);
 ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(
 processor, ".", false);
 Node root = new Node("(function() {\n" +
 "  require('../../../test.js')\n" +
 "})()");

 // When
 processor.process(input, root);
 }
}

class ProcessCommonJSModulesCallback extends NodeTraversal.AbstractPostOrderCallback {
 // ... (same as in the original class)
}