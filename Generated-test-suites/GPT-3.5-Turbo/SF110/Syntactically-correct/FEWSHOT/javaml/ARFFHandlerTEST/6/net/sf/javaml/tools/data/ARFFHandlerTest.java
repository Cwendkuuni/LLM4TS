package net.sf.javaml.tools.data;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

public class ARFFHandlerTest {
    
    @Test
    public void testLoadARFF() throws FileNotFoundException {
        File file = new File("data.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        
        // assert statements
        assertEquals(5, dataset.size());
    }
    
    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        File file = new File("data.arff");
        int classIndex = 2;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        
        // assert statements
        assertEquals(5, dataset.size());
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFFileNotFound() throws FileNotFoundException {
        File file = new File("nonexistent.arff");
        ARFFHandler.loadARFF(file);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFWithClassIndexFileNotFound() throws FileNotFoundException {
        File file = new File("nonexistent.arff");
        int classIndex = 2;
        ARFFHandler.loadARFF(file, classIndex);
    }
}