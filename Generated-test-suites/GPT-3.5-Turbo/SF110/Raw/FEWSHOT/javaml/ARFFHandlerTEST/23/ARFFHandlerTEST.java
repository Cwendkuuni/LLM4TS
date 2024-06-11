package net.sf.javaml.tools.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.ARFFHandler;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        
        assertEquals(0, dataset.noAttributes());
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 0;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        
        assertEquals(0, dataset.noAttributes());
        assertEquals(0, dataset.size());
    }
    
    @Test
    public void testLoadARFFWithData() throws FileNotFoundException {
        File file = new File("test.arff");
        List<String> dataLines = Arrays.asList(
            "1.0,2.0,3.0,4.0,class1",
            "5.0,6.0,7.0,8.0,class2",
            "9.0,10.0,11.0,12.0,class3"
        );
        createTestFile(file, dataLines);
        
        Dataset dataset = ARFFHandler.loadARFF(file);
        
        assertEquals(4, dataset.noAttributes()); // 4 attributes
        assertEquals(3, dataset.size()); // 3 instances
        
        double[][] expectedData = {
            {1.0, 2.0, 3.0, 4.0},
            {5.0, 6.0, 7.0, 8.0},
            {9.0, 10.0, 11.0, 12.0}
        };
        String[] expectedClasses = {"class1", "class2", "class3"};
        
        for (int i = 0; i < dataset.size(); i++) {
            Instance instance = dataset.get(i);
            double[] expectedValues = expectedData[i];
            String expectedClass = expectedClasses[i];
            
            assertArrayEquals(expectedValues, instance.toDoubleArray(), 0.0);
            assertEquals(expectedClass, instance.classValue());
        }
    }
    
    @Test
    public void testLoadARFFWithClassIndexAndData() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 4;
        List<String> dataLines = Arrays.asList(
            "1.0,2.0,3.0,4.0,class1",
            "5.0,6.0,7.0,8.0,class2",
            "9.0,10.0,11.0,12.0,class3"
        );
        createTestFile(file, dataLines);
        
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        
        assertEquals(3, dataset.noAttributes()); // 3 attributes
        assertEquals(3, dataset.size()); // 3 instances
        
        double[][] expectedData = {
            {1.0, 2.0, 3.0},
            {5.0, 6.0, 7.0},
            {9.0, 10.0, 11.0}
        };
        String[] expectedClasses = {"class1", "class2", "class3"};
        
        for (int i = 0; i < dataset.size(); i++) {
            Instance instance = dataset.get(i);
            double[] expectedValues = expectedData[i];
            String expectedClass = expectedClasses[i];
            
            assertArrayEquals(expectedValues, instance.toDoubleArray(), 0.0);
            assertEquals(expectedClass, instance.classValue());
        }
    }
    
    private void createTestFile(File file, List<String> lines) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("@relation test");
            writer.println();
            for (int i = 0; i < lines.size(); i++) {
                writer.println(lines.get(i));    
            }
            writer.println();
            writer.println("@data");
            for (String line : lines) {
                writer.println(line);
            }
        }
    }
}