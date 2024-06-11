package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.source.SourceFile;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.annotation.Nullable;

/**
 * Unit tests for {@link ScopedAliases} class.
 */
public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    transformationHandler = mock(AliasTransformationHandler.class);
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);
  }

  @Test
  public void testSimpleAlias() {
    SourceFile sourceFile = new SourceFile("Test.java", "test");
    Node root = new Node(sourceFile);
    Node googScopeCall = new Node(new Token(Token.CALL));
    Node googScope = new Node(new Token(Token.NAME, "goog"), googScopeCall);
    Node function = new Node(new Token(Token.FUNCTION));
    Node nameNode = new Node(new Token(Token.NAME, "dom"));
    Node assignNode = new Node(new Token(Token.ASSIGN), nameNode);
    Node body = new Node(new Token(Token.NAME, "createElement"), nameNode);
    Node block = new Node(new Token(Token.LEFT_BRACE), body, new Token(Token.RIGHT_BRACE));
    function.setChild(new Node(new Token(Token.NAME, "Scope"), null), null);
    function.addChild(googScope);
    function.setChild(block, null);
    googScopeCall.setChild(function, null);
    root.setChild(googScopeCall, null);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertEquals(1, traversal.getScopeCalls().size());
    ArgumentCaptor<AliasTransformation> aliasTransformationCaptor =
        ArgumentCaptor.forClass(AliasTransformation.class);
    Mockito.verify(transformationHandler).logAliasTransformation(
        traversal.getSourceFile().getName(), traversal.getSourceRegion(googScopeCall),
        aliasTransformationCaptor.capture());
    AliasTransformation aliasTransformation = aliasTransformationCaptor.getValue();
    assertEquals("dom", aliasTransformation.getAliasName());
    assertEquals("goog.dom", aliasTransformation.getAliasValue());
  }

  @Test
  public void testMultipleAliases() {
    SourceFile sourceFile = new SourceFile("Test.java", "test");
    Node root = new Node(sourceFile);
    Node googScopeCall = new Node(new Token(Token.CALL));
    Node googScope = new Node(new Token(Token.NAME, "goog"), googScopeCall);
    Node function = new Node(new Token(Token.FUNCTION));
    Node nameNode1 = new Node(new Token(Token.NAME, "dom"));
    Node assignNode1 = new Node(new Token(Token.ASSIGN), nameNode1);
    Node body1 = new Node(new Token(Token.NAME, "createElement"), nameNode1);
    Node block1 = new Node(new Token(Token.LEFT_BRACE), body1, new Token(Token.RIGHT_BRACE));
    Node nameNode2 = new Node(new Token(Token.NAME, "Math"));
    Node assignNode2 = new Node(new Token(Token.ASSIGN), nameNode2);
    Node body2 = new Node(new Token(Token.NAME, "round"), nameNode2);
    Node block2 = new Node(new Token(Token.LEFT_BRACE), body2, new Token(Token.RIGHT_BRACE));
    function.setChild(new Node(new Token(Token.NAME, "Scope"), null), null);
    function.addChild(googScope);
    function.setChild(block1, null);
    function.addChild(block2, null);
    googScopeCall.setChild(function, null);
    root.setChild(googScopeCall, null);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertEquals(1, traversal.getScopeCalls().size());
    ArgumentCaptor<AliasTransformation> aliasTransformationCaptor1 =
        ArgumentCaptor.forClass(AliasTransformation.class);
    ArgumentCaptor<AliasTransformation> aliasTransformationCaptor2 =
        ArgumentCaptor.forClass(AliasTransformation.class);
    Mockito.verify(transformationHandler).logAliasTransformation(
        traversal.getSourceFile().getName(), traversal.getSourceRegion(googScopeCall),
        aliasTransformationCaptor1.capture());
    AliasTransformation aliasTransformation1 = aliasTransformationCaptor1.getValue();
    assertEquals("dom", aliasTransformation1.getAliasName());
    assertEquals("goog.dom", aliasTransformation1.getAliasValue());
    Mockito.verify(transformationHandler).logAliasTransformation(
        traversal.getSourceFile().getName(), traversal.getSourceRegion(googScopeCall),
        aliasTransformationCaptor2.capture());
    AliasTransformation aliasTransformation2 = aliasTransformationCaptor2.getValue();
    assertEquals("Math", aliasTransformation2.getAliasName());
    assertEquals("goog.Math", aliasTransformation2.getAliasValue());
  }

  @Test(expected = AssertionError.class)
  public void testInvalidGoogScopeCall() {
    SourceFile sourceFile = new SourceFile("Test.java", "test");
    Node root = new Node(sourceFile);
    Node googScopeCall = new Node(new Token(Token.CALL));
    Node googScope = new Node(new Token(Token.NAME, "goog"), googScopeCall);
    Node function = new Node(new Token(Token.FUNCTION), null);
    Node nameNode = new Node(new Token(Token.NAME, "dom"));
    Node assignNode = new Node(new Token(Token.ASSIGN), nameNode);
    Node body = new Node(new Token(Token.NAME, "createElement"), nameNode);
    Node block = new Node(new Token(Token.LEFT_BRACE), body, new Token(Token.RIGHT_BRACE));
    function.setChild(new Node(new Token(Token.NAME, "Scope"), null), null);
    function.addChild(googScope);
    function.setChild(block, null);
    Node localVarNode = new Node(new Token(Token.NAME, "dom"));
    Node assignLocalVarNode = new Node(new Token(Token.ASSIGN), localVarNode);
    Node statement = new Node(new Token(Token.NAME, "log"), new Node(Token.STRING, "dom"), assignLocalVarNode);
    node.addChild(statement, null);
    googScopeCall.setChild(function, null);
    root.setChild(googScopeCall, null);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    Mockito.verify(compiler, Mockito.times(0)).report(traversal.makeError(AssignNode, GOOG_SCOPE_REFERENCES_THIS));
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeWithReturnStatement() {
    SourceFile sourceFile = new SourceFile("Test.java", "test");
    Node root = new Node(sourceFile);
    Node googScopeCall = new Node(new Token(Token.CALL));
    Node googScope = new Node(new Token(Token.NAME, "goog"), googScopeCall);
    Node function = new Node(new Token(Token.FUNCTION), null);
    Node returnNode = new Node(new Token(Token.RETURN));
    Node block = new Node(new Token(Token.LEFT_BRACE), null, new Token(Token.RIGHT_BRACE));
    function.setChild(new Node(new Token(Token.NAME, "Scope"), null), null);
    function.addChild(googScope);
    function.setChild(block, null);
    googScopeCall.setChild(function, null);
    root.setChild(googScopeCall, null);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    Mockito.verify(compiler, Mockito.times(1)).report(traversal.makeError(ReturnNode, GOOG_SCOPE_USES_RETURN));
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeWithThrowStatement() {
    SourceFile sourceFile = new SourceFile("Test.java", "test");
    Node root = new Node(sourceFile);
    Node googScopeCall = new Node(new Token(Token.CALL));
    Node googScope = new Node(new Token(Token.NAME, "goog"), googScopeCall);
    Node function = new Node(new Token(Token.FUNCTION), null);
    Node throwNode = new Node(new Token(Token.THROW), null);
    Node block = new Node(new Token(Token.LEFT_BRACE), null, new Token(Token.RIGHT_BRACE));
    function.setChild(new Node(new Token(Token.NAME, "Scope"), null), null);
    function.addChild(googScope);
    function.setChild(block, null);
    googScopeCall.setChild(function, null);
    root.setChild(googScopeCall, null);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    Mockito.verify(compiler, Mockito.times(1)).report(traversal.makeError(ThrowNode, GOOG_SCOPE_USES_THROW));
  }
}