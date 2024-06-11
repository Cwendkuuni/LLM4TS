import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.runner.RunWith;

@RunWith(JUnit4.class)
public class FromStringDeserializerTest {

    @Test
    public void testTypes() {
        Class<?>[] expectedTypes = {
            File.class,
            URL.class,
            URI.class,
            Class.class,
            JavaType.class,
            Currency.class,
            Pattern.class,
            Locale.class,
            Charset.class,
            TimeZone.class,
            InetAddress.class,
            InetSocketAddress.class
        };
        assertArrayEquals(expectedTypes, FromStringDeserializer.types());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDeserializerUnknownType() {
        assertNull(FromStringDeserializer.findDeserializer(String.class));
    }

    @Test
    public void testDeserializeFromEmptyStringWithURI() throws IOException {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        assertEquals(URI.create(""), uriDeserializer.deserialize(new JsonParser(), DeserializationContext()));
    }

    @Test
    public void testDeserializeFromEmptyStringWithOtherTypes() throws IOException {
        FromStringDeserializer.Std defaultDeserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        assertNull(defaultDeserializer.deserialize(new JsonParser(), DeserializationContext()));
    }

    @Test
    public void testDeserializeValidURI() throws IOException {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI expectedURI = URI.create("http://example.com");
        assertEquals(expectedURI, uriDeserializer.deserialize(new JsonParser(expectedURI.toString()), DeserializationContext()));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidURI() throws IOException {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        uriDeserializer.deserialize(new JsonParser("invalidURI"), DeserializationContext());
    }

    @Test
    public void testDeserializeValidCurrency() throws IOException {
        FromStringDeserializer.Std currencyDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        Currency expectedCurrency = Currency.getInstance("USD");
        assertEquals(expectedCurrency, currencyDeserializer.deserialize(new JsonParser("USD"), DeserializationContext()));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidCurrency() throws IOException {
        FromStringDeserializer.Std currencyDeserializer = new FromStringDeserializer.Std(Currency.class, FromStringDeserializer.Std.STD_CURRENCY);
        currencyDeserializer.deserialize(new JsonParser("InvalidCurrency"), DeserializationContext());
    }

    // Additional test cases for other methods in the class can be added here

}