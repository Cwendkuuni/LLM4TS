import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.fasterxml.jackson.databind.node.JsonNodeFactory.instanceOf;

public class TypeDeserializerBaseTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @After
    public void cleanup() throws Exception {
        objectMapper.close();
    }

    @Test
    public void testForProperty() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
        BeanProperty beanProperty = new BeanProperty.Std("propertyName", String.class);
        TypeDeserializer actual = typeDeserializerBase.forProperty(beanProperty);
        Assert.assertNotNull(actual);
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
        JsonTypeInfo.As actual = typeDeserializerBase.getTypeInclusion();
        Assert.assertEquals(JsonTypeInfo.As.INCLUDE_EXISTING_PROPERTY, actual);
    }

    @Test
    public void testBaseTypeName() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
        String actual = typeDeserializerBase.baseTypeName();
        Assert.assertEquals("com.example.MyClass", actual);
    }

    @Test
    public void testGetPropertyName() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
        String actual = typeDeserializerBase.getPropertyName();
        Assert.assertEquals("propertyName", actual);
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
        TypeIdResolver actual = typeDeserializerBase.getTypeIdResolver();
        Assert.assertNotNull(actual);
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
        Class<?> actual = typeDeserializerBase.getDefaultImpl();
        Assert.assertNull(actual);
    }

    @Test
    public void testToString() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
        String actual = typeDeserializerBase.toString();
        Assert.assertEquals("[com.example.MyTypeDeserializerBase; base-type:com.example.MyClass; id-resolver:null]", actual);
    }

    @Test
    public void testFindDeserializer() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
        String typeId = "com.example.MyClassSubType";
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonDeserializer<Object> actual = typeDeserializerBase._findDeserializer(ctxt, typeId);
        Assert.assertNotNull(actual);
    }

    @Test(expected = IOException.class) // expected exception
    public void testFindDeserializerNotFound() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
        String typeId = "com.example.UnknownType";
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        typeDeserializerBase._findDeserializer(ctxt, typeId);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonDeserializer<Object> actual = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        Assert.assertNotNull(actual);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
        String json = "{\"typeId\":\"com.example.MyClassSubType\", \"value\":\"test\"}";
        JsonParser jp = objectMapper.getFactory().createParser(json);
        Object actual = typeDeserializerBase._deserializeWithNativeTypeId(jp, objectMapper.getDeserializationContext(), jp.getTypeId());
        Assert.assertEquals("Test", actual);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase();
        String json = "{\"typeId\":\"com.example.UnknownType\", \"value\":\"test\"}";
        JsonParser jp = objectMapper.getFactory().createParser(json);
        ObjectMapper objectMapperWithStrictTyping = new ObjectMapper().enable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
        try {
            typeDeserializerBase._deserializeWithNativeTypeId(jp, objectMapperWithStrictTyping.getDeserializationContext(), jp.getTypeId());
        } catch (IOException e) {
            JsonNode errorMessage = instanceOf(e.getMessage(), JsonNode.class);
            String actual = errorMessage.get("message");
            Assert.assertEquals("Unknown type id [com.example.UnknownType] for class [class com.example.MyTypeDeserializerBase]", actual);
        }
    }
}

class MyTypeDeserializerBase extends TypeDeserializerBase {
    MyTypeDeserializerBase() {
        super(null, null, null, false, null);
    }
}