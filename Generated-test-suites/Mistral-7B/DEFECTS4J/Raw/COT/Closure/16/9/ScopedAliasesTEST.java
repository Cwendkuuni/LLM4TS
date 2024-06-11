import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

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

  @Before
  public void setUp() {
    compiler = Mockito.mock(AbstractCompiler.class);
    preprocessorSymbolTable = Mockito.mock(PreprocessorSymbolTable.class);
    transformationHandler = Mockito.mock(AliasTransformationHandler.class);

    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testValidScopedAliases() {
    // Given
    Node rootNode = createTestNode("test.js");

    // When
    scopedAliases.process(null, rootNode);

    // Then
    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testScopeCallsAreRemoved() {
    // Given
    Node rootNode = createTestNode("test.js");

    // When
    scopedAliases.process(null, rootNode);

    // Then
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertFalse(scopeCalls.isEmpty());

    for (Node scopeCall : scopeCalls) {
      assertFalse(rootNode.contains(scopeCall));
    }
  }

  @Test
  public void testAliasDefinitionsAreAppliedInOrder() {
    // Given
    Node rootNode = createTestNode("test.js");

    // When
    scopedAliases.process(null, rootNode);

    // Then
    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    List<String> expectedAliases = Lists.newArrayList("dom", "DIV");

    for (int i = 0; i < aliasDefinitions.size(); i++) {
      Node aliasDefinition = aliasDefinitions.get(i);
      String aliasName = aliasDefinition.getString();
      assertEquals(expectedAliases.get(i), aliasName);
    }
  }

  @Test
  public void testAliasesAreAppliedCorrectly() {
    // Given
    Node rootNode = createTestNode("test.js");

    // When
    scopedAliases.process(null, rootNode);

    // Then
    Node domNode = rootNode.getFirstChild().getLastChild();
    assertEquals("dom", domNode.getString());

    Node divNode = findNodeByQualifiedName(rootNode, "goog.dom.DIV");
    assertNotNull(divNode);
  }

  @Test
  public void testAliasRedefinitionError() {
    // Given
    Node rootNode = createTestNode("test_redefinition.js");

    // When / Then
    assertThrows(DiagnosticType.class, () -> scopedAliases.process(null, rootNode))
        .isInstanceOf(GOOG_SCOPE_ALIAS_REDEFINED);
  }

  @Test
  public void testNonAliasLocalError() {
    // Given
    Node rootNode = createTestNode("test_non_alias_local.js");

    // When / Then
    assertThrows(DiagnosticType.class, () -> scopedAliases.process(null, rootNode))
        .isInstanceOf(GOOG_SCOPE_NON_ALIAS_LOCAL);
  }

  @Test
  public void testImproperUsageError() {
    // Given
    Node rootNode = createTestNode("test_improper_usage.js");

    // When / Then
    assertThrows(DiagnosticType.class, () -> scopedAliases.process(null, rootNode))
        .isInstanceOf(GOOG_SCOPE_USED_IMPROPERLY);
  }

  @Test
  public void testHasBadParametersError() {
    // Given
    Node rootNode = createTestNode("test_bad_parameters.js");

    // When / Then
    assertThrows(DiagnosticType.class, () -> scopedAliases.process(null, rootNode))
        .isInstanceOf(GOOG_SCOPE_HAS_BAD_PARAMETERS);
  }

  @Test
  public void testRefersThisError() {
    // Given
    Node rootNode = createTestNode("test_refers_this.js");

    // When / Then
    assertThrows(DiagnosticType.class, () -> scopedAliases.process(null, rootNode))
        .isInstanceOf(GOOG_SCOPE_REFERENCES_THIS);
  }

  @Test
  public void testUsesReturnError() {
    // Given
    Node rootNode = createTestNode("test_uses_return.js");

    // When / Then
    assertThrows(DiagnosticType.class, () -> scopedAliases.process(null, rootNode))
        .isInstanceOf(GOOG_SCOPE_USES_RETURN);
  }

  @Test
  public void testUsesThrowError() {
    // Given
    Node rootNode = createTestNode("test_uses_throw.js");

    // When / Then
    assertThrows(DiagnosticType.class, () -> scopedAliases.process(null, rootNode))
        .isInstanceOf(GOOG_SCOPE_USES_THROW);
  }

  private Node createTestNode(String fileName) {
    Node rootNode = new Node(Node.ROOT, new SourcePosition(0, 0), fileName);
    rootNode.setJSDocInfo(new JSDocInfo());
    return rootNode;
  }

  private Node findNodeByQualifiedName(Node node, String qualifiedName) {
    if (node.isFunction() && NodeUtil.isLValue(node)) {
      Node nameNode = node.getFirstChild();
      if (nameNode.isName()) {
        String name = nameNode.getString();
        if (name.equals(qualifiedName.substring(0, name.length()))) {
          Node child = node.getFirstChild();
          while (child != null) {
            if (child.isFunction() && NodeUtil.isLValue(child)) {
              String childName = child.getFirstChild().getString();
              if (qualifiedName.length() > name.length() + 1 &&
                  qualifiedName.substring(name.length() + 1).equals("." + childName)) {
                return child;
              }
            }
            child = child.getNext();
          }
        }
      }
    }
    return null;
  }
}