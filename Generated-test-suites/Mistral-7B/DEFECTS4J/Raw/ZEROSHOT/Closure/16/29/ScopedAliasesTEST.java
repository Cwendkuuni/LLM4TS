import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    // Given
    AliasTransformationHandler transformationHandler = mock(AliasTransformationHandler.class);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node rootNode = createNode("goog.scope(function() { var dom = goog.dom; var DIV = dom.TagName.DIV; dom.createElement(DIV); });");

    // When
    scopedAliases.process(null, rootNode);

    // Then
    verify(transformationHandler, times(1)).logAliasTransformation(
        anyString(), isA(SourcePosition.class));

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("goog.dom", aliasDefinition.getString());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertFalse(scopeCall.isCall());

    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(2, aliasUsages.size());
    AliasUsage aliasUsage1 = aliasUsages.get(0);
    Node aliasReference1 = ((AliasedNode) aliasUsage1).getAliasReference();
    assertEquals("goog.dom", aliasReference1.getString());
    Node aliasDefinition1 = ((AliasedNode) aliasUsage1).getAliasDefinition();
    assertEquals("goog.dom", aliasDefinition1.getString());

    AliasUsage aliasUsage2 = aliasUsages.get(1);
    Node typeReference = ((AliasedTypeNode) aliasUsage2).getTypeReference();
    assertEquals("goog.dom.TagName", typeReference.getString());
    String aliasName = ((AliasedTypeNode) aliasUsage2).getAliasName();
    assertEquals("DIV", aliasName);
  }

  private Node createNode(String code) {
    Node rootNode = new Node(Token.SCRIPT_START);
    SourcePosition position = new SourcePosition();
    position.setLine(1);
    position.setColumn(1);
    rootNode.setPos(position);
    Node script = new Node(Token.SCRIPT, code, null, null, null, null);
    rootNode.appendChild(script);
    return rootNode;
  }
}