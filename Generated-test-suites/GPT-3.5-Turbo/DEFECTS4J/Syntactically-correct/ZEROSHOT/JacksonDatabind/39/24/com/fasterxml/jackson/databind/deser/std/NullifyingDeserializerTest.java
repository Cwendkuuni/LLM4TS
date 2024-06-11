package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        Object result = nullifyingDeserializer.deserialize(parser, context);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        TypeDeserializer typeDeserializer = null;
        NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;
        Object result = nullifyingDeserializer.deserializeWithType(parser, context, typeDeserializer);
        assertNull(result);
    }
}