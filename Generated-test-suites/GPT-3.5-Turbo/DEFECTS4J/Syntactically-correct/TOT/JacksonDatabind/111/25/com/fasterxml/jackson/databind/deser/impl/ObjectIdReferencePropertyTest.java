import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReferenceProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.StringWriter;

public class ObjectIdReferencePropertyTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    public void testObjectIdReferenceProperty_Constructor() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        Assert.assertNotNull(property);
    }

    @Test
    public void testObjectIdReferenceProperty_WithDeserAndNullProvider() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null, null);
        Assert.assertNotNull(property);
    }

    @Test
    public void testWithPropertyName() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) property.withName(PropertyName.construct("name"));
        Assert.assertEquals("name", newProperty.getFullName().getSimpleName());
    }

    @Test
    public void testWithValueDeserializer_SameDeserializer() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) property.withValueDeserializer(null);
        Assert.assertEquals(property, newProperty);
    }

    @Test
    public void testWithValueDeserializer_DifferentDeserializer() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) property.withValueDeserializer(new CustomDeserializer());
        Assert.assertEquals(new CustomDeserializer().getClass(), newProperty.getValueDeserializer().getClass());
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        ObjectIdReferenceProperty newProperty = (ObjectIdReferenceProperty) property.withNullProvider(null);
        Assert.assertNull(newProperty.getNullValueProvider());
    }

    @Test
    public void testFixAccess() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        property.fixAccess(null);
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        Assert.assertNull(property.getAnnotation(Test.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMember() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        property.getMember();
    }

    @Test
    public void testGetCreatorIndex() {
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        Assert.assertEquals(-1, property.getCreatorIndex());
    }

    @Test
    public void testDeserializeSetAndReturn_ResolvedForwardReference() throws Exception {
        StringWriter jsonWriter = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(jsonWriter);
        generator.writeStartObject();
        generator.writeFieldId(1);
        generator.writeFieldName("value");
        generator.writeString("Test Value");
        generator.writeEndObject();
        generator.close();

        String jsonString = jsonWriter.toString();

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);

        TestObject testObject = new TestObject();
        ReadableObjectId.Referring referring = new ObjectIdReferenceProperty.PropertyReferring(property,
                new UnresolvedForwardReference(null, null, null, 1), TestObject.class, testObject);

        property.deserializeSetAndReturn(
                new JsonFactory().createParser(jsonString), objectMapper.getDeserializationContext(), referring);

        Assert.assertEquals("Test Value", testObject.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeSetAndReturn_UnresolvedForwardReferenceWithoutIdentityInfo() throws Exception {
        StringWriter jsonWriter = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(jsonWriter);
        generator.writeStartObject();
        generator.writeFieldId(1);
        generator.writeEndObject();
        generator.close();

        String jsonString = jsonWriter.toString();

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);

        TestObject testObject = new TestObject();
        ReadableObjectId.Referring referring = new ObjectIdReferenceProperty.PropertyReferring(property,
                new UnresolvedForwardReference(null, null, null, 1), TestObject.class, testObject);

        property.deserializeSetAndReturn(
                new JsonFactory().createParser(jsonString), objectMapper.getDeserializationContext(), referring);
    }

    @Test
    public void testSet() throws Exception {
        StringWriter jsonWriter = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(jsonWriter);
        generator.writeStartObject();
        generator.writeFieldName("value");
        generator.writeString("Test Value");
        generator.writeEndObject();
        generator.close();

        String jsonString = jsonWriter.toString();

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);

        TestObject testObject = new TestObject();
        property.set(testObject, objectMapper.readValue(jsonString, TestObject.class));

        Assert.assertEquals("Test Value", testObject.getValue());
    }

    @Test
    public void testSetAndReturn() throws Exception {
        StringWriter jsonWriter = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(jsonWriter);
        generator.writeStartObject();
        generator.writeFieldName("value");
        generator.writeString("Test Value");
        generator.writeEndObject();
        generator.close();

        String jsonString = jsonWriter.toString();

        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);

        TestObject testObject = new TestObject();
        Object result = property.setAndReturn(testObject, objectMapper.readValue(jsonString, TestObject.class));

        Assert.assertEquals("Test Value", testObject.getValue());
        Assert.assertEquals(testObject, result);
    }

    @Test
    public void testPropertyReferring() {
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(
                null, null, TestObject.class, new TestObject());

        Assert.assertNotNull(referring);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertyReferring_HandleResolvedForwardReference_InvalidId() throws Exception {
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(
                null, null, TestObject.class, new TestObject());

        referring.handleResolvedForwardReference("invalidId", new TestObject());
    }

    @Test
    public void testPropertyReferring_HandleResolvedForwardReference() throws Exception {
        ObjectIdReferenceProperty.PropertyReferring referring = new ObjectIdReferenceProperty.PropertyReferring(
                null, null, TestObject.class, new TestObject());

        referring.handleResolvedForwardReference("validId", new TestObject());

        Assert.assertEquals("validId", referring.getRef().getId());
    }

    private static class TestObject {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    private static class CustomDeserializer extends com.fasterxml.jackson.databind.JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return null;
        }
    }
}