import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

public class StdKeySerializerTest {

    @Test
    public void testSerialize_DateValue_ShouldCallDefaultSerializeDateKey() throws IOException{
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator mockGenerator = new MockJsonGenerator();
        SerializerProvider mockProvider = new MockSerializerProvider();
        Date date = new Date();

        // Act
        serializer.serialize(date, mockGenerator, mockProvider);

        // Assert
        Assert.assertTrue(((MockSerializerProvider) mockProvider).getIsDefaultSerializeDateKeyCalled());
    }

    @Test
    public void testSerialize_OtherValue_ShouldWriteFieldNameWithStringValue() throws IOException {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator mockGenerator = new MockJsonGenerator();
        SerializerProvider mockProvider = new MockSerializerProvider();
        Object value = "test value";

        // Act
        serializer.serialize(value, mockGenerator, mockProvider);

        // Assert
        Assert.assertEquals("test value", ((MockJsonGenerator) mockGenerator).getWrittenFieldName());
    }

    @Test
    public void testGetSchema_ShouldReturnStringSchemaNode() throws JsonMappingException {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider mockProvider = new MockSerializerProvider();
        Type typeHint = new Type() {};

        // Act
        JsonNode schemaNode = serializer.getSchema(mockProvider, typeHint);

        // Assert
        Assert.assertEquals("string", schemaNode.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_ShouldExpectStringFormat() throws JsonMappingException {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper mockVisitor = new MockJsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType() {};

        // Act
        serializer.acceptJsonFormatVisitor(mockVisitor, typeHint);

        // Assert
        Assert.assertTrue(((MockJsonFormatVisitorWrapper) mockVisitor).getIsExpectStringFormatCalled());
    }
}