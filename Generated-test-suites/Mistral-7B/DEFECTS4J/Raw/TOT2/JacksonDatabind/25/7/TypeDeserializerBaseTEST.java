import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testForProperty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializerBase = mapper.getTypeDeserializerFactory().findValueDeserializer(Map.class, null);
        BeanProperty property = new BeanProperty.Std("testProperty", Map.class);
        TypeDeserializer forProperty = deserializerBase.forProperty(property);
        assertNotNull(forProperty);
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializerBase = mapper.getTypeDeserializerFactory().findValueDeserializer(Map.class, null);
        assertEquals(JsonTypeInfo.As.WRAP_ONLY, deserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializerBase = mapper.getTypeDeserializerFactory().findValueDeserializer(Map.class, null);
        assertEquals("java.util.Map", deserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializerBase = mapper.getTypeDeserializerFactory().findValueDeserializer(Map.class, null);
        assertEquals("typePropertyName", deserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializerBase = mapper.getTypeDeserializerFactory().findValueDeserializer(Map.class, null);
        assertNotNull(deserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializerBase = mapper.getTypeDeserializerFactory().findValueDeserializer(Map.class, null);
        assertNull(deserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializerBase = mapper.getTypeDeserializerFactory().findValueDeserializer(Map.class, null);
        String toString = deserializerBase.toString();
        assertTrue(toString.contains("[TypeDeserializerBase"));
    }

    @Test
    public void testFindDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializerBase = mapper.getTypeDeserializerFactory().findValueDeserializer(Map.class, null);
        String typeId = "com.example.MyClass";
        JsonDeserializer<Object> deserializer = deserializerBase._findDeserializer(mock(DeserializationContext.class), typeId);
        assertNotNull(deserializer);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerNotFound() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializerBase = mapper.getTypeDeserializerFactory().findValueDeserializer(Map.class, null);
        String typeId = "com.nonexistent.MyClass";
        deserializerBase._findDeserializer(mock(DeserializationContext.class), typeId);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializerBase = mapper.getTypeDeserializerFactory().findValueDeserializer(Map.class, null);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> deserializer = deserializerBase._findDefaultImplDeserializer(ctxt);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializerBase = mapper.getTypeDeserializerFactory().findValueDeserializer(Map.class, null);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        synchronized (deserializerBase._defaultImpl) {
            deserializerBase._defaultImplDeserializer = null;
        }
        JsonDeserializer<Object> deserializer = deserializerBase._findDefaultImplDeserializer(ctxt);
        assertNull(deserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializerBase = mapper.getTypeDeserializerFactory().findValueDeserializer(Map.class, null);
        JsonParser jp = mapper.getFactory().createParser("{\"typeId\":\"java.util.HashMap\"}");
        Object deserialized = deserializerBase._deserializeWithNativeTypeId(jp, mock(DeserializationContext.class));
        assertInstanceOf(Map.class, deserialized);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdUnknown() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase deserializerBase = mapper.getTypeDeserializerFactory().findValueDeserializer(Map.class, null);
        JsonParser jp = mapper.getFactory().createParser("{\"typeId\":\"com.nonexistent.MyClass\"}");
        deserializerBase._deserializeWithNativeTypeId(jp, mock(DeserializationContext.class));
    }
}