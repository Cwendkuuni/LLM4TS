
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IFXObjectTest {

    private IFXObject obj1;
    private IFXObject obj2;
    private IFXObject obj3;
    private IFXObject obj4;

    @Before
    public void setUp() {
        obj1 = new IFXObject();
        obj2 = new IFXObject();
        obj3 = new IFXObject();
        obj4 = new IFXObject();
    }

    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        assertTrue(obj1.equals(obj1));
    }

    @Test
    public void testEquals_NullObject_ReturnsFalse() {
        assertFalse(obj1.equals(null));
    }

    @Test
    public void testEquals_SameClassButDifferentObject_ReturnsFalse() {
        assertFalse(obj1.equals(obj2));
    }

    @Test
    public void testToString_SingleLevelHierarchy_ReturnsExpectedString() {
        String expected = "IFXObject = {}";
        assertEquals(expected, obj1.toString());
    }

    @Test
    public void testToString_MultiLevelHierarchy_ReturnsExpectedString() {
        obj1 = new IFXInternalObject();
        String expected = "IFXInternalObject = {\n  }";
        assertEquals(expected, obj1.toString());
    }

    @Test
    public void testToString_MultiLevelHierarchyWithArray_ReturnsExpectedString() {
        obj1 = new IFXInternalArrayObject();

        IFXObject arrayElement1 = new IFXObject();
        IFXObject arrayElement2 = new IFXObject();
        obj1.array = new IFXObject[]{arrayElement1, arrayElement2};

        String expected = "IFXInternalArrayObject = {\n  array = [\n    [0]:IFXObject = {\n      }\n    ,\n    [1]:IFXObject = {\n      }\n  ]\n}";
        assertEquals(expected, obj1.toString());
    }

    @Test
    public void testToString_MultiLevelHierarchyWithIBaseType_ReturnsExpectedString() {
        obj1 = new IFXInternalIBaseTypeObject();

        IBaseType baseType = new IBaseTypeImpl();
        obj1.baseType = baseType;

        String expected = "IFXInternalIBaseTypeObject = {\n  baseType = IBaseTypeImpl\n}";
        assertEquals(expected, obj1.toString());
    }

    @Test
    public void testToString_ExceptionOccurred_ReturnsExpectedStringWithExceptionMessage() {
        obj1 = new IFXObject() {
            @Override
            private String stringify(final IFXObject obj, final int level) throws Exception {
                throw new Exception("Custom Exception");
            }
        };

        String expected = "IFXObject = {\n  Exception! java.lang.Exception: Custom Exception\n}";
        assertEquals(expected, obj1.toString());
    }

    @Test(expected = Exception.class)
    public void testDoCheckEquals_ExceptionOccurred_ThrowsException() {
        obj1 = new IFXObject() {
            @Override
            private boolean doCheckEquals(final IFXObject thisObj, final IFXObject thatObj) throws Exception {
                throw new Exception("Custom Exception");
            }
        };

        obj1.equals(obj2);
    }

    private class IFXInternalObject extends IFXObject {}

    private class IFXInternalArrayObject extends IFXObject {
        private IFXObject[] array;
    }

    private class IFXInternalIBaseTypeObject extends IFXObject {
        private IBaseType baseType;
    }

    private interface IBaseType {
        String getString();
    }

    private class IBaseTypeImpl implements IBaseType {
        @Override
        public String getString() {
            return "IBaseTypeImpl";
        }
    }
}
