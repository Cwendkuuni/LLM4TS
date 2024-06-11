import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {
    private ObjectMapper mapper;

    public TypeDeserializerBaseTest() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testForPropertyMethod() throws Exception {
        TypeDeserializerBase testClass = new TypeDeserializerBase(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typeProperty",
                true,
                String.class
        );
        BeanProperty property = new BeanProperty("propertyName", String.class);
        TypeDeserializer typeDeserializer = testClass.forProperty(property);
        assertNotNull(typeDeserializer);
    }

    @Test
    public void testGetTypeInclusionMethod() throws Exception {
        TypeDeserializerBase testClass = new TypeDeserializerBase(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typeProperty",
                true,
                String.class
        );
        JsonTypeInfo.As typeInclusion = testClass.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.WRAP_ONLY, typeInclusion);
    }

    @Test
    public void testBaseTypeNameMethod() throws Exception {
        TypeDeserializerBase testClass = new TypeDeserializerBase(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typeProperty",
                true,
                String.class
        );
        String baseTypeName = testClass.baseTypeName();
        assertEquals("java.lang.String", baseTypeName);
    }

    @Test
    public void testGetPropertyNameMethod() throws Exception {
        TypeDeserializerBase testClass = new TypeDeserializerBase(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typeProperty",
                true,
                String.class
        );
        String propertyName = testClass.getPropertyName();
        assertEquals("typeProperty", propertyName);
    }

    @Test
    public void testGetTypeIdResolverMethod() throws Exception {
        TypeDeserializerBase testClass = new TypeDeserializerBase(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typeProperty",
                true,
                String.class
        );
        TypeIdResolver typeIdResolver = testClass.getTypeIdResolver();
        assertNotNull(typeIdResolver);
    }

    @Test
    public void testGetDefaultImplMethod() throws Exception {
        TypeDeserializerBase testClass = new TypeDeserializerBase(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typeProperty",
                true,
                String.class
        );
        Class<?> defaultImpl = testClass.getDefaultImpl();
        assertNull(defaultImpl);
    }

    @Test
    public void testToStringMethod() throws Exception {
        TypeDeserializerBase testClass = new TypeDeserializerBase(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typeProperty",
                true,
                String.class
        );
        String toString = testClass.toString();
        assertTrue(toString.startsWith("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase;"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForPropertyMethodWithNullProperty() throws Exception {
        TypeDeserializerBase testClass = new TypeDeserializerBase(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typeProperty",
                true,
                String.class
        );
        BeanProperty property = null;
        TypeDeserializer typeDeserializer = testClass.forProperty(property);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForPropertyMethodWithNullBaseType() throws Exception {
        TypeDeserializerBase testClass = new TypeDeserializerBase(null,
                mapper.getTypeIdResolver(),
                "typeProperty",
                true,
                String.class
        );
        BeanProperty property = new BeanProperty("propertyName", String.class);
        TypeDeserializer typeDeserializer = testClass.forProperty(property);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForPropertyMethodWithNullIdResolver() throws Exception {
        TypeDeserializerBase testClass = new TypeDeserializerBase(
                mapper.getTypeFactory().constructType(String.class),
                null,
                "typeProperty",
                true,
                String.class
        );
        BeanProperty property = new BeanProperty("propertyName", String.class);
        TypeDeserializer typeDeserializer = testClass.forProperty(property);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForPropertyMethodWithNullPropertyName() throws Exception {
        TypeDeserializerBase testClass = new TypeDeserializerBase(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                null,
                true,
                String.class
        );
        BeanProperty property = new BeanProperty("propertyName", String.class);
        TypeDeserializer typeDeserializer = testClass.forProperty(property);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForPropertyMethodWithNullDefaultImpl() throws Exception {
        TypeDeserializerBase testClass = new TypeDeserializerBase(
                mapper.getTypeFactory().constructType(String.class),
                mapper.getTypeIdResolver(),
                "typeProperty",
                true,
                null
        );
        BeanProperty property = new BeanProperty("propertyName", String.class);
        TypeDeserializer typeDeserializer = testClass.forProperty(property);
    }
}