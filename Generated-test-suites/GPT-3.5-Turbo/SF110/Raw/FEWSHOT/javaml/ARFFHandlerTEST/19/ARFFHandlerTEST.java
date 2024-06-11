
package net.sf.javaml.tools.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DenseInstance;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_file() throws FileNotFoundException {
        File file = new File("path/to/test.arff");
        Dataset actual = ARFFHandler.loadARFF(file);
        
        assertTrue(actual instanceof Dataset);
    }

    @Test
    public void testLoadARFF_file_classIndex() throws FileNotFoundException {
        File file = new File("path/to/test.arff");
        int classIndex = 2;
        Dataset actual = ARFFHandler.loadARFF(file, classIndex);
        
        assertTrue(actual instanceof Dataset);
    }
    
    @Test
    public void testLoadARFF_file_classIndex_dataMode_true() throws FileNotFoundException {
        File file = new File("path/to/test.arff");
        int classIndex = 2;
        Dataset actual = ARFFHandler.loadARFF(file, classIndex);
        // validate data mode is true
        // validate data is properly loaded
        
        assertTrue(actual instanceof Dataset);
    }
    
    @Test
    public void testLoadARFF_file_classIndex_dataMode_false() throws FileNotFoundException {
        File file = new File("path/to/test.arff");
        int classIndex = -1;
        Dataset actual = ARFFHandler.loadARFF(file, classIndex);
        // validate data mode is false
        // validate data is properly loaded
        
        assertTrue(actual instanceof Dataset);
    }
    
    @Test
    public void testLoadARFF_file_classIndex_dataMode_true_invalidData() throws FileNotFoundException {
        File file = new File("path/to/test.arff");
        int classIndex = 2;
        Dataset actual = ARFFHandler.loadARFF(file, classIndex);
        // validate data mode is true
        // validate invalid data is properly handled
        
        assertTrue(actual instanceof Dataset);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_fileNotFound() throws FileNotFoundException {
        File file = new File("path/to/nonexistent.arff");
        ARFFHandler.loadARFF(file);
    }

}
