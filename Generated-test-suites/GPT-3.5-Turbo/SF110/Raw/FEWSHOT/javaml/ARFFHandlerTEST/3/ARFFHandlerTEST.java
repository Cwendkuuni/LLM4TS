import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.ARFFHandler;

public class ARFFHandlerTest {
    
    @Test
    public void testLoadARFF() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
    }
    
    @Test
    public void testLoadARFF_withClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 3;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        assertNotNull(dataset);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_fileNotFound() throws FileNotFoundException {
        File file = new File("nonexistent.arff");
        ARFFHandler.loadARFF(file);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_fileNotFound_withClassIndex() throws FileNotFoundException {
        File file = new File("nonexistent.arff");
        int classIndex = 3;
        ARFFHandler.loadARFF(file, classIndex);
    }
    
    @Test
    public void testLoadARFF_invalidData() throws FileNotFoundException {
        File file = new File("invalid.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }
    
    // Add more test cases to cover different scenarios
    
}