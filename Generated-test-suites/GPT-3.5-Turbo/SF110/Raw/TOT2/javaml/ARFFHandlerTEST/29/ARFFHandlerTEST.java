import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import org.junit.Test;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() throws FileNotFoundException {
        File file = new File("data.arff");
        assertNotNull(ARFFHandler.loadARFF(file));
    }
    
    @Test
    public void testLoadARFFClassIndex() throws FileNotFoundException {
        File file = new File("data.arff");
        assertNotNull(ARFFHandler.loadARFF(file, 1));
    }
    
    @Test
    public void testLoadARFFClassIndexNegative() throws FileNotFoundException {
        File file = new File("data.arff");
        assertEquals(0,ARFFHandler.loadARFF(file, -1).size()); //Verify no data with negative class index
    }
    
    @Test
    public void testLoadARFFInvalidFile() {
        File file = new File("invalid.arff");
        try {
            ARFFHandler.loadARFF(file);
            fail("FileNotFoundException expected");
        } catch (FileNotFoundException e) {
            assertTrue(true);
        }
    }
}