import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Object id = new Object();
        ObjectIdGenerator.IdKey key = new ObjectIdGenerator.IdKey(id, null);
        ReadableObjectId objectId = context.findObjectId(id, null, null);
        
        assertEquals(objectId, context.findObjectId(id, null, null));
        assertEquals(objectId, context.findObjectId(id, null, null));
        assertNotNull(context.findObjectId(new Object(), null, null));
        assertNotNull(context.findObjectId(new Object(), null, null));
    }
    
    @Test
    public void testCheckUnresolvedObjectId() throws UnresolvedForwardReference {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);

        context.checkUnresolvedObjectId();
        context.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, true);
        UnresolvedForwardReference exception = null;
        try {
            context.checkUnresolvedObjectId();
        } catch(UnresolvedForwardReference e) {
            exception = e;
        }
        assertNotNull(exception);
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        assertEquals(null, context.deserializerInstance(null, null));
        JsonDeserializer<Object> deserializer = context.deserializerInstance(null, new Class<Object>());
        assertNotNull(deserializer);
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        assertEquals(null, context.keyDeserializerInstance(null, null));
        KeyDeserializer keyDeserializer = context.keyDeserializerInstance(null, new Class<KeyDeserializer>());
        assertNotNull(keyDeserializer);
    }

    @Test
    public void testWith() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        DeserializerFactory factory = new DeserializerFactory();
        DefaultDeserializationContext newContext = context.with(factory);

        assertEquals(factory, newContext._factory);
        assertEquals(context._cache, newContext._cache);
        assertEquals(context._config, newContext._config);
        assertEquals(context._injectableValues, newContext._injectableValues);
    }

    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        DeserializationConfig config = new DeserializationConfig(null);
        JsonParser jp = new JsonParser(new JsonFactory());
        InjectableValues values = new InjectableValues.Std();
        DefaultDeserializationContext newContext = context.createInstance(config, jp, values);

        assertEquals(context._factory, newContext._factory);
        assertEquals(context._cache, newContext._cache);
        assertEquals(config, newContext._config);
        assertEquals(jp, newContext._parser);
        assertEquals(values, newContext._injectableValues);
    }

    @Test
    public void testCopy() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        DefaultDeserializationContext newContext = context.copy();

        assertEquals(context._factory, newContext._factory);
        assertEquals(context._cache, newContext._cache);
        assertEquals(context._config, newContext._config);
        assertEquals(context._parser, newContext._parser);
        assertEquals(context._injectableValues, newContext._injectableValues);
    }

}