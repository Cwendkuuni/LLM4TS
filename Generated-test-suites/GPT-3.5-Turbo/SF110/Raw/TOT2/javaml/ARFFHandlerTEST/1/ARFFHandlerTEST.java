package net.sf.javaml.tools.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF1() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            
            // Test dataset is not null
            assertNotNull(dataset);
            
            // Test dataset contains instances
            assertTrue(dataset.size() > 0);
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        }
    }
    
    @Test
    public void testLoadARFF2() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, 1);
            
            // Test dataset is not null
            assertNotNull(dataset);
            
            // Test dataset contains instances
            assertTrue(dataset.size() > 0);
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        }
    }
    
    @Test
    public void testLoadARFF3() {
        File file = new File("empty.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            
            // Test dataset is not null
            assertNotNull(dataset);
            
            // Test dataset is empty
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        }
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFException() throws FileNotFoundException {
        File file = new File("nonexistent_file.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
    }
    
}