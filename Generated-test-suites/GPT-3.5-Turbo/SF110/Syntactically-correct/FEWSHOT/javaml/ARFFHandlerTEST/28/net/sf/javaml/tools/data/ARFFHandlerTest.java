package net.sf.javaml.tools.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFWithNonExistingFile() throws FileNotFoundException {
        File nonExistingFile = new File("nonExisting.arff");
        ARFFHandler.loadARFF(nonExistingFile);
    }

    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 1;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testLoadARFFWithNegativeClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = -1;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testLoadARFFWithData() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
        
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "A");
        dataset.add(instance1);
        assertEquals(1, dataset.size());
        assertTrue(dataset.contains(instance1));
        
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, "B");
        dataset.add(instance2);
        assertEquals(2, dataset.size());
        assertTrue(dataset.contains(instance2));
    }
}