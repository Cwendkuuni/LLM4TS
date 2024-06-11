
package net.sf.javaml.tools.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 0;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);

        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }
}
