import static org.junit.Assert.*;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

    @Test
    public void testCreateScope_withNullParent_returnsScope() {
        // Setup
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node n = new Node();
        Scope parent = null;

        // Execute
        Scope result = creator.createScope(n, parent);

        // Verify
        assertNotNull(result);
    }

    @Test
    public void testCreateScope_withNonNullParent_returnsScope() {
        // Setup
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node n = new Node();
        Scope parent = new Scope();

        // Execute
        Scope result = creator.createScope(n, parent);

        // Verify
        assertNotNull(result);
    }

    @Test
    public void testScanVars_withVarNode_declaresVariables() {
        // Setup
        SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
        Node n = new Node(Token.VAR);
        n.addChildToBack(new Node(Token.NAME, "x"));
        n.addChildToBack(new Node(Token.NAME, "y"));
        n.addChildToBack(new Node(Token.NAME, "z"));
        Node parent = new Node();

        // Execute
        creator.scanVars(n, parent);
        Scope scope = creator.getScope();

        // Verify
        assertTrue(scope.isDeclared("x", false));
        assertTrue(scope.isDeclared("y", false));
        assertTrue(scope.isDeclared("z", false));
    }

    @Test
    public void testScanVars_withFunctionNode_doesNotDeclareVariables() {
        // Setup
        SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
        Node n = new Node(Token.FUNCTION);

        // Execute
        creator.scanVars(n, null);
        Scope scope = creator.getScope();

        // Verify
        assertFalse(scope.isDeclared("x", false));
        assertFalse(scope.isDeclared("y", false));
        assertFalse(scope.isDeclared("z", false));
    }

    @Test
    public void testScanVars_withScriptNode_setsSourceName() {
        // Setup
        SyntacticScopeCreator creator = new SyntacticScopeCreator(new AbstractCompiler());
        Node n = new Node(Token.SCRIPT);
        n.putProp(Node.SOURCENAME_PROP, "test");

        // Execute
        creator.scanVars(n, null);
        String sourceName = creator.getSourceName();

        // Verify
        assertEquals("test", sourceName);
    }
}