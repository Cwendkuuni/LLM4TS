import static org.junit.Assert.*;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

    @Test
    public void testCreateScope() {
        // Test creating scope with parent
        SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
        Node node = new Node(Token.FUNCTION);
        Scope parentScope = new Scope(node, new AbstractCompiler());
        Scope scope = creator.createScope(node, parentScope);
        assertNotNull(scope);
        assertEquals(parentScope, scope.getParent());
    }

    @Test
    public void testScanVars() {
        // Test scanning variable declarations under a node
        SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
        Node node = new Node(Token.VAR);
        Node childNode = new Node(Token.NAME, "x");
        node.addChildToBack(childNode);
        creator.scanVars(node, null);
        Scope scope = creator.createScope(node, null);
        assertNotNull(scope.getVar("x"));
    }

    @Test
    public void testDeclareVar() {
        // Test declaring a variable
        SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
        Node nameNode = new Node(Token.NAME, "x");
        Node parentNode = new Node(Token.CATCH);
        Node grandparentNode = new Node(Token.SCRIPT);
        creator.declareVar("x", nameNode, parentNode, grandparentNode, null, new Node(Token.FUNCTION));
        Scope scope = creator.createScope(new Node(Token.FUNCTION), new Scope(new Node(Token.SCRIPT), new AbstractCompiler()));
        assertNotNull(scope.getVar("x"));
    }

}