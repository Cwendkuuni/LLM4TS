package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NullifyingDeserializerTest {

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @InjectMocks
    private NullifyingDeserializer nullifyingDeserializer;

    @Test
    public void testDeserialize() throws IOException {
        nullifyingDeserializer.deserialize(jsonParser, deserializationContext);

        verify(jsonParser, times(1)).skipChildren();
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);

        nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        verify(typeDeserializer, times(1)).deserializeTypedFromAny(jsonParser, deserializationContext);
    }
}