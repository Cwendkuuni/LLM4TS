package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.JsonParser.NumberType.INT;
import static com.fasterxml.jackson.core.JsonParser.NumberType.LONG;
import static com.fasterxml.jackson.databind.JsonMapping.TypeSerializer.emptyInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.test.BaseTest;

public class NumberSerializersTest extends BaseTest {

    public void testNumberSerializers() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test serialization and deserialization of various types
        short inputShort = 127;
        Short outputShort = mapper.readValue(mapper.writeValueAsString(inputShort), Short.class);
        assertEquals(inputShort, outputShort.shortValue());

        int inputInt = 12345;
        Integer outputInt = mapper.readValue(mapper.writeValueAsString(inputInt), Integer.class);
        assertEquals(inputInt, outputInt.intValue());

        long inputLong = 9876543210L;
        Long outputLong = mapper.readValue(mapper.writeValueAsString(inputLong), Long.class);
        assertEquals(inputLong, outputLong.longValue());

        float inputFloat = 1.2345f;
        Float outputFloat = mapper.readValue(mapper.writeValueAsString(inputFloat), Float.class);
        assertEquals(inputFloat, outputFloat.floatValue(), 0.001f);

        double inputDouble = 1.23456789d;
        Double outputDouble = mapper.readValue(mapper.writeValueAsString(inputDouble), Double.class);
        assertEquals(inputDouble, outputDouble.doubleValue(), 0.000001);

        // Test serialization and deserialization of Integer and Long using IntLikeSerializer
        Number inputNumberInt = 123;
        Number outputNumberInt = mapper.readValue(mapper.writeValueAsString(inputNumberInt), Number.class);
        assertEquals(inputNumberInt.intValue(), outputNumberInt.intValue());

        Number inputNumberLong = 1234567890L;
        Number outputNumberLong = mapper.readValue(mapper.writeValueAsString(inputNumberLong), Number.class);
        assertEquals(inputNumberLong.longValue(), outputNumberLong.longValue());

        // Test serialization and deserialization using FloatSerializer and DoubleSerializer
        float inputFloatValue = 1.2345f;
        Float outputFloatValue = mapper.readValue(mapper.writeValueAsString(inputFloatValue), Float.class);
        assertEquals(inputFloatValue, outputFloatValue.floatValue(), 0.001f);

        double inputDoubleValue = 1.23456789d;
        Double outputDoubleValue = mapper.readValue(mapper.writeValueAsString(inputDoubleValue), Double.class);
        assertEquals(inputDoubleValue, outputDoubleValue.doubleValue(), 0.000001);

        // Test serialization and deserialization with JsonFormat annotation
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        short formattedShort = 127;
        String formattedShortString = mapper.writeValueAsString(formattedShort);
        short deserializedFormattedShort = mapper.readValue(formattedShortString, Short.class);
        assertEquals(inputShort, deserializedFormattedShort);

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Integer formattedInt = 12345;
        String formattedIntString = mapper.writeValueAsString(formattedInt);
        Integer deserializedFormattedInt = mapper.readValue(formattedIntString, Integer.class);
        assertEquals(inputInt, deserializedFormattedInt);
    }

    @Test
    public void testContextualSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<?> serializer = mapper.findValueSerializer(Short.class);
        assertNotNull(serializer);
        JsonSerializer<?> serializerForInt = mapper.findValueSerializer(Integer.class);
        assertNotNull(serializerForInt);
        assertEquals(ShortSerializer.class, serializer.getClass());
        assertEquals(IntegerSerializer.class, serializerForInt.getClass());
    }

    @Test
    public void testAddAll() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, JsonSerializer<?>> allDeserializers = new LinkedHashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals(11, allDeserializers.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAllWithNullMap() throws Exception {
        NumberSerializers.addAll(null);
    }
}