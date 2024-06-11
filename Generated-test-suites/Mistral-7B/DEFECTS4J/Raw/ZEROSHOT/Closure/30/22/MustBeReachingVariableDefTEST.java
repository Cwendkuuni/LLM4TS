import static org.junit.Assert.*;
import static com.google.common.truth.Truth.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MustBeReachingVariableDefTest {

  private final AbstractCompiler compiler = Mockito.mock(AbstractCompiler.class);
  private final Scope jsScope = Mockito.mock(Scope.class);
  private final ControlFlowGraph<Node> cfg = Mockito.mock(ControlFlowGraph.class);

  MustBeReachingVariableDef mustBeReachingVariableDef;

  @Before
  public void setUp() {
    when(compiler.getNodeForSourcePos(anyInt(), anyInt())).then((invocation) -> {
      int line = invocation.getArgument(0);
      int column = invocation.getArgument(1);
      switch (line) {
        case 1:
          return Node.NEW_ARRAY(1, Node.INT_LITERAL(1));
        case 2:
          return Node.ASSIGN(Node.VAR("a"), Node.INT_LITERAL(1));
        case 3:
          return Node.ASSIGN(Node.VAR("a"), Node.VAR("a"));
        case 4:
          return Node.ASSIGN(Node.VAR("b"), Node.INT_LITERAL(1));
        case 5:
          return Node.ASSIGN(Node.VAR("a"), Node.VAR("b"));
        case 6:
          return Node.VAR("a");
        case 7:
          return Node.VAR("b");
        case 8:
          return Node.ASSIGN(Node.VAR("a"), Node.VAR("c"));
        case 9:
          return Node.ASSIGN(Node.VAR("c"), Node.VAR("a"));
        case 10:
          return Node.ASSIGN(Node.VAR("c"), Node.INT_LITERAL(1));
        default:
          throw new AssertionError("Unexpected node line number: " + line);
      }
    });

    when(cfg.getRootNode()).thenReturn(Node.BLOCK(Arrays.asList(
        Node.VAR("a"),
        Node.ASSIGN(Node.VAR("a"), Node.INT_LITERAL(1)),
        Node.ASSIGN(Node.VAR("a"), Node.VAR("a")),
        Node.ASSIGN(Node.VAR("b"), Node.INT_LITERAL(1)),
        Node.ASSIGN(Node.VAR("a"), Node.VAR("b")),
        Node.ASSIGN(Node.VAR("a"), Node.VAR("c")),
        Node.ASSIGN(Node.VAR("c"), Node.VAR("a")),
        Node.ASSIGN(Node.VAR("c"), Node.INT_LITERAL(1)),
        Node.ASSIGN(Node.VAR("x"), Node.VAR("a")),
        Node.RETURN(Node.VAR("x"))
    )));

    when(jsScope.getVars()).thenReturn(Collections.singletonList(
        Map.entry(Node.VAR("a"), new Var(Node.BLOCK(Node.VAR("a")), null, null, null, null, null))));

    mustBeReachingVariableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);
  }

  @Test
  public void testGetDef_aAtUse6() {
    Node useNode = Node.VAR("a");
    Node expectedDefNode = Node.ASSIGN(Node.VAR("a"), Node.INT_LITERAL(1));
    Node actualDefNode = mustBeReachingVariableDef.getDef("a", useNode);
    assertEquals(expectedDefNode, actualDefNode);
  }

  @Test
  public void testGetDef_bAtUse7() {
    Node useNode = Node.VAR("b");
    Node expectedDefNode = Node.ASSIGN(Node.VAR("b"), Node.INT_LITERAL(1));
    Node actualDefNode = mustBeReachingVariableDef.getDef("b", useNode);
    assertEquals(expectedDefNode, actualDefNode);
  }

  @Test
  public void testGetDef_aAtUse10() {
    Node useNode = Node.VAR("c");
    Node expectedDefNode = Node.ASSIGN(Node.VAR("c"), Node.INT_LITERAL(1));
    Node actualDefNode = mustBeReachingVariableDef.getDef("c", useNode);
    assertEquals(expectedDefNode, actualDefNode);
  }

  @Test
  public void testGetDef_xAtUse10() {
    Node useNode = Node.VAR("x");
    Node expectedDefNode = Node.ASSIGN(Node.VAR("a"), Node.VAR("c"));
    Node actualDefNode = mustBeReachingVariableDef.getDef("x", useNode);
    assertEquals(expectedDefNode, actualDefNode);
  }

  @Test
  public void testGetDef_zAtUse10() {
    Node useNode = Node.VAR("z");
    Node actualDefNode = mustBeReachingVariableDef.getDef("z", useNode);
    assertNull(actualDefNode);
  }

  @Test
  public void testDependsonOuterScopeVars_aAtUse6() {
    Node useNode = Node.VAR("a");
    assertFalse(mustBeReachingVariableDef.dependsOnOuterScopeVars("a", useNode));
  }

  @Test
  public void testDependsonOuterScopeVars_bAtUse7() {
    Node useNode = Node.VAR("b");
    assertFalse(mustBeReachingVariableDef.dependsOnOuterScopeVars("b", useNode));
  }

  @Test
  public void testDependsonOuterScopeVars_cAtUse8() {
    Node useNode = Node.VAR("c");
    assertFalse(mustBeReachingVariableDef.dependsOnOuterScopeVars("c", useNode));
  }

  @Test
  public void testDependsonOuterScopeVars_xAtUse10() {
    Node useNode = Node.VAR("x");
    assertTrue(mustBeReachingVariableDef.dependsOnOuterScopeVars("x", useNode));
  }

  @Test
  public void testDependsonOuterScopeVars_zAtUse10() {
    Node useNode = Node.VAR("z");
    assertFalse(mustBeReachingVariableDef.dependsOnOuterScopeVars("z", useNode));
  }

}