package net.sf.javaml.tools.data;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class ARFFHandlerTest {

    private ARFFHandler arffHandler;

    @Before
    public void setUp() {
        arffHandler = new ARFFHandler();
    }

    @Test
    public void testLoadARFF_file() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = arffHandler.loadARFF(file);
        assertNotNull(dataset);
        // Perform additional assertions
    }

    @Test
    public void testLoadARFF_fileClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 0;
        Dataset dataset = arffHandler.loadARFF(file, classIndex);
        assertNotNull(dataset);
        // Perform additional assertions
    }

    // Add more test methods to cover all scenarios

}