package net.sf.javaml.tools.data;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import net.sf.javaml.core.Dataset;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_withFile() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
    }

    @Test
    public void testLoadARFF_withFileAndClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 1;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        assertNotNull(dataset);
    }
}