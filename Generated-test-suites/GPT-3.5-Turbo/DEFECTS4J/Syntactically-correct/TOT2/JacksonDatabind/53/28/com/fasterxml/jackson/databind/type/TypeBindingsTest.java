import static org.junit.Assert.*;
import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testCreate() {
        TypeBindings typeBindings = TypeBindings.create(String.class, new JavaType[] { JavaType.String });
        assertNotNull(typeBindings);
        assertEquals(String.class, typeBindings.getBoundType(0).getRawClass());
    }

    @Test
    public void testCreateIfNeeded() {
        TypeBindings typeBindings = TypeBindings.createIfNeeded(Integer.class, new JavaType[]{ JavaType.Integer });
        assertNotNull(typeBindings);
        assertEquals(Integer.class, typeBindings.getBoundType(0).getRawClass());
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings typeBindings = TypeBindings.create(String.class, new JavaType[] { JavaType.String });
        TypeBindings updatedBindings = typeBindings.withUnboundVariable("T");
        assertNotNull(updatedBindings);
        assertTrue(updatedBindings.hasUnbound("T"));
    }

    @Test
    public void testFindBoundType() {
        TypeBindings typeBindings = TypeBindings.create(String.class, new JavaType[] { JavaType.String });
        JavaType foundType = typeBindings.findBoundType("T");
        assertNull(foundType);
    }

    @Test
    public void testReadResolve() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        assertNotNull(typeBindings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchingTypes() {
        TypeBindings.create(String.class, new JavaType[] { JavaType.String, JavaType.Integer });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchingNames() {
        TypeBindings.create(String.class, new JavaType[] { JavaType.String }, new String[] { "T", "U" });
    }

}