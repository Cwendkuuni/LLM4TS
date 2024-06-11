
package net.sf.javaml.tools.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;

import org.junit.Test;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_file() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset result = ARFFHandler.loadARFF(file);
        
        assertNotNull(result);
        // Add assertions for specific dataset values if needed
    }
    
    @Test
    public void testLoadARFF_file_classIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 2; // Set class index
        
        Dataset result = ARFFHandler.loadARFF(file, classIndex);
        
        assertNotNull(result);
        // Add assertions for specific dataset values if needed
    }
    
}
