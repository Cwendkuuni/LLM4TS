import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {
    @Test
    public void testConstructorWithSource() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        SourceExcerpt excerpt = new SourceExcerpt();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, excerpt);
        assertNotNull(formatter);
    }
    
    @Test
    public void testFormatErrorWithSource() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        JSError error = new JSError("Error description", "source.js", 10, CheckLevel.ERROR);
        
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String formattedMessage = formatter.formatError(error);
        
        String expectedMessage = "source.js:10: ERROR - Error description\n";
        assertEquals(expectedMessage, formattedMessage);
    }
}