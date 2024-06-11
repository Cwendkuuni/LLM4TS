package com.example.valueinstantiator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ValueInstantiatorTest {

    private ValueInstantiator valueInstantiator;
    private DeserializationContext context;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        valueInstantiator = new ValueInstantiator.Base(String.class);
        context = mock(DeserializationContext.class);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testgetValueTypeDesc() {
        assertEquals("java.lang.String", valueInstantiator.getValueTypeDesc());
    }

    @Test
    public void testCanInstantiate_canCreateUsingDefault() throws IOException {
        when(valueInstantiator.getDefaultCreator()).thenReturn(mock(AnnotatedWithParams.class));

        Object actual = valueInstantiator.createUsingDefault(context);
        assertEquals(String.class, actual.getClass());

        verifyDefaultCreatorCalled();
    }

    @Test
    public void testCanInstantiate_cannotCreateUsingDefault() throws IOException {
        valueInstantiator = new ValueInstantiator.Base(Integer.class);

        Object actual = valueInstantiator.createUsingDefault(context);
        assertNull(actual);

        verifyNoDefaultCreatorCalled();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateFromObjectWith_missingArguments() throws IOException {
        SettableBeanProperty[] properties = new SettableBeanProperty[]{};
        when(valueInstantiator.getFromObjectArguments(context)).thenReturn(properties);

        valueInstantiator.createFromObjectWith(context, properties, null);
    }

    @Test
    public void testCreateFromObjectWith_validArguments() throws IOException {
        SettableBeanProperty[] properties = new SettableBeanProperty[]{new SettableBeanProperty("name", String.class)};
        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
        when(valueInstantiator.getFromObjectArguments(context)).thenReturn(properties);
        when(buffer.getParameters(properties)).thenReturn(new Object[]{new Object()});

        Object actual = valueInstantiator.createFromObjectWith(context, properties, buffer);
        assertEquals(String.class, actual.getClass());

        verifyCreateFromObjectWithCalled();
    }

    @Test
    public void testCreateFromString() throws IOException {
        when(valueInstantiator.getDefaultCreator()).thenReturn(mock(AnnotatedWithParams.class));

        Object actual = valueInstantiator.createFromString(context, "test");
        assertEquals("test", actual);

        verifyDefaultCreatorCalled();
    }

    @Test
    public void testCreateFromInt() throws IOException {
        when(valueInstantiator.getDefaultCreator()).thenReturn(mock(AnnotatedWithParams.class));

        Object actual = valueInstantiator.createFromInt(context, 42);
        assertEquals(Integer.valueOf(42), actual);

        verifyDefaultCreatorCalled();
    }

    @Test(expected = IOException.class)
    public void testCreateFromInt_missingCreator() throws IOException {
        valueInstantiator = new ValueInstantiator.Base(Integer.class);

        valueInstantiator.createFromInt(context, 42);
    }

    private void verifyDefaultCreatorCalled() {
        verify(context).handleMissingInstantiator(eq(valueInstantiator.getValueClass()), any(JsonParser.class), anyString());
    }

    private void verifyNoDefaultCreatorCalled() {
        verify(context, times(0)).handleMissingInstantiator(eq(valueInstantiator.getValueClass()), any(JsonParser.class), anyString());
    }

    private void verifyCreateFromObjectWithCalled() {
        verify(context).handleMissingInstantiator(eq(valueInstantiator.getValueClass()), any(JsonParser.class), anyString());
    }
}