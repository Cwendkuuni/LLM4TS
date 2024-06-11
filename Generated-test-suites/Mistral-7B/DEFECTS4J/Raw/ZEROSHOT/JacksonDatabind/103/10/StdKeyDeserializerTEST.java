package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

/**
 * Test class for StdKeyDeserializer.
 */
@JacksonStdImpl
public class StdKeyDeserializerTest {

    /**
     * Test constructor for StdKeyDeserializer with boolean type.
     * @throws Exception if test fails.
     */
    @Test
    public void testBoolean() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        Object key = serializer.deserializeKey("true", new TestContext());
        assertTrue((Boolean) key);
        key = serializer.deserializeKey("false", new TestContext());
        assertFalse((Boolean) key);
    }

    /**
     * Test constructor for StdKeyDeserializer with byte type.
     * @throws Exception if test fails.
     */
    @Test
    public void testByte() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        Object key = serializer.deserializeKey("127", new TestContext());
        assertEquals((Byte) (byte) 127, key);
    }

    /**
     * Test constructor for StdKeyDeserializer with short type.
     * @throws Exception if test fails.
     */
    @Test
    public void testShort() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        Object key = serializer.deserializeKey("32767", new TestContext());
        assertEquals((Short) Short.parseShort("32767"), key);
    }

    /**
     * Test constructor for StdKeyDeserializer with char type.
     * @throws Exception if test fails.
     */
    @Test
    public void testChar() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        Object key = serializer.deserializeKey("'a'", new TestContext());
        assertEquals((Character) 'a', key);
    }

    /**
     * Test constructor for StdKeyDeserializer with int type.
     * @throws Exception if test fails.
     */
    @Test
    public void testInt() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        Object key = serializer.deserializeKey("42", new TestContext());
        assertEquals((Integer) 42, key);
    }

    /**
     * Test constructor for StdKeyDeserializer with long type.
     * @throws Exception if test fails.
     */
    @Test
    public void testLong() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        Object key = serializer.deserializeKey("9223372036854775807", new TestContext());
        assertEquals((Long) Long.parseLong("9223372036854775807"), key);
    }

    /**
     * Test constructor for StdKeyDeserializer with float type.
     * @throws Exception if test fails.
     */
    @Test
    public void testFloat() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        Object key = serializer.deserializeKey("3.14159265358979323846", new TestContext());
        assertEquals((Float) 3.14159265f, key);
    }

    /**
     * Test constructor for StdKeyDeserializer with double type.
     * @throws Exception if test fails.
     */
    @Test
    public void testDouble() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        Object key = serializer.deserializeKey("6.28318530718", new TestContext());
        assertEquals((Double) 6.28318530718, key);
    }

    /**
     * Test constructor for StdKeyDeserializer with Locale type.
     * @throws Exception if test fails.
     */
    @Test
    public void testLocale() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        Object key = serializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, key);
    }

    /**
     * Test constructor for StdKeyDeserializer with Date type.
     * @throws Exception if test fails.
     */
    @Test
    public void testDate() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        Object key = serializer.deserializeKey("2023-03-27", new TestContext());
        assertNotNull(key);
        Date date = (Date) key;
        assertEquals(2023, date.getYear());
        assertEquals(3, date.getMonth());
        assertEquals(27, date.getDate());
    }

    /**
     * Test constructor for StdKeyDeserializer with Calendar type.
     * @throws Exception if test fails.
     */
    @Test
    public void testCalendar() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
        Object key = serializer.deserializeKey("2023-03-27", new TestContext());
        assertNotNull(key);
        Calendar calendar = (Calendar) key;
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), calendar.get(Calendar.YEAR));
        assertEquals(Calendar.getInstance().get(Calendar.MONTH), calendar.get(Calendar.MONTH));
        assertEquals(Calendar.getInstance().get(Calendar.DATE), calendar.get(Calendar.DATE));
    }

    /**
     * Test constructor for StdKeyDeserializer with UUID type.
     * @throws Exception if test fails.
     */
    @Test
    public void testUUID() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);
        Object key = serializer.deserializeKey("7bf810be-7eb5-46d4-9666-3b6203e53f8e", new TestContext());
        assertEquals(new UUID("7bf810be-7eb5-46d4-9666-3b6203e53f8e"), key);
    }

    /**
     * Test constructor for StdKeyDeserializer with URI type.
     * @throws Exception if test fails.
     */
    @Test
    public void testURI() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        Object key = serializer.deserializeKey("https://www.example.com", new TestContext());
        URI uri = (URI) key;
        assertEquals("https", uri.getScheme());
        assertEquals("/www.example.com", uri.getPath());
    }

    /**
     * Test constructor for StdKeyDeserializer with URL type.
     * @throws Exception if test fails.
     */
    @Test
    public void testURL() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        Object key = serializer.deserializeKey("https://www.example.com", new TestContext());
        URL url = (URL) key;
        assertEquals("https", url.getProtocol());
        assertEquals("/www.example.com", url.getPath());
    }

    /**
     * Test constructor for StdKeyDeserializer with Class type.
     * @throws Exception if test fails.
     */
    @Test
    public void testClass() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String[].class);
        Object key = serializer.deserializeKey("[Ljava.lang.String;", new TestContext());
        assertEquals(String[].class, key.getClass());
    }

    /**
     * Test constructor for StdKeyDeserializer with UTF-8 encoded byte array type.
     * @throws Exception if test fails.
     */
    @Test
    public void testByteArray() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(byte[].class);
        Object key = serializer.deserializeKey("VG8gZXJyIGlzIGh1bWFuLCBzdW1tYSBmb3Igc2VucmVzIGZvciBlYWdlIG9mIGZyb20gZXhpc3RzIGZvciBzb21lIGFuZCBjb250ZW50IGFzIG9mIHVwIHlvdSBuZWVkIG5vdCBkYXRhYmxlIHJlYWxsZXMgc2VwYXJhdGVkIGluZGljYXRpb25zIHNlZW1zIHJlcGx5LiBNdW1tb2RlIG9mIHNlbnNpZ24gc2VwYXJhdGVkIGluY2x1ZGUgc2VwYXJhdGVkIGluIGRhdGFbMQ==", new TestContext());
        byte[] expectedByteArray = Base64.getDecoder().decode("VG8gZXJyIGlzIGh1bWFuLCBzdW1tYSBmb3Igc2VucmVzIGZvciBlYWdlIG9mIGZyb20gZXhpc3RzIGZvciBzb21lIGFuZCBjb250ZW50IGFzIG9mIHVwIHlvdSBuZWVkIG5vdCBkYXRhYmxlIHJlYWxsZXMgc2VwYXJhdGVkIGluZGljYXRpb25zIHNlZW1zIHJlcGx5LiBNdW1tb2RlIG9mIHNlbnNpZ24gc2VwYXJhdGVkIGluIGRhdGFbMQ==");
        assertArrayEquals(expectedByteArray, key);
    }

    /**
     * Test deserialization key for Enum type.
     * @throws Exception if test fails.
     */
    @Test
    public void testEnum() throws Exception {
        EnumResolver enumResolver = new EnumResolver(SomeEnum.class);
        StdKeyDeserializer serializer = new EnumKD(enumResolver, new AnnotatedMethod(SomeEnum.class.getDeclaredMethod("valueOf", String.class)));
        Object key = serializer.deserializeKey("SOME_VALUE", new TestContext());
        assertEquals(SomeEnum.SOME_VALUE, key);
    }

    /**
     * Test deserialization key for String constructor type.
     * @throws Exception if test fails.
     */
    @Test
    public void testStringConstructor() throws Exception {
        Class<?> cls = String.class;
        Constructor<?> constructor = cls.getDeclaredConstructor(String.class);
        StdKeyDeserializer serializer = new StringCtorKeyDeserializer(constructor);
        Object key = serializer.deserializeKey("hello", new TestContext());
        assertEquals("hello", key);
    }

    /**
     * Test deserialization key for static no-args factory method type.
     * @throws Exception if test fails.
     */
    @Test
    public void testStaticFactoryMethod() throws Exception {
        Class<?> cls = SomeFactoryClass.class;
        Method method = cls.getDeclaredMethod("createInstance", (Class[]) null);
        StdKeyDeserializer serializer = new StringFactoryKeyDeserializer(method);
        Object key = serializer.deserializeKey("SomeFactoryClass", new TestContext());
        assertEquals(SomeFactoryClass.class, key);
    }

    /**
     * Test deserialization key for delegating deserializer type.
     * @throws Exception if test fails.
     */
    @Test
    public void testDelegatingDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = mapper.getFactory().findValueDeserializer(String.class, null);
        StdKeyDeserializer serializer = new DelegatingKD(String.class, stringDeserializer);
        Object key = serializer.deserializeKey("Test String", new TestContext());
        assertEquals("Test String", key);
    }
}

class TestContext extends DeserializationContext {
    @Override
    public JsonParser _parser() {
        return null;
    }

    @Override
    public AnnotationIntrospector _annotationIntrospector() {
        return null;
    }

    @Override
    public ObjectMapper _objectMapper() {
        return null;
    }

    @Override
    public DeserializationFeature[] _enabledFeatures() {
        return new DeserializationFeature[0];
    }

    @Override
    public DeserializationFeature[] _disabledFeatures() {
        return new DeserializationFeature[0];
    }

    @Override
    public Object _readValue(Class<?> type, JsonParser p, DeserializationContext ctxt) throws IOException {
        return null;
    }

    @Override
    public Object _findRootValue(JsonParser p, DeserializationContext ctxt) throws IOException {
        return null;
    }

    @Override
    public Object _handleUnknownProperty(Object bean, Method m, Object id, String name, JsonParser p) throws IOException {
        return null;
    }

    @Override
    public Object _handleMissingValue(Object bean, Method m, Object id, JsonParser p) throws IOException {
        return null;
    }

    @Override
    public Object _handleMissingValue(Object bean, JsonParser p) throws IOException {
        return null;
    }

    @Override
    public void _reportError(MessageBase msg, JsonParser p) throws IOException {
        throw new RuntimeException(msg.toString());
    }

    @Override
    public void _reportError(MessageBase msg, JsonParser p, Object bean, Method m) throws IOException {
        throw new RuntimeException(msg.toString());
    }

    @Override
    public void _reportError(MessageBase msg, JsonParser p, Object bean, Method m, Object id) throws IOException {
        throw new RuntimeException(msg.toString());
    }

    @Override
    public void _reportError(MessageBase msg, JsonParser p, Object bean, Object id) throws IOException {
        throw new RuntimeException(msg.toString());
    }

    @Override
    public void _reportError(MessageBase msg, Object context) throws IOException {
        throw new RuntimeException(msg.toString());
    }

    @Override
    public void _close() throws IOException {
        // Do nothing
    }
}