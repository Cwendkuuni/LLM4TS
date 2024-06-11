import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Test;

import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node rootNode = createNode(
        "function() {" +
            "  goog.scope(function() {" +
            "    var dom = goog.dom;" +
            "    var DIV = dom.TagName.DIV;" +
            "    goog.dom.createElement(DIV);" +
            "  });" +
            "  goog.dom.createElement(goog.dom.TagName.DIV);" +
            "}");

    scopedAliases.process(null, rootNode);

    List<Node> aliasDefinitions = Lists.newArrayList(
        createNode("function() { var DIV = goog.dom.TagName.DIV; }"),
        createNode("var DIV = goog.dom.TagName.DIV;")
    );

    List<AliasUsage> aliasUsages = Lists.newArrayList(
        new AliasedNode(
            createNode("goog.dom.createElement(DIV)"),
            createNode("goog.dom.TagName.DIV")
        ),
        new AliasedNode(
            createNode("goog.dom.createElement(DIV)"),
            createNode("function() { return goog.dom.TagName.DIV; }")
        )
    );

    List<Node> scopeCalls = Lists.newArrayList(
        createNode("goog.scope(function() {})")
    );

    Traversal traversal = new Traversal();
    when(compiler.report(mock(NodeTraversal.TraversalError.class))).thenReturn(true);
    when(preprocessorSymbolTable.addReference(mock(Node.class))).thenReturn(true);

    NodeTraversal.traverse(compiler, rootNode, traversal);

    assertEquals(aliasDefinitions, traversal.getAliasDefinitionsInOrder());
    assertEquals(aliasUsages, traversal.getAliasUsages());
    assertEquals(scopeCalls, traversal.getScopeCalls());
  }

  private Node createNode(String jsCode) {
    SourcePosition pos = new SourcePosition();
    Node node = new Node(Token.START_FUNCTION, null, null, jsCode, pos, null, null);
    node.setJSDocInfo(new JSDocInfo());
    return node;
  }
}

class AliasedNode implements AliasUsage {
  private final Node aliasReference;
  private final Node aliasDefinition;

  AliasedNode(Node aliasReference, Node aliasDefinition) {
    this.aliasReference = aliasReference;
    this.aliasDefinition = aliasDefinition;
  }

  @Override
  public void applyAlias() {
    aliasReference.getParent().replaceChild(aliasReference, aliasDefinition);
  }
}

class AliasedTypeNode implements AliasUsage {
  private final Node typeReference;
  private final String aliasName;

  AliasedTypeNode(Node typeReference, String aliasName) {
    this.typeReference = typeReference;
    this.aliasName = aliasName;
  }

  @Override
  public void applyAlias() {
    typeReference.setString(aliasName);
  }
}

interface AliasUsage {
  public void applyAlias();
}