import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import net.sf.javaml.core.*;
import java.util.*;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_noClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset expected = new DefaultDataset();
        expected.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, null));
        expected.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, null));

        Dataset result = ARFFHandler.loadARFF(file);

        assertEquals(expected, result);
    }

    @Test
    public void testLoadARFF_withClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 2;
        Dataset expected = new DefaultDataset();
        expected.add(new DenseInstance(new double[]{1.0, 2.0}, "class1"));
        expected.add(new DenseInstance(new double[]{3.0, 4.0}, "class2"));

        Dataset result = ARFFHandler.loadARFF(file, classIndex);

        assertEquals(expected, result);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_fileNotFound() throws FileNotFoundException {
        File file = new File("nonexistent.arff");

        ARFFHandler.loadARFF(file);
    }
}