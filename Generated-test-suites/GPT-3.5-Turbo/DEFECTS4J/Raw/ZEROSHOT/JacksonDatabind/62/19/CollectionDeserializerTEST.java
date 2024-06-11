package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class CollectionDeserializerTest {

    private CollectionDeserializer deserializer;

    @Before
    public void setup() {
        deserializer = new CollectionDeserializer(null, null, null, null);
    }

    @Test
    public void testDeserialize() throws Exception {
        List<Object> result = new ArrayList<>();
        result.add(1);
        result.add(2);
        result.add(3);

        assertEquals(result, deserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        List<Object> result = new ArrayList<>();
        result.add(1);
        result.add(2);
        result.add(3);

        assertEquals(result, deserializer.deserializeWithType(null, null, null));
    }

    @Test
    public void testHandleNonArray() throws Exception {
        List<Object> result = new ArrayList<>();
        result.add(1);

        assertEquals(result, deserializer.handleNonArray(null, null, null));
    }
}