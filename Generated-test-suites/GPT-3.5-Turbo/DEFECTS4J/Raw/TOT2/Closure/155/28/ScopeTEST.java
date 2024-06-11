import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

    private Scope globalScope;
    private Scope localScope;
    
    @Before
    public void setUp() {
        globalScope = new Scope(null, null);
        localScope = new Scope(globalScope, null);
    }
    
    @Test
    public void testDeclareVariable() {
        Var var = localScope.declare("testVar", new Node(Token.NAME, "testVar"), JSType.STRING, null);
        assertNotNull(var);
        assertEquals("testVar", var.getName());
        assertEquals(JSType.STRING, var.getType());
    }
    
    @Test
    public void testUndeclareVariable() {
        Var var = localScope.declare("testVar", new Node(Token.NAME, "testVar"), JSType.STRING, null);
        localScope.undeclare(var);
        assertNull(localScope.getVar("testVar"));
    }
    
    @Test
    public void testGetVarFromParentScope() {
        Var var = globalScope.declare("testVar", new Node(Token.NAME, "testVar"), JSType.STRING, null);
        assertEquals(var, localScope.getVar("testVar"));
    }
    
    @Test
    public void testIsDeclaredInScopeHierarchy() {
        Var var = globalScope.declare("testVar", new Node(Token.NAME, "testVar"), JSType.STRING, null);
        assertTrue(localScope.isDeclared("testVar", true));
    }
}