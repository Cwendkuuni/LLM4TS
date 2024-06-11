import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;
import org.junit.Before;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    safeNameIdSupplier = mock(Supplier.class);
  }

  @Test
  public void testInlineObjectLiterals() {
    CompilerPass inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Scope scope = mock(Scope.class);
    when(scope.getRootNode()).thenReturn(createNode("root"));
    when(scope.getVars()).thenReturn(createIterator(createVar("a")));

    Node externs = createNode("externs");
    Node root = createNode("root");

    inlineObjectLiterals.process(externs, root);

    verify(compiler, times(1)).reportCodeChange();
  }

  @Test
  public void testInlineObjectLiteralsWithInlineForbiddenVariable() {
    CompilerPass inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Scope scope = mock(Scope.class);
    when(scope.getRootNode()).thenReturn(createNode("root"));
    when(scope.getVars()).thenReturn(createIterator(createVar("a"), createVar("forbidden")));

    Node externs = createNode("externs");
    Node root = createNode("root");

    inlineObjectLiterals.process(externs, root);

    verify(compiler, never()).reportCodeChange();

    Var forbidden = getVarByName("forbidden", scope);
    assertNull(forbidden);
  }

  @Test
  public void testInlineObjectLiteralsWithInvalidObjectLiteral() {
    CompilerPass inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Scope scope = mock(Scope.class);
    when(scope.getRootNode()).thenReturn(createNode("root"));
    when(scope.getVars()).thenReturn(createIterator(createVar("a")));

    Node externs = createNode("externs");
    Node root = createNode(
        "root",
        createNode("var a = function() {}"),
        createNode("{ b: a } = {}"));

    inlineObjectLiterals.process(externs, root);

    verify(compiler, times(1)).reportCodeChange();

    Node aFunction = getNodeByName("a", scope);
    Node object = getNodeByName("{ b: a}", scope);
    Node b = getNodeByName("b", scope);

    assertNull(object);
    assertNotNull(aFunction);
    assertEquals(Node.OP_ASSIGN, b.getParent().getType());
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReference() {
    CompilerPass inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Scope scope = mock(Scope.class);
    when(scope.getRootNode()).thenReturn(createNode("root"));
    when(scope.getVars()).thenReturn(createIterator(createVar("a")));

    Node externs = createNode("externs");
    Node root = createNode(
        "root",
        createNode("var a = { a: a }"));

    inlineObjectLiterals.process(externs, root);

    verify(compiler, times(1)).reportCodeChange();

    Node a = getNodeByName("a", scope);

    assertNotNull(a);
    assertEquals(Node.OP_ASSIGN, a.getParent().getType());
  }

  @Test
  public void testInlineObjectLiteralsWithPropertyFunction() {
    CompilerPass inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Scope scope = mock(Scope.class);
    when(scope.getRootNode()).thenReturn(createNode("root"));
    when(scope.getVars()).thenReturn(createIterator(createVar("a")));

    Node externs = createNode("externs");
    Node root = createNode(
        "root",
        createNode("var a = { get b() { return a; } }"));

    inlineObjectLiterals.process(externs, root);

    verify(compiler, times(1)).reportCodeChange();

    Node a = getNodeByName("a", scope);

    assertNotNull(a);
    assertNotNull(a.getFirstChild());
    assertEquals(Token.GETPROP, a.getFirstChild().getType());
  }

  @Test
  public void testInlineObjectLiteralsWithInitValue() {
    CompilerPass inlineObjectLiterals =
        new InlineObjectLiterals(compiler, safeNameIdSupplier);

    Scope scope = mock(Scope.class);
    when(scope.getRootNode()).thenReturn(createNode("root"));
    when(scope.getVars()).thenReturn(createIterator(createVar("a")));

    Node externs = createNode("externs");
    Node root = createNode(
        "root",
        createNode("var a = { b: 1 }"));

    inlineObjectLiterals.process(externs, root);

    verify(compiler, times(1)).reportCodeChange();

    Node a = getNodeByName("a", scope);
    Node b = getNodeByName("b", scope);

    assertNotNull(a);
    assertNotNull(b);
    assertEquals(Node.OP_ASSIGN, a.getParent().getType());
  }

  private Node createNode(String... children) {
    Node node = new Node(Token.NULL);
    for (String child : children) {
      node.addChild(new Node(Token.fromString(child)));
    }
    return node;
  }

  private Node createNode(int type, String... children) {
    Node node = new Node(type);
    for (String child : children) {
      node.addChild(new Node(Token.fromString(child)));
    }
    return node;
  }

  private Iterator<Var> createIterator(Var... vars) {
    return mock(Iterator.class);
  }

  private Var createVar(String name) {
    return mock(Var.class);
  }

  private Node getNodeByName(String name, Scope scope) {
    return scope.getVar(name);
  }

  private Var getVarByName(String name, Scope scope) {
    return scope.getVar(name);
  }
}