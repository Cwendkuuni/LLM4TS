import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultDeserializationContextTest {
    
    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        Object id = new Object();
        ObjectIdGenerator gen = new ObjectIdGenerator();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        
        ReadableObjectId result = context.findObjectId(id, gen, resolver);
        
        assertNotNull(result);
    }
    
    @Test
    public void testFindObjectIdWithNullObjectIds() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        Object id = null;
        ObjectIdGenerator gen = new ObjectIdGenerator();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        
        ReadableObjectId result = context.findObjectId(id, gen, resolver);
        
        assertNotNull(result);
    }
    
    @Test
    public void testCheckUnresolvedObjectIdWithUnresolvedRefs() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        context.findObjectId(new Object(), new ObjectIdGenerator(), new SimpleObjectIdResolver());
        
        try {
            context.checkUnresolvedObjectId();
            fail("Exception expected");
        } catch (UnresolvedForwardReference e) {
            // Expected
        }
    }
    
    @Test
    public void testDeserializerInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        
        try {
            context.deserializerInstance(null, "InvalidDeserializer");
            fail("Exception expected");
        } catch (JsonMappingException e) {
            // Expected
        }
    }
    
    @Test
    public void testKeyDeserializerInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        
        try {
            context.keyDeserializerInstance(null, "InvalidKeyDeserializer");
            fail("Exception expected");
        } catch (JsonMappingException e) {
            // Expected
        }
    }
    
    @Test
    public void testWith() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        DeserializerFactory factory = new DeserializerFactory();
        
        DefaultDeserializationContext newContext = (DefaultDeserializationContext.Impl) context.with(factory);
        
        assertNotSame(context, newContext);
        assertEquals(factory, newContext._factory);
    }
    
    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        DeserializationConfig config = new DeserializationConfig();
        JsonParser jp = new JsonParser();
        InjectableValues values = new InjectableValues();
        
        DefaultDeserializationContext newContext = (DefaultDeserializationContext.Impl) context.createInstance(config, jp, values);
        
        assertNotNull(newContext);
    }

}