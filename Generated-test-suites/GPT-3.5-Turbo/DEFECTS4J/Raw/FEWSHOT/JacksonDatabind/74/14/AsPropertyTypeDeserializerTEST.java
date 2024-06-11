package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonToken;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AsPropertyTypeDeserializerTest {
    
    @Mock
    private JavaType bt;
    
    @Mock
    private TypeIdResolver idRes;
    
    private String typePropertyName = "type";
    
    private boolean typeIdVisible = true;
    
    @Mock
    private JavaType defaultImpl;
    
    private As inclusion = As.PROPERTY;
    
    @Mock
    private BeanProperty property;
    
    @Mock
    private DeserializationContext ctxt;
    
    @Mock
    private TokenBuffer tb;
    
    @Mock
    private JsonDeserializer<Object> deser;
    
    private AsPropertyTypeDeserializer deserializer;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
    }
    
    @Test
    public void testDeserializeTypedFromObjectWithTypeIds() throws IOException {
        JsonParser p = mock(JsonParser.class);
        
        when(p.canReadTypeId()).thenReturn(true);
        when(p.getTypeId()).thenReturn("typeId");
        when(deserializer._deserializeWithNativeTypeId(p, ctxt, "typeId")).thenReturn("typeIdDeserialized");
        
        assertEquals("typeIdDeserialized", deserializer.deserializeTypedFromObject(p, ctxt));
        verifyZeroInteractions(p, ctxt, tb);
        verify(deserializer).deserializeTypedFromObject(p, ctxt);
    }
    
    @Test
    public void testDeserializeTypedFromObjectStartObject() throws IOException {
        JsonParser p = mock(JsonParser.class);
        
        when(p.canReadTypeId()).thenReturn(false);
        when(p.getCurrentToken()).thenReturn(JsonToken.START_OBJECT, JsonToken.FIELD_NAME, JsonToken.END_OBJECT);
        
        assertNull(deserializer.deserializeTypedFromObject(p, ctxt));
        verify(p, times(2)).getCurrentToken();
        verify(deserializer).deserializeTypedUsingDefaultImpl(p, ctxt, null);
        verifyZeroInteractions(ctxt, tb);
    }
    
    @Test
    public void testDeserializeTypedFromObjectStartArray() throws IOException {
        JsonParser p = mock(JsonParser.class);
        
        when(p.canReadTypeId()).thenReturn(false);
        when(p.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        
        assertNull(deserializer.deserializeTypedFromObject(p, ctxt));
        verify(p).getCurrentToken();
        verify(deserializer).deserializeTypedUsingDefaultImpl(p, ctxt, null);
        verifyZeroInteractions(ctxt, tb);
    }
    
    @Test
    public void testDeserializeTypedFromObjectDefaultImplDeserializer() throws IOException {
        JsonParser p = mock(JsonParser.class);
        
        when(p.canReadTypeId()).thenReturn(false);
        when(p.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
        when(deserializer._findDefaultImplDeserializer(ctxt)).thenReturn(deser);
        when(deser.deserialize(p, ctxt)).thenReturn("defaultImplDeserialized");
        
        assertEquals("defaultImplDeserialized", deserializer.deserializeTypedFromObject(p, ctxt));
        verify(p).getCurrentToken();
        verify(deserializer).deserializeTypedUsingDefaultImpl(p, ctxt, null);
        verifyZeroInteractions(tb);
    }
    
    @Test
    public void testDeserializeTypedFromObjectNaturalValue() throws IOException {
        JsonParser p = mock(JsonParser.class);
        
        when(p.canReadTypeId()).thenReturn(false);
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_FALSE);
        when(deserializer.deserializeIfNatural(p, ctxt, deserializer._baseType)).thenReturn(false);
        
        assertEquals(false, deserializer.deserializeTypedFromObject(p, ctxt));
        verify(p).getCurrentToken();
        verify(deserializer).deserializeTypedUsingDefaultImpl(p, ctxt, null);
        verifyZeroInteractions(tb);
    }
    
    @Test
    public void testDeserializeTypedFromObjectMissingProperty() throws IOException {
        JsonParser p = mock(JsonParser.class);
        
        when(p.canReadTypeId()).thenReturn(false);
        when(p.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
        when(p.getCurrentName()).thenReturn("other");
        
        deserializer.deserializeTypedFromObject(p, ctxt);
        verify(p).getCurrentToken();
        verify(deserializer).deserializeTypedUsingDefaultImpl(p, ctxt, null);
        verifyZeroInteractions(tb);
        verify(ctxt).reportWrongTokenException(p, JsonToken.FIELD_NAME,
                "missing property 'type' that is to contain type id  (for class javaType)");
    }
    
    @Test
    public void testDeserializeTypedForId() throws IOException {
        JsonParser p = mock(JsonParser.class);
        
        when(p.getText()).thenReturn("typeId");
        when(deserializer._findDeserializer(ctxt, "typeId")).thenReturn(deser);
        when(deserializer._typeIdVisible).thenReturn(true);
        
        assertEquals(deser.deserialize(p, ctxt), deserializer.deserializeTypedForId(p, ctxt, tb));
        verify(deserializer).deserializeTypedForId(p, ctxt, tb);
        verifyZeroInteractions(ctxt, tb);
        verify(deser).deserialize(p, ctxt);
    }
    
    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws IOException {
        JsonParser p = mock(JsonParser.class);
        
        when(deserializer._findDefaultImplDeserializer(ctxt)).thenReturn(deser);
        when(deser.deserialize(p, ctxt)).thenReturn("defaultImplDeserialized");
        
        assertEquals("defaultImplDeserialized", deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, tb));
        verifyZeroInteractions(p, tb);
        verify(deserializer)._deserializeTypedUsingDefaultImpl(p, ctxt, tb);
    }
    
    @Test
    public void testDeserializeTypedUsingDefaultImplNaturalValue() throws IOException {
        JsonParser p = mock(JsonParser.class);
        
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_FALSE);
        when(deserializer.deserializeIfNatural(p, ctxt, deserializer._baseType)).thenReturn(false);
        
        assertEquals(false, deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, tb));
        verifyZeroInteractions(p, tb);
        verify(deserializer)._deserializeTypedUsingDefaultImpl(p, ctxt, tb);
    }
    
    @Test
    public void testDeserializeTypedUsingDefaultImplStartArray() throws IOException {
        JsonParser p = mock(JsonParser.class);
        
        when(p.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        
        assertNull(deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, tb));
        verifyZeroInteractions(p, tb);
        verify(deserializer)._deserializeTypedUsingDefaultImpl(p, ctxt, tb);
    }
    
    @Test
    public void testDeserializeTypedUsingDefaultImplFieldMismatch() throws IOException {
        JsonParser p = mock(JsonParser.class);
        
        when(p.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
        when(p.getCurrentName()).thenReturn("other");
        when(ctxt.reportWrongTokenException(p, JsonToken.FIELD_NAME,
                "missing property 'type' that is to contain type id  (for class javaType)")).thenReturn(Boolean.TRUE);
        
        assertNull(deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, tb));
        verifyZeroInteractions(p, tb);
        verify(deserializer)._deserializeTypedUsingDefaultImpl(p, ctxt, tb);
        verify(ctxt).reportWrongTokenException(p, JsonToken.FIELD_NAME,
                "missing property 'type' that is to contain type id  (for class javaType)");
    }
    
}