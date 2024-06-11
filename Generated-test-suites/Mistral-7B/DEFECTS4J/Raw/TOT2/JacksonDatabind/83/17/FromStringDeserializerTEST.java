package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;

@RunWith(MockitoJUnitRunner.class)
public class FromStringDeserializerTest {

    @Test
    public void testDeserializeURL() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();

        String jsonString = "\"http://www.example.com\"";
        URL url = new FromStringDeserializer<URL>(URL.class).deserialize(jsonFactory.createParser(jsonString), mapper.getDeserializationContext());
        assertEquals("http://www.example.com", url.toString());
    }

    @Test
    public void testDeserializeURI() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();

        String jsonString = "\"http://www.example.com/path/%22%61%6D%22\"";
        URI uri = new FromStringDeserializer<URI>(URI.class).deserialize(jsonFactory.createParser(jsonString), mapper.getDeserializationContext());
        assertEquals("http://www.example.com/path/apple", uri.toString());
    }

    @Test
    public void testDeserializeClass() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();

        String jsonString = "String";
        Class<?> clazz = new FromStringDeserializer<Class<String>>(String.class).deserialize(jsonFactory.createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(String.class, clazz);
    }

    @Test
    public void testDeserializeJavaType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();

        String jsonString = "com.fasterxml.jackson.databind.JavaType";
        JavaType javaType = new FromStringDeserializer<JavaType>(JavaType.class).deserialize(jsonFactory.createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(JavaType.class, javaType.getRawClass());
    }

    @Test
    public void testDeserializeCurrency() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();

        String jsonString = "USD";
        Currency currency = new FromStringDeserializer<Currency>(Currency.class).deserialize(jsonFactory.createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(Currency.getInstance("USD"), currency);
    }

    @Test
    public void testDeserializePattern() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();

        String jsonString = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = new FromStringDeserializer<Pattern>(Pattern.class).deserialize(jsonFactory.createParser(jsonString), mapper.getDeserializationContext());
        // validate pattern
        assertTrue(pattern.matcher("test@example.com").matches());
        assertTrue(pattern.matcher("TEST@EXAMPLE.CO.UK").matches());
        assertFalse(pattern.matcher("test@example,com").matches());
        assertFalse(pattern.matcher("test@example..com").matches());
    }

    @Test
    public void testDeserializeLocale() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();

        String jsonString = "en_US";
        Locale locale = new FromStringDeserializer<Locale>(Locale.class).deserialize(jsonFactory.createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(Locale.US, locale);

        jsonString = "fr_FR@Europe";
        locale = new FromStringDeserializer<Locale>(Locale.class).deserialize(jsonFactory.createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(Locale.FRENCH, locale);
        assertEquals("Europe", locale.getDisplayCountry());
    }

    @Test
    public void testDeserializeCharset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();

        String jsonString = "UTF-8";
        Charset charset = new FromStringDeserializer<Charset>(Charset.class).deserialize(jsonFactory.createParser(jsonString), mapper.getDeserializationContext());
        assertEquals(Charset.forName("UTF-8"), charset);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();

        String jsonString = "Europe/London";
        TimeZone timeZone = new FromStringDeserializer<TimeZone>(TimeZone.class).deserialize(jsonFactory.createParser(jsonString), mapper.getDeserializationContext());
        assertEquals("Europe/London", timeZone.getID());
        assertEquals(TimeZone.getTimeZone("Europe/London"), timeZone);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();

        String jsonString = "192.168.1.1";
        InetAddress inetAddress = new FromStringDeserializer<InetAddress>(InetAddress.class).deserialize(jsonFactory.createParser(jsonString), mapper.getDeserializationContext());
        assertEquals("192.168.1.1", inetAddress.getHostAddress());

        jsonString = "[2001:0db8:85a3:0000:0000:8a2e:0370:7334]";
        inetAddress = new FromStringDeserializer<InetAddress>(InetAddress.class).deserialize(jsonFactory.createParser(jsonString), mapper.getDeserializationContext());
        assertEquals("[2001:0db8:85a3:0:0:8a2e:370:7334]", inetAddress.getHostAddress());
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();

        String jsonString = "192.168.1.1:8080";
        InetSocketAddress inetSocketAddress = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class).deserialize(jsonFactory.createParser(jsonString), mapper.getDeserializationContext());
        assertEquals("192.168.1.1", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress.getPort());

        jsonString = "[2001:0db8:85a3:0:0:8a2e:370:7334]:8080";
        inetSocketAddress = new FromStringDeserializer<InetSocketAddress>(InetSocketAddress.class).deserialize(jsonFactory.createParser(jsonString), mapper.getDeserializationContext());
        assertEquals("[2001:0db8:85a3:0:0:8a2e:370:7334]", inetSocketAddress.getAddress().getHostAddress());
        assertEquals(8080, inetSocketAddress.getPort());
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();

        String jsonString = "";
        Object deserializedObject = new FromStringDeserializer<Object>(Object.class).deserialize(jsonFactory.createParser(jsonString), mapper.getDeserializationContext());
        assertNull(deserializedObject);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();

        String jsonString = "null";
        new FromStringDeserializer<Object>(Object.class).deserialize(jsonFactory.createParser(jsonString), mapper.getDeserializationContext());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();

        String jsonString = "InvalidFormatException";
        new FromStringDeserializer<Object>(Object.class).deserialize(jsonFactory.createParser(jsonString), mapper.getDeserializationContext());
    }

    @Test(expected = MalformedURLException.class)
    public void testDeserializeMalformedURL() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonFactory jsonFactory = mapper.getFactory();

        String jsonString = "malformed:/";
        new FromStringDeserializer<URL>(URL.class).deserialize(jsonFactory.createParser(jsonString), mapper.getDeserializationContext());
    }
}