import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;

public class ProcessCommonJSModulesTest {

 @Test
 public void testProcessCommonJSModules() {
 CompilerInput input = new CompilerInput("test.js", "test.js");
 AbstractCompiler compiler = new AbstractCompiler() {
 @Override
 public CompilerPass getPass(String passName) {
 if (passName.equals(ProcessCommonJSModules.class.getName())) {
 return new ProcessCommonJSModules(this, "");
 }
 return null;
 }
 };
 CompilerOptions options = new CompilerOptions();
 ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, "");
 JSError error = processCommonJSModules.process(input, null);
 assertNull(error);

 Node root = input.getTree();
 String expectedModuleName = "test_module";
 String expectedModuleNameWithExports = "test_module_exports";
 String expectedModuleNameWithDep = "depend_module";
 String expectedModuleNameWithDepExports = "depend_module_exports";

 // Test with single module
 checkModuleName(root, expectedModuleName, "test.js", "test.js");

 // Test with module importing another module
 String dependModuleContent = "var exports = module.exports = function() {" +
 "return 'hello';" +
 "};";
 CompilerInput dependInput = new CompilerInput("depend.js", dependModuleContent);
 input.addDependency(dependInput);
 checkModuleNames(root, expectedModuleName, expectedModuleNameWithExports,
 expectedModuleNameWithDep, expectedModuleNameWithDepExports, "test.js",
 "depend.js");
 }

 private void checkModuleName(Node root, String expectedName, String inputFile,
 String sourceFile) {
 Node moduleNode = findModuleNode(root, inputFile, sourceFile);
 assertEquals(expectedName, moduleNode.getFirstChild().getString());
 }

 private void checkModuleNames(Node root, String... expectedModuleNames) {
 for (String expectedModuleName : expectedModuleNames) {
 Node moduleNode = findModuleNode(root, null, null);
 assertEquals(expectedModuleName, moduleNode.getFirstChild().getString());
 }
 }

 private Node findModuleNode(Node root, String inputFile, String sourceFile) {
 Node moduleNode = root;
 if (inputFile != null && sourceFile != null) {
 Node inputNode = findNodeBySourceName(root, inputFile);
 moduleNode = findNodeRecursively(inputNode, sourceFile, "module");
 }
 return moduleNode;
 }

 private Node findNodeBySourceName(Node node, String name) {
 if (node.isName() && node.getString().equals(name)) {
 return node;
 }
 if (node.getChildCount() == 0) {
 return null;
 }
 for (int i = 0; i < node.getChildCount(); i++) {
 Node child = node.getChildAtIndex(i);
 Node result = findNodeBySourceName(child, name);
 if (result != null) {
 return result;
 }
 }
 return null;
 }

 private Node findNodeRecursively(Node node, String name, String propName) {
 if (node.isObjectLiteral()) {
 for (int i = 0; i < node.getChildCount(); i++) {
 Node child = node.getChildAtIndex(i);
 if (child.isName() && child.getString().equals(propName)) {
 Node result = findNodeRecursively(child.getChildAtIndex(0), name, null);
 if (result != null) {
 return result;
 }
 }
 }
 }
 return null;
 }
}