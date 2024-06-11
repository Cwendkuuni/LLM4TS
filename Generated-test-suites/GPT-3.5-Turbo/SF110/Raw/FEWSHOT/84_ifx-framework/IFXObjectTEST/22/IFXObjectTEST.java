package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class IFXObjectTest {
    
    private IFXObject createObject() {
        // create an instance of IFXObject with sample data
        // return the instance
    }

    @Test
    public void testEquals() {
        IFXObject obj1 = createObject();
        IFXObject obj2 = createObject();
        
        assertEquals(obj1, obj2);
    }

    @Test
    public void testToString() {
        IFXObject obj = createObject();
        
        String expected = ""; // expected toString representation of the object
        String actual = obj.toString();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testStringify() {
        IFXObject obj = createObject();
        
        String expected = ""; // expected stringify result
        String actual = obj.stringify(obj, 0);
        
        assertEquals(expected, actual);
    }

    @Test
    public void testDoCheckEquals() {
        IFXObject thisObj = createObject();
        IFXObject thatObj = createObject();
        
        boolean expected = true; // expected result of doCheckEquals method
        boolean actual = thisObj.doCheckEquals(thisObj, thatObj);
        
        assertEquals(expected, actual);
    }

    @Test
    public void testTab() {
        IFXObject obj = createObject();
        
        int level = 0; // level value for testing
        String expected = ""; // expected tab string
        String actual = obj.tab(level);
        
        assertEquals(expected, actual);
    }
}