
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    private StdKeyDeserializer stdKeyDeserializer;

    @Before
    public void setUp() {
        stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
    }

    @Test
    public void deserializeKey_validKey_success() throws IOException {
        String key = "123";
        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        Object result = stdKeyDeserializer.deserializeKey(key, ctxt);

        assertEquals(Integer.valueOf(123), result);
    }

    @Test
    public void deserializeKey_nullKey_success() throws IOException {
        String key = null;
        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        Object result = stdKeyDeserializer.deserializeKey(key, ctxt);

        assertNull(result);
    }

    @Test
    public void deserializeKey_invalidKey_handleError() throws IOException {
        String key = "abc";
        DeserializationContext ctxt = new DeserializationContext(null, null, null);

        Object result = stdKeyDeserializer.deserializeKey(key, ctxt);

        assertEquals("abc", result);
    }

    @Test
    public void deserializeKey_invalidKey_throwException() throws IOException {
        String key = "abc";
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        ctxt.getConfig().disable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);

        try {
            stdKeyDeserializer.deserializeKey(key, ctxt);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("abc", e.getMessage());
        }
    }
}
