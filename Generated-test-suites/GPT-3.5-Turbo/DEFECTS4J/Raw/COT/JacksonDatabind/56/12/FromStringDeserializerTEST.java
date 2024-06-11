
import org.junit.Test;
import java.io.File;
import java.net.URL;
import java.net.URI;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;

public class FromStringDeserializerTest {

    @Test
    public void testDeserializeFile() throws Exception {
        String value = "file.txt";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, 1);
        DeserializationContext ctxt = null;
        assert deserializer._deserialize(value, ctxt).getClass().equals(File.class);
    }

    @Test
    public void testDeserializeURL() throws Exception {
        String value = "https://www.google.com";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, 2);
        DeserializationContext ctxt = null;
        assert deserializer._deserialize(value, ctxt).getClass().equals(URL.class);
    }

    @Test
    public void testDeserializeURI() throws Exception {
        String value = "http://www.example.com";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);
        DeserializationContext ctxt = null;
        assert deserializer._deserialize(value, ctxt).getClass().equals(URI.class);
    }

    @Test
    public void testDeserializeCurrency() throws Exception {
        String value = "USD";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Currency.class, 6);
        DeserializationContext ctxt = null;
        assert deserializer._deserialize(value, ctxt).getClass().equals(Currency.class);
    }

    @Test
    public void testDeserializePattern() throws Exception {
        String value = "[a-z]+";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Pattern.class, 7);
        DeserializationContext ctxt = null;
        assert deserializer._deserialize(value, ctxt).getClass().equals(Pattern.class);
    }

    @Test
    public void testDeserializeLocale() throws Exception {
        String value = "en_US";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Locale.class, 8);
        DeserializationContext ctxt = null;
        assert deserializer._deserialize(value, ctxt).getClass().equals(Locale.class);
    }

    @Test
    public void testDeserializeTimeZone() throws Exception {
        String value = "America/Los_Angeles";
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(TimeZone.class, 10);
        DeserializationContext ctxt = null;
        assert deserializer._deserialize(value, ctxt).getClass().equals(TimeZone.class);
    }
}
