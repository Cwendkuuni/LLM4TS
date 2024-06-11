import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testConstructor() {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, "type", false, null, As.PROPERTY);
        assertNotNull(deserializer);
        assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
    }

    @Test
    public void testForProperty() {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, "type", false, null, As.PROPERTY);
        TypeDeserializer deserializerProp = deserializer.forProperty(null);
        assertNotNull(deserializerProp);
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, "type", false, null, As.PROPERTY);
        JsonParser parser = new JsonFactory().createParser("{\"type\":\"value\"}");
        DeserializationContext ctxt = new DefaultDeserializationContext.Builder().build();
        Object result = deserializer.deserializeTypedFromObject(parser, ctxt);
        assertNotNull(result);
        assertEquals("value", result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, "type", false, null, As.PROPERTY);
        JsonParser parser = new JsonFactory().createParser("[{\"type\":\"value\"}]");
        DeserializationContext ctxt = new DefaultDeserializationContext.Builder().build();
        Object result = deserializer.deserializeTypedFromAny(parser, ctxt);
        assertNotNull(result);
        assertTrue(result instanceof Object[]);
    }
}