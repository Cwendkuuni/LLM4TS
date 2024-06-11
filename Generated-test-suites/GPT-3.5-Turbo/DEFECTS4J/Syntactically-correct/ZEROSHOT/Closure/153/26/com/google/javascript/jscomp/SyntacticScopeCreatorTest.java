import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {
    
    private SyntacticScopeCreator scopeCreator;
    
    @Before
    public void setUp() {
        scopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
    }
    
    @Test
    public void testCreateScope() {
        // Test case 1
        Node n1 = new Node(Token.FUNCTION);
        Scope parent1 = new Scope(n1, new AbstractCompiler());
        Scope scope1 = scopeCreator.createScope(n1, parent1);
        assertEquals(parent1, scope1.getParent());
        
        // Test case 2
        Node n2 = new Node(Token.SCRIPT);
        Scope parent2 = new Scope(n2, new AbstractCompiler());
        Scope scope2 = scopeCreator.createScope(n2, parent2);
        assertEquals(parent2, scope2.getParent());
        
        // Test case 3
        Node n3 = new Node(Token.VAR);
        Node child1 = new Node(Token.NAME, "x");
        Node child2 = new Node(Token.NAME, "y");
        n3.addChildToBack(child1);
        n3.addChildToBack(child2);
        Scope parent3 = new Scope(n3, new AbstractCompiler());
        Scope scope3 = scopeCreator.createScope(n3, parent3);
        
        assertEquals(true, scope3.isDeclared("x", false));
        assertEquals(true, scope3.isDeclared("y", false));
        assertEquals(parent3, scope3.getParent());
    }
}