package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class StdKeyDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testBoolean() throws JsonMappingException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(boolean.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("true", ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals(true, result);

        result = keyDeserializer.deserializeKey("false", ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals(false, result);

        result = keyDeserializer.deserializeKey("invalid", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testByte() throws JsonMappingException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(byte.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("1", ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals((byte) 1, result);

        result = keyDeserializer.deserializeKey("-127", ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals((byte) -127, result);

        result = keyDeserializer.deserializeKey("256", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testShort() throws JsonMappingException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(short.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("1", ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals((short) 1, result);

        result = keyDeserializer.deserializeKey("-32768", ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals((short) -32768, result);

        result = keyDeserializer.deserializeKey("32768", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testChar() throws JsonMappingException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(char.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("a", ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals('a', result);

        result = keyDeserializer.deserializeKey("InvalidCharacter", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testInt() throws JsonMappingException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(int.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("1", ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result);

        result = keyDeserializer.deserializeKey("-2147483648", ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals(-2147483648, result);

        result = keyDeserializer.deserializeKey("4294967296", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testLong() throws JsonMappingException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(long.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("1", ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals(1L, result);

        result = keyDeserializer.deserializeKey("-9223372036854775808", ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals(-9223372036854775808L, result);

        result = keyDeserializer.deserializeKey("9223372036854775807", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testFloat() throws JsonMappingException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(float.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("1.0", ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals(1.0f, result, 0.001f);

        result = keyDeserializer.deserializeKey("-1.5", ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals(-1.5f, result, 0.001f);

        result = keyDeserializer.deserializeKey("NaN", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testDouble() throws JsonMappingException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(double.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("1.0", ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals(1.0, result, 0.001);

        result = keyDeserializer.deserializeKey("-1.5", ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals(-1.5, result, 0.001);

        result = keyDeserializer.deserializeKey("NaN", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testLocale() throws JsonMappingException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("en_US", ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals(Locale.US, result);

        result = keyDeserializer.deserializeKey("es_ES", ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals(Locale.SPAIN, result);

        result = keyDeserializer.deserializeKey("InvalidLocale", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testDate() throws JsonMappingException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Date.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("2022-03-15", ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals(new Date(1647238400000L), result);

        result = keyDeserializer.deserializeKey("InvalidDateFormat", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testCalendar() throws JsonMappingException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Calendar.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("2022-03-15", ctxt);
        Assert.assertNotNull(result);
        GregorianCalendar expected = new GregorianCalendar(2022, Calendar.MARCH, 15);
        Assert.assertEquals(expected.getTime(), result.getTime());

        result = keyDeserializer.deserializeKey("InvalidDateFormat", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testUUID() throws JsonMappingException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(UUID.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("00000000-0000-0000-0000-000000000000", ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), result);

        result = keyDeserializer.deserializeKey("InvalidUUID", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testURI() throws JsonMappingException, JsonProcessingException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("http://www.example.com", ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals(new URI("http://www.example.com"), result);

        result = keyDeserializer.deserializeKey("InvalidURIFormat", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testURL() throws JsonMappingException, JsonProcessingException, IOException {
        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("http://www.example.com", ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals(new URL("http://www.example.com"), result);

        result = keyDeserializer.deserializeKey("InvalidURLFormat", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testEnum() throws JsonMappingException, JsonProcessingException {
        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
        @JsonDeserialize(using = EnumDeserializer.class)
        enum MyEnum {
            VALUE_A,
            VALUE_B;
        }

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(MyEnum.class);

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object result = keyDeserializer.deserializeKey("VALUE_A", ctxt);
        Assert.assertNotNull(result);
        Assert.assertEquals(MyEnum.VALUE_A, result);

        result = keyDeserializer.deserializeKey("VALUE_C", ctxt);
        Assert.assertNull(result);

        result = keyDeserializer.deserializeKey("INVALID", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testUserDefinedClass() throws JsonMappingException, JsonProcessingException {
        enum MyEnum {
            VALUE_A,
            VALUE_B;
        }

        class MyClass {
            @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
            @JsonDeserialize(using = StdKeyDeserializer.class)
            private final MyEnum _enumValue;

            public MyClass(MyEnum enumValue) {
                _enumValue = enumValue;
            }

            public MyEnum getEnumValue() {
                return _enumValue;
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("enumValue", "VALUE_A");

        MyClass expected = new MyClass(MyEnum.VALUE_A);
        MyClass result = objectMapper.readValue(rootNode.toString(), MyClass.class);
        Assert.assertEquals(expected.getEnumValue(), result.getEnumValue());
    }
}