
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

public class StringCollectionDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        Collection<String> result = deserializer.deserialize(null, null);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        Object result = deserializer.deserializeWithType(null, null, null);

        Assert.assertNull(result);
    }

    @Test
    public void testHandleNonArray() throws Exception {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        Collection<String> result = new ArrayList<>();
        Collection<String> actual = deserializer.handleNonArray(null, null, result);

        Assert.assertEquals(result, actual);
    }

}
