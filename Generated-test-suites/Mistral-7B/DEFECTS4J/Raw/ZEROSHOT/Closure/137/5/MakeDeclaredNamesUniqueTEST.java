import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TraversalUtils;

import java.util.Arrays;
import java.util.List;

public class MakeDeclaredNamesUniqueTest {

  private static final String TEST_CASE_ID = "MakeDeclaredNamesUniqueTest";

  @SuppressWarnings("unchecked")
  private static final Node DUMMY_NODE = mock(Node.class);

  private static final Node DUMMY_FUNCTION_DECLARATION = mock(Node.class);
  static {
    when(DUMMY_FUNCTION_DECLARATION.getType()).thenReturn(Token.FUNCTION);
    when(DUMMY_FUNCTION_DECLARATION.getFirstChild()).thenReturn(DUMMY_NODE);
  }

  private static final Node DUMMY_VAR_DECLARATION = mock(Node.class);
  static {
    when(DUMMY_VAR_DECLARATION.getType()).thenReturn(Token.VAR);
    when(DUMMY_VAR_DECLARATION.getString()).thenReturn("testVar");
  }

  private static final Node DUMMY_NAME_NODE = mock(Node.class);
  static {
    when(DUMMY_NAME_NODE.getString()).thenReturn("testName");
  }

  private static final Node DUMMY_FUNCTION = mock(Node.class);
  static {
    when(DUMMY_FUNCTION.getFirstChild()).thenReturn(DUMMY_FUNCTION_DECLARATION);
    when(DUMMY_FUNCTION.getLastChild()).thenReturn(DUMMY_NODE);
  }

  private static final Scope DUMMY_SCOPE = mock(Scope.class);

  private static final AbstractCompiler DUMMY_COMPILER = mock(AbstractCompiler.class);

  private static final NodeTraversal TRAVERSAL = mock(NodeTraversal.class);

  private static final MakeDeclaredNamesUnique RENAMER = new MakeDeclaredNamesUnique();
  private static final ContextualRenamer ROOT_RENAMER = new ContextualRenamer();

  @SuppressWarnings("unchecked")
  private static <T> List<T> createList(T... values) {
    return Lists.newArrayList(Arrays.asList(values));
  }

  private static void assertDeclaredNameAdded(
      String name, String expectedReplacementName, boolean isGlobal) {
    MakeDeclaredNamesUnique renamer = isGlobal ? RENAMER : new MakeDeclaredNamesUnique(ROOT_RENAMER);
    Scope scope = createMockScope();
    when(scope.getVar(name)).thenReturn(createMockVar());
    when(TRAVERSAL.getScope()).thenReturn(scope);

    renamer.enterScope(TRAVERSAL);

    Node nameNode = createMockNameNode(name);
    renamer.visit(TRAVERSAL, TRAVERSAL.getRootNode(), DUMMY_NODE);

    String actualReplacementName = renamer.getReplacementName(name);
    assertEquals(expectedReplacementName, actualReplacementName);

    renamer.exitScope(TRAVERSAL);
  }

  private static void assertDeclaredNameNotAdded(String name) {
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    Scope scope = createMockScope();
    when(scope.getVar(name)).thenReturn(createMockVar());
    when(TRAVERSAL.getScope()).thenReturn(scope);

    renamer.enterScope(TRAVERSAL);

    Node nameNode = createMockNameNode(name);
    renamer.visit(TRAVERSAL, TRAVERSAL.getRootNode(), DUMMY_NODE);

    String replacementName = renamer.getReplacementName(name);
    assertNull(replacementName);

    renamer.exitScope(TRAVERSAL);
  }

  private static Node createMockNameNode(String name) {
    Node node = mock(Node.class);
    when(node.getString()).thenReturn(name);
    return node;
  }

  private static Var createMockVar() {
    Var var = mock(Var.class);
    return var;
  }

  private static Scope createMockScope() {
    Scope scope = mock(Scope.class);
    when(scope.getRootNode()).thenReturn(DUMMY_FUNCTION);
    when(scope.declare(
        eq(DUMMY_NAME_NODE),
        eq(DUMMY_VAR_DECLARATION),
        eq(null),
        eq(null)))
        .thenReturn(null);
    return scope;
  }

  @org.junit.Test
  public void testContextualRenamer_globalScope_simpleTest() {
    // Test case 1:
    // Test the root contextual renamer in global scope.

    assertDeclaredNameAdded("testVar", "testVar", true);
  }

  @org.junit.Test
  public void testContextualRenamer_childScope_simpleTest() {
    // Test case 2:
    // Test a child contextual renamer.

    assertDeclaredNameAdded("testVar", "testVar$$0", false);

    // Test that the global renamer does not rename the same variable.
    assertDeclaredNameAdded("testVar", "testVar$$0", true);
  }

  @org.junit.Test
  public void testContextualRenamer_globalScope_multipleDeclaredNames() {
    // Test case 3:
    // Test the root contextual renamer in global scope with multiple declared
    // names.

    assertDeclaredNameAdded("testVar1", "testVar1", true);
    assertDeclaredNameAdded("testVar2", "testVar2$$0", true);
    assertDeclaredNameAdded("testVar3", "testVar3$$1", true);
  }

  @org.junit.Test
  public void testContextualRenamer_childScope_multipleDeclaredNames() {
    // Test case 4:
    // Test a child contextual renamer with multiple declared names.

    assertDeclaredNameAdded("testVar1", "testVar1$$0", false);
    assertDeclaredNameAdded("testVar2", "testVar2$$1", false);
    assertDeclaredNameAdded("testVar3", "testVar3$$2", false);

    // Test that the parent contextual renamer does not rename the same variable.
    assertDeclaredNameAdded("testVar1", "testVar1$$0", true);
  }

  @org.junit.Test
  public void testInlineRenamer_simpleTest() {
    // Test case 5:
    // Test an inline renamer with a simple test.

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique(
        new InlineRenamer(() -> "0", "i_") {
          @Override
          public boolean stripConstIfReplaced() {
            return false;
          }
        });

    assertDeclaredNameAdded("testVar", "testVari_0", false);
  }

  @org.junit.Test
  public void testInlineRenamer_multipleDeclaredNames() {
    // Test case 6:
    // Test an inline renamer with multiple declared names.

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique(
        new InlineRenamer(() -> "0", "i_") {
          @Override
          public boolean stripConstIfReplaced() {
            return true;
          }
        });

    assertDeclaredNameAdded("testVar1", "testVar1i_0", false);
    assertDeclaredNameAdded("testVar2", "testVar2i_1", false);
  }

  @org.junit.Test
  public void testInlineRenamer_globalScope_multipleDeclaredNames() {
    // Test case 7:
    // Test an inline renamer in global scope with multiple declared names.

    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique(
        new InlineRenamer(() -> "0", "g_") {
          @Override
          public boolean stripConstIfReplaced() {
            return false;
          }
        });

    assertDeclaredNameAdded("testVar1", "testVar1g_0", true);
    assertDeclaredNameAdded("testVar2", "testVar2g_1", true);
  }
}