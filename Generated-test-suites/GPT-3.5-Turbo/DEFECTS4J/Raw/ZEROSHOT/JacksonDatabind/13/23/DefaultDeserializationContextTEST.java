import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DeserializerFactory;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.deser.impl.DefaultDeserializationContext.Impl;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext deserializationContext;
    private DeserializationConfig config;
    private JsonParser jp;
    private InjectableValues values;

    @Before
    public void setup() {
        DeserializerFactory deserializerFactory = new DeserializerFactory();
        deserializationContext = new Impl(deserializerFactory);
        config = new DeserializationConfig();
        jp = new JsonParser();
        values = new InjectableValues() {
            @Override
            public Object findInjectableValue(Object valueId, DeserializationContext ctxt, BeanProperty forProperty,
                    Object beanInstance) {
                return null;
            }
        };
    }

    @Test
    public void testFindObjectId() {
        Object id = new Object();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<>();
        ObjectIdResolver resolverType = new SimpleJsonObjectResolver();

        ReadableObjectId result = deserializationContext.findObjectId(id, gen, resolverType);

        assertNotNull(result);
        assertEquals(id, result.getKey().key);
        assertNotNull(result.getResolver());
        assertTrue(deserializationContext._objectIds.containsKey(gen.key(id)));
        assertEquals(result, deserializationContext._objectIds.get(gen.key(id)));
        assertTrue(deserializationContext._objectIdResolvers.get(0).canUseFor(resolverType));
    }

    @Test
    public void testFindObjectIdWithExistingId() {
        Object id = new Object();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<>();
        ObjectIdResolver resolverType = new SimpleJsonObjectResolver();

        ReadableObjectId existingObjectId = new ReadableObjectId(gen.key(id));
        deserializationContext._objectIds = new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>();
        deserializationContext._objectIds.put(existingObjectId.getKey(), existingObjectId);

        ReadableObjectId result = deserializationContext.findObjectId(id, gen, resolverType);

        assertNotNull(result);
        assertEquals(existingObjectId, result);
        assertTrue(deserializationContext._objectIds.containsKey(gen.key(id)));
        assertEquals(existingObjectId, deserializationContext._objectIds.get(gen.key(id)));
    }

    @Test
    public void testFindObjectIdWithNullId() {
        Object id = null;
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<>();
        ObjectIdResolver resolverType = new SimpleJsonObjectResolver();

        ReadableObjectId result = deserializationContext.findObjectId(id, gen, resolverType);

        assertNotNull(result);
        assertEquals(id, result.getKey().key);
        assertNotNull(result.getResolver());
        assertTrue(deserializationContext._objectIds.containsKey(gen.key(id)));
        assertEquals(result, deserializationContext._objectIds.get(gen.key(id)));
        assertTrue(deserializationContext._objectIdResolvers.get(0).canUseFor(resolverType));
    }

    @Test
    public void testCheckUnresolvedObjectId() throws UnresolvedForwardReference {
        deserializationContext._objectIds = new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>();
        ReadableObjectId objectId = new ReadableObjectId(new ObjectIdGenerator.IdKey("key", Object.class, null, null));
        deserializationContext._objectIds.put(objectId.getKey(), objectId);

        deserializationContext.checkUnresolvedObjectId();
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectIdWithFailOnUnresolvedObjectIds() throws UnresolvedForwardReference {
        config.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        deserializationContext._objectIds = new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>();
        ReadableObjectId objectId = new ReadableObjectId(new ObjectIdGenerator.IdKey("key", Object.class, null, null));
        deserializationContext._objectIds.put(objectId.getKey(), objectId);

        deserializationContext.checkUnresolvedObjectId();
    }

    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext result = deserializationContext.createInstance(config, jp, values);

        assertNotNull(result);
        assertNotEquals(deserializationContext, result);
        assertEquals(config, result._config);
        assertEquals(jp, result._parser);
        assertEquals(values, result._injectableValues);
    }

    
    @Test
    public void testWith() {
        DeserializerFactory factory = new DeserializerFactory();

        DefaultDeserializationContext result = deserializationContext.with(factory);

        assertNotNull(result);
        assertEquals(factory, result._factory);
        assertNotEquals(deserializationContext, result);
    }

    @Test
    public void testDeserializerInstanceWithJsonDeserializer() throws JsonMappingException {
        Annotated ann = new Annotated() {
            
        };
        JsonDeserializer<?> deserDef = new JsonDeserializer<Object>() {
            
        };
        
        JsonDeserializer<Object> result = deserializationContext.deserializerInstance(ann, deserDef);
        
        assertNotNull(result);
        assertEquals(deserDef, result);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceWithDeserDefNull() throws JsonMappingException {
        Annotated ann = new Annotated() {
            
        };
        Object deserDef = null;
        
        deserializationContext.deserializerInstance(ann, deserDef);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceWithInvalidDeserDefType() throws JsonMappingException {
        Annotated ann = new Annotated() {
            
        };
        Object deserDef = new Object();
        
        deserializationContext.deserializerInstance(ann, deserDef);
    }
    
    @Test
    public void testDeserializerInstanceWithDeserClass() throws JsonMappingException {
        Annotated ann = new Annotated() {
            
        };
        Class<?> deserDef = new JsonDeserializer<Object>() {}.getClass();
        
        JsonDeserializer<Object> result = deserializationContext.deserializerInstance(ann, deserDef);
        
        assertNotNull(result);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceWithInvalidDeserClassType() throws JsonMappingException {
        Annotated ann = new Annotated() {
            
        };
        Class<?> deserDef = new Object().getClass();
        
        deserializationContext.deserializerInstance(ann, deserDef);
    }

    @Test
    public void testKeyDeserializerInstanceWithValidDeserClass() throws JsonMappingException {
        Annotated ann = new Annotated() {

        };
        Class<?> deserDef = new MockKeyDeserializer().getClass();

        KeyDeserializer result = deserializationContext.keyDeserializerInstance(ann, deserDef);

        assertNotNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstanceWithInvalidDeserClassType() throws JsonMappingException {
        Annotated ann = new Annotated() {

        };
        Class<?> deserDef = new Object().getClass();

        deserializationContext.keyDeserializerInstance(ann, deserDef);
    }

    static class SimpleJsonObjectResolver extends SimpleObjectIdResolver {

        @Override
        public boolean canUseFor(ObjectIdResolver resolverType) {
            return super.canUseFor(this);
        }
    }

    static class MockKeyDeserializer extends KeyDeserializer {

        @Override
        public Object deserializeKey(String key, DeserializationContext ctxt) throws IllegalArgumentException {
            return null;
        }
    }
}