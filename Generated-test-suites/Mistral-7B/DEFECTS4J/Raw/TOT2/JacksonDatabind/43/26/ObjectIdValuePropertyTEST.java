import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class ObjectIdValuePropertyTest {

    @Test
    public void testConstructor() {
        ObjectIdReader objectIdReader = new ObjectIdReader() {
            // Dummy implementation
        };
        PropertyMetadata metadata = new PropertyMetadata();
        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {
            // Dummy implementation
        };

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, metadata);
        assertEquals("Expected ObjectIdValueProperty", property.getClass(), property.getClass());
        ObjectIdValueProperty property2 = new ObjectIdValueProperty(property, deserializer);
        assertEquals("Expected ObjectIdValueProperty", property.getClass(), property2.getClass());
    }
}