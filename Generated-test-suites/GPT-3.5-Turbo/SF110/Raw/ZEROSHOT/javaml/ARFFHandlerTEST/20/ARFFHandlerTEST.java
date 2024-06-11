import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DenseInstance;
import be.abeel.util.LineIterator;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_noClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset expectedResult = new DefaultDataset();
        Dataset actualResult = ARFFHandler.loadARFF(file);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testLoadARFF_withClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 1;
        Dataset expectedResult = new DefaultDataset();
        Dataset actualResult = ARFFHandler.loadARFF(file, classIndex);
        assertEquals(expectedResult, actualResult);
    }
}