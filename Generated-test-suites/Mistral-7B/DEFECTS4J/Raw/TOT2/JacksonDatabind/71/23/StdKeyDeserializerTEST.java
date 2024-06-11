import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;
    private StdKeyDeserializer booleanDeserializer;
    private StdKeyDeserializer integerDeserializer;
    private StdKeyDeserializer stringDeserializer;
    private StdKeyDeserializer dateDeserializer;
    private StdKeyDeserializer uriDeserializer;
    private StdKeyDeserializer urlDeserializer;
    private StdKeyDeserializer localeDeserializer;
    private StdKeyDeserializer currencyDeserializer;

    @BeforeEach
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        booleanDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        integerDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        stringDeserializer = StdKeyDeserializer.forType(String.class);
        dateDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        uriDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
        urlDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URL, URL.class);
        localeDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LOCALE, Locale.class);
        currencyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CURRENCY, Currency.class);
    }

    @Test
    public void testBooleanDeserializer() throws IOException, JsonMappingException {
        Assertions.assertEquals(Boolean.TRUE, booleanDeserializer.deserializeKey("true", objectMapper.getDeserializationContext()));
        Assertions.assertEquals(Boolean.FALSE, booleanDeserializer.deserializeKey("false", objectMapper.getDeserializationContext()));
        Assertions.assertThrows(JsonMappingException.class, () -> booleanDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext()));
    }

    @Test
    public void testIntegerDeserializer() throws IOException, JsonMappingException {
        Assertions.assertEquals(1, integerDeserializer.deserializeKey("1", objectMapper.getDeserializationContext()));
        Assertions.assertEquals(-1, integerDeserializer.deserializeKey("-1", objectMapper.getDeserializationContext()));
        Assertions.assertThrows(JsonMappingException.class, () -> integerDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext()));
    }

    @Test
    public void testStringDeserializer() throws IOException, JsonMappingException {
        Assertions.assertEquals("test", stringDeserializer.deserializeKey("test", objectMapper.getDeserializationContext()));
        Assertions.assertNotEquals("test", stringDeserializer.deserializeKey("123", objectMapper.getDeserializationContext()));
    }

    @Test
    public void testDateDeserializer() throws IOException, JsonMappingException {
        Date date = new Date();
        Assertions.assertEquals(date, dateDeserializer.deserializeKey(date.toString(), objectMapper.getDeserializationContext()));
        Assertions.assertThrows(JsonMappingException.class, () -> dateDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext()));
    }

    @Test
    public void testUriDeserializer() throws IOException, JsonMappingException {
        URI uri = new URI("https://test.com");
        Assertions.assertEquals(uri, uriDeserializer.deserializeKey(uri.toString(), objectMapper.getDeserializationContext()));
        Assertions.assertThrows(JsonMappingException.class, () -> uriDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext()));
    }

    @Test
    public void testUrlDeserializer() throws IOException, JsonMappingException {
        URL url = new URL("https://test.com");
        Assertions.assertEquals(url, urlDeserializer.deserializeKey(url.toString(), objectMapper.getDeserializationContext()));
        Assertions.assertThrows(JsonMappingException.class, () -> urlDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext()));
    }

    @Test
    public void testLocaleDeserializer() throws IOException, JsonMappingException {
        Locale locale = new Locale("en_US");
        Assertions.assertEquals(locale, localeDeserializer.deserializeKey(locale.toString(), objectMapper.getDeserializationContext()));
        Assertions.assertThrows(JsonMappingException.class, () -> localeDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext()));
    }

    @Test
    public void testCurrencyDeserializer() throws IOException, JsonMappingException {
        Currency currency = Currency.getInstance("USD");
        Assertions.assertEquals(currency, currencyDeserializer.deserializeKey(currency.getSymbol(), objectMapper.getDeserializationContext()));
        Assertions.assertThrows(JsonMappingException.class, () -> currencyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext()));
    }

    @Test
    public void testNullKey() throws IOException, JsonMappingException {
        Assertions.assertNull(stringDeserializer.deserializeKey(null, objectMapper.getDeserializationContext()));
    }

    @Test
    public void testEnumDeserializer() throws IOException, JsonMappingException {
        EnumSet<EnumTest> expectedEnumValues = EnumSet.allOf(EnumTest.class);

        for (EnumTest enumValue : expectedEnumValues) {
            Assertions.assertEquals(enumValue, enumDeserializer.deserializeKey(enumValue.name(), objectMapper.getDeserialationContext()));
        }

        Assertions.assertThrows(JsonMappingException.class, () -> enumDeserializer.deserializeKey("invalid", objectMapper.getDeserialationContext()));
    }

    @Test
    public void testUnknownEnumValue() throws IOException, JsonMappingException {
        Assertions.assertNull(enumDeserializer.deserializeKey("UnknownValue", objectMapper.getDeserialationContext()));
    }

    @Test
    public void testNullEnumDeserializer() throws IOException, JsonMappingException {
        Assertions.assertNull(enumDeserializer.deserializeKey(null, objectMapper.getDeserialationContext()));
    }

    @Test
    public void testUnknownClassDeserializer() throws IOException, JsonMappingException {
        Assertions.assertNull(keyDeserializer.deserializeKey("UnknownClass", objectMapper.getDeserialationContext()));
    }
}