
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

public class StdKeyDeserializerTest {

    // Test data
    private StdKeyDeserializer keyDeserializer;

    @Test
    public void testDeserializeKey() throws IOException {
        String key = "true";
        DeserializationContext ctxt = null;
        Object result = keyDeserializer.deserializeKey(key, ctxt);
        assert result.equals(Boolean.TRUE);

        key = "false";
        result = keyDeserializer.deserializeKey(key, ctxt);
        assert result.equals(Boolean.FALSE);

        key = "test key";
        result = keyDeserializer.deserializeKey(key, ctxt);
        assert result.equals(key);
    }

    @Test
    public void testGetKeyClass() {
        Class<?> cls = keyDeserializer.getKeyClass();
        assert cls.equals(keyDeserializer._keyClass);
    }

    @Test
    public void testGetIntValue() {
        String key = "10";
        int value = keyDeserializer._parseInt(key);
        assert value == 10;
    }

    @Test
    public void testGetLongValue() {
        String key = "10000000000";
        long value = keyDeserializer._parseLong(key);
        assert value == 10000000000L;
    }

    @Test
    public void testGetDoubleValue() {
        String key = "1.234";
        double value = keyDeserializer._parseDouble(key);
        assert value == 1.234;
    }
}

