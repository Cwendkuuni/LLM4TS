import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        Object id = new Object();
        ObjectIdGenerator generator = new ObjectIdGenerator();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        
        ReadableObjectId objectId = context.findObjectId(id, generator, resolver);
        
        assertNotNull(objectId);
    }

    @Test
    public void testFindObjectId_NullObjectIds() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        context.setObjectIds(null);
        Object id = new Object();
        ObjectIdGenerator generator = new ObjectIdGenerator();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        
        ReadableObjectId objectId = context.findObjectId(id, generator, resolver);
        
        assertNotNull(objectId);
    }

    @Test
    public void testCheckUnresolvedObjectId() throws UnresolvedForwardReference {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        context.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        
        context.checkUnresolvedObjectId();
        
        // If no exception is thrown, the test passes
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId_WithUnresolvedReferences() throws UnresolvedForwardReference {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        context.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        
        ObjectIdGenerator.IdKey idKey = new ObjectIdGenerator.IdKey("key", "type");
        ReadableObjectId objectId = new ReadableObjectId(idKey);
        objectId.setResolver(new SimpleObjectIdResolver());
        objectId.appendReferring(new Referring(null, null, null));
        context.setObjectIds(Collections.singletonMap(idKey, objectId));
        
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance_DeserializerDefIsNull() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        Annotated ann = new Annotated();
        Object deserDef = null;
        
        JsonDeserializer<Object> deserializer = context.deserializerInstance(ann, deserDef);
        
        assertNull(deserializer);
    }

    @Test
    public void testDeserializerInstance_DeserializerDefIsJsonDeserializer() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        Annotated ann = new Annotated();
        Object deserDef = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return null;
            }
        };
        
        JsonDeserializer<Object> deserializer = context.deserializerInstance(ann, deserDef);
        
        assertNotNull(deserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstance_DeserDefIsClassAndNotAssignable() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        Annotated ann = new Annotated();
        Object deserDef = String.class;
        
        context.deserializerInstance(ann, deserDef);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstance_DeserDefIsClassAndNotDeser() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        Annotated ann = new Annotated();
        Object deserDef = Integer.class;
        
        context.deserializerInstance(ann, deserDef);
    }

    @Test
    public void testKeyDeserializerInstance_DeserDefIsNull() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        Annotated ann = new Annotated();
        Object deserDef = null;
        
        KeyDeserializer keyDeserializer = context.keyDeserializerInstance(ann, deserDef);
        
        assertNull(keyDeserializer);
    }

    @Test
    public void testKeyDeserializerInstance_DeserDefIsKeyDeserializer() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        Annotated ann = new Annotated();
        Object deserDef = new KeyDeserializer() {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return null;
            }
        };

        KeyDeserializer keyDeserializer = context.keyDeserializerInstance(ann, deserDef);

        assertNotNull(keyDeserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstance_DeserDefIsClassAndNotAssignable() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        Annotated ann = new Annotated();
        Object deserDef = String.class;
        
        context.keyDeserializerInstance(ann, deserDef);
    }

    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstance_DeserDefIsClassAndNotDeser() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DeserializerFactory());
        Annotated ann = new Annotated();
        Object deserDef = Integer.class;
        
        context.keyDeserializerInstance(ann, deserDef);
    }

}