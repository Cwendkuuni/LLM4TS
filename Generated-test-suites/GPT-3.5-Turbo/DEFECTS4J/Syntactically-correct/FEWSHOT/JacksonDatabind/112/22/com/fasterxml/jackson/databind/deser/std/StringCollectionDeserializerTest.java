package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StringCollectionDeserializerTest {
    private StringCollectionDeserializer stringCollectionDeserializer;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private Collection<String> result;

    @Before
    public void setUp() {
        JsonFactory jsonFactory = new JsonFactory();
        try {
            jsonParser = jsonFactory.createParser("\"foo\", \"bar\", \"baz\"");
            deserializationContext = mock(DeserializationContext.class);
            stringCollectionDeserializer = new StringCollectionDeserializer(
                    null, null, null, null, null, null);
            result = Arrays.asList("foo", "bar", "baz");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeserialize() {
        try {
            Collection<String> collection =
                    stringCollectionDeserializer.deserialize(jsonParser, deserializationContext);
            assertEquals(result, collection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeserializeWithType() {
        try {
            Collection<String> collection =
                    stringCollectionDeserializer.deserializeWithType(jsonParser, deserializationContext, null);
            assertEquals(result, collection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeserializeWithCustomDeserializer() {
        JsonDeserializer<String> customDeserializer = mock(JsonDeserializer.class);
        try {
            when(customDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("custom");
            stringCollectionDeserializer = stringCollectionDeserializer.withResolved(null, customDeserializer, null, false);
            Collection<String> collection =
                    stringCollectionDeserializer.deserialize(jsonParser, deserializationContext);
            assertTrue(collection.contains("custom"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeserializeWithCustomDeserializerAndDelegateDeserializer() {
        JsonDeserializer<String> customDeserializer = mock(JsonDeserializer.class);
        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        try {
            when(customDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("custom");
            when(delegateDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(Arrays.asList("foo", "bar", "baz"));
            stringCollectionDeserializer = stringCollectionDeserializer.withResolved(delegateDeserializer, customDeserializer, null, false);
            Collection<String> collection =
                    stringCollectionDeserializer.deserialize(jsonParser, deserializationContext);
            assertTrue(collection.contains("custom"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeserializeWhenValueDeserializerIsNull() {
        stringCollectionDeserializer = stringCollectionDeserializer.withResolved(null, null, null, false);
        try {
            Collection<String> collection =
                    stringCollectionDeserializer.deserialize(jsonParser, deserializationContext);
            assertEquals(result, collection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeserializeWhenNextTextValueIsNull() {
        JsonParser jsonParser = mock(JsonParser.class);
        try {
            when(jsonParser.nextTextValue()).thenReturn(null, null, null, null);
            when(jsonParser.getCurrentToken()).thenReturn(JsonToken.END_ARRAY);
            Collection<String> collection =
                    stringCollectionDeserializer.deserialize(jsonParser, deserializationContext);
            assertTrue(collection.isEmpty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IOException.class)
    public void testDeserializeWhenParsingExceptionIsThrown() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        JsonToken jsonToken = mock(JsonToken.class);
        when(jsonParser.nextTextValue()).thenThrow(IOException.class);
        when(jsonParser.getCurrentToken()).thenReturn(jsonToken);
        when(jsonToken.equals(JsonToken.END_ARRAY)).thenReturn(false);

        stringCollectionDeserializer.deserialize(jsonParser, deserializationContext);
    }
}