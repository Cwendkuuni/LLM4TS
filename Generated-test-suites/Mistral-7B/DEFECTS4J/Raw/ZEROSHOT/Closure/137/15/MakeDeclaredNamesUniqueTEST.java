import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

public class MakeDeclaredNamesUniqueTest {

  private static final String ORIGINAL_NAME = "testName";
  private static final String RENAMED_NAME_1 = "testName$$0";
  private static final String RENAMED_NAME_2 = "testName$$1";

  @org.junit.Test
  public void makeDeclaredNamesUniqueTest() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);

    Scope scope = mock(Scope.class);
    when(scope.getVar(ORIGINAL_NAME)).thenReturn(new Var(ORIGINAL_NAME, null, null, null, null));
    when(scope.isDeclared(RENAMED_NAME_1, true)).thenReturn(false);
    when(scope.isDeclared(RENAMED_NAME_2, true)).thenReturn(false);
    when(scope.getRootNode()).thenReturn(Node.newTextNode(new Token(Token.PROGRAM, 0, 0)));

    makeDeclaredNamesUnique.enterScope(nodeTraversal);
    Node nameNode = Node.newNameNode("testName");
    makeDeclaredNamesUnique.findDeclaredNames(nameNode.getParentNode(), nameNode, makeDeclaredNamesUnique);
    makeDeclaredNamesUnique.exitScope(nodeTraversal);

    assertEquals(RENAMED_NAME_1, nameNode.getString());

    makeDeclaredNamesUnique.enterScope(nodeTraversal);
    Node functionNode = Node.newFunctionNode(
        Token.FUNCTION,
        Token.EMPTY,
        Lists.newArrayList(Node.newNameNode("testName")),
        Lists.newArrayList(Node.newBlock(Node.newTextNode(new Token(Token.END, 0, 0)))));
    makeDeclaredNamesUnique.findDeclaredNames(functionNode, null, makeDeclaredNamesUnique);
    makeDeclaredNamesUnique.exitScope(nodeTraversal);

    assertEquals(RENAMED_NAME_2, functionNode.getFirstChild().getFirstChild().getString());
  }

  @org.junit.Test
  public void shouldNotRenameGlobalNames() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node globalNode = Node.newNameNode("testGlobalName");

    makeDeclaredNamesUnique.enterScope(nodeTraversal);
    makeDeclaredNamesUnique.visit(nodeTraversal, globalNode, null);
    makeDeclaredNamesUnique.exitScope(nodeTraversal);

    assertEquals(ORIGINAL_NAME, globalNode.getString());
  }

  @org.junit.Test
  public void shouldKeepConstNames() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node constNode = Node.newNameNode("constTestName");
    Node constNodeClone = Node.newNameNode("constTestName");

    makeDeclaredNamesUnique.visit(nodeTraversal, constNode, null);
    constNode.setProp(Node.IS_CONSTANT_NAME, Boolean.TRUE);
    makeDeclaredNamesUnique.visit(nodeTraversal, constNodeClone, null);

    assertEquals(ORIGINAL_NAME, constNode.getString());
    assertEquals(ORIGINAL_NAME, constNodeClone.getString());
  }

  @org.junit.Test
  public void shouldNotRenameNamesWithinFunctions() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node functionNode = Node.newFunctionNode(
        Token.FUNCTION,
        Token.EMPTY,
        Lists.newArrayList(Node.newNameNode("testName")),
        Lists.newArrayList(Node.newBlock(Node.newTextNode(new Token(Token.END, 0, 0)))));
    Node innerFunctionNode = Node.newFunctionNode(
        Token.FUNCTION,
        Token.EMPTY,
        Lists.newArrayList(Node.newNameNode("testName")),
        Lists.newArrayList(Node.newBlock(Node.newTextNode(new Token(Token.END, 0, 0)))));

    makeDeclaredNamesUnique.enterScope(nodeTraversal);
    makeDeclaredNamesUnique.findDeclaredNames(functionNode, null, makeDeclaredNamesUnique);
    makeDeclaredNamesUnique.exitScope(nodeTraversal);

    makeDeclaredNamesUnique.enterScope(nodeTraversal);
    makeDeclaredNamesUnique.findDeclaredNames(innerFunctionNode, null, makeDeclaredNamesUnique);
    makeDeclaredNamesUnique.exitScope(nodeTraversal);

    assertEquals(ORIGINAL_NAME, functionNode.getFirstChild().getFirstChild().getString());
    assertEquals(ORIGINAL_NAME, innerFunctionNode.getFirstChild().getFirstChild().getString());
  }

  @org.junit.Test
  public void shouldRenameNamesWithinCatchBlocks() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node catchNode = Node.newCatchNode(
        Token.CATCH,
        Node.newNameNode("testName"),
        Node.newBlock(Node.newTextNode(new Token(Token.END, 0, 0))));

    makeDeclaredNamesUnique.enterScope(nodeTraversal);
    makeDeclaredNamesUnique.findDeclaredNames(catchNode, null, makeDeclaredNamesUnique);
    makeDeclaredNamesUnique.exitScope(nodeTraversal);

    assertEquals(RENAMED_NAME_1, catchNode.getFirstChild().getString());
  }
}