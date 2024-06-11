import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class DefaultDeserializationContextTest {
    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        context.findObjectId("", null, null);
    }
    
    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        context.checkUnresolvedObjectId();
    }
    
    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        JsonDeserializer<?> deser = context.deserializerInstance(null, null);
        assertNull(deser);
        
        deser = context.deserializerInstance(null, String.class);
        assertTrue(deser instanceof JsonDeserializer);
    }
    
    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        KeyDeserializer deser = context.keyDeserializerInstance(null, null);
        assertNull(deser);

        deser = context.keyDeserializerInstance(null, String.class);
        assertTrue(deser instanceof KeyDeserializer);
    }
    
    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        DefaultDeserializationContext result = context.createInstance(null, null, null);
        assertNotNull(result);
    }
    
    @Test
    public void testWith() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        DefaultDeserializationContext result = context.with(null);
        assertNotNull(result);
    }
    
    @Test
    public void testCopy() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        DefaultDeserializationContext result = context.copy();
        assertNotNull(result);
    }
}