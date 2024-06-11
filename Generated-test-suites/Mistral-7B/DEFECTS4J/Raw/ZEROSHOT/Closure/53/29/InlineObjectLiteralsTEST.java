import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;
import com.google.javascript.rhino.NodeTraversal;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Scope rootScope = createTestScope();
    Node root = createTestRootNode(rootScope);

    ReferenceCollection referenceCollection = createTestReferenceCollection(rootScope);
    inlineObjectLiterals.process(null, root);

    assertFalse(isVariableBlacklisted(rootScope, "x"));
    assertFalse(isVariableBlacklisted(rootScope, "y"));
    assertFalse(isVariableBlacklisted(rootScope, "z"));
    assertFalse(isVariableBlacklisted(rootScope, "a"));
    assertFalse(isVariableBlacklisted(rootScope, "b"));

    Reference xRef = referenceCollection.getReferenceByPath("x", 0);
    Reference yRef = referenceCollection.getReferenceByPath("y", 0);
    Reference zRef = referenceCollection.getReferenceByPath("z", 0);
    Reference aRef = referenceCollection.getReferenceByPath("a", 0);
    Reference bRef = referenceCollection.getReferenceByPath("b", 0);

    Map<String, Node> expectedVariables = ImmutableMap.of(
        "x_JSCompiler_object_inline_0", rootScope.getNode("x"),
        "y_JSCompiler_object_inline_1", rootScope.getNode("y"),
        "z_JSCompiler_object_inline_2", rootScope.getNode("z"),
        "a_JSCompiler_object_inline_3", rootScope.getNode("a"),
        "b_JSCompiler_object_inline_4", rootScope.getNode("b")
    );

    for (Map.Entry<String, Reference> entry : referenceCollection.references.entrySet()) {
      String key = entry.getKey();
      Reference ref = entry.getValue();
      if (ref == xRef || ref == yRef || ref == zRef || ref == aRef || ref == bRef) {
        continue;
      }

      Node actualVariable = ref.getNode();
      assertTrue(expectedVariables.containsKey(actualVariable.getString()));
      assertTrue(expectedVariables.get(actualVariable.getString()).equals(actualVariable));
    }
  }

  private Scope createTestScope() {
    return new Scope(null, createTestRootNode(null));
  }

  private Node createTestRootNode(Scope parent) {
    Node root = new Node(Token.PROGRAM, null, null);
    root.addChild(new Node(Token.BLOCK, root, null));

    Node block = root.getFirstChild();
    Node assignX = new Node(Token.ASSIGN, null, null);
    assignX.addChild(new Node(Token.NAME, "x", null));
    assignX.addChild(new Node(Token.OBJECTLIT, null, null));
    assignX.addChildToFront(new Node(Token.COMMA, assignX, null));
    assignX.addChildToFront(new Node(Token.TRUE, assignX, null));
    block.addChild(assignX);

    Node assignY = new Node(Token.ASSIGN, null, null);
    assignY.addChild(new Node(Token.NAME, "y", null));
    assignY.addChild(new Node(Token.OBJECTLIT, null, null));
    assignY.addChildToFront(new Node(Token.COMMA, assignY, null));
    assignY.addChildToFront(new Node(Token.TRUE, assignY, null));
    block.addChild(assignY);

    Node assignZ = new Node(Token.ASSIGN, null, null);
    assignZ.addChild(new Node(Token.NAME, "z", null));
    assignZ.addChild(new Node(Token.OBJECTLIT, null, null));
    assignZ.addChildToFront(new Node(Token.COMMA, assignZ, null));
    assignZ.addChildToFront(new Node(Token.TRUE, assignZ, null));
    block.addChild(assignZ);

    Node assignA = new Node(Token.ASSIGN, null, null);
    assignA.addChild(new Node(Token.NAME, "a", null));
    assignA.addChild(new Node(Token.OBJECTLIT, null, null));
    assignA.addChildToFront(new Node(Token.COMMA, assignA, null));
    assignA.addChildToFront(new Node(Token.TRUE, assignA, null));
    block.addChild(assignA);

    Node assignB = new Node(Token.ASSIGN, null, null);
    assignB.addChild(new Node(Token.NAME, "b", null));
    assignB.addChild(new Node(Token.OBJECTLIT, null, null));
    assignB.addChildToFront(new Node(Token.COMMA, assignB, null));
    assignB.addChildToFront(new Node(Token.TRUE, assignB, null));
    block.addChild(assignB);

    return root;
  }

  private ReferenceCollection createTestReferenceCollection(Scope scope) {
    ReferenceCollection referenceCollection = new ReferenceCollection(null, null, null);
    when(safeNameIdSupplier.get()).thenReturn("_0");

    when(compiler.getScope()).thenReturn(scope);
    when(compiler.getCodingConvention()).thenReturn(mock(Node.CodingConvention.class));
    when(compiler.reportCodeChange()).thenReturn(new Runnable() {
      @Override
      public void run() {}
    });

    inlineObjectLiterals.process(null, null);
   inlineObjectLiterals.new InliningBehavior().process(null, referenceCollection);

    return referenceCollection;
  }

  private boolean isVariableBlacklisted(Scope scope, String name) {
    for (Var var : scope.getVars()) {
      if (var.name.equals(name)) {
        return var.isBlacklisted();
      }
    }
    return false;
  }
}