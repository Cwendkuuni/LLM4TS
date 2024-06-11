import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {

    @Test
    public void testCreateScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

        Node n = new Node(Token.FUNCTION);
        Scope parent = null;

        Scope scope = scopeCreator.createScope(n, parent);
        
        assertNotNull(scope);
    }

    @Test
    public void testScanRoot() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

        Node n = new Node(Token.SCRIPT);
        Scope parent = null;

        scopeCreator.createScope(n, parent);
        
        // Verify scanRoot logic here
    }

    @Test
    public void testScanVars() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

        Node n = new Node(Token.VAR);
        Node parent = new Node(Token.SCRIPT);

        scopeCreator.scanVars(n, parent);
        
        // Verify scanVars logic here
    }

    // Additional test cases for edge cases and exception handling scenarios can be added here

}