import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty property;

    @Before
    public void setUp() {
        ObjectIdReader reader = new ObjectIdReader();
        PropertyMetadata metadata = new PropertyMetadata();
        property = new ObjectIdValueProperty(reader, metadata);
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName();
        ObjectIdValueProperty newProperty = property.withName(newName);
        assertEquals(newName, newProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        ObjectIdValueProperty newProperty = property.withValueDeserializer(deser);
        assertEquals(deser, newProperty.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(property.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            property.deserializeAndSet(jp, ctxt, instance);
        } catch (IOException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testDeserializeSetAndReturn() {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        try {
            Object value = property.deserializeSetAndReturn(jp, ctxt, instance);
            assertEquals(instance, value);
        } catch (IOException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testSet() {
        Object instance = new Object();
        Object value = new Object();
        try {
            property.set(instance, value);
        } catch (IOException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testSetAndReturn() {
        Object instance = new Object();
        Object value = new Object();
        try {
            Object returned = property.setAndReturn(instance, value);
            assertEquals(instance, returned);
        } catch (IOException e) {
            fail("Should not throw exception");
        }
    }
}