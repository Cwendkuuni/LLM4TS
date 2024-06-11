
import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.NumberInput;
import org.junit.Ignore;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

public class StdKeyDeserializerTest {
    
    @Test
    public void testDeserializeKey() throws IOException {
        StdKeyDeserializer deserializer = new StdKeyDeserializer();
        DeserializationContext ctxt = new DeserializationContext();
        String key = "test";
        
        Object result = deserializer.deserializeKey(key, ctxt);
        
        assertEquals("test", result);
    }
    
    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer();
        
        Class<?> result = deserializer.getKeyClass();
        
        assertEquals(Object.class, result);
    }
    
    @Test
    public void testParse() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer();
        DeserializationContext ctxt = new DeserializationContext();
        String key = "test";
        
        Object result = deserializer._parse(key, ctxt);
        
        assertEquals("test", result);
    }
    
    @Test
    public void testParseInt() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer();
        int value = 10;
        
        int result = deserializer._parseInt(Integer.toString(value));
        
        assertEquals(value, result);
    }
    
    @Test
    public void testParseLong() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer();
        long value = 10L;
        
        long result = deserializer._parseLong(Long.toString(value));
        
        assertEquals(value, result);
    }
    
    @Test
    public void testParseDouble() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer();
        double value = 10.5;
        
        double result = deserializer._parseDouble(Double.toString(value));
        
        assertEquals(value, result, 0.001);
    }
    
    @Test
    public void testStringCtorKeyDeserializer() throws Exception {
        StringCtorKeyDeserializer deserializer = new StringCtorKeyDeserializer(null);
        Constructor<?> ctor = String.class.getConstructor(String.class);
        deserializer._ctor = ctor;
        String key = "test";
        
        Object result = deserializer._parse(key, null);
        
        assertEquals("test", result);
    }
    
    @Test
    public void testStringFactoryKeyDeserializer() throws Exception {
        StringFactoryKeyDeserializer deserializer = new StringFactoryKeyDeserializer(null);
        Method factoryMethod = String.class.getMethod("valueOf", String.class);
        deserializer._factoryMethod = factoryMethod;
        String key = "test";
        
        Object result = deserializer._parse(key, null);
        
        assertEquals("test", result);
    }
    
}
