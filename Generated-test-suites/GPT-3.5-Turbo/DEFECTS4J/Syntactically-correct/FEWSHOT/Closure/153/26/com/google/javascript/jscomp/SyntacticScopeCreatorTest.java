import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SyntacticScopeCreatorTest {
  
    @Test
    public void testCreateScope_Function_DeclaredVar() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node n = new Node(Token.FUNCTION);

        creator.createScope(n, null);
        Scope scope = creator.scope;

        assertEquals(null, scope.getParent());
        assertEquals(n, scope.getRootNode());
    }
    
    @Test
    public void testCreateScope_FunctionExpression_DeclaredVar() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node n = new Node(Token.FUNCTION);
        n.putBooleanProp(Node.FUNCTION_EXPRESSION, true);

        creator.createScope(n, null);
        Scope scope = creator.scope;

        assertEquals(null, scope.getParent());
        assertEquals(n, scope.getRootNode());
    }
    
    @Test
    public void testCreateScope_Function_DeclaredVarWithArguments() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node n = new Node(Token.FUNCTION);
        Node fnNameNode = new Node(Token.NAME, "testFunc");
        n.addChildToBack(fnNameNode);
        Node args = new Node(Token.LP);
        Node arg1 = new Node(Token.NAME, "arg1");
        Node arg2 = new Node(Token.NAME, "arg2");
        args.addChildToBack(arg1);
        args.addChildToBack(arg2);
        n.addChildToBack(args);

        creator.createScope(n, null);
        Scope scope = creator.scope;

        assertEquals(null, scope.getParent());
        assertEquals(n, scope.getRootNode());
        assertEquals(3, scope.getVarCount());
        assertEquals("testFunc", scope.getVar(0).getName());
        assertEquals("arg1", scope.getVar(1).getName());
        assertEquals("arg2", scope.getVar(2).getName());
    }
    
    @Test
    public void testCreateScope_CatchBlock_DeclaredVar() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node n = new Node(Token.CATCH);
        Node catchVar = new Node(Token.NAME, "e");
        n.addChildToBack(catchVar);
        Node block = new Node(Token.BLOCK);
        n.addChildToBack(block);

        creator.createScope(n, null);
        Scope scope = creator.scope;

        assertEquals(null, scope.getParent());
        assertEquals(n, scope.getRootNode());
        assertEquals(1, scope.getVarCount());
        assertEquals("e", scope.getVar(0).getName());
    }
    
    @Test
    public void testCreateScope_Script_DeclaredVar() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node n = new Node(Token.SCRIPT);

        creator.createScope(n, null);
        Scope scope = creator.scope;

        assertEquals(null, scope.getParent());
        assertEquals(n, scope.getRootNode());
    }
    
    @Test
    public void testDeclareVar_UniqueName() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Scope scope = new Scope(new Node(Token.SCRIPT), compiler);
        creator.scope = scope;
        Node n = new Node(Token.NAME, "x");
        Node parent = new Node(Token.VAR);
        
        creator.declareVar("x", n, parent, null, null, n);
        
        assertEquals(1, scope.getVarCount());
        assertEquals("x", scope.getVar(0).getName());
    }
    
    @Test
    public void testDeclareVar_DuplicateName() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Scope scope = new Scope(new Node(Token.SCRIPT), compiler);
        creator.scope = scope;
        Node n1 = new Node(Token.NAME, "x");
        Node n2 = new Node(Token.NAME, "x");
        Node parent1 = new Node(Token.VAR);
        Node parent2 = new Node(Token.VAR);
        
        creator.declareVar("x", n1, parent1, null, null, n1);
        creator.declareVar("x", n2, parent2, null, null, n2);
        
        assertEquals(1, scope.getVarCount());
        assertEquals("x", scope.getVar(0).getName());
    }
    
    @Test
    public void testDeclareVar_ReAssignedVar() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Scope scope = new Scope(new Node(Token.SCRIPT), compiler);
        creator.scope = scope;
        Node n1 = new Node(Token.NAME, "x");
        Node n2 = new Node(Token.NAME, "x");
        Node parent1 = new Node(Token.VAR);
        Node parent2 = new Node(Token.VAR);
        
        creator.declareVar("x", n1, parent1, null, null, n1);
        creator.declareVar("x", n2, parent2, null, null, n2);
        
        assertEquals(1, scope.getVarCount());
        assertEquals("x", scope.getVar(0).getName());
    }
    
    @Test
    public void testDeclareVar_FunctionDeclName() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Scope scope = new Scope(new Node(Token.SCRIPT), compiler);
        creator.scope = scope;
        Node n1 = new Node(Token.NAME, "testFunc");
        Node n2 = new Node(Token.NAME, "testFunc");
        Node parent1 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.FUNCTION);
        
        creator.declareVar("testFunc", n1, parent1, null, null, n1);
        creator.declareVar("testFunc", n2, parent2, null, null, n2);
        
        assertEquals(2, scope.getVarCount());
        assertEquals("testFunc", scope.getVar(0).getName());
        assertEquals("testFunc", scope.getVar(1).getName());
    }
    
    @Test
    public void testDeclareVar_ArgumentsVarInGlobalScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Scope scope = new Scope(new Node(Token.SCRIPT), compiler);
        creator.scope = scope;
        Node n = new Node(Token.NAME, "arguments");
        Node parent = new Node(Token.SCRIPT);
        
        creator.declareVar("arguments", n, parent, null, null, n);
        
        assertEquals(1, scope.getVarCount());
        assertEquals("arguments", scope.getVar(0).getName());
    }
    
    @Test
    public void testDeclareVar_ArgumentsVarInFunctionScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Scope scope = new Scope(new Node(Token.FUNCTION), compiler);
        creator.scope = scope;
        Node n = new Node(Token.NAME, "arguments");
        Node parent = new Node(Token.FUNCTION);
        
        creator.declareVar("arguments", n, parent, null, null, n);
        
        assertEquals(1, scope.getVarCount());
        assertEquals("arguments", scope.getVar(0).getName());
    }
    
    @Test
    public void testDefaultRedeclarationHandler_OnRedeclaration() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator syntacticScopeCreator = new SyntacticScopeCreator(compiler);
        Scope scope = new Scope(new Node(Token.SCRIPT), compiler);
        syntacticScopeCreator.scope = scope;
        SyntacticScopeCreator.DefaultRedeclarationHandler handler = syntacticScopeCreator.new DefaultRedeclarationHandler();
        Node n = new Node(Token.NAME, "x");
        Node parent = new Node(Token.VAR);
        Node gramps = new Node(Token.SCRIPT);
        JSType jsType = null;
        Node nodeWithLineNumber = new Node(Token.NAME, "x");
        
        handler.onRedeclaration(scope, "x", n, parent, gramps, nodeWithLineNumber);
        
        assertEquals(1, compiler.getErrorCount());
    }
}