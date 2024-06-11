package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.JsonCodec.NumberType.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.std.*;

import org.junit.Test;

public class NumberSerializersTest {
    @Test
    public void testNumberSerializers_addAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.LinkedHashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertEquals(11, allDeserializers.size());
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    static class TestBean {
        private Integer intField;
        private Integer intArray[];
        private Long longField;
        private Long longArray[];
        private Short shortField;
        private Short shortArray[];
        private Double doubleField;
        private Double doubleArray[];
        private Float floatField;
        private Float floatArray[];

        public Integer getIntField() {
            return intField;
        }

        public void setIntField(Integer intField) {
            this.intField = intField;
        }

        public Integer[] getIntArray() {
            return intArray;
        }

        public void setIntArray(Integer[] intArray) {
            this.intArray = intArray;
        }

        public Long getLongField() {
            return longField;
        }

        public void setLongField(Long longField) {
            this.longField = longField;
        }

        public Long[] getLongArray() {
            return longArray;
        }

        public void setLongArray(Long[] longArray) {
            this.longArray = longArray;
        }

        public Short getShortField() {
            return shortField;
        }

        public void setShortField(Short shortField) {
            this.shortField = shortField;
        }

        public Short[] getShortArray() {
            return shortArray;
        }

        public void setShortArray(Short[] shortArray) {
            this.shortArray = shortArray;
        }

        public Double getDoubleField() {
            return doubleField;
        }

        public void setDoubleField(Double doubleField) {
            this.doubleField = doubleField;
        }

        public Double[] getDoubleArray() {
            return doubleArray;
        }

        public void setDoubleArray(Double[] doubleArray) {
            this.doubleArray = doubleArray;
        }

        public Float getFloatField() {
            return floatField;
        }

        public void setFloatField(Float floatField) {
            this.floatField = floatField;
        }

        public Float[] getFloatArray() {
            return floatArray;
        }

        public void setFloatArray(Float[] floatArray) {
            this.floatArray = floatArray;
        }
    }

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();
        assertEquals("1", shortSerializer.serializeToString(Short.valueOf("1")));
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        assertEquals("1", integerSerializer.serializeToString(Integer.valueOf("1")));
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        assertEquals("1", intLikeSerializer.serializeToString(Integer.valueOf("1")));
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer(Long.class);
        assertEquals("1", longSerializer.serializeToString(Long.valueOf("1")));
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = new FloatSerializer();
        assertEquals("1.0", floatSerializer.serializeToString(Float.valueOf("1")));
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
        assertEquals("1.0", doubleSerializer.serializeToString(Double.valueOf("1")));
    }

    @Test
    public void testBaseClass() {
        Base<Object> base = new Base<Object>(Short.class, JsonParser.NumberType.INT, "number");
        assertEquals("number", base.getSchema(null, null).get("type").textValue());
    }

    @Test
    public void testSerializeWithType() throws IOException {
        ObjectWriter writer = new ObjectMapper().writerWithTypeValue();
        TestBean testBean = new TestBean();
        testBean.intField = 1;
        testBean.longField = 1L;
        testBean.shortField = (short) 1;
        testBean.doubleField = 1.0;
        testBean.floatField = 1.0f;

        String jsonWithType = writer.writeValueAsString(testBean);
        JsonParser parser = new ObjectMapper().getFactory().createParser(jsonWithType);

        JsonNode node = parser.readValueAsTree();
        assertEquals("{\"intField\":1,\"longField\":1,\"shortField\":1,\"doubleField\":1.0,\"floatField\":1.0}", node.toString());

        JsonNode nodeWithoutType = parser.readValueAsTree();
        assertEquals("{\"intField\":1,\"longField\":1,\"shortField\":1,\"doubleField\":1.0,\"floatField\":1.0}", nodeWithoutType.toString());
    }

    @Test
    public void testContextualSerializer() throws IOException {
        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        TestBean testBean = new TestBean();
        testBean.intField = 1;
        testBean.longField = 1L;
        testBean.shortField = (short) 1;
        testBean.doubleField = 1.0;
        testBean.floatField = 1.0f;

        String jsonWithToString = writer.writeValueAsString(testBean);
        JsonParser parser = new ObjectMapper().getFactory().createParser(jsonWithToString);

        JsonNode node = parser.readValueAsTree();
        assertEquals("{\"intField\":1,\"longField\":1,\"shortField\":1,\"doubleField\":1.0,\"floatField\":1.0}", node.toString());
    }
}