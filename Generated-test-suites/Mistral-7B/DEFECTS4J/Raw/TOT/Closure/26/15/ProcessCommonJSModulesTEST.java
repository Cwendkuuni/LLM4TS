import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.NodeFactory;

public class ProcessCommonJSModulesTest {

  private CompilerInput input;
  private NodeFactory nodeFactory;
  private AbstractCompiler compiler;
  private ProcessCommonJSModules processor;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    nodeFactory = compiler.getOptions().nodeFactory;
    input = new CompilerInput("test.js", "test.js", null);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
  }

  @Test
  public void testSimpleModule() {
    // Prepare input
    Node root = nodeFactory.literalNull();
    Node module = nodeFactory.script(nodeFactory.stringLiteral("module.exports = 42;"));
    root.addChildToBack(module);

    // Process
    processor.process(input, root);

    // Check output
    Node expected = nodeFactory.numberLiteral(42);
    Node actual = processor.getModule().getModuleExports();

    assertEquals("Module exports should be equal", expected, actual);
  }

  @Test
  public void testModuleWithDependencies() {
    // Prepare input
    Node rootA = nodeFactory.script(nodeFactory.stringLiteral("require('./a'); module.exports = 42;"));
    Node moduleA = nodeFactory.script(nodeFactory.stringLiteral("module.exports = 21;"));

    Node rootB = nodeFactory.script(nodeFactory.stringLiteral("require('./b'); module.exports = 43;"));
    Node moduleB = rootB;

    rootA.addChildToBack(moduleA);
    rootB.addChildToBack(moduleB);

    // Process
    processor.process(input, rootA);

    // Check output
    Node expectedA = nodeFactory.numberLiteral(21);
    Node actualA = processor.getModule().require("./a");

    Node expectedB = nodeFactory.numberLiteral(43);
    Node actualB = processor.getModule().require("./b");

    assertEquals("Module A should be imported correctly", expectedA, actualA);
    assertEquals("Module B should be imported correctly", expectedB, actualB);

    Node expectedRoot = nodeFactory.block(
        nodeFactory.assign(nodeFactory.name("a"), expectedA),
        nodeFactory.assign(nodeFactory.name("b"), expectedB),
        nodeFactory.numberLiteral(42)
    );

    Node actualRoot = processor.getRootNode();

    assertEquals("Root node should be equal", expectedRoot, actualRoot);
  }

  @Test
  public void testModuleWithInvalidRequireCall() {
    // Prepare input
    Node root = nodeFactory.script(nodeFactory.stringLiteral("require('invalid'); module.exports = 42;"));

    // Process
    try {
      processor.process(input, root);
      fail("Expected ProcessCommonJSModulesException");
    } catch (ProcessCommonJSModulesException e) {
      // Expected
    }

    // Check that no code change was made
    Node actualRoot = processor.getRootNode();
    Node expectedRoot = root.cloneTree();
    assertEquals("Root node should be equal", expectedRoot, actualRoot);
  }

  @Test(expected = ProcessCommonJSModulesException.class)
  public void testModuleWithCyclicDependencies() {
    // Prepare input
    Node moduleA = nodeFactory.script(nodeFactory.stringLiteral(
        "require('./b'); module.exports = 21;"));

    Node moduleB = nodeFactory.script(nodeFactory.stringLiteral(
        "require('./a'); module.exports = 21;"));

    Node root = nodeFactory.script(nodeFactory.stringLiteral(
        "require('./a'); require('./b');"));

    root.addChildToBack(moduleA);
    root.addChildToBack(moduleB);

    // Process
    processor.process(input, root);

    fail("Expected ProcessCommonJSModulesException");
  }

  @Test
  public void testModuleWithMissingDependency() {
    // Prepare input
    Node root = nodeFactory.script(nodeFactory.stringLiteral(
        "require('./missing'); module.exports = 42;"));

    // Process
    ProcessCommonJSModulesException e = assertThrows(
        ProcessCommonJSModulesException.class, () -> {
          processor.process(input, root);
        });

    // Check error message
    String expectedMessage = "Module 'missing' not found.";
    assertEquals("Error message should be equal", expectedMessage, e.getMessage());

    // Check that no code change was made
    Node actualRoot = processor.getRootNode();
    Node expectedRoot = root.cloneTree();
    assertEquals("Root node should be equal", expectedRoot, actualRoot);
  }
}