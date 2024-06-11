
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {

  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    scopedAliases = new ScopedAliases(null, null, null);
  }

  @Test
  public void testProcess_WithValidInputs_ShouldApplyAliasesAndUpdateTheCode() {
    // Arrange
    Node externs = new Node(Token.SCRIPT);
    Node root = new Node(Token.SCRIPT);
    Node aliasDefinition1 = new Node(Token.NAME, "dom");
    Node aliasDefinition2 = new Node(Token.NAME, "DIV");
    Node aliasReference1 = new Node(Token.NAME, "dom");
    Node aliasReference2 = new Node(Token.NAME, "DIV");

    // Act
    scopedAliases.process(externs, root);

    // Assert
    Collection<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitions.size());
    assertTrue(aliasDefinitions.contains(aliasDefinition1));
    assertTrue(aliasDefinitions.contains(aliasDefinition2));

    Collection<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(0, scopeCalls.size());

    Collection<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(2, aliasUsages.size());
    assertTrue(aliasUsages.contains(new AliasedNode(aliasReference1, aliasDefinition1.cloneTree())));
    assertTrue(aliasUsages.contains(new AliasedNode(aliasReference2, aliasDefinition2.cloneTree())));

    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testHotSwapScript_WithValidInputs_ShouldApplyAliasesAndUpdateTheCode() {
    // Arrange
    Node root = new Node(Token.SCRIPT);
    Node aliasDefinition1 = new Node(Token.NAME, "dom");
    Node aliasDefinition2 = new Node(Token.NAME, "DIV");
    Node aliasReference1 = new Node(Token.NAME, "dom");
    Node aliasReference2 = new Node(Token.NAME, "DIV");

    // Act
    scopedAliases.hotSwapScript(root, null);

    // Assert
    Collection<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitions.size());
    assertTrue(aliasDefinitions.contains(aliasDefinition1));
    assertTrue(aliasDefinitions.contains(aliasDefinition2));

    Collection<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(0, scopeCalls.size());

    Collection<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(2, aliasUsages.size());
    assertTrue(aliasUsages.contains(new AliasedNode(aliasReference1, aliasDefinition1.cloneTree())));
    assertTrue(aliasUsages.contains(new AliasedNode(aliasReference2, aliasDefinition2.cloneTree())));

    assertTrue(scopedAliases.hasErrors());
  }
}

