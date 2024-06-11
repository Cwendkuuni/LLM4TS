import static org.junit.Assert.*;
import org.junit.Test;

public class MethodPropertyTest {

    @Test
    public void testSet() {
        MethodProperty mp = new MethodProperty(null, null, null, null, null);
        mp.set("instance", "value");
        // assert the value has been set correctly
        assertEquals("value", mp.get("instance"));
    }

    @Test
    public void testDeserializeAndSet() {
        MethodProperty mp = new MethodProperty(null, null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        try {
            mp.deserializeAndSet(p, ctxt, "instance");
        } catch (IOException e) {
            // assert the exception message is correct
            assertEquals("Exception message", e.getMessage());
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        MethodProperty mp = new MethodProperty(null, null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = new Object();
        try {
            Object result = mp.deserializeSetAndReturn(p, ctxt, instance);
            // assert the instance has not been changed
            assertSame(instance, result);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testWithNullProvider() {
        MethodProperty mp = new MethodProperty(null, null, null, null, null);
        NullValueProvider nva = null;
        SettableBeanProperty result = mp.withNullProvider(nva);
        // assert the returned object is a new instance
        assertNotSame(mp, result);
    }

    @Test
    public void testWithFullName() {
        MethodProperty mp = new MethodProperty(null, null, null, null, null);
        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty result = mp.withName(newName);
        // assert the returned object is a new instance
        assertNotSame(mp, result);
    }

    @Test
    public void testWithDeserializer() {
        MethodProperty mp = new MethodProperty(null, null, null, null, null);
        JsonDeserializer<?> deser = null;
        SettableBeanProperty result = mp.withValueDeserializer(deser);
        // assert the returned object is a new instance
        assertNotSame(mp, result);
    }

    @Test
    public void testFixAccess() {
        MethodProperty mp = new MethodProperty(null, null, null, null, null);
        DeserializationConfig config = null;
        mp.fixAccess(config);
        // assert no exception is thrown
    }

    @Test
    public void testGetAnnotation() {
        MethodProperty mp = new MethodProperty(null, null, null, null, null);
        Class<A> acls = null;
        A result = mp.getAnnotation(acls);
        assertNull(result);
    }

}