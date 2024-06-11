package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class IFXObjectTest {
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        obj1.setString("foo");
        IFXObject obj2 = new IFXObject();
        obj2.setString("foo");
        assertTrue(obj1.equals(obj2));
        
        obj1.setString("bar");
        assertFalse(obj1.equals(obj2));
        
        obj2.setString("bar");
        assertTrue(obj1.equals(obj2));
        
        obj2.setIntValue(123);
        assertFalse(obj1.equals(obj2));
        
        obj1.setIntValue(123);
        assertTrue(obj1.equals(obj2));
        
        obj1.setIntArrayValue(new int[]{1, 2, 3});
        assertFalse(obj1.equals(obj2));
        
        obj2.setIntArrayValue(new int[]{1, 2, 3});
        assertTrue(obj1.equals(obj2));
        
        obj2.setIntArrayValue(new int[]{1, 2, 4});
        assertFalse(obj1.equals(obj2));
        
        obj2.setIntArrayValue(new int[]{1, 2, 3, 4});
        assertFalse(obj1.equals(obj2));
    }
    
    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        obj.setString("foo");
        assertEquals("IFXObject = {\n  StringValue = foo\n}", obj.toString());
        
        obj.setIntValue(123);
        assertEquals("IFXObject = {\n  StringValue = foo,\n  IntValue = 123\n}", obj.toString());
        
        IFXObject nestedObj = new IFXObject();
        nestedObj.setString("bar");
        obj.setNestedObj(nestedObj);
        assertEquals("IFXObject = {\n  StringValue = foo,\n  IntValue = 123,\n  NestedObj = {\n    StringValue = bar\n  }\n}", obj.toString());
        
        IFXObject[] objArray = new IFXObject[2];
        objArray[0] = new IFXObject();
        objArray[0].setString("baz");
        objArray[1] = new IFXObject();
        objArray[1].setString("qux");
        obj.setObjArrayValue(objArray);
        assertEquals("IFXObject = {\n  StringValue = foo,\n  IntValue = 123,\n  NestedObj = {\n    StringValue = bar\n  },\n  ObjArrayValue = [\n    [0]:IFXObject = {\n      StringValue = baz\n    },\n    [1]:IFXObject = {\n      StringValue = qux\n    }\n  ]\n}", obj.toString());
    }
    
    @Test
    public void testTab() {
        IFXObject obj = new IFXObject();
        assertEquals("", obj.tab(0));
        assertEquals("  ", obj.tab(1));
        assertEquals("    ", obj.tab(2));
    }
}