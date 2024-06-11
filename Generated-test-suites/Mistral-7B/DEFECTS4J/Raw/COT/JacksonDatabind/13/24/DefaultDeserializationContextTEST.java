package com.example.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.DeserializerFactory;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class DefaultDeserializationContextTest extends TestBase {

    private DeserializerFactory factory;
    private DeserializationConfig config;
    private JsonParser parser;
    private InjectableValues values;
    private DefaultDeserializationContext context;
    private DefaultDeserializationContext srcContext;

    @Before
    public void setUp() throws Exception {
        sourceFolder("src/test/resources");
        ObjectMapper objectMapper = new ObjectMapper();
        factory = objectMapper.getDeserializerFactory();
        config = objectMapper.getConfig();
        parser = objectMapper.getFactory().createParser(newFile("input.json"));
        values = new InjectableValues();
        srcContext = new DefaultDeserializationContext.Impl(factory);
        context = new DefaultDeserializationContext.Impl(srcContext, config, parser, values);
    }

    @After
    public void tearDown() throws Exception {
        if (parser != null) {
            parser.close();
        }
    }

    @Test
    public void testDefaultDeserializationContextConstructor() {
        assertNotNull(context);
    }

    @Test
    public void testDefaultDeserializationContextCopy() {
        DefaultDeserializationContext copiedContext = context.copy();
        assertNotNull(copiedContext);
        assertNotNull(copiedContext.getDeserializerFactory());
        assertNotNull(copiedContext.getConfig());
        assertNotNull(copiedContext.getParser());
        assertNotNull(copiedContext.getInjectableValues());
        assertNotNull(copiedContext._objectIds);
        assertNotNull(copiedContext._objectIdResolvers);
        assertNull(copiedContext.findObjectId(new Object(), new ObjectIdGenerator<Object>() {}));
    }

    @Test
    public void testFindObjectId() {
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator<Object>() {};
        ObjectIdResolver idResolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return this;
            }
        };
        ReadableObjectId objectId = context.findObjectId(new Object(), idGenerator, idResolver);
        assertNotNull(objectId);
        assertNull(objectId.referringProperties());
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() {
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator<Object>() {};
        ObjectIdResolver idResolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return this;
            }

            @Override
            public ObjectId resolve(DefaultDeserializationContext ctxt, ObjectId id)
                    throws JsonMappingException {
                return null;
            }
        };
        for (int i = 0; i < 10; i++) {
            context.findObjectId(new Object(), idGenerator, idResolver);
        }
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() {
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator<Object>() {};
        Annotated ann = mock(Annotated.class);
        Object deserDef = new Object() {};
        JsonDeserializer<Object> deserializer = context.deserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceWithInvalidDefinitionType() {
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator<Object>() {};
        Annotated ann = mock(Annotated.class);
        Object deserDef = new String() {};
        context.deserializerInstance(ann, deserDef);
    }

    @Test
    public void testDeserializerInstanceWithNullDeserDef() {
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator<Object>() {};
        Annotated ann = mock(Annotated.class);
        JsonDeserializer<Object> deserializer = context.deserializerInstance(ann, null);
        assertNull(deserializer);
    }

    @Test
    public void testKeyDeserializerInstance() {
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator<Object>() {};
        Annotated ann = mock(Annotated.class);
        Object deserDef = new Object() {};
        KeyDeserializer deserializer = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstanceWithInvalidDefinitionType() {
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator<Object>() {};
        Annotated ann = mock(Annotated.class);
        Object deserDef = new String() {};
        context.keyDeserializerInstance(ann, deserDef);
    }

    @Test
    public void testKeyDeserializerInstanceWithNullDeserDef() {
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator<Object>() {};
        Annotated ann = mock(Annotated.class);
        KeyDeserializer deserializer = context.keyDeserializerInstance(ann, null);
        assertNull(deserializer);
    }

    @Test
    public void testWith() {
        DeserializerFactory newFactory = mock(DeserializerFactory.class);
        DefaultDeserializationContext newContext = context.with(newFactory);
        assertNotNull(newContext);
        assertNotNull(newContext.getDeserializerFactory());
        assertNotSame(factory, newFactory);
    }

    @Test
    public void testCreateInstance() {
        DeserializationConfig newConfig = new DeserializationConfig(config);
        JsonParser newParser = factory.createParser(newFile("input.json"));
        InjectableValues newValues = new InjectableValues();
        DefaultDeserializationContext newContext = context.createInstance(newConfig, newParser, newValues);
        assertNotNull(newContext);
        assertNotSame(context, newContext);
    }

    @Test
    public void testCreateInstanceWithNullConfig() {
        JsonParser newParser = factory.createParser(newFile("input.json"));
        InjectableValues newValues = new InjectableValues();
        DefaultDeserializationContext newContext = context.createInstance(null, newParser, newValues);
        assertNotNull(newContext);
        assertNotSame(context, newContext);
    }

    @Test
    public void testCreateInstanceWithNullParser() {
        DeserializationConfig newConfig = new DeserializationConfig(config);
        InjectableValues newValues = new InjectableValues();
        DefaultDeserializationContext newContext = context.createInstance(newConfig, null, newValues);
        assertNotNull(newContext);
        assertNotSame(context, newContext);
    }

    @Test
    public void testCreateInstanceWithNullValues() {
        DeserializationConfig newConfig = new DeserializationConfig(config);
        JsonParser newParser = factory.createParser(newFile("input.json"));
        DefaultDeserializationContext newContext = context.createInstance(newConfig, newParser, null);
        assertNotNull(newContext);
        assertNotSame(context, newContext);
    }

    @Test
    public void testGetObjectIds() {
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator<Object>() {};
        ObjectIdResolver idResolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return this;
            }
        };
        for (int i = 0; i < 10; i++) {
            context.findObjectId(new Object(), idGenerator, idResolver);
        }
        Map<ObjectIdGenerator.IdKey, ReadableObjectId> objectIds = context.getObjectIds();
        assertNotNull(objectIds);
        assertFalse(objectIds.isEmpty());
    }

    @Test
    public void testGetObjectIdResolvers() {
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator<Object>() {};
        ObjectIdResolver idResolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return this;
            }
        };
        List<ObjectIdResolver> objectIdResolvers = new ArrayList<ObjectIdResolver>(context._objectIdResolvers.size());
        for (ObjectIdResolver resolver : context._objectIdResolvers) {
            objectIdResolvers.add(resolver);
        }
        assertNotNull(objectIdResolvers);
        assertFalse(objectIdResolvers.isEmpty());
    }
}