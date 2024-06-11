import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializerFactory;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DefaultDeserializationContextTest {

    public void testFindObjectId() {
        DefaultDeserializationContext dc = new DefaultDeserializationContext.Impl(null);
        Object id = null;
        ObjectMapper objectMapper = null;
        objectIdGenerator generator = null;

        ReadableObjectId result = dc.findObjectId(id, generator);
        assertNotNull(result);

    }

    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext dc = new DefaultDeserializationContext.Impl(null);

        try {
            dc.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            fail();
        }

        List<ObjectIdResolver> objectIdResolvers = new ArrayList<>();

        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        objectIdResolvers.add(resolver);
        dc._objectIdResolvers = objectIdResolvers;

        LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId> objectIds = new LinkedHashMap<>();
        dc._objectIds = objectIds;

        ReadableObjectId objectId = new ReadableObjectId(null);
        objectId.setResolver(resolver);
        objectIds.put(null, objectId);

        try {
            dc.checkUnresolvedObjectId();
            fail();
        } catch (UnresolvedForwardReference e) {
            assertNotNull(e.getMessage());
        }

        objectId.setResolver(null);
        try {
            dc.checkUnresolvedObjectId();
            fail();
        } catch (UnresolvedForwardReference e) {
            assertNotNull(e.getMessage());
        }
    }

    public void testDeserializerInstance() {
        DefaultDeserializationContext dc = new DefaultDeserializationContext.Impl(null);
        Annotated ann = null;

        Object deserDef = null;

        try {
            dc.deserializerInstance(ann, deserDef);
            fail();
        } catch (IllegalStateException e) {
            assertNotNull(e.getMessage());
        }

        Class<JsonDeserializer> deserDefClass = JsonDeserializer.class;
        deserDef = deserDefClass;

        try {
            dc.deserializerInstance(ann, deserDef);
            fail();
        } catch (IllegalStateException e) {
            assertNotNull(e.getMessage());
        }

        Class<JsonDeserializer> jsonDeserializerClass = null;
        deserDef = jsonDeserializerClass;

        try {
            dc.deserializerInstance(ann, deserDef);
            fail();
        } catch (IllegalStateException e) {
            assertNotNull(e.getMessage());
        }

    }

    public void testKeyDeserializerInstance() {
        DefaultDeserializationContext dc = new DefaultDeserializationContext.Impl(null);

        Annotated ann = null;
        Object deserDef = null;

        try {
            dc.keyDeserializerInstance(ann, deserDef);
            fail();
        } catch (IllegalStateException e) {
            assertNotNull(e.getMessage());
        }

        Class<KeyDeserializer> deserDefClass = KeyDeserializer.class;
        deserDef = deserDefClass;

        try {
            dc.keyDeserializerInstance(ann, deserDef);
            fail();
        } catch (IllegalStateException e) {
            assertNotNull(e.getMessage());
        }

        Class<KeyDeserializer> jsonDeserializerClass = null;
        deserDef = jsonDeserializerClass;

        try {
            dc.keyDeserializerInstance(ann, deserDef);
            fail();
        } catch (IllegalStateException e) {
            assertNotNull(e.getMessage());
        }
    }
}