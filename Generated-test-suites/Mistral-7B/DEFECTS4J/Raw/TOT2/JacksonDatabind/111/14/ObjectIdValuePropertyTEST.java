
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.datatype.jsr310.*;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;
import com.fasterxml.jackson.module.afterburner.*;
import com.fasterxml.jackson.module.paramnames.*;

public class ObjectIdValuePropertyTest {

    static class IdReaderStub implements ObjectIdReader {

        @Override
        public PropertyName propertyName() {
            return new PropertyName("id");
        }

        @Override
        public Class<?> idType() {
            return Long.class;
        }

        @Override
        public ObjectIdGenerator idGenerator() {
            return new AlwaysNullObjectIdGenerator();
        }

        @Override
        public ObjectIdResolver idResolver() {
            return new AlwaysNullObjectIdResolver();
        }

        @Override
        public JsonDeserializer<ObjectId> idDeserializer() {
            return null;
        }

        @Override
        public ObjectIdGenerator idGenerator(DeserializationConfig config) throws MismatchedInputException {
            return null;
        }

        @Override
        public ObjectIdGenerator idGenerator(DeserializationConfig config, BeanProperty property) {
            return null;
        }

        @Override
        public ObjectIdGenerator idGenerator(DeserializationConfig config, BeanProperty property, JsonDeserializer<?> deserializer) {
            return null;
        }

        @Override
        public ObjectIdResolver idResolver(DeserializationConfig config, BeanProperty property) {
            return null;
        }
    }

    @Test
    public void testSerializeAndDeserialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReader objectIdReader = mapper.readerFor(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.getParser();
        ObjectNode node = factory.createObjectNode();
        node.put("id", "123");

        parser.readValueAs(Object.class, node);
        property.deserializeAndSet(parser, null, null);

        Object id = property.getValue();
        assertEquals("123", id);
    }

    @Test(expected = IOException.class)
    public void testDeserializeFailure() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReader objectIdReader = mapper.readerFor(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.getParser();
        ObjectNode node = factory.createObjectNode();
        node.put("id", "invalid_json");

        property.deserializeAndSet(parser, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testIdGeneratorNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReader objectIdReader = new IdReaderStub() {
            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }
        };
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.getParser();
        ObjectNode node = factory.createObjectNode();
        node.put("id", "123");

        property.deserializeAndSet(parser, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testIdResolverNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReader objectIdReader = new IdReaderStub() {
            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }
        };
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.getParser();
        ObjectNode node = factory.createObjectNode();
        node.put("id", "123");

        property.deserializeAndSet(parser, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testInstanceNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReader objectIdReader = mapper.readerFor(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.getParser();
        ObjectNode node = factory.createObjectNode();
        node.put("id", "123");

        property.deserializeAndSet(parser, null, null);
        property.set(null, "value");
    }

    @Test
    public void testValueNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReader objectIdReader = mapper.readerFor(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.getParser();
        ObjectNode node = factory.createObjectNode();
        node.put("id", null);

        property.deserializeAndSet(parser, null, null);
        Object id = property.getValue();
        assertEquals(null, id);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIdPropertyNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReader objectIdReader = mapper.readerFor(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.getParser();
        ObjectNode node = factory.createObjectNode();
        node.put("id", "123");

        property.deserializeAndSet(parser, null, null);
        property.set(null, null);
    }
}
