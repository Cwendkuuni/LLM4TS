import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;

public class StringCollectionDeserializerTest {

    private StringCollectionDeserializer deserializer;
    private ObjectMapper objectMapper;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;

    @Before
    public void setUp() throws Exception {
        deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        objectMapper = new ObjectMapper();
        jsonParser = objectMapper.getFactory().createParser("[]");
        deserializationContext = objectMapper.getDeserializationContext();
    }

    @Test
    public void testDeserialize() throws IOException {
        Collection<String> result = deserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeWithValues() throws IOException {
        jsonParser = objectMapper.getFactory().createParser("[\"value1\",\"value2\"]");
        Collection<String> result = deserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("value2"));
    }

    @Test
    public void testDeserializeNonArray() throws IOException {
        jsonParser = objectMapper.getFactory().createParser("\"value\"");
        Collection<String> result = deserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains("value"));
    }

    @Test
    public void testDeserializeNullValue() throws IOException {
        deserializer = new StringCollectionDeserializer(null, null, null, null, NullValueProvider.forValue(null), null);
        Collection<String> result = deserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonToken jsonToken = JsonToken.START_ARRAY;
        TypeDeserializer typeDeserializer = null;
        Object result = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertNull(result);
    }

    @Test
    public void testIsCachable() {
        assertTrue(deserializer.isCachable());
    }

    @Test
    public void testGetContentDeserializer() {
        JsonDeserializer<Object> result = deserializer.getContentDeserializer();
        assertNull(result);
    }

    @Test
    public void testGetContentDeserializerWithDeser() {
        deserializer = new StringCollectionDeserializer(null, new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        }, null);
        JsonDeserializer<Object> result = deserializer.getContentDeserializer();
        assertNotNull(result);
    }

    @Test
    public void testGetValueInstantiator() {
        ValueInstantiator result = deserializer.getValueInstantiator();
        assertNull(result);
    }

    @Test
    public void testDeserializeUsingCustom() throws IOException {
        Collection<String> result = deserializer.deserializeUsingCustom(jsonParser, deserializationContext, new ArrayList<String>(), new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        });
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeUsingCustomWithValues() throws IOException {
        jsonParser = objectMapper.getFactory().createParser("[\"value1\",\"value2\"]");
        Collection<String> result = deserializer.deserializeUsingCustom(jsonParser, deserializationContext, new ArrayList<String>(), new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.nextTextValue();
            }
        });
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("value2"));
    }

    @Test
    public void testDeserializeUsingCustomWithNullValue() throws IOException {
        deserializer = new StringCollectionDeserializer(null, null, null, new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        }, NullValueProvider.forValue(null), null);
        Collection<String> result = deserializer.deserializeUsingCustom(jsonParser, deserializationContext, new ArrayList<String>(), deserializer.getContentDeserializer());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testHandleNonArray() throws IOException {
        jsonParser = objectMapper.getFactory().createParser("\"value\"");
        Collection<String> result = deserializer.handleNonArray(jsonParser, deserializationContext, new ArrayList<String>());
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains("value"));
    }

    @Test
    public void testHandleNonArrayWithNullProvider() throws IOException {
        deserializer = new StringCollectionDeserializer(null, null, null, null, NullValueProvider.forValue(null), null);
        Collection<String> result = deserializer.handleNonArray(jsonParser, deserializationContext, new ArrayList<String>());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testHandleNonArrayWithDeser() throws IOException {
        deserializer = new StringCollectionDeserializer(null, new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        }, null, null, null, null);
        jsonParser = objectMapper.getFactory().createParser("\"value\"");
        Collection<String> result = deserializer.handleNonArray(jsonParser, deserializationContext, new ArrayList<String>());
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains("value"));
    }

    @Test(expected = RuntimeException.class)
    public void testHandleNonArrayWithInvalidToken() throws IOException {
        jsonParser = objectMapper.getFactory().createParser("123");
        deserializer.handleNonArray(jsonParser, deserializationContext, new ArrayList<String>());
    }

    @Test
    public void testDeserializationWithCustomValueDeserializer() throws IOException {
        deserializer = new StringCollectionDeserializer(null, new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.nextTextValue().toUpperCase();
            }
        }, null, null, null, null);
        jsonParser = objectMapper.getFactory().createParser("[\"value1\",\"value2\"]");
        Collection<String> result = deserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("VALUE1"));
        assertTrue(result.contains("VALUE2"));
    }

    @Test
    public void testDeserializeWithContextual() throws IOException {
        List<String> data = Arrays.asList("value1", "value2");
        objectMapper.registerModule(new com.fasterxml.jackson.databind.module.SimpleModule().addDeserializer(Collection.class, new JsonDeserializer<Collection<String>>() {
            @Override
            public Collection<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return data;
            }
        }));
        deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        jsonParser = objectMapper.getFactory().createParser("[]");
        Collection<String> result = deserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(result);
        assertEquals(data, result);
    }
}