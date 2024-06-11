import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {

    @Test
    public void testWithName() {
        // Test case for withName method
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        PropertyName name = new PropertyName("test");
        SettableBeanProperty result = property.withName(name);
        
        assertEquals(result.getName(), name);
    }

    @Test
    public void testWithValueDeserializer() {
        // Test case for withValueDeserializer method
        ObjectIdReferenceProperty property = new ObjectIdReferenceProperty(null, null);
        JsonDeserializer<?> deser = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return null;
            }
        };
        
        SettableBeanProperty result = property.withValueDeserializer(deser);
        
        assertEquals(result.getValueDeserializer(), deser);
    }

    // Additional test cases can be added for the remaining methods in the class

}