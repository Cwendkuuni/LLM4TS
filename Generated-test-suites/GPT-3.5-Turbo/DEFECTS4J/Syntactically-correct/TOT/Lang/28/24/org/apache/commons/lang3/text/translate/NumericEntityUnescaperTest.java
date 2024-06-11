import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.Writer;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_WhenInputContainsNumericEntity_ReturnsCorrectCodepoint() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder stringBuilder = new StringBuilder();
        Writer writer = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                stringBuilder.append(cbuf, off, len);
            }
            @Override
            public void flush() throws IOException {
                // not implemented
            }
            @Override
            public void close() throws IOException {
                // not implemented
            }
        };
        
        int result = unescaper.translate("&#65;", 0, writer);
        
        assertEquals(1, result);
        assertEquals("A", stringBuilder.toString());
    }
    
    @Test
    public void testTranslate_WhenInputContainsHexadecimalNumericEntity_ReturnsCorrectCodepoint() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder stringBuilder = new StringBuilder();
        Writer writer = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                stringBuilder.append(cbuf, off, len);
            }
            @Override
            public void flush() throws IOException {
                // not implemented
            }
            @Override
            public void close() throws IOException {
                // not implemented
            }
        };
        
        int result = unescaper.translate("&#x41;", 0, writer);
        
        assertEquals(1, result);
        assertEquals("A", stringBuilder.toString());
    }
    
    @Test
    public void testTranslate_WhenInputDoesNotContainNumericEntity_ReturnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder stringBuilder = new StringBuilder();
        Writer writer = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                stringBuilder.append(cbuf, off, len);
            }
            @Override
            public void flush() throws IOException {
                // not implemented
            }
            @Override
            public void close() throws IOException {
                // not implemented
            }
        };
        
        int result = unescaper.translate("ABC", 0, writer);
        
        assertEquals(0, result);
        assertEquals("", stringBuilder.toString());
    }
    
    @Test
    public void testTranslate_WhenInputContainsInvalidNumericEntity_ReturnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder stringBuilder = new StringBuilder();
        Writer writer = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                stringBuilder.append(cbuf, off, len);
            }
            @Override
            public void flush() throws IOException {
                // not implemented
            }
            @Override
            public void close() throws IOException {
                // not implemented
            }
        };
        
        int result = unescaper.translate("&#invalid;", 0, writer);
        
        assertEquals(0, result);
        assertEquals("", stringBuilder.toString());
    }
}