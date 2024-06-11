import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setup() {
        objectIdReader = new ObjectIdReader();
        metadata = new PropertyMetadata();
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty newProperty = objectIdValueProperty.withName(newName);
        assertEquals(newName, newProperty.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deserializer = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) {
                return null;
            }
        };
        ObjectIdValueProperty newProperty = objectIdValueProperty.withValueDeserializer(deserializer);
        assertEquals(deserializer, newProperty.getValueDeserializer());
    }

    // Additional edge cases and exception handling scenarios testing to be added

}