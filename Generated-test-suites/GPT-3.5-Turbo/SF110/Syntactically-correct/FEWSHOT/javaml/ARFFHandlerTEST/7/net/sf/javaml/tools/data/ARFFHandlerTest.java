
package net.sf.javaml.tools.data;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Dataset;
import be.abeel.util.LineIterator;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_noClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset expected = new DefaultDataset();
        expected.add(new DenseInstance(new double[]{1.0, 2.0}));
        expected.add(new DenseInstance(new double[]{3.0, 4.0}));
        expected.add(new DenseInstance(new double[]{5.0, 6.0}));

        Dataset result = ARFFHandler.loadARFF(file);

        assertEquals(expected, result);
    }

    @Test
    public void testLoadARFF_withClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 2;
        Dataset expected = new DefaultDataset();
        expected.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}));
        expected.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}));
        expected.add(new DenseInstance(new double[]{7.0, 8.0, 9.0}));

        Dataset result = ARFFHandler.loadARFF(file, classIndex);

        assertEquals(expected, result);
    }
}
