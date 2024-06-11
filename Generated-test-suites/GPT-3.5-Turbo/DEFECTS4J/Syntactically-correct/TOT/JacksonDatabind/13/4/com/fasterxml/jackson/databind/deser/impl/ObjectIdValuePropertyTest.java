import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.IOException;

public class ObjectIdValuePropertyTest {

    private static ObjectMapper objectMapper;

    @BeforeClass
    public static void setUp() {
        objectMapper = new ObjectMapper(new JsonFactory());
        objectMapper.registerModule(new SimpleModule().addDeserializer(ObjectIdReader.class, new ObjectIdReaderDeserializer()));
    }

    // Test for ObjectIdValueProperty constructor with ObjectIdReader and PropertyMetadata
    @Test
    public void testObjectIdValuePropertyConstructor1() {
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdReader objectIdReader = new ObjectIdReader.Builder()
                .withPropertyName("id")
                .withIdType(Object.class)
                .withDeserializer(new StdDeserializer<Object>(Object.class) {
                    @Override
                    public Object deserialize(JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws IOException {
                        return null;
                    }
                })
                .build();

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);

        Assert.assertEquals("id", objectIdValueProperty.getName().getSimpleName());
        Assert.assertEquals(Object.class, objectIdValueProperty.getType().getRawClass());
        Assert.assertEquals(metadata, objectIdValueProperty.getMetadata());
        Assert.assertNotNull(objectIdValueProperty.getDeserializer());
    }

    // Test for ObjectIdValueProperty constructor with ObjectIdValueProperty and JsonDeserializer
    @Test
    public void testObjectIdValuePropertyConstructor2() {
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdReader objectIdReader = new ObjectIdReader.Builder()
                .withPropertyName("id")
                .withIdType(Object.class)
                .withDeserializer(new StdDeserializer<Object>(Object.class) {
                    @Override
                    public Object deserialize(JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws IOException {
                        return null;
                    }
                })
                .build();

        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(src, new StdDeserializer<Object>(Object.class) {
            @Override
            public Object deserialize(JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws IOException {
                return null;
            }
        });

        Assert.assertEquals("id", objectIdValueProperty.getName().getSimpleName());
        Assert.assertEquals(Object.class, objectIdValueProperty.getType().getRawClass());
        Assert.assertEquals(metadata, objectIdValueProperty.getMetadata());
        Assert.assertNotNull(objectIdValueProperty.getDeserializer());
    }

    // Test for ObjectIdValueProperty with ObjectIdValueProperty and PropertyName
    @Test
    public void testObjectIdValuePropertyConstructor3() {
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdReader objectIdReader = new ObjectIdReader.Builder()
                .withPropertyName("id")
                .withIdType(Object.class)
                .withDeserializer(new StdDeserializer<Object>(Object.class) {
                    @Override
                    public Object deserialize(JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws IOException {
                        return null;
                    }
                })
                .build();

        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(src, new PropertyName("newName"));

        Assert.assertEquals("newName", objectIdValueProperty.getName().getSimpleName());
        Assert.assertEquals(Object.class, objectIdValueProperty.getType().getRawClass());
        Assert.assertEquals(metadata, objectIdValueProperty.getMetadata());
        Assert.assertNotNull(objectIdValueProperty.getDeserializer());
    }

    // Test for ObjectIdValueProperty with ObjectIdValueProperty and String
    @Test
    public void testObjectIdValuePropertyConstructor4() {
        PropertyMetadata metadata = new PropertyMetadata();
        ObjectIdReader objectIdReader = new ObjectIdReader.Builder()
                .withPropertyName("id")
                .withIdType(Object.class)
                .withDeserializer(new StdDeserializer<Object>(Object.class) {
                    @Override
                    public Object deserialize(JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws IOException {
                        return null;
                    }
                })
                .build();

        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, metadata);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(src, "newName");

        Assert.assertEquals("newName", objectIdValueProperty.getName().getSimpleName());
        Assert.assertEquals(Object.class, objectIdValueProperty.getType().getRawClass());
        Assert.assertEquals(metadata, objectIdValueProperty.getMetadata());
        Assert.assertNotNull(objectIdValueProperty.getDeserializer());
    }

    // Test for getAnnotation method
    @Test
    public void testGetAnnotation() {
        ObjectIdReader objectIdReader = new ObjectIdReader.Builder()
                .withPropertyName("id")
                .withIdType(Object.class)
                .withDeserializer(new StdDeserializer<Object>(Object.class) {
                    @Override
                    public Object deserialize(JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws IOException {
                        return null;
                    }
                })
                .build();

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        Assert.assertNull(objectIdValueProperty.getAnnotation(JsonCreator.class));
    }

    // Test for getMember method
    @Test
    public void testGetMember() {
        ObjectIdReader objectIdReader = new ObjectIdReader.Builder()
                .withPropertyName("id")
                .withIdType(Object.class)
                .withDeserializer(new StdDeserializer<Object>(Object.class) {
                    @Override
                    public Object deserialize(JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws IOException {
                        return null;
                    }
                })
                .build();

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        Assert.assertNull(objectIdValueProperty.getMember());
    }

    // Test for deserializeAndSet method
    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader.Builder()
                .withPropertyName("id")
                .withIdType(Object.class)
                .withDeserializer(new StdDeserializer<Object>(Object.class) {
                    @Override
                    public Object deserialize(JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws IOException {
                        return "test";
                    }
                })
                .build();

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        Object instance = new Object();
        objectIdValueProperty.deserializeAndSet(null, null, instance);

        Assert.assertEquals("test", instance);
    }

    // Test for deserializeSetAndReturn method
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader.Builder()
                .withPropertyName("id")
                .withIdType(Object.class)
                .withDeserializer(new StdDeserializer<Object>(Object.class) {
                    @Override
                    public Object deserialize(JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws IOException {
                        return "test";
                    }
                })
                .build();

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        Object instance = new Object();
        Object result = objectIdValueProperty.deserializeSetAndReturn(null, null, instance);

        Assert.assertEquals(instance, result);
        Assert.assertEquals("test", instance);
    }

    // Test for set method - UnsupportedOperationException expected
    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader.Builder()
                .withPropertyName("id")
                .withIdType(Object.class)
                .withDeserializer(new StdDeserializer<Object>(Object.class) {
                    @Override
                    public Object deserialize(JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws IOException {
                        return null;
                    }
                })
                .build();

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        objectIdValueProperty.set(new Object(), new Object());
    }

    // Test for setAndReturn method - UnsupportedOperationException expected
    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithoutIdProperty() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader.Builder()
                .withPropertyName("id")
                .withIdType(Object.class)
                .withDeserializer(new StdDeserializer<Object>(Object.class) {
                    @Override
                    public Object deserialize(JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws IOException {
                        return null;
                    }
                })
                .build();

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        objectIdValueProperty.setAndReturn(new Object(), new Object());
    }

    // Custom deserializer for ObjectIdReader
    private static class ObjectIdReaderDeserializer extends StdDeserializer<ObjectIdReader> {
        public ObjectIdReaderDeserializer() {
            super(ObjectIdReader.class);
        }

        @Override
        public ObjectIdReader deserialize(JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws IOException {
            return new ObjectIdReader.Builder()
                    .withPropertyName("id")
                    .withIdType(Object.class)
                    .withDeserializer(new StdDeserializer<Object>(Object.class) {
                        @Override
                        public Object deserialize(JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws IOException {
                            return null;
                        }
                    })
                    .build();
        }
    }
}