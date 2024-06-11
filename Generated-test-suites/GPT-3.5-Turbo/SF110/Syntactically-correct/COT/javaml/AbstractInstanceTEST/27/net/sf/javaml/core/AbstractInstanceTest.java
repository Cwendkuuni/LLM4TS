
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractInstanceTest {
    
    //1. Test getID()
    @Test
    public void testGetID() {
        AbstractInstance instance = new AbstractInstance() {};
        int id = instance.getID();
        assertEquals(0, id);
    }
    
    //2. Test iterator()
    @Test
    public void testIterator() {
        AbstractInstance instance = new AbstractInstance() {};
        Iterator<Double> iterator = instance.iterator();
        assertTrue(iterator.hasNext());
    }
    
    //3. Test classValue()
    @Test
    public void testClassValue() {
        AbstractInstance instance = new AbstractInstance() {};
        Object classValue = instance.classValue();
        assertNull(classValue);
    }
    
    //4. Test setClassValue()
    @Test
    public void testSetClassValue() {
        AbstractInstance instance = new AbstractInstance() {};
        Object classValue = new Object();
        instance.setClassValue(classValue);
        assertEquals(classValue, instance.classValue());
    }
    
    //5. Test minus(Instance min)
    @Test
    public void testMinusInstance() {
        AbstractInstance instance = new AbstractInstance() {};
        Instance min = new DenseInstance(new double[instance.noAttributes()]);
        Instance result = instance.minus(min);
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.0001);
        }
    }
    
    //6. Test minus(double min)
    @Test
    public void testMinusDouble() {
        AbstractInstance instance = new AbstractInstance() {};
        double min = 5.0;
        Instance result = instance.minus(min);
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.0001);
        }
    }
    
    //7. Test divide(double min)
    @Test
    public void testDivideDouble() {
        AbstractInstance instance = new AbstractInstance() {};
        double min = 5.0;
        Instance result = instance.divide(min);
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.0001);
        }
    }
    
    //8. Test multiply(double value)
    @Test
    public void testMultiply() {
        AbstractInstance instance = new AbstractInstance() {};
        double value = 5.0;
        Instance result = instance.multiply(value);
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.0001);
        }
    }
    
    //9. Test hashCode()
    @Test
    public void testHashCode() {
        AbstractInstance instance = new AbstractInstance() {};
        int hashCode = instance.hashCode();
        assertEquals(0, hashCode);
    }
    
    //10. Test equals(Object obj)
    @Test
    public void testEquals() {
        AbstractInstance instance1 = new AbstractInstance() {};
        AbstractInstance instance2 = new AbstractInstance() {};
        assertFalse(instance1.equals(null));
        assertTrue(instance1.equals(instance1));
        assertFalse(instance1.equals(instance2));
    }
    
    //11. Test multiply(Instance value)
    @Test
    public void testMultiplyInstance() {
        AbstractInstance instance = new AbstractInstance() {};
        Instance value = new DenseInstance(new double[instance.noAttributes()]);
        Instance result = instance.multiply(value);
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.0001);
        }
    }
    
    //12. Test divide(Instance min)
    @Test
    public void testDivideInstance() {
        AbstractInstance instance = new AbstractInstance() {};
        Instance min = new DenseInstance(new double[instance.noAttributes()]);
        Instance result = instance.divide(min);
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.0001);
        }
    }
    
    //13. Test add(double min)
    @Test
    public void testAddDouble() {
        AbstractInstance instance = new AbstractInstance() {};
        double min = 5.0;
        Instance result = instance.add(min);
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.0001);
        }
    }
    
    //14. Test add(Instance min)
    @Test
    public void testAddInstance() {
        AbstractInstance instance = new AbstractInstance() {};
        Instance min = new DenseInstance(new double[instance.noAttributes()]);
        Instance result = instance.add(min);
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.0001);
        }
    }
    
    //15. Test sqrt()
    @Test
    public void testSqrt() {
        AbstractInstance instance = new AbstractInstance() {};
        Instance result = instance.sqrt();
        
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(0.0, result.get(i), 0.0001);
        }
    }
}
