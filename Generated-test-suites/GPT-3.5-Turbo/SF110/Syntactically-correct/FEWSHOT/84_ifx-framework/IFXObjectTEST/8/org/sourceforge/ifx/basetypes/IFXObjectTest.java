package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.*;
import org.junit.Test;

public class IFXObjectTest {
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertEquals(true, obj1.equals(obj2));
    }
    
    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                            "  }\n";
        assertEquals(expected, obj.toString());
    }
    
    @Test
    public void testStringify() {
        IFXObject obj = new IFXObject();
        int level = 0;
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                            "  }\n";
        assertEquals(expected, obj.stringify(obj, level));
    }
    
    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertEquals(true, obj1.doCheckEquals(obj1, obj2));
    }
    
    @Test
    public void testTab() {
        IFXObject obj = new IFXObject();
        int level = 0;
        String expected = "";
        assertEquals(expected, obj.tab(level));
    }
}