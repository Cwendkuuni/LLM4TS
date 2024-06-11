import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateWithSingleType() {
        Class<?> clazz = String.class;
        JavaType type = JavaType.construct(clazz);
        TypeBindings bindings = TypeBindings.create(clazz, type);
        assertEquals(1, bindings.size());
        assertEquals(clazz, bindings.getBoundType(0).getRawClass());
    }

    @Test
    public void testCreateIfNeededWithNoTypeParameters() {
        Class<?> clazz = ArrayList.class;
        JavaType type = JavaType.construct(clazz);
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, type);
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testFindBoundType() {
        Class<?> clazz = List.class;
        JavaType type = JavaType.construct(clazz);
        TypeBindings bindings = TypeBindings.create(clazz, type);
        JavaType boundType = bindings.findBoundType("T");
        assertNotNull(boundType);
        assertEquals(clazz, boundType.getRawClass());
    }

    @Test
    public void testWithUnboundVariable() {
        Class<?> clazz = Map.class;
        JavaType type = JavaType.construct(clazz);
        TypeBindings bindings = TypeBindings.create(clazz, type);
        TypeBindings updatedBindings = bindings.withUnboundVariable("K");
        assertTrue(updatedBindings.hasUnbound("K"));
    }

    @Test
    public void testEquals() {
        Class<?> clazz = List.class;
        JavaType type = JavaType.construct(clazz);
        TypeBindings bindings1 = TypeBindings.create(clazz, type);
        TypeBindings bindings2 = TypeBindings.create(clazz, type);
        assertEquals(bindings1, bindings2);
    }

}