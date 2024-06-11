import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public class ScopedAliasesTest {
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    scopedAliases = new ScopedAliases(new AbstractCompiler(), null, new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(String fileName, SourcePosition<AliasTransformation> aliasTransformationSourcePosition) {
        return null;
      }
    });
  }

  @Test
  public void testProcess() {
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    scopedAliases.process(externs, root);
    Node expectedExterns = new Node(Token.EXTERN);
    Node expectedRoot = new Node(Token.ROOT);
    Assert.assertEquals(expectedExterns, externs);
    Assert.assertEquals(expectedRoot, root);
  }

  @Test
  public void testHotSwapScript() {
    Node root = new Node(Token.ROOT);
    Node originalRoot = new Node(Token.ROOT);
    scopedAliases.hotSwapScript(root, originalRoot);
    Node expectedRoot = new Node(Token.ROOT);
    Assert.assertEquals(expectedRoot, root);
    Assert.assertEquals(expectedRoot, originalRoot);
  }

  @Test
  public void testAliasedNodeApplyAlias() {
    Node aliasReference = new Node(Token.NAME, "aliasReference");
    Node aliasDefinition = new Node(Token.NAME, "aliasDefinition");
    ScopedAliases.AliasedNode aliasedNode = new ScopedAliases.AliasedNode(aliasReference, aliasDefinition);
    aliasedNode.applyAlias();
    Node expectedAliasReference = new Node(Token.NAME, "aliasDefinition");
    Assert.assertEquals(expectedAliasReference, aliasReference.getParent());
  }

  @Test
  public void testAliasedTypeNodeApplyAlias() {
    Node aliasReference = new Node(Token.NAME, "aliasReference");
    String correctedType = "correctedType";
    ScopedAliases.AliasedTypeNode aliasedTypeNode = new ScopedAliases.AliasedTypeNode(aliasReference, correctedType);
    aliasedTypeNode.applyAlias();
    Node expectedAliasReference = new Node(Token.STRING, correctedType);
    Assert.assertEquals(expectedAliasReference, aliasReference);
  }

  @Test
  public void testTraversalEnterScope() {
    NodeTraversal t = new NodeTraversal(new AbstractCompiler());
    Node n = new Node(Token.ROOT);
    scopedAliases.new Traversal().enterScope(t);
    Assert.assertEquals(null, scopedAliases.new Traversal().transformation);
  }

  @Test
  public void testTraversalExitScope() {
    NodeTraversal t = new NodeTraversal(new AbstractCompiler());
    Node n = new Node(Token.ROOT);
    scopedAliases.new Traversal().exitScope(t);
    Assert.assertEquals(null, scopedAliases.new Traversal().transformation);
  }

  @Test
  public void testTraversalShouldTraverse() {
    NodeTraversal t = new NodeTraversal(new AbstractCompiler());
    Node n = new Node(Token.ROOT);
    Assert.assertEquals(true, scopedAliases.new Traversal().shouldTraverse(t, n, null));
  }

  @Test
  public void testTraversalValidateScopeCall() {
    NodeTraversal t = new NodeTraversal(new AbstractCompiler());
    Node n = new Node(Token.ROOT);
    Node parent = new Node(Token.ROOT);
    scopedAliases.new Traversal().validateScopeCall(t, n, parent);
    Node expectedN = new Node(Token.ROOT);
    Assert.assertEquals(expectedN, n);
    Node expectedParent = new Node(Token.ROOT);
    Assert.assertEquals(expectedParent, parent);
  }

  @Test
  public void testTraversalVisit() {
    NodeTraversal t = new NodeTraversal(new AbstractCompiler());
    Node n = new Node(Token.ROOT);
    Node parent = new Node(Token.ROOT);
    scopedAliases.new Traversal().visit(t, n, parent);
    Node expectedN = new Node(Token.ROOT);
    Assert.assertEquals(expectedN, n);
    Node expectedParent = new Node(Token.ROOT);
    Assert.assertEquals(expectedParent, parent);
  }

  @Test
  public void testFixTypeNode() {
    Node typeNode = new Node(Token.NAME, "typeNode");
    scopedAliases.new Traversal().fixTypeNode(typeNode);
    Node expectedTypeNode = new Node(Token.NAME, "typeNode");
    Assert.assertEquals(expectedTypeNode, typeNode);
  }
}