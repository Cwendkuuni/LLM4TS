package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NullifyingDeserializerTest {

    private NullifyingDeserializer nullifyingDeserializer = NullifyingDeserializer.instance;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private TypeDeserializer typeDeserializer;

    @Test
    public void testDeserialize() throws Exception {
        Object result = nullifyingDeserializer.deserialize(jsonParser, deserializationContext);

        verify(jsonParser, times(1)).skipChildren();
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_ArrayObjectFieldName() throws Exception {
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY)
                                               .thenReturn(JsonTokenId.ID_START_OBJECT)
                                               .thenReturn(JsonTokenId.ID_FIELD_NAME);

        Object result1 = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        Object result2 = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        Object result3 = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        verify(typeDeserializer, times(1)).deserializeTypedFromAny(jsonParser, deserializationContext);
        verify(typeDeserializer, times(1)).deserializeTypedFromAny(jsonParser, deserializationContext);
        verify(typeDeserializer, times(1)).deserializeTypedFromAny(jsonParser, deserializationContext);
        assertNull(result1);
        assertNull(result2);
        assertNull(result3);
    }

    @Test
    public void testDeserializeWithType_OtherToken() throws Exception {
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_STRING)
                                               .thenReturn(JsonTokenId.ID_BOOLEAN)
                                               .thenReturn(JsonTokenId.ID_NULL);

        Object result1 = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        Object result2 = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        Object result3 = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        verify(typeDeserializer, never()).deserializeTypedFromAny(jsonParser, deserializationContext);
        assertNull(result1);
        assertNull(result2);
        assertNull(result3);
    }
}