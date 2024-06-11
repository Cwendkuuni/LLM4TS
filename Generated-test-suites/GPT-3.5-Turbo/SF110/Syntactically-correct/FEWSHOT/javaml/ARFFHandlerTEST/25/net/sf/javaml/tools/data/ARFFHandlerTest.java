import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_File() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset expected = new DefaultDataset();
        expected.add(new DenseInstance(new double[]{1.0, 2.0}, "A"));
        expected.add(new DenseInstance(new double[]{3.0, 4.0}, "B"));
        expected.add(new DenseInstance(new double[]{5.0, 6.0}, "C"));
        Dataset actual = ARFFHandler.loadARFF(file);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLoadARFF_FileClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset expected = new DefaultDataset();
        expected.add(new DenseInstance(new double[]{2.0}, "A"));
        expected.add(new DenseInstance(new double[]{4.0}, "B"));
        expected.add(new DenseInstance(new double[]{6.0}, "C"));
        Dataset actual = ARFFHandler.loadARFF(file, 1);
        assertEquals(expected, actual);
    }
}