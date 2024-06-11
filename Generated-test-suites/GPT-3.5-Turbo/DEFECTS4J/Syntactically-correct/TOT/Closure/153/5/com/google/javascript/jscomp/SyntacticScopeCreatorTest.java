import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Scope;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {

    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
    }

    @Test
    public void testCreateScope_globalBlock() {
        Node rootNode = new Node(Token.SCRIPT);
        rootNode.putProp(Node.SOURCENAME_PROP, "test.js");
        ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
        
        Scope scope = scopeCreator.createScope(rootNode, null);
        
        assertEquals(null, scope.getParent());
        assertEquals("test.js", scope.getSourceName());
    }

    @Test
    public void testCreateScope_functionDeclaration() {
        Node rootNode = new Node(Token.FUNCTION);
        rootNode.putProp(Node.SOURCENAME_PROP, "test.js");
        Node fnNameNode = new Node(Token.NAME, "testFunction");
        rootNode.addChildToFront(fnNameNode);
        ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
        
        Scope scope = scopeCreator.createScope(rootNode, null);
        
        assertEquals(null, scope.getParent());
        assertEquals("test.js", scope.getSourceName());
        assertTrue(scope.isDeclared("testFunction", false));
    }

    @Test
    public void testCreateScope_functionExpression() {
        Node rootNode = new Node(Token.FUNCTION);
        rootNode.putProp(Node.SOURCENAME_PROP, "test.js");
        Node fnNameNode = new Node(Token.NAME, "");
        rootNode.addChildToFront(fnNameNode);
        ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
        
        Scope scope = scopeCreator.createScope(rootNode, null);
        
        assertEquals(null, scope.getParent());
        assertEquals("test.js", scope.getSourceName());
        assertFalse(scope.isDeclared("", false));
    }

    @Test
    public void testCreateScope_catchBlock() {
        Node rootNode = new Node(Token.CATCH);
        rootNode.putProp(Node.SOURCENAME_PROP, "test.js");
        Node varNode = new Node(Token.NAME, "error");
        rootNode.addChildToFront(varNode);
        ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
        
        Scope scope = scopeCreator.createScope(rootNode, null);
        
        assertEquals(null, scope.getParent());
        assertEquals("test.js", scope.getSourceName());
        assertTrue(scope.isDeclared("error", false));
    }

    @Test
    public void testCreateScope_multipleVarsInVarStatement() {
        Node rootNode = new Node(Token.VAR);
        rootNode.putProp(Node.SOURCENAME_PROP, "test.js");
        Node var1 = new Node(Token.NAME, "x");
        Node var2 = new Node(Token.NAME, "y");
        rootNode.addChildToFront(var1);
        rootNode.addChildToBack(var2);
        ScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
        
        Scope scope = scopeCreator.createScope(rootNode, null);
        
        assertEquals(null, scope.getParent());
        assertEquals("test.js", scope.getSourceName());
        assertTrue(scope.isDeclared("x", false));
        assertTrue(scope.isDeclared("y", false));
    }

}