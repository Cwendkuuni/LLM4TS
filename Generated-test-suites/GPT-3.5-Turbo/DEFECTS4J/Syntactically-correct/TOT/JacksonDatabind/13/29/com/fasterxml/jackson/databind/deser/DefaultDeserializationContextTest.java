import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParseException;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            DefaultDeserializationContext context = new DefaultDeserializationContext(mapper.getDeserializerProvider(), mapper.getDeserializerProvider().getCache());
            Object id = "123";
            ObjectIdGenerator<?> generator = new ObjectIdGenerator<>();
            ObjectIdResolver resolver = new SimpleObjectIdResolver();
            ReadableObjectId objectId = context.findObjectId(id, generator, resolver);
            assertNotNull(objectId);
            assertEquals(id, objectId.getKey().key);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            DefaultDeserializationContext context = new DefaultDeserializationContext(mapper.getDeserializerProvider(), mapper.getDeserializerProvider().getCache());
            context.checkUnresolvedObjectId();
            assertFalse(context.isEnabled(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testDeserializerInstance() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            DefaultDeserializationContext context = new DefaultDeserializationContext(mapper.getDeserializerProvider(), mapper.getDeserializerProvider().getCache());
            JsonDeserializer<Object> deser = context.deserializerInstance(null, new CustomDeserializer());
            assertNotNull(deser);
            assertEquals(CustomDeserializer.class, deser.getClass());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testKeyDeserializerInstance() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            DefaultDeserializationContext context = new DefaultDeserializationContext(mapper.getDeserializerProvider(), mapper.getDeserializerProvider().getCache());
            KeyDeserializer keyDeser = context.keyDeserializerInstance(null, new CustomKeyDeserializer());
            assertNotNull(keyDeser);
            assertEquals(CustomKeyDeserializer.class, keyDeser.getClass());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateInstance() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            DefaultDeserializationContext context = new DefaultDeserializationContext(mapper.getDeserializerProvider(), mapper.getDeserializerProvider().getCache());
            JsonParser parser = mapper.getFactory().createParser("{}");
            InjectableValues values = new InjectableValues.Std();
            DefaultDeserializationContext newInstance = context.createInstance(mapper.getDeserializationConfig(), parser, values);
            assertNotNull(newInstance);
            assertEquals(DefaultDeserializationContext.Impl.class, newInstance.getClass());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testWith() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            DefaultDeserializationContext context = new DefaultDeserializationContext(mapper.getDeserializerProvider(), mapper.getDeserializerProvider().getCache());
            DefaultDeserializationContext newContext = context.with(mapper.getDeserializerProvider());
            assertNotNull(newContext);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testCopy() {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = new DefaultDeserializationContext(mapper.getDeserializerProvider(), mapper.getDeserializerProvider().getCache());
        context.copy();
    }

    private static class CustomDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) {
            return null;
        }
    }

    private static class CustomKeyDeserializer extends KeyDeserializer {
        @Override
        public Object deserializeKey(String key, DeserializationContext ctxt) {
            return null;
        }
    }
}