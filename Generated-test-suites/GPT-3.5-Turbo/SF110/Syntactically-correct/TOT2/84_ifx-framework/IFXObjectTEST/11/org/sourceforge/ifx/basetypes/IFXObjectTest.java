
package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        assertTrue(obj1.equals(obj2));
        assertTrue(obj2.equals(obj1));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        
        assertNotNull(obj.toString());
    }

    @Test
    public void testNullObjectEquals() {
        IFXObject obj1 = null;
        IFXObject obj2 = null;
        
        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void testNonEqualObjects() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        assertFalse(obj1.equals("Not an IFXObject"));
        assertFalse(obj1.equals(null));
    }

    @Test
    public void testEmptyObjectToString() {
        IFXObject obj = new IFXObject();
        
        String expectedString = "org.sourceforge.ifx.basetypes.IFXObject = {" +
                                "\n  }";
        
        assertEquals(expectedString, obj.toString());
    }

    @Test
    public void testExceptionInEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        obj2 = null;
        
        assertFalse(obj1.equals(obj2));
    }
}
