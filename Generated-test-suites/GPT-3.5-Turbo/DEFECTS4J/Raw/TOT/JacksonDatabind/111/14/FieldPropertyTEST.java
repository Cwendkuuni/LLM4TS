
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.deser.impl.FieldProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

public class FieldPropertyTest {
    @Mock
    private SettableBeanProperty settableBeanProperty;

    @Mock
    private Field field;

    @Mock
    private JsonParser jsonParser;

    private FieldProperty fieldProperty;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        fieldProperty = new FieldProperty(null, null, null, null, new AnnotatedField(null, null, null));
        fieldProperty = spy(fieldProperty);
        doNothing().when(settableBeanProperty).fixAccess(any());
    }

    @Test
    public void testDeserializeAndSet_NullValue_SkipNullsTrue() throws IOException {
        fieldProperty._skipNulls = true;
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        fieldProperty.deserializeAndSet(jsonParser, null, null);

        verify(jsonParser, never()).nextToken();
        verify(field, never()).set(any(), any());
    }

    @Test
    public void testDeserializeAndSet_NullValue_SkipNullsFalse() throws IOException {
        fieldProperty._skipNulls = false;
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        fieldProperty.deserializeAndSet(jsonParser, null, null);

        verify(jsonParser, never()).nextToken();
        verify(field, never()).set(any(), any());
    }

    @Test
    public void testDeserializeAndSet_NullValueWithoutSkipNulls() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        fieldProperty.deserializeAndSet(jsonParser, null, null);

        verify(jsonParser).nextToken();
        verify(field, never()).set(any(), any());
    }

    @Test
    public void testDeserializeAndSet_NonNullValueWithoutTypeDeserializer() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(fieldProperty._valueTypeDeserializer).thenReturn(null);

        fieldProperty.deserializeAndSet(jsonParser, null, null);

        verify(jsonParser).nextToken();
        verify(field).set(any(), any());
    }

    @Test
    public void testDeserializeAndSet_NonNullValueWithTypeDeserializer() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(fieldProperty._valueTypeDeserializer).thenReturn(mock(TypeDeserializer.class));

        fieldProperty.deserializeAndSet(jsonParser, null, null);

        verify(jsonParser).nextToken();
        verify(field).set(any(), any());
    }

    @Test
    public void testDeserializeAndSet_NullValueWithNullProvider() throws IOException {
        fieldProperty._skipNulls = false;
        fieldProperty._nullProvider = stubNullValueProvider();
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        fieldProperty.deserializeAndSet(jsonParser, null, null);

        verify(jsonParser, never()).nextToken();
        verify(field, never()).set(any(), any());
    }

    @Test
    public void testDeserializeSetAndReturn_NullValue_SkipNullsTrue() throws IOException {
        fieldProperty._skipNulls = true;
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        Object instance = new Object();
        fieldProperty.deserializeSetAndReturn(jsonParser, null, instance);

        verify(jsonParser, never()).nextToken();
        verify(field, never()).set(any(), any());
    }

    @Test
    public void testDeserializeSetAndReturn_NullValue_SkipNullsFalse() throws IOException {
        fieldProperty._skipNulls = false;
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        Object instance = new Object();
        fieldProperty.deserializeSetAndReturn(jsonParser, null, instance);

        verify(jsonParser, never()).nextToken();
        verify(field, never()).set(any(), any());
        verify(field, never()).set(any(), any());
    }

    @Test
    public void testDeserializeSetAndReturn_NullValueWithoutSkipNulls() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        Object instance = new Object();
        fieldProperty.deserializeSetAndReturn(jsonParser, null, instance);

        verify(jsonParser).nextToken();
        verify(field, never()).set(any(), any());
    }

    @Test
    public void testDeserializeSetAndReturn_NonNullValueWithoutTypeDeserializer() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(fieldProperty._valueTypeDeserializer).thenReturn(null);

        Object instance = new Object();
        fieldProperty.deserializeSetAndReturn(jsonParser, null, instance);

        verify(jsonParser).nextToken();
        verify(field).set(any(), any());
    }

    @Test
    public void testDeserializeSetAndReturn_NonNullValueWithTypeDeserializer() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(fieldProperty._valueTypeDeserializer).thenReturn(mock(TypeDeserializer.class));

        Object instance = new Object();
        fieldProperty.deserializeSetAndReturn(jsonParser, null, instance);

        verify(jsonParser).nextToken();
        verify(field).set(any(), any());
    }

    @Test
    public void testDeserializeSetAndReturn_NullValueWithNullProvider() throws IOException {
        fieldProperty._skipNulls = false;
        fieldProperty._nullProvider = stubNullValueProvider();
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        Object instance = new Object();
        fieldProperty.deserializeSetAndReturn(jsonParser, null, instance);

        verify(jsonParser, never()).nextToken();
        verify(field, never()).set(any(), any());
    }

    @Test
    public void testSet() throws IOException, IllegalAccessException {
        Object instance = new Object();
        Object value = new Object();

        fieldProperty.set(instance, value);

        verify(field).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException, IllegalAccessException {
        Object instance = new Object();
        Object value = new Object();

        fieldProperty.setAndReturn(instance, value);

        verify(field).set(instance, value);
    }

    private static NullValueProvider stubNullValueProvider() {
        return new NullValueProvider(null) {
            @Override
            public Object getNullValue(DeserializationContext ctxt) {
                return null;
            }
        };
    }
}
