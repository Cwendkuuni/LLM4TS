package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Dynamic;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.StringKeySerializer;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();

        Class<?> rawKeyTypes[] = {Object.class, String.class, int.class, Class.class, Date.class, Calendar.class,
                java.util.UUID.class};
        JsonSerializer<Object> serializers[] = new JsonSerializer[rawKeyTypes.length];

        for (int i = 0; i < rawKeyTypes.length; i++) {
            JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, rawKeyTypes[i], false);
            serializers[i] = serializer;
            testSerializer(serializers[i], rawKeyTypes[i]);
        }
    }

    @Test
    public void testGetFallbackKeySerializer() {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();

        Class<?> rawKeyTypes[] = {null, Enum.class, Object.class, int.class, Class.class, Date.class, Calendar.class,
                java.util.UUID.class};
        JsonSerializer<Object> serializers[] = new JsonSerializer[rawKeyTypes.length];

        for (int i = 0; i < rawKeyTypes.length; i++) {
            JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(config, rawKeyTypes[i]);
            serializers[i] = serializer;
            testSerializer(serializers[i], rawKeyTypes[i]);
        }
    }

    private void testSerializer(JsonSerializer<Object> serializer, Class<?> rawKeyType) {
        try {
            serializer.serialize(null, new JsonGenerator(), new SerializerProvider());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals(StdKeySerializers.class, serializer.getClass().getPackage().getName());
        assertEquals(rawKeyType, serializer.getParameterizedType(0).getRawType());
    }

    @Test
    public void testDynamicSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        PropertySerializerMap dynamicSerializers = PropertySerializerMap.emptyForProperties();

        Class<?> dynamicClass = DynamicTest.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(config, dynamicClass, true);

        assertEquals(Dynamic.class, serializer.getClass());
        assertEquals(String.class, serializer.getParameterizedType(0).getRawType());

        ObjectWriter writer = mapper.writer().withRootValueSerializer(serializer);
        String json = writer.writeValueAsString(new DynamicTest("test"));

        assertEquals("{\"name\":\"test\"}", json);

        dynamicSerializers = (PropertySerializerMap) serializer.readResolve();
        JsonSerializer<Object> serializer2 = dynamicSerializers.serializerFor(dynamicClass);

        assertEquals(StringKeySerializer.class, serializer2.getClass());
    }
}

class DynamicTest {
    String name;

    public DynamicTest(String name) {
        this.name = name;
    }
}