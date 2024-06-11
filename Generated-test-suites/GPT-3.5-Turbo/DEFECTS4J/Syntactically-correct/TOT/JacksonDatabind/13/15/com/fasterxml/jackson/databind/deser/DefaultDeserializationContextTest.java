import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        // Test when _objectIds is null
        assertNull(context.findObjectId("1", new ObjectIdGenerator(), new SimpleObjectIdResolver()));

        // Test when _objectIds is not null
        context._objectIds = new LinkedHashMap<>();
        ObjectIdGenerator.IdKey key = new ObjectIdGenerator.IdKey("1", null);
        ReadableObjectId readableObjectId = new ReadableObjectId(key);
        context._objectIds.put(key, readableObjectId);
        
        assertSame(readableObjectId, context.findObjectId("1", new ObjectIdGenerator(), new SimpleObjectIdResolver()));
        
        // Test when ObjectIdResolver is null
        assertNull(context.findObjectId("1", new ObjectIdGenerator(), null));
    }

    @Test
    public void testFindObjectIdDeprecated() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        assertNull(context.findObjectId("1", new ObjectIdGenerator()));
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        // Test when _objectIds is null
        assertNull(context.checkUnresolvedObjectId());

        // Test when _objectIds is not null and isEnabled is true
        context._objectIds = new LinkedHashMap<>();
        ObjectIdGenerator.IdKey key = new ObjectIdGenerator.IdKey("1", null);
        ReadableObjectId readableObjectId = new ReadableObjectId(key);
        context._objectIds.put(key, readableObjectId);
        assertThrows(UnresolvedForwardReference.class, () -> {
            context.checkUnresolvedObjectId();
        });

        // Test when _objectIds is not null and isEnabled is false
        context.disable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        assertNull(context.checkUnresolvedObjectId());
    }

    @Test
    public void testDeserializerInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        // Test when deserDef is null
        assertNull(context.deserializerInstance(null, null));

        // Test when deserDef is a JsonDeserializer
        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) {
                return null;
            }
        };
        assertSame(deserializer, context.deserializerInstance(null, deserializer));

        // Test when deserDef is a Class
        assertNull(context.deserializerInstance(null, Double.class));
    }

    @Test
    public void testKeyDeserializerInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        // Test when deserDef is null
        assertNull(context.keyDeserializerInstance(null, null));

        // Test when deserDef is a KeyDeserializer
        KeyDeserializer keyDeserializer = new KeyDeserializer() {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) {
                return null;
            }
        };
        assertSame(keyDeserializer, context.keyDeserializerInstance(null, keyDeserializer));

        // Test when deserDef is a Class
        assertNull(context.keyDeserializerInstance(null, String.class));
    }

    @Test
    public void testWith() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        // Test when factory is different from the original factory
        DefaultDeserializationContext newContext = (DefaultDeserializationContext.Impl) context.with(new DeserializerFactory());
        assertNotSame(context, newContext);
        assertSame(newContext.getFactory().getClass(), DeserializerFactory.class);
        
        // Test when factory is the same as the original factory
        DefaultDeserializationContext sameContext = (DefaultDeserializationContext.Impl) context.with(context.getFactory());
        assertSame(context, sameContext);
    }

    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        // Test createInstance method with different parameters
        assertNotNull(context.createInstance(new DeserializationConfig(), null, null));
    }
}