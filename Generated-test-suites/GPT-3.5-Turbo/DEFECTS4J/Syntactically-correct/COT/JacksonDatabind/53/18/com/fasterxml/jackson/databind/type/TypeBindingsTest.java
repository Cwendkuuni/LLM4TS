import static org.junit.Assert.*;
import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testFindBoundType() {
        TypeBindings bindings = TypeBindings.create(String.class, null);
        assertNull(bindings.findBoundType("test"));
    }
    
    @Test
    public void testIsEmpty() {
        TypeBindings bindings = TypeBindings.create(String.class, null);
        assertTrue(bindings.isEmpty());
    }
    
    @Test
    public void testSize() {
        TypeBindings bindings = TypeBindings.create(String.class, null);
        assertEquals(0, bindings.size());
    }
    
    @Test
    public void testGetBoundName() {
        TypeBindings bindings = TypeBindings.create(String.class, null);
        assertNull(bindings.getBoundName(0));
    }
    
    @Test
    public void testGetBoundType() {
        TypeBindings bindings = TypeBindings.create(String.class, null);
        assertNull(bindings.getBoundType(0));
    }
    
    @Test
    public void testGetTypeParameters() {
        TypeBindings bindings = TypeBindings.create(String.class, null);
        assertEquals(0, bindings.getTypeParameters().size());
    }

    @Test
    public void testHasUnbound() {
        TypeBindings bindings = TypeBindings.create(String.class, null);
        assertFalse(bindings.hasUnbound("test"));
    }

    @Test
    public void testReadResolve() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertSame(bindings, bindings.readResolve());
    }
}