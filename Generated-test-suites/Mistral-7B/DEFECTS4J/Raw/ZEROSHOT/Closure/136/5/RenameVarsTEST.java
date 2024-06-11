import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RenameVarsTest {

  @SuppressWarnings("unchecked")
  private CompilerPass createRenameVars(String prefix, boolean localRenamingOnly, boolean preserveAnonymousFunctionNames, boolean generatePseudoNames, VariableMap prevUsedRenameMap, char[] reservedCharacters, Set<String> reservedNames) {
    NodeTraversal mockNodeTraversal = mock(NodeTraversal.class);
    Scope mockScope = mock(Scope.class);
    AbstractCompiler mockCompiler = mock(AbstractCompiler.class);

    when(mockScope.getVar(Eq("fa"))).thenReturn(createVar("fa", 0, mockCompiler, null));
    when(mockScope.getVar(Eq("fb"))).thenReturn(createVar("fb", 1, mockCompiler, null));
    when(mockScope.getVar(Eq("fc"))).thenReturn(createVar("fc", 2, mockCompiler, null));
    when(mockNodeTraversal.getScope()).thenReturn(mockScope);

    CompilerInput mockCompilerInput = mock(CompilerInput.class);
    when(mockCompilerInput.getName()).thenReturn("inputName");

    return new RenameVars(mockCompiler, prefix, localRenamingOnly, preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames) {
      @Override
      public void process(Node externs, Node root) {
        super.process(externs, root);
      }
    };
  }

  private Variable createVar(String name, int index, AbstractCompiler compiler, Node parentNode) {
    return new Variable(name, index, compiler, parentNode);
  }

  private static String Eq(final String s) {
    return new String() {
      @Override
      public String toString() {
        return s;
      }
    };
  }

  @org.junit.Test
  public void testRenameVarsEmpty() {
    CompilerPass renameVars = createRenameVars("", false, false, false, null, null, null);
    Node mockRoot = mock(Node.class);
    Node mockExterns = mock(Node.class);
    renameVars.process(mockExterns, mockRoot);
    assertNull(renameVars.getVariableMap());
  }

  @org.junit.Test
  public void testRenameVars() {
    Set<String> reservedNames = Sets.newHashSet("reserved1", "reserved2");
    CompilerPass renameVars = createRenameVars("p_", false, false, false, null, null, reservedNames);
    Node mockRoot = mock(Node.class);
    Node mockExterns = mock(Node.class);

    when(mockExterns.getLastChild()).thenReturn(createStringNode("var a = 1;"));
    when(mockRoot.getLastChild()).thenReturn(createStringNode("function f() { var a = 1; }"));

    renameVars.process(mockExterns, mockRoot);

    Map<String, String> variableMap = renameVars.getVariableMap();
    assertNotNull(variableMap);
    ListMultimap<String, String> oldToNewMap = ArrayListMultimap.create();
    for (Map.Entry<String, String> entry : variableMap.entrySet()) {
      oldToNewMap.put(entry.getKey(), entry.getValue());
    }

    assertNotNull(variableMap.get("a"));
    assertNotNull(variableMap.get("p_a"));
    assertEquals("p_a", variableMap.get("a"));
    assertEquals("a", variableMap.get("p_a"));

    assertEquals("p_a", oldToNewMap.get("a").get(0));
    assertEquals("p_a", oldToNewMap.get("a").get(1));
  }

  @org.junit.Test
  public void testRenameVarsLocal() {
    Set<String> reservedNames = Sets.newHashSet("reserved1", "reserved2");
    CompilerPass renameVars = createRenameVars("", true, false, false, null, null, reservedNames);
    Node mockRoot = mock(Node.class);
    Node mockExterns = mock(Node.class);

    when(mockExterns.getLastChild()).thenReturn(createStringNode(""));
    when(mockRoot.getLastChild()).thenReturn(createStringNode("function f() { var a = 1; var b = 1; }"));

    renameVars.process(mockExterns, mockRoot);

    Map<String, String> variableMap = renameVars.getVariableMap();
    assertNotNull(variableMap);
    ListMultimap<String, String> oldToNewMap = ArrayListMultimap.create();
    for (Map.Entry<String, String> entry : variableMap.entrySet()) {
      oldToNewMap.put(entry.getKey(), entry.getValue());
    }

    assertEquals("L0", oldToNewMap.get("a").get(0));
    assertEquals("L1", oldToNewMap.get("b").get(0));
    assertEquals("L0", oldToNewMap.get("a").get(1));
    assertEquals("L1", oldToNewMap.get("b").get(1));
    assertEquals("a", oldToNewMap.get("L0").get(0));
    assertEquals("b", oldToNewMap.get("L1").get(0));
  }

  @org.junit.Test
  public void testRenameVarsGlobal() {
    Set<String> reservedNames = Sets.newHashSet("reserved1", "reserved2");
    CompilerPass renameVars = createRenameVars("g_", false, false, false, null, null, reservedNames);
    Node mockRoot = mock(Node.class);
    Node mockExterns = mock(Node.class);

    when(mockExterns.getLastChild()).thenReturn(createStringNode("var a = 1; var b = 1;"));
    when(mockRoot.getLastChild()).thenReturn(createStringNode("function f() { var a = 1; var b = 1; }"));

    renameVars.process(mockExterns, mockRoot);

    Map<String, String> variableMap = renameVars.getVariableMap();
    assertNotNull(variableMap);
    ListMultimap<String, String> oldToNewMap = ArrayListMultimap.create();
    for (Map.Entry<String, String> entry : variableMap.entrySet()) {
      oldToNewMap.put(entry.getKey(), entry.getValue());
    }

    assertEquals("g_a", oldToNewMap.get("a").get(0));
    assertEquals("g_b", oldToNewMap.get("b").get(0));
    assertEquals("a", oldToNewMap.get("g_a").get(0));
    assertEquals("b", oldToNewMap.get("g_b").get(0));
  }

  @org.junit.Test
  public void testRenameVarsGlobalWithExterns() {
    Set<String> reservedNames = Sets.newHashSet("reserved1", "reserved2");
    CompilerPass renameVars = createRenameVars("g_", false, false, false, null, null, reservedNames);
    Node mockRoot = mock(Node.class);
    Node mockExterns = mock(Node.class);

    when(mockExterns.getLastChild()).thenReturn(createStringNode("var a = 1; var b = 1;"));
    when(mockRoot.getLastChild()).thenReturn(createStringNode("function f() { var a = 1; var b = 1; } var c = 1;"));

    renameVars.process(mockExterns, mockRoot);

    Map<String, String> variableMap = renameVars.getVariableMap();
    assertNotNull(variableMap);
    ListMultimap<String, String> oldToNewMap = ArrayListMultimap.create();
    for (Map.Entry<String, String> entry : variableMap.entrySet()) {
      oldToNewMap.put(entry.getKey(), entry.getValue());
    }

    assertEquals("g_a", oldToNewMap.get("a").get(0));
    assertEquals("g_b", oldToNewMap.get("b").get(0));
    assertEquals("g_c", oldToNewMap.get("c").get(0));
    assertEquals("a", oldToNewMap.get("g_a").get(0));
    assertEquals("b", oldToNewMap.get("g_b").get(0));
    assertEquals("c", oldToNewMap.get("g_c").get(0));
  }

  @org.junit.Test
  public void testRenameVarsAnonymousFunction() {
    Set<String> reservedNames = Sets.newHashSet("reserved1", "reserved2");
    CompilerPass renameVars = createRenameVars("", false, true, false, null, null, reservedNames);
    Node mockRoot = mock(Node.class);
    Node mockExterns = mock(Node.class);

    when(mockExterns.getLastChild()).thenReturn(createStringNode(""));
    when(mockRoot.getLastChild()).thenReturn(createStringNode("function f() { var a = function() { return a; } }"));

    renameVars.process(mockExterns, mockRoot);

    Map<String, String> variableMap = renameVars.getVariableMap();
    assertNotNull(variableMap);
    ListMultimap<String, String> oldToNewMap = ArrayListMultimap.create();
    for (Map.Entry<String, String> entry : variableMap.entrySet()) {
      oldToNewMap.put(entry.getKey(), entry.getValue());
    }

    assertEquals("anonymous$0", oldToNewMap.get("a").get(0));
    assertEquals("anonymous$0", oldToNewMap.get("anonymous$0").get(0));
  }

  @org.junit.Test
  public void testRenameVarsPreservePseudoName() {
    Set<String> reservedNames = Sets.newHashSet("reserved1", "reserved2");
    CompilerPass renameVars = createRenameVars("", false, false, true, null, null, reservedNames);
    Node mockRoot = mock(Node.class);
    Node mockExterns = mock(Node.class);

    when(mockExterns.getLastChild()).thenReturn(createStringNode("var a = 1; var b = 1;"));
    when(mockRoot.getLastChild()).thenReturn(createStringNode("function f() { var a = 1; var b = 1; } var c = 1;"));

    renameVars.process(mockExterns, mockRoot);

    Map<String, String> variableMap = renameVars.getVariableMap();
    assertNotNull(variableMap);
    ListMultimap<String, String> oldToNewMap = ArrayListMultimap.create();
    for (Map.Entry<String, String> entry : variableMap.entrySet()) {
      oldToNewMap.put(entry.getKey(), entry.getValue());
    }

    assertEquals("$a", oldToNewMap.get("a").get(0));
    assertEquals("$b", oldToNewMap.get("b").get(0));
    assertEquals("$c", oldToNewMap.get("c").get(0));
    assertEquals("a", oldToNewMap.get("$a").get(0));
    assertEquals("b", oldToNewMap.get("$b").get(0));
    assertEquals("c", oldToNewMap.get("$c").get(0));
  }

  private Node createStringNode(String value) {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Token.STRING);
    when(node.getString()).thenReturn(value);
    return node;
  }
}