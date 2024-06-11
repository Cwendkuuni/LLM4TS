package net.sf.javaml.tools.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DefaultDataset;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        File file = new File("testFile.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, 3);
        assertNotNull(dataset);
        assertEquals(5, dataset.size());
        assertEquals(4, dataset.noAttributes());
    }

    @Test
    public void testLoadARFFWithoutClassIndex() throws FileNotFoundException {
        File file = new File("testFile.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(5, dataset.size());
        assertEquals(5, dataset.noAttributes());
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFFileNotFound() throws FileNotFoundException {
        File file = new File("nonExistentFile.arff");
        ARFFHandler.loadARFF(file);
    }

    @Test
    public void testLoadARFFInvalidNumericValue() throws FileNotFoundException {
        File file = new File("invalidNumericValue.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(4, dataset.size());
        assertEquals(5, dataset.noAttributes());
        Instance instance = dataset.get(0);
        assertEquals(Double.NaN, instance.value(2), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadARFFNullFile() throws FileNotFoundException {
        ARFFHandler.loadARFF(null);
    }
}