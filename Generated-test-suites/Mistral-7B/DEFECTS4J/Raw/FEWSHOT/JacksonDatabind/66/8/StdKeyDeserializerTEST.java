package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanTypeKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer booleanKD = StdKeyDeserializer.forType(Boolean.class);

        Object result = booleanKD.deserializeKey("true", new TestContext());
        assertEquals(Boolean.TRUE, result);

        result = booleanKD.deserializeKey("false", new TestContext());
        assertEquals(Boolean.FALSE, result);

        result = booleanKD.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testByteTypeKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer byteKD = StdKeyDeserializer.forType(Byte.class);

        Object result = byteKD.deserializeKey("1", new TestContext());
        assertEquals((byte) 1, result);

        result = byteKD.deserializeKey("127", new TestContext());
        assertEquals((byte) 127, result);

        result = byteKD.deserializeKey("128", new TestContext());
        assertNull(result);
    }

    @Test
    public void testShortTypeKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer shortKD = StdKeyDeserializer.forType(Short.class);

        Object result = shortKD.deserializeKey("1", new TestContext());
        assertEquals((short) 1, result);

        result = shortKD.deserializeKey("32767", new TestContext());
        assertEquals((short) 32767, result);

        result = shortKD.deserializeKey("32768", new TestContext());
        assertNull(result);
    }

    @Test
    public void testCharTypeKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer charKD = StdKeyDeserializer.forType(Character.class);

        Object result = charKD.deserializeKey("A", new TestContext());
        assertEquals('A', result);

        result = charKD.deserializeKey(" invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testIntTypeKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer intKD = StdKeyDeserializer.forType(Integer.class);

        Object result = intKD.deserializeKey("1", new TestContext());
        assertEquals(1, result);

        result = intKD.deserializeKey("2147483647", new TestContext());
        assertEquals(Integer.MAX_VALUE, result);

        result = intKD.deserializeKey("-1", new TestContext());
        assertEquals(Integer.MIN_VALUE, result);

        result = intKD.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testLongTypeKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer longKD = StdKeyDeserializer.forType(Long.class);

        Object result = longKD.deserializeKey("1", new TestContext());
        assertEquals(1L, result);

        result = longKD.deserializeKey("9223372036854775807", new TestContext());
        assertEquals(Long.MAX_VALUE, result);

        result = longKD.deserializeKey("-1", new TestContext());
        assertEquals(Long.MIN_VALUE, result);

        result = longKD.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testFloatTypeKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer floatKD = StdKeyDeserializer.forType(Float.class);

        Object result = floatKD.deserializeKey("1.0", new TestContext());
        assertEquals(Float.valueOf(1.0f), result);

        result = floatKD.deserializeKey("3.141592653589793", new TestContext());
        assertEquals(Float.valueOf(3.141592653589793f), result);

        result = floatKD.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testDoubleTypeKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer doubleKD = StdKeyDeserializer.forType(Double.class);

        Object result = doubleKD.deserializeKey("1.0", new TestContext());
        assertEquals(Double.valueOf(1.0), result);

        result = doubleKD.deserializeKey("6.283185307185506e+01", new TestContext());
        assertEquals(Double.valueOf(6.283185307185506), result);

        result = doubleKD.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testLocaleTypeKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer localeKD = StdKeyDeserializer.forType(Locale.class);

        Object result = localeKD.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, result);

        result = localeKD.deserializeKey("fr_FR", new TestContext());
        assertEquals(Locale.FRANCE, result);

        result = localeKD.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testDateTypeKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer dateKD = StdKeyDeserializer.forType(Date.class);

        Object result = dateKD.deserializeKey("2022-01-01", new TestContext());
        assertNotNull(result);
        assertEquals("Sat Jan 01 00:00:00 GMT 2022", result.toString());

        result = dateKD.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testCalendarTypeKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer calendarKD = StdKeyDeserializer.forType(Calendar.class);

        Object result = calendarKD.deserializeKey("2022-01-01", new TestContext());
        assertNotNull(result);

        Calendar calendar = (Calendar) result;
        assertEquals(2022, calendar.get(Calendar.YEAR));
        assertEquals(1, calendar.get(Calendar.MONTH));
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));

        result = calendarKD.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testUriTypeKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer uriKD = StdKeyDeserializer.forType(URI.class);

        Object result = uriKD.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URI("http://example.com"), result);

        result = uriKD.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testUrlTypeKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer urlKD = StdKeyDeserializer.forType(URL.class);

        Object result = urlKD.deserializeKey("http://example.com", new TestContext());
        assertEquals(new URL("http://example.com"), result);

        result = urlKD.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testClassTypeKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer classKD = StdKeyDeserializer.forType(String.class);

        Object result = classKD.deserializeKey("java.lang.String", new TestContext());
        assertEquals(String.class, result);

        result = classKD.deserializeKey("java.util.List", new TestContext());
        assertEquals(List.class, result);

        result = classKD.deserializeKey("java.util.ArrayList", new TestContext());
        assertEquals(ArrayList.class, result);

        result = classKD.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testEnumTypeKeyDeserializer() throws IOException, JsonProcessingException {
        Enum e = Enum.values()[0];
        StdKeyDeserializer enumKD = StdKeyDeserializer.forType(e.getClass());

        Object result = enumKD.deserializeKey(e.name(), new TestContext());
        assertEquals(e, result);

        result = enumKD.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testDelegateKeyDeserializer() throws IOException, JsonProcessingException {
        JsonDeserializer<String> stringDeserializer = new StandardDeserializer<String>(String.class);
        StdKeyDeserializer delegateKD = new StdKeyDeserializer.DelegingKD(String.class, stringDeserializer);

        Object result = delegateKD.deserializeKey("value", new TestContext());
        assertEquals("value", result);
    }

    @Test
    public void testStringKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer stringKD = StdKeyDeserializer.forType(String.class);

        Object result = stringKD.deserializeKey("value", new TestContext());
        assertEquals("value", result);
    }

    @Test
    public void testObjectKeyDeserializer() throws IOException, JsonProcessingException {
        StdKeyDeserializer objectKD = StdKeyDeserializer.forType(Object.class);

        Object result = objectKD.deserializeKey("{\"name\":\"value\"}", new TestContext());
        assertNotNull(result);
    }
}

class TestContext extends DeserializationContext {
    @Override
    public ObjectReader _getRootValueFactory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ObjectReader _unwrappingValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DeserializationConfig getConfig() {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonFactory _getBaseJsonFactory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BeanDescription _getAnnotationIntrospector() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object _findValueHandler(DeserializationContext ctxt, java.lang.reflect.Type valueType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object _handleUnknownProperty(String name, Object bean, Object currentNestedValue) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object _handleUnknownValue(Object value, DeserializationContext ctxt) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object _valueInstantiator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void _reportError(Throwable e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void _reportError(String msg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void _reportError(String msg, Object details) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void _reportError(String msg, Object[] details) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void _reportError(String msg, Object[] details, Throwable e) {
        throw new UnsupportedOperationException();
    }
}