
package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new ConcreteInstance();
        assertEquals(0, instance.getID());
    }
    
    // test other methods
    
    private class ConcreteInstance extends AbstractInstance {
        @Override
        public double value(int index) {
            return 0;
        }
        
        @Override
        public void put(int index, double value) {
            // do nothing
        }
        
        @Override
        public int noAttributes() {
            return 0;
        }
        
        @Override
        public double classValue() {
            return 0;
        }
        
        @Override
        public void setClassValue(double classValue) {
            // do nothing
        }
    }
}

