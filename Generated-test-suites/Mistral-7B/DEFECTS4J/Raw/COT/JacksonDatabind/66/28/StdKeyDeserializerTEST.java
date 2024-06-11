package com.example.test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;
    private DeserializationContext deserializationContext;
    private TypeDeserializer typeDeserializer;
    private JavaType keyType;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper(new JsonFactory());
        deserializationContext = objectMapper.getDeserializationContext();
        typeDeserializer = deserializationContext.getTypeDeserializer();
        keyType = TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, Object.class);
    }

    @Test
    public void testBooleanType() throws Exception {
        StdKeyDeserializer booleanSerializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = booleanSerializer.deserializeKey("true", deserializationContext);
        assertEquals(Boolean.TRUE, key);

        Object keyFromMap = typeDeserializer.deserializeTypedFromObject(deserializationContext, keyType, "true");
        assertEquals(Boolean.TRUE, keyFromMap);
    }

    @Test
    public void testByteType() throws Exception {
        StdKeyDeserializer byteSerializer = StdKeyDeserializer.forType(Byte.class);
        Object key = byteSerializer.deserializeKey("10", deserializationContext);
        assertEquals((Byte) 10, key);

        Object keyFromMap = typeDeserializer.deserializeTypedFromObject(deserializationContext, keyType, "10");
        assertEquals((Byte) 10, keyFromMap);

        // test negative number
        Object keyNegative = byteSerializer.deserializeKey("-10", deserializationContext);
        assertEquals((Byte) -10, keyNegative);

        Object keyNegativeFromMap = typeDeserializer.deserializeTypedFromObject(deserializationContext, keyType, "-10");
        assertEquals((Byte) -10, keyNegativeFromMap);
    }

    @Test
    public void testShortType() throws Exception {
        StdKeyDeserializer shortSerializer = StdKeyDeserializer.forType(Short.class);
        Object key = shortSerializer.deserializeKey("100", deserializationContext);
        assertEquals((Short) 100, key);

        Object keyFromMap = typeDeserializer.deserializeTypedFromObject(deserializationContext, keyType, "100");
        assertEquals((Short) 100, keyFromMap);

        // test negative number
        Object keyNegative = shortSerializer.deserializeKey("-100", deserializationContext);
        assertEquals((Short) -100, keyNegative);

        Object keyNegativeFromMap = typeDeserializer.deserializeTypedFromObject(deserializationContext, keyType, "-100");
        assertEquals((Short) -100, keyNegativeFromMap);
    }

    @Test
    public void testCharType() throws Exception {
        StdKeyDeserializer charSerializer = StdKeyDeserializer.forType(Character.class);
        Object key = charSerializer.deserializeKey("A", deserializationContext);
        assertEquals(Character.valueOf('A'), key);

        Object keyFromMap = typeDeserializer.deserializeTypedFromObject(deserializationContext, keyType, "A");
        assertEquals(Character.valueOf('A'), keyFromMap);
    }

    @Test
    public void testIntType() throws Exception {
        StdKeyDeserializer intSerializer = StdKeyDeserializer.forType(Integer.class);
        Object key = intSerializer.deserializeKey("1000", deserializationContext);
        assertEquals((Integer) 1000, key);

        Object keyFromMap = typeDeserializer.deserializeTypedFromObject(deserializationContext, keyType, "1000");
        assertEquals((Integer) 1000, keyFromMap);
    }

    @Test
    public void testLongType() throws Exception {
        StdKeyDeserializer longSerializer = StdKeyDeserializer.forType(Long.class);
        Object key = longSerializer.deserializeKey("1000000000000", deserializationContext);
        assertEquals((Long) 1000000000000L, key);

        Object keyFromMap = typeDeserializer.deserializeTypedFromObject(deserializationContext, keyType, "1000000000000");
        assertEquals((Long) 1000000000000L, keyFromMap);
    }

    @Test
    public void testFloatType() throws Exception {
        StdKeyDeserializer floatSerializer = StdKeyDeserializer.forType(Float.class);
        Object key = floatSerializer.deserializeKey("10.5", deserializationContext);
        assertEquals(Float.valueOf(10.5f), key);

        Object keyFromMap = typeDeserializer.deserializeTypedFromObject(deserializationContext, keyType, "10.5");
        assertEquals(Float.valueOf(10.5f), keyFromMap);
    }

    @Test
    public void testDoubleType() throws Exception {
        StdKeyDeserializer doubleSerializer = StdKeyDeserializer.forType(Double.class);
        Object key = doubleSerializer.deserializeKey("10.56", deserializationContext);
        assertEquals(Double.valueOf(10.56), key);

        Object keyFromMap = typeDeserializer.deserializeTypedFromObject(deserializationContext, keyType, "10.56");
        assertEquals(Double.valueOf(10.56), keyFromMap);
    }

    @Test
    public void testLocaleType() throws Exception {
        StdKeyDeserializer localeSerializer = StdKeyDeserializer.forType(Locale.class);
        Object key = localeSerializer.deserializeKey("en_US", deserializationContext);
        assertEquals(Locale.US, key);

        Object keyFromMap = typeDeserializer.deserializeTypedFromObject(deserializationContext, keyType, "en_US");
        assertEquals(Locale.US, keyFromMap);
    }

    @Test
    public void testDateType() throws Exception {
        StdKeyDeserializer dateSerializer = StdKeyDeserializer.forType(Date.class);
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        node.put("date", "2022-02-01");
        Object key = dateSerializer.deserializeKey(node.toString(), deserializationContext);
        assertEquals(new Date(2022, 1, 1), key);

        Object keyFromMap = typeDeserializer.deserializeTypedFromObject(deserializationContext, keyType, node.toString());
        assertEquals(new Date(2022, 1, 1), keyFromMap);
    }

    @Test
    public void testCalendarType() throws Exception {
        StdKeyDeserializer calendarSerializer = StdKeyDeserializer.forType(Calendar.class);
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        node.put("calendar", "2022-02-01");
        Object key = calendarSerializer.deserializeKey(node.toString(), deserializationContext);
        assertEquals(Calendar.getInstance().getTime(), key);

        Object keyFromMap = typeDeserializer.deserializeTypedFromObject(deserializationContext, keyType, node.toString());
        assertEquals(Calendar.getInstance().getTime(), keyFromMap);
    }

    @Test
    public void testURIType() throws Exception {
        StdKeyDeserializer uriSerializer = StdKeyDeserializer.forType(URI.class);
        Object key = uriSerializer.deserializeKey("https://www.example.com", deserializationContext);
        assertEquals(new URI("https://www.example.com"), key);

        Object keyFromMap = typeDeserializer.deserializeTypedFromObject(deserializationContext, keyType, "https://www.example.com");
        assertEquals(new URI("https://www.example.com"), keyFromMap);
    }

    @Test
    public void testURLType() throws Exception {
        StdKeyDeserializer urlSerializer = StdKeyDeserializer.forType(URL.class);
        Object key = urlSerializer.deserializeKey("https://www.example.com", deserializationContext);
        assertEquals(new URL("https://www.example.com"), key);

        Object keyFromMap = typeDeserializer.deserializeTypedFromObject(deserializationContext, keyType, "https://www.example.com");
        assertEquals(new URL("https://www.example.com"), keyFromMap);
    }

    @Test
    public void testClassType() throws Exception {
        StdKeyDeserializer classSerializer = StdKeyDeserializer.forType(String.class);
        Object key = classSerializer.deserializeKey("java.lang.String", deserializationContext);
        assertEquals(String.class, key);

        Object keyFromMap = typeDeserializer.deserializeTypedFromObject(deserializationContext, keyType, "java.lang.String");
        assertEquals(String.class, keyFromMap);
    }

    @Test
    public void testEnumType() throws Exception {
        StdKeyDeserializer enumSerializer = StdKeyDeserializer.forType(Color.class);
        Object key = enumSerializer.deserializeKey("RED", deserializationContext);
        assertEquals(Color.RED, key);

        Object keyFromMap = typeDeserializer.deserializeTypedFromObject(deserializationContext, keyType, "RED");
        assertEquals(Color.RED, keyFromMap);
    }

    @Test
    public void testNullKey() throws Exception {
        StdKeyDeserializer stringSerializer = StdKeyDeserializer.forType(String.class);
        Object key = stringSerializer.deserializeKey(null, deserializationContext);
        assertNull(key);

        Object keyFromMap = typeDeserializer.deserializeTypedFromObject(deserializationContext, keyType, null);
        assertNull(keyFromMap);
    }

    @Test
    public void testUnknownType() throws Exception {
        StdKeyDeserializer unknownSerializer = StdKeyDeserializer.forType(UnknownType.class);
        Object key = unknownSerializer.deserializeKey("invalidKey", deserializationContext);
        assertNull(key);

        Object keyFromMap = typeDeserializer.deserializeTypedFromObject(deserializationContext, keyType, "invalidKey");
        assertNull(keyFromMap);
    }

    @Test
    public void testInvalidKeyFormat() throws Exception {
        StdKeyDeserializer stringSerializer = StdKeyDeserializer.forType(String.class);
        Object key = stringSerializer.deserializeKey("invalidFormat", deserializationContext);
        assertNull(key);

        Object keyFromMap = typeDeserializer.deserializeTypedFromObject(deserializationContext, keyType, "invalidFormat");
        assertNull(keyFromMap);
    }
}

class UnknownType {
}