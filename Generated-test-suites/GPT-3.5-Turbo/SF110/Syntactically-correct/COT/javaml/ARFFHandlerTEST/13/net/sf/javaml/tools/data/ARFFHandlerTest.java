import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DenseInstance;

public class ARFFHandlerTest {
    
    @Test
    public void testLoadARFF_noClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset result = ARFFHandler.loadARFF(file);
        assertNotNull(result);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testLoadARFF_withClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 2;
        Dataset result = ARFFHandler.loadARFF(file, classIndex);
        assertNotNull(result);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testLoadARFF_missingFile() {
        File file = new File("missing.arff");
        try {
            Dataset result = ARFFHandler.loadARFF(file);
            fail("FileNotFoundException expected");
        } catch (FileNotFoundException e) {
            // FileNotFoundException expected
        }
    }

    @Test
    public void testLoadARFF_invalidData() throws FileNotFoundException {
        File file = new File("invalid.arff");
        try {
            Dataset result = ARFFHandler.loadARFF(file);
            fail("NumberFormatException expected");
        } catch (NumberFormatException e) {
            // NumberFormatException expected
        }
    }

    @Test
    public void testLoadARFF_emptyFile() throws FileNotFoundException {
        File file = new File("empty.arff");
        Dataset result = ARFFHandler.loadARFF(file);
        assertNotNull(result);
        assertEquals(0, result.size());
    }
    
    // Additional test cases for potential edge cases and exceptions
    
    @Test
    public void testLoadARFF_zeroLengthData() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 2;
        Dataset result = ARFFHandler.loadARFF(file, classIndex);
        assertNotNull(result);
        assertEquals(0, result.size());
    }
    
    @Test
    public void testLoadARFF_negativeClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = -1;
        Dataset result = ARFFHandler.loadARFF(file, classIndex);
        assertNotNull(result);
        assertEquals(0, result.size());
    }
    
}

