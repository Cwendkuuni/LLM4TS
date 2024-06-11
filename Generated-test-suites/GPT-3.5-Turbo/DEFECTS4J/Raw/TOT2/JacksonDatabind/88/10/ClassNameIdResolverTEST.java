import org.junit.Test;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        assertEquals("java.lang.Object", resolver.idFromValue(value));
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Class<?> type = Object.class;
        Object value = new Object();
        assertEquals("java.lang.Object", resolver.idFromValueAndType(value, type));
    }

    @Test
    public void testTypeFromId() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = null;
        String id = "java.lang.String";
        try {
            assertNotNull(resolver.typeFromId(context, id));
        } catch (IOException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testTypeFromIdWithInvalidId() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = null;
        String id = "invalid.class.Name";
        try {
            assertNull(resolver.typeFromId(context, id));
        } catch (IOException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void test_idFrom() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = new Object();
        Class<?> cls = Object.class;
        assertNotNull(resolver._idFrom(value, cls, null));
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }
}