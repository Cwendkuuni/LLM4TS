import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
public class ScopedAliasesTest {

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private PreprocessorSymbolTable preprocessorSymbolTable;

  @Mock
  private AliasTransformationHandler transformationHandler;

  @InjectMocks
  private ScopedAliases scopedAliases;

  private Node rootNode;

  @Before
  public void setUp() {
    rootNode = Node.createTreeFromString("goog.scope(function() {" +
        "var dom = goog.dom; " +
        "var DIV = dom.TagName.DIV; " +
        "goog.dom.createElement(DIV); " +
        "});");
  }

  @Test
  public void testScopedAliases() {
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(Node.NULL, rootNode);

    // No errors reported
    assertTrue(scopedAliases.hasErrors());

    // Aliases were applied
    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    Node aliasedNode = aliasDefinitionsInOrder.get(0);
    assertEquals("goog.dom", aliasedNode.getString());

    // Aliases were removed from the root node
    Node rootNodeCopy = rootNode.cloneTree();
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    Node expressionWithScopeCall = scopeCall.getParent();
    Node scopeClosureBlock = scopeCall.getLastChild().getLastChild();
    assertFalse(rootNode.contains(scopeCall));
    assertFalse(rootNode.contains(scopeClosureBlock));
    assertTrue(expressionWithScopeCall.getParent().contains(aliasedNode));

    // Scopes were collapsed
    List<Node> scopeCallsAfterCollapse = Lists.newArrayList();
    NodeTraversal traversal = new NodeTraversal();
    NodeTraversal.traverse(compiler, rootNodeCopy, traversal);
    for (Node node = traversal.getTreeTraversalOrder().getFirstChild();
         node != null; node = node.getNext()) {
      if (node.isCall() && SCOPED_ALIASES_CLASS_NAME.equals(node.getFirstChild().getQualifiedName())) {
        scopeCallsAfterCollapse.add(node);
      }
    }
    assertEquals(1, scopeCallsAfterCollapse.size());
    Node collapsedScopeCall = scopeCallsAfterCollapse.get(0);
    assertNull(collapsedScopeCall.getLastChild());
  }

  private static final String SCOPED_ALIASES_CLASS_NAME = "ScopedAliases";
}