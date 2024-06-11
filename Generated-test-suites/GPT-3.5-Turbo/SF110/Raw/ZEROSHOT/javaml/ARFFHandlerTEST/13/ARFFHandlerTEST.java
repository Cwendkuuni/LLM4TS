package net.sf.javaml.tools.data;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DenseInstance;
import be.abeel.util.LineIterator;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() {
        try {
            File file = new File("test.arff");
            Dataset dataset = ARFFHandler.loadARFF(file);

            assertNotNull(dataset);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        }
    }

    @Test
    public void testLoadARFFWithClassIndex() {
        try {
            File file = new File("test.arff");
            int classIndex = 0;
            Dataset dataset = ARFFHandler.loadARFF(file, classIndex);

            assertNotNull(dataset);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        }
    }

}