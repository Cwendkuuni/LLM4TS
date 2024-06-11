import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserUtils;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class FunctionRewriterTest {
  @Test
  public void testEmptyFunctionReduction() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Node parseSingleStatement(int token, int lineNumber, int charPositionInLine) {
        return ParserUtils.parseStatement(token, lineNumber, charPositionInLine, null);
      }
    };

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node emptyFunctionNode = new Node(Token.FUNCTION, 1, 1);
    Node rootNode = new Node(Token.BLOCK, 1, 1);
    rootNode.addChildToBack(emptyFunctionNode);

    functionRewriter.process(null, rootNode);

    Node expectedEmptyFunctionHelper = NodeFactory.createCall(
        "JSCompiler_emptyFn", null);

    Node actualEmptyFunctionHelper = findNodeByType(rootNode, Token.CALL);
    assertNotNull(actualEmptyFunctionHelper);
    assertNotNull(actualEmptyFunctionHelper.getFirstChild());
    assertNotNull(actualEmptyFunctionHelper.getFirstChild().getString());
    assertNotNull(actualEmptyFunctionHelper.getFirstChild().getString().equals(
        "JSCompiler_emptyFn"));
    assertNull(emptyFunctionNode.getLastChild());
  }

  @Test
  public void testIdentityFunctionReduction() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Node parseSingleStatement(int token, int lineNumber, int charPositionInLine) {
        return ParserUtils.parseStatement(token, lineNumber, charPositionInLine, null);
      }
    };

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String identityFunctionSource = "function identityFunction(arg) { return arg; }";
    Node identityFunctionNode = ParserUtils.parseCompilationUnit(identityFunctionSource);

    Node rootNode = new Node(Token.BLOCK, 1, 1);
    rootNode.addChildToBack(identityFunctionNode);

    functionRewriter.process(null, rootNode);

    Node expectedIdentityFunctionHelper = NodeFactory.createCall(
        "JSCompiler_identityFn", null);
    Node expectedIdentityFunctionHelperArgNode = NodeFactory.newIdent("identityFunction_value");

    Node actualIdentityFunctionHelper = findNodeByType(rootNode, Token.CALL);
    assertNotNull(actualIdentityFunctionHelper);
    assertNotNull(actualIdentityFunctionHelper.getFirstChild());
    assertNotNull(actualIdentityFunctionHelper.getFirstChild().getFirstChild());
    assertNotNull(actualIdentityFunctionHelper.getFirstChild().getFirstChild().getString());
    assertNotNull(actualIdentityFunctionHelper.getFirstChild().getLastChild());
    assertNotNull(actualIdentityFunctionHelper.getFirstChild().getLastChild().getString());
    assertNotNull(actualIdentityFunctionHelper.getLastChild());
    assertNull(identityFunctionNode.getLastChild());

    Node actualIdentityFunctionHelperArg = findNodeByType(actualIdentityFunctionHelper, Token.GETPROP);
    Node expectedIdentityFunctionHelperArg = NodeFactory.newIdent("identityFunction_value");
    assertNotNull(actualIdentityFunctionHelperArg);
    assertNotNull(actualIdentityFunctionHelperArg.getFirstChild());
    assertNotNull(actualIdentityFunctionHelperArg.getFirstChild().getString());
    assertNotNull(expectedIdentityFunctionHelperArg.getString().equals(
        actualIdentityFunctionHelperArg.getFirstChild().getString()));
  }

  @Test
  public void testReturnConstantReduction() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Node parseSingleStatement(int token, int lineNumber, int charPositionInLine) {
        return ParserUtils.parseStatement(token, lineNumber, charPositionInLine, null);
      }
    };

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String returnConstantSource = "function returnConstantFunction() { return 42; }";
    Node returnConstantNode = ParserUtils.parseCompilationUnit(returnConstantSource);

    Node rootNode = new Node(Token.BLOCK, 1, 1);
    rootNode.addChildToBack(returnConstantNode);

    functionRewriter.process(null, rootNode);

    Node expectedReturnConstantHelper = NodeFactory.createCall(
        "JSCompiler_returnArg", new Node(Token.INT_LITERAL, 42, 1));

    Node actualReturnConstantHelper = findNodeByType(rootNode, Token.CALL);
    assertNotNull(actualReturnConstantHelper);
    assertNotNull(actualReturnConstantHelper.getFirstChild());
    assertNotNull(actualReturnConstantHelper.getFirstChild().getString());
    assertNotNull(actualReturnConstantHelper.getFirstChild().getString().equals(
        "JSCompiler_returnArg"));
    assertNotNull(actualReturnConstantHelper.getLastChild());
    assertNull(returnConstantNode.getLastChild());
  }

  @Test
  public void testGetterReduction() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Node parseSingleStatement(int token, int lineNumber, int charPositionInLine) {
        return ParserUtils.parseStatement(token, lineNumber, charPositionInLine, null);
      }
    };

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String getterSource = "function getterFunction(obj) { return obj.prop; }";
    Node getterNode = ParserUtils.parseCompilationUnit(getterSource);

    Node rootNode = new Node(Token.BLOCK, 1, 1);
    Node objNode = new Node(Token.OBJECT_LITERAL, 1, 1);
    objNode.addProperty("prop", new Node(Token.STRING_LITERAL, "test", 1));
    rootNode.addChildToBack(objNode);
    rootNode.addChildToBack(getterNode);

    functionRewriter.process(null, rootNode);

    Node expectedGetterHelper = NodeFactory.createCall(
        "JSCompiler_get", new Node(Token.STRING_LITERAL, "prop", 1));

    Node actualGetterHelper = findNodeByType(rootNode, Token.CALL);
    assertNotNull(actualGetterHelper);
    assertNotNull(actualGetterHelper.getFirstChild());
    assertNotNull(actualGetterHelper.getFirstChild().getString());
    assertNotNull(actualGetterHelper.getFirstChild().getString().equals(
        "JSCompiler_get"));
    assertNotNull(actualGetterHelper.getLastChild());
    assertNull(getterNode.getLastChild());
  }

  @Test
  public void testSetterReduction() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Node parseSingleStatement(int token, int lineNumber, int charPositionInLine) {
        return ParserUtils.parseStatement(token, lineNumber, charPositionInLine, null);
      }
    };

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String setterSource = "function setterFunction(obj, value) { obj.prop = value; }";
    Node setterNode = ParserUtils.parseCompilationUnit(setterSource);

    Node rootNode = new Node(Token.BLOCK, 1, 1);
    Node objNode = new Node(Token.OBJECT_LITERAL, 1, 1);
    objNode.addProperty("prop", new Node(Token.NULL, 1, 1));
    rootNode.addChildToBack(objNode);
    rootNode.addChildToBack(setterNode);

    functionRewriter.process(null, rootNode);

    Node expectedSetterHelper = NodeFactory.createCall(
        "JSCompiler_set", new Node(Token.STRING_LITERAL, "prop", 1),
        new Node(Token.NULL, 1, 1));

    Node actualSetterHelper = findNodeByType(rootNode, Token.CALL);
    assertNotNull(actualSetterHelper);
    assertNotNull(actualSetterHelper.getFirstChild());
    assertNotNull(actualSetterHelper.getFirstChild().getString());
    assertNotNull(actualSetterHelper.getFirstChild().getString().equals(
        "JSCompiler_set"));
    assertNotNull(actualSetterHelper.getLastChild());
    assertNull(setterNode.getLastChild());
  }

  private Node findNodeByType(Node node, int type) {
    if (node == null) {
      return null;
    }

    if (node.getType() == type) {
      return node;
    }

    for (int i = 0; i < node.getNumChildren(); i++) {
      Node child = findNodeByType(node.getChild(i), type);
      if (child != null) {
        return child;
      }
    }

    return null;
  }
}