import static org.junit.Assert.*;
import org.junit.Test;

public class SetterlessPropertyTest {

    @Test
    public void testDeserializeAndSet_NullValue() {
        // Null value should be treated as empty Collection/Map
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();
        try {
            property.deserializeAndSet(null, null, instance);
            assertNull(property.get(instance));
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }

    @Test
    public void testDeserializeAndSet_TypeDeserializer() {
        // Typed deser with setterless property is not supported
        SetterlessProperty property = new SetterlessProperty(null, null, new TypeDeserializer(), null, null);
        Object instance = new Object();
        try {
            property.deserializeAndSet(null, null, instance);
            fail("Should throw BadDefinitionException");
        } catch (IOException e) {
            assertEquals("Problem deserializing 'setterless' property (null): no way to handle typed deser with setterless yet", e.getMessage());
        }
    }

    @Test
    public void testDeserializeAndSet_GetMethodReturnsNull() {
        // Get method returns null, should throw BadDefinitionException
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object() {
            public Object getGetter() {
                return null;
            }
        };
        try {
            property.deserializeAndSet(null, null, instance);
            fail("Should throw BadDefinitionException");
        } catch (IOException e) {
            assertEquals("Problem deserializing 'setterless' property 'null': get method returned null", e.getMessage());
        }
    }

    @Test
    public void testDeserializeAndSet_Success() {
        // Typical use case, should deserialize and set value successfully
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object() {
            public Object getGetter() {
                return new Object();
            }
        };
        try {
            property.deserializeAndSet(null, null, instance);
            assertNotNull(property.get(instance));
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        // Test deserializeSetAndReturn method, should deserialize, set, and return instance
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();
        try {
            Object result = property.deserializeSetAndReturn(null, null, instance);
            assertEquals(instance, result);
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet_UnsupportedOperationException() throws IOException {
        // Should throw UnsupportedOperationException when calling `set()`
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        property.set(null, null);
    }

    @Test
    public void testSetAndReturn() {
        // Test setAndReturn method, should set and return instance
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = new Object();
        try {
            Object result = property.setAndReturn(null, null);
            assertEquals(instance, result);
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }

}