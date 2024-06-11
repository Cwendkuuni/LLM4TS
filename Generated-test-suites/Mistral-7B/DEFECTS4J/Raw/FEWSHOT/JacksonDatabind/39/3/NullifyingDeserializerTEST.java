package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for NullifyingDeserializer.
 */
public class NullifyingDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Test deserialize method with JsonParser containing various tokens.
     */
    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = mapper.getFactory().createParser("[]");
        assertNull(NullifyingDeserializer.instance.deserialize(p, null));

        p = mapper.getFactory().createParser("{ \"name\": \"John\" }");
        assertNull(NullifyingDeserializer.instance.deserialize(p, null));

        p = mapper.getFactory().createParser("{\"name\": null}");
        assertNull(NullifyingDeserializer.instance.deserialize(p, null));

        p = mapper.getFactory().createParser("{\"name\": [], 1, 2}");
        assertNull(NullifyingDeserializer.instance.deserialize(p, null));

        p = mapper.getFactory().createParser("{\"name\": false}");
        assertNull(NullifyingDeserializer.instance.deserialize(p, null));

        p = mapper.getFactory().createParser("{\"name\": null}");
        assertNull(NullifyingDeserializer.instance.deserialize(p, null));

        p = mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30}");
        assertNull(NullifyingDeserializer.instance.deserialize(p, null));
    }

    /**
     * Test deserializeWithType method with JsonParser containing various tokens.
     */
    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser p = mapper.getFactory().createParser("[]");
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, null, null));

        p = mapper.getFactory().createParser("{ \"name\": \"John\" }");
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, null, null));

        p = mapper.getFactory().createParser("{\"name\": null}");
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, null, null));

        p = mapper.getFactory().createParser("{\"name\": [], 1, 2}");
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, null, null));

        p = mapper.getFactory().createParser("{\"name\": false}");
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, null, null));

        p = mapper.getFactory().createParser("{\"name\": null}");
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, null, null));

        p = mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30}");
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, null, null));

        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(String[].class);
        p = mapper.getFactory().createParser("[\"apple\", \"banana\"]");
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, null, typeDeserializer));

        p = mapper.getFactory().createParser("{\"name\": [1, 2, 3], \"age\": 30}");
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, null, null));
    }

    /**
     * Test deserializeWithType method when JsonParser contains FieldName token.
     */
    @Test
    public void testDeserializeWithType_FieldName() throws IOException {
        JsonParser p = mapper.getFactory().createParser("{ \"name\": null }");
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, null, null));

        p = mapper.getFactory().createParser("{ \"name\": { \"foo\": null } }");
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, null, null));

        p = mapper.getFactory().createParser("{ \"name\": { \"foo\": [], 1, 2 } }");
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, null, null));

        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(String[].class);
        p = mapper.getFactory().createParser("{ \"name\": { \"foo\": [\"apple\", \"banana\"] } }");
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, null, typeDeserializer));
    }

    /**
     * Test deserializeWithType method when JsonParser contains START_ARRAY or START_OBJECT tokens.
     */
    @Test
    public void testDeserializeWithType_START_ARRAY_START_OBJECT() throws IOException {
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructType(String[].class);
        JsonParser p = mapper.getFactory().createParser("[\"apple\", \"banana\"]");
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, null, typeDeserializer));

        p = mapper.getFactory().createParser("{ \"name\": null, \"list\": [\"apple\", \"banana\"] }");
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, null, null));

        p = mapper.getFactory().createParser("{ \"name\": null, \"list\": [null, null] }");
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, null, null));
    }
}