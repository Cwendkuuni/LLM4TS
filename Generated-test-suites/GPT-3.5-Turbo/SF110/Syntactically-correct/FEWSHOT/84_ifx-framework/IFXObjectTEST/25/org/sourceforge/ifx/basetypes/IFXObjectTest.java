package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.sourceforge.ifx.basetypes.IFXObject.IBaseType;

public class IFXObjectTest {
    @Test
    public void testEquals() {
        IFXObject object1 = new IFXObject();
        IFXObject object2 = new IFXObject();
        
        assertTrue(object1.equals(object1));
        assertFalse(object1.equals(null));
        assertFalse(object1.equals(new Object()));
        assertTrue(object1.equals(object2));
    }
    
    @Test
    public void testToString() {
        IFXObject object = new IFXObject();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n  }";
        
        assertEquals(expected, object.toString());
    }
}