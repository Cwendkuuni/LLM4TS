import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_noClassIndex() {
        File file = new File("test.arff");
        Dataset dataset = null;
        try {
            dataset = ARFFHandler.loadARFF(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(dataset);
    }

    @Test
    public void testLoadARFF_withClassIndex() {
        File file = new File("test.arff");
        Dataset dataset = null;
        try {
            dataset = ARFFHandler.loadARFF(file, 0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(dataset);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_nonExistentFile() throws FileNotFoundException {
        File file = new File("nonexistent.arff");
        ARFFHandler.loadARFF(file);
    }

}