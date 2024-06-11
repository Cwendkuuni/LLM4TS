package org.sourceforge.ifx.basetypes;

import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        obj1.setTestProperty(1);
        obj2.setTestProperty(1);
        assertEquals(obj1.equals(obj2), true);
        
        obj1.setTestProperty(1);
        obj2.setTestProperty(2);
        assertEquals(obj1.equals(obj2), false);
        
        obj1.setTestProperty(null);
        obj2.setTestProperty(null);
        assertEquals(obj1.equals(obj2), true);
    }
    
    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        String expectedString = "org.sourceforge.ifx.basetypes.IFXObject = {\n  testProperty = NULL\n}";
        assertEquals(obj.toString(), expectedString);
        
        obj.setTestProperty(1);
        expectedString = "org.sourceforge.ifx.basetypes.IFXObject = {\n  testProperty = 1\n}";
        assertEquals(obj.toString(), expectedString);
    }
    
    // Define additional test cases for other methods in the IFXObject class
    @Test
    public void testStringify() {
        IFXObject obj = new IFXObject();
        
        obj.setTestProperty(1);
        obj.setArrayProperty(new IFXObject[]{new IFXObject(), new IFXObject()});
        obj.setNullProperty(null);
        
        String expectedString = "org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                               "  testProperty = 1\n" +
                               "  arrayProperty = [\n" +
                               "    [0]:org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                               "      testProperty = NULL\n" +
                               "    },\n" +
                               "    [1]:org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                               "      testProperty = NULL\n" +
                               "    }\n" +
                               "  ]\n" +
                               "  nullProperty = NULL\n" +
                               "}";
        assertEquals(obj.toString(), expectedString);
    }
    
    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        obj1.setTestProperty(1);
        obj2.setTestProperty(1);
        assertEquals(obj1.doCheckEquals(obj1, obj2), true);
        
        obj1.setTestProperty(1);
        obj2.setTestProperty(2);
        assertEquals(obj1.doCheckEquals(obj1, obj2), false);
        
        obj1.setTestProperty(null);
        obj2.setTestProperty(null);
        assertEquals(obj1.doCheckEquals(obj1, obj2), true);
    }
    
    @Test
    public void testTab() {
        IFXObject obj = new IFXObject();
        
        assertEquals(obj.tab(0), "");
        assertEquals(obj.tab(1), "  ");
        assertEquals(obj.tab(2), "    ");
    }
}