import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

public class ARFFHandlerTest {
    
    @Test
    public void testLoadARFF_fileOnly() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        
        assertNotNull(dataset);
        assertTrue(dataset.isEmpty());
    }
    
    @Test
    public void testLoadARFF_fileAndClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 2;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        
        assertNotNull(dataset);
        assertTrue(dataset.isEmpty());
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_nonexistentFile() throws FileNotFoundException {
        File file = new File("nonexistent.arff");
        ARFFHandler.loadARFF(file);
    }
    
    @Test
    public void testLoadARFF_dataModeFalse() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        
        assertNotNull(dataset);
        assertTrue(dataset.isEmpty());
    }
    
    @Test
    public void testLoadARFF_dataModeTrue() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 2;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        
        assertNotNull(dataset);
        assertTrue(dataset.isEmpty());
    }
    
    // Additional test cases can be added here to further cover edge cases
    
}