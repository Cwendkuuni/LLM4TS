
package net.sf.javaml.tools.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_File() throws FileNotFoundException {
        File file = new File("path/to/arff/file.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);

        assertNotNull(dataset);
        assertTrue(dataset.isEmpty());
    }

    @Test
    public void testLoadARFF_FileInt() throws FileNotFoundException {
        File file = new File("path/to/arff/file.arff");
        int classIndex = 2;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);

        assertNotNull(dataset);
        assertTrue(dataset.isEmpty());
    }

    @Test
    public void testLoadARFF_ClassValueIsDouble() throws FileNotFoundException {
        File file = new File("path/to/arff/file.arff");
        int classIndex = 2;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);

        for (Instance instance : dataset) {
            double classValue = instance.classValue();
            assertTrue(Double.isNaN(classValue));
        }
    }

    @Test
    public void testLoadARFF_CheckValues() throws FileNotFoundException {
        File file = new File("path/to/arff/file.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);

        for (Instance instance : dataset) {
            double[] values = instance.values();
            assertNotNull(values);
            assertEquals(0, values.length);
        }
    }
}
