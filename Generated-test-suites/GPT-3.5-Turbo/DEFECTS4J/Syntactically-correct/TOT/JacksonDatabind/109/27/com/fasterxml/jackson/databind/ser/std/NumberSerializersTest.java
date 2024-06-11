
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> data = new HashMap<>();
        data.put("value", 42);

        String json = mapper.writeValueAsString(data);

        Assert.assertEquals("{\"value\":42}", json);
    }

    @Test
    public void testShortSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> data = new HashMap<>();
        data.put("value", (short) 123);

        String json = mapper.writeValueAsString(data);

        Assert.assertEquals("{\"value\":123}", json);
    }

    @Test
    public void testLongSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> data = new HashMap<>();
        data.put("value", 9223372036854775807L);

        String json = mapper.writeValueAsString(data);

        Assert.assertEquals("{\"value\":9223372036854775807}", json);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> data = new HashMap<>();
        data.put("value", 3.14f);

        String json = mapper.writeValueAsString(data);

        Assert.assertEquals("{\"value\":3.14}", json);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> data = new HashMap<>();
        data.put("value", 3.14159265359);

        String json = mapper.writeValueAsString(data);

        Assert.assertEquals("{\"value\":3.14159265359}", json);
    }
}

