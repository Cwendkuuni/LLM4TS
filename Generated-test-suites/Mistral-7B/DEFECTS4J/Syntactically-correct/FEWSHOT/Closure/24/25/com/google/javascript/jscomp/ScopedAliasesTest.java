package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RunWith(PowerMockRunner.class)
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    AbstractCompiler compiler = PowerMockito.mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = PowerMockito.mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = PowerMockito.mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // Test null input
    Node externs = null;
    Node root = null;
    scopedAliases.process(externs, root);

    // Test empty input
    externs = Node.EMPTY_NODE;
    root = Node.EMPTY_NODE;
    scopedAliases.process(externs, root);

    // Test valid usage of goog.scope
    Node rootNode = new Node(Token.BLOCK, null, null, null, null, null, null, null,
        new SourcePosition(1, 1, 1, 1));
    Node scopeCallNode = new Node(Token.CALL, null, null, null, null, null, null,
        new Node(Token.NAME, "goog.scope", null, null, null, null, null, null,
            new SourcePosition(1, 2, 1, 2)),
        new Node(Token.FUNCTION, null, null, null, null, null, null, null,
            new SourcePosition(1, 7, 1, 7), "function() {}", null));
    rootNode.appendChild(scopeCallNode);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);

    assertTrue(traversal.getScopeCalls().isEmpty());
    assertTrue(traversal.getAliasDefinitionsInOrder().isEmpty());
    assertTrue(traversal.getAliasUsages().isEmpty());

    // Test invalid usage of goog.scope
    Node invalidScopeCallNode = new Node(Token.CALL, null, null, null, null, null, null,
        new Node(Token.NAME, "goog.scope", null, null, null, null, null, null,
            new SourcePosition(1, 2, 1, 2)),
        new Node(Token.FUNCTION, null, null, null, null, null, null, null,
            new SourcePosition(1, 7, 1, 7), "function() { return 42; }", null));
    rootNode.appendChild(invalidScopeCallNode);

    traversal = new Traversal();
    NodeTraversal.traverse(compiler, rootNode, traversal);

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals(invalidScopeCallNode, scopeCalls.get(0));

    List<DiagnosticType> expectedDiagnosticTypes = Lists.newArrayList(
        GOOG_SCOPE_USED_IMPROPERLY,
        GOOG_SCOPE_HAS_BAD_PARAMETERS);

    for (DiagnosticType diagnosticType : expectedDiagnosticTypes) {
      assertEquals(diagnosticType, traversal.getFirstError().getType());
    }
  }
}

enum DiagnosticType {
  JSC_GOOG_SCOPE_USED_IMPROPERLY,
  JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS,
  // Add other diagnostic types here if necessary
}