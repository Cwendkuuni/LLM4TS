import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {
    
    @Test
    public void testRead() throws IOException {
        // Verify read method functionality
        StringReader reader = new StringReader("Test");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        
        int ch = extendedReader.read();
        
        assertEquals('T', ch);
    }
    
    @Test
    public void testReadAgain() throws IOException {
        // Verify readAgain method functionality
        StringReader reader = new StringReader("Test");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        
        extendedReader.read();
        int lastChar = extendedReader.readAgain();
        
        assertEquals('T', lastChar);
    }
    
    @Test
    public void testReadLine() throws IOException {
        // Verify readLine method functionality
        StringReader reader = new StringReader("Test\nLine\n");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        
        String line1 = extendedReader.readLine();
        String line2 = extendedReader.readLine();
        
        assertEquals("Test", line1);
        assertEquals("Line", line2);
    }
    
    @Test
    public void testLookAhead() throws IOException {
        // Verify lookAhead method functionality
        StringReader reader = new StringReader("Test");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        
        int nextChar = extendedReader.lookAhead();
        
        assertEquals('T', nextChar);
    }
    
    @Test
    public void testGetLineNumber() {
        // Verify getLineNumber method functionality
        StringReader reader = new StringReader("Test\nLine\n");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        
        extendedReader.readLine();
        
        assertEquals(1, extendedReader.getLineNumber());
    }
    
    @Test(expected = IOException.class)
    public void testIOExceptionRead() throws IOException {
        // Verify IOException is thrown for read method
        StringReader reader = new StringReader("Test");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);
        
        reader.close();
        extendedReader.read();
    }

}