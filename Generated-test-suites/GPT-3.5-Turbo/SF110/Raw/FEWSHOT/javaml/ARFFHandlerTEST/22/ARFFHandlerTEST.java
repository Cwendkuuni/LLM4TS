package net.sf.javaml.tools.data;

import static org.junit.Assert.assertEquals;

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
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, 1);
        assertEquals(0, dataset.size());
    }
}