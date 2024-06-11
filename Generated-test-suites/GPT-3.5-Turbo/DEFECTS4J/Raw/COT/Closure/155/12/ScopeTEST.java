import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScopeTest {
    private Scope scope;

    @Before
    public void setUp() {
        scope = new Scope(null, null);
    }

    @Test
    public void testGetDepth() {
        assertEquals(0, scope.getDepth());
    }

    @Test
    public void testGetRootNode() {
        assertNull(scope.getRootNode());
    }

    @Test
    public void testGetParent() {
        assertNull(scope.getParent());
    }

    @Test
    public void testGetGlobalScope() {
        assertEquals(scope, scope.getGlobalScope());
    }

    @Test
    public void testGetTypeOfThis() {
        assertNull(scope.getTypeOfThis());
    }
}