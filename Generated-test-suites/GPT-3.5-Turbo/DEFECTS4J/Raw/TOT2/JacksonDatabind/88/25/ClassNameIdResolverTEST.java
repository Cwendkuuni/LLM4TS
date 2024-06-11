import static org.junit.Assert.*;
import org.junit.Test;

public class ClassNameIdResolverTest {

    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }
    
    @Test
    public void testRegisterSubtype() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        try {
            resolver.registerSubtype(String.class, "string");
        } catch (Exception e) {
            fail("Should not throw exception for registerSubtype");
        }
    }
    
    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = "test";
        assertEquals("java.lang.String", resolver.idFromValue(value));
    }
    
    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object value = "test";
        Class<?> type = String.class;
        assertEquals("java.lang.String", resolver.idFromValueAndType(value, type));
    }
    
    @Test
    public void testTypeFromId() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = null;
        String id = "java.lang.String";
        try {
            assertNotNull(resolver.typeFromId(context, id));
        } catch (Exception e) {
            fail("Should not throw exception for typeFromId");
        }
    }
}