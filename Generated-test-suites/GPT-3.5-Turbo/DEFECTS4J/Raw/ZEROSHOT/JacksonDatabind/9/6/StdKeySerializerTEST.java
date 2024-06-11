
import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {

    private StdKeySerializer stdKeySerializer;

    @Before
    public void setUp() {
        stdKeySerializer = new StdKeySerializer();
    }

    @After
    public void tearDown() {
        stdKeySerializer = null;
    }

    @Test
    public void testSerialize_DateValue_CallsDefaultSerializeDateKey() throws IOException {
        // Arrange
        Date date = new Date();
        JsonGenerator jgen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        // Act
        stdKeySerializer.serialize(date, jgen, provider);

        // Assert
        Mockito.verify(provider).defaultSerializeDateKey(Mockito.eq(date), Mockito.eq(jgen));
    }

    @Test
    public void testSerialize_NonDateValue_CallsWriteFieldNameWithValueToString() throws IOException {
        // Arrange
        Object value = new Object();
        JsonGenerator jgen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        // Act
        stdKeySerializer.serialize(value, jgen, provider);

        // Assert
        Mockito.verify(jgen).writeFieldName(Mockito.eq(value.toString()));
    }

    @Test
    public void testGetSchema_ReturnsStringSchemaNode() throws JsonMappingException {
        // Arrange
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Mockito.mock(Type.class);

        // Act
        JsonNode schemaNode = stdKeySerializer.getSchema(provider, typeHint);

        // Assert
        assertEquals("string", schemaNode.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_ExpectStringFormatCalled() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);

        // Act
        stdKeySerializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        Mockito.verify(visitor).expectStringFormat(Mockito.eq(typeHint));
    }
}
