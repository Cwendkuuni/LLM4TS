package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.impl.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.TypeUtils;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class JsonValueSerializerTest {

    @SuppressWarnings("unchecked")
    @JsonSerialize(using = JsonValueSerializer.class)
    static class TestBean {
        private int intValue;
        private String stringValue;
        private boolean booleanValue;

        public int getIntValue() {
            return intValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        public boolean getBooleanValue() {
            return booleanValue;
        }
    }

    @Test
    public void testSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonValueSerializer serializer = mapper
                .getTypeFactory()
                .constructValueType(TestBean.class, TestBean.class)
                .getSerializer();

        TestBean bean = new TestBean();
        bean.intValue = 1;
        bean.stringValue = "foo";
        bean.booleanValue = true;

        JsonGenerator gen = mapper.createGenerator(new ByteArrayOutputStream(), null);
        serializer.serialize(bean, gen, null);
        assertNotNull(gen.getOutputBuffer().toString());

        JsonNode node = mapper.readTree(new ByteArrayInputStream(gen.getOutputBuffer().toString().getBytes()));
        assertEquals(3, node.size());
        assertEquals(1, node.path("intValue").asInt());
        assertEquals("foo", node.path("stringValue").asText());
        assertEquals(true, node.path("booleanValue").asBoolean());
    }

    @Test
    public void testSerializationWithTypeSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = new TestBean();
        bean.intValue = 1;
        bean.stringValue = "foo";
        bean.booleanValue = true;

        JsonSerializer<TestBean> beanSerializer = mapper.findValueSerializer(TestBean.class, null);
        TypeSerializer typeSerializer = mapper.getTypeSerializer();

        JsonGenerator gen = mapper.createGenerator(new ByteArrayOutputStream(), null);
        JsonValueSerializer serializer = new JsonValueSerializer(
                AnnotatedMethod.construct(TestBean.class, "getIntValue", Integer.TYPE),
                beanSerializer);

        serializer.serializeWithType(bean, gen, null, typeSerializer);

        assertNotNull(gen.getOutputBuffer().toString());

        JsonNode node = mapper.readTree(new ByteArrayInputStream(gen.getOutputBuffer().toString().getBytes()));
        assertEquals(2, node.size());
        assertEquals(1, node.path("intValue").asInt());
        assertEquals(JsonType.INT, node.get("intValue").getType());
    }

    @Test
    public void testSerializationWithContextualSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JsonValueSerializer serializer1 = new JsonValueSerializer(
                AnnotatedMethod.construct(TestBean.class, "getStringValue", String.class),
                new StringSerializer());

        JsonValueSerializer serializer2 = mapper.getTypeFactory()
                .constructValueType(TestBean.class, TestBean.class)
                .getSerializer()
                .withResolved(null, null, false);

        assertEquals(serializer1, serializer2);

        TestBean bean = new TestBean();
        bean.stringValue = "foo";

        JsonGenerator gen = mapper.createGenerator(new ByteArrayOutputStream(), null);
        serializer1.serializeWithType(bean, gen, null, mapper.getTypeSerializer());

        assertNotNull(gen.getOutputBuffer().toString());

        JsonNode node = mapper.readTree(new ByteArrayInputStream(gen.getOutputBuffer().toString().getBytes()));
        assertEquals("foo", node.path("stringValue").asText());
        assertEquals(JsonType.STRING, node.get("stringValue").getType());
    }

    @Test
    public void testSerializationWithEnum() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        enum EnumValue {
            FOO, BAR
        }

        JsonValueSerializer serializer = mapper.getTypeFactory()
                .constructValueType(EnumValue.class, EnumValue.class)
                .getSerializer();

        EnumValue value = EnumValue.FOO;

        JsonGenerator gen = mapper.createGenerator(new ByteArrayOutputStream(), null);
        serializer.serialize(value, gen, null);

        assertNotNull(gen.getOutputBuffer().toString());

        JsonNode node = mapper.readTree(new ByteArrayInputStream(gen.getOutputBuffer().toString().getBytes()));
        assertEquals("FOO", node.path("$value").asText());
        assertEquals(JsonType.STRING, node.get("$value").getType());
    }

    @Test
    public void testSerializationWithEnumAndCustomSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        enum EnumValue {
            FOO, BAR
        }

        class EnumSerializer extends JsonSerializer<EnumValue> {
            @Override
            public void serialize(EnumValue value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString(value.name());
            }
        }

        JsonValueSerializer serializer = mapper.getTypeFactory()
                .constructValueType(EnumValue.class, EnumValue.class)
                .getSerializer();

        EnumValue value = EnumValue.FOO;

        JsonGenerator gen = mapper.createGenerator(new ByteArrayOutputStream(), null);
        serializer = new JsonValueSerializer(
                AnnotatedMethod.construct(EnumValue.class, "name", String.class),
                new EnumSerializer());

        serializer.serializeWithType(value, gen, null, mapper.getTypeSerializer());

        assertNotNull(gen.getOutputBuffer().toString());

        JsonNode node = mapper.readTree(new ByteArrayInputStream(gen.getOutputBuffer().toString().getBytes()));
        assertEquals("FOO", node.path("$value").asText());
        assertEquals(JsonType.STRING, node.get("$value").getType());
    }

    @Test
    public void testSerializationWithMethodReturningPrimitiveTypeAndCustomSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        class TestBean {
            private int intValue;

            public int getIntValue() {
                return intValue;
            }

            public void setIntValue(int i) {
                intValue = i;
            }

            public int getIntValueCustom() {
                return intValue * 2;
            }
        }

        class IntSerializer extends JsonSerializer<Integer> {
            @Override
            public void serialize(Integer value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeNumber(value.intValue());
            }
        }

        TestBean bean = new TestBean();
        bean.intValue = 1;

        JsonSerializer<TestBean> beanSerializer = mapper.findValueSerializer(TestBean.class, null);

        JsonValueSerializer serializer = new JsonValueSerializer(
                AnnotatedMethod.construct(TestBean.class, "getIntValueCustom", Integer.TYPE),
                new IntSerializer());

        JsonGenerator gen = mapper.createGenerator(new ByteArrayOutputStream(), null);
        serializer.serializeWithType(bean, gen, null, mapper.getTypeSerializer());

        assertNotNull(gen.getOutputBuffer().toString());

        JsonNode node = mapper.readTree(new ByteArrayInputStream(gen.getOutputBuffer().toString().getBytes()));
        assertEquals(2, node.path("intValueCustom").asInt());
        assertEquals(JsonType.INT, node.get("intValueCustom").getType());
    }

    @Test
    public void testSerializationWithMethodReturningObjectType() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        class TestBean {
            private String stringValue;

            public String getStringValue() {
                return stringValue;
            }

            public String getStringValueWrapper() {
                return new StringWrapper(stringValue);
            }
        }

        class StringWrapper {
            private final String value;

            public StringWrapper(String value) {
                this.value = value;
            }

            public String getValue() {
                return value;
            }

            @Override
            public String toString() {
                return value;
            }
        }

        JsonSerializer<TestBean> beanSerializer = mapper.findValueSerializer(TestBean.class, null);

        JsonValueSerializer serializer = new JsonValueSerializer(
                AnnotatedMethod.construct(TestBean.class, "getStringValueWrapper", StringWrapper.class),
                new StringSerializer());

        TestBean bean = new TestBean();
        bean.stringValue = "foo";

        JsonGenerator gen = mapper.createGenerator(new ByteArrayOutputStream(), null);
        serializer.serializeWithType(bean, gen, null, mapper.getTypeSerializer());

        assertNotNull(gen.getOutputBuffer().toString());

        JsonNode node = mapper.readTree(new ByteArrayInputStream(gen.getOutputBuffer().toString().getBytes()));
        assertEquals("foo", node.path("$value").asText());
        assertEquals(JsonType.STRING, node.get("$value").getType());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithMethodReturningStringTypeAndCustomSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        class TestBean {
            private String stringValue;

            public String getStringValue() {
                return stringValue;
            }

            public String getStringValueCustom() {
                return stringValue.toUpperCase();
            }
        }

        class StringSerializer extends JsonSerializer<String> {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                throw new RuntimeException();
            }
        }

        TestBean bean = new TestBean();
        bean.stringValue = "foo";

        JsonSerializer<TestBean> beanSerializer = mapper.findValueSerializer(TestBean.class, null);

        JsonValueSerializer serializer = new JsonValueSerializer(
                AnnotatedMethod.construct(TestBean.class, "getStringValueCustom", String.class),
                new StringSerializer());

        JsonGenerator gen = mapper.createGenerator(new ByteArrayOutputStream(), null);
        try {
            serializer.serializeWithType(bean, gen, null, mapper.getTypeSerializer());
        } finally {
            gen.close();
        }
    }

    @Test
    public void testSerializationWithSetters() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        class TestBean {
            private int intValue;

            public int getIntValue() {
                return intValue;
            }

            public void setIntValue(int i) {
                intValue = i;
            }
        }

        JsonSerializer<TestBean> beanSerializer = mapper.findValueSerializer(TestBean.class, null);

        JsonValueSerializer serializer = new JsonValueSerializer(
                AnnotatedMethod.construct(TestBean.class, "setIntValue", Integer.TYPE),
                new IntSerializer());

        TestBean bean = new TestBean();
        int value = 1;

        JsonGenerator gen = mapper.createGenerator(new ByteArrayOutputStream(), null);
        serializer.serializeWithType(value, gen, null, mapper.getTypeSerializer());

        assertNotNull(gen.getOutputBuffer().toString());

        JsonNode node = mapper.readTree(new ByteArrayInputStream(gen.getOutputBuffer().toString().getBytes()));
        assertEquals(1, node.path("intValue").asInt());
        assertEquals(JsonType.INT, node.get("intValue").getType());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        class TestBean {
            private String stringValue;

            public String getStringValue() {
                return stringValue;
            }
        }

        JsonValueSerializer serializer = mapper.getTypeFactory()
                .constructValueType(TestBean.class, TestBean.class)
                .getSerializer();

        TestBean bean = new TestBean();
        bean.stringValue = "foo";

        JsonNode expectedNode = new ObjectMapper().createObjectNode();
        expectedNode.put("type", "string");
        expectedNode.put("pattern", TypeUtils.quoteString("^[^\\]]*$"));

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new ObjectMapper()) {
            @Override
            public void visitStringFormat(JsonStringFormatVisitor visitor) {
                visitor.setFormat(expectedNode);
            }
        };

        serializer.acceptJsonFormatVisitor(visitor, null);

        JsonNode node = visitor.getProvider().getDefaultSerializerProvider().getSchemaFactory()
                .findSchemaFromAnnotation(TestBean.class);

        assertEquals(expectedNode, node);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithEnum() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        enum EnumValue {
            FOO, BAR
        }

        JsonValueSerializer serializer = mapper.getTypeFactory()
                .constructValueType(EnumValue.class, EnumValue.class)
                .getSerializer();

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new ObjectMapper());

        serializer.acceptJsonFormatVisitor(visitor, null);

        JsonNode node = visitor.getProvider().getDefaultSerializerProvider().getSchemaFactory()
                .findSchemaFromAnnotation(EnumValue.class);

        Set<String> enumValues = new LinkedHashSet<>();
        for (EnumValue value : EnumValue.values()) {
            enumValues.add(value.name());
        }

        assertEquals(Arrays.asList(enumValues), node.path("enumValues").asValue().asValueCollection().getValue());
    }

    @Test
    public void testIsNaturalTypeWithStdHandling() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(int[].class, new IntSerializer()));
        assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(int.class, new IntSerializer()));
        assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(boolean.class, new BooleanSerializer()));
        assertTrue(JsonValueSerializer.isNaturalTypeWithStdHandling(String.class, new StringSerializer()));

        assertFalse(JsonValueSerializer.isNaturalTypeWithStdHandling(Object.class, new StringSerializer()));
        assertFalse(JsonValueSerializer.isNaturalTypeWithStdHandling(TestBean.class, new IntSerializer()));
    }
}