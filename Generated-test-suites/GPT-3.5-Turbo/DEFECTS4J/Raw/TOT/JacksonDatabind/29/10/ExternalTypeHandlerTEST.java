import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler.Builder().build();
        // Test when the property is not present in the map
        assertFalse(externalTypeHandler.handleTypePropertyValue(null, null, "unknownProp", null));
        // Test when the property is present in the map, but does not have the correct type property name
        assertFalse(externalTypeHandler.handleTypePropertyValue(null, null, "propName", null));
        // Test when the property is present in the map with the correct type property name
        assertTrue(externalTypeHandler.handleTypePropertyValue(null, null, "typePropertyName", null));
    }

    @Test
    public void testHandlePropertyValue() {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler.Builder().build();
        // Test when the property is not present in the map
        assertFalse(externalTypeHandler.handlePropertyValue(null, null, "unknownProp", null));
        // Test when the property is present in the map, but does not have the correct type property name
        assertFalse(externalTypeHandler.handlePropertyValue(null, null, "propName", null));
        // Test when the property is present in the map with the correct type property name
        assertTrue(externalTypeHandler.handlePropertyValue(null, null, "typePropertyName", null));
    }

    @Test
    public void testComplete() {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler.Builder().build();
        // Test when type id is missing for an external type id property
        try {
            externalTypeHandler.complete(null, null, null);
            fail("Expected MappingException to be thrown");
        } catch (MappingException e) {
            assertEquals("Missing external type id property 'typePropertyName'", e.getMessage());
        }
    }

    @Test
    public void testCompleteWithBuffer() {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler.Builder().build();
        // Test when type id is missing for an external type id property
        try {
            externalTypeHandler.complete(null, null, null, null);
            fail("Expected MappingException to be thrown");
        } catch (MappingException e) {
            assertEquals("Missing external type id property 'typePropertyName'", e.getMessage());
        }
    }
}