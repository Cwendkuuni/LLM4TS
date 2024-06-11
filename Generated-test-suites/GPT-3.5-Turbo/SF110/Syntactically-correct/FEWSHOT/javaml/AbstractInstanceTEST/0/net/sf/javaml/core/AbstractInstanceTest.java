import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testAbstractInstance() {
        AbstractInstance instance1 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 3;
            }
            
            @Override
            public double value(int index) {
                return index + 1;
            }
        };
        
        AbstractInstance instance2 = new AbstractInstance() {
            @Override
            public int noAttributes() {
                return 3;
            }
            
            @Override
            public double value(int index) {
                return index + 1;
            }
        };
        
        assertEquals(0, instance1.getID());
        assertTrue(instance1.iterator().hasNext());
        assertEquals(Double.valueOf(1), instance1.iterator().next());
        assertNull(instance1.classValue());
        
        instance1.setClassValue("class1");
        assertEquals("class1", instance1.classValue());
        
        assertNull(instance1.minus(instance2));
        assertNull(instance1.minus(1));
        assertNull(instance1.divide(1));
        assertNull(instance1.multiply(2));
        assertNull(instance1.multiply(instance2));
        assertNull(instance1.divide(instance2));
        assertNull(instance1.add(2));
        assertNull(instance1.add(instance2));
        assertNull(instance1.sqrt());
        
        assertEquals(0, instance1.hashCode());
        assertTrue(instance1.equals(instance1));
        assertTrue(!instance1.equals(null));
        assertTrue(!instance1.equals(new Object()));
        assertTrue(!instance1.equals(instance2));
    }

}