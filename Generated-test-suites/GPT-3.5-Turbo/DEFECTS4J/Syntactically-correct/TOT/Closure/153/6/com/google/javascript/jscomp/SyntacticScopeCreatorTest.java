import static org.junit.Assert.*;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

  @Test
  public void testCreateScope_GlobalBlock_ReturnsGlobalScope() {
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Scope scope = creator.createScope(Node.newString(Token.SCRIPT, ""), null);
    assertNull(scope.getParent());
  }

  @Test
  public void testCreateScope_FunctionDeclaration_ReturnsFunctionScope() {
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node functionNode = Node.newString(Token.FUNCTION, "");
    functionNode.addChildToBack(Node.newString(Token.NAME, "add"));
    Node args = Node.newString(Token.LP, "");
    args.addChildToBack(Node.newString(Token.NAME, "a"));
    args.addChildToBack(Node.newString(Token.NAME, "b"));
    functionNode.addChildToBack(args);
    Node body = Node.newString(Token.BLOCK, "");
    functionNode.addChildToBack(body);
    Scope scope = creator.createScope(functionNode, null);
    assertEquals("add", scope.getVar("add").getName());
  }

  @Test
  public void testCreateScope_FunctionExpression_ReturnsFunctionScope() {
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node functionNode = Node.newString(Token.FUNCTION, "");
    Node args = Node.newString(Token.LP, "");
    args.addChildToBack(Node.newString(Token.NAME, "a"));
    args.addChildToBack(Node.newString(Token.NAME, "b"));
    functionNode.addChildToBack(args);
    Node body = Node.newString(Token.BLOCK, "");
    functionNode.addChildToBack(body);
    Scope scope = creator.createScope(functionNode, null);
    assertEquals("", scope.getVar("").getName());
  }
  
  @Test
  public void testCreateScope_BleedFunctionNameIntoScope() {
      SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
      Node functionNode = Node.newString(Token.FUNCTION, "");
      Node functionName = Node.newString(Token.NAME, "add");
      functionNode.addChildToBack(functionName);
      Node args = Node.newString(Token.LP, "");
      args.addChildToBack(Node.newString(Token.NAME, "a"));
      args.addChildToBack(Node.newString(Token.NAME, "b"));
      functionNode.addChildToBack(args);
      Node body = Node.newString(Token.BLOCK, "");
      functionNode.addChildToBack(body);
      Scope scope = creator.createScope(functionNode, null);
      assertEquals("add", scope.getVar("add").getName());
  }

  @Test
  public void testCreateScope_CatchBlock_ReturnsCatchScope() {
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node catchNode = Node.newString(Token.CATCH, "");
    Node catchVar = Node.newString(Token.NAME, "e");
    catchNode.addChildToBack(catchVar);
    Node block = Node.newString(Token.BLOCK, "");
    catchNode.addChildToBack(block);
    Scope scope = creator.createScope(catchNode, null);
    assertEquals("e", scope.getVar("e").getName());
  }

  @Test
  public void testCreateScope_VariableDeclaration_InnerScopeOverridesOuterScope() {
    SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
    Node varNode = Node.newString(Token.VAR, "");
    varNode.addChildToBack(Node.newString(Token.NAME, "a"));
    Node innerVar = Node.newString(Token.VAR, "");
    innerVar.addChildToBack(Node.newString(Token.NAME, "a"));
    Node functionNode = Node.newString(Token.FUNCTION, "");
    functionNode.addChildToBack(innerVar);
    functionNode.addChildToBack(varNode);
    Scope scope = creator.createScope(functionNode, null);
    assertEquals(innerVar, scope.getVar("a").getParentNode());
  }

}