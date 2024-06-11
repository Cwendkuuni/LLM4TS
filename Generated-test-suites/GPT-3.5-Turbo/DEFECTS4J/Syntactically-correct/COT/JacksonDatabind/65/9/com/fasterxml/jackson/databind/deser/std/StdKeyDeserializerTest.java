
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws IOException {
        DeserializationContext ctxt = null;
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        String key = "test";
        stdKeyDeserializer.deserializeKey(key, ctxt);
    }

    @Test
    public void test_forType() {
        Class<?> raw = String.class;
        StdKeyDeserializer.forType(raw);
    }

    @Test
    public void test_getKeyClass() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        stdKeyDeserializer.getKeyClass();
    }

    @Test
    public void test_parse() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = null;
        stdKeyDeserializer._parse("test", ctxt);
    }

    @Test
    public void test_parseInt() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        stdKeyDeserializer._parseInt("10");
    }

    @Test
    public void test_parseLong() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        stdKeyDeserializer._parseLong("100");
    }

    @Test
    public void test_parseDouble() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        stdKeyDeserializer._parseDouble("10.5");
    }
}
