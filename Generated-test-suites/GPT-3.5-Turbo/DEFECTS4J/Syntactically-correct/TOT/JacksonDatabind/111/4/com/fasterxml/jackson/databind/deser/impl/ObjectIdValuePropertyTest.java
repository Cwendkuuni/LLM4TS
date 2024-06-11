import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor1() {
        ObjectIdReader objectIdReader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        Assert.assertEquals(objectIdReader.propertyName, property.getName());
        Assert.assertEquals(objectIdReader.getIdType(), property.getType());
        Assert.assertEquals(metadata, property.getMetadata());
        Assert.assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
        Assert.assertEquals(objectIdReader, property.getReader());
    }

    @Test
    public void testConstructor2() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(new ObjectIdReader(), new JsonDeserializer<>(), null);
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);
        Assert.assertEquals(newName, property.getName());
        Assert.assertEquals(src.getType(), property.getType());
        Assert.assertEquals(src.getMetadata(), property.getMetadata());
        Assert.assertEquals(src.getValueDeserializer(), property.getValueDeserializer());
        Assert.assertEquals(src.getReader(), property.getReader());
    }

    @Test
    public void testWithNullProvider() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(new ObjectIdReader(), new JsonDeserializer<>(), null);
        NullValueProvider nvp = NullValueProvider.defaultValueProvider();
        ObjectIdValueProperty property = src.withNullProvider(nvp);
        Assert.assertEquals(src.getName(), property.getName());
        Assert.assertEquals(src.getType(), property.getType());
        Assert.assertEquals(src.getMetadata(), property.getMetadata());
        Assert.assertEquals(src.getValueDeserializer(), property.getValueDeserializer());
        Assert.assertEquals(nvp, property.getNullValueProvider());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        Assert.assertNull(property.getAnnotation(TestAnnotation.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        Assert.assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet_WithNull() throws IOException {
        JsonParser jsonParser = TestUtils.createJsonParserWithToken(JsonToken.VALUE_NULL);
        DeserializationContext deserializationContext = new DeserializationContext(null, null, null);
        Object instance = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        property.deserializeAndSet(jsonParser, deserializationContext, instance);
        Assert.assertNull(property.deserializeSetAndReturn(jsonParser, deserializationContext, instance));
    }

    @Test
    public void testDeserializeAndSet_WithNonNull() throws IOException {
        JsonParser jsonParser = TestUtils.createJsonParserWithToken(JsonToken.VALUE_STRING);
        DeserializationContext deserializationContext = new DeserializationContext(null, null, null);
        Object instance = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        property.deserializeAndSet(jsonParser, deserializationContext, instance);
        Assert.assertEquals(instance, property.deserializeSetAndReturn(jsonParser, deserializationContext, instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn_NoIdProperty() throws IOException {
        Object instance = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        property.setAndReturn(instance, new Object());
    }

    @Test
    public void testSetAndReturn_WithIdProperty() throws IOException {
        Object instance = new TestObject();
        ObjectIdReader objectIdReader = new ObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object value = new Object();
        Assert.assertEquals(value, property.setAndReturn(instance, value));
    }

    private static class ObjectIdReader {
        public String propertyName = "property";
        public Class<?> idType = String.class;
        public PropertyMetadata metadata = new PropertyMetadata();
        public JsonDeserializer<?> deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        public String getPropertyName() {
            return propertyName;
        }

        public Class<?> getIdType() {
            return idType;
        }

        public PropertyMetadata getMetadata() {
            return metadata;
        }

        public JsonDeserializer<?> getDeserializer() {
            return deserializer;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        public void setIdType(Class<?> idType) {
            this.idType = idType;
        }

        public void setMetadata(PropertyMetadata metadata) {
            this.metadata = metadata;
        }

        public void setDeserializer(JsonDeserializer<?> deserializer) {
            this.deserializer = deserializer;
        }
    }

    private static class TestObject {
        private String property;

        public void setProperty(String property) {
            this.property = property;
        }

        public String getProperty() {
            return property;
        }
    }

    private static class TestAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }
    }

}