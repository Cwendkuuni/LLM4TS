import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext deserializationContext;

    @Before
    public void setUp() {
        deserializationContext = new DefaultDeserializationContext.Impl(new DeserializerFactory(), new DeserializerCache());
    }

    @Test
    public void testFindObjectId() {
        Object id = new Object();
        ObjectIdGenerator generator = new ObjectIdGenerator();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId objectId = deserializationContext.findObjectId(id, generator, resolver);

        // Check if the object id is successfully retrieved
        Assert.assertEquals(id, objectId.getId());

        // Check if the resolver is set correctly
        Assert.assertEquals(resolver, objectId.getResolver());
    }

    @Test
    public void testFindObjectIdWithNullResolver() {
        Object id = new Object();
        ObjectIdGenerator generator = new ObjectIdGenerator();
        ReadableObjectId objectId = deserializationContext.findObjectId(id, generator, null);

        // Check if the object id is successfully retrieved
        Assert.assertEquals(id, objectId.getId());

        // Check if a new resolver is created
        Assert.assertNotNull(objectId.getResolver());
    }

    @Test
    public void testCopy() {
        try {
            deserializationContext.copy();
            Assert.fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected IllegalStateException
        }
    }

    @Test
    public void testDeserializerInstance() {
        Annotated annotated = new Annotated();
        Object deserDef = new Object();
        try {
            deserializationContext.deserializerInstance(annotated, deserDef);
            Assert.fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected IllegalStateException
        }
    }

    @Test
    public void testKeyDeserializerInstance() {
        Annotated annotated = new Annotated();
        Object deserDef = new Object();
        try {
            deserializationContext.keyDeserializerInstance(annotated, deserDef);
            Assert.fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected IllegalStateException
        }
    }

    @Test
    public void testWith() {
        DeserializerFactory factory = new DeserializerFactory();
        DefaultDeserializationContext newContext = deserializationContext.with(factory);

        // Check if the new context has the correct factory
        Assert.assertEquals(factory, newContext.getFactory());
    }

    @Test
    public void testCreateInstance() {
        DeserializationConfig config = new DeserializationConfig();
        JsonParser jp = new JsonParser();
        InjectableValues values = new InjectableValues();

        DefaultDeserializationContext newContext = deserializationContext.createInstance(config, jp, values);

        // Check if the new context has the correct configuration, parser and values
        Assert.assertEquals(config, newContext.getConfig());
        Assert.assertEquals(jp, newContext.getParser());
        Assert.assertEquals(values, newContext.getInjectableValues());
    }
}