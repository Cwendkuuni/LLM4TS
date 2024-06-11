import static org.junit.Assert.*;
import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
        assertEquals(0, empty.size());
    }

    @Test
    public void testCreateWithNoTypes() {
        TypeBindings bindings = TypeBindings.create(String.class, null);
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateWithTypeArg1() {
        JavaType type = new JavaType();
        TypeBindings bindings = TypeBindings.create(String.class, type);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertEquals("java.lang.String", bindings.findBoundType("T").toString());
    }

    @Test
    public void testCreateWithTypeArgs2() {
        JavaType type1 = new JavaType();
        JavaType type2 = new JavaType();
        TypeBindings bindings = TypeBindings.create(List.class, type1, type2);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.findBoundType("E").toString());
        assertEquals("java.lang.String", bindings.findBoundType("T").toString());
    }

    @Test
    public void testCreateIfNeededWithNoParams() {
        TypeBindings bindings = TypeBindings.createIfNeeded(String.class, new JavaType());
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateIfNeededWithTypeArg1() {
        JavaType type = new JavaType();
        TypeBindings bindings = TypeBindings.createIfNeeded(List.class, type);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertEquals("java.lang.String", bindings.findBoundType("E").toString());
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings bindings = TypeBindings.createIfNeeded(ArrayList.class, new JavaType());
        TypeBindings updated = bindings.withUnboundVariable("K");
        assertFalse(updated.isEmpty());
        assertEquals(1, updated.size());
        assertEquals("K", updated.getBoundName(0));
    }
}