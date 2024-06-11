import static org.junit.Assert.*;
import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateWithOneType() {
        Class<?> erasedType = List.class;
        JavaType type1 = new JavaType(); // Mock JavaType object
        TypeBindings bindings = TypeBindings.create(erasedType, type1);
        assertEquals(1, bindings.size());
    }

    @Test
    public void testCreateWithTwoTypes() {
        Class<?> erasedType = Map.class;
        JavaType type1 = new JavaType(); // Mock JavaType object
        JavaType type2 = new JavaType(); // Mock JavaType object
        TypeBindings bindings = TypeBindings.create(erasedType, type1, type2);
        assertEquals(2, bindings.size());
    }

    // Add more test cases for other methods and edge cases

}