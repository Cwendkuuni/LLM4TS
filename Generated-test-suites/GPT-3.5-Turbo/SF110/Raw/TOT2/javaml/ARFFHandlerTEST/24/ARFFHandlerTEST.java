import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;
import net.sf.javaml.core.*;

public class ARFFHandlerTest {
    
    @Test
    public void testLoadARFF_noClassIndex() throws FileNotFoundException {
        File file = new File("testfile.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testLoadARFF_withClassIndex() throws FileNotFoundException {
        File file = new File("testfile.arff");
        int classIndex = 1;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testLoadARFF_invalidFile() {
        File file = new File("invalidfile.txt");
        try {
            ARFFHandler.loadARFF(file);
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            // expected
        }
    }
    
    @Test
    public void testLoadARFF_invalidClassIndex() {
        File file = new File("testfile.arff");
        int classIndex = -1;
        try {
            ARFFHandler.loadARFF(file, classIndex);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}