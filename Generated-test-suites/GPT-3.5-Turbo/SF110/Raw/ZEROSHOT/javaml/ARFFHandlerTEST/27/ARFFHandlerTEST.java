import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;

public class ARFFHandlerTest {

    private static final String ARFF_FILE_PATH = "path/to/arff/file.arff";
    private static final String NON_EXISTING_FILE_PATH = "path/to/non/existing/file.arff";

    private File arffFile;

    @Before
    public void setUp() {
        arffFile = new File(ARFF_FILE_PATH);
    }

    @Test
    public void testLoadARFF() {
        try {
            Dataset dataset = ARFFHandler.loadARFF(arffFile);
            
            assertNotNull(dataset);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail("Unexpected FileNotFoundException");
        }
    }

    @Test
    public void testLoadARFFWithClassIndex() {
        int classIndex = 2;

        try {
            Dataset dataset = ARFFHandler.loadARFF(arffFile, classIndex);

            assertNotNull(dataset);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            fail("Unexpected FileNotFoundException");
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFWithNonExistingFile() throws FileNotFoundException {
        ARFFHandler.loadARFF(new File(NON_EXISTING_FILE_PATH));
    }
}