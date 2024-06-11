import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

   @Test
   public void testCreateScope_FunctionDeclaration() {
      AbstractCompiler compiler = new AbstractCompiler();
      SyntacticScopeCreator syntacticScopeCreator = new SyntacticScopeCreator(compiler);
      Node functionNode = new Node(Token.FUNCTION);
      functionNode.putProp(Node.SOURCENAME_PROP, "test.js");
      Node nameNode = new Node(Token.NAME, "foo");
      Node argsNode = new Node(Token.LP);
      Node bodyNode = new Node(Token.BLOCK);

      functionNode.addChildToFront(nameNode);
      nameNode.addChildToFront(argsNode);
      argsNode.addChildToFront(new Node(Token.NAME, "arg1"));
      argsNode.addChildToBack(new Node(Token.NAME, "arg2"));
      argsNode.addChildToBack(new Node(Token.NAME, "arg3"));
      nameNode.addChildToBack(bodyNode);

      Scope parentScope = new Scope();
      Scope scope = syntacticScopeCreator.createScope(functionNode, parentScope);

      assertEquals("test.js", scope.getSourceName());
      assertNotNull(scope.getVar("foo"));
      assertNotNull(scope.getVar("arg1"));
      assertNotNull(scope.getVar("arg2"));
      assertNotNull(scope.getVar("arg3"));
   }

   @Test
   public void testCreateScope_GlobalBlock() {
      AbstractCompiler compiler = new AbstractCompiler();
      SyntacticScopeCreator syntacticScopeCreator = new SyntacticScopeCreator(compiler);
      Node globalBlockNode = new Node(Token.SCRIPT);
      globalBlockNode.putProp(Node.SOURCENAME_PROP, "test.js");
      Node varNode = new Node(Token.VAR);
      varNode.addChildToBack(new Node(Token.NAME, "globalVar"));
      globalBlockNode.addChildToFront(varNode);

      Scope scope = syntacticScopeCreator.createScope(globalBlockNode, null);

      assertEquals("test.js", scope.getSourceName());
      assertNotNull(scope.getVar("globalVar"));
   }

   @Test
   public void testCreateScope_VarDeclaration() {
      AbstractCompiler compiler = new AbstractCompiler();
      SyntacticScopeCreator syntacticScopeCreator = new SyntacticScopeCreator(compiler);
      Node functionNode = new Node(Token.FUNCTION);
      functionNode.putProp(Node.SOURCENAME_PROP, "test.js");
      Node nameNode = new Node(Token.NAME, "foo");
      Node argsNode = new Node(Token.LP);
      Node bodyNode = new Node(Token.BLOCK);
      Node varNode = new Node(Token.VAR);
      varNode.addChildToBack(new Node(Token.NAME, "localVar"));
      bodyNode.addChildToFront(varNode);

      functionNode.addChildToFront(nameNode);
      nameNode.addChildToFront(argsNode);
      argsNode.addChildToFront(new Node(Token.NAME, "arg1"));
      argsNode.addChildToBack(new Node(Token.NAME, "arg2"));
      argsNode.addChildToBack(new Node(Token.NAME, "arg3"));
      nameNode.addChildToBack(bodyNode);

      Scope parentScope = new Scope();
      Scope scope = syntacticScopeCreator.createScope(functionNode, parentScope);

      assertEquals("test.js", scope.getSourceName());
      assertNull(parentScope.getVar("localVar"));
      assertNotNull(scope.getVar("localVar"));
   }
}