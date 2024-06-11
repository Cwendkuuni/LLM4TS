import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testCreate() {
        TypeBindings typeBindings = TypeBindings.create(Class.class, null);
        assertNotNull(typeBindings);
        assertEquals(TypeBindings.emptyBindings(), typeBindings);
    }

    @Test
    public void testCreateIfNeeded() {
        TypeBindings typeBindings = TypeBindings.createIfNeeded(Class.class, null);
        assertNotNull(typeBindings);
        assertEquals(TypeBindings.emptyBindings(), typeBindings);
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        TypeBindings newTypeBindings = typeBindings.withUnboundVariable("test");
        assertNotNull(newTypeBindings);
        assertFalse(newTypeBindings.isEmpty());
        assertTrue(newTypeBindings.hasUnbound("test"));
        assertFalse(newTypeBindings.hasUnbound("unknown"));
    }

    @Test
    public void testFindBoundType() {
        TypeBindings typeBindings = TypeBindings.create(Class.class, new JavaType[] {JavaType.constructSimple(Class.class)});
        JavaType boundType = typeBindings.findBoundType("T");
        assertNotNull(boundType);
        assertEquals(JavaType.constructSimple(Class.class), boundType);
    }

}