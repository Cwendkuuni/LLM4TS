import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;

@RunWith(MockitoJUnitRunner.class)
public class ObjectIdValuePropertyTest {
    
    private ObjectIdReader objectIdReader;
    private PropertyMetadata metadata;
    private JsonDeserializer<?> deserializer;
    private ObjectIdValueProperty objectIdValueProperty;
    
    @Mock
    private JsonParser jsonParser;
    
    @Mock
    private DeserializationContext deserializationContext;
    
    @Mock
    private Object instance;
    
    @Before
    public void setup() {
        objectIdReader = mock(ObjectIdReader.class);
        metadata = mock(PropertyMetadata.class);
        deserializer = mock(JsonDeserializer.class);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(objectIdReader.propertyName, objectIdValueProperty.getName());
        assertEquals(objectIdReader.getIdType(), objectIdValueProperty.getType());
        assertEquals(metadata, objectIdValueProperty.getMetadata());
        assertEquals(objectIdReader.getDeserializer(), objectIdValueProperty.getValueDeserializer());
        assertEquals(objectIdReader, objectIdValueProperty._objectIdReader);
    }
    
    @Test
    public void testWithNewName() {
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty newProperty = objectIdValueProperty.withName(newName);
        assertEquals(newName, newProperty.getName());
        assertEquals(objectIdValueProperty.getType(), newProperty.getType());
        assertEquals(objectIdValueProperty.getMetadata(), newProperty.getMetadata());
        assertEquals(objectIdValueProperty.getValueDeserializer(), newProperty.getValueDeserializer());
        assertEquals(objectIdValueProperty._objectIdReader, newProperty._objectIdReader);
    }
    
    @Test
    public void testWithNewDeserializer() {
        JsonDeserializer<?> newDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty newProperty = objectIdValueProperty.withValueDeserializer(newDeserializer);
        assertEquals(objectIdValueProperty.getName(), newProperty.getName());
        assertEquals(objectIdValueProperty.getType(), newProperty.getType());
        assertEquals(objectIdValueProperty.getMetadata(), newProperty.getMetadata());
        assertEquals(newDeserializer, newProperty.getValueDeserializer());
        assertEquals(objectIdValueProperty._objectIdReader, newProperty._objectIdReader);
    }
    
    @Test
    public void testGetAnnotation() {
        assertNull(objectIdValueProperty.getAnnotation(Object.class));
    }
    
    @Test
    public void testGetMember() {
        assertNull(objectIdValueProperty.getMember());
    }
    
    @Test
    public void testDeserializeAndSet_NullValue() throws IOException {
        when(objectIdReader.getDeserializer()).thenReturn(deserializer);
        when(deserializer.deserialize(Mockito.eq(jsonParser), Mockito.eq(deserializationContext))).thenReturn(null);
        
        assertNull(objectIdValueProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance));
        
        verify(objectIdReader.getDeserializer()).deserialize(Mockito.eq(jsonParser), Mockito.eq(deserializationContext));
    }
    
    @Test
    public void testDeserializeAndSet_NonNullValue() throws IOException {
        when(objectIdReader.getDeserializer()).thenReturn(deserializer);
        Object deserializedValue = new Object();
        when(deserializer.deserialize(Mockito.eq(jsonParser), Mockito.eq(deserializationContext))).thenReturn(deserializedValue);
        when(objectIdReader.getGenerator()).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.getResolver()).thenReturn(mock(ObjectIdResolver.class));
        
        ObjectIdValueProperty spyProperty = Mockito.spy(objectIdValueProperty);
        ObjectIdReader spyReader = Mockito.spy(objectIdReader);
        SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
        when(spyReader.idProperty).thenReturn(idProperty);
        Mockito.doReturn(spyReader).when(spyProperty)._objectIdReader;
        
        ArgumentCaptor<Object> instanceCaptor = ArgumentCaptor.forClass(Object.class);
        
        assertEquals(instance, spyProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance));
        
        verify(objectIdReader.getDeserializer()).deserialize(Mockito.eq(jsonParser), Mockito.eq(deserializationContext));
        verify(spyReader.getGenerator()).bindItem(instanceCaptor.capture());
        verify(idProperty).setAndReturn(instance, deserializedValue);
        
        assertEquals(instance, instanceCaptor.getValue());
    }
    
    @Test
    public void testDeserializeAndSet_NullIdProperty() throws IOException {
        when(objectIdReader.getDeserializer()).thenReturn(deserializer);
        Object deserializedValue = new Object();
        when(deserializer.deserialize(Mockito.eq(jsonParser), Mockito.eq(deserializationContext))).thenReturn(deserializedValue);
        when(objectIdReader.getGenerator()).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.getResolver()).thenReturn(mock(ObjectIdResolver.class));
        
        ObjectIdValueProperty spyProperty = Mockito.spy(objectIdValueProperty);
        Mockito.doReturn(null).when(spyProperty)._objectIdReader;
        
        assertEquals(instance, spyProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance));
        
        verify(objectIdReader.getDeserializer()).deserialize(Mockito.eq(jsonParser), Mockito.eq(deserializationContext));
    }
    
    @Test
    public void testSet_NullIdProperty() throws IOException {
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            objectIdValueProperty.set(instance, new Object());
        });
        
        assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", exception.getMessage());
    }
    
    @Test
    public void testSetAndReturn_NullIdProperty() throws IOException {
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            objectIdValueProperty.setAndReturn(instance, new Object());
        });
        
        assertEquals("Should not call set() on ObjectIdProperty that has no SettableBeanProperty", exception.getMessage());
    }
    
    @Test
    public void testResolvableDeserializer() throws IOException {
        ObjectIdResolver resolver = mock(ObjectIdResolver.class);
        ObjectIdGenerator generator = mock(ObjectIdGenerator.class);
        
        when(objectIdReader.getResolver()).thenReturn(resolver);
        when(objectIdReader.getGenerator()).thenReturn(generator);
        
        objectIdValueProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        
        verify(resolver).bindItem(instance);
        verify(generator).bindItem(instance);
    }
}