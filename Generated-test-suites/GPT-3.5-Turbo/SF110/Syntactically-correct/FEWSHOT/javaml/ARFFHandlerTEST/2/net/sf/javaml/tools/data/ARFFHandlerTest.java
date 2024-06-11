package net.sf.javaml.tools.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

public class ARFFHandlerTest {
    
    @Test
    public void testLoadARFF() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadARFFWithClassIndex() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, 1);
            assertNotNull(dataset);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadARFFWithInvalidFile() {
        File file = new File("invalid.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertNotNull(dataset);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadARFFWithInvalidClassIndex() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, 10);
            assertNotNull(dataset);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadARFFWithDataMode() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertEquals(5, dataset.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadARFFWithDifferentDataMode() {
        File file = new File("test.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertEquals(10, dataset.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}