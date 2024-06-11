package com.example.junit;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.*;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

// ... Test cases from all three experts are included here ...

@Test
public void testDeserializeWithJsonObject() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    JsonParser parser = mapper.getFactory().createParser("{\"key\":\"value\"}");

    NullifyingDeserializer deserializer = new NullifyingDeserializer();
    Object result = deserializer.deserialize(parser, new DeserializationContext());

    assertNull(result);
}

@Test
public void testDeserializeWithJsonArray() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    JsonParser parser = mapper.getFactory().createParser("[1,2,3]");

    NullifyingDeserializer deserializer = new NullifyingDeserializer();
    Object result = deserializer.deserializeWithType(parser, new DeserializationContext(), null);

    assertNull(result);
}

// ...

}