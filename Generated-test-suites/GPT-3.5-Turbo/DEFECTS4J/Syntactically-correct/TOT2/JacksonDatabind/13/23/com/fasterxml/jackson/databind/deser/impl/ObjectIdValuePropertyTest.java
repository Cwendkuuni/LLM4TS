import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader reader = new ObjectIdReader("id", Long.class, new PropertyMetadata(), new CustomDeserializer());
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, new PropertyMetadata());
        
        assertEquals("id", property.getName());

        assertNotNull(property.getType());

        assertEquals(reader.getDeserializer(), property.getValueDeserializer());
    }

    @Test
    public void testWithPropertyName() {
        ObjectIdReader reader = new ObjectIdReader("id", Long.class, new PropertyMetadata(), new CustomDeserializer());
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, new PropertyMetadata());

        PropertyName newName = new PropertyName("newId");
        ObjectIdValueProperty newProperty = property.withName(newName);

        assertEquals("newId", newProperty.getName());

        assertNotNull(newProperty.getType());

        assertEquals(reader.getDeserializer(), newProperty.getValueDeserializer());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader reader = new ObjectIdReader("id", Long.class, new PropertyMetadata(), new CustomDeserializer());
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, new PropertyMetadata());

        JsonDeserializer<?> newDeserializer = new CustomDeserializer();
        ObjectIdValueProperty newProperty = property.withValueDeserializer(newDeserializer);

        assertEquals("id", newProperty.getName());

        assertNotNull(newProperty.getType());

        assertEquals(newDeserializer, newProperty.getValueDeserializer());
    }

    // Additional edge cases and exception handling scenarios can be tested here

}