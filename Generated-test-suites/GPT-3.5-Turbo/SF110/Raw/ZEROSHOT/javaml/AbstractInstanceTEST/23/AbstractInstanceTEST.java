import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AbstractInstanceTest {
    
    private AbstractInstance instance;
    
    @Before
    public void setUp() {
        instance = new AbstractInstance();
    }
    
    @Test
    public void testGetID() {
        int id = instance.getID();
        assertNotNull(id);
    }
    
    @Test
    public void testIterator() {
        Iterator<Double> iterator = instance.iterator();
        assertNotNull(iterator);
    }
    
    @Test
    public void testClassValue() {
        Object classValue = instance.classValue();
        assertNull(classValue);
    }
    
    @Test
    public void testSetClassValue() {
        Object newClassValue = new Object();
        instance.setClassValue(newClassValue);
        Object updatedClassValue = instance.classValue();
        assertEquals(newClassValue, updatedClassValue);
    }
    
    @Test
    public void testMinusInstance() {
        Instance min = new DenseInstance(new double[this.noAttributes()]);
        Instance result = instance.minus(min);
        assertNotNull(result);
    }
    
    @Test
    public void testMinusDouble() {
        double min = 0.0;
        Instance result = instance.minus(min);
        assertNotNull(result);
    }
    
    @Test
    public void testDivideDouble() {
        double min = 1.0;
        Instance result = instance.divide(min);
        assertNotNull(result);
    }
    
    @Test
    public void testMultiplyDouble() {
        double value = 1.0;
        Instance result = instance.multiply(value);
        assertNotNull(result);
    }
    
    @Test
    public void testHashCode() {
        int hashCode = instance.hashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testEquals() {
        Object obj = new AbstractInstance();
        boolean isEqual = instance.equals(obj);
        assertTrue(isEqual);
    }
    
    @Test
    public void testMultiplyInstance() {
        Instance value = new DenseInstance(new double[this.noAttributes()]);
        Instance result = instance.multiply(value);
        assertNotNull(result);
    }
    
    @Test
    public void testDivideInstance() {
        Instance min = new DenseInstance(new double[this.noAttributes()]);
        Instance result = instance.divide(min);
        assertNotNull(result);
    }
    
    @Test
    public void testAddDouble() {
        double min = 0.0;
        Instance result = instance.add(min);
        assertNotNull(result);
    }
    
    @Test
    public void testAddInstance() {
        Instance min = new DenseInstance(new double[this.noAttributes()]);
        Instance result = instance.add(min);
        assertNotNull(result);
    }
    
    @Test
    public void testSqrt() {
        Instance result = instance.sqrt();
        assertNotNull(result);
    }
}