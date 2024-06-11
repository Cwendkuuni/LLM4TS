import org.junit.*;
import static org.junit.Assert.*;
import java.lang.reflect.*;
import java.util.*;

public class IFXObjectTest {
    
    private IFXObject obj1;
    private IFXObject obj2;
    private IFXObject obj3;
    
    @Before
    public void setUp() {
        obj1 = new IFXObject();
        obj2 = new IFXObject();
        obj3 = new IFXObject();
    }
    
    @Test
    public void testEquals_ObjectsSame_ReturnsTrue() {
        assertTrue(obj1.equals(obj1));
    }
    
    @Test
    public void testEquals_ObjectAndNull_ReturnsFalse() {
        assertFalse(obj1.equals(null));
    }
    
    @Test
    public void testEquals_ObjectsDifferentClass_ReturnsFalse() {
        Integer obj = new Integer(10);
        assertFalse(obj1.equals(obj));
    }
    
    @Test
    public void testEquals_ObjectsNotEqual_ReturnsFalse() throws NoSuchFieldException, IllegalAccessException {
        Field field = IFXObject.class.getDeclaredField("TABSTOP");
        field.setAccessible(true);
        field.set(obj3, " ");
        assertFalse(obj1.equals(obj3));
    }
    
    @Test
    public void testToString_ReturnsValidString() {
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n}";
        String actual = obj1.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToString_ReturnsValidStringWithNestedObject() {
        IFXObject nestedObj = new IFXObject();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n  nestedObj = {\n  }\n}";
        String actual = obj1.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToString_ReturnsValidStringWithArray() {
        IFXObject[] array = new IFXObject[2];
        array[0] = new IFXObject();
        array[1] = new IFXObject();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n  array = [\n    [0]:\n      org.sourceforge.ifx.basetypes.IFXObject = {\n      },\n    [1]:\n      org.sourceforge.ifx.basetypes.IFXObject = {\n      }\n  ]\n}";
        String actual = obj1.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToString_ReturnsValidStringWithInvalidMember() throws NoSuchFieldException, IllegalAccessException {
        Field field = IFXObject.class.getDeclaredField("TABSTOP");
        field.setAccessible(true);
        field.set(obj3, null);
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n  TABSTOP = NULL\n}";
        String actual = obj1.toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDoCheckEquals_ObjectsBothNull_ReturnsTrue() {
        assertTrue(obj1.doCheckEquals(null, null));
    }
    
    @Test
    public void testDoCheckEquals_OneObjectNull_ReturnsFalse() {
        assertFalse(obj1.doCheckEquals(null, obj2));
    }
    
    @Test
    public void testDoCheckEquals_ObjectsDifferentClass_ReturnsFalse() {
        Integer obj = new Integer(10);
        assertFalse(obj1.doCheckEquals(obj1, obj));
    }
    
    @Test
    public void testDoCheckEquals_ObjectsNotEqual_ReturnsFalse() {
        assertFalse(obj1.doCheckEquals(obj1, obj2));
    }
    
    @Test
    public void testTab_ReturnsCorrectString() {
        String expected = "    ";
        String actual = obj1.tab(2);
        assertEquals(expected, actual);
    }
}