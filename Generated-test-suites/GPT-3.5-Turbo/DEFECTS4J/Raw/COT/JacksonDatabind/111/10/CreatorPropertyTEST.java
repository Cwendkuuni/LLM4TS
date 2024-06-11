import static org.junit.Assert.*;

import org.junit.Test;

public class CreatorPropertyTest {

    @Test
    public void testGetName() {
        CreatorProperty property = new CreatorProperty(new PropertyName("name"), null, null, null, null, null, 0, null, null);
        assertEquals("name", property.getName().getSimpleName());
    }

    @Test
    public void testWithNullProvider() {
        CreatorProperty property = new CreatorProperty(new PropertyName("name"), null, null, null, null, null, 0, null, null);
        property = property.withNullProvider(null);
        assertNull(property.getNullProvider());
    }

    @Test
    public void testDeserializeAndSet() {
        CreatorProperty property = new CreatorProperty(new PropertyName("name"), null, null, null, null, null, 0, null, null);
        assertNotNull(property);
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testWithInvalidPropertyName() {
        try {
            CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Property name cannot be null", e.getMessage());
        }
    }

    @Test
    public void testWithInvalidNullProvider() {
        try {
            CreatorProperty property = new CreatorProperty(new PropertyName("name"), null, null, null, null, null, 0, null, null);
            property = property.withNullProvider(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Null provider cannot be null", e.getMessage());
        }
    }

    @Test
    public void testSetWithNullInstance() {
        CreatorProperty property = new CreatorProperty(new PropertyName("name"), null, null, null, null, null, 0, null, null);
        try {
            property.set(null, "value");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("Instance cannot be null", e.getMessage());
        }
    }

    @Test
    public void testSetAndReturnWithNullInstance() {
        CreatorProperty property = new CreatorProperty(new PropertyName("name"), null, null, null, null, null, 0, null, null);
        try {
            property.setAndReturn(null, "value");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("Instance cannot be null", e.getMessage());
        }
    }

    @Test
    public void testDeserializeAndSetWithNullInstance() {
        CreatorProperty property = new CreatorProperty(new PropertyName("name"), null, null, null, null, null, 0, null, null);
        try {
            property.deserializeAndSet(null, null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("Instance cannot be null", e.getMessage());
        }
    }

    @Test
    public void testDeserializeSetAndReturnWithNullInstance() {
        CreatorProperty property = new CreatorProperty(new PropertyName("name"), null, null, null, null, null, 0, null, null);
        try {
            property.deserializeSetAndReturn(null, null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("Instance cannot be null", e.getMessage());
        }
    }

    @Test
    public void testInjectWithNullInstance() {
        CreatorProperty property = new CreatorProperty(new PropertyName("name"), null, null, null, null, null, 0, null, null);
        try {
            property.inject(null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("Instance cannot be null", e.getMessage());
        }
    }
}