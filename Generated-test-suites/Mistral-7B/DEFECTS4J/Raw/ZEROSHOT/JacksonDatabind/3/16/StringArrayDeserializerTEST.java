package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StdDeserializer;
import com.fasterxml.jackson.databind.deser.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyContextVisitor;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link StringArrayDeserializer}
 */
@JacksonStdImpl
public class StringArrayDeserializerTest {

    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private TypeDeserializer typeDeserializer;
    private StringArrayDeserializer stringArrayDeserializer;

    @Before
    public void setup() {
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext)).thenReturn(new Object[] {});

        stringArrayDeserializer = new StringArrayDeserializer();
    }

    @Test
    public void testDeserializeStandard() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);

        ObjectBuffer buffer = Mockito.mock(ObjectBuffer.class);
        when(deserializationContext.leaseObjectBuffer()).thenReturn(buffer);

        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);

        Mockito.verify(jsonParser, Mockito.times(1)).nextToken();
        Mockito.verify(jsonParser, Mockito.times(1)).isExpectedStartArrayToken();
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeCustom() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("test");
        stringArrayDeserializer = new StringArrayDeserializer(jsonDeserializer);

        ObjectBuffer buffer = Mockito.mock(ObjectBuffer.class);
        when(deserializationContext.leaseObjectBuffer()).thenReturn(buffer);

        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);

        Mockito.verify(jsonParser, Mockito.times(1)).nextToken();
        Mockito.verify(jsonParser, Mockito.times(1)).isExpectedStartArrayToken();
        assertArrayEquals(new String[] {"test"}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeNonArray() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(jsonParser.getText()).thenReturn("test");

        stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
    }

    @Test(expected = IOException.class)
    public void testDeserializeNullValue() throws IOException {
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

        stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
    }

    @Test
    public void testCreateContextual() throws IOException {
        when(deserializationContext.findContextualValueDeserializer(Mockito.any(Class.class), Mockito.any(BeanProperty.class))).thenReturn(mock(JsonDeserializer.class));

        JsonDeserializer<String> jsonDeserializer = mock(JsonDeserializer.class);
        when(deserializationContext.handleSecondaryContextualization(jsonDeserializer, Mockito.any(BeanProperty.class))).thenReturn(jsonDeserializer);

        StringArrayDeserializer deserializer = stringArrayDeserializer.createContextual(deserializationContext, Mockito.mock(BeanProperty.class));

        assert(deserializer instanceof StringArrayDeserializer);
        JsonDeserializer<String> elementDeserializer = deserializer.getElementDeserializer();
        assert(elementDeserializer instanceof JsonDeserializer);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        when(typeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext)).thenReturn(new Object[] {null});

        String[] result = (String[]) stringArrayDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        Mockito.verify(jsonParser, Mockito.times(1)).nextToken();
        Mockito.verify(jsonParser, Mockito.times(1)).isExpectedStartArrayToken();
        assertArrayEquals(new String[0], result);
    }
}