import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StdKeySerializerTest {

    @Mock
    private JsonGenerator jgen;

    @Mock
    private SerializerProvider provider;

    private StdKeySerializer serializer = new StdKeySerializer();

    @Test
    public void testSerializeWithDateValue() throws IOException {
        Date date = new Date();
        serializer.serialize(date, jgen, provider);
        verify(provider).defaultSerializeDateKey(date, jgen);
        verifyNoMoreInteractions(jgen, provider);
    }

    @Test
    public void testSerializeWithStringValue() throws IOException {
        String value = "test";
        serializer.serialize(value, jgen, provider);
        verify(jgen).writeFieldName(value);
        verifyNoMoreInteractions(jgen, provider);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        Type typeHint = String.class;
        JsonNode expectedSchema = TextNode.valueOf("string");
        JsonNode actualSchema = serializer.getSchema(provider, typeHint);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JavaType typeHint = mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor).expectStringFormat(typeHint);
        verifyNoMoreInteractions(visitor);
    }
}