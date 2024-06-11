import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FromStringDeserializer.class})
public class FromStringDeserializerTest {

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTypes() {
        Class<?>[] types = FromStringDeserializer.types();
        assertNotNull(types);
        assertEquals(12, types.length);
    }

    @Test
    public void testFindDeserializer() {
        Std deserializer = FromStringDeserializer.findDeserializer(File.class);
        assertNotNull(deserializer);
        assertEquals(Std.STD_FILE, deserializer._kind);
    }

    @Test
    public void testDeserialize() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, Std.STD_CURRENCY);
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(jsonParser.getValueAsString()).thenReturn("USD");
        Currency result = (Currency) deserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(result);
        assertEquals("USD", result.getCurrencyCode());
    }

    @Test
    public void testDeserialize_EmbeddedObject() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, Std.STD_TIME_ZONE);
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_EMBEDDED_OBJECT);
        Mockito.when(jsonParser.getEmbeddedObject()).thenReturn(TimeZone.getDefault());
        TimeZone result = (TimeZone) deserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(result);
        assertEquals(TimeZone.getDefault(), result);
    }

    @Test
    public void testDeserialize_EmptyString() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, Std.STD_URI);
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(jsonParser.getValueAsString()).thenReturn("");
        URI result = (URI) deserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(result);
        assertEquals("", result.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_InvalidValue() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, Std.STD_LOCALE);
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(jsonParser.getValueAsString()).thenReturn("invalid_locale_value");
        deserializer.deserialize(jsonParser, deserializationContext);
    }

    @Test
    public void testDeserialize_EmbeddedObjectConversion() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, Std.STD_INET_ADDRESS);
        InetAddress inetAddress = InetAddress.getLocalHost();
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_EMBEDDED_OBJECT);
        Mockito.when(jsonParser.getEmbeddedObject()).thenReturn(inetAddress);
        InetAddress result = (InetAddress) deserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(result);
        assertEquals(inetAddress, result);
    }

    @Test
    public void testDeserialize_EmbeddedObjectError() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(InetAddress.class, Std.STD_INET_ADDRESS);
        InetAddress inetAddress = InetAddress.getLocalHost();
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(jsonParser.getValueAsString()).thenReturn("invalid_address");
        try {
            deserializer.deserialize(jsonParser, deserializationContext);
            fail("Expected IOException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

}