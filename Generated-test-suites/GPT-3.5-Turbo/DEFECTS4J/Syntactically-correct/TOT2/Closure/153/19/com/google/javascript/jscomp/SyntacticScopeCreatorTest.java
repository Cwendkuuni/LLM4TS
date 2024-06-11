import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

    @Test
    public void testCreateScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
        Scope parent = new Scope(new Node(Token.ROOT), compiler);
        Node n = new Node(Token.FUNCTION);
        
        Scope scope = scopeCreator.createScope(n, parent);
        
        assertNotNull(scope);
        assertNull(scope.getParent());
        assertNotNull(scope.getRootNode());
        assertEquals(compiler, scope.getCompiler());
    }

    @Test
    public void testScanRoot() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);
        Node n = new Node(Token.FUNCTION);
        Scope parent = new Scope(new Node(Token.ROOT), compiler);

        scopeCreator.createScope(n, parent);
        
        // Add test cases here for scanning root methods
    }

    // Add more test cases for other public methods in SyntacticScopeCreator

}