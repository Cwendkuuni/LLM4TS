
package net.sf.javaml.tools.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

public class ARFFHandlerTest {
    
    @Test
    public void testLoadARFF() {
        File file = new File("data.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testLoadARFF_withClassIndex() {
        File file = new File("data.arff");
        try {
            Dataset dataset = ARFFHandler.loadARFF(file, 1);
            assertEquals(0, dataset.size());
        } catch (FileNotFoundException e) {
            assertTrue(false);
        }
    }
}
