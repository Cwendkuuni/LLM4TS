import org.junit.Test;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;
import com.fasterxml.jackson.databind.JsonGenerator;

import java.io.IOException;
import java.util.Date;

import static org.mockito.Mockito.*;

public class StdKeySerializerTest {

    private StdKeySerializer stdKeySerializer = new StdKeySerializer();

    @Test
    public void testSerialize_DateValue_Success() throws IOException {
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonGenerator jgen = mock(JsonGenerator.class);
        Date date = new Date();

        stdKeySerializer.serialize(date, jgen, provider);

        verify(provider).defaultSerializeDateKey(date, jgen);
        verifyNoMoreInteractions(provider, jgen);
    }

    @Test
    public void testSerialize_ObjectValue_Success() throws IOException {
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonGenerator jgen = mock(JsonGenerator.class);
        Object obj = new Object();

        stdKeySerializer.serialize(obj, jgen, provider);
        
        verify(jgen).writeFieldName(obj.toString());
        verifyNoMoreInteractions(provider, jgen);
    }

    @Test
    public void testGetSchema_Success() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);

        stdKeySerializer.getSchema(provider, typeHint);

        verify(provider).createSchemaNode("string");
        verifyNoMoreInteractions(provider);
    }

    @Test
    public void testAcceptJsonFormatVisitor_Success() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        stdKeySerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectStringFormat(typeHint);
        verifyNoMoreInteractions(visitor);
    }
}