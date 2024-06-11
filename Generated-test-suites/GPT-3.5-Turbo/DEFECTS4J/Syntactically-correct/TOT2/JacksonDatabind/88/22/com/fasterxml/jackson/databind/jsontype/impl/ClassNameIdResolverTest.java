import static org.junit.Assert.*;
import org.junit.Test;

public class ClassNameIdResolverTest {

    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.construct(TypeUtil.class), TypeFactory.defaultInstance());
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.construct(TypeUtil.class), TypeFactory.defaultInstance());
        Object value = new Object();
        assertEquals(value.getClass().getName(), resolver.idFromValue(value));
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.construct(TypeUtil.class), TypeFactory.defaultInstance());
        Object value = new Object();
        Class<?> type = value.getClass();
        assertEquals(type.getName(), resolver.idFromValueAndType(value, type));
    }

    @Test
    public void testTypeFromId() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.construct(TypeUtil.class), TypeFactory.defaultInstance());
        DatabindContext context = new DeserializationContext(null);
        String id = "java.lang.String";

        try {
            JavaType result = resolver.typeFromId(context, id);
            assertEquals(TypeFactory.defaultInstance().constructType(String.class), result);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void test_idFrom() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.construct(TypeUtil.class), TypeFactory.defaultInstance());
        Object value = new Object();
        Class<?> cls = value.getClass();
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        assertEquals(cls.getName(), resolver._idFrom(value, cls, typeFactory));
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(JavaType.construct(TypeUtil.class), TypeFactory.defaultInstance());
        assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }
}