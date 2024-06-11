
package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SetterlessPropertyTest {

    @Test
    public void testDeserializeAndSetWithNullToken() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null,
                null, null, null);

        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, null);

        Mockito.verify(jsonParser, Mockito.times(1)).getCurrentToken();
        Mockito.verifyZeroInteractions(deserializationContext);
    }

    @Test
    public void testDeserializeAndSetWithValueTokenAndTypeDeserializer() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null,
                mock(TypeDeserializer.class), null, null);

        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, null);

        Mockito.verify(jsonParser, Mockito.times(1)).getCurrentToken();
        Mockito.verify(deserializationContext, Mockito.times(1)).reportBadDefinition(null,
                String.format("Problem deserializing 'setterless' property (\"%s\"): no way to handle typed deser with setterless yet",
                        null));
    }

    @Test
    public void testDeserializeAndSetWithValueTokenAndNonNullGetter() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null,
                null, null, null);

        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_TRUE);

        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, null);

        Mockito.verify(jsonParser, Mockito.times(1)).getCurrentToken();
        Mockito.verifyNoMoreInteractions(deserializationContext);
    }

    @Test
    public void testDeserializeAndSetWithValueTokenAndNullGetter() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null,
                null, null, null);

        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_TRUE);

        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, null);

        Mockito.verify(jsonParser, Mockito.times(1)).getCurrentToken();
        Mockito.verifyZeroInteractions(deserializationContext);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null,
                null, null, null);

        setterlessProperty.deserializeSetAndReturn(jsonParser, deserializationContext, null);

        Mockito.verify(jsonParser, Mockito.times(1)).getCurrentToken();
        Mockito.verifyZeroInteractions(deserializationContext);
    }

    @Test
    public void testSet() throws IOException {
        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null,
                null, null, null);

        try {
            setterlessProperty.set(null, null);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testSetAndReturn() throws IOException {
        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null,
                null, null, null);

        try {
            setterlessProperty.setAndReturn(null, null);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
}
