
import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.ValueDeserializer;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdValuePropertyTest {

    @Test
    public void testWithName() {
        ObjectIdReader reader = new ObjectIdReader();
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, new PropertyMetadata());
        ObjectIdValueProperty newProperty = property.withName(PropertyName.valueOf("newName"));
        
        assertEquals("newName", newProperty.getPropertyName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdReader reader = new ObjectIdReader();
        ValueDeserializer<?> deserializer = new ValueDeserializer<>();
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, new PropertyMetadata());
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);
        
        assertEquals(deserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        assertNull(property.getAnnotation(SomeAnnotation.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        assertNull(property.getMember());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNoIdProperty() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdReader(), new PropertyMetadata());
        property.set(new Object(), new Object());
    }
}
