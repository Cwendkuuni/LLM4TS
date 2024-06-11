package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

public class FromStringDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeFile() throws IOException, JsonProcessingException {
        FromStringDeserializer<File> fileDeserializer = FromStringDeserializer.findDeserializer(File.class);
        File expectedResult = new File("test.txt");
        String jsonString = "test.txt";
        File result = fileDeserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeURL() throws IOException, JsonProcessingException {
        FromStringDeserializer<URL> urlDeserializer = FromStringDeserializer.findDeserializer(URL.class);
        URL expectedResult = new URL("http://www.example.com");
        String jsonString = "http://www.example.com";
        URL result = urlDeserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeURI() throws IOException, JsonProcessingException {
        FromStringDeserializer<URI> uriDeserializer = FromStringDeserializer.findDeserializer(URI.class);
        URI expectedResult = new URI("http://www.example.com");
        String jsonString = "http://www.example.com";
        URI result = uriDeserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeClass() throws IOException, JsonProcessingException {
        FromStringDeserializer<String> stringDeserializer = FromStringDeserializer.findDeserializer(String.class);
        String expectedResult = "Test String";
        String jsonString = "Test String";
        String result = stringDeserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext()).getValueAsString();
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeJavaType() throws IOException, JsonProcessingException {
        JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
        FromStringDeserializer<Object> objectDeserializer = FromStringDeserializer.findDeserializer(Object.class);
        FromStringDeserializer<Object> javaTypeDeserializer = FromStringDeserializer.findDeserializer(javaType.getRawClass());
        Object[] expectedResult = new Object[]{"Test String 1", "Test String 2"};
        String jsonString = "[\"Test String 1\", \"Test String 2\"]";
        Object result = javaTypeDeserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext()).getValueAsObject();
        Assert.assertArrayEquals(expectedResult, (Object[])result);
    }

    @Test
    public void testDeserializeCurrency() throws IOException, JsonProcessingException {
        FromStringDeserializer<Currency> currencyDeserializer = FromStringDeserializer.findDeserializer(Currency.class);
        Currency expectedResult = Currency.getInstance("USD");
        String jsonString = "USD";
        Currency result = currencyDeserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializePattern() throws IOException, JsonProcessingException {
        FromStringDeserializer<Pattern> patternDeserializer = FromStringDeserializer.findDeserializer(Pattern.class);
        Pattern expectedResult = Pattern.compile("\\w+");
        String jsonString = "\\w+";
        Pattern result = patternDeserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeLocale() throws IOException, JsonProcessingException {
        FromStringDeserializer<Locale> localeDeserializer = FromStringDeserializer.findDeserializer(Locale.class);
        Locale expectedResult = Locale.ENGLISH;
        String jsonStringEN = "en_US";
        Locale expectedResult_FR = new Locale("fr", "FR");
        String jsonStringFR = "fr_FR";

        Locale resultEN = localeDeserializer.deserialize(mapper.getFactory().createParser(jsonStringEN), mapper.getDeserializationContext());
        Assert.assertEquals(expectedResultEN, resultEN);

        Locale resultFR = localeDeserializer.deserialize(mapper.getFactory().createParser(jsonStringFR), mapper.getDeserializationContext());
        Assert.assertEquals(expectedResult_FR, resultFR);
    }

    @Test
    public void testDeserializeCharset() throws IOException, JsonProcessingException {
        FromStringDeserializer<Charset> charsetDeserializer = FromStringDeserializer.findDeserializer(Charset.class);
        Charset expectedResult = Charset.forName("UTF-8");
        String jsonString = "UTF-8";
        Charset result = charsetDeserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeTimeZone() throws IOException, JsonProcessingException {
        FromStringDeserializer<TimeZone> timeZoneDeserializer = FromStringDeserializer.findDeserializer(TimeZone.class);
        TimeZone expectedResult = TimeZone.getTimeZone("UTC");
        String jsonString = "UTC";
        TimeZone result = timeZoneDeserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeInetAddress() throws IOException, JsonProcessingException {
        FromStringDeserializer<InetAddress> inetAddressDeserializer = FromStringDeserializer.findDeserializer(InetAddress.class);
        InetAddress expectedResult = InetAddress.getByName("127.0.0.1");
        String jsonString = "127.0.0.1";
        InetAddress result = inetAddressDeserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeInetSocketAddress() throws IOException, JsonProcessingException {
        FromStringDeserializer<InetSocketAddress> inetSocketAddressDeserializer = FromStringDeserializer.findDeserializer(InetSocketAddress.class);
        InetSocketAddress expectedResult = new InetSocketAddress("127.0.0.1", 80);
        String jsonString = "[\"127.0.0.1\", 80]";
        InetSocketAddress result = inetSocketAddressDeserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext()).getValueAsObject();
        Assert.assertEquals(expectedResult.getAddress(), ((InetSocketAddress)result).getAddress());
        Assert.assertEquals(expectedResult.getPort(), ((InetSocketAddress)result).getPort());
    }

    @Test
    public void testDeserializeEmbeddedObject() throws IOException, JsonProcessingException {
        class TestClass {
            private String name;

            public TestClass(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        FromStringDeserializer<TestClass> testClassDeserializer = new FromStringDeserializer<TestClass>(TestClass.class) {
            @Override
            protected TestClass _deserializeEmbedded(Object ob, DeserializationContext ctxt) throws IOException {
                String name = ((String)ob).trim();
                return new TestClass(name);
            }
        };

        TestClass expectedResult = new TestClass("Test Embedded Object");
        String jsonString = "{\"name\": \"Test Embedded Object\"}";
        TestClass result = testClassDeserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
        Assert.assertEquals(expectedResult.getName(), result.getName());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidType() throws IOException, JsonProcessingException {
        FromStringDeserializer<Integer> integerDeserializer = FromStringDeserializer.findDeserializer(Integer.class);
        String jsonString = "Test String";
        integerDeserializer.deserialize(mapper.getFactory().createParser(jsonString), mapper.getDeserializationContext());
    }
}