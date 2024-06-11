import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_File() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test
    public void testLoadARFF_FileInt() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 1;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_File_Exception() throws FileNotFoundException {
        File file = new File("nonexistent.arff");
        ARFFHandler.loadARFF(file);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_FileInt_Exception() throws FileNotFoundException {
        File file = new File("nonexistent.arff");
        int classIndex = 1;
        ARFFHandler.loadARFF(file, classIndex);
    }

    @Test
    public void testLoadARFF_File_DataMode() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);

        FileAssert.assertTrue(dataMode);
        assertEquals(0, dataset.size());
    }

    @Test
    public void testLoadARFF_FileInt_DataMode() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 1;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);

        FileAssert.assertTrue(dataMode);
        assertEquals(0, dataset.size());
    }

    @Test
    public void testLoadARFF_File_InstanceValues() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);

        Instance instance = dataset.instance(0);
        assertNotNull(instance.values());
        assertEquals(0, instance.values().length);
    }

    @Test
    public void testLoadARFF_FileInt_InstanceValues() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 1;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);

        Instance instance = dataset.instance(0);
        assertNotNull(instance.values());
        assertEquals(0, instance.values().length);
    }

    @Test
    public void testLoadARFF_File_InstanceClassValue() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);

        Instance instance = dataset.instance(0);
        assertNull(instance.classValue());
    }

    @Test
    public void testLoadARFF_FileInt_InstanceClassValue() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 1;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);

        Instance instance = dataset.instance(0);
        assertNull(instance.classValue());
    }

    @Test
    public void testLoadARFF_File_InstanceDoubleParsing() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);

        Instance instance = dataset.instance(0);
        assertNotNull(instance.values());
        assertEquals(0, instance.values().length);
        assertTrue(Double.isNaN(instance.value(0)));
    }

    @Test
    public void testLoadARFF_FileInt_InstanceDoubleParsing() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 1;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);

        Instance instance = dataset.instance(0);
        assertNotNull(instance.values());
        assertEquals(0, instance.values().length);
        assertTrue(Double.isNaN(instance.value(0)));
    }

    @Test
    public void testLoadARFF_File_InstanceValueIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);

        Instance instance = dataset.instance(0);
        assertNotNull(instance.values());
        assertEquals(0, instance.values().length);
        assertTrue(Double.isNaN(instance.value(1)));
    }

    @Test
    public void testLoadARFF_FileInt_InstanceValueIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 1;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);

        Instance instance = dataset.instance(0);
        assertNotNull(instance.values());
        assertEquals(0, instance.values().length);
        assertTrue(Double.isNaN(instance.value(1)));
    }

    @Test
    public void testLoadARFF_FileInt_InstanceValuesLength() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 1;
        Dataset dataset = ARFFHandler.loadARFF(file, classIndex);

        Instance instance = dataset.instance(0);
        assertNotNull(instance.values());
        assertEquals(0, instance.values().length);
    }
}