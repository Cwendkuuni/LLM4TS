import org.junit.Test;

import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // Checking if the method does nothing (not used with class name - based resolvers)
        resolver.registerSubtype(Object.class, "object");
    }

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object object = new Object();
        String expectedId = object.getClass().getName();
        String actualId = resolver.idFromValue(object);
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Object object = new Object();
        String expectedId = object.getClass().getName();
        String actualId = resolver.idFromValueAndType(object, object.getClass());
        assertEquals(expectedId,actualId);
    }

    @Test
    public void testTypeFromId_existingClass() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null, null, null);
        String id = Object.class.getName();
        JavaType expectedType = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType actualType = resolver.typeFromId(context, id);
        assertEquals(expectedType, actualType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromId_invalidClass() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null, null, null);
        String id = "invalid_class";
        resolver.typeFromId(context, id);
    }

    @Test
    public void testTypeFromId_unknownClass() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null, null, null);
        String id = "com.example.UnknownClass";
        JavaType actualType = resolver.typeFromId(context, id);
        assertNull(actualType);
    }

    // Additional test cases can be added for other methods as needed

}