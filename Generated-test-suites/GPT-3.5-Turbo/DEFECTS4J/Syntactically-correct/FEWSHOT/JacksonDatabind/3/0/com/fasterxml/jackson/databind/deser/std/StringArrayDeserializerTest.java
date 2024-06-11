
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;


public class StringArrayDeserializerTest {
    
    private StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
    private JsonParser jsonParser = new ObjectMapper().getFactory().createParser("[\"apple\", \"banana\", \"cherry\"]");
    private DeserializationContext deserializationContext = new ObjectMapper().getDeserializationContext();
    private TypeDeserializer typeDeserializer = new ObjectMapper().getDeserializationConfig().findTypeDeserializer(deserializer.getType(), deserializationContext.getConfig(), jsonParser.getCurrentToken());

    @Test
    public void testDeserialize() throws Exception {
        String[] expected = {"apple", "banana", "cherry"};
        String[] actual = deserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        Object expected = new ObjectMapper().readValue("[\"apple\", \"banana\", \"cherry\"]", Object.class);
        Object actual = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeEmptyArray() throws Exception {
        String[] expected = {};
        jsonParser = new ObjectMapper().getFactory().createParser("[]");
        String[] actual = deserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testDeserializeWithNulls() throws Exception {
        String[] expected = {"apple", null, "cherry"};
        jsonParser = new ObjectMapper().getFactory().createParser("[\"apple\", null, \"cherry\"]");
        String[] actual = deserializer.deserialize(jsonParser, deserializationContext);
        assertArrayEquals(expected, actual);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeInvalidJson() throws Exception {
        jsonParser = new ObjectMapper().getFactory().createParser("[apple, banana, cherry]");
        deserializer.deserialize(jsonParser, deserializationContext);
    }
}
