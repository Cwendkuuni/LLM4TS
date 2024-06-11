package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class ObjectIdValuePropertyTest {
    
    @Mock
    JsonParser mockJsonParser;
    
    @Mock
    DeserializationContext mockDeserializationContext;
    
    @Mock
    Object mockInstance;
    
    @Mock
    JsonDeserializer<?> mockJsonDeserializer;
    
    ObjectIdValueProperty objectIdValueProperty;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        objectIdValueProperty.deserializeAndSet(mockJsonParser, mockDeserializationContext, mockInstance);
        verify(mockJsonDeserializer, times(1)).deserialize(mockJsonParser, mockDeserializationContext);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object result = objectIdValueProperty.deserializeSetAndReturn(mockJsonParser, mockDeserializationContext, mockInstance);
        verify(mockJsonDeserializer, times(1)).deserialize(mockJsonParser, mockDeserializationContext);
        assertNull(result);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        SettableBeanProperty mockSettableBeanProperty = mock(SettableBeanProperty.class);
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        objectIdReader.idProperty = mockSettableBeanProperty;
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        Object result = objectIdValueProperty.setAndReturn(mockInstance, "test");
        verify(mockSettableBeanProperty, times(1)).setAndReturn(mockInstance, "test");
        assertEquals(mockInstance, result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithNoIdProperty() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        objectIdReader.idProperty = null;
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, metadata);
        objectIdValueProperty.setAndReturn(mockInstance, "test");
    }

}