
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class ObjectIdValuePropertyTest {

    @Mock
    private JsonParser mockJsonParser;
    @Mock
    private DeserializationContext mockDeserializationContext;
    @Mock
    private Object mockInstance;
    @Mock
    private ObjectIdReader mockObjectIdReader;
    @Mock
    private ReadableObjectId mockReadableObjectId;
    @Mock
    private SettableBeanProperty mockIdProperty;
    private ObjectIdValueProperty objectIdValueProperty;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, PropertyMetadata.STD_REQUIRED);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(mockJsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(mockJsonObjectIdReader.getDeserializer()).thenReturn(new CustomDeserializer());
        when(mockJsonObjectIdReader.getResolver()).thenReturn(mockResolver);
        when(mockDeserializationContext.findObjectId(any(), any(), any())).thenReturn(mockReadableObjectId);

        Object result = objectIdValueProperty.deserializeSetAndReturn(mockJsonParser, mockDeserializationContext,
                mockInstance);

        assertEquals(mockInstance, result);
        verify(mockJsonObjectIdReader, never()).idProperty;
    }

    @Test
    public void testDeserializeAndSetWithNullToken() throws IOException {
        when(mockJsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        Object result = objectIdValueProperty.deserializeSetAndReturn(mockJsonParser, mockDeserializationContext,
                mockInstance);

        assertNull(result);
        verify(mockJsonObjectIdReader, never()).idProperty;
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithoutIdProperty() throws IOException {
        when(mockObjectIdReader.idProperty).thenReturn(null);

        objectIdValueProperty.setAndReturn(mockInstance, null);
    }

    @Test
    public void testSetAndReturnWithIdProperty() throws IOException {
        when(mockObjectIdReader.idProperty).thenReturn(mockIdProperty);
        when(mockIdProperty.setAndReturn(mockInstance, null)).thenReturn(mockInstance);

        Object result = objectIdValueProperty.setAndReturn(mockInstance, null);

        assertEquals(mockInstance, result);
    }

    /**
     * Custom deserializer for testing purposes
     */
    private class CustomDeserializer extends StdDeserializer<Object> {

        protected CustomDeserializer() {
            super(Object.class);
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }

    /**
     * Custom resolver for testing purposes
     */
    private class CustomResolver extends DefaultDeserializationContext.Impl.Referring {

        protected CustomResolver(JsonDeserializer<?> deser, ObjectIdReader oir, Object ref,
                                 UnresolvedForwardReference reference, Class<?> refType) {
            super(deser, oir, ref, reference, refType);
        }

        @Override
        public void handleResolvedForwardReference(Object id, Object value) {

        }
    }
}
