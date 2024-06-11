package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for NullifyingDeserializer class.
 */
public class NullifyingDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeNullifyingDeserializer() throws IOException {
        JsonParser p = mapper.getFactory().createParser("[]");
        p.setCodec(mapper);
        p.setContext(new TestContextFinder().findContext(NullifyingDeserializer.class, p.getCurrentLocation()));
        assertEquals(NullifyingDeserializer.instance, p.getCurrentSerializer());
        assertNull(NullifyingDeserializer.instance.deserialize(p, new TestContext()));
    }

    @Test
    public void testDeserializeWithTypeNullifyingDeserializer() throws IOException {
        JsonParser p = mapper.getFactory().createParser("[]");
        p.setCodec(mapper);
        p.setContext(new TestContextFinder().findContext(NullifyingDeserializer.class, p.getCurrentLocation()));
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializer();
        assertEquals(NullifyingDeserializer.instance, p.getCurrentSerializer());
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, new TestContext(), typeDeserializer));
    }

    @Test
    public void testDeserializeWithStartArrayNullifyingDeserializer() throws IOException {
        JsonParser p = mapper.getFactory().createParser("[]");
        p.setCodec(mapper);
        p.setContext(new TestContextFinder().findContext(NullifyingDeserializer.class, p.getCurrentLocation()));
        assertEquals(JsonTokenId.ID_START_ARRAY, p.nextTokenID());
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, new TestContext(), mapper.getTypeDeserializer()));
    }

    @Test
    public void testDeserializeWithStartObjectNullifyingDeserializer() throws IOException {
        JsonParser p = mapper.getFactory().createParser("{\"name\":\"John\"}");
        p.setCodec(mapper);
        p.setContext(new TestContextFinder().findContext(NullifyingDeserializer.class, p.getCurrentLocation()));
        assertEquals(JsonTokenId.ID_START_OBJECT, p.nextTokenID());
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, new TestContext(), mapper.getTypeDeserializer()));
    }

    @Test
    public void testDeserializeWithFieldNameNullifyingDeserializer() throws IOException {
        JsonParser p = mapper.getFactory().createParser("{\"name\":null}");
        p.setCodec(mapper);
        p.setContext(new TestContextFinder().findContext(NullifyingDeserializer.class, p.getCurrentLocation()));
        assertEquals(JsonTokenId.FIELD_NAME, p.nextTokenID());
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, new TestContext(), mapper.getTypeDeserializer()));
    }

    @Test
    public void testDeserializeIncorrectTokenNullifyingDeserializer() throws IOException {
        JsonParser p = mapper.getFactory().createParser("{\"name\":null, 123}");
        p.setCodec(mapper);
        p.setContext(new TestContextFinder().findContext(NullifyingDeserializer.class, p.getCurrentLocation()));
        assertEquals(JsonTokenId.VALUE_NUMBER_INT, p.nextTokenID());
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, new TestContext(), mapper.getTypeDeserializer()));
    }
}

class TestContext extends DeserializationContext {
    // Empty implementation
}

class TestContextFinder {
    TestContextFinder() {
    }

    DeserializationContext findContext(Class<?> cls, Location loc) {
        return new TestContext();
    }
}