package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.JsonParser.Feature.WRITE_ENUMS_USING_TO_STRING;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.impl.*;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
        assertNotNull(serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
        assertNotNull(serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
        assertNotNull(serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, boolean.class, false);
        assertNotNull(serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, Object.class, false);
        assertNull(serializer);

        serializer = StdKeySerializers.getStdKeySerializer(config, null, false);
        assertNull(serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();

        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, Date.class);
        assertNotNull(serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, Calendar.class);
        assertNotNull(serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, String.class);
        assertNotNull(serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, boolean.class);
        assertNotNull(serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, Object.class);
        assertNull(serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, null);
        assertNull(serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(config, Enum.class);
        assertNotNull(serializer);
    }

    @Test
    public void testDynamicSerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, Object[].class);

        Object input = new Object[] { new Date(), new Calendar(), new Integer(1), new String("test") };
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        serializer.serialize(input, gen, config);

        JsonParser parser = new JsonFactory().createParser(System.in);
        parser.nextToken();
        assertNotNull(parser.getCurrentName());

        assertEquals("Date", parser.getCurrentName());
        parser.nextToken();
        assertEquals("Calendar", parser.getCurrentName());
        parser.nextToken();
        assertEquals("int", parser.getCurrentName());
        parser.nextToken();
        assertEquals("test", parser.getCurrentName());

        parser.close();
        gen.close();
    }

    @Test
    public void testStringKeySerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();

        assertEquals(StdKeySerializers.StringKeySerializer.class, serializer.getClass());

        Object input = "test";
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        serializer.serialize(input, gen, config);

        JsonParser parser = new JsonFactory().createParser(System.in);
        parser.nextToken();
        assertEquals("test", parser.getCurrentName());

        parser.close();
        gen.close();
    }

    @Test
    public void testEnumSerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        config.enable(WRITE_ENUMS_USING_TO_STRING);

        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, Enum.class);

        Object input = Enum.valueOf(MyEnum.class, "VALUE_1");
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        serializer.serialize(input, gen, config);

        JsonParser parser = new JsonFactory().createParser(System.in);
        parser.nextToken();
        assertEquals("VALUE_1", parser.getText());

        parser.close();
        gen.close();
    }
}