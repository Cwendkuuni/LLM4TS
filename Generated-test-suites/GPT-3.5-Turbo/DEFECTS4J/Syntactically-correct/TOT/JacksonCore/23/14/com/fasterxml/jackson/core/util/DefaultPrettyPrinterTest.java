import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter prettyPrinter;
    private JsonGenerator jsonGeneratorMock;
    private ArgumentCaptor<String> stringArgumentCaptor; // used to capture the output string
    
    @Before
    public void setUp() {
        prettyPrinter = new DefaultPrettyPrinter();
        jsonGeneratorMock = mock(JsonGenerator.class);
        stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
    }
    
    @Test
    public void testWriteRootValueSeparator() throws IOException {
        prettyPrinter.writeRootValueSeparator(jsonGeneratorMock);
        
        verify(jsonGeneratorMock).writeRaw(any(SerializedString.class));
    }
    
    @Test
    public void testWriteStartObject() throws IOException {
        prettyPrinter.writeStartObject(jsonGeneratorMock);
        
        verify(jsonGeneratorMock).writeRaw(stringArgumentCaptor.capture());
        assertEquals("{", stringArgumentCaptor.getValue());
        
        verify(jsonGeneratorMock).writeRaw(any(String.class));
    }
    
    // Add more test cases for other methods
    
    @Test
    public void testWriteEndObject() throws IOException {
        prettyPrinter.writeEndObject(jsonGeneratorMock, 2);
        
        verify(jsonGeneratorMock, times(2)).writeRaw(any(String.class));
    }
    
    @Test
    public void testWriteStartArray() throws IOException {
        prettyPrinter.writeStartArray(jsonGeneratorMock);
        
        verify(jsonGeneratorMock).writeRaw(stringArgumentCaptor.capture());
        assertEquals("[", stringArgumentCaptor.getValue());
        
        verify(jsonGeneratorMock).writeRaw(any(String.class));
    }
    
    @Test
    public void testWriteEndArray() throws IOException {
        prettyPrinter.writeEndArray(jsonGeneratorMock, 3);
        
        verify(jsonGeneratorMock, times(2)).writeRaw(any(String.class));
    }
    
    // Add more test cases for other methods

}