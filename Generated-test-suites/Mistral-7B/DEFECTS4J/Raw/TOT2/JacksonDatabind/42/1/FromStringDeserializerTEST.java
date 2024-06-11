package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.Test;

/**
 * Test class for {@link FromStringDeserializer} class.
 */
public class FromStringDeserializerTest {

    /**
     * Test method for {@link FromStringDeserializer#deserialize(JsonParser, DeserializationContext)}.
     */
    @Test
    public void testDeserialize() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test deserializing a String to a supported type (File)
        String fileString = "path/to/file.txt";
        FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer<File>(File.class);
        File actualFile = fileDeserializer.deserialize(factory.createParser(mapper.writeValueAsString(fileString)), new TestContext());
        File expectedFile = new File(fileString);
        assertEquals(expectedFile, actualFile);

        // Test deserializing an empty String to a supported type (URL)
        String urlString = "";
        FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer<URL>(URL.class);
        URL actualUrl = urlDeserializer.deserialize(factory.createParser(mapper.writeValueAsString(urlString)), new TestContext());
        URL expectedUrl = new URL("");
        assertEquals(expectedUrl, actualUrl);

        // Test deserializing a null String to a supported type (URI)
        String uriString = null;
        FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer<URI>(URI.class);
        URI actualUri = uriDeserializer.deserialize(factory.createParser(mapper.writeValueAsString(uriString)), new TestContext());
        assertNull(actualUri);

        // Test deserializing a non-supported type
        String unsupportedString = "unsupportedType";
        FromStringDeserializer<String> unsupportedDeserializer = new FromStringDeserializer<String>(String.class);
        try {
            unsupportedDeserializer.deserialize(factory.createParser(mapper.writeValueAsString(unsupportedString)), new TestContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link FromStringDeserializer#findDeserializer(Class)}
     * and {@link Std#deserializerFromString(JsonParser, DeserializationContext)} methods.
     */
    @Test
    public void testFindDeserializerAndDeserializerFromString() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test deserializing a known type (Class)
        Class<File> fileClass = File.class;
        Std.StdFile fileDeserializer = Std.findDeserializer(fileClass);
        String fileString = "path/to/file.txt";
        File actualFile = fileDeserializer.deserializerFromString(factory.createParser(mapper.writeValueAsString(fileString)), new TestContext());
        File expectedFile = new File(fileString);
        assertEquals(expectedFile, actualFile);

        // Test deserializing an unknown type
        Class<String> stringClass = String.class;
        Std.StdClass stringDeserializer = Std.findDeserializer(stringClass);
        String stringValue = "unknownType";
        try {
            stringDeserializer.deserializerFromString(factory.createParser(mapper.writeValueAsString(stringValue)), new TestContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link FromStringDeserializer#deserializeEmbedded(Object, DeserializationContext)}
     * and {@link Std#deserializeEmbeddedObject(JsonParser, DeserializationContext)} methods.
     */
    @Test
    public void testDeserializeEmbedded() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test deserializing an embedded null object to a supported type (Locale)
        String localeString = "en_US";
        FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
        Locale actualLocale = localeDeserializer.deserializeEmbedded(factory.createParser(mapper.writeValueAsString("[\"{ \"locale\":\"" + localeString + "\"}]")), new TestContext());
        Locale expectedLocale = new Locale("en", "US");
        assertEquals(expectedLocale, actualLocale);

        // Test deserializing an embedded null object to an unsupported type (Float)
        String floatString = "3.14";
        FromStringDeserializer<Float> floatDeserializer = new FromStringDeserializer<Float>(Float.class);
        try {
            floatDeserializer.deserializeEmbedded(factory.createParser(mapper.writeValueAsString("[\"{ \"value\": " + floatString + "}\"]")), new TestContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link FromStringDeserializer#deserializeFromEmptyString()}
     * and {@link Std#STD_LOCALE} method.
     */
    @Test
    public void testDeserializeFromEmptyString() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test deserializing an empty String to a supported type (Locale)
        FromStringDeserializer<Locale> localeDeserializer = new FromStringDeserializer<Locale>(Locale.class);
        Locale actualLocale = localeDeserializer.deserializeFromEmptyString();
        Locale expectedLocale = Locale.ROOT;
        assertEquals(expectedLocale, actualLocale);
    }

    /**
     * Test class for deserializing special cases like InetAddress, InetSocketAddress, and URI.
     */
    @Test
    public void testSpecialCases() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test deserializing an InetAddress
        String ipv4AddressString = "192.168.1.1";
        FromStringDeserializer<InetAddress> ipv4AddressDeserializer = new FromStringDeserializer<InetAddress>(InetAddress.class);
        InetAddress actualAddress = ipv4AddressDeserializer.deserialize(factory.createParser(mapper.writeValueAsString(ipv4AddressString)), new TestContext());
        InetAddress expectedAddress = InetAddress.getByName(ipv4AddressString);
        assertEquals(expectedAddress, actualAddress);

        // Test deserializing an IPv6 address with a port number
        String ipv6AddressString = "[2001:0db8:85a3:0000:0000:8a2e:0370:7334]:1234";
        FromStringDeserializer<InetSocketAddress> ipv6AddressDeserializer = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class);
        InetSocketAddress actualAddress2 = ipv6AddressDeserializer.deserialize(factory.createParser(mapper.writeValueAsString(ipv6AddressString)), new TestContext());
        InetSocketAddress expectedAddress2 = new InetSocketAddress(ipv6AddressString, 1234);
        assertEquals(expectedAddress2, actualAddress2);

        // Test deserializing a URI
        String uriString = "http://www.example.com/path/to/resource";
        FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer<URI>(URI.class);
        URI actualUri2 = uriDeserializer.deserialize(factory.createParser(mapper.writeValueAsString(uriString)), new TestContext());
        URI expectedUri2 = new URI(uriString);
        assertEquals(expectedUri2, actualUri2);
    }

    /**
     * Test method for {@link Std#STD_FILE} and {@link Std#STD_URL} methods.
     */
    @Test
    public void testStdFileAndStdUrl() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test deserializing a File
        String fileString = "path/to/file.txt";
        FromStringDeserializer<File> fileDeserializer = new FromStringDeserializer<File>(File.class);
        File actualFile2 = fileDeserializer.deserialize(factory.createParser(mapper.writeValueAsString(fileString)), new TestContext());
        File expectedFile2 = new File(fileString);
        assertEquals(expectedFile2.getAbsolutePath(), actualFile2.getAbsolutePath());

        // Test deserializing a URL
        String urlString = "http://www.example.com";
        FromStringDeserializer<URL> urlDeserializer = new FromStringDeserializer<URL>(URL.class);
        URL actualUrl2 = urlDeserializer.deserialize(factory.createParser(mapper.writeValueAsString(urlString)), new TestContext());
        URL expectedUrl2 = new URL(urlString);
        assertEquals(expectedUrl2.toString(), actualUrl2.toString());
    }

    /**
     * Test method for {@link Std#STD_URI} method.
     */
    @Test
    public void testStdUri() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test deserializing a URI
        String uriString = "http://www.example.com/path/to/resource";
        FromStringDeserializer<URI> uriDeserializer = new FromStringDeserializer<URI>(URI.class);
        URI actualUri3 = uriDeserializer.deserialize(factory.createParser(mapper.writeValueAsString(uriString)), new TestContext());
        URI expectedUri3 = new URI(uriString);
        assertEquals(expectedUri3.toString(), actualUri3.toString());
    }

    private static class TestContext extends StdDeserializationContext {
        @Override
        public DeserializationFeature[] getActiveFeatures() {
            return new DeserializationFeature[] {};
        }

        @Override
        public DeserializationFeature forDeserializerFeature(String name) {
            return null;
        }

        @Override
        public void findValueHandler(Class<?> valueType) throws JavaRuntimeException {
            // empty
        }

        @Override
        public void findValueHandler(Class<?> valueType, String name) throws JavaRuntimeException {
            // empty
        }

        @Override
        public Object findValueHandler(Class<?> valueType, String name, JavaType propertyType) throws JavaRuntimeException {
            return null;
        }

        @Override
        public void findValueHandler(Class<?> valueType, String name, JavaType propertyType, JavaType contextType) throws JavaRuntimeException {
            // empty
        }

        @Override
        public void findValueHandler(Class<?> valueType, String name, JavaType propertyType, JavaType contextType, Object bean) throws JavaRuntimeException {
            // empty
        }

        @Override
        public JsonFactory getFactory() {
            return factory;
        }

        @Override
        public ObjectReader getGenericFactory() {
            return null;
        }

        @Override
        public ObjectReader getValueInstantiator() {
            return null;
        }

        @Override
        public ObjectReader getValueInstantiator(JavaType valueType) {
            return null;
        }

        @Override
        public ObjectReader getValueInstantiator(JavaType valueType, JavaType contextType) {
            return null;
        }

        @Override
        public ObjectReader getValueInjector() {
            return null;
        }

        @Override
        public ObjectReader getValueInjector(JavaType valueType) {
            return null;
        }

        @Override
        public ObjectReader getValueInjector(JavaType valueType, JavaType contextType) {
            return null;
        }

        @Override
        public ObjectReader getValueInjector(JavaType valueType, JsonDeserializer<?> deserializer) {
            return null;
        }

        @Override
        public ObjectReader getValueInjector(JavaType valueType, JsonDeserializer<?> deserializer, JavaType contextType) {
            return null;
        }

        @Override
        public ObjectReader getValueInjector(JavaType valueType, JsonDeserializer<?> deserializer, JsonDeserializer<?> contextDeserializer) {
            return null;
        }

        @Override
        public ObjectReader getValueInjector(JavaType valueType, Object bean) {
            return null;
        }

        @Override
        public ObjectReader getValueInjector(JavaType valueType, Object bean, JavaType contextType) {
            return null;
        }

        @Override
        public ObjectReader getValueInjector(JavaType valueType, Object bean, Object context) {
            return null;
        }

        @Override
        public ObjectReader getValueInjector(JavaType valueType, Object bean, Object context, JavaType contextType) {
            return null;
        }

        @Override
        public JsonValueProcessor getValueProcessor(Class<?> valueType) {
            return null;
        }

        @Override
        public JsonValueProcessor getValueProcessor(Class<?> valueType, JsonDeserializer<?> deserializer) {
            return null;
        }

        @Override
        public JsonValueProcessor getValueProcessor(Class<?> valueType, JsonDeserializer<?> deserializer, JavaType contextType) {
            return null;
        }

        @Override
        public JavaType handleValueType(JavaType valueType, DeserializationContext ctxt) throws JavaRuntimeException {
            return null;
        }

        @Override
        public JavaType handleValueType(JavaType valueType, DeserializationContext ctxt, JavaType propertyType) throws JavaRuntimeException {
            return null;
        }

        @Override
        public JavaType handleValueType(JavaType valueType, DeserializationContext ctxt, JavaType propertyType, JavaType contextType) throws JavaRuntimeException {
            return null;
        }

        @Override
        public JavaType handleValueType(JavaType valueType, DeserializationContext ctxt, JavaType propertyType, Object bean) throws JavaRuntimeException {
            return null;
        }

        @Override
        public JavaType handleValueType(JavaType valueType, DeserializationContext ctxt, JavaType propertyType, Object bean, JavaType contextType) throws JavaRuntimeException {
            return null;
        }

        // Other default methods

        @Override
        public JavaType handleValueType(Class<?> valueType, DeserializationContext ctxt) throws JavaRuntimeException {
            return null;
        }

        @Override
        public JavaType handleValueType(JavaType valueType, DeserializationContext ctxt, Object context) throws JavaRuntimeException {
            return null;
        }

        @Override
        public JavaType handleValueType(JavaType valueType, Object context) throws JavaRuntimeException {
            return null;
        }
    }
}