package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testEquals() {
        ComboLeg leg1 = new ComboLeg();
        ComboLeg leg2 = new ComboLeg(0, 0, null, null, 0, 0, null);
        ComboLeg leg3 = new ComboLeg(1, 1, "BUY", "NYSE", 1, 1, "LOC");
        ComboLeg leg4 = new ComboLeg(1, 1, "BUY", "NYSE", 2, 1, "LOC");
        
        assertEquals(leg1, leg1);
        assertEquals(leg1, leg2);
        assertNotEquals(leg1, leg3);
        assertNotEquals(leg1, leg4);
    }
    
}