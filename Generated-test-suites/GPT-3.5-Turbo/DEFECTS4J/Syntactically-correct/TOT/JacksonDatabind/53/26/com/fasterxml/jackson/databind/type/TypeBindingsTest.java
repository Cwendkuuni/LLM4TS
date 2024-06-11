import static org.junit.Assert.*;
import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testFindBoundType() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType type = bindings.findBoundType("name");
        assertNull(type);
    }

    @Test
    public void testCreate() {
        Class<?> erasedType = TypeBindingsTest.class;
        JavaType[] types = { new JavaType() };
        try {
            TypeBindings.create(erasedType, types);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not create TypeBindings for class com.fasterxml.jackson.databind.type.TypeBindingsTest with 1 type parameter: class expects 0", e.getMessage());
        }
    }

    @Test
    public void testCreateIfNeeded() {
        Class<?> erasedType = TypeBindingsTest.class;
        JavaType type = new JavaType();
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, type);
        assertFalse(bindings.isEmpty());
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        TypeBindings newBindings = bindings.withUnboundVariable("name");
        assertTrue(newBindings.hasUnbound("name"));
    }

    @Test
    public void testToString() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        String str = bindings.toString();
        assertEquals("<>", str);
    }

    @Test
    public void testHashCode() {
        TypeBindings bindings1 = TypeBindings.emptyBindings();
        TypeBindings bindings2 = TypeBindings.emptyBindings();
        assertEquals(bindings1.hashCode(), bindings2.hashCode());
    }

    @Test
    public void testEquals() {
        TypeBindings bindings1 = TypeBindings.emptyBindings();
        TypeBindings bindings2 = TypeBindings.emptyBindings();
        assertTrue(bindings1.equals(bindings2));
    }

    @Test
    public void testSize() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        int size = bindings.size();
        assertEquals(0, size);
    }

    @Test
    public void testGetBoundName() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        String name = bindings.getBoundName(0);
        assertNull(name);
    }

    @Test
    public void testGetBoundType() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType type = bindings.getBoundType(0);
        assertNull(type);
    }

    @Test
    public void testGetTypeParameters() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        int size = bindings.getTypeParameters().size();
        assertEquals(0, size);
    }

    @Test
    public void testHasUnbound() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertFalse(bindings.hasUnbound("name"));
    }

    @Test
    public void testTypeParameterArray() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType[] types = bindings.typeParameterArray();
        assertNotNull(types);
    }
}