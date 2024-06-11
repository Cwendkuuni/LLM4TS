import org.junit.*;
import static org.junit.Assert.*;

public class SyntacticScopeCreatorTest {

    @Test
    public void testCreateScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        
        // Test case 1: Create scope with parent
        Node n1 = new Node(Token.FUNCTION);
        Scope parent1 = new Scope(null, null);
        Scope scope1 = creator.createScope(n1, parent1);
        
        assertEquals(parent1, scope1.getParent());
        assertEquals(null, scope1.getRoot());
        
        // Test case 2: Create scope without parent
        Node n2 = new Node(Token.VAR);
        Scope scope2 = creator.createScope(n2, null);
        
        assertEquals(null, scope2.getParent());
        assertEquals(n2, scope2.getRoot());
    }

    @Test
    public void testScanVars() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        Node n = new Node(Token.VAR);
        
        // Test case 1: Declare variables
        creator.scanVars(n, null);
        assertEquals(3, creator.getScope().getVarCount());
        
        // Test case 2: Declare function
        creator.scanVars(n, new Node(Token.FUNCTION));
        assertEquals(4, creator.getScope().getVarCount());
        
        // Test case 3: Declare catch variable
        creator.scanVars(n, new Node(Token.CATCH));
        assertEquals(5, creator.getScope().getVarCount());
        
        // Test case 4: Global block
        creator.scanVars(n, new Node(Token.SCRIPT));
        assertEquals(6, creator.getScope().getVarCount());
    }

    @Test
    public void testDeclareVar() {
        AbstractCompiler compiler = new AbstractCompiler();
        SyntacticScopeCreator creator = new SyntacticScopeCreator(compiler);
        
        Scope scope1 = new Scope(null, null);
        Node n1 = new Node(Token.NAME);
        Node parent1 = new Node(Token.VAR);
        Node gramps1 = new Node(Token.FUNCTION);
        JSType declaredType1 = new JSType();
        Node nodeWithLineNumber1 = new Node(Token.NUMBER);
        
        // Test case 1: Declare variable without redeclaration
        creator.setScope(scope1);
        creator.declareVar("var1", n1, parent1, gramps1, declaredType1, nodeWithLineNumber1);
        assertEquals(1, creator.getScope().getVarCount());
        
        // Test case 2: Declare variable with redeclaration
        creator.declareVar("var1", n1, parent1, gramps1, declaredType1, nodeWithLineNumber1);
        assertEquals(1, creator.getScope().getVarCount());
    }

}