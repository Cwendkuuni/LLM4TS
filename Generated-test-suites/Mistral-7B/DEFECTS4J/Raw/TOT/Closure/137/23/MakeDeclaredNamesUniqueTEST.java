package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class MakeDeclaredNamesUniqueTest {

  private AbstractCompiler compiler;
  private Node rootNode;
  private NodeTraversal traversal;
  private MakeDeclaredNamesUnique renamer;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    rootNode = createTestNode();
    traversal = new NodeTraversal(compiler, null, null, null, null);
    traversal.setRootNode(rootNode);
    renamer = new MakeDeclaredNamesUnique();
  }

  @Test
  public void testSimple() {
    traversal.traverse(renamer);

    Var var1 = new Var("a", null, rootNode.getFirstChild(), null);
    Var var2 = new Var("a", null, rootNode.getLastChild().getNext(), null);

    ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);

    // The first declaration of 'a' should not be renamed.
    assertThat(renamer.rootRenamer.getReplacementName(var1.name)).isEqualTo("a");

    // The second declaration of 'a' should be renamed to 'a__0'.
    assertThat(renamer.rootRenamer.getReplacementName(var2.name)).isEqualTo("a__0");
  }

  @Test
  public void testFunction() {
    Node functionNode = new Node(Token.FUNCTION, null, null, null, null);
    Node functionBody = new Node(Token.BLOCK, null, null, null, null);

    Node functionDeclaration = new Node(Token.FUNCTION, null, null, null, null);
    functionDeclaration.setChild(new Node(Token.NAME, null, null, null, "myFunction"));
    functionDeclaration.setLastChild(functionBody);

    rootNode.setLastChild(functionNode);
    functionNode.setChild(functionDeclaration);

    Var functionParam = new Var("param", null, functionDeclaration.getLastChild(), null);
    Var functionLocal = new Var("local", null, functionBody.getFirstChild(), null);

    traversal.traverse(renamer);

    // 'param' in the function declaration should be renamed to 'param__0'.
    assertThat(renamer.rootRenamer.getReplacementName(functionParam.name)).isEqualTo("param__0");

    // 'local' in the function body should be renamed to 'local__0'.
    assertThat(renamer.rootRenamer.getReplacementName(functionLocal.name)).isEqualTo("local__0");
  }

  @Test
  public void testCatch() {
    Node catchNode = new Node(Token.CATCH, null, null, null, null);
    Node catchNameNode = new Node(Token.NAME, null, null, null, "exception");

    Node functionBody = new Node(Token.BLOCK, null, null, null, null);
    rootNode.setLastChild(functionBody);

    Node tryStatement = new Node(Token.TRY, null, null, null, null);
    Node tryBody = new Node(Token.BLOCK, null, null, null, null);
    functionBody.setChild(tryStatement);
    tryStatement.setChild(tryBody);

    catchNode.setChild(catchNameNode);
    tryBody.setLastChild(catchNode);

    Var catchVariable = new Var("exception", null, catchNameNode, null);

    traversal.traverse(renamer);

    // 'exception' in the catch block should be renamed to 'exception__0'.
    assertThat(renamer.rootRenamer.getReplacementName(catchVariable.name)).isEqualTo("exception__0");
  }

  @Test
  public void testGlobal() {
    rootNode.setType(Token.PROGRAM);
    rootNode.setFirstChild(new Node(Token.NAME, null, null, null, "globalVar"));

    traversal.traverse(renamer);

    // 'globalVar' should not be renamed.
    assertThat(renamer.rootRenamer.getReplacementName("globalVar")).isEqualTo("globalVar");
  }

  @Test(expected = AssertionError.class)
  public void testInvalidName() {
    Node invalidNameNode = new Node(Token.NAME, null, null, null, "1variable");

    traversal.traverse(renamer);

    // An invalid name (starting with a digit) should cause an error.
    renamer.rootRenamer.getReplacementName(invalidNameNode.getString());
  }

  private Node createTestNode() {
    Node rootNode = new Node(Token.PROGRAM, null, null, null, null);

    Node var1 = new Node(Token.VAR, null, null, null, null);
    var1.setChild(new Node(Token.NAME, null, null, null, "a"));
    rootNode.setLastChild(var1);

    Node var2 = new Node(Token.VAR, null, null, null, null);
    var2.setChild(new Node(Token.NAME, null, null, null, "a"));
    rootNode.setLastChild(var2);

    return rootNode;
  }
}