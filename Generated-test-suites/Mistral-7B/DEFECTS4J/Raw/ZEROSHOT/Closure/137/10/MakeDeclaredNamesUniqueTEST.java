import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.traverse.AbstractPostOrderCallback;
import com.google.javascript.jscomp.traverse.NodeVisitor;

import java.util.Arrays;
import java.util.List;

public class MakeDeclaredNamesUniqueTest {

  private static final String CLASS_NAME = "com.google.javascript.jscomp.MakeDeclaredNamesUnique";
  private static final String RENAME_POLICY_INTERFACE = "Renamer";

  @SuppressWarnings("unchecked")
  private static <T> T createMockRenamer() {
    return mock(Renamer.class);
  }

  private static void testMakeDeclaredNamesUnique(
      boolean isGlobalScope, AbstractPostOrderCallback callback) {
    Scope scope = mock(Scope.class);
    Node node = mock(Node.class);
    Node parentNode = mock(Node.class);
    Node rootNode = mock(Node.class);

    when(scope.getVar(anyString())).thenReturn(createMockVar());
    when(scope.getRootNode()).thenReturn(rootNode);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    NodeTraversal traversal = createNodeTraversal(makeDeclaredNamesUnique, scope);

    if (isGlobalScope) {
      when(rootNode.getType()).thenReturn(Token.FUNCTION);
      when(rootNode.getFirstChild()).thenReturn(null);
    } else {
      when(rootNode.getType()).thenReturn(Token.BLOCK);
      when(rootNode.getFirstChild()).thenReturn(createNode("var a = 1;"));
    }

    when(parentNode.getType()).thenReturn(Token.BLOCK);

    when(node.getType()).thenReturn(Token.NAME);
    when(node.getString()).thenReturn("a");

    callback.enterScope(traversal);
    callback.visit(traversal, node, parentNode);
    callback.exitScope(traversal);

    String expectedName = isGlobalScope ? "a" : "b";
    String actualName = getNameFromVar(scope, "a");

    assertEquals(expectedName, actualName);
  }

  private static Node createNode(String nodeContent) {
    TokenStream tokenStream = new TokenStream();
    tokenStream.setSourceFile("test.js");
    tokenStream.parse(nodeContent);
    return tokenStream.rootNode();
  }

  private static NodeTraversal createNodeTraversal(
      NodeVisitor callback, Scope scope) {
    NodeTraversal traversal = NodeTraversal.create(
        new NodeTraversal.ScopeCallback() {
          @Override
          public NodeVisitor enterScope(NodeTraversal t) {
            return callback;
          }

          @Override
          public NodeVisitor exitScope(NodeTraversal t) {
            return callback;
          }
        },
        scope);
    traversal.traverse(NodeTraversal.fullTraversal(traversal));
    return traversal;
  }

  private static Var createMockVar() {
    Var var = mock(Var.class);
    when(var.getNameNode()).thenReturn(createMockNode("a"));
    when(var.isGlobal()).thenReturn(false);
    when(var.scope).thenReturn(mock(Scope.class));
    return var;
  }

  private static Node createMockNode(String name) {
    Node node = mock(Node.class);
    when(node.getString()).thenReturn(name);
    return node;
  }

  private static String getNameFromVar(Scope scope, String name) {
    Iterator<Map.Entry<String, Var>> iterator = scope.decls.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<String, Var> entry = iterator.next();
      if (entry.getValue().getName().equals(name)) {
        return entry.getKey();
      }
    }
    throw new IllegalArgumentException("Variable not found.");
  }

  public void testMakeDeclaredNamesUniqueInGlobalScope() {
    testMakeDeclaredNamesUnique(true, new MakeDeclaredNamesUnique());
  }

  public void testMakeDeclaredNamesUniqueInLocalScope() {
    testMakeDeclaredNamesUnique(false, new MakeDeclaredNamesUnique());
  }

  public void testMakeDeclaredNamesUniqueWithContextualRenamer() {
    testMakeDeclaredNamesUnique(false, new ContextualRenamer());
  }

  public void testMakeDeclaredNamesUniqueWithInlineRenamer() {
    testMakeDeclaredNamesUnique(false, new InlineRenamer(() -> "1", "", false));
  }

}

interface Renamer {
  void addDeclaredName(String name);

  String getReplacementName(String oldName);

  boolean stripConstIfReplaced();

  Renamer forChildScope();
}

class ContextualRenamer implements Renamer {
  private Multiset<String> nameUsage;
  private Map<String, String> declarations = Maps.newHashMap();
  private boolean global;

  ContextualRenamer() {
    this.global = true;
    this.nameUsage = HashMultiset.create();
  }

  ContextualRenamer(Multiset<String> nameUsage) {
    this.global = false;
    this.nameUsage = nameUsage;
  }

  @Override
  public void addDeclaredName(String name) {
    if (global) {
      reserveName(name);
    } else {
      if (!declarations.containsKey(name)) {
        int id = incrementNameCount(name);
        String newName = getUniqueName(name, id);
        declarations.put(name, newName);
      }
    }
  }

  @Override
  public String getReplacementName(String oldName) {
    return declarations.get(oldName);
  }

  @Override
  public boolean stripConstIfReplaced() {
    return false;
  }

  @Override
  public Renamer forChildScope() {
    return new ContextualRenamer(nameUsage);
  }

  private void reserveName(String name) {
    nameUsage.setCount(name, 0, 1);
  }

  private int incrementNameCount(String name) {
    return nameUsage.add(name, 1);
  }

  private String getUniqueName(String name, int id) {
    return name + UNIQUE_ID_SEPARATOR + id;
  }
}

class InlineRenamer implements Renamer {
  private Supplier<String> uniqueIdSupplier;
  private String idPrefix;
  private boolean removeConstness;

  InlineRenamer(
      Supplier<String> uniqueIdSupplier,
      String idPrefix,
      boolean removeConstness) {
    this.uniqueIdSupplier = uniqueIdSupplier;
    this.idPrefix = idPrefix;
    this.removeConstness = removeConstness;
  }

  @Override
  public void addDeclaredName(String name) {
    if (!declarations.containsKey(name)) {
      declarations.put(name, getUniqueName(name));
    }
  }

  @Override
  public String getReplacementName(String oldName) {
    return declarations.get(oldName);
  }

  @Override
  public boolean stripConstIfReplaced() {
    return removeConstness;
  }

  @Override
  public Renamer forChildScope() {
    return new InlineRenamer(
        uniqueIdSupplier, idPrefix, removeConstness);
  }

  private String getUniqueName(String name) {
    if (name.isEmpty()) {
      return name;
    }

    if (name.indexOf(ContextualRenamer.UNIQUE_ID_SEPARATOR) != -1) {
      name = name.substring(
          0, name.lastIndexOf(ContextualRenamer.UNIQUE_ID_SEPARATOR));
    }

    // By using the same separator the id will be stripped if it isn't
    // needed when variable renaming is turned off.
    return name + ContextualRenamer.UNIQUE_ID_SEPARATOR
        + idPrefix + uniqueIdSupplier.get();
  }
}