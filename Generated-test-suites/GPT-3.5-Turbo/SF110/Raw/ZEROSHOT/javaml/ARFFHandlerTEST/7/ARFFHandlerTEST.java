import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.ArrayList;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.ARFFHandler;
import org.junit.Before;

public class ARFFHandlerTest {

    private static final String TEST_FILE = "test.arff";
    private static final String TEST_DATA = "@relation test\n\n@attribute attr1 numeric\n@attribute attr2 numeric\n@attribute class {A,B,C}\n\n@data\n1,2,A\n2,3,B\n3,4,C";

    @Before
    public void setup() {
        try {
            FileWriter writer = new FileWriter(TEST_FILE);
            writer.write(TEST_DATA);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadARFFWithoutClassIndex() {
        try {
            File file = new File(TEST_FILE);
            Dataset dataset = ARFFHandler.loadARFF(file);
            
            ArrayList<Instance> instances = dataset.getInstances();
            assertEquals(3, instances.size());
            
            Instance instance1 = instances.get(0);
            double[] values1 = instance1.getValues();
            assertEquals(1.0, values1[0], 0);
            assertEquals(2.0, values1[1], 0);
            assertEquals("A", instance1.getClassValue());
            
            Instance instance2 = instances.get(1);
            double[] values2 = instance2.getValues();
            assertEquals(2.0, values2[0], 0);
            assertEquals(3.0, values2[1], 0);
            assertEquals("B", instance2.getClassValue());
            
            Instance instance3 = instances.get(2);
            double[] values3 = instance3.getValues();
            assertEquals(3.0, values3[0], 0);
            assertEquals(4.0, values3[1], 0);
            assertEquals("C", instance3.getClassValue());
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoadARFFWithClassIndex() {
        try {
            File file = new File(TEST_FILE);
            Dataset dataset = ARFFHandler.loadARFF(file, 2);
            
            ArrayList<Instance> instances = dataset.getInstances();
            assertEquals(3, instances.size());
            
            Instance instance1 = instances.get(0);
            double[] values1 = instance1.getValues();
            assertEquals(1.0, values1[0], 0);
            assertEquals(2.0, values1[1], 0);
            assertEquals("A", instance1.getClassValue());
            
            Instance instance2 = instances.get(1);
            double[] values2 = instance2.getValues();
            assertEquals(2.0, values2[0], 0);
            assertEquals(3.0, values2[1], 0);
            assertEquals("B", instance2.getClassValue());
            
            Instance instance3 = instances.get(2);
            double[] values3 = instance3.getValues();
            assertEquals(3.0, values3[0], 0);
            assertEquals(4.0, values3[1], 0);
            assertEquals("C", instance3.getClassValue());
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadARFFFileNotFoundException() {
        try {
            File file = new File("nonexistent.arff");
            Dataset dataset = ARFFHandler.loadARFF(file);
            
            assertNull(dataset);
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
}