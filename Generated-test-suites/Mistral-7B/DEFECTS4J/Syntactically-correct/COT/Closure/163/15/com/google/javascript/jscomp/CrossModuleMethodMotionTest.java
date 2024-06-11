import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPass.Pass;
import static com.google.javascript.jscomp.NodeType.*;
import static com.google.javascript.rhino.NodeOps.isFunction;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.IDescriptor;
import com.google.javascript.jscomp.JavaScriptContext;
import com.google.javascript.jscomp.JSFile;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.NodeTraversingCompiler;
import com.google.javascript.jscomp.NodeTraversingProcessor;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SymbolTable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

/**
 * Test class for CrossModuleMethodMotion.
 */
public class CrossModuleMethodMotionTest {

  private static final String CLASS_NAME = "CrossModuleMethodMotion";
  private static final String STUB_FUNCTION_CALL = "JSCompiler_stubMethod";
  private static final String UNSTUB_FUNCTION_CALL = "JSCompiler_unstubMethod";

  @SuppressWarnings("unchecked")
  private static Node createNode(String code) {
    JavaScriptContext context = new JavaScriptContext();
    Node rootNode = context.parseJs(code, null, null);
    return rootNode;
  }

  /**
   * Test method to check if the constructor is initialized correctly.
   */
  @Test
  public void testConstructor() {
    AbstractCompiler compiler = new NodeTraversingCompiler(
        new CompilerOptions(), new Diagnostics());
    IdGenerator idGenerator = new IdGenerator();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
    assertEquals(Pass.CROSS_MODULE_METHOD_MOTION, pass.getPassName());
  }

  /**
   * Test method to check if process method is working as expected.
   */
  @Test
  public void testProcess() {
    AbstractCompiler compiler = new NodeTraversingCompiler(
        new CompilerOptions(), new Diagnostics());
    IdGenerator idGenerator = new IdGenerator();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, true);

    String moduleOneCode = "function foo() {}";
    String moduleTwoCode = "function bar() {}";
    String rootCode =
        "var moduleOne = {" +
        "  exports: {" +
        "    foo: function() { return 'Module One'; }" +
        "  }," +
        "  __esModule: true" +
        "};" +
        "var moduleTwo = {" +
        "  exports: {" +
        "    bar: function() { return 'Module Two'; }" +
        "  }," +
        "  __esModule: true" +
        "};";

    Node root = createNode(rootCode);
    Node moduleOne = createNode(moduleOneCode);
    Node moduleTwo = createNode(moduleTwoCode);

    JSFile moduleOneFile = new JSFile(null, "moduleOne.js", moduleOne, null);
    JSFile moduleTwoFile = new JSFile(null, "moduleTwo.js", moduleTwo, null);

    JSModuleGraph moduleGraph = new JSModuleGraph();
    moduleGraph.addModule(moduleOneFile);
    moduleGraph.addModule(moduleTwoFile);

    NodeTraversingProcessor processor = new NodeTraversingProcessor();
    pass.process(createNode(rootCode), root);
    pass.process(moduleOne, moduleOneFile);
    pass.process(moduleTwo, moduleTwoFile);

    assertEquals(2, idGenerator.currentId);

    Node moduleOneFoo = moduleOne.getChild(1);
    Node moduleTwoBar = moduleTwo.getChild(1);

    // Check that the original function calls are replaced with stubs
    assertThat(moduleOneFoo, isFunction());
    assertEquals("function() { return JSCompiler_stubMethod(0); }",
        moduleOneFoo.toSource());
    assertEquals("Module One", moduleOne.getChild(0).toString());

    // Check that the unstubbed functions are added to the deeper module
    Node moduleOneProto = moduleOne.getChild(0);
    Node unstubbedFunction =
        moduleOneProto.getChildByFunctionName("JSCompiler_unstubMethod");
    Node unstubbedFunctionBody = unstubbedFunction.getChild(1);
    assertEquals("function bar() { return 'Module Two'; }",
        unstubbedFunctionBody.toSource());

    // Check that the deeper module has the unstubbed function
    Node moduleTwoBar = moduleTwo.getChild(1);
    assertEquals("function bar() { return 'Module Two'; }",
        moduleTwoBar.toSource());
  }

  /**
   * Test method to check if the moveMethods method is working as expected
   * with no modules.
   */
  @Test
  public void testNoModules() {
    AbstractCompiler compiler = new NodeTraversingCompiler(
        new CompilerOptions(), new Diagnostics());
    IdGenerator idGenerator = new IdGenerator();

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
    String code = "var x = 1;";

    Node root = createNode(code);
    pass.process(root, root);

    assertEquals(0, idGenerator.currentId);
  }

  /**
   * Test method to check if the moveMethods method is working as expected
   * with a single module.
   */
  @Test
  public void testSingleModule() {
    AbstractCompiler compiler = new NodeTraversingCompiler(
        new CompilerOptions(), new Diagnostics());
    IdGenerator idGenerator = new IdGenerator();

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
    String moduleCode = "function foo() {}";
    String rootCode = "var module = {" + "exports: {" + "foo: function() { return 'Module'; }" + "}," +
        "__esModule: true" + "}";

    Node root = createNode(rootCode);
    Node module = createNode(moduleCode);

    JSFile moduleFile = new JSFile(null, "module.js", module, null);

    NodeTraversingProcessor processor = new NodeTraversingProcessor();
    pass.process(createNode(rootCode), root);
    pass.process(module, moduleFile);

    assertEquals(0, idGenerator.currentId);
  }

  /**
   * Test method to check if the moveMethods method is working as expected
   * with multiple modules.
   */
  @Test
  public void testMultipleModules() {
    AbstractCompiler compiler = new NodeTraversingCompiler(
        new CompilerOptions(), new Diagnostics());
    IdGenerator idGenerator = new IdGenerator();

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
    String moduleOneCode = "function foo() {}";
    String moduleTwoCode = "function bar() {}";
    String rootCode =
        "var moduleOne = {" +
        "  exports: {" +
        "    foo: function() { return 'Module One'; }" +
        "  }," +
        "  __esModule: true" +
        "};" +
        "var moduleTwo = {" +
        "  exports: {" +
        "    bar: function() { return 'Module Two'; }" +
        "  }," +
        "  __esModule: true" +
        "};";

    Node root = createNode(rootCode);
    Node moduleOne = createNode(moduleOneCode);
    Node moduleTwo = createNode(moduleTwoCode);

    JSFile moduleOneFile = new JSFile(null, "moduleOne.js", moduleOne, null);
    JSFile moduleTwoFile = new JSFile(null, "moduleTwo.js", moduleTwo, null);

    JSModuleGraph moduleGraph = new JSModuleGraph();
    moduleGraph.addModule(moduleOneFile);
    moduleGraph.addModule(moduleTwoFile);

    NodeTraversingProcessor processor = new NodeTraversingProcessor();
    pass.process(createNode(rootCode), root);
    pass.process(moduleOne, moduleOneFile);
    pass.process(moduleTwo, moduleTwoFile);

    assertEquals(2, idGenerator.currentId);

    Node moduleOneFoo = moduleOne.getChild(1);
    Node moduleTwoBar = moduleTwo.getChild(1);

    // Check that the original function calls are replaced with stubs
    assertThat(moduleOneFoo, isFunction());
    assertEquals("function() { return JSCompiler_stubMethod(0); }",
        moduleOneFoo.toSource());
    assertEquals("Module One", moduleOne.getChild(0).toString());

    // Check that the unstubbed functions are added to the deeper module
    Node moduleOneProto = moduleOne.getChild(0);
    Node unstubbedFunction =
        moduleOneProto.getChildByFunctionName("JSCompiler_unstubMethod");
    Node unstubbedFunctionBody = unstubbedFunction.getChild(1);
    assertEquals("function bar() { return 'Module Two'; }",
        unstubbedFunctionBody.toSource());

    // Check that the deeper module has the unstubbed function
    Node moduleTwoBar = moduleTwo.getChild(1);
    assertEquals("function bar() { return 'Module Two'; }",
        moduleTwoBar.toSource());
  }

  /**
   * Test method to check if the moveMethods method is working as expected
   * with a circular dependency.
   */
  @Test
  public void testCircularDependency() {
    AbstractCompiler compiler = new NodeTraversingCompiler(
        new CompilerOptions(), new Diagnostics());
    IdGenerator idGenerator = new IdGenerator();

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
    String moduleOneCode = "function foo() { return bar(); }";
    String moduleTwoCode = "function bar() { return foo(); }";
    String rootCode =
        "var moduleOne = {" +
        "  exports: {" +
        "    foo: function() { return 'Module One'; }" +
        "  }," +
        "  __esModule: true" +
        "};" +
        "var moduleTwo = {" +
        "  exports: {" +
        "    bar: function() { return 'Module Two'; }" +
        "  }," +
        "  __esModule: true" +
        "};";

    Node root = createNode(rootCode);
    Node moduleOne = createNode(moduleOneCode);
    Node moduleTwo = createNode(moduleTwoCode);

    JSFile moduleOneFile = new JSFile(null, "moduleOne.js", moduleOne, null);
    JSFile moduleTwoFile = new JSFile(null, "moduleTwo.js", moduleTwo, null);

    JSModuleGraph moduleGraph = new JSModuleGraph();
    moduleGraph.addModule(moduleOneFile);
    moduleGraph.addModule(moduleTwoFile);

    NodeTraversingProcessor processor = new NodeTraversingProcessor();

    try {
      pass.process(createNode(rootCode), root);
      fail("Expected CompilerException to be thrown");
    } catch (CompilerException e) {
      // Expected
    }

    // Check that an error message is logged
    Logger logger = Logger.getLogger(CLASS_NAME);
    List<String> logList = Arrays.asList(logger.getLevel(), logger.getHandlers());
    logger.setLevel(Level.FINEST);
    NodeTraversingCompiler compilerInstance = new NodeTraversingCompiler(
        new CompilerOptions(), new Diagnostics());
    CrossModuleMethodMotion passInstance = new CrossModuleMethodMotion(
        compilerInstance, idGenerator, true);
    passInstance.process(createNode(rootCode), root);
    logger.setLevel(Level.INFO);

    List<String> logMessages = Arrays.stream(logger.getMessages())
        .map(msg -> msg.getMessage())
        .collect(Collectors.toList());
    List<String> expectedLogMessages = Collections.singletonList(
        "JSC_INTERNAL_ERROR_CIRCULAR_DEPENDENCY: circular dependency: " +
            "Module One depends on Module Two, but Module Two depends on " +
            "Module One.");
    assertEquals(expectedLogMessages, logMessages);
  }

  /**
   * Test method to check if the moveMethods method is working as expected
   * with a deeper circular dependency.
   */
  @Test
  public void testDeeperCircularDependency() {
    AbstractCompiler compiler = new NodeTraversingCompiler(
        new CompilerOptions(), new Diagnostics());
    IdGenerator idGenerator = new IdGenerator();

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
    String moduleOneCode = "function foo() { return bar(); }";
    String moduleTwoCode = "function bar() { return foo(); }";
    String moduleThreeCode = "function baz() { return foo(); }";
    String rootCode =
        "var moduleOne = {" +
        "  exports: {" +
        "    foo: function() { return 'Module One'; }" +
        "  }," +
        "  __esModule: true" +
        "};" +
        "var moduleTwo = {" +
        "  exports: {" +
        "    bar: function() { return 'Module Two'; }" +
        "  }," +
        "  __esModule: true" +
        "};" +
        "var moduleThree = {" +
        "  exports: {" +
        "    baz: function() { return 'Module Three'; }" +
        "  }," +
        "  __esModule: true" +
        "};";

    Node root = createNode(rootCode);
    Node moduleOne = createNode(moduleOneCode);
    Node moduleTwo = createNode(moduleTwoCode);
    Node moduleThree = createNode(moduleThreeCode);

    JSFile moduleOneFile = new JSFile(null, "moduleOne.js", moduleOne, null);
    JSFile moduleTwoFile = new JSFile(null, "moduleTwo.js", moduleTwo, null);
    JSFile moduleThreeFile = new JSFile(null, "moduleThree.js", moduleThree, null);

    JSModuleGraph moduleGraph = new JSModuleGraph();
    moduleGraph.addModule(moduleOneFile);
    moduleGraph.addModule(moduleTwoFile);
    moduleGraph.addModule(moduleThreeFile);

    NodeTraversingProcessor processor = new NodeTraversingProcessor();

    try {
      pass.process(createNode(rootCode), root);
      fail("Expected CompilerException to be thrown");
    } catch (CompilerException e) {
      // Expected
    }

    // Check that an error message is logged
    Logger logger = Logger.getLogger(CLASS_NAME);
    List<String> logList = Arrays.asList(logger.getLevel(), logger.getHandlers());
    logger.setLevel(Level.FINEST);
    NodeTraversingCompiler compilerInstance = new NodeTraversingCompiler(
        new CompilerOptions(), new Diagnostics());
    CrossModuleMethodMotion passInstance = new CrossModuleMethodMotion(
        compilerInstance, idGenerator, true);
    passInstance.process(createNode(rootCode), root);
    logger.setLevel(Level.INFO);

    List<String> logMessages = Arrays.stream(logger.getMessages())
        .map(msg -> msg.getMessage())
        .collect(Collectors.toList());
    List<String> expectedLogMessages = ImmutableList.of(
        "JSC_INTERNAL_ERROR_CIRCULAR_DEPENDENCY: circular dependency: " +
            "Module One depends on Module Two, but Module Two depends on " +
            "Module One.",
        "JSC_INTERNAL_ERROR_CIRCULAR_DEPENDENCY: circular dependency: " +
            "Module Two depends on Module One, but Module One depends on " +
            "Module Three, but Module Three depends on Module Two."
    );
    assertEquals(expectedLogMessages, logMessages);
  }
}