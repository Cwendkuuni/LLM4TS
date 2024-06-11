package org.sourceforge.ifx.basetypes;
import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        assertFalse(obj1.equals(new Object()));
        assertTrue(obj1.equals(obj2));
    }
    
    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        
        assertNotNull(obj.toString());
    }

    @Test
    public void testEdgeCases() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();

        // Testing when the object is null
        assertFalse(obj1.equals(null));
        
        // Testing when the object is not of type IFXObject
        assertFalse(obj1.equals("String"));
    }

    @Test
    public void testExceptionHandling() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();

        // Testing exception handling when invoking methods
        try {
            obj1.toString();
            obj2.toString();
        } catch (Exception e) {
            fail("Exception thrown when invoking toString method");
        }
    }
}