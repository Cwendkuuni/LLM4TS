import static org.junit.Assert.*;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        assertNotNull(typeBindings);
        assertTrue(typeBindings.isEmpty());
    }

    @Test
    public void testCreate() {
        Class<?> erasedType = String.class;
        TypeBindings typeBindings = TypeBindings.create(erasedType, null);
        assertNotNull(typeBindings);
        assertTrue(typeBindings.isEmpty());
    }

    @Test
    public void testCreateWithTypes() {
        Class<?> erasedType = List.class;
        JavaType typeArg = new JavaType();
        TypeBindings typeBindings = TypeBindings.create(erasedType, typeArg);
        assertNotNull(typeBindings);
        assertEquals(1, typeBindings.size());
        assertEquals(typeArg, typeBindings.getBoundType(0));
    }

    @Test
    public void testFindBoundType() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        JavaType typeArg = new JavaType();
        String name = "Test";
        JavaType foundType = typeBindings.findBoundType(name);
        assertNull(foundType);
        
        typeBindings = TypeBindings.create(String.class, typeArg);
        foundType = typeBindings.findBoundType(name);
        assertEquals(typeArg, foundType);
    }
    
    @Test
    public void testToString() {
        TypeBindings typeBindings = TypeBindings.create(String.class, new JavaType(), new JavaType());
        String result = typeBindings.toString();
        assertEquals("<JavaType,JavaType>", result);
    }

    @Test
    public void testEquals() {
        TypeBindings typeBindings1 = TypeBindings.create(String.class, new JavaType());
        TypeBindings typeBindings2 = TypeBindings.create(String.class, new JavaType());
        TypeBindings typeBindings3 = TypeBindings.create(Integer.class, new JavaType());
        
        assertTrue(typeBindings1.equals(typeBindings2));
        assertFalse(typeBindings1.equals(typeBindings3));
    }
}