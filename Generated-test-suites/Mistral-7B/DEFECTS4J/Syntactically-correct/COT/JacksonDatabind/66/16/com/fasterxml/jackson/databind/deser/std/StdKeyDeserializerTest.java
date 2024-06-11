package com.example; // replace with your test package if needed

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.FinderCache;
import com.fasterxml.jackson.databind.util.TypeFactory;

public class StdKeyDeserializerTest {

    // Test utility method to create ObjectMapper instance for testing
    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setDefaultTyping(DefaultTyping.NON_FINAL);
        mapper.activateDefaultTyping(FinderCache.getDefaultInstance(), TypeDeserializer.emptyArray());
        mapper.activateDefaultTyping(FinderCache.getDefaultInstance(), EnumSet.of(None.class));
        return mapper;
    }

    @Test
    public void testDeserializeKey_Boolean() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        DeserializationContext context = createContext();
        String key = "true";
        Object result = serializer.deserializeKey(key, context);
        assertEquals(Boolean.TRUE, result);

        key = "false";
        result = serializer.deserializeKey(key, context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testDeserializeKey_Byte() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext context = createContext();
        String key = "127";
        Object result = serializer.deserializeKey(key, context);
        assertEquals((Byte) 127, result);
    }

    @Test
    public void testDeserializeKey_Short() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        DeserializationContext context = createContext();
        String key = "32767";
        Object result = serializer.deserializeKey(key, context);
        assertEquals((Short) 32767, result);
    }

    @Test
    public void testDeserializeKey_Char() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        DeserializationContext context = createContext();
        String key = "A";
        Object result = serializer.deserializeKey(key, context);
        assertEquals((Character) 'A', result);
    }

    @Test
    public void testDeserializeKey_Int() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext context = createContext();
        String key = "42";
        Object result = serializer.deserializeKey(key, context);
        assertEquals(42, result.hashCode());
        assertEquals(Integer.valueOf(42), result);
    }

    @Test
    public void testDeserializeKey_Long() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext context = createContext();
        String key = "9223372036854775807";
        Object result = serializer.deserializeKey(key, context);
        assertEquals(Long.parseLong(key), result.hashCode());
        assertEquals(Long.valueOf(Long.parseLong(key)), result);
    }

    @Test
    public void testDeserializeKey_Float() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        DeserializationContext context = createContext();
        String key = "3.14";
        Object result = serializer.deserializeKey(key, context);
        assertEquals(Float.valueOf((float) 3.14), result);
    }

    @Test
    public void testDeserializeKey_Double() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        DeserializationContext context = createContext();
        String key = "3.14159265358979323846";
        Object result = serializer.deserializeKey(key, context);
        assertEquals(Double.valueOf(3.14159265358979323846), result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKey_InvalidBoolean() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        DeserializationContext context = createContext();
        String key = "invalid";
        Object result = serializer.deserializeKey(key, context);
        assertNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKey_InvalidByte() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        DeserializationContext context = createContext();
        String key = "256";
        Object result = serializer.deserializeKey(key, context);
        assertNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKey_InvalidShort() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        DeserializationContext context = createContext();
        String key = "32768";
        Object result = serializer.deserializeKey(key, context);
        assertNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKey_InvalidChar() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        DeserializationContext context = createContext();
        String key = "AB"; // 2 characters
        Object result = serializer.deserializeKey(key, context);
        assertNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKey_InvalidInt() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext context = createContext();
        String key = "-1";
        Object result = serializer.deserializeKey(key, context);
        assertNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKey_InvalidLong() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext context = createContext();
        String key = "-9223372036854775808";
        Object result = serializer.deserializeKey(key, context);
        assertNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKey_InvalidLocale() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        DeserializationContext context = createContext();
        String key = "invalid";
        Object result = serializer.deserializeKey(key, context);
        assertNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKey_InvalidUri() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        DeserializationContext context = createContext();
        String key = "invalid";
        Object result = serializer.deserializeKey(key, context);
        assertNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKey_InvalidUrl() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext context = createContext();
        String key = "invalid";
        Object result = serializer.deserializeKey(key, context);
        assertNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKey_InvalidDate() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        DeserializationContext context = createContext();
        String key = "invalid";
        Object result = serializer.deserializeKey(key, context);
        assertNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeKey_InvalidClass() throws Exception {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext context = createContext();
        String key = "java.util.ArrayList";
        Object result = serializer.deserializeKey(key, context);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeKey_InvalidEnum() throws IOException {
        EnumResolver enumResolver = createEnumResolver(MyEnum.class);
        StdKeyDeserializer serializer = new EnumKD(enumResolver, null);
        DeserializationContext context = createContext();
        String key = "InvalidEnumValue";
        Object result = serializer.deserializeKey(key, context);
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_Uri() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        DeserializationContext context = createContext();
        String key = "http://www.example.com";
        Object result = serializer.deserializeKey(key, context);
        assertEquals(new URI("http://www.example.com"), result);
    }

    @Test
    public void testDeserializeKey_Url() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext context = createContext();
        String key = "http://www.example.com/";
        Object result = serializer.deserializeKey(key, context);
        assertEquals(new URL("http://www.example.com/"), result);
    }

    @Test(expected = MalformedURLException.class)
    public void testDeserializeKey_InvalidUrl() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        DeserializationContext context = createContext();
        String key = "invalid";
        Object result = serializer.deserializeKey(key, context);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidInt_Negative() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext context = createContext();
        String key = "-1";
        Object result = serializer.deserializeKey(key, context);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeKey_InvalidLong_Negative() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        DeserializationContext context = createContext();
        String key = "-9223372036854775808";
        Object result = serializer.deserializeKey(key, context);
        assertNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializeKey_UnknownType() throws IOException {
        StdKeyDeserializer serializer = new StdKeyDeserializer(-3, null);
        DeserializationContext context = createContext();
        Object result = serializer.deserializeKey("", context);
        assertNull(result);
    }

    private DeserializationContext createContext() {
        return new TestDeserializationContext();
    }

    private static EnumResolver createEnumResolver(Class<? extends Enum> enumClass) {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructEnumType(enumClass);
        return mapper.getFactory().getEnumResolverFactory().createResolver(javaType);
    }

    private static class TestDeserializationContext extends DeserializationContextBase {
        @Override
        public ObjectMapper getMapper() {
            return createObjectMapper();
        }

        @Override
        public JavaType findValueType(Class<?> rawType, XMLTypeDescriptor typeDeserializer,
                TypeDeserializer valueTypeDeserializer, AnnotatedMethod propertyValueDeserializer,
                PropertyName propertyName, JavaType valueType) {
            return TypeFactory.defaultInstance().constructType(valueType.getRawClass());
        }
    }
}