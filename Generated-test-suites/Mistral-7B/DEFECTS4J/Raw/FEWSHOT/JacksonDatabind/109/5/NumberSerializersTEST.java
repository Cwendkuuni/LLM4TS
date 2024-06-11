package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.JsonParser.NumberType.INT;
import static com.fasterxml.jackson.core.JsonParser.NumberType.LONG;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Test cases for {@link NumberSerializers} class.
 */
@SuppressWarnings("unchecked")
public class NumberSerializersTest {

    @Test
    public void testNumberSerializers() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);

        // Test mapping of various number types
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        JsonSerializer<?> serializerForType;

        serializerForType = allDeserializers.get(Integer.class.getName());
        assertEquals(Integer.class, serializerForType.handledType());
        assertInstanceOf(IntegerSerializer.class, serializerForType);

        serializerForType = allDeserializers.get(Integer.TYPE.getName());
        assertEquals(Integer.TYPE, serializerForType.handledType());
        assertInstanceOf(IntegerSerializer.class, serializerForType);

        serializerForType = allDeserializers.get(Long.class.getName());
        assertEquals(Long.class, serializerForType.handledType());
        assertInstanceOf(LongSerializer.class, serializerForType);

        serializerForType = allDeserializers.get(Long.TYPE.getName());
        assertEquals(Long.TYPE, serializerForType.handledType());
        assertInstanceOf(LongSerializer.class, serializerForType);

        serializerForType = allDeserializers.get(Short.class.getName());
        assertEquals(Short.class, serializerForType.handledType());
        assertInstanceOf(ShortSerializer.class, serializerForType);

        serializerForType = allDeserializers.get(Short.TYPE.getName());
        assertEquals(Short.TYPE, serializerForType.handledType());
        assertInstanceOf(ShortSerializer.class, serializerForType);

        serializerForType = allDeserializers.get(Byte.class.getName());
        assertEquals(Byte.class, serializerForType.handledType());
        assertInstanceOf(IntLikeSerializer.class, serializerForType);

        serializerForType = allDeserializers.get(Byte.TYPE.getName());
        assertEquals(Byte.TYPE, serializerForType.handledType());
        assertInstanceOf(IntLikeSerializer.class, serializerForType);

        serializerForType = allDeserializers.get(Float.class.getName());
        assertEquals(Float.class, serializerForType.handledType());
        assertInstanceOf(FloatSerializer.class, serializerForType);

        serializerForType = allDeserializers.get(Float.TYPE.getName());
        assertEquals(Float.TYPE, serializerForType.handledType());
        assertInstanceOf(FloatSerializer.class, serializerForType);

        serializerForType = allDeserializers.get(Double.class.getName());
        assertEquals(Double.class, serializerForType.handledType());
        assertInstanceOf(DoubleSerializer.class, serializerForType);

        serializerForType = allDeserializers.get(Double.TYPE.getName());
        assertEquals(Double.TYPE, serializerForType.handledType());
        assertInstanceOf(DoubleSerializer.class, serializerForType);

        // Test serialization of various number types
        ObjectWriter writer = mapper.writer();
        ObjectWriter writerWithType = mapper.writerWithTypeLoader(writer.getTypeFactory());

        Integer i = 123;
        Long l = 456L;
        Float f = 123.45f;
        Double d = 123.45;

        String jsonWithAnnotations = mapper.writeValueAsString(
                new Object() {
                    @JsonFormat(shape = JsonFormat.Shape.STRING)
                    private Integer intAnnotated;

                    @JsonFormat(shape = JsonFormat.Shape.STRING, numberFormat = "0.000")
                    private Float floatAnnotated;

                    @JsonFormat(shape = JsonFormat.Shape.STRING, numberFormat = "0.##E0")
                    private Double doubleAnnotated;

                    public Integer getIntAnnotated() {
                        return intAnnotated;
                    }

                    public Float getFloatAnnotated() {
                        return floatAnnotated;
                    }

                    public Double getDoubleAnnotated() {
                        return doubleAnnotated;
                    }

                    public Integer getInt() {
                        return i;
                    }

                    public Long getLong() {
                        return l;
                    }

                    public Float getFloat() {
                        return f;
                    }

                    public Double getDouble() {
                        return d;
                    }
                });

        String intJson = writer.writeValueAsString(i);
        String longJson = writer.writeValueAsString(l);
        String intJsonWithType = writerWithType.writeValueAsString(i);
        String longJsonWithType = writerWithType.writeValueAsString(l);
        String floatJson = writer.writeValueAsString(f);
        String doubleJson = writer.writeValueAsString(d);
        String intJsonAnnotated = jsonWithAnnotations.substring(
                jsonWithAnnotations.indexOf("\"intAnnotated\":") + 14,
                jsonWithAnnotations.indexOf(",", jsonWithAnnotations.indexOf("\"intAnnotated\":")))
                .replace("\"", "").replace(":", "");
        String floatJsonAnnotated = jsonWithAnnotations.substring(
                jsonWithAnnotations.indexOf("\"floatAnnotated\":") + 14,
                jsonWithAnnotations.indexOf(",", jsonWithAnnotations.indexOf("\"floatAnnotated\":")))
                .replace("\"", "").replace(":", "");
        String doubleJsonAnnotated = jsonWithAnnotations.substring(
                jsonWithAnnotations.indexOf("\"doubleAnnotated\":") + 14,
                jsonWithAnnotations.indexOf(",", jsonWithAnnotations.indexOf("\"doubleAnnotated\":")))
                .replace("\"", "").replace(":", "");

        assertEquals(intJson, intJsonWithType);
        assertEquals(longJson, longJsonWithType);
        assertEquals(intJsonAnnotated, "123");
        assertEquals(floatJsonAnnotated, "123.450");
        assertEquals(doubleJsonAnnotated, "123.450E+00");

        assertEquals(intJson, writer.writeValueAsString(new Integer(i)));
        assertEquals(longJson, writer.writeValueAsString(l));
        assertEquals(floatJson, writer.writeValueAsString(f));
        assertEquals(doubleJson, writer.writeValueAsString(d));

        assertEquals(intJson, writer.writeValueAsString(i.shortValue()));
        assertEquals(longJson, writer.writeValueAsString(l.shortValue()));
        assertEquals(floatJson, writer.writeValueAsString(Float.intBitsToFloat(i.intValue())));
        assertEquals(doubleJson, writer.writeValueAsString(Double.longBitsToDouble(l.longValue())));

        // Test serialization with custom format
        JsonFormat intFormat = new JsonFormat();
        intFormat.setShape(JsonFormat.Shape.STRING);
        intFormat.setNumberFormat("000");

        JsonFormat floatFormat = new JsonFormat();
        floatFormat.setShape(JsonFormat.Shape.STRING);
        floatFormat.setNumberFormat("0.##");

        JsonFormat doubleFormat = new JsonFormat();
        doubleFormat.setShape(JsonFormat.Shape.STRING);
        doubleFormat.setNumberFormat("0.##E0");

        assertEquals("123", mapper.writer().with(intFormat).writeValueAsString(i));
        assertEquals("123.00", mapper.writer().with(intFormat).writeValueAsString(new Integer(i)));
        assertEquals("123", mapper.writer().with(floatFormat).writeValueAsString(f));
        assertEquals("123.45", mapper.writer().with(floatFormat).writeValueAsString(new Float(f)));
        assertEquals("123.450E+00", mapper.writer().with(doubleFormat).writeValueAsString(d));
        assertEquals("123.450E+00", mapper.writer().with(doubleFormat).writeValueAsString(new Double(d)));

        // Test null serialization
        assertNull(writer.writeValueAsString(null));
        assertNull(writerWithType.writeValueAsString(null));
    }
}