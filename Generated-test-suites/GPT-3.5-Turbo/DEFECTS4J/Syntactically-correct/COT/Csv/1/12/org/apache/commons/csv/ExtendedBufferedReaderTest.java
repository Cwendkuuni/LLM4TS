import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testReadAgain() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(null);
        assertEquals(ExtendedBufferedReader.UNDEFINED, reader.readAgain());
        
        reader.read(); // Read a character
        assertNotEquals(ExtendedBufferedReader.UNDEFINED, reader.readAgain());
        
        reader.close(); // Simulate end of stream
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }
}