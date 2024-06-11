import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private JsonDeserializer<?> deserializer;
    private PropertyMetadata metadata;
    private ObjectIdValueProperty property;
    
    @Before
    public void setUp() {
        objectIdReader = mock(ObjectIdReader.class);
		deserializer = mock(JsonDeserializer.class);
		metadata = mock(PropertyMetadata.class);
        property = new ObjectIdValueProperty(objectIdReader, metadata);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(objectIdReader.getIdType(), property.getType());
        assertEquals(metadata, property.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
        assertEquals(objectIdReader, property.getObjectIdReader());
    }
    
    @Test
    public void testWithNewName() {
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty newProperty = property.withName(newName);
        
        assertEquals(newName, newProperty.getName());
        assertEquals(objectIdReader.getIdType(), newProperty.getType());
        assertEquals(metadata, newProperty.getMetadata());
        assertEquals(deserializer, newProperty.getValueDeserializer());
        assertEquals(objectIdReader, newProperty.getObjectIdReader());
    }
    
    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> newDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty newProperty = property.withValueDeserializer(newDeserializer);
        
        assertEquals(objectIdReader.propertyName, newProperty.getName());
        assertEquals(objectIdReader.getIdType(), newProperty.getType());
        assertEquals(metadata, newProperty.getMetadata());
        assertEquals(newDeserializer, newProperty.getValueDeserializer());
        assertEquals(objectIdReader, newProperty.getObjectIdReader());
    }
    
    @Test
    public void testGetAnnotation() {
        // No implementation in the class, expected to return null
        assertEquals(null, property.getAnnotation(JsonDeserialize.class));
    }
    
    @Test
    public void testGetMember() {
        // No implementation in the class, expected to return null
        assertEquals(null, property.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        
        // Mocking the internal method calls
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        when(objectIdReader.getDeserializer()).thenReturn(valueDeserializer);
        when(valueDeserializer.deserialize(p, ctxt)).thenReturn("123");
        
        ReadableObjectId roid = mock(ReadableObjectId.class);
        when(ctxt.findObjectId(any(), any(), any())).thenReturn(roid);
        
        SettableBeanProperty idProp = mock(SettableBeanProperty.class);
        when(objectIdReader.idProperty).thenReturn(idProp);
        when(idProp.setAndReturn(instance, "123")).thenReturn(instance);
        
        assertEquals(instance, property.deserializeAndSet(p, ctxt, instance));
        
        // Verify the internal method calls
        verify(valueDeserializer, times(1)).deserialize(p, ctxt);
        
        verify(ctxt, times(1)).findObjectId(eq("123"), eq(objectIdReader.generator), eq(objectIdReader.resolver));
        verify(roid, times(1)).bindItem(instance);
        
        verify(idProp, times(1)).setAndReturn(instance, "123");
    }
    
    @Test
    public void testDeserializeSetAndReturn_NullId() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        
        // Mocking the internal method calls
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        when(objectIdReader.getDeserializer()).thenReturn(valueDeserializer);
        when(valueDeserializer.deserialize(p, ctxt)).thenReturn(null);
        
        ReadableObjectId roid = mock(ReadableObjectId.class);
        when(ctxt.findObjectId(any(), any(), any())).thenReturn(roid);
        
        SettableBeanProperty idProp = mock(SettableBeanProperty.class);
        when(objectIdReader.idProperty).thenReturn(idProp);
        when(idProp.setAndReturn(instance, null)).thenReturn(instance);
        
        assertEquals(null, property.deserializeSetAndReturn(p, ctxt, instance));
        
        // Verify the internal method calls
        verify(valueDeserializer, times(1)).deserialize(p, ctxt);
        
        verify(ctxt, never()).findObjectId(any(), any(), any());
        verify(roid, never()).bindItem(any());
        
        verify(idProp, never()).setAndReturn(any(), any());
    }
    
    @Test
    public void testDeserializeSetAndReturn_NullIdProperty() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        
        // Mocking the internal method calls
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        when(objectIdReader.getDeserializer()).thenReturn(valueDeserializer);
        when(valueDeserializer.deserialize(p, ctxt)).thenReturn("123");
        
        ReadableObjectId roid = mock(ReadableObjectId.class);
        when(ctxt.findObjectId(any(), any(), any())).thenReturn(roid);
        
        when(objectIdReader.idProperty).thenReturn(null);
        
        assertEquals(instance, property.deserializeSetAndReturn(p, ctxt, instance));
        
        // Verify the internal method calls
        verify(valueDeserializer, times(1)).deserialize(p, ctxt);
        
        verify(ctxt, times(1)).findObjectId(eq("123"), eq(objectIdReader.generator), eq(objectIdReader.resolver));
        verify(roid, times(1)).bindItem(instance);
        
        verify(idProp, never()).setAndReturn(any(), any());
    }
    
    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        SettableBeanProperty idProp = mock(SettableBeanProperty.class);
        when(objectIdReader.idProperty).thenReturn(idProp);
        when(idProp.setAndReturn(instance, value)).thenReturn(instance);
        
        assertEquals(instance, property.setAndReturn(instance, value));
        
        // Verify the internal method calls
        verify(idProp, times(1)).setAndReturn(instance, value);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSet_NoIdProperty() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        when(objectIdReader.idProperty).thenReturn(null);
        
        property.setAndReturn(instance, value);
    }
}