package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, String.class, true));
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, int.class, true));
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, Date.class, true));
    }

    @Test
    public void testGetFallbackKeySerializer() {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();

        assertNotNull(StdKeySerializers.getFallbackKeySerializer(null, Enum.class));
        assertNotNull(StdKeySerializers.getStdKeySerializer(null, String.class, true));
    }

    @Test
    public void testDefaultKeySerializer() {
        StdKeySerializers.Default keySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_DATE, Date.class);

        assertNotNull(keySerializer);
    }

    @Test
    public void testDynamicKeySerializer() {
        StdKeySerializers.Dynamic keySerializer = new StdKeySerializers.Dynamic();

        assertNotNull(keySerializer);
    }

    @Test
    public void testStringKeySerializer() {
        StdKeySerializers.StringKeySerializer keySerializer = new StdKeySerializers.StringKeySerializer();

        assertNotNull(keySerializer);
    }

}