import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;

import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext.Impl(new BasicDeserializerFactory());
        SimpleObjectIdResolver simpleResolver = new SimpleObjectIdResolver();
        ReadableObjectId objectId = deserializationContext.findObjectId("123", null, simpleResolver);
        
        assertNotNull(objectId);
        assertEquals("123", objectId.getKey().key);
        assertEquals(simpleResolver, objectId.getResolver());
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext.Impl(new BasicDeserializerFactory());
        LinkedHashMap<ReadableObjectId.IdKey, ReadableObjectId> objectIds = new LinkedHashMap<>();
        ReadableObjectId objectId = new ReadableObjectId(new ReadableObjectId.IdKey("test"));
        objectIds.put(objectId.getKey(), objectId);
        deserializationContext.setObjectIds(objectIds);
        
        try {
            deserializationContext.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            fail("Should not throw UnresolvedForwardReference");
        }
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext.Impl(new BasicDeserializerFactory());
        Annotated annotated = null;
        Object deserDef = new BasicDeserializerFactory();
        JsonDeserializer<Object> deserializer = deserializationContext.deserializerInstance(annotated, deserDef);
        
        assertNotNull(deserializer);
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext.Impl(new BasicDeserializerFactory());
        Annotated annotated = null;
        Object deserDef = new BasicDeserializerFactory();
        KeyDeserializer keyDeserializer = deserializationContext.keyDeserializerInstance(annotated, deserDef);
        
        assertNotNull(keyDeserializer);
    }

    @Test
    public void testWith() {
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext.Impl(new BasicDeserializerFactory());
        DeserializerFactory factory = new BasicDeserializerFactory();
        DefaultDeserializationContext newContext = (DefaultDeserializationContext) deserializationContext.with(factory);
        
        assertSame(factory, newContext.getFactory());
    }

    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext deserializationContext = new DefaultDeserializationContext.Impl(new BasicDeserializerFactory());
        DeserializationConfig config = new DeserializationConfig(null);
        JsonParser jp = new JsonFactory().createParser("{}");
        InjectableValues values = new InjectableValues.Std();
        DefaultDeserializationContext newContext = (DefaultDeserializationContext) deserializationContext.createInstance(config, jp, values);
        
        assertNotNull(newContext);
    }

}