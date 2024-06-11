import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;

public class DevirtualizePrototypeMethodsTest {

 private static final String TEST_CODE =
     "class TestClass {" +
     "  static int staticMethod(int a, int b) {" +
     "    return a + b;" +
     "  }" +
     "  int prototypeMethod(int a, int b) {" +
     "    return this.total + a + b;" +
     "  }" +
     "  int total = 0;" +
     "  TestClass() {" +
     "    this.total += 5;" +
     "  }" +
     "}" +
     "TestClass testClass = new TestClass();";

 @SuppressWarnings("unchecked")
 @Test
 public void testDevirtualizePrototypeMethods() {
   CompilerOptions options = new CompilerOptions(null);
   Compiler compiler = new Compiler(options);
   CompilerPassManager passManager = new CompilerPassManager(compiler);
   passManager.addPass(new DevirtualizePrototypeMethods(compiler));

   Node root = compiler.parse(TEST_CODE, "Test", null);
   Node externs = null;

   passManager.process(externs, root);

   SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
   defFinder.process(externs, root);

   Node testClassNode = NodeUtil.findSingleDefinition(root, "TestClass");
   Definition testClassDefinition = defFinder.getDefinition(testClassNode);
   assertNotNull(testClassDefinition);

   // Test basic functionality
   assertEquals(10, compileAndRun(testClassDefinition, 2, 3));

   // Test edge cases
   // Method with no arguments
   assertEquals(5, compileAndRun(testClassDefinition, 0, 0));
   // Method with more than two arguments
   assertEquals(15, compileAndRun(testClassDefinition, 2, 3, 4));
   // Method with varargs
   assertEquals(8, compileAndRun(testClassDefinition, 2, 3, 3));

   // Test exception handling
   assertThrows(RuntimeException.class, () -> {
     compileAndRun(testClassDefinition, -1, 3);
   });
 }

 private int compileAndRun(Definition definition, int... args) {
   String code = NodeUtil.getCode(definition.getRValue());
   CompilerOptions options = new CompilerOptions(null);
   Compiler compiler = new Compiler(options);
   Node root = compiler.parse(code, "Test", null);
   Node externs = null;

   CompilerPassManager passManager = new CompilerPassManager(compiler);
   passManager.addPass(new DevirtualizePrototypeMethods(compiler));

   passManager.process(externs, root);

   Node testClassNode = NodeUtil.findSingleDefinition(root, "TestClass");
   Node testInstanceNode = NodeUtil.getChildByName(testClassNode, "instance");
   Node methodCallNode = NodeUtil.getChildByName(testInstanceNode, "prototypeMethod");

   Node argsList = Node.newList();
   for (int arg : args) {
     Node argNode = Node.newInt(arg);
     argsList.addChild(argNode);
   }

   Node callNode = NodeUtil.createCallNode(methodCallNode, argsList);
   Node resultNode = compiler.compile(callNode, null);

   int result = ((NodeInt) resultNode).getIntValue();
   compiler.disposeCurrentSourceUnit();
   return result;
 }

 private static void assertNotNull(Object object) {
   if (object == null) {
     throw new AssertionError();
   }
 }
}