
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.annotation.Annotation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private PropertyMetadata propertyMetadata;
    private JsonDeserializer<Object> deserializer;

    @Before
    public void setup() {
        objectIdReader = mock(ObjectIdReader.class);
        propertyMetadata = mock(PropertyMetadata.class);
        deserializer = mock(JsonDeserializer.class);
    }

    @Test
    public void testConstructor() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        
        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(propertyMetadata, property.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
        assertEquals(objectIdReader, property.getObjectIdReader());
    }

    @Test
    public void testConstructorWithDeserializerAndNullValueProvider() {
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, deserializer, null);
        
        assertEquals(src.getName(), property.getName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(deserializer, property.getValueDeserializer());
        assertEquals(src.getObjectIdReader(), property.getObjectIdReader());
    }

    @Test
    public void testConstructorWithNewName() {
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        ObjectIdValueProperty property = new ObjectIdValueProperty(src, newName);
        
        assertEquals(newName, property.getName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(src.getValueDeserializer(), property.getValueDeserializer());
        assertEquals(src.getObjectIdReader(), property.getObjectIdReader());
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        ObjectIdValueProperty property = (ObjectIdValueProperty) src.withName(newName);
        
        assertEquals(newName, property.getName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(src.getValueDeserializer(), property.getValueDeserializer());
        assertEquals(src.getObjectIdReader(), property.getObjectIdReader());
    }

    @Test
    public void testWithValueDeserializerSameDeserializer() {
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        ObjectIdValueProperty property = (ObjectIdValueProperty) src.withValueDeserializer(deser);
        
        assertSame(src, property);
    }

    @Test
    public void testWithValueDeserializerDifferentDeserializer() {
        JsonDeserializer<Object> deser1 = mock(JsonDeserializer.class);
        JsonDeserializer<Object> deser2 = mock(JsonDeserializer.class);
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        ObjectIdValueProperty property = (ObjectIdValueProperty) src.withValueDeserializer(deser2);
        
        assertNotSame(src, property);
        assertEquals(src.getName(), property.getName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(deser2, property.getValueDeserializer());
        assertNull(property.getNullProvider());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = mock(NullValueProvider.class);
        ObjectIdValueProperty src = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        ObjectIdValueProperty property = (ObjectIdValueProperty) src.withNullProvider(nva);
        
        assertEquals(src.getName(), property.getName());
        assertEquals(src.getType(), property.getType());
        assertEquals(src.getMetadata(), property.getMetadata());
        assertEquals(src.getValueDeserializer(), property.getValueDeserializer());
        assertEquals(nva, property.getNullProvider());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        
        assertNull(property.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        
        property.deserializeAndSet(p, ctxt, instance);
        
        verify(property).deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        ObjectMapper mapper = new ObjectMapper();
        
        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        
        assertNull(mapper.readValue("{\"id\": null}", Object.class));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        ObjectIdProperty idProperty = mock(ObjectIdProperty.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        
        when(objectIdReader.idProperty).thenReturn(idProperty);
        
        try {
            property.set(instance, value);
            fail("Expected UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        ObjectIdProperty idProperty = mock(ObjectIdProperty.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, propertyMetadata);
        
        when(objectIdReader.idProperty).thenReturn(idProperty);
        
        try {
            property.setAndReturn(instance, value);
            fail("Expected UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
}
