import static org.junit.Assert.*;
import static com.google.javascript.jscomp.node.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeVisitor;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.NodeTree;

import java.io.File;
import java.net.URI;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new Compiler();
    compiler.setOption(CompilerOptions.LanguageMode.ES6);
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testProcessCommonJSModules() throws Exception {
    String input = "var module = {\n" +
        "  exports: {\n" +
        "    x: 1,\n" +
        "  },\n" +
        "};\n" +
        "require('module').exports = module.exports;\n" +
        "function require(moduleName) {\n" +
        "  return moduleName.module$exports;\n" +
        "}\n" +
        "module.exports = {\n" +
        "  x: 2,\n" +
        "};\n";

    NodeTree ast = NodeTree.fromCode(nodeFactory, input);
    CompilerInput ci = new CompilerInput(new File("test.js"), input, ast);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
    processor.process(ci.getExterns(), ci.getRootNode());
    Node root = ci.getRootNode();

    // Basic functionality test
    assertEquals(2, root.getChildAtIndex(0).getChildAtIndex(0).getNumber());

    // Edge cases and exception handling tests

    // Test with empty module
    String emptyModule = "var module = {\n" +
        "  exports: {}\n" +
        "};\n" +
        "require('module').exports = module.exports;\n" +
        "module.exports = {};\n";
    NodeTree emptyAst = NodeTree.fromCode(nodeFactory, emptyModule);
    CompilerInput emptyCi = new CompilerInput(new File("test.js"), emptyModule, emptyAst);
    ProcessCommonJSModules emptyProcessor = new ProcessCommonJSModules(compiler, "", false);
    emptyProcessor.process(emptyCi.getExterns(), emptyCi.getRootNode());
    Node emptyRoot = emptyCi.getRootNode();

    // Test with circular dependency
    String circularDependency = "var module = {\n" +
        "  exports: {}\n" +
        "};\n" +
        "require('module2').exports = module.exports;\n" +
        "module.exports = {\n" +
        "  require: function() {\n" +
        "    return require('module');\n" +
        "  },\n" +
        "  x: 1\n" +
        "};\n" +
        "require('module').exports = {\n" +
        "  require: function() {\n" +
        "    return require('module2');\n" +
        "  },\n" +
        "  x: 2\n" +
        "};\n";
    NodeTree circularDependencyAst = NodeTree.fromCode(nodeFactory, circularDependency);
    CompilerInput circularDependencyCi = new CompilerInput(new File("test.js"), circularDependency, circularDependencyAst);
    ProcessCommonJSModules circularDependencyProcessor = new ProcessCommonJSModules(compiler, "", false);
    try {
      circularDependencyProcessor.process(circularDependencyCi.getExterns(), circularDependencyCi.getRootNode());
      fail("Expected CompilerException for circular dependency.");
    } catch (CompilerException e) {
      // Expected
    }

    // Test with non-existent module
    String nonExistentModule = "require('non-existent-module');\n";
    NodeTree nonExistentModuleAst = NodeTree.fromCode(nodeFactory, nonExistentModule);
    CompilerInput nonExistentModuleCi = new CompilerInput(new File("test.js"), nonExistentModule, nonExistentModuleAst);
    ProcessCommonJSModules nonExistentModuleProcessor = new ProcessCommonJSModules(compiler, "", false);
    try {
      nonExistentModuleProcessor.process(nonExistentModuleCi.getExterns(), nonExistentModuleCi.getRootNode());
      fail("Expected CompilerException for non-existent module.");
    } catch (CompilerException e) {
      // Expected
    }

    // Test with empty source name
    String emptySourceName = "require('');\n";
    NodeTree emptySourceNameAst = NodeTree.fromCode(nodeFactory, emptySourceName);
    CompilerInput emptySourceNameCi = new CompilerInput(new File("test.js"), emptySourceName, emptySourceNameAst);
    ProcessCommonJSModules emptySourceNameProcessor = new ProcessCommonJSModules(compiler, "", false);
    try {
      emptySourceNameProcessor.process(emptySourceNameCi.getExterns(), emptySourceNameCi.getRootNode());
      fail("Expected CompilerException for empty source name.");
    } catch (CompilerException e) {
      // Expected
    }

    // Test with relative source name
    String relativeSourceName = "require('./module');\n";
    NodeTree relativeSourceNameAst = NodeTree.fromCode(nodeFactory, relativeSourceName);
    CompilerInput relativeSourceNameCi = new CompilerInput(new File("test.js"), relativeSourceName, relativeSourceNameAst);
    ProcessCommonJSModules relativeSourceNameProcessor = new ProcessCommonJSModules(compiler, "", false);
    String expectedRelativeModuleName = "module$./module";
    JSModule expectedRelativeModule = new JSModule(expectedRelativeModuleName);
    try {
      ProcessCommonJSModules.process(relativeSourceNameCi.getExterns(), relativeSourceNameCi.getRootNode(), expectedRelativeModule);
      ProcessCommonJSModules expectedProcessor = new ProcessCommonJSModules(compiler, "", false);
      Node expectedRoot = expectedProcessor.process(ci.getExterns(), ci.getRootNode()).getModule().getRootNode();
      assertEquals(expectedRoot, root);
    } catch (CompilerException e) {
      fail("Unexpected CompilerException.");
    }

    // Test with multiple require statements
    String multipleRequire = "var module = {\n" +
        "  exports: {}\n" +
        "};\n" +
        "require('module2').exports = module.exports;\n" +
        "require('module3').exports = module.exports;\n" +
        "module.exports = {\n" +
        "  require: function() {\n" +
        "    return require('module');\n" +
        "  },\n" +
        "  x: 1\n" +
        "};\n" +
        "require('module2').exports = {\n" +
        "  x: 2\n" +
        "};\n" +
        "require('module3').exports = {\n" +
        "  x: 3\n" +
        "};\n";
    NodeTree multipleRequireAst = NodeTree.fromCode(nodeFactory, multipleRequire);
    CompilerInput multipleRequireCi = new CompilerInput(new File("test.js"), multipleRequire, multipleRequireAst);
    ProcessCommonJSModules multipleRequireProcessor = new ProcessCommonJSModules(compiler, "", false);
    Node expectedMultipleRoot = nodeFactory.parse(
        "var module = {\n" +
        "  exports: {\n" +
        "    x: 1\n" +
        "  },\n" +
        "  require: function() {\n" +
        "    return require('module2').module$exports;\n" +
        "  }\n" +
        "};\n" +
        "require('module2').exports = {\n" +
        "  x: 2\n" +
        "};\n" +
        "require('module3').exports = {\n" +
        "  x: 3\n" +
        "};\n" +
        "module.exports = module;\n" +
        "require('module2').module$exports = {\n" +
        "  x: 2\n" +
        "};\n" +
        "require('module3').module$exports = {\n" +
        "  x: 3\n" +
        "};\n" +
        "goog.provide('module');\n" +
        "goog.require('module2');\n" +
        "goog.require('module3');\n");
    try {
      ProcessCommonJSModules.process(multipleRequireCi.getExterns(), multipleRequireCi.getRootNode(), null);
      Node multipleRoot = multipleRequireProcessor.process(multipleRequireCi.getExterns(), multipleRequireCi.getRootNode()).getModule().getRootNode();
      assertEquals(expectedMultipleRoot, multipleRoot);
    } catch (CompilerException e) {
      fail("Unexpected CompilerException.");
    }
  }
}