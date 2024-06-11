import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResults;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class DevirtualizePrototypeMethodsTest {

 private static final String TEST_CASE_SOURCE =
     "function Test() {}\n" +
     "Test.prototype.testMethod = function(a) {" +
     " this.test = a;\n" +
     " return this.test;" +
     "}";

 @Test
 public void testDevirtualizePrototypeMethods() throws Exception {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 options.setWarningLevel(Compiler.WarningLevel.QUIET);
 options.setGenerateErrors(false);

 CompilerPass devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);

 Node root = NodeFactory.createProgram(NodeFactory.createScript(Token.NEW_LINE, TEST_CASE_SOURCE));
 CompilerResults results = new CompilerResults();
 devirtualizePrototypeMethods.process(null, root);
compiler.compile(root, options, results);

 Node testMethod = results.getOutput().getFirstChild();
 assertEquals(NodeUtil.getFunctionType(testMethod, 1),
 NodeUtil.getFunctionType(testMethod, new String[]{"self", "a"}));

 Node testInstance = NodeFactory.createCall(Token.NEW, "Test", null);
 Node testCall = NodeFactory.createCall(Token.GETPROP, testInstance, "testMethod", null);
 Node testValue = NodeFactory.createCall(Token.GETPROP, testInstance, "test", null);
 assertEquals(NodeUtil.toString(testValue), "a");
 assertEquals(NodeUtil.toString(testCall), "JSCompiler_StaticMethods_testMethod(Test, a)");
 }
}