import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    private ClassNameIdResolver resolver;
    private JavaType baseType;
    private TypeFactory typeFactory;

    @Before
    public void setUp() {
        baseType = TypeFactory.defaultInstance().constructType(String.class);
        typeFactory = TypeFactory.defaultInstance();
        resolver = new ClassNameIdResolver(baseType, typeFactory);
    }

    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        // This method is not used with Class name-based resolvers
    }

    @Test
    public void testIdFromValue() {
        String value = "TestString";
        String expectedId = "java.lang.String";
        String id = resolver.idFromValue(value);
        assertEquals(expectedId, id);

        Integer intValue = 10;
        expectedId = "java.lang.Integer";
        id = resolver.idFromValue(intValue);
        assertEquals(expectedId, id);
    }

    @Test
    public void testIdFromValueAndType() {
        String value = "TestString";
        Class<?> type = String.class;
        String expectedId = "java.lang.String";
        String id = resolver.idFromValueAndType(value, type);
        assertEquals(expectedId, id);

        Integer intValue = 10;
        type = Integer.class;
        expectedId = "java.lang.Integer";
        id = resolver.idFromValueAndType(intValue, type);
        assertEquals(expectedId, id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        String id = "java.lang.String";
        JavaType expectedType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType type = resolver.typeFromId(null, id);
        assertEquals(expectedType, type);

        id = "java.lang.Integer";
        expectedType = TypeFactory.defaultInstance().constructType(Integer.class);
        type = resolver.typeFromId(null, id);
        assertEquals(expectedType, type);
    }

    @Test
    public void testTypeFromIdWithUnknownId() throws IOException {
        String id = "com.example.UnknownClass";
        try {
            resolver.typeFromId(null, id);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("no such class found", e.getMessage());
        }
    }

    @Test
    public void testTypeFromIdWithInvalidId() throws IOException {
        String id = "InvalidClass";
        try {
            resolver.typeFromId(null, id);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid type id 'InvalidClass' (for id type 'Id.class'): null", e.getMessage());
        }
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        String expectedDesc = "class name used as type id";
        String desc = resolver.getDescForKnownTypeIds();
        assertEquals(expectedDesc, desc);
    }
}