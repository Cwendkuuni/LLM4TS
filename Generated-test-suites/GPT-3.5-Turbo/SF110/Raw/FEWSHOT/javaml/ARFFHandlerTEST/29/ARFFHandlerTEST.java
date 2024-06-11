package net.sf.javaml.tools.data;

import static org.junit.Assert.*;
import org.junit.Test;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DenseInstance;
import java.io.File;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {
    @Test
    public void testLoadARFF() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 2;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFFileNotFound() throws FileNotFoundException {
        File file = new File("nonexistent.arff");
        ARFFHandler.loadARFF(file);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFWithClassIndexFileNotFound() throws FileNotFoundException {
        File file = new File("nonexistent.arff");
        int classIndex = 1;
        ARFFHandler.loadARFF(file, classIndex);
    }
}