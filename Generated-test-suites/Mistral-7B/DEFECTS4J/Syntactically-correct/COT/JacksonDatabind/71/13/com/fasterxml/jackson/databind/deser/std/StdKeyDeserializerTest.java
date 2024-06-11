package com.example.tests;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.EnumConstants;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class StdKeyDeserializerTest {

    private StdKeyDeserializer<String> stringKD;
    private StdKeyDeserializer<Boolean> booleanKD;
    private StdKeyDeserializer<Byte> byteKD;
    private StdKeyDeserializer<Short> shortKD;
    private StdKeyDeserializer<Character> charKD;
    private StdKeyDeserializer<Integer> intKD;
    private StdKeyDeserializer<Long> longKD;
    private StdKeyDeserializer<Float> floatKD;
    private StdKeyDeserializer<Double> doubleKD;
    private StdKeyDeserializer<Locale> localeKD;
    private StdKeyDeserializer<Currency> currencyKD;
    private StdKeyDeserializer<Date> dateKD;
    private StdKeyDeserializer<Calendar> calendarKD;
    private StdKeyDeserializer<UUID> uuidKD;
    private StdKeyDeserializer<URI> uriKD;
    private StdKeyDeserializer<URL> urlKD;
    private StdKeyDeserializer<Class<?>> clsKD;

    @Before
    public void setUp() throws Exception {
        stringKD = StdKeyDeserializer.forType(String.class);
        booleanKD = StdKeyDeserializer.forType(Boolean.class);
        byteKD = StdKeyDeserializer.forType(Byte.class);
        shortKD = StdKeyDeserializer.forType(Short.class);
        charKD = StdKeyDeserializer.forType(Character.class);
        intKD = StdKeyDeserializer.forType(Integer.class);
        longKD = StdKeyDeserializer.forType(Long.class);
        floatKD = StdKeyDeserializer.forType(Float.class);
        doubleKD = StdKeyDeserializer.forType(Double.class);
        localeKD = StdKeyDeserializer.forType(Locale.class);
        currencyKD = StdKeyDeserializer.forType(Currency.class);
        dateKD = StdKeyDeserializer.forType(Date.class);
        calendarKD = StdKeyDeserializer.forType(Calendar.class);
        uuidKD = StdKeyDeserializer.forType(UUID.class);
        uriKD = StdKeyDeserializer.forType(URI.class);
        urlKD = StdKeyDeserializer.forType(URL.class);
        clsKD = StdKeyDeserializer.forType(Class.class);
    }

    @Test
    public void testDeserializeKey_String() throws IOException {
        String key = "test";
        Object result = stringKD.deserializeKey(key, null);
        assertEquals(key, result);
    }

    @Test
    public void testDeserializeKey_Boolean() throws IOException {
        String trueKey = "true";
        Object trueResult = booleanKD.deserializeKey(trueKey, null);
        assertTrue((Boolean) trueResult);

        String falseKey = "false";
        Object falseResult = booleanKD.deserializeKey(falseKey, null);
        assertFalse((Boolean) falseResult);
    }

    @Test
    public void testDeserializeKey_Byte() throws IOException {
        String byteKey = "127";
        Object byteResult = byteKD.deserializeKey(byteKey, null);
        assertEquals((Byte) Byte.parseByte(byteKey), byteResult);

        String outOfRangeByteKey = "128";
        assertNull(byteKD.deserializeKey(outOfRangeByteKey, null));
    }

    @Test
    public void testDeserializeKey_Short() throws IOException {
        String shortKey = "12345";
        Object shortResult = shortKD.deserializeKey(shortKey, null);
        assertEquals((Short) Short.parseShort(shortKey), shortResult);

        String outOfRangeShortKey = "12346";
        assertNull(shortKD.deserializeKey(outOfRangeShortKey, null));
    }

    @Test
    public void testDeserializeKey_Character() throws IOException {
        String charKey = "A";
        Object charResult = charKD.deserializeKey(charKey, null);
        assertEquals((Character) charKey.charAt(0), charResult);

        String multiCharKey = "AB";
        assertNull(charKD.deserializeKey(multiCharKey, null));
    }

    @Test
    public void testDeserializeKey_Integer() throws IOException {
        String intKey = "123456";
        Object intResult = intKD.deserializeKey(intKey, null);
        assertEquals(Integer.parseInt(intKey), intResult);
    }

    @Test
    public void testDeserializeKey_Long() throws IOException {
        String longKey = "1234567890123456";
        Object longResult = longKD.deserializeKey(longKey, null);
        assertEquals(Long.parseLong(longKey), longResult);
    }

    @Test
    public void testDeserializeKey_Float() throws IOException {
        String floatKey = "123.45";
        Object floatResult = floatKD.deserializeKey(floatKey, null);
        assertEquals(Float.parseFloat(floatKey), floatResult, 0.01f);
    }

    @Test
    public void testDeserializeKey_Double() throws IOException {
        String doubleKey = "123.45";
        Object doubleResult = doubleKD.deserializeKey(doubleKey, null);
        assertEquals(Double.parseDouble(doubleKey), doubleResult, 0.001);
    }

    @Test
    public void testDeserializeKey_Locale() throws IOException {
        String localeKey = "en_US";
        Object localeResult = localeKD.deserializeKey(localeKey, null);
        assertEquals(Locale.forLanguageTag(localeKey), localeResult);

        String invalidLocaleKey = "en_XX";
        assertNull(localeKD.deserializeKey(invalidLocaleKey, null));
    }

    @Test
    public void testDeserializeKey_Currency() throws IOException {
        String currencyKey = "USD";
        Object currencyResult = currencyKD.deserializeKey(currencyKey, null);
        assertEquals(Currency.getInstance(currencyKey), currencyResult);

        String invalidCurrencyKey = "XXX";
        assertNull(currencyKD.deserializeKey(invalidCurrencyKey, null));
    }

    @Test
    public void testDeserializeKey_Date() throws IOException {
        String dateKey = "2023-03-24";
        Object dateResult = dateKD.deserializeKey(dateKey, null);
        assertEquals(new Date(2023, 3, 24), dateResult);
    }

    @Test
    public void testDeserializeKey_Calendar() throws IOException {
        String calendarKey = "2023-03-24";
        Object calendarResult = calendarKD.deserializeKey(calendarKey, null);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(2023, 3, 24));
        assertEquals(calendar, calendarResult);
    }

    @Test
    public void testDeserializeKey_UUID() throws IOException {
        String uuidKey = "123e4567-e89b-12d3-a456-426614174000";
        Object uuidResult = uuidKD.deserializeKey(uuidKey, null);
        assertEquals(UUID.fromString(uuidKey), uuidResult);
    }

    @Test
    public void testDeserializeKey_URI() throws IOException {
        String uriKey = "http://test.com";
        Object uriResult = uriKD.deserializeKey(uriKey, null);
        assertEquals(new URI(uriKey), uriResult);
    }

    @Test
    public void testDeserializeKey_URL() throws IOException {
        String urlKey = "http://test.com";
        Object urlResult = urlKD.deserializeKey(urlKey, null);
        assertEquals(new URL(urlKey), urlResult);
    }

    @Test
    public void testDeserializeKey_Class() throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String className = "java.util.Date";
        Class<?> cls = Class.forName(className);
        Method constructor = cls.getDeclaredConstructor();
        Object clsResult = clsKD.deserializeKey(className, null);
        assertEquals(cls, clsResult.getClass());
        assertEquals(constructor.newInstance(), clsResult);
    }

    @Test(expected = IOException.class)
    public void testDeserializeKey_InvalidType() throws IOException {
        String invalidTypeKey = "java.lang.String";
        stringKD.deserializeKey(invalidTypeKey, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_NullKey() throws IOException {
        stringKD.deserializeKey(null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeKey_EnumUnknownValue() throws IOException {
        EnumSet<MyEnum> expectedValues = EnumSet.allOf(MyEnum.class);
        Properties configProperties = new Properties();
        configProperties.setProperty(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL.getName(), "true");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setConfig(new DeserializationConfig(configProperties));
        ObjectWriter writer = mapper.writer();
        String unknownValue = "X_VALUE";
        String json = writer.writeValueAsString(expectedValues);
        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
        StdKeyDeserializer<MyEnum> enumKD = StdKeyDeserializer.forType(MyEnum.class);
        MyEnum deserialized = (MyEnum) enumKD.deserializeKey(unknownValue, null);
        assertNull(deserialized);
    }
}

enum MyEnum {
    VALUE1, VALUE2, VALUE3
}