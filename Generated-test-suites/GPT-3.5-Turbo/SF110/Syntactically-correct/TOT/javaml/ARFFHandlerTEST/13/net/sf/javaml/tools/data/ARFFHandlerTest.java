import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import net.sf.javaml.core.*;

public class ARFFHandlerTest {
    
    @Test
    public void testLoadARFF_noClassIndex() {
        File file = new File("testData.arff");
        Dataset expected = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, "A");
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, "B");
        expected.add(instance1);
        expected.add(instance2);
        
        try {
            Dataset result = ARFFHandler.loadARFF(file);
            assertEquals(expected.size(), result.size());
            for (int i = 0; i < expected.size(); i++) {
                assertArrayEquals(expected.get(i).values(), result.get(i).values(), 0.00001);
                assertEquals(expected.get(i).classValue(), result.get(i).classValue());
            }
        } catch (FileNotFoundException e) {
            fail("File not found exception should not be thrown.");
        }
    }
    
    @Test
    public void testLoadARFF_withClassIndex() {
        File file = new File("testData.arff");
        int classIndex = 2;
        Dataset expected = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, "A");
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0}, "B");
        expected.add(instance1);
        expected.add(instance2);
        
        try {
            Dataset result = ARFFHandler.loadARFF(file, classIndex);
            assertEquals(expected.size(), result.size());
            for (int i = 0; i < expected.size(); i++) {
                assertArrayEquals(expected.get(i).values(), result.get(i).values(), 0.00001);
                assertEquals(expected.get(i).classValue(), result.get(i).classValue());
            }
        } catch (FileNotFoundException e) {
            fail("File not found exception should not be thrown.");
        }
    }
    
    @Test
    public void testLoadARFF_fileNotFound() {
        File file = new File("nonexistentFile.arff");
        
        try {
            ARFFHandler.loadARFF(file);
            fail("FileNotFoundException should be thrown.");
        } catch (FileNotFoundException e) {
            // Exception is expected
        }
    }
    
    @Test
    public void testLoadARFF_invalidDataValue() {
        File file = new File("testData.arff");
        int classIndex = -1;
        
        try {
            Dataset result = ARFFHandler.loadARFF(file, classIndex);
            assertEquals(2, result.size());
            assertEquals(Double.NaN, result.get(0).get(2));
            assertEquals(Double.NaN, result.get(1).get(2));
        } catch (FileNotFoundException e) {
            fail("File not found exception should not be thrown.");
        }
    }
    
    @Test
    public void testLoadARFF_emptyFile() {
        File file = new File("emptyData.arff");
        
        try {
            Dataset result = ARFFHandler.loadARFF(file);
            assertEquals(0, result.size());
        } catch (FileNotFoundException e) {
            fail("File not found exception should not be thrown.");
        }
    }
}