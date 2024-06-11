import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;
import java.util.UUID;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;
import java.util.Currency;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import org.junit.Before;
import org.junit.runners.Parameterized;
import org.junit.runner.RunWith;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.BigDecimalDeserializer;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.BigIntegerDeserializer;

@RunWith(Parameterized.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StdKeyDeserializerTest {

    private StdKeyDeserializer stdKeyDeserializer;
    private Class<?> raw;
    private int kind;
    private Class<?> _keyClass;
    private FromStringDeserializer<?> deser;

    public StdKeyDeserializerTest(Class<?> raw, int kind, Class<?> _keyClass, FromStringDeserializer<?> deser) {
        this.raw = raw;
        this.kind = kind;
        this._keyClass = _keyClass;
        this.deser = deser;
    }

    @Before
    public void setUp() throws Exception {
        stdKeyDeserializer = StdKeyDeserializer.forType(raw);
    }

    @Test
    public void testDeserializeKey_ValidString_ReturnsObject() throws IOException {
        String key = "test";

        Object result = stdKeyDeserializer.deserializeKey(key, new DeserializationContext(Mockito.mock(JsonParser.class), new DeserializerFactory(), null));

        assertNotNull(result);
        assertEquals(_keyClass, result.getClass());
        assertEquals(key, result.toString());
    }

    @Test
    public void testDeserializeKey_NullString_ReturnsNull() throws IOException {
        String key = null;

        Object result = stdKeyDeserializer.deserializeKey(key, new DeserializationContext(Mockito.mock(JsonParser.class), new DeserializerFactory(), null));

        assertNull(result);
    }

    @Test
    public void testDeserializeKey_InvalidString_ThrowsException() throws IOException {
        String key = "invalid";

        try {
            stdKeyDeserializer.deserializeKey(key, new DeserializationContext(Mockito.mock(JsonParser.class), new DeserializerFactory(), null));
            fail("Exception not thrown");
        } catch (IOException ex) {
            assertEquals("not a valid representation", ex.getMessage());
        }
    }

}