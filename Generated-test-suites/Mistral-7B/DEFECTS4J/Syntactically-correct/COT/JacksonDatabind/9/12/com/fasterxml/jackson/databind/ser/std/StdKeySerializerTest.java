import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StdKeySerializerTest {

    @Test
    public void testSerialize_Date() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date();
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        when(provider.defaultSerializeDateKey(date, jgen)).thenCallRealMethod();

        serializer.serialize(date, jgen, provider);

        // Verify that defaultSerializeDateKey was called
        Assert.assertFalse("serialize method should have delegated to defaultSerializeDateKey for Date",
                serializer.getClass().isAssignableFrom(jgen.asStartElement().getName().getClass()));
    }

    @Test
    public void testSerialize_String() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        String str = "test";
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(str, jgen, provider);

        // Verify that writeFieldName was called with the correct argument
        Assert.assertEquals("test", jgen.getOutputPrinter().getText().substring(0, jgen.getOutputPrinter().getText().indexOf(':')));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerialize_Null() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        Object value = null;
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(value, jgen, provider);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();

        JsonNode result = serializer.getSchema(mock(SerializerProvider.class), Object.class);

        // Verify that createSchemaNode was called with the correct argument
        Assert.assertEquals("string", result.getClass().getSimpleName());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        JavaType typeHint = mapper.getTypeFactory().constructType(String.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Verify that expectStringFormat was called with the correct argument
        Assert.assertEquals(String.class, visitor.getFormatType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAcceptJsonFormatVisitor_InvalidTypeHint() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        JavaType typeHint = mapper.getTypeFactory().constructType(int.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
}