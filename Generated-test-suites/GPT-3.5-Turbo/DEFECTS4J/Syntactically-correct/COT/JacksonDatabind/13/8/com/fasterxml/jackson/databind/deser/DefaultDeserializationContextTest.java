
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        ObjectIdGenerator.IdKey idKey = new ObjectIdGenerator.IdKey("test", null);
        ReadableObjectId objId = context.findObjectId("id", idKey, new SimpleObjectIdResolver());
        assertEquals(idKey, objId.getKey());
        assertEquals(new SimpleObjectIdResolver(), objId.getResolver());
    }
    
    @Test
    public void testFindObjectIdWithNullResolver() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        ObjectIdGenerator.IdKey idKey = new ObjectIdGenerator.IdKey("test", null);
        ReadableObjectId objId = context.findObjectId("id", idKey, null);
        assertEquals(idKey, objId.getKey());
        assertNull(objId.getResolver());
    }

    @Test
    public void testFindObjectIdWithExistingEntry() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        ObjectIdGenerator.IdKey idKey = new ObjectIdGenerator.IdKey("test", null);
        ReadableObjectId objId = context.findObjectId("id", idKey, new SimpleObjectIdResolver());
        ReadableObjectId existingEntry = context.findObjectId("id", idKey, new SimpleObjectIdResolver());
        assertEquals(objId, existingEntry);
    }

    @Test
    public void testCheckUnresolvedObjectIdWithNullObjectIds() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        context.checkUnresolvedObjectId();
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectIdWithFailOnUnresolvedObjectIdsEnabled() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        context.disable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        ObjectIdGenerator.IdKey idKey = new ObjectIdGenerator.IdKey("test", null);
        ReadableObjectId objId = context.findObjectId("id", idKey, new SimpleObjectIdResolver());
        Referring referring = new ReadableObjectId.Referring(objId, null, null);
        objId.appendReferring(referring);
        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstanceWithJsonDeserializer() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Annotated annotated = Mockito.mock(Annotated.class);
        JsonDeserializer<Object> deserializer = context.deserializerInstance(annotated, Mockito.mock(JsonDeserializer.class));
        assertNotNull(deserializer);
    }

    @Test
    public void testDeserializerInstanceWithNullDefinition() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Annotated annotated = Mockito.mock(Annotated.class);
        JsonDeserializer<Object> deserializer = context.deserializerInstance(annotated, null);
        assertNull(deserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceWithNonJsonDeserializerDefinition() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Annotated annotated = Mockito.mock(Annotated.class);
        context.deserializerInstance(annotated, new Object());
    }

    @Test
    public void testKeyDeserializerInstanceWithKeyDeserializer() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Annotated annotated = Mockito.mock(Annotated.class);
        KeyDeserializer keyDeserializer = context.keyDeserializerInstance(annotated, Mockito.mock(KeyDeserializer.class));
        assertNotNull(keyDeserializer);
    }

    @Test
    public void testKeyDeserializerInstanceWithNullDefinition() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Annotated annotated = Mockito.mock(Annotated.class);
        KeyDeserializer keyDeserializer = context.keyDeserializerInstance(annotated, null);
        assertNull(keyDeserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstanceWithNonKeyDeserializerDefinition() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        Annotated annotated = Mockito.mock(Annotated.class);
        context.keyDeserializerInstance(annotated, new Object());
    }
}
