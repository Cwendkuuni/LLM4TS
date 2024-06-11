import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        serializer.serialize(date, jsonGenerator, provider);
        
        verify(provider, times(1)).defaultSerializeDateKey(date, jsonGenerator);
    }
    
    @Test
    public void testSerializeString() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        String value = "test";
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        
        serializer.serialize(value, jsonGenerator, provider);
        
        verify(jsonGenerator, times(1)).writeFieldName(value);
    }
    
    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = Date.class;
        
        assertEquals("string", serializer.getSchema(provider, typeHint).asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor, times(1)).expectStringFormat(typeHint);
    }
}