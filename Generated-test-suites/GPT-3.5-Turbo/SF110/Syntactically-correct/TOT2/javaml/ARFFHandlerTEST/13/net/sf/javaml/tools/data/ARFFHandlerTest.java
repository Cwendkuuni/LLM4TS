package net.sf.javaml.tools.data;
import static org.junit.Assert.*;
import org.junit.Test;
import net.sf.javaml.tools.data.ARFFHandler;
import net.sf.javaml.core.*;

import java.io.File;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() {
        File file = new File("test.arff");
        Dataset dataset = null;
        try {
            dataset = ARFFHandler.loadARFF(file);
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        }
        assertNotNull("Dataset is null", dataset);
        assertEquals("Wrong number of instances", 0, dataset.size());
    }

    @Test
    public void testLoadARFFWithClassIndex() {
        File file = new File("test.arff");
        Dataset dataset = null;
        try {
            dataset = ARFFHandler.loadARFF(file, 0);
        } catch (FileNotFoundException e) {
            fail("File not found exception");
        }
        assertNotNull("Dataset is null", dataset);
        assertEquals("Wrong number of instances", 0, dataset.size());
    }
}