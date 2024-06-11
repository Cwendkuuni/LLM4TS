import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setup() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata();
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testWithDeser() {
        JsonDeserializer<?> deser = null;
        ObjectIdValueProperty prop = objectIdValueProperty.withValueDeserializer(deser);
        assertNotNull(prop);
        assertEquals(deser, prop.getValueDeserializer());
    }

    @Test
    public void testWithDeser_NullDeser() {
        JsonDeserializer<?> deser = null;
        try {
            ObjectIdValueProperty prop = objectIdValueProperty.withValueDeserializer(deser);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("JsonDeserializer cannot be null", e.getMessage());
        }
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty newName = objectIdValueProperty.withName(new PropertyName("newName"));
        assertNotNull(newName);
        assertEquals("newName", newName.getName().getSimpleName());
    }

    @Test
    public void testGetName() {
        PropertyName propertyName = objectIdValueProperty.getName();
        assertNotNull(propertyName);
        assertNull(propertyName.getSimpleName());
    }

    @Test
    public void testSerializeAndSet() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object instance = null;
     
        try {
            objectIdValueProperty.deserializeAndSet(jp, ctxt, instance);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("JsonParser cannot be null", e.getMessage());
        }
    }

    @Test
    public void testSerializedSetAndReturn() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object instance = null;
     
        try {
            objectIdValueProperty.deserializeSetAndReturn(jp, ctxt, instance);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("JsonParser cannot be null", e.getMessage());
        }
    }

    @Test
    public void testSerializeAndSet_NullReader() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        Object instance = null;
     
        try {
            objectIdValueProperty.deserializeAndSet(jp, ctxt, instance);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("JsonParser cannot be null", e.getMessage());
        }
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
     
        try {
            objectIdValueProperty.set(instance, value);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
     
        try {
            objectIdValueProperty.setAndReturn(instance, value);
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", e.getMessage());
        }
    }

}