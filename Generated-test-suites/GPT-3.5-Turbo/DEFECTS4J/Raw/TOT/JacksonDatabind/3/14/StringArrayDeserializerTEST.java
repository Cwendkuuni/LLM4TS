import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class StringArrayDeserializerTest {

    @Mock
    private JsonParser mockJsonParser;
    
    @Mock
    private DeserializationContext mockDeserializationContext;
    
    @Mock
    private JsonMappingException mockJsonMappingException;

    private StringArrayDeserializer stringArrayDeserializer;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        stringArrayDeserializer = StringArrayDeserializer.instance;
    }

    @Test
    void testDeserialize_Typical() throws IOException {
        // Prepare
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(mockJsonParser.getText()).thenReturn("Value1");
        
        // Execute
        String[] result = stringArrayDeserializer.deserialize(mockJsonParser, mockDeserializationContext);
        
        // Verify
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result[0]).isEqualTo("Value1");
    }
    
    @Test
    void testDeserialize_EmptyArray() throws IOException {
        // Prepare
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        
        // Execute
        String[] result = stringArrayDeserializer.deserialize(mockJsonParser, mockDeserializationContext);
        
        // Verify
        assertThat(result).isNotNull();
        assertThat(result).hasSize(0);
    }

    @Test
    void testDeserialize_NullValue() throws IOException {
        // Prepare
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);

        // Execute
        String[] result = stringArrayDeserializer.deserialize(mockJsonParser, mockDeserializationContext);

        // Verify
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result[0]).isNull();
    }

    @Test
    void testDeserialize_NonArrayToken() throws IOException {
        // Prepare
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(mockDeserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("Value1");

        // Execute and Verify
        assertThatExceptionOfType(JsonMappingException.class)
                .isThrownBy(() -> stringArrayDeserializer.deserialize(mockJsonParser, mockDeserializationContext));
    }

    @Test
    void testDeserialize_NonArrayToken_AcceptEmptyStringAsNull_WithEmptyString() throws IOException {
        // Prepare
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(mockDeserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(mockDeserializationContext.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
        when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("");

        // Execute
        String[] result = stringArrayDeserializer.deserialize(mockJsonParser, mockDeserializationContext);

        // Verify
        assertThat(result).isNull();
    }

    @Test
    void testDeserialize_NonArrayToken_AcceptEmptyStringAsNull_WithNonEmptyString() throws IOException {
        // Prepare
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(mockDeserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(mockDeserializationContext.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(true);
        when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("Value1");

        // Execute
        String[] result = stringArrayDeserializer.deserialize(mockJsonParser, mockDeserializationContext);

        // Verify
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result[0]).isEqualTo("Value1");
    }

    @Test
    void testDeserialize_NonArrayToken_NotAcceptEmptyStringAsNull_WithEmptyString() throws IOException {
        // Prepare
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(mockDeserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(mockDeserializationContext.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(false);
        when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("");

        // Execute and Verify
        assertThatExceptionOfType(JsonMappingException.class)
                .isThrownBy(() -> stringArrayDeserializer.deserialize(mockJsonParser, mockDeserializationContext));
    }

    @Test
    void testDeserialize_NonArrayToken_NotAcceptEmptyStringAsNull_WithNonEmptyString() throws IOException {
        // Prepare
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(mockDeserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(mockDeserializationContext.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(false);
        when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("Value1");

        // Execute
        String[] result = stringArrayDeserializer.deserialize(mockJsonParser, mockDeserializationContext);

        // Verify
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result[0]).isEqualTo("Value1");
    }

    @Test
    void testDeserialize_CustomDeserializer() throws IOException {
        // Prepare
        MockCustomDeserializer mockCustomDeserializer = new MockCustomDeserializer();
        StringArrayDeserializer customStringArrayDeserializer = new StringArrayDeserializer(mockCustomDeserializer);

        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(mockJsonParser.getText()).thenReturn("Value1");

        // Execute
        String[] result = customStringArrayDeserializer.deserialize(mockJsonParser, mockDeserializationContext);

        // Verify
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result[0]).isEqualTo("Custom_Deserialize:Value1");
    }

    @Test
    void testDeserializeWithContext_DefaultDeserializer() throws IOException {
        // Prepare
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(mockJsonParser.getText()).thenReturn("Value1");

        // Execute
        Object result = stringArrayDeserializer.deserializeWithType(mockJsonParser, mockDeserializationContext, null);

        // Verify
        assertThat(result).isNull();
    }

    @Test
    void testDeserializeWithContext_CustomDeserializer() throws IOException {
        // Prepare
        MockCustomDeserializer mockCustomDeserializer = new MockCustomDeserializer();
        StringArrayDeserializer customStringArrayDeserializer = new StringArrayDeserializer(mockCustomDeserializer);

        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(mockJsonParser.getText()).thenReturn("Value1");

        // Execute
        Object result = customStringArrayDeserializer.deserializeWithType(mockJsonParser, mockDeserializationContext, null);

        // Verify
        assertThat(result).isNull();
    }
    
    private static class MockCustomDeserializer extends StringArrayDeserializer {
        public MockCustomDeserializer() {
        }
        @Override
        protected String[] _deserializeCustom(JsonParser jp, DeserializationContext ctxt) throws IOException {
            final ObjectBuffer buffer = ctxt.leaseObjectBuffer();
            Object[] chunk = buffer.resetAndStart();

            int ix = 0;
            JsonToken t;

            while ((t = jp.nextToken()) != JsonToken.END_ARRAY) {
                // Ok: no need to convert Strings, but must recognize nulls
                String value = "Custom_Deserialize:" + (t == JsonToken.VALUE_NULL ? null : jp.getText());
                if (ix >= chunk.length) {
                    chunk = buffer.appendCompletedChunk(chunk);
                    ix = 0;
                }
                chunk[ix++] = value;
            }
            String[] result = buffer.completeAndClearBuffer(chunk, ix, String.class);
            ctxt.returnObjectBuffer(buffer);
            return result;
        }
    }
}