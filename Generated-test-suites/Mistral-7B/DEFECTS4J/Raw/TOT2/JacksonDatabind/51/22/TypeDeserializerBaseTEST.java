import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testForProperty() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        BeanProperty beanProperty = new BeanProperty.Std("testProperty", String.class);
        TypeDeserializer actual = typeDeserializerBase.forProperty(beanProperty);
        assertEquals(String.class, actual.getType(null));
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        assertEquals("com.example.TestClass", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        assertEquals("testProperty", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        assertNotNull(typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        String result = typeDeserializerBase.toString();
        assertTrue(result.contains("[com.example.TypeDeserializerBase"));
    }

    @Test
    public void testFindDeserializer() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        String typeId = "TestClass";
        JsonDeserializer<Object> actual = typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), typeId);
        assertEquals(TestSerializer.class, actual.getClass());
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithUnknownTypeId() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        String typeId = "UnknownType";
        typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), typeId);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        JsonDeserializer<Object> actual = typeDeserializerBase._findDefaultImplDeserializer(objectMapper.getDeserializationContext());
        assertEquals(NullifyingDeserializer.instance.getClass(), actual.getClass());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"typeId\":\"TestClass\"}");
        Object actual = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext());
        assertEquals("Test", actual);
    }

    @Test
    public void testDeserializeWithUnknownTypeId() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"typeId\":\"UnknownType\"}");
        Object actual = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext());
        assertNull(actual);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        String typeId = "UnknownType";
        JavaType javaType = typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), typeId, null, null);
        assertNull(javaType);
    }
}

class TestTypeDeserializerBase extends TypeDeserializerBase {

    public TestTypeDeserializerBase() {
        super(null, null, "", false, null);
    }

    @Override
    public TypeDeserializer forProperty(BeanProperty prop) {
        return null;
    }
}

class TestSerializer extends StdSerializer<String> {

}