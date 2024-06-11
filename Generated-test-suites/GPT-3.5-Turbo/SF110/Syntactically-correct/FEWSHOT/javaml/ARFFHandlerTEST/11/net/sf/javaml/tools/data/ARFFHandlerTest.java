package net.sf.javaml.tools.data;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() {
        File file = new File("test.arff");
        Dataset expectedDataset = new DefaultDataset();
        Dataset actualDataset = ARFFHandler.loadARFF(file);
        assertEquals(expectedDataset, actualDataset);
    }

    @Test
    public void testLoadARFFWithClassIndex() {
        File file = new File("test.arff");
        int classIndex = 2;
        Dataset expectedDataset = new DefaultDataset();
        Dataset actualDataset = ARFFHandler.loadARFF(file, classIndex);
        assertEquals(expectedDataset, actualDataset);
    }
}