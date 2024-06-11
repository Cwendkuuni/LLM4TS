import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class StdKeySerializerTest {

    private StdKeySerializer stdKeySerializer;

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private Date date;

    @Mock
    private JsonFormatVisitorWrapper jsonFormatVisitorWrapper;

    @Mock
    private JavaType javaType;

    @Before
    public void setup() {
        stdKeySerializer = new StdKeySerializer();
    }

    @Test
    public void testSerialize_DateValue_DateSerialized() throws IOException {
        // Arrange
        when(date.toString()).thenReturn("2019-01-01");

        // Act
        stdKeySerializer.serialize(date, jsonGenerator, serializerProvider);

        // Assert
        verify(serializerProvider, times(1)).defaultSerializeDateKey(date, jsonGenerator);
        verifyZeroInteractions(jsonGenerator);
    }

    @Test
    public void testSerialize_NonDateValue_ValueSerializedAsString() throws IOException {
        // Arrange
        String expectedValue = "test";
        when(date.toString()).thenReturn(expectedValue);

        // Act
        stdKeySerializer.serialize(date, jsonGenerator, serializerProvider);

        // Assert
        verifyZeroInteractions(serializerProvider);
        verify(jsonGenerator, times(1)).writeFieldName(expectedValue);
    }

    @Test
    public void testGetSchema_ReturnsStringSchemaNode() throws JsonMappingException {
        // Arrange

        // Act
        JsonNode schemaNode = stdKeySerializer.getSchema(serializerProvider, null);

        // Assert
        assertEquals("string", schemaNode.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_VisitorExpectStringFormatCalled() throws JsonMappingException {
        // Arrange

        // Act
        stdKeySerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType);

        // Assert
        verify(jsonFormatVisitorWrapper, times(1)).expectStringFormat(javaType);
    }
}