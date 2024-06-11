import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(1, String.class);
        DeserializationContext ctxt = null;
        stdKeyDeserializer.deserializeKey("test", ctxt);
        stdKeyDeserializer.deserializeKey(null, ctxt);
    }

    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(1, String.class);
        stdKeyDeserializer.getKeyClass();
    }

    @Test
    public void test_parse() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(1, String.class);
        DeserializationContext ctxt = null;
        stdKeyDeserializer._parse("test", ctxt);
    }

    @Test
    public void test_parseInt() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(1, String.class);
        stdKeyDeserializer._parseInt("test");
    }

    @Test
    public void test_parseLong() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(1, String.class);
        stdKeyDeserializer._parseLong("test");
    }

    @Test
    public void test_parseDouble() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(1, String.class);
        stdKeyDeserializer._parseDouble("test");
    }

    @Test
    public void testStringKD() throws IOException, JsonProcessingException {
        StdKeyDeserializer.StringKD stringKD = new StdKeyDeserializer.StringKD(String.class);
        DeserializationContext ctxt = null;
        stringKD.deserializeKey("test", ctxt);
    }

    @Test
    public void testDelegatingKD() throws IOException, JsonProcessingException {
        StdKeyDeserializer.DelegatingKD delegatingKD = new StdKeyDeserializer.DelegatingKD(String.class, new JsonDeserializer());
        DeserializationContext ctxt = null;
        delegatingKD.deserializeKey("test", ctxt);
    }

    @Test
    public void testEnumKD() throws IOException, JsonProcessingException {
        StdKeyDeserializer.EnumKD enumKD = new StdKeyDeserializer.EnumKD(new EnumResolver(), new AnnotatedMethod());
        DeserializationContext ctxt = null;
        enumKD._parse("test", ctxt);
    }

    @Test
    public void testStringCtorKeyDeserializer() throws Exception {
        Constructor<?> ctor = null;
        StdKeyDeserializer.StringCtorKeyDeserializer stringCtorKeyDeserializer = new StdKeyDeserializer.StringCtorKeyDeserializer(ctor);
        stringCtorKeyDeserializer._parse("test", null);
    }

    @Test
    public void testStringFactoryKeyDeserializer() throws Exception {
        Method fm = null;
        StdKeyDeserializer.StringFactoryKeyDeserializer stringFactoryKeyDeserializer = new StdKeyDeserializer.StringFactoryKeyDeserializer(fm);
        stringFactoryKeyDeserializer._parse("test", null);
    }
}