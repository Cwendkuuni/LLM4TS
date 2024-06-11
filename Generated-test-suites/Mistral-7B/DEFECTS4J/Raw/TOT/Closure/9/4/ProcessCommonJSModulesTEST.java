import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProcessCommonJSModulesTest {

 @Test
 public void testProcessCommonJSModules() {
 // Set up compiler
 Compiler compiler = new Compiler();
 CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler, "");

 // Set up input code
 String sourceCode = "var module1 = {\n" +
 "exports: {\n" +
 "someFunction: function() {\n" +
 "console.log('Module 1');\n" +
 "}\n" +
 "},\n" +
 "require: function(moduleName) {\n" +
 "return require('./module2');\n" +
 "}\n" +
 "};\n" +
 "\n" +
 "function someFunction() {\n" +
 "console.log('Global');\n" +
 "}\n" +
 "\n" +
 "var module2 = {\n" +
 "exports: {\n" +
 "someOtherFunction: function() {\n" +
 "console.log('Module 2');\n" +
 "}\n" +
 "}\n" +
 "};\n";

 // Process the code
 Node externs = Node.parse(sourceCode);
 Node root = Node.objectLiteral();
 processCommonJSModules.process(externs, root);

 // Assert the output
 Node expectedOutput = Node.objectLiteral();
 expectedOutput.addChild(new Node("module1", Node.IS_VAR | Node.IS_GLOBAL));
 expectedOutput.addChild(new Node("module1$exports", Node.IS_OBJECT | Node.IS_GLOBAL));
 expectedOutput.addChild(new Node("someFunction", Node.IS_FUNCTION | Node.IS_GLOBAL));
 expectedOutput.addChild(new Node("module2", Node.IS_VAR | Node.IS_GLOBAL));
 expectedOutput.addChild(new Node("module2$exports", Node.IS_OBJECT | Node.IS_GLOBAL));
 expectedOutput.addChild(new Node("someOtherFunction", Node.IS_FUNCTION | Node.IS_GLOBAL));
 expectedOutput.addChild(new Node("someFunction", Node.IS_FUNCTION));

 Node actualOutput = Node.getFirstChild(root);
 assertEquals(expectedOutput, actualOutput);
 }
}