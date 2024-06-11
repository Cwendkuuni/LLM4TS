import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.Annotation;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import java.io.IOException;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setUp() {
        objectIdReader = new ObjectIdReader();
        metadata = new PropertyMetadata();
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }

    @Test
    public void testConstructor() {
        assertNotNull(objectIdValueProperty);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = null;
        ObjectIdValueProperty result = objectIdValueProperty.withNullProvider(nva);
        assertNotNull(result);
    }

    @Test
    public void testWithSimpleName() {
        PropertyName newName = null;
        ObjectIdValueProperty result = objectIdValueProperty.withSimpleName(newName);
        assertNotNull(result);
    }

    @Test
    public void testGetIdType() {
        Class<?> result = objectIdValueProperty.getIdType();
        assertNotNull(result);
    }

    @Test
    public void testWithSimpleName() {
        PropertyName newName = null;
        ObjectIdValueProperty result = objectIdValueProperty.withSimpleName(newName);
        assertNotNull(result);
    }

    @Test
    public void testWithName() {
        PropertyName newName = null;
        ObjectIdValueProperty result = objectIdValueProperty.withName(newName);
        assertNotNull(result);
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = null;
        A result = objectIdValueProperty.getAnnotation(acls);
        assertNull(result);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember result = objectIdValueProperty.getMember();
        assertNull(result);
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
        objectIdValueProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = null;
        Object value = null;
        Object result = objectIdValueProperty.setAndReturn(instance, value);
        assertNull(result);
    }
}