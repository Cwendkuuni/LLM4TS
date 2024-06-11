import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ScopeTest {
    
    private Scope scope;
    
    @Before
    public void setUp() {
        scope = new Scope(null, null);
    }

    @Test
    public void testDeclare() {
        Var var = scope.declare("testVar", null, null, null);
        assertNotNull(var);
        assertEquals("testVar", var.getName());
        assertNull(var.getNameNode());
        assertNull(var.getType());
        assertNull(var.getInputName());
    }

    @Test
    public void testUndeclare() {
        Var var1 = scope.declare("var1", null, null, null);
        assertNotNull(var1);
        assertTrue(scope.isDeclared("var1", true));

        scope.undeclare(var1);
        assertFalse(scope.isDeclared("var1", true));
    }
    
    @Test
    public void testGetVar() {
        Var var1 = scope.declare("var1", null, null, null);
        Var var2 = scope.declare("var2", null, null, null);
        
        Var res1 = scope.getVar("var1");
        assertNotNull(res1);
        assertEquals(var1, res1);
        
        Var res2 = scope.getVar("var2");
        assertNotNull(res2);
        assertEquals(var2, res2);
        
        Var res3 = scope.getVar("var3");
        assertNull(res3);
    }
    
    @Test
    public void testIsDeclared() {
        Var var1 = scope.declare("var1", null, null, null);
        assertTrue(scope.isDeclared("var1", true));
        assertTrue(scope.isDeclared("var1", false));
        assertFalse(scope.isDeclared("var2", true));
        assertFalse(scope.isDeclared("var2", false));
        
        assertFalse(scope.isDeclared("var3", true));
        assertFalse(scope.isDeclared("var3", false));
    }
    
    @Test
    public void testGetVars() {
        Var var1 = scope.declare("var1", null, null, null);
        Var var2 = scope.declare("var2", null, null, null);
        
        Iterator<Var> iterator = scope.getVars();
        assertTrue(iterator.hasNext());
        assertEquals(var1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(var2, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testGetVarCount() {
        assertEquals(0, scope.getVarCount());
        
        scope.declare("var1", null, null, null);
        assertEquals(1, scope.getVarCount());
        
        scope.declare("var2", null, null, null);
        assertEquals(2, scope.getVarCount());
    }
    
    @Test
    public void testIsGlobal() {
        assertTrue(scope.isGlobal());
        
        Scope childScope = new Scope(scope, null);
        assertFalse(childScope.isGlobal());
    }
    
    @Test
    public void testIsLocal() {
        assertFalse(scope.isLocal());

        Scope childScope = new Scope(scope, null);
        assertTrue(childScope.isLocal());
    }
}