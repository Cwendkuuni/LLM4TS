import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class StringCollectionDeserializerTest {
    
    private StringCollectionDeserializer deserializer;
    private JsonDeserializer<String> valueDeserializer;
    private ValueInstantiator valueInstantiator;
    
    @Before
    public void setUp() {
        deserializer = new StringCollectionDeserializer(null, valueDeserializer, valueInstantiator);
    }
    
    @Test
    public void testDeserialize() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        
        Collection<String> result = deserializer.deserialize(parser, context);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testDeserializeWithResult() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        Collection<String> result = new ArrayList<String>();
        
        Collection<String> deserialized = deserializer.deserialize(parser, context, result);
        
        assertNotNull(deserialized);
        assertSame(result, deserialized);
    }
    
    @Test
    public void testDeserializeUsingCustom() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        Collection<String> result = new ArrayList<String>();
        JsonDeserializer<String> deser = null;
        
        Collection<String> deserialized = deserializer.deserializeUsingCustom(parser, context, result, deser);
        
        assertNotNull(deserialized);
        assertSame(result, deserialized);
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        TypeDeserializer typeDeserializer = null;
        
        Object deserialized = deserializer.deserializeWithType(parser, context, typeDeserializer);
        
        assertNull(deserialized);
    }
}