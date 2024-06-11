import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

import org.junit.Test;
import org.sourceforge.ifx.basetypes.IFXObject;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        // Test when both objects are null
        assertFalse(obj1.equals(null));
        
        // Test when the object is not an instance of IFXObject
        assertFalse(obj1.equals("test"));
        
        // Test when the objects are equal
        assertTrue(obj1.equals(obj1));
        
        // Test when the objects are not equal
        assertFalse(obj1.equals(obj2));
    }
    
    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        assertNotNull(obj.toString());
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testStringify() throws Exception {
        IFXObject obj = new IFXObject();
        Method stringifyMethod = IFXObject.class.getDeclaredMethod("stringify", IFXObject.class, int.class);
        stringifyMethod.setAccessible(true);
        
        // Test when the object is an instance of IBaseType
        obj.setString("test");
        String result = (String) stringifyMethod.invoke(obj, obj, 0);
        assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {\n  string = test\n}", result);
        
        // Test when the object is not an instance of IBaseType
        IFXObject innerObj = new IFXObject();
        innerObj.setString("innerTest");
        obj.setObject(innerObj);
        result = (String) stringifyMethod.invoke(obj, obj, 0);
        assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {\n  object = org.sourceforge.ifx.basetypes.IFXObject = {\n    string = innerTest\n  }\n}", result);

        // Test when the object is an array
        IFXObject[] array = new IFXObject[2];
        array[0] = obj;
        array[1] = innerObj;
        obj.setArray(array);
        result = (String) stringifyMethod.invoke(obj, obj, 0);
        assertEquals("org.sourceforge.ifx.basetypes.IFXObject = {\n  array = [\n    [0]:org.sourceforge.ifx.basetypes.IFXObject = {\n      object = org.sourceforge.ifx.basetypes.IFXObject = {\n        string = innerTest\n      }\n    },\n    [1]:org.sourceforge.ifx.basetypes.IFXObject = {\n      string = test\n    }\n  ]\n}", result);
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testDoCheckEquals() throws Exception {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        Method doCheckEqualsMethod = IFXObject.class.getDeclaredMethod("doCheckEquals", IFXObject.class, IFXObject.class);
        doCheckEqualsMethod.setAccessible(true);
        
        // Test when both objects are null
        assertTrue((boolean) doCheckEqualsMethod.invoke(obj1, null, null));
        
        // Test when one object is null and the other is not
        assertFalse((boolean) doCheckEqualsMethod.invoke(obj1, obj1, null));
        assertFalse((boolean) doCheckEqualsMethod.invoke(obj1, null, obj2));
        
        // Test when the objects are not of the same class
        IFXObject diffObj = new IFXObject();
        assertFalse((boolean) doCheckEqualsMethod.invoke(obj1, obj1, diffObj));
        
        // Test when the objects are of the same class
        assertTrue((boolean) doCheckEqualsMethod.invoke(obj1, obj1, obj2));
        
        // Test when the objects are IBaseType objects with the same string
        obj1.setString("test");
        obj2.setString("test");
        assertTrue((boolean) doCheckEqualsMethod.invoke(obj1, obj1, obj2));
        
        // Test when the objects are IBaseType objects with different strings
        obj2.setString("diffTest");
        assertFalse((boolean) doCheckEqualsMethod.invoke(obj1, obj1, obj2));

        // Test when the objects are arrays of IFXObject
        IFXObject[] array1 = new IFXObject[2];
        array1[0] = obj1;
        array1[1] = obj2;
        IFXObject[] array2 = new IFXObject[2];
        array2[0] = obj2;
        array2[1] = obj1;
        assertTrue((boolean) doCheckEqualsMethod.invoke(obj1, array1, array1));
        assertFalse((boolean) doCheckEqualsMethod.invoke(obj1, array1, array2));
        
        // Test when the objects are arrays of IBaseType with the same string
        array1[0] = new IFXObject();
        array2[0] = new IFXObject();
        array1[0].setString("testArray");
        array2[0].setString("testArray");
        assertTrue((boolean) doCheckEqualsMethod.invoke(obj1, array1, array2));
        
        // Test when the objects are arrays of IBaseType with different strings
        array2[0].setString("diffArray");
        assertFalse((boolean) doCheckEqualsMethod.invoke(obj1, array1, array2));
    }
    
    @Test
    public void testTab() throws Exception {
        IFXObject obj = new IFXObject();
        Method tabMethod = IFXObject.class.getDeclaredMethod("tab", int.class);
        tabMethod.setAccessible(true);
        
        // Test for 0 levels
        String result = (String) tabMethod.invoke(obj, 0);
        assertEquals("", result);
        
        // Test for 2 levels
        result = (String) tabMethod.invoke(obj, 2);
        assertEquals("    ", result);
    }
}