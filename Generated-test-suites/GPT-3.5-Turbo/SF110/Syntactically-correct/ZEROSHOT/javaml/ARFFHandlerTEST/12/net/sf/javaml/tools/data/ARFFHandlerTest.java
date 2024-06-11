package net.sf.javaml.tools.data;

import static org.junit.Assert.*;
import org.junit.Test;
import net.sf.javaml.core.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

public class ARFFHandlerTest {

    private final static String TEST_ARFF_FILE = "test.arff";

    // Helper method to delete test file
    private void deleteTestFile() {
        File file = new File(TEST_ARFF_FILE);
        file.delete();
    }

    @Test
    public void testLoadARFF_defaultClassIndex() throws FileNotFoundException {
        Dataset expected = new DefaultDataset();
        expected.add(new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}, "class1"));
        expected.add(new DenseInstance(new double[]{5.0, 6.0, 7.0, 8.0}, "class2"));
        
        // Create a test ARFF file
        String content = "@relation test\n\n"
                + "@attribute attr1 numeric\n"
                + "@attribute attr2 numeric\n"
                + "@attribute attr3 numeric\n"
                + "@attribute attr4 numeric\n"
                + "@data\n"
                + "1.0, 2.0, 3.0, 4.0, class1\n"
                + "5.0, 6.0, 7.0, 8.0, class2\n";
        
        File arffFile = new File(TEST_ARFF_FILE);
        try {
            FileUtils.writeStringToFile(arffFile, content);
            
            Dataset actual = ARFFHandler.loadARFF(arffFile);
            
            assertEquals(expected.size(), actual.size());
            for (int i = 0; i < expected.size(); i++) {
                Instance expectedInstance = expected.get(i);
                Instance actualInstance = actual.get(i);
                
                assertTrue(Arrays.equals(expectedInstance.toDoubleArray(), actualInstance.toDoubleArray()));
                assertEquals(expectedInstance.classValue(), actualInstance.classValue());
            }
        } finally {
            deleteTestFile();
        }
    }

    @Test
    public void testLoadARFF_customClassIndex() throws FileNotFoundException {
        Dataset expected = new DefaultDataset();
        expected.add(new DenseInstance(new double[]{1.0, 2.0, 4.0}, "class1"));
        expected.add(new DenseInstance(new double[]{5.0, 6.0, 8.0}, "class2"));
        
        // Create a test ARFF file
        String content = "@relation test\n\n"
                + "@attribute attr1 numeric\n"
                + "@attribute attr2 numeric\n"
                + "@attribute class {class1, class2}\n"
                + "@data\n"
                + "1.0, 2.0, class1\n"
                + "5.0, 6.0, class2\n";
        
        File arffFile = new File(TEST_ARFF_FILE);
        try {
            FileUtils.writeStringToFile(arffFile, content);
            
            Dataset actual = ARFFHandler.loadARFF(arffFile, 2);
            
            assertEquals(expected.size(), actual.size());
            for (int i = 0; i < expected.size(); i++) {
                Instance expectedInstance = expected.get(i);
                Instance actualInstance = actual.get(i);
                
                assertTrue(Arrays.equals(expectedInstance.toDoubleArray(), actualInstance.toDoubleArray()));
                assertEquals(expectedInstance.classValue(), actualInstance.classValue());
            }
        } finally {
            deleteTestFile();
        }
    }
}