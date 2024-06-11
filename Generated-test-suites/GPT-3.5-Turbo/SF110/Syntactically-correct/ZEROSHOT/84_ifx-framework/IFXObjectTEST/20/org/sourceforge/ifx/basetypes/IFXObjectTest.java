package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        // Test when obj1 and obj2 are equal
        assertTrue(obj1.equals(obj2));
        
        // Test when obj1 and obj2 are not equal
        obj2 = new IFXObject();
        assertFalse(obj1.equals(obj2));
    }
    
    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {}\n";
        String actual = obj.toString();
        
        // Test that toString method returns expected string
        assertEquals(expected, actual);
    }
    
    @Test
    public void testStringify() {
        IFXObject obj = new IFXObject();
        int level = 0;
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n}";
        String actual = obj.stringify(obj, level);
        
        // Test that stringify method returns expected string
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        // Test when obj1 and obj2 are equal
        assertTrue(obj1.doCheckEquals(obj1, obj2));
        
        // Test when obj1 and obj2 are not equal
        obj2 = new IFXObject();
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }
    
    @Test
    public void testTab() {
        IFXObject obj = new IFXObject();
        int level = 2;
        String expected = "    ";
        String actual = obj.tab(level);
        
        // Test that tab method returns expected string
        assertEquals(expected, actual);
    }
}