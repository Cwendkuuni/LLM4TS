
package net.sf.javaml.tools.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_file() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);

        assertNotNull(dataset);
        // additional assertions
    }

    @Test
    public void testLoadARFF_fileWithClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 2;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);

        assertNotNull(dataset);
        // additional assertions
    }

    // Add more test cases for all methods in ARFFHandler class

}

