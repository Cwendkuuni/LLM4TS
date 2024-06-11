package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FileDeserializer fileDeserializer = (FileDeserializer) FromStringDeserializer.findDeserializer(File.class);
        Object result = fileDeserializer.deserialize(mapper.getFactory().createParser("path/to/file.txt"), new TestContext());
        assertEquals(new File("path/to/file.txt"), result);
    }

    @Test
    public void testDeserializeURL() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        URLEndPointDeserializer urlDeserializer = (URLEndPointDeserializer) FromStringDeserializer.findDeserializer(URL.class);
        Object result = urlDeserializer.deserialize(mapper.getFactory().createParser("http://example.com"), new TestContext());
        assertEquals(new URL("http://example.com"), result);
    }

    @Test
    public void testDeserializeURI() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        URIDeserializer uriDeserializer = (URIDeserializer) FromStringDeserializer.findDeserializer(URI.class);
        Object result = uriDeserializer.deserialize(mapper.getFactory().createParser("uri://example.com/path/to"), new TestContext());
        assertEquals(new URI("uri://example.com/path/to"), result);
    }

    @Test
    public void testDeserializeClass() throws IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        ClassDeserializer classDeserializer = (ClassDeserializer) FromStringDeserializer.findDeserializer(String.class);
        Object result = classDeserializer.deserialize(mapper.getFactory().createParser("java.lang.String"), new TestContext());
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaTypeDeserializer javaTypeDeserializer = (JavaTypeDeserializer) FromStringDeserializer.findDeserializer(JavaType.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        Object result = javaTypeDeserializer.deserialize(mapper.getFactory().createParser("class java.lang.String"), new TestContext());
        assertEquals(javaType, result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CurrencyDeserializer currencyDeserializer = (CurrencyDeserializer) FromStringDeserializer.findDeserializer(Currency.class);
        Object result = currencyDeserializer.deserialize(mapper.getFactory().createParser("USD"), new TestContext());
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        PatternDeserializer patternDeserializer = (PatternDeserializer) FromStringDeserializer.findDeserializer(Pattern.class);
        Object result = patternDeserializer.deserialize(mapper.getFactory().createParser("\\w+"), new TestContext());
        assertEquals(Pattern.compile("\\w+"), result);
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        LocaleDeserializer localeDeserializer = (LocaleDeserializer) FromStringDeserializer.findDeserializer(Locale.class);
        Object result = localeDeserializer.deserialize(mapper.getFactory().createParser("en_US"), new TestContext());
        assertEquals(Locale.US, result);

        Object result2 = localeDeserialializer.deserialize(mapper.getFactory().createParser("en_US_POSIX"), new TestContext());
        assertEquals(Locale.US, result2);

        Object result3 = localeDeserialializer.deserialize(mapper.getFactory().createParser("en_US_POSIX.lepo"), new TestContext());
        assertEquals(Locale.US, result3);
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CharsetDeserializer charsetDeserializer = (CharsetDeserializer) FromStringDeserializer.findDeserializer(Charset.class);
        Object result = charsetDeserializer.deserialize(mapper.getFactory().createParser("UTF-8"), new TestContext());
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TimeZoneDeserializer timeZoneDeserializer = (TimeZoneDeserializer) FromStringDeserializer.findDeserializer(TimeZone.class);
        Object result = timeZoneDeserializer.deserialize(mapper.getFactory().createParser("UTC"), new TestContext());
        assertEquals(TimeZone.getTimeZone("UTC"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InetAddressDeserializer inetAddressDeserializer = (InetAddressDeserializer) FromStringDeserializer.findDeserializer(InetAddress.class);
        Object result = inetAddressDeserializer.deserialize(mapper.getFactory().createParser("192.168.1.1"), new TestContext());
        assertEquals(InetAddress.getByName("192.168.1.1"), result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InetSocketAddressDeserializer inetSocketAddressDeserializer = (InetSocketAddressDeserializer) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        Object result = inetSocketAddressDeserializer.deserialize(mapper.getFactory().createParser("[255.255.255.255]:80"), new TestContext());
        assertEquals(new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 80), result);

        Object result2 = inetSocketAddressDeserializer.deserialize(mapper.getFactory().createParser("192.168.1.1:80"), new TestContext());
        assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.1.1"), 80), result2);
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object result = new FromStringDeserializer<Object>(Object.class).deserialize(mapper.getFactory().createParser(""), new TestContext());
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object embeddedObject = new Object();
        Object result = new FromStringDeserializer<Object>(Object.class).deserialize(mapper.getFactory().createEmbeddedObjectParser(embeddedObject), new TestContext());
        assertEquals(embeddedObject, result);
    }

    @Test
    public void testDeserializeNullEmbeddedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object result = new FromStringDeserializer<Object>(Object.class).deserialize(mapper.getFactory().createEmbeddedObjectParser(null), new TestContext());
        assertNull(result);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public boolean isEnabled(DeserializationFeature feature) {
            return true;
        }

        @Override
        public Object findValueInRoot(String name) throws IOException {
            return null;
        }

        @Override
        public JavaType findTypeByDescriptionOnly(String name, DeserializationContext ctxt, JavaType baseType) throws IOException, JsonProcessingException {
            return null;
        }

        @Override
        public void handleUnknownProperty(String name, Object beanOrClass, String propertyName) throws JsonMappingException {
        }

        @Override
        public void handleIncompleteValue(String propertyName, JsonParser p, DeserializationContext ctxt) throws IOException, JsonMappingException {
        }

        @Override
        public void handleMissingValue(String propertyName, Object bean, JsonParser p) throws JsonMappingException {
        }

        @Override
        public void handleEmptyStringProperty(String propertyName, Object bean) throws JsonMappingException {
        }

        @Override
        public void handleValueNotWritable(String propertyName, Object bean) throws JsonMappingException {
        }

        @Override
        public void handleNonWritableProperty(String propertyName, Object bean, JsonParser p) throws JsonMappingException {
        }

        @Override
        public void handleUnknownValue(Object value, Class<?> valueType, JsonParser p) throws JsonMappingException {
        }

        @Override
        public void handleNullValue(String propertyName, Object bean) throws JsonMappingException {
        }

        @Override
        public void handleMissingValue(String propertyName, Object bean, JsonParser p) throws JsonMappingException {
        }

        @Override
        public void handleInfiniteRecursion(String propertyName, Object bean) throws JsonMappingException {
        }

        @Override
        public void handleValueTooComplex(Class<?> valueType, Object value, JsonParser p) throws JsonMappingException {
        }

        @Override
        public void handleUnknownClass(Class<?> valueType, JsonParser p) throws JsonMappingException {
        }
    }
}