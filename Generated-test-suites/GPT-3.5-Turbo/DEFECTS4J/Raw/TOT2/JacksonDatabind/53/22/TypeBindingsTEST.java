import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testFindBoundType() {
        TypeBindings typeBindings = TypeBindings.create(TestClass.class, new JavaType[]{JavaType.INT});
        JavaType result = typeBindings.findBoundType("T");
        assertNotNull(result);
        assertEquals(JavaType.INT, result);
    }

    @Test
    public void testCreate() {
        TypeBindings typeBindings = TypeBindings.create(TestClass.class, new JavaType[]{JavaType.INT, JavaType.STRING});
        assertFalse(typeBindings.isEmpty());
        assertEquals(2, typeBindings.size());
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings typeBindings = TypeBindings.create(TestClass.class, new JavaType[]{JavaType.INT});
        TypeBindings updatedTypeBindings = typeBindings.withUnboundVariable("U");
        assertTrue(updatedTypeBindings.hasUnbound("U"));
    }

    @Test
    public void testGetTypeParameters() {
        TypeBindings typeBindings = TypeBindings.create(TestClass.class, new JavaType[]{JavaType.INT, JavaType.STRING});
        assertEquals(2, typeBindings.getTypeParameters().size());
    }

    // Add more test cases as needed

    private static class TestClass<T, U> {}

}