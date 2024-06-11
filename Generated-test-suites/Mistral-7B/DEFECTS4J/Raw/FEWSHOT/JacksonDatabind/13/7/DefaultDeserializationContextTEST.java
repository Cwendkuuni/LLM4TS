package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

    @Test
    public void testCreateInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext ctxt = mapper.getDeserializationContext().createInstance(mapper.getConfig(), null, null);
        assertNotNull(ctxt);
    }

    @Test
    public void testFindObjectId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext ctxt = mapper.getDeserializationContext();

        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("testKey");
            }
        };

        ReadableObjectId objId = ctxt.findObjectId(new Object(), idGen, SimpleObjectIdResolver.instance());
        assertNotNull(objId);
        assertEquals("testKey", objId.getKey().key);
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt._objectIds = new LinkedHashMap<>();
        ctxt._objectIdResolvers = new ArrayList<>();

        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("testKey");
            }
        };

        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("testKey2");
            }

            @Override
            public ObjectId resolveId(DeserializationContext ctxt, ObjectId id) throws JsonMappingException {
                throw new RuntimeException();
            }
        };

        ctxt._objectIds.put(new ObjectIdGenerator.IdKey("testKey"), new ReadableObjectId(null));
        ctxt._objectIdResolvers.add(resolver);

        ctxt.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext ctxt = mapper.getDeserializationContext();

        JsonDeserializer deser = new JsonDeserializer() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        Annotated ann = mock(Annotated.class);
        Object deserDef = deser;
        JsonDeserializer result = ctxt.deserializerInstance(ann, deserDef);
        assertEquals(deser.getClass(), result.getClass());

        deserDef = JsonDeserializer.class;
        result = ctxt.deserializerInstance(ann, deserDef);
        assertTrue(result instanceof JsonDeserializer);

        deserDef = null;
        result = ctxt.deserializerInstance(ann, deserDef);
        assertNull(result);

        deserDef = String.class;
        result = ctxt.deserializerInstance(ann, deserDef);
        assertNull(result);

        deserDef = Integer.class;
        result = ctxt.deserializerInstance(ann, deserDef);
        assertNull(result);

        deserDef = JsonDeserializer.None.class;
        result = ctxt.deserializerInstance(ann, deserDef);
        assertNull(result);
    }

    @Test
    public void testKeyDeserializerInstance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext ctxt = mapper.getDeserializationContext();

        KeyDeserializer deser = new KeyDeserializer() {
            @Override
            public Object keyDeserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        Annotated ann = mock(Annotated.class);
        Object deserDef = deser;
        KeyDeserializer result = ctxt.keyDeserializerInstance(ann, deserDef);
        assertEquals(deser.getClass(), result.getClass());

        deserDef = KeyDeserializer.None.class;
        result = ctxt.keyDeserializerInstance(ann, deserDef);
        assertNull(result);
    }
}