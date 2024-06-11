package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("[]");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        Assert.assertNull(NullifyingDeserializer.instance.deserialize(p, ctxt));

        p = mapper.getFactory().createParser("{\"field\":[]}");
        ctxt = mapper.getDeserializationContext();
        Assert.assertNull(NullifyingDeserializer.instance.deserialize(p, ctxt));

        p = mapper.getFactory().createParser("{\"field\":1}");
        ctxt = mapper.getDeserializationContext();
        Assert.assertNull(NullifyingDeserializer.instance.deserialize(p, ctxt));
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        JsonParser p = mapper.getFactory().createParser("[]");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Assert.assertNull(NullifyingDeserializer.instance.deserializeWithType(p, ctxt, typeFactory.constructType(String[].class)));

        p = mapper.getFactory().createParser("{\"field\":[]}");
        ctxt = mapper.getDeserializationContext();
        Assert.assertNull(NullifyingDeserializer.instance.deserializeWithType(p, ctxt, typeFactory.constructType(Map.class)));
    }
}