import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import java.lang.annotation.Annotation;

public class ObjectIdValuePropertyTest {

    private ObjectMapper objectMapper;
    private JsonParser jsonParser;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        jsonParser = objectMapper.getFactory().createParser("test json");
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null);
        PropertyMetadata propertyMetadata = new PropertyMetadata(null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);

        Object instance = new Object();
        DeserializationContext context = objectMapper.getDeserializationContext();

        // typical use case
        property.deserializeAndSet(jsonParser, context, instance);

        // edge cases
        property.deserializeAndSet(null, context, instance);
        property.deserializeAndSet(jsonParser, null, instance);
        property.deserializeAndSet(jsonParser, context, null);

        // error scenarios
        try {
            jsonParser.close();
            property.deserializeAndSet(jsonParser, context, instance);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // expected exception
        }

        try {
            jsonParser = objectMapper.getFactory().createParser("{\"id\": 123}");
            property.deserializeAndSet(jsonParser, context, instance);
            fail("Expected JsonParseException to be thrown");
        } catch (JsonParseException e) {
            // expected exception
        }
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null);
        PropertyMetadata propertyMetadata = new PropertyMetadata(null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);

        Object instance = new Object();
        DeserializationContext context = objectMapper.getDeserializationContext();

        // typical use case
        Object result = property.deserializeSetAndReturn(jsonParser, context, instance);
        assertNull(result);

        // edge cases
        result = property.deserializeSetAndReturn(null, context, instance);
        assertNull(result);

        property.deserializeSetAndReturn(jsonParser, null, instance);
        assertNull(result);

        property.deserializeSetAndReturn(jsonParser, context, null);
        assertNull(result);

        // error scenarios
        try {
            jsonParser.close();
            property.deserializeSetAndReturn(jsonParser, context, instance);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // expected exception
        }

        try {
            jsonParser = objectMapper.getFactory().createParser("{\"id\": 123}");
            property.deserializeSetAndReturn(jsonParser, context, instance);
            fail("Expected JsonParseException to be thrown");
        } catch (JsonParseException e) {
            // expected exception
        }
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null);
        PropertyMetadata propertyMetadata = new PropertyMetadata(null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);

        Object instance = new Object();

        // typical use case
        Object value = new Object();
        Object result = property.setAndReturn(instance, value);
        assertNull(result);

        // error scenarios
        try {
            property.setAndReturn(instance, null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // expected exception
        }
    }

    @Test
    public void testGetAnnotation() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null);
        PropertyMetadata propertyMetadata = new PropertyMetadata(null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);

        // typical use case
        assertNull(property.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null);
        PropertyMetadata propertyMetadata = new PropertyMetadata(null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);

        // typical use case
        assertNull(property.getMember());
    }

    @Test
    public void testWithName() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null);
        PropertyMetadata propertyMetadata = new PropertyMetadata(null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        PropertyName newName = new PropertyName("newName");

        // typical use case
        ObjectIdValueProperty result = property.withName(newName);
        assertEquals(property, result);
    }

    @Test
    public void testWithValueDeserializer() throws IOException {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null);
        PropertyMetadata propertyMetadata = new PropertyMetadata(null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        JsonDeserializer<?> deser = new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                return null;
            }
        };

        // typical use case
        ObjectIdValueProperty result = property.withValueDeserializer(deser);
        assertEquals(property, result);
    }
}