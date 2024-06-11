
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class DefaultPrettyPrinterTest {
    
    @Test
    public void testWriteRootValueSeparator() throws Exception {
        JsonGenerator g = mock(JsonGenerator.class);
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.writeRootValueSeparator(g);
        
        verify(g, times(1)).writeRaw(any(SerializedString.class));
    }
    
    @Test
    public void testWriteStartObject() throws Exception {
        JsonGenerator g = mock(JsonGenerator.class);
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.writeStartObject(g);
        
        verify(g, times(1)).writeRaw(anyString());
    }
    
    // Add more test methods for other public methods in DefaultPrettyPrinter class

}
