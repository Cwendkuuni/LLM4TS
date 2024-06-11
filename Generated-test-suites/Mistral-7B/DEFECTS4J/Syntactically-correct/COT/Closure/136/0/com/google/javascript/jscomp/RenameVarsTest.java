import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SortedMap;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RenameVarsTest {
  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private RenameVars renamer;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = new NodeFactory(null);
    renamer = new RenameVars(compiler, "", false, false, false, null, null, null);
  }

  @Test
  public void testEmpty() {
    Node root = nodeFactory.createNull();
    renamer.process(nodeFactory.createList(), root);
    assertEquals(Collections.emptyList(), renamer.getVariableMap().variables());
  }

  @Test
  public void testGlobalRenaming() {
    String code = "function f() { var a = 1, b = 2, c = 3; }";
    Node root = nodeFactory.parse(code);

    List<Node> globalNameNodes = Arrays.asList(
        root.getFirstChild().getFirstChild(),
        root.getFirstChild().getFirstChild().getNextSibling(),
        root.getFirstChild().getLastChild().getNextSibling()
    );

    // Expected global renames
    Map<String, String> expectedGlobalRenames = ImmutableMap.of(
        "a", "a0",
        "b", "b0",
        "c", "c0"
    );

    renamer.process(nodeFactory.createList(), root);
    SortedMap<String, String> actualGlobalRenames = renamer.getVariableMap().getGlobalAssignments();
    assertEquals(expectedGlobalRenames, actualGlobalRenames);
  }

  @Test
  public void testLocalRenaming() {
    String code = "function f(a, b) { var c = function(d) { return a; }, e = 2; }";
    Node root = nodeFactory.parse(code);

    List<Node> localNameNodes = Arrays.asList(
        root.getFirstChild().getLastChild().getFirstChild(),
        root.getLastChild().getFirstChild()
    );

    // Expected local renames
    Map<String, String> expectedLocalRenames = ImmutableMap.of(
        "c", "L0",
        "e", "e0"
    );

    renamer.process(nodeFactory.createList(), root);
    SortedMap<String, String> actualLocalRenames = renamer.getVariableMap().getLocalAssignments();
    assertEquals(expectedLocalRenames, actualLocalRenames);
  }

  @Test
  public void testGlobalAndLocalRenaming() {
    String code = "function f(a, b) { var c = 1, d = function(e) { return a; }; }";
    Node root = nodeFactory.parse(code);

    List<Node> globalNameNodes = Arrays.asList(root.getFirstChild());
    List<Node> localNameNodes = Arrays.asList(
        root.getFirstChild().getLastChild().getFirstChild(),
        root.getLastChild().getFirstChild()
    );

    // Expected global renames
    Map<String, String> expectedGlobalRenames = ImmutableMap.of(
        "a", "a0"
    );

    // Expected local renames
    Map<String, String> expectedLocalRenames = ImmutableMap.of(
        "c", "L1",
        "d", "L2"
    );

    renamer.process(nodeFactory.createList(), root);
    SortedMap<String, String> actualGlobalRenames = renamer.getVariableMap().getGlobalAssignments();
    SortedMap<String, String> actualLocalRenames = renamer.getVariableMap().getLocalAssignments();

    assertEquals(expectedGlobalRenames, actualGlobalRenames);
    assertEquals(expectedLocalRenames, actualLocalRenames);
  }

  @Test(expected = IllegalStateException.class)
  public void testReservedNames() {
    String code = "function f(if, else, var) { var a = 1; }";
    Node root = nodeFactory.parse(code);

    renamer.process(nodeFactory.createList(), root);
  }

  @Test
  public void testExternRenaming() {
    String code = "var a = 1, b = 2, c = 3; function f() {}";
    Node root = nodeFactory.parse(code);

    List<Node> globalNameNodes = Arrays.asList(
        root.getFirstChild(),
        root.getFirstChild().getNextSibling(),
        root.getFirstChild().getNextSibling()
    );

    // Expected global renames
    Map<String, String> expectedGlobalRenames = ImmutableMap.of(
        "a", "a0",
        "b", "b0",
        "c", "c0"
    );

    renamer = new RenameVars(compiler, "", false, false, false, null, null, Sets.newHashSet("a", "b", "c"));
    renamer.process(nodeFactory.createList(), root);
    SortedMap<String, String> actualGlobalRenames = renamer.getVariableMap().getGlobalAssignments();
    assertEquals(expectedGlobalRenames, actualGlobalRenames);
  }

  @Test
  public void testNoRenaming() {
    String code = "function f() { var a = 1, b = 2, c = 3; }";
    Node root = nodeFactory.parse(code);

    Node input = root.getParent();
    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));

    renamer = new RenameVars(compiler, "", false, false, false, null, null, null);
    renamer.process(input, root);

    List<Node> globalNameNodes = Arrays.asList(
        root.getFirstChild(),
        root.getFirstChild().getNextSibling(),
        root.getFirstChild().getNextSibling()
    );

    for (Node n : globalNameNodes) {
      assertEquals(n.getString(), n.getString());
    }
  }
}