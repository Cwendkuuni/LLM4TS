import static org.junit.Assert.*;
import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testFindBoundType() {
        TypeBindings typeBindings = TypeBindings.create(TypeBindingsTest.class, new JavaType());
        JavaType boundType = typeBindings.findBoundType("T");
        assertNotNull(boundType);
    }

    @Test
    public void testCreateWithTypeArg1() {
        TypeBindings created = TypeBindings.create(TypeBindingsTest.class, new JavaType());
        assertNotNull(created);
    }

    @Test
    public void testCreateWithTypeArgs() {
        TypeBindings created = TypeBindings.create(TypeBindingsTest.class, new JavaType(), new JavaType());
        assertNotNull(created);
    }

    @Test
    public void testCreateIfNeededWithTypeArg1() {
        TypeBindings created = TypeBindings.createIfNeeded(TypeBindingsTest.class, new JavaType());
        assertNotNull(created);
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings typeBindings = TypeBindings.create(TypeBindingsTest.class, new JavaType());
        TypeBindings withUnbound = typeBindings.withUnboundVariable("T");
        assertTrue(withUnbound.hasUnbound("T"));
    }

    @Test
    public void testGetBoundName() {
        TypeBindings typeBindings = TypeBindings.create(TypeBindingsTest.class, new JavaType());
        String boundName = typeBindings.getBoundName(0);
        assertEquals("T", boundName);
    }

    @Test
    public void testGetBoundType() {
        TypeBindings typeBindings = TypeBindings.create(TypeBindingsTest.class, new JavaType());
        JavaType boundType = typeBindings.getBoundType(0);
        assertNotNull(boundType);
    }

}