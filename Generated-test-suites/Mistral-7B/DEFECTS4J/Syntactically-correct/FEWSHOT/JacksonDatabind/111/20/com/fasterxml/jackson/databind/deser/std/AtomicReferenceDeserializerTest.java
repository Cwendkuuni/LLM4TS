package com.fasterxml.jackson.databind.deser.std;

import static com.fasterxml.jackson.core.util.TestUtils.constructObjectMapper;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.json.TypeIdResolver;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testAtomicReferenceDeserialization() throws JsonMappingException {
        ObjectMapper mapper = constructObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        TypeDeserializer typeDeserializer = new AtomicReferenceDeserializer(
                mapper.getTypeFactory().constructType(AtomicReference.class),
                null, null, null);

        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        JsonTypeInfo.Id intId = idResolver.findValueId("com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer", AtomicReference.class);

        AtomicReference<String> atomicRef = new AtomicReference<>("initialValue");
        String valueToSet = "newValue";

        // Test serialization with TypeId
        String json = mapper.writerWithTypeId(intId).writeValueAsString(atomicRef);
        assertEquals("{\"typeId\":\"" + intId.getId() + "\",\"value\":\"initialValue\"}", json);

        // Test deserialization with TypeId
        AtomicReference<String> deserializedRef = mapper.readerWithTypeId(intId).readValue(json);
        assertSame(atomicRef, deserializedRef);

        // Test setting a new value
        mapper.readValue(mapper.writerWithTypeId(intId).writeValueAsString(valueToSet), AtomicReference.class, atomicRef);
        assertEquals(valueToSet, atomicRef.get());
    }

    @Test
    public void testAtomicReferenceDeserializationWithoutTypeId() throws JsonMappingException {
        ObjectMapper mapper = constructObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test serialization without TypeId
        AtomicReference<String> atomicRef = new AtomicReference<>("initialValue");
        String json = mapper.writeValueAsString(atomicRef);
        assertEquals("{\"value\":\"initialValue\"}", json);

        // Test deserialization without TypeId
        AtomicReference<String> deserializedRef = mapper.readValue(json, AtomicReference.class);
        assertSame(atomicRef, deserializedRef);

        // Test setting a new value
        mapper.readValue("\"newValue\"", AtomicReference.class, atomicRef);
        assertEquals("newValue", atomicRef.get());
    }
}