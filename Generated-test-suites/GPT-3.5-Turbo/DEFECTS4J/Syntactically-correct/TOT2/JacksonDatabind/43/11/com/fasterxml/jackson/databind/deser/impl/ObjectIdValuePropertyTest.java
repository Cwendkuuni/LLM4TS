import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    ObjectIdValueProperty property;

    @Before
    public void setUp() {
        // Initialize ObjectIdReader for testing
        property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata(false));
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty newProperty = property.withName(newName);

        assertNotNull(newProperty);
        assertEquals(newName, newProperty.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> newDeserializer = new CustomDeserializer();
        ObjectIdValueProperty newProperty = property.withValueDeserializer(newDeserializer);

        assertNotNull(newProperty);
        assertEquals(newDeserializer, newProperty.getValueDeserializer());
    }

    // Additional test cases for edge cases and error scenarios can be added here

}