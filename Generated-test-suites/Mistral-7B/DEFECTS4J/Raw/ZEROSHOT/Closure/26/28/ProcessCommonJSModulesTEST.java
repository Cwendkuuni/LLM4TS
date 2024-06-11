import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerState;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeVisitor;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private static final String FILENAME_PREFIX = "test";

  @org.junit.Test
  public void testProcessCommonJSModules() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerPass processor = new ProcessCommonJSModules(compiler, FILENAME_PREFIX);

    CompilerPassManager manager = new CompilerPassManager();
    manager.addPass(ProcessorType.TOP_LEVEL, processor);

    CompilerState state = new CompilerState();
    state.setInput(createInput());

    Node root = mock(Node.class);
    NodeTraversal traversal = new NodeTraversal(compiler, root, state);
    manager.process(state, traversal);

    Node expectedOutput = getExpectedOutput();
    assertEquals(expectedOutput, root);
  }

  private Node createInput() {
    NodeFactory nodeFactory = NodeFactory.getFactory();
    String jsCode = "var a = require('a');\n" +
        "var b = require('b');\n" +
        "exports.c = 42;\n" +
        "module.exports = exports;\n";

    Node externs = nodeFactory.createExterns();
    Node script = nodeFactory.createScript(jsCode, new File("test.js").toURI());

    Node root = nodeFactory.createTopLevel(externs, script);
    return root;
  }

  private Node getExpectedOutput() {
    NodeFactory nodeFactory = NodeFactory.getFactory();
    String jsCode = "var a$$a = require('a');\n" +
        "var b$$b = require('b');\n" +
        "goog.provide('test');\n" +
        "test.c = 42;\n" +
        "goog.require('a$$a');\n" +
        "goog.require('b$$b');\n";

    Node root = nodeFactory.createTopLevel(NodeFactory.nullExterns(),
        nodeFactory.createScript(jsCode, new File("test.js").toURI()));
    return root;
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithReportDependencies() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerPass processor = new ProcessCommonJSModules(compiler, FILENAME_PREFIX,
        true);

    CompilerPassManager manager = new CompilerPassManager();
    manager.addPass(ProcessorType.TOP_LEVEL, processor);

    CompilerState state = new CompilerState();
    state.setInput(createInputWithDependencies());

    Node root = mock(Node.class);
    NodeTraversal traversal = new NodeTraversal(compiler, root, state);
    manager.process(state, traversal);

    Map<String, Node> dependencies = new HashMap<>();
    dependencies.put("a", createDependencyNode("a"));
    dependencies.put("b", createDependencyNode("b"));
    Set<String> expectedDependencies = Sets.newHashSet("a", "b");

    verifyDependencies(dependencies, expectedDependencies);
  }

  private Node createInputWithDependencies() {
    NodeFactory nodeFactory = NodeFactory.getFactory();
    String jsCode = "var a = require('a');\n" +
        "var b = require('b');\n" +
        "exports.c = 42;\n" +
        "module.exports = exports;\n";

    Node externs = nodeFactory.createExterns();
    Node script = nodeFactory.createScript(jsCode, new File("test.js").toURI());

    Node root = nodeFactory.createTopLevel(externs, script);
    root.putProp(AbstractCompiler.INPUT_DEPENDENCIES_PROP,
        createDependencyNodeSet("a", "b"));
    return root;
  }

  private Node createDependencyNodeSet(String... dependencies) {
    NodeFactory nodeFactory = NodeFactory.getFactory();
    Node root = nodeFactory.createNodeList();

    for (String dependency : dependencies) {
      Node dependencyNode = createDependencyNode(dependency);
      root.addChild(dependencyNode);
    }

    return root;
  }

  private Node createDependencyNode(String dependency) {
    NodeFactory nodeFactory = NodeFactory.getFactory();
    Node dependencyNode = nodeFactory.createCall(
        nodeFactory.createName("goog", "require"),
        nodeFactory.createString(dependency));
    dependencyNode.putProp(Node.ORIGINALNAME_PROP, dependency);
    return dependencyNode;
  }

  private void verifyDependencies(Map<String, Node> dependencies,
      Set<String> expectedDependencies) {
    Node dependenciesNode = getDependenciesNode(getExpectedOutput());
    Node dependencyList = dependenciesNode.getFirstChild();

    for (Map.Entry<String, Node> dependencyEntry : dependencies.entrySet()) {
      Node dependencyNode = dependencyEntry.getValue();
      String dependencyName = dependencyEntry.getKey();

      assertEquals(dependencyNode, dependencyList.getChildAtIndex(
          dependencies.keySet().iterator().next().hashCode()));

      String dependencyNameInOutput = dependencyNode.getString();
      assertEquals(dependencyName, dependencyNameInOutput.substring(0, dependencyNameInOutput.length() - 2));

      assertEquals(dependencyNode, dependencyList.getChildAtIndex(
          dependencies.keySet().containsAll(expectedDependencies)
              ? expectedDependencies.stream()
                  .map(String::hashCode)
                  .findFirst()
                  .get() : -1));
    }

    assertEquals(expectedDependencies.size(), dependencyList.getChildCount());
  }

  private Node getDependenciesNode(Node node) {
    Node dependenciesNode = null;

    if (node.isTopLevel()) {
      NodeVisitor visitor = new NodeVisitor() {
        @Override
        public void visitCallExpression(Node node) {
          if (node.getQualifiedName().equals("goog.require")) {
            dependenciesNode = node;
            return;
          }
          super.visitCallExpression(node);
        }
      };

      NodeTraversal.traverse(nodeFactory, node, visitor);
    }

    return dependenciesNode;
  }
}