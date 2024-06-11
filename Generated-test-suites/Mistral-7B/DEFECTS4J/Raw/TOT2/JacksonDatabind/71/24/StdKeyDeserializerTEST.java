import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

    @Test
    public void testBooleanDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
        Object result = deserializer.deserializeKey("true", new TestContext());
        assertEquals(Boolean.TRUE, result);

        result = deserializer.deserializeKey("false", new TestContext());
        assertEquals(Boolean.FALSE, result);

        result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }

    @Test
    public void testByteDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Byte.class);

        Object result = deserializer.deserializeKey("0", new TestContext());
        assertEquals((Byte) 0, result);

        result = deserializer.deserializeKey("50", new TestContext());
        assertEquals((Byte) 50, result);

        result = deserializer.deserializeKey("127", new TestContext());
        assertEquals((Byte) 127, result);

        result = deserializer.deserializeKey("128", new TestContext());
        assertNull(result);
    }

    // Add test cases for other data types like Short, Char, Int, Long, Float, Double, Locale, Date, Calendar, UUID, URI, and URL

    @Test
    public void testEnumDeserializer() throws IOException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);

        Object result = deserializer.deserializeKey("en_US", new TestContext());
        assertEquals(Locale.US, result);

        result = deserializer.deserializeKey("invalidLocale", new TestContext());
        assertNull(result);
    }

    @Test
    public void testNullDeserializer() {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);

        Object result = deserializer.deserializeKey(null, new TestContext());
        assertNull(result);
    }

    @Test
    public void testInvalidDeserializer() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Double.class);

        Object result = deserializer.deserializeKey("invalid", new TestContext());
        assertNull(result);
    }
}

class TestContext extends DeserializationContext {
    // Implement methods required by DeserializationContext interface
}