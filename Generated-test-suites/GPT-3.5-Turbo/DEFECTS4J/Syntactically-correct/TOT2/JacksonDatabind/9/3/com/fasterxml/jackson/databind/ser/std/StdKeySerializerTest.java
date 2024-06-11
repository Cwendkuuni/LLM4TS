import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;

public class StdKeySerializerTest {
    
    private StdKeySerializer serializer;
    private JsonGenerator mockJsonGenerator;
    private SerializerProvider mockProvider;
    
    @Before
    public void setUp() {
        serializer = new StdKeySerializer();
        mockJsonGenerator = mock(JsonGenerator.class);
        mockProvider = mock(SerializerProvider.class);
    }
    
    @Test
    public void testSerialize_DateValue_Success() throws IOException {
        Date date = new Date();
        serializer.serialize(date, mockJsonGenerator, mockProvider);
        verify(mockProvider).defaultSerializeDateKey(date, mockJsonGenerator);
    }
    
    @Test
    public void testSerialize_NonDateValue_Success() throws IOException {
        String testValue = "test";
        serializer.serialize(testValue, mockJsonGenerator, mockProvider);
        verify(mockJsonGenerator).writeFieldName(testValue);
    }
    
    @Test
    public void testGetSchema_Success() throws JsonMappingException {
        assertEquals("string", serializer.getSchema(mockProvider, null).textValue());
    }

    @Test
    public void testAcceptJsonFormatVisitor_Success() throws JsonMappingException {
        JavaType mockTypeHint = mock(JavaType.class);
        serializer.acceptJsonFormatVisitor(mockVisitor, mockTypeHint);
        verify(mockVisitor).expectStringFormat(mockTypeHint);
    }
}