import org.junit.Test;
import static org.junit.Assert.*;

public class IFXObjectTest {

    @Test
    public void testEquals_WhenEqual_ReturnsTrue() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void testEquals_WhenNotEqual_ReturnsFalse() {
        IFXObject obj1 = new IFXObject();
        String obj2 = "Not an IFXObject";
        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void testToString_WhenObjectHasNoFields_ReturnsCorrectStringRepresentation() {
        IFXObject obj = new IFXObject();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {}";
        assertEquals(expected, obj.toString());
    }

    @Test
    public void testToString_WhenObjectHasFields_ReturnsCorrectStringRepresentation() {
        IFXObject obj = new IFXObject();
        obj.setField1("Field1Value");
        obj.setField2(123);
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {" +
                          "\n  field1 = Field1Value" +
                          "\n  field2 = 123" +
                          "\n}";
        assertEquals(expected, obj.toString());
    }

    @Test
    public void testToString_WhenObjectHasArrayField_ReturnsCorrectStringRepresentation() {
        IFXObject obj = new IFXObject();
        IFXObject[] array = new IFXObject[2];
        obj.setArrayField(array);
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {" +
                          "\n  arrayField = [" +
                          "\n    [0]: org.sourceforge.ifx.basetypes.IFXObject = {}" +
                          "\n    [1]: org.sourceforge.ifx.basetypes.IFXObject = {}" +
                          "\n  ]" +
                          "\n}";
        assertEquals(expected, obj.toString());
    }
    
    @Test
    public void testToString_WhenObjectHasNullField_ReturnsCorrectStringRepresentation() {
        IFXObject obj = new IFXObject();
        obj.setField1(null);
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {" +
                          "\n  field1 = NULL" +
                          "\n}";
        assertEquals(expected, obj.toString());
    }
    
    @Test
    public void testToString_WhenObjectHasNestedObjects_ReturnsCorrectStringRepresentation() {
        IFXObject obj1 = new IFXObject();
        obj1.setField1("Field1Value");
        IFXObject obj2 = new IFXObject();
        obj2.setField2(123);
        obj1.setNestedObject(obj2);
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {" +
                          "\n  field1 = Field1Value" +
                          "\n  nestedObject = org.sourceforge.ifx.basetypes.IFXObject = {" +
                          "\n    field2 = 123" +
                          "\n  }" +
                          "\n}";
        assertEquals(expected, obj1.toString());
    }
    
    @Test
    public void testToString_WhenObjectThrowsException_ReturnsCorrectStringRepresentation() {
        IFXObject obj = new IFXObject();
        obj.setField1("TestValue");
        obj.setField2(123);
        obj.setThrowException(true);
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {" +
                          "\n  field1 = TestValue" +
                          "\n  field2 = 123" +
                          "\n  field3 = Exception! java.lang.Exception" +
                          "\n}";
        assertEquals(expected, obj.toString());
    }
    
}