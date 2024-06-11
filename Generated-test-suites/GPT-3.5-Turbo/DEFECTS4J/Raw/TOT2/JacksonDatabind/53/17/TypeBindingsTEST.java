import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings emptyBindings = TypeBindings.emptyBindings();
        assertTrue(emptyBindings.isEmpty());
    }

    @Test
    public void testCreate() {
        JavaType type1 = new JavaType();
        JavaType type2 = new JavaType();
        TypeBindings typeBindings = TypeBindings.create(String.class, type1, type2);
        assertNotNull(typeBindings);
    }

    @Test
    public void testCreateIfNeeded() {
        JavaType type = new JavaType();
        TypeBindings typeBindings = TypeBindings.createIfNeeded(List.class, type);
        assertFalse(typeBindings.isEmpty());
    }

    @Test
    public void testFindBoundType() {
        JavaType type1 = new JavaType();
        JavaType type2 = new JavaType();
        TypeBindings typeBindings = TypeBindings.create(String.class, type1, type2);
        JavaType foundType = typeBindings.findBoundType("name");
        assertNull(foundType);
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings oldTypeBindings = TypeBindings.emptyBindings();
        TypeBindings newTypeBindings = oldTypeBindings.withUnboundVariable("variable");
        assertNotNull(newTypeBindings);
    }

    @Test
    public void testToString() {
        JavaType type1 = new JavaType();
        JavaType type2 = new JavaType();
        TypeBindings typeBindings = TypeBindings.create(String.class, type1, type2);
        String result = typeBindings.toString();
        assertNotNull(result);
        assertTrue(result.contains("<"));
        assertTrue(result.contains(">"));
    }

    @Test
    public void testEquals() {
        JavaType type1 = new JavaType();
        JavaType type2 = new JavaType();
        TypeBindings typeBindings1 = TypeBindings.create(String.class, type1, type2);
        TypeBindings typeBindings2 = TypeBindings.create(String.class, type1, type2);
        assertEquals(typeBindings1, typeBindings2);
    }

    @Test
    public void testHashCode() {
        JavaType type1 = new JavaType();
        JavaType type2 = new JavaType();
        TypeBindings typeBindings = TypeBindings.create(String.class, type1, type2);
        assertEquals(typeBindings.hashCode(), typeBindings.hashCode());
    }
}