import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
        assertEquals(0, empty.size());
    }

    @Test
    public void testCreateSimpleBindings() {
        TypeBindings bindings = TypeBindings.create(String[].class, String.class, Integer.class);
        assertEquals(2, bindings.size());
        assertEquals("T:J", bindings.getBoundName(0));
        assertEquals(String[].class, bindings.getBoundType(0));
        assertEquals("E", bindings.getBoundName(1));
        assertEquals(Integer.class, bindings.getBoundType(1));
    }

    @Test
    public void testCreateComplexBindings() {
        TypeBindings bindings = TypeBindings.create(List.class, List.class, Map.class, String.class);
        assertEquals(3, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(List.class, bindings.getBoundType(0));
        assertEquals("E", bindings.getBoundName(1));
        assertEquals(List.class, bindings.getBoundType(1));
        assertEquals("K", bindings.getBoundName(2));
        assertEquals(String.class, bindings.getBoundType(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidBindings() {
        TypeBindings bindings = TypeBindings.create(List.class, List.class, Map.class, String.class, Integer.class);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testFindBoundType() {
        TypeBindings bindings = TypeBindings.create(List.class, List.class, Map.class, String.class, Integer.class);
        assertEquals(List.class, bindings.findBoundType("T"));
        assertEquals(Map.class, bindings.findBoundType("K"));
        assertEquals(null, bindings.findBoundType("X"));
    }

    @Test
    public void testHasUnbound() {
        TypeBindings bindings = TypeBindings.create(List.class, List.class, Map.class, String.class, Integer.class);
        assertFalse(bindings.hasUnbound("T"));
        assertFalse(bindings.hasUnbound("E"));
        assertFalse(bindings.hasUnbound("K"));
        assertTrue(bindings.hasUnbound("X"));
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings bindings = TypeBindings.create(List.class, List.class, Map.class, String.class, Integer.class);
        TypeBindings newBindings = bindings.withUnboundVariable("X");
        assertNotSame(bindings, newBindings);
        assertTrue(newBindings.hasUnbound("X"));
    }
}