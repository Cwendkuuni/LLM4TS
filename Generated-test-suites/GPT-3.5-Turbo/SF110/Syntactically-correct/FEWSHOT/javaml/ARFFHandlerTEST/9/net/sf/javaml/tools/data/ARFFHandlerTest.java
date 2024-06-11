package net.sf.javaml.tools.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

public class ARFFHandlerTest {
    
    @Test
    public void testLoadARFFFile() {
        try {
            File file = new File("test.arff");
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
            assertEquals(0, dataset.size());
            
            // Add your assertions here for the expected behavior of loading the file
            // Make sure to test with different ARFF files
            
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }
    
    @Test
    public void testLoadARFFFileWithClassIndex() {
        try {
            File file = new File("test.arff");
            int classIndex = 2; // Set the class index here
            Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
            assertNotNull(dataset);
            assertEquals(0, dataset.size());
            
            // Add your assertions here for the expected behavior of loading the file with class index
            // Make sure to test with different ARFF files and class indexes
            
        } catch (FileNotFoundException e) {
            fail("File not found");
        }
    }
    
    @Test
    public void testLoadARFFFileInvalidFile() {
        try {
            File file = new File("invalid.arff");
            Dataset dataset = ARFFHandler.loadARFF(file);
            fail("Expected FileNotFoundException to be thrown");
        } catch (FileNotFoundException e) {
            assertTrue(true);
        }
    }
}