package net.sf.javaml.tools.data;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DenseInstance;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        
        // Assert dataset is not null
        assertNotNull(dataset);
        
        // Assert dataset size is greater than 0
        assertTrue(dataset.size() > 0);
    }
    
    @Test
    public void testLoadARFF_withClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 2;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        
        // Assert dataset is not null
        assertNotNull(dataset);
        
        // Assert dataset size is greater than 0
        assertTrue(dataset.size() > 0);
        
        // Assert class index is set correctly
        for (Instance instance : dataset) {
            assertEquals(instance.classValue(), instance.value(classIndex));
        }
    }

}