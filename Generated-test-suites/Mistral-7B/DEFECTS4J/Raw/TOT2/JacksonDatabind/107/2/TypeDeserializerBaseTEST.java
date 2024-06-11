import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class TypeDeserializerBaseTest {

    private static final String TEST_CLASS_NAME = "com.example.TestTypeDeserializerBase";
    private static final String TEST_BASE_TYPE_NAME = "com.example.TestBaseClass";
    private static final String TEST_TYPE_PROPERTY_NAME = "typePropertyName";
    private static final String TEST_TYPE_ID_PROPERTY_NAME = "typeIdPropertyName";
    private static final String TEST_DEFAULT_IMPL_CLASS_NAME = "com.example.TestDefaultImplClass";
    private static final String TEST_TYPE_ID = "testTypeId";

    private ObjectMapper objectMapper;
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class);
        typeDeserializerBase = (TypeDeserializerBase) objectMapper.readValue(
                new StringReader("{" +
                        "\"@class\":\"" + TEST_CLASS_NAME + "\"," +
                        "\"baseType\":{" +
                        "\"@class\":\"" + TEST_BASE_TYPE_NAME + "\"" +
                        "}," +
                        "\"typePropertyName\":\"" + TEST_TYPE_PROPERTY_NAME + "\"," +
                        "\"typeIdVisible\":false," +
                        "\"defaultImpl\":{" +
                        "\"@class\":\"" + TEST_DEFAULT_IMPL_CLASS_NAME + "\"" +
                        "}" +
                        "}"), javaType);
    }

    @Test
    public void testForProperty() throws IOException {
        BeanProperty beanProperty = new BeanProperty(null, "testProperty");
        TypeDeserializer forProperty = typeDeserializerBase.forProperty(beanProperty);
        Assert.assertNotNull(forProperty);
        Assert.assertEquals(TypeDeserializerBase.class, forProperty.getClass());
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        Assert.assertNotNull(typeInclusion);
    }

    @Test
    public void testBaseTypeName() {
        String baseTypeName = typeDeserializerBase.baseTypeName();
        Assert.assertEquals(TEST_BASE_TYPE_NAME, baseTypeName);
    }

    @Test
    public void testPropertyName() {
        String propertyName = typeDeserializerBase.getPropertyName();
        Assert.assertEquals(TEST_TYPE_PROPERTY_NAME, propertyName);
    }

    @Test
    public void testgetTypeIdResolver() {
        TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();
        Assert.assertNotNull(typeIdResolver);
    }

    @Test
    public void testDefaultImpl() {
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        Assert.assertNotNull(defaultImpl);
        Assert.assertEquals(TEST_DEFAULT_IMPL_CLASS_NAME, defaultImpl.getName());
    }

    @Test
    public void testFindDeserializerWithValidTypeId() throws IOException {
        String typeId = "testTypeId1";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(
                objectMapper.getDeserializationContext(), typeId);
        Assert.assertNotNull(deserializer);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithInvalidTypeId() throws IOException {
        String typeId = "testTypeId2";
        typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), typeId);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(
                objectMapper.getDeserializationContext());
        Assert.assertNotNull(deserializer);
        Assert.assertEquals(TEST_DEFAULT_IMPL_CLASS_NAME, deserializer.getClass().getName());
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdNull() throws IOException {
        typeDeserializerBase._deserializeWithNativeTypeId(null, objectMapper.getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdUnknownTypeId() throws IOException {
        JsonParser jsonParser = objectMapper.getFactory()
                .createParser(new StringReader("{@typeId:\"unknownTypeId\"}"));
        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext());
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        String extraDesc = "TypeDeserializerBaseTest: handleUnknownTypeId test";
        JavaType javaType = objectMapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class);
        TypeDeserializerBase typeDeserializerBase = (TypeDeserializerBase) objectMapper.readValue(
                new StringReader("{" +
                        "\"@class\":\"" + TEST_CLASS_NAME + "\"," +
                        "\"baseType\":{" +
                        "\"@class\":\"" + TEST_BASE_TYPE_NAME + "\"" +
                        "}," +
                        "\"typePropertyName\":\"" + TEST_TYPE_PROPERTY_NAME + "\"," +
                        "\"typeIdVisible\":true," +
                        "\"defaultImpl\":{" +
                        "\"@class\":\"" + TEST_DEFAULT_IMPL_CLASS_NAME + "\"" +
                        "}" +
                        "}"), javaType);
        try {
            typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), null);
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), "testTypeId3");
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            JavaType javaTypeWithTypeId = objectMapper.getTypeFactory()
                    .constructType(TypeDeserializerBase.class, String.class);
            TypeDeserializerBase typeDeserializerBaseWithTypeId = (TypeDeserializerBase) objectMapper.readValue(
                    new StringReader("{" +
                            "\"@class\":\"" + TEST_CLASS_NAME + "\"," +
                            "\"baseType\":{" +
                            "\"@class\":\"" + TEST_BASE_TYPE_NAME + "\"" +
                            "}," +
                            "\"typePropertyName\":\"" + TEST_TYPE_PROPERTY_NAME + "\"," +
                            "\"typeIdVisible\":true," +
                            "\"defaultImpl\":{" +
                            "\"@class\":\"" + TEST_DEFAULT_IMPL_CLASS_NAME + "\"" +
                            "}" +
                            ",\"typeId\":\"" + TEST_TYPE_ID + "\"}"), javaTypeWithTypeId);
            typeDeserializerBaseWithTypeId._handleUnknownTypeId(objectMapper.getDeserializationContext(), null);
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        try {
            typeDeserializerBaseWithTypeId._handleUnknownTypeId(objectMapper.getDeserialationContext(), "testTypeId4");
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        String extraDesc = "TypeDeserializerBaseTest: handleMissingTypeId test";
        JavaType javaType = objectMapper.getTypeFactory()
                .constructType(TypeDeserializerBase.class);
        TypeDeserializerBase typeDeserializerBase = (TypeDeserializerBase) objectMapper.readValue(
                new StringReader("{" +
                        "\"@class\":\"" + TEST_CLASS_NAME + "\"," +
                        "\"baseType\":{" +
                        "\"@class\":\"" + TEST_BASE_TYPE_NAME + "\"" +
                        "}," +
                        "\"typePropertyName\":\"" + TEST_TYPE_PROPERTY_NAME + "\"," +
                        "\"typeIdVisible\":false," +
                        "\"defaultImpl\":{" +
                        "\"@class\":\"" + TEST_DEFAULT_IMPL_CLASS_NAME + "\"" +
                        "}" +
                        "}"), javaType);
        try {
            typeDeserializerBase._handleMissingTypeId(objectMapper.getDeserialationContext(), null);
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }
}