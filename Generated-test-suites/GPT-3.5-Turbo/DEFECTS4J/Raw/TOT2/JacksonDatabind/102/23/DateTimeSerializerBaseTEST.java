import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.mockito.Mockito;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateTimeSerializerBaseTest {
    
    @Test
    public void testWithContextual() {
        DateTimeSerializerBase serializer = Mockito.mock(DateTimeSerializerBase.class);

        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        DateTimeSerializerBase result = serializer.withFormat(true, dateFormat);
        
        assertNotNull(result);
        assertEquals(Boolean.TRUE, result._useTimestamp);
        assertEquals(dateFormat, result._customFormat);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase serializer = Mockito.mock(DateTimeSerializerBase.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        Date date = new Date();
        
        boolean result = serializer.isEmpty(serializers, date);
        
        assertFalse(result);
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase serializer = Mockito.mock(DateTimeSerializerBase.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        JsonNode schema = serializer.getSchema(serializers, Date.class);
        
        assertNotNull(schema);
        assertTrue(schema.isObject());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        DateTimeSerializerBase serializer = Mockito.mock(DateTimeSerializerBase.class);
        Mockito.when(serializer.handledType()).thenReturn(Date.class);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        serializer.acceptJsonFormatVisitor(visitor, ObjectMapper.constructType(Date.class));
        
        // Verify visit to correct format
        Mockito.verify(visitor, Mockito.times(1)).visitStringFormat(Mockito.any(), Mockito.eq(ObjectMapper.constructType(Date.class)), Mockito.eq(JsonValueFormat.DATE_TIME));
    }
    
    @Test
    public void testSerialize() {
        DateTimeSerializerBase serializer = Mockito.mock(DateTimeSerializerBase.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        Date date = new Date();
        
        serializer.serialize(date, jsonGenerator, serializers);
        
        // Verifying serializer called correctly
        Mockito.verify(serializer, Mockito.times(1))._serializeAsString(date, jsonGenerator, serializers);
    }
}