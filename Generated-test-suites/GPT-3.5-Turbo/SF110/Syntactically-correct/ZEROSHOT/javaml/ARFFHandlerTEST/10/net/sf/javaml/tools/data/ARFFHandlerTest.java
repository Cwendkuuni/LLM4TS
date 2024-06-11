package net.sf.javaml.tools.data;

import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DenseInstance;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            Assert.assertNotNull(dataset);
        } catch (FileNotFoundException e) {
            Assert.fail("FileNotFoundException should not be thrown");
        }
    }

    @Test
    public void testLoadARFFWithClassIndex() {
        File file = new File("test.arff");
        try {
            int classIndex = 2;
            Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
            Assert.assertNotNull(dataset);
        } catch (FileNotFoundException e) {
            Assert.fail("FileNotFoundException should not be thrown");
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFWithNonExistingFile() throws FileNotFoundException {
        File file = new File("non_existing.arff");
        ARFFHandler.loadARFF(file);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFWithClassIndexAndNonExistingFile() throws FileNotFoundException {
        File file = new File("non_existing.arff");
        int classIndex = 2;
        ARFFHandler.loadARFF(file, classIndex);
    }
}