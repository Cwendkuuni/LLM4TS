import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.DefinitionsRemover.DefinitionSite;
import com.google.javascript.jscomp.ErrorReporter;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeNative;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.SimpleDefinitionFinder.UseSite;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DevirtualizePrototypeMethodsTest {

  @Test
  public void testDevirtualizePrototypeMethods() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel("JSCOMP_WARNING", CompilerPass.Level.WARNING);

    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ErrorReporter errorReporter = mock(ErrorReporter.class);
    CompilerPassManager passManager = new CompilerPassManager(compiler, errorReporter);
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);

    when(compiler.getTypeRegistry()).thenReturn(typeRegistry);
    when(compiler.getModuleGraph()).thenReturn(new MockModuleGraph());
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());

    CompilerPass devirtualizePrototypeMethods =
        new DevirtualizePrototypeMethods(compiler);

    Node root = createTestNode();
    DefinitionSite definitionSite = new DefinitionSite(null, null, null, null);

    passManager.addPass(devirtualizePrototypeMethods);
    passManager.process(new Node[]{root}, options);

    assertTrue(nodeContainsString(root, "JSCompiler_StaticMethods_accumulate"));
    assertFalse(nodeContainsString(root, "a.prototype.accumulate"));
  }

  private Node createTestNode() {
    Node root = new Node(Token.BLOCK, null);

    Node aNode = newNode("var a = function() {this.total = 0;}", null);
    root.addChild(aNode);

    Node prototypeNode = newNode("a.prototype.accumulate = function(value) {this.total += value; return this.total}", null);
    root.addChild(prototypeNode);

    Node accumulateCallNode = newNode("var total = a.accumulate(2)", null);
    root.addChild(accumulateCallNode);

    return root;
  }

  private Node newNode(String jsCode, Node parent) {
    Node node = new Node(Token.BLOCK, parent);
    Node codeNode = new Node(Token.ASSIGN, null);
    Node codeNodeValue = new Node(Token.BLOCK, codeNode);
    codeNodeValue.setJSType(JSType.NULL);
    codeNode.setLastChild(codeNodeValue);
    node.appendChild(new Node(Token.STRING, jsCode));
    node.setLastChild(codeNode);
    return node;
  }

  private boolean nodeContainsString(Node node, String substring) {
    return NodeUtil.containsSubtree(node, new Node(Token.STRING, substring));
  }

  private static class MockModuleGraph implements JSModuleGraph {
    @Override
    public Map<JSModule, JSModule> getDirectDependencies() {
      return Map.of();
    }

    @Override
    public Map<JSModule, JSModule> getAllDependencies() {
      return Map.of();
    }

    @Override
    public boolean dependsOn(JSModule module1, JSModule module2) {
      return false;
    }
  }

  private static class CodingConvention implements CompilerPass.CodingConvention {
    @Override
    public boolean isExported(Node node) {
      return false;
    }
  }
}