import static org.junit.Assert.*;
import org.junit.*;

public class IFXObjectTest {
    
    @Test
    public void testEquals_nullObject_returnsFalse() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = null;
        
        assertFalse(obj1.equals(obj2));
    }
    
    @Test
    public void testEquals_sameObject_returnsTrue() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = obj1;
        
        assertTrue(obj1.equals(obj2));
    }
    
    @Test
    public void testEquals_differentObjectClass_returnsFalse() {
        IFXObject obj1 = new IFXObject();
        Object obj2 = new Object();
        
        assertFalse(obj1.equals(obj2));
    }
    
    @Test
    public void testEquals_differentStringValue_returnsFalse() {
        IFXObject obj1 = new IFXObject();
        // Set obj1 to have a non-null string value
        obj1.setStringValue("test");
        IFXObject obj2 = new IFXObject();
        
        assertFalse(obj1.equals(obj2));
    }
    
    @Test
    public void testEquals_differentArrayLength_returnsFalse() {
        IFXObject obj1 = new IFXObject();
        IFXObject[] array1 = new IFXObject[3];
        obj1.setArrayValue(array1);
        IFXObject obj2 = new IFXObject();
        IFXObject[] array2 = new IFXObject[2];
        obj2.setArrayValue(array2);
        
        assertFalse(obj1.equals(obj2));
    }
    
    @Test
    public void testEquals_differentArrayElement_returnsFalse() {
        IFXObject obj1 = new IFXObject();
        IFXObject[] array1 = { new IFXObject(), new IFXObject() };
        obj1.setArrayValue(array1);
        IFXObject obj2 = new IFXObject();
        IFXObject[] array2 = { new IFXObject(), new IFXObject() };
        obj2.setArrayValue(array2);
        
        assertFalse(obj1.equals(obj2));
    }
    
    @Test
    public void testEquals_differentBaseTypeValue_returnsFalse() {
        IFXObject obj1 = new IFXObject();
        IBaseType baseType1 = new BaseType();
        baseType1.setString("test");
        obj1.setBaseType(baseType1);
        IFXObject obj2 = new IFXObject();
        IBaseType baseType2 = new BaseType();
        baseType2.setString("different");
        obj2.setBaseType(baseType2);
        
        assertFalse(obj1.equals(obj2));
    }
    
    @Test
    public void testToString_nullObject_returnsExpectedString() {
        IFXObject obj = null;
        String expected = "NULL";
        
        assertEquals(expected, obj.toString());
    }
    
    @Test
    public void testToString_emptyObject_returnsExpectedString() {
        IFXObject obj = new IFXObject();
        String expected = "IFXObject = {}";
        
        assertEquals(expected, obj.toString());
    }
    
    @Test
    public void testToString_objectWithStringValue_returnsExpectedString() {
        IFXObject obj = new IFXObject();
        obj.setStringValue("test");
        String expected = "IFXObject = {\n  stringValue = test\n}";
        
        assertEquals(expected, obj.toString());
    }
    
    @Test
    public void testToString_objectWithArrayValue_returnsExpectedString() {
        IFXObject obj = new IFXObject();
        IFXObject[] array = { new IFXObject(), new IFXObject() };
        obj.setArrayValue(array);
        String expected = "IFXObject = {\n  arrayValue = [\n    [0]:IFXObject = {}\n    [1]:IFXObject = {}\n  ]\n}";
        
        assertEquals(expected, obj.toString());
    }
    
    @Test
    public void testToString_objectWithBaseTypeValue_returnsExpectedString() {
        IFXObject obj = new IFXObject();
        IBaseType baseType = new BaseType();
        baseType.setString("test");
        obj.setBaseType(baseType);
        String expected = "IFXObject = {\n  baseType = test\n}";
        
        assertEquals(expected, obj.toString());
    }
    
    @Test
    public void testToString_exceptionThrownInStringifyMethod_returnsExpectedStringWithExceptionMessage() {
        IFXObject obj = new IFXObject();
        
        // Manually set a private field to null to trigger an exception in stringify method
        Field field = null;
        try {
            field = obj.getClass().getDeclaredField("TABSTOP");
            field.setAccessible(true);
            field.set(obj, null);
        } catch (Exception e) {
            fail();
        }
        
        String expected = "IFXObject = {\n  Exception! java.lang.NullPointerException\n}";
        
        assertEquals(expected, obj.toString());
    }
    
    @Test
    public void testEquals_exceptionThrownInDoCheckEqualsMethod_returnsFalse() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        
        // Manually set a private field to null to trigger an exception in doCheckEquals method
        Field field = null;
        try {
            field = obj1.getClass().getDeclaredField("TABSTOP");
            field.setAccessible(true);
            field.set(obj1, null);
        } catch (Exception e) {
            fail();
        }
        
        assertFalse(obj1.equals(obj2));
    }
}