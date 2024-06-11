import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.*;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_file_noClassIndex() throws FileNotFoundException {
        File file = new File("testFile.arff");
        Dataset expectedDataset = new DefaultDataset();
        expectedDataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, null));
        expectedDataset.add(new DenseInstance(new double[]{4.0, 5.0, 6.0}, null));
        
        Dataset actualDataset = ARFFHandler.loadARFF(file);
        
        assertEquals(expectedDataset.size(), actualDataset.size());
        assertEquals(expectedDataset.get(0).classValue(), actualDataset.get(0).classValue());
        assertArrayEquals(expectedDataset.get(0).values(), actualDataset.get(0).values(), 0.001);
        assertEquals(expectedDataset.get(1).classValue(), actualDataset.get(1).classValue());
        assertArrayEquals(expectedDataset.get(1).values(), actualDataset.get(1).values(), 0.001);
    }

    @Test
    public void testLoadARFF_file_withClassIndex() throws FileNotFoundException {
        File file = new File("testFile.arff");
        int classIndex = 2;
        Dataset expectedDataset = new DefaultDataset();
        expectedDataset.add(new DenseInstance(new double[]{1.0, 2.0}, "3.0"));
        expectedDataset.add(new DenseInstance(new double[]{4.0, 5.0}, "6.0"));
        
        Dataset actualDataset = ARFFHandler.loadARFF(file, classIndex);
        
        assertEquals(expectedDataset.size(), actualDataset.size());
        assertEquals(expectedDataset.get(0).classValue(), actualDataset.get(0).classValue());
        assertArrayEquals(expectedDataset.get(0).values(), actualDataset.get(0).values(), 0.001);
        assertEquals(expectedDataset.get(1).classValue(), actualDataset.get(1).classValue());
        assertArrayEquals(expectedDataset.get(1).values(), actualDataset.get(1).values(), 0.001);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_fileNotFoundException() throws FileNotFoundException {
        File file = new File("nonExistentFile.arff");
        
        ARFFHandler.loadARFF(file);
    }

    @Test
    public void testLoadARFF_emptyFile() throws FileNotFoundException {
        File file = new File("emptyFile.arff");
        Dataset expectedDataset = new DefaultDataset();
        
        Dataset actualDataset = ARFFHandler.loadARFF(file);
        
        assertEquals(expectedDataset.size(), actualDataset.size());
    }

    @Test
    public void testLoadARFF_noDataSection() throws FileNotFoundException {
        File file = new File("noDataSection.arff");
        Dataset expectedDataset = new DefaultDataset();
        
        Dataset actualDataset = ARFFHandler.loadARFF(file);
        
        assertEquals(expectedDataset.size(), actualDataset.size());
    }

    @Test
    public void testLoadARFF_missingClassValue() throws FileNotFoundException {
        File file = new File("missingClassValue.arff");
        Dataset expectedDataset = new DefaultDataset();
        expectedDataset.add(new DenseInstance(new double[]{1.0, 2.0}, null));
        expectedDataset.add(new DenseInstance(new double[]{4.0, 5.0}, null));
        
        Dataset actualDataset = ARFFHandler.loadARFF(file);
        
        assertEquals(expectedDataset.size(), actualDataset.size());
        assertEquals(expectedDataset.get(0).classValue(), actualDataset.get(0).classValue());
        assertArrayEquals(expectedDataset.get(0).values(), actualDataset.get(0).values(), 0.001);
        assertEquals(expectedDataset.get(1).classValue(), actualDataset.get(1).classValue());
        assertArrayEquals(expectedDataset.get(1).values(), actualDataset.get(1).values(), 0.001);
    }
}