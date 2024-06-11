package org.apache.commons.lang3.builder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

public class HashCodeBuilderTest {
    
    private HashCodeBuilder hcb;
    
    @Before
    public void setUp() {
        hcb = new HashCodeBuilder();
    }
    
    @Test
    public void testAppendBoolean() {
        hcb.append(true);
        assertEquals(37, hcb.toHashCode());
        
        hcb.append(false);
        assertNotEquals(37, hcb.toHashCode());
    }
    
    @Test
    public void testAppendPrimitiveByteArray() {
        byte[] byteArray = {1, 2, 3};
        hcb.append(byteArray);
        assertNotEquals(0, hcb.toHashCode());
    }
    
    @Test
    public void testAppendObject() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        hcb.append(obj1);
        assertNotEquals(hcb.toHashCode(), hcb.append(obj2).toHashCode());
    }
    
    @Test
    public void testAppendSuper() {
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.appendSuper(hcb.toHashCode());
        
        assertNotEquals(0, hcb2.toHashCode());
    }
    
    // Add more test cases for all methods

}