import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import java.io.IOException;
import java.lang.annotation.Annotation;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
public class ObjectIdValuePropertyTest {

    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setUp() {
        ObjectIdReader objectIdReader = new ObjectIdReader(null, null, null, null);
        PropertyMetadata metadata = new PropertyMetadata(null, null, null, null);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testWithDeserName() {
        JsonDeserializer<?> deser = null;
        ObjectIdValueProperty actual = objectIdValueProperty.withValueDeserializer(deser);
        assertNotNull(actual);
    }

    @Test
    public void testWithNewName() {
        PropertyName newName = null;
        ObjectIdValueProperty actual = objectIdValueProperty.withName(newName);
        assertNotNull(actual);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        objectIdValueProperty.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object instance = null;
        Object result = objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance);
        assertNull(result);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = null;
        Object value = null;
        try {
            objectIdValueProperty.set(instance, value);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", ex.getMessage());
        }
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = null;
        Object value = null;
        try {
            objectIdValueProperty.setAndReturn(instance, value);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", ex.getMessage());
        }
    }
}