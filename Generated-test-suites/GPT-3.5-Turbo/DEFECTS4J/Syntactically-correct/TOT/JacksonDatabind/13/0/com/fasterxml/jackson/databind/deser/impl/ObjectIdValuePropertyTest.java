import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValuePropertyTest.NotFoundDeserializer;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null);

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(null, null));
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser("123");
        Object instance = new Object();

        try {
            property.deserializeAndSet(jsonParser, null, instance);
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            assertEquals("Incompatible types: expected ObjectId but found java.lang.String", e.getOriginalMessage());
        }

        // Additional test cases can be added here to cover other scenarios
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(null, null));
        JsonParser jsonParser = new ObjectMapper().getFactory().createParser("123");
        Object instance = new Object();

        try {
            property.deserializeSetAndReturn(jsonParser, null, instance);
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            assertEquals("Incompatible types: expected ObjectId but found java.lang.String", e.getOriginalMessage());
        }

        // Additional test cases can be added here to cover other scenarios
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(null, null));
        Object instance = new Object();

        try {
            property.set(instance, "123");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }

        // Additional test cases can be added here to cover other scenarios
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(null, null));
        Object instance = new Object();

        try {
            property.setAndReturn(instance, "123");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }

        // Additional test cases can be added here to cover other scenarios
    }
}