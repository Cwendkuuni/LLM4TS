import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext context;
    
    @Before
    public void setUp() {
        context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
    }
    
    @Test
    public void testFindObjectId_WithNullId_ReturnsNull() {
        assertNull(context.findObjectId(null, new ObjectIdGenerator(), new SimpleObjectIdResolver()));
    }
    
    @Test
    public void testFindObjectId_WithNonNullId_ReturnsReadableObjectId() {
        ObjectIdGenerator.IdKey idKey = new ObjectIdGenerator.IdKey("key");
        ReadableObjectId objectId = context.findObjectId("id", new ObjectIdGenerator(), new SimpleObjectIdResolver());
        assertNotNull(objectId);
        assertEquals(idKey, objectId.getKey());
    }
    
    @Test
    public void testCheckUnresolvedObjectId_WithNoUnresolvedReferences_DoesNotThrowException() {
        context.checkUnresolvedObjectId();
    }
    
    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId_WithUnresolvedReferences_ThrowsException() {
        ReadableObjectId objectId = new ReadableObjectId(new ObjectIdGenerator.IdKey("key"));
        objectId.appendReferring(new Referring(), "beanType", "location");
        context.get_objectIds().put(objectId.getKey(), objectId);
        context.checkUnresolvedObjectId();
    }
    
    @Test
    public void testDeserializerInstance_WithNullDeserDef_ReturnsNull() {
        assertNull(context.deserializerInstance(null, null));
    }
    
    @Test
    public void testDeserializerInstance_WithDeserDefAsJsonDeserializer_ReturnsJsonDeserializer() {
        JsonDeserializer<Object> deserializer = new JsonDeserializer<>();
        assertSame(deserializer, context.deserializerInstance(null, deserializer));
    }
    
    @Test
    public void testDeserializerInstance_WithDeserDefAsClass_ReturnsJsonDeserializer() {
        Class<?> deserializerClass = JsonDeserializer.class;
        JsonDeserializer<Object> deserializer = (JsonDeserializer<Object>) context.deserializerInstance(null, deserializerClass);
        assertNotNull(deserializer);
        assertEquals(deserializerClass, deserializer.getClass());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstance_WithInvalidDeserDef_ThrowsIllegalStateException() {
        context.deserializerInstance(null, new Object());
    }
    
    @Test
    public void testKeyDeserializerInstance_WithNullDeserDef_ReturnsNull() {
        assertNull(context.keyDeserializerInstance(null, null));
    }
    
    @Test
    public void testKeyDeserializerInstance_WithDeserDefAsKeyDeserializer_ReturnsKeyDeserializer() {
        KeyDeserializer deserializer = new KeyDeserializer();
        assertSame(deserializer, context.keyDeserializerInstance(null, deserializer));
    }
    
    @Test
    public void testKeyDeserializerInstance_WithDeserDefAsClass_ReturnsKeyDeserializer() {
        Class<?> deserializerClass = KeyDeserializer.class;
        KeyDeserializer deserializer = context.keyDeserializerInstance(null, deserializerClass);
        assertNotNull(deserializer);
        assertEquals(deserializerClass, deserializer.getClass());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstance_WithInvalidDeserDef_ThrowsIllegalStateException() {
        context.keyDeserializerInstance(null, new Object());
    }
    
    @Test
    public void testWith_ReturnsNewDeserializationContextWithGivenDeserializerFactory() {
        DeserializerFactory factory = new DeserializerFactory();
        DefaultDeserializationContext newContext = context.with(factory);
        assertNotSame(context, newContext);
        assertSame(factory, newContext.getFactory());
    }
    
    @Test
    public void testCreateInstance_ReturnsNewDeserializationContextWithGivenConfigParserAndInjectableValues() {
        DeserializationConfig config = new DeserializationConfig();
        JsonParser jp = new JsonParser();
        InjectableValues values = new InjectableValues();
        DefaultDeserializationContext newContext = context.createInstance(config, jp, values);
        assertNotSame(context, newContext);
        assertSame(config, newContext.getConfig());
        assertSame(jp, newContext.getParser());
        assertSame(values, newContext.getInjectableValues());
    }
}