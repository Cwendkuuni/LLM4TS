import org.junit.*;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateWithNullTypes() {
        TypeBindings bindings = TypeBindings.create(String.class, null);
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateWithSingleType() {
        JavaType type = new JavaType(String.class);
        TypeBindings bindings = TypeBindings.create(String.class, type);
        assertEquals(1, bindings.size());
        assertEquals("String", bindings.getBoundType(0).toString());
    }

    @Test
    public void testCreateWithTwoTypes() {
        JavaType type1 = new JavaType(String.class);
        JavaType type2 = new JavaType(Integer.class);
        TypeBindings bindings = TypeBindings.create(String.class, type1, type2);
        assertEquals(2, bindings.size());
        assertEquals("String", bindings.getBoundType(0).toString());
        assertEquals("Integer", bindings.getBoundType(1).toString());
    }

    @Test
    public void testCreateIfNeededWithNoTypeParameters() {
        TypeBindings bindings = TypeBindings.createIfNeeded(String.class, new JavaType(Integer.class));
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings bindings = TypeBindings.create(String.class, new JavaType(Integer.class));
        TypeBindings newBindings = bindings.withUnboundVariable("T");
        assertTrue(newBindings.hasUnbound("T"));
    }

    @Test
    public void testFindBoundType() {
        JavaType type1 = new JavaType(String.class);
        JavaType type2 = new JavaType(Integer.class);
        TypeBindings bindings = TypeBindings.create(String.class, type1, type2);
        assertEquals(type2, bindings.findBoundType("Integer"));
    }

    @Test
    public void testEquals() {
        TypeBindings bindings1 = TypeBindings.create(String.class, new JavaType(Integer.class));
        TypeBindings bindings2 = TypeBindings.create(String.class, new JavaType(Integer.class));
        assertEquals(bindings1, bindings2);
    }

}