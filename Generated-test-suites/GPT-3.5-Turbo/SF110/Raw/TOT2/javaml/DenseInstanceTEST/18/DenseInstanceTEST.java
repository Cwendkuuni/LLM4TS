import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import net.sf.javaml.core.DenseInstance;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithAttributeArray() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertArrayEquals(att, instance.attributes, 0.0);
        assertNull(instance.classValue());
    }
    
    @Test
    public void testConstructorWithAttributeArrayAndClassValue() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class";
        DenseInstance instance = new DenseInstance(att, classValue);
        assertArrayEquals(att, instance.attributes, 0.0);
        assertEquals(classValue, instance.classValue());
    }

    // Add more test cases for other public methods...

}