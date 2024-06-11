
import org.junit.Test;
import org.sourceforge.ifx.basetypes.IFXObject;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        // Test when obj is not an instance of IFXObject
        IFXObject obj1 = new IFXObject();
        Object obj2 = new Object();
        boolean result1 = obj1.equals(obj2);
        assertFalse(result1);

        // Test when obj is an instance of IFXObject and is equal to this
        IFXObject obj3 = new IFXObject();
        boolean result2 = obj1.equals(obj3);
        assertTrue(result2);

        // Test when obj is an instance of IFXObject and is not equal to this
        IFXObject obj4 = new IFXObject();
        obj4.someProperty = "Some value";
        boolean result3 = obj1.equals(obj4);
        assertFalse(result3);
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();

        // Test when obj is empty
        String result1 = obj.toString();
        String expected1 = "org.sourceforge.ifx.basetypes.IFXObject = { }";
        assertEquals(expected1, result1);

        // Test when obj has a property
        obj.someProperty = "Some value";
        String result2 = obj.toString();
        String expected2 = "org.sourceforge.ifx.basetypes.IFXObject = { \n  someProperty = Some value\n }";
        assertEquals(expected2, result2);

        // Test when obj has an array property
        IFXObject[] array = new IFXObject[2];
        array[0] = new IFXObject();
        array[1] = new IFXObject();
        obj.arrayProperty = array;
        String result3 = obj.toString();
        String expected3 = "org.sourceforge.ifx.basetypes.IFXObject = { \n  someProperty = Some value\n  arrayProperty = [\n    [0]:org.sourceforge.ifx.basetypes.IFXObject = { \n    }";
        assertEquals(expected3, result3);
    }
}
