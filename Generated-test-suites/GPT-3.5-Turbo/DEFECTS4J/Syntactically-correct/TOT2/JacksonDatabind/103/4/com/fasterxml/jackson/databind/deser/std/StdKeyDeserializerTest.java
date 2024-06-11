import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;

@RunWith(MockitoJUnitRunner.class)
public class StdKeyDeserializerTest {

    @Mock
    DeserializationContext deserializationContext;

    @Test
    public void testDeserializeKey_NullKey_ReturnsNull() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, Object.class);
        assertNull(stdKeyDeserializer.deserializeKey(null, deserializationContext));
    }

    @Test
    public void testDeserializeKey_NotNullKey_ReturnsParsedValue() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, Object.class);
        assertEquals("test", stdKeyDeserializer.deserializeKey("test", deserializationContext));
    }

    @Test
    public void testParse_NullKey_ThrowsIOException() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, Object.class);
        assertThrows(IOException.class, () -> {
            stdKeyDeserializer.parse(null, deserializationContext);
        });
    }

    @Test
    public void testParse_InvalidKey_ThrowsException() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertThrows(Exception.class, () -> {
            stdKeyDeserializer.parse("invalid", deserializationContext);
        });
    }

    @Test
    public void testParse_IntKey_ReturnsInteger() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(Integer.valueOf(42), stdKeyDeserializer.parse("42", deserializationContext));
    }

    @Test
    public void testParse_DateKey_ReturnsDate() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DATE, Date.class);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date expectedDate = dateFormat.parse("2020-10-31");
        assertEquals(expectedDate, stdKeyDeserializer.parse("2020-10-31", deserializationContext));
    }

}