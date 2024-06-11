package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {
    
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        assertTrue(obj1.equals(obj2));
    }
    
    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        assertNotNull(obj.toString());
    }
    
    @Test
    public void testStringify() {
        IFXObject obj = new IFXObject();
        int level = 0;
        String result = obj.stringify(obj, level);
        assertNotNull(result);
    }
    
    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        assertTrue(obj1.doCheckEquals(obj1, obj2));
    }
    
    @Test
    public void testTab() {
        IFXObject obj = new IFXObject();
        int level = 0;
        String result = obj.tab(level);
        assertNotNull(result);
    }
}