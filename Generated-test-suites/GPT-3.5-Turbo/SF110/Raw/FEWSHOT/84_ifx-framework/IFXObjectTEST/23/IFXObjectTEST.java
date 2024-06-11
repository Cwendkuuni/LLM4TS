package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class IFXObjectTest {
    
    private IFXObject obj;
    
    @Before
    public void setup() {
        obj = new IFXObject();
    }
    
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        assertEquals(true, obj.equals(obj1));
        
        Object obj2 = new Object();
        assertEquals(false, obj.equals(obj2));
    }
    
    @Test
    public void testToString() {
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                "  }\n";
        assertEquals(expected, obj.toString());
    }
    
    @Test
    public void testStringify() {
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                "  }\n";
        assertEquals(expected, obj.stringify(obj, 0));
    }
    
    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertEquals(true, obj.doCheckEquals(obj1, obj2));
    }
    
    @Test
    public void testTab() {
        String expected = "  ";
        assertEquals(expected, obj.tab(1));
    }
}