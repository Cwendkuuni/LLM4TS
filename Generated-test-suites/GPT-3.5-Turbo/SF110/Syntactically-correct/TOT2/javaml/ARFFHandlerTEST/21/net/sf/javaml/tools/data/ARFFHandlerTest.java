package net.sf.javaml.tools.data;
import static org.junit.Assert.*;
import org.junit.Test;
import net.sf.javaml.core.*;
import java.io.File;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {
    
    @Test
    public void testLoadARFF_basic() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testLoadARFF_classIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, 1);
        
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testLoadARFF_edgeCases() throws FileNotFoundException {
        // Testing with empty file
        File emptyFile = new File("empty.arff");
        Dataset emptyDataset = ARFFHandler.loadARFF(emptyFile);
        
        assertNotNull(emptyDataset);
        assertEquals(0, emptyDataset.size());
        
        // Testing with invalid class index
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, 10);
        
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }
}