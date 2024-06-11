import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId_nullObjectIds() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Object id = new Object();
        ObjectIdGenerator.IdKey key = new ObjectIdGenerator.IdKey(id, null);
        ReadableObjectId result = context.findObjectId(id, null);
        assertNotNull(result);
        assertTrue(context._objectIds.containsKey(key));
        assertEquals(context._objectIds.get(key), result);

    }

    @Test
    public void testFindObjectId_ObjectIdsNotNull() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        int numIds = 3;
        for (int i = 0; i < numIds; i++) {
            Object id = new Object();
            ObjectIdGenerator.IdKey key = new ObjectIdGenerator.IdKey(id, null);
            ReadableObjectId result = context.findObjectId(id, null);
            assertNotNull(result);
            assertTrue(context._objectIds.containsKey(key));
            assertEquals(context._objectIds.get(key), result);
        }
        assertEquals(numIds, context._objectIds.size());
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        int numIds = 3;
        for (int i = 0; i < numIds; i++) {
            Object id = new Object();
            ObjectIdGenerator.IdKey key = new ObjectIdGenerator.IdKey(id, null);
            ReadableObjectId result = context.findObjectId(id, null);
            assertNotNull(result);
            assertTrue(context._objectIds.containsKey(key));
            assertEquals(context._objectIds.get(key), result);
        }

        try {
            context.checkUnresolvedObjectId();
        } catch (Exception e) {
            fail("Unexpected Exception: " + e.getMessage());
        }

        context.disable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        try {
            context.checkUnresolvedObjectId();
        } catch (Exception e) {
            fail("Unexpected Exception: " + e.getMessage());
        }
    }

    @Test
    public void testDeserializerInstance_withJsonDeserializerObject() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        MyJsonDeserializer deser = new MyJsonDeserializer();
        JsonDeserializer<Object> result = context.deserializerInstance(null, deser);
        assertNotNull(result);
        assertEquals(deser, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstance_withClassWhichIsNotJsonDeserializer() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        context.deserializerInstance(null, String.class);
    }

    @Test
    public void testDeserializerInstance_withClass() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Class<?> deserClass = MyJsonDeserializer.class;
        JsonDeserializer<Object> result = context.deserializerInstance(null, deserClass);
        assertNotNull(result);
        assertTrue(result instanceof MyJsonDeserializer);
    }

    @Test
    public void testKeyDeserializerInstance_withKeyDeserializerObject() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        MyKeyDeserializer deser = new MyKeyDeserializer();
        KeyDeserializer result = context.keyDeserializerInstance(null, deser);
        assertNotNull(result);
        assertEquals(deser, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstance_withClassWhichIsNotKeyDeserializer() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        context.keyDeserializerInstance(null, String.class);
    }

    @Test
    public void testKeyDeserializerInstance_withClass() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Class<?> deserClass = MyKeyDeserializer.class;
        KeyDeserializer result = context.keyDeserializerInstance(null, deserClass);
        assertNotNull(result);
        assertTrue(result instanceof MyKeyDeserializer);
    }
}