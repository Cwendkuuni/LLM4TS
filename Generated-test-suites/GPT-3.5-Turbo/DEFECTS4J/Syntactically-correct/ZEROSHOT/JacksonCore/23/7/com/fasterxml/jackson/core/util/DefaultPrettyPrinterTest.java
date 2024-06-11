import org.junit.Test;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import static org.mockito.Mockito.*;

public class DefaultPrettyPrinterTest {
    
    @Test
    public void testWriteRootValueSeparator() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.writeRootValueSeparator(jsonGenerator);
        
        verify(jsonGenerator).writeRaw(anyString());
    }
    
    @Test
    public void testWriteStartObject() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.writeStartObject(jsonGenerator);
        
        verify(jsonGenerator).writeRaw(anyChar());
    }
    
    @Test
    public void testBeforeObjectEntries() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.beforeObjectEntries(jsonGenerator);
        
        verify(jsonGenerator).writeRaw(anyString());
    }
    
    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        
        verify(jsonGenerator).writeRaw(anyString());
    }
    
    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        
        verify(jsonGenerator).writeRaw(anyString());
    }
    
    @Test
    public void testWriteEndObject() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.writeEndObject(jsonGenerator, 0);
        
        verify(jsonGenerator).writeRaw(anyChar());
    }
    
    @Test
    public void testWriteStartArray() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.writeStartArray(jsonGenerator);
        
        verify(jsonGenerator).writeRaw(anyChar());
    }
    
    @Test
    public void testBeforeArrayValues() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.beforeArrayValues(jsonGenerator);
        
        verify(jsonGenerator).writeRaw(anyString());
    }
    
    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.writeArrayValueSeparator(jsonGenerator);
        
        verify(jsonGenerator).writeRaw(anyString());
    }
    
    @Test
    public void testWriteEndArray() throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.writeEndArray(jsonGenerator, 0);
        
        verify(jsonGenerator).writeRaw(anyChar());
    }
}