package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.node.TextNode;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws Exception {
        FromStringDeserializer<File> serializer = (FromStringDeserializer<File>) FromStringDeserializer.findDeserializer(File.class);
        DeserializationContext ctxt = new DeserializationContext(new JsonFactory(), new StandardDeserializer.Std(Object.class));
        JsonParser jp = new JsonFactory().createParser(new ByteArrayInputStream(new TextNode("path/to/file").textValue().getBytes()));
        File result = serializer.deserialize(jp, ctxt);
        assertEquals(new File("path/to/file"), result);
    }

    @Test
    public void testDeserializeURL() throws Exception {
        FromStringDeserializer<URL> serializer = (FromStringDeserializer<URL>) FromStringDeserializer.findDeserializer(URL.class);
        DeserializationContext ctxt = new DeserializationContext(new JsonFactory(), new StandardDeserializer.Std(Object.class));
        JsonParser jp = new JsonFactory().createParser(new ByteArrayInputStream(new TextNode("http://example.com").textValue().getBytes()));
        URL result = serializer.deserialize(jp, ctxt);
        assertEquals(new URL("http://example.com"), result);
    }

    @Test
    public void testDeserializeURI() throws Exception {
        FromStringDeserializer<URI> serializer = (FromStringDeserializer<URI>) FromStringDeserializer.findDeserializer(URI.class);
        DeserializationContext ctxt = new DeserializationContext(new JsonFactory(), new StandardDeserializer.Std(Object.class));
        JsonParser jp = new JsonFactory().createParser(new ByteArrayInputStream(new TextNode("http://example.com/path/to/resource").textValue().getBytes()));
        URI result = serializer.deserialize(jp, ctxt);
        assertEquals(new URI("http://example.com/path/to/resource"), result);
    }

    @Test
    public void testDeserializeClass() throws Exception {
        FromStringDeserializer<Class<?>> serializer = (FromStringDeserializer<Class<?>>) FromStringDeserializer.findDeserializer(Class.class);
        DeserializationContext ctxt = new DeserializationContext(new JsonFactory(), new StandardDeserializer.Std(Object.class));
        JsonParser jp = new JsonFactory().createParser(new ByteArrayInputStream(new TextNode("java.lang.String.class").textValue().getBytes()));
        Class<?> result = serializer.deserialize(jp, ctxt);
        assertEquals(String.class, result);
    }

    @Test
    public void testDeserializeJavaType() throws Exception {
        FromStringDeserializer<JavaType> serializer = (FromStringDeserializer<JavaType>) FromStringDeserializer.findDeserializer(JavaType.class);
        DeserializationContext ctxt = new DeserializationContext(new JsonFactory(), new StandardDeserializer.Std(Object.class));
        JsonParser jp = new JsonFactory().createParser(new ByteArrayInputStream(new TextNode("com.example.MyClass").textValue().getBytes()));
        JavaType result = serializer.deserialize(jp, ctxt);
        assertEquals(JavaType.constructUnchecked(MyClass.class), result);
    }

    @Test
    public void testDeserializeCurrency() throws Exception {
        FromStringDeserializer<Currency> serializer = (FromStringDeserializer<Currency>) FromStringDeserializer.findDeserializer(Currency.class);
        DeserializationContext ctxt = new DeserializationContext(new JsonFactory(), new StandardDeserializer.Std(Object.class));
        JsonParser jp = new JsonFactory().createParser(new ByteArrayInputStream(new TextNode("USD").textValue().getBytes()));
        Currency result = serializer.deserialize(jp, ctxt);
        assertEquals(Currency.getInstance("USD"), result);
    }

    @Test
    public void testDeserializePattern() throws Exception {
        FromStringDeserializer<Pattern> serializer = (FromStringDeserializer<Pattern>) FromStringDeserializer.findDeserializer(Pattern.class);
        DeserializationContext ctxt = new DeserializationContext(new JsonFactory(), new StandardDeserializer.Std(Object.class));
        JsonParser jp = new JsonFactory().createParser(new ByteArrayInputStream(new TextNode("/(.*).txt").textValue().getBytes()));
        Pattern result = serializer.deserialize(jp, ctxt);
        assertEquals(Pattern.compile("/(.*).txt"), result);
    }

    @Test
    public void testDeserializeLocale() throws Exception {
        FromStringDeserializer<Locale> serializer = (FromStringDeserializer<Locale>) FromStringDeserializer.findDeserializer(Locale.class);
        DeserializationContext ctxt = new DeserializationContext(new JsonFactory(), new StandardDeserializer.Std(Object.class));
        JsonParser jp = new JsonFactory().createParser(new ByteArrayInputStream(new TextNode("en_US").textValue().getBytes()));
        Locale result = serializer.deserialize(jp, ctxt);
        assertEquals(Locale.US, result);

        jp = new JsonFactory().createParser(new ByteArrayInputStream(new TextNode("en_GB_x_POSIX").textValue().getBytes()));
        result = serializer.deserialize(jp, ctxt);
        assertEquals(Locale.Builder.getInstance().setLanguageTag("en").setRegion("GB").setVariants(new Locale.VariableKey[]{}).build(), result);
    }

    @Test
    public void testDeserializeCharset() throws Exception {
        FromStringDeserializer<Charset> serializer = (FromStringDeserializer<Charset>) FromStringDeserializer.findDeserializer(Charset.class);
        DeserializationContext ctxt = new DeserializationContext(new JsonFactory(), new StandardDeserializer.Std(Object.class));
        JsonParser jp = new JsonFactory().createParser(new ByteArrayInputStream(new TextNode("UTF-8").textValue().getBytes()));
        Charset result = serializer.deserialize(jp, ctxt);
        assertEquals(Charset.forName("UTF-8"), result);
    }

    @Test
    public void testDeserializeTimeZone() throws Exception {
        FromStringDeserializer<TimeZone> serializer = (FromStringDeserializer<TimeZone>) FromStringDeserializer.findDeserializer(TimeZone.class);
        DeserializationContext ctxt = new DeserializationContext(new JsonFactory(), new StandardDeserializer.Std(Object.class));
        JsonParser jp = new JsonFactory().createParser(new ByteArrayInputStream(new TextNode("UTC").textValue().getBytes()));
        TimeZone result = serializer.deserialize(jp, ctxt);
        assertEquals(TimeZone.getTimeZone("UTC"), result);
    }

    @Test
    public void testDeserializeInetAddress() throws Exception {
        FromStringDeserializer<InetAddress> serializer = (FromStringDeserializer<InetAddress>) FromStringDeserializer.findDeserializer(InetAddress.class);
        DeserializationContext ctxt = new DeserializationContext(new JsonFactory(), new StandardDeserializer.Std(Object.class));
        JsonParser jp = new JsonFactory().createParser(new ByteArrayInputStream(new TextNode("192.168.1.1").textValue().getBytes()));
        InetAddress result = serializer.deserialize(jp, ctxt);
        assertEquals(InetAddress.getByName("192.168.1.1"), result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws Exception {
        FromStringDeserializer<InetSocketAddress> serializer = (FromStringDeserializer<InetSocketAddress>) FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        DeserializationContext ctxt = new DeserializationContext(new JsonFactory(), new StandardDeserializer.Std(Object.class));
        JsonParser jp = new JsonFactory().createParser(new ByteArrayInputStream(new TextNode("[255.255.255.255]:80").textValue().getBytes()));
        InetSocketAddress result = serializer.deserialize(jp, ctxt);
        assertEquals(new InetSocketAddress(InetAddress.getByName("255.255.255.255"), 80), result);

        jp = new JsonFactory().createParser(new ByteArrayInputStream(new TextNode("192.168.1.1:80").textValue().getBytes()));
        result = serializer.deserialize(jp, ctxt);
        assertEquals(new InetSocketAddress(InetAddress.getByName("192.168.1.1"), 80), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnknownType() throws Exception {
        FromStringDeserializer<Object> serializer = (FromStringDeserializer<Object>) FromStringDeserializer.findDeserializer(Integer.class);
        DeserializationContext ctxt = new DeserializationContext(new JsonFactory(), new StandardDeserializer.Std(Object.class));
        JsonParser jp = new JsonFactory().createParser(new ByteArrayInputStream(new TextNode("42").textValue().getBytes()));
        serializer.deserialize(jp, ctxt);
        fail(); // This test should not reach here
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserializeInvalidString() throws Exception {
        FromStringDeserializer<Object> serializer = (FromStringDeserializer<Object>) FromStringDeserializer.findDeserializer(Object.class);
        DeserializationContext ctxt = new DeserializationContext(new JsonFactory(), new StandardDeserializer.Std(Object.class));
        JsonParser jp = new JsonFactory().createParser(new ByteArrayInputStream(new TextNode("foo").textValue().getBytes()));
        serializer.deserialize(jp, ctxt);
        fail(); // This test should not reach here
    }
}