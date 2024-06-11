import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.*;

import java.lang.annotation.Annotation;

import static org.junit.Assert.*;

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
    public void testGetName() {
        PropertyName expectedName = objectIdReader.propertyName;
        PropertyName actualName = objectIdValueProperty.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetType() {
        Class<?> expectedType = objectIdReader.getIdType();
        Class<?> actualType = objectIdValueProperty.getType();
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetMetadata() {
        PropertyMetadata expectedMetadata = metadata;
        PropertyMetadata actualMetadata = objectIdValueProperty.getMetadata();
        assertEquals(expectedMetadata, actualMetadata);
    }

    @Test
    public void testGetDeserializer() {
        JsonDeserializer<?> expectedDeserializer = objectIdReader.getDeserializer();
        JsonDeserializer<?> actualDeserializer = objectIdValueProperty.getDeserializer();
        assertEquals(expectedDeserializer, actualDeserializer);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = new NullValueProvider();
        SettableBeanProperty expectedProperty = new ObjectIdValueProperty(objectIdValueProperty, objectIdValueProperty.getDeserializer(), nva);
        SettableBeanProperty actualProperty = objectIdValueProperty.withNullProvider(nva);
        assertEquals(expectedProperty, actualProperty);
    }

    @Test
    public void testWithNewName() {
        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty expectedProperty = new ObjectIdValueProperty(objectIdValueProperty, newName);
        SettableBeanProperty actualProperty = objectIdValueProperty.withName(newName);
        assertEquals(expectedProperty, actualProperty);
    }

    @Test
    public void testWithValueDeserializerSame() {
        JsonDeserializer<?> deser = objectIdValueProperty.getDeserializer();
        SettableBeanProperty actualProperty = objectIdValueProperty.withValueDeserializer(deser);
        assertEquals(objectIdValueProperty, actualProperty);
    }

    @Test
    public void testGetValueDeserializer() {
        JsonDeserializer<?> expectedDeserializer = objectIdValueProperty.getDeserializer();
        JsonDeserializer<?> actualDeserializer = objectIdValueProperty.getValueDeserializer();
        assertEquals(expectedDeserializer, actualDeserializer);
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> acls = Annotation.class;
        Annotation actualAnnotation = objectIdValueProperty.getAnnotation(acls);
        assertNull(actualAnnotation);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember expectedMember = null;
        AnnotatedMember actualMember = objectIdValueProperty.getMember();
        assertEquals(expectedMember, actualMember);
    }

    @Test
    public void testDeserializeAndSet_WithNullValue() throws IOException {
        JsonParser p = new ObjectMapper().getFactory().createParser("null");
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        objectIdValueProperty.deserializeAndSet(p, ctxt, instance);
        assertNull(instance);
    }

    @Test
    public void testDeserializeAndSet_WithNonNullValue() throws IOException {
        JsonParser p = new ObjectMapper().getFactory().createParser("\"test\"");
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        objectIdValueProperty.deserializeAndSet(p, ctxt, instance);
        assertNotNull(instance);
    }

    @Test
    public void testDeserializeSetAndReturn_WithNullValue() throws IOException {
        JsonParser p = new ObjectMapper().getFactory().createParser("null");
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        Object result = objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance);
        assertNull(result);
    }

    @Test
    public void testDeserializeSetAndReturn_WithNonNullValue() throws IOException {
        JsonParser p = new ObjectMapper().getFactory().createParser("\"test\"");
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();
        Object result = objectIdValueProperty.deserializeSetAndReturn(p, ctxt, instance);
        assertNotNull(result);
    }

    @Test
    public void testSet_WithNoIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        try {
            objectIdValueProperty.set(instance, value);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            String expectedMessage = "Should not call set() on ObjectIdProperty that has no SettableBeanProperty";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testSetAndReturn_WithNoIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        try {
            objectIdValueProperty.setAndReturn(instance, value);
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            String expectedMessage = "Should not call set() on ObjectIdProperty that has no SettableBeanProperty";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}