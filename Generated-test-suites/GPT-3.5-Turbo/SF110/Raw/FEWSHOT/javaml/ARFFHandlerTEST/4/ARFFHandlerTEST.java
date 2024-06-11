package net.sf.javaml.tools.data;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import be.abeel.util.*;
import net.sf.javaml.core.*;
import java.util.*;

public class ARFFHandlerTest {
    
    @Test
    public void testLoadARFF_noClassIndex() {
        File file = new File("test.arff");
        Dataset expected = new DefaultDataset();
        
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        expected.add(instance1);
        
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
        expected.add(instance2);
        
        try {
            Dataset result = ARFFHandler.loadARFF(file);
            assertEquals(expected, result);
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException was thrown");
        }
    }
    
    @Test
    public void testLoadARFF_withClassIndex() {
        File file = new File("test.arff");
        int classIndex = 2;
        Dataset expected = new DefaultDataset();
        
        String classValue1 = "A";
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0}, classValue1);
        expected.add(instance1);
        
        String classValue2 = "B";
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0}, classValue2);
        expected.add(instance2);
        
        try {
            Dataset result = ARFFHandler.loadARFF(file, classIndex);
            assertEquals(expected, result);
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException was thrown");
        }
    }
}