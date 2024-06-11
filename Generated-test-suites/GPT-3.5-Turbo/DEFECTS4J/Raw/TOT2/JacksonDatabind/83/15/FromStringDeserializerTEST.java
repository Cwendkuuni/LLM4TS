import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;

public class FromStringDeserializerTest {

    @Test
    public void testTypesMethod() {
        Class<?>[] types = FromStringDeserializer.types();
        assertNotNull(types);
        assertEquals(13, types.length);
    }

    @Test
    public void testFindDeserializer() {
        assertNull(FromStringDeserializer.findDeserializer(String.class));
        assertNotNull(FromStringDeserializer.findDeserializer(File.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeEmptyStringError() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(File.class, 1);
        stdDeserializer.deserialize(null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeMalformedURLError() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(URL.class, 2);
        stdDeserializer.deserialize(null, null);
    }

    @Test
    public void testDeserializeValidURL() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(URL.class, 2);
        Object result = stdDeserializer.deserialize("http://www.example.com", null);
        assertNotNull(result);
        assertTrue(result instanceof URL);
    }

    @Test
    public void testDeserializeEmptyStringToURI() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(URI.class, 3);
        Object result = stdDeserializer.deserialize("", null);
        assertNotNull(result);
        assertTrue(result instanceof URI);
        assertEquals("", result.toString());
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidUTF8Bytes() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Class.class, 4);
        stdDeserializer.deserialize("utf-8", null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidCurrency() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Currency.class, 6);
        stdDeserializer.deserialize("INVALID", null);
    }

    @Test
    public void testDeserializeValidCurrency() throws Exception {
        FromStringDeserializer.Std stdDeserializer = new FromStringDeserializer.Std(Currency.class, 6);
        Object result = stdDeserializer.deserialize("USD", null);
        assertNotNull(result);
        assertTrue(result instanceof Currency);
        assertEquals("USD", ((Currency) result).getCurrencyCode());
    }

}