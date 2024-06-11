import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;

    @Before
    public void setup() {
        coreOperationCompare = new CoreOperationCompare(null, null);
    }

    @Test
    public void testEqual_sameObjects_shouldReturnTrue() {
        String obj = "test";
        boolean result = coreOperationCompare.equal(obj, obj);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_differentObjects_shouldReturnFalse() {
        String obj1 = "test1";
        String obj2 = "test2";
        boolean result = coreOperationCompare.equal(obj1, obj2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_bothPointersEqual_shouldReturnTrue() {
        Pointer pointer = createPointer("test");
        boolean result = coreOperationCompare.equal(pointer, pointer);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_bothPointersNotEqual_shouldReturnFalse() {
        Pointer pointer1 = createPointer("test1");
        Pointer pointer2 = createPointer("test2");
        boolean result = coreOperationCompare.equal(pointer1, pointer2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_oneSidePointerEqual_shouldReturnTrue() {
        Pointer pointer1 = createPointer("test");
        String stringObj = "test";
        boolean result = coreOperationCompare.equal(pointer1, stringObj);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_oneSidePointerNotEqual_shouldReturnFalse() {
        Pointer pointer = createPointer("test");
        String stringObj = "test1";
        boolean result = coreOperationCompare.equal(pointer, stringObj);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_bothSidesNull_shouldReturnFalse() {
        boolean result = coreOperationCompare.equal(null, null);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_leftSideNull_shouldReturnFalse() {
        String obj = "test";
        boolean result = coreOperationCompare.equal(null, obj);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_rightSideNull_shouldReturnFalse() {
        String obj = "test";
        boolean result = coreOperationCompare.equal(obj, null);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_leftSideBoolean_shouldReturnTrue() {
        boolean bool1 = true;
        boolean bool2 = true;
        boolean result = coreOperationCompare.equal(bool1, bool2);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_rightSideBoolean_shouldReturnTrue() {
        boolean bool = true;
        boolean result = coreOperationCompare.equal(bool, bool);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_leftSideDifferentBoolean_shouldReturnFalse() {
        boolean bool1 = true;
        boolean bool2 = false;
        boolean result = coreOperationCompare.equal(bool1, bool2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_rightSideDifferentBoolean_shouldReturnFalse() {
        boolean bool1 = true;
        boolean bool2 = false;
        boolean result = coreOperationCompare.equal(bool1, bool2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_bothSidesBoolean_shouldReturnTrue() {
        Boolean bool1 = Boolean.TRUE;
        boolean bool2 = true;
        boolean result = coreOperationCompare.equal(bool1, bool2);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_bothSidesNaN_shouldReturnTrue() {
        Double double1 = Double.NaN;
        Double double2 = Double.NaN;
        boolean result = coreOperationCompare.equal(double1, double2);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_leftSideNaN_shouldReturnFalse() {
        Double double1 = Double.NaN;
        Double double2 = 5.0;
        boolean result = coreOperationCompare.equal(double1, double2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_rightSideNaN_shouldReturnFalse() {
        Double double1 = 1.0;
        Double double2 = Double.NaN;
        boolean result = coreOperationCompare.equal(double1, double2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_leftSideString_shouldReturnTrue() {
        String string = "test";
        boolean result = coreOperationCompare.equal(string, string);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_rightSideString_shouldReturnTrue() {
        String string = "test";
        boolean result = coreOperationCompare.equal(string, string);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqual_leftSideDifferentString_shouldReturnFalse() {
        String string1 = "test1";
        String string2 = "test2";
        boolean result = coreOperationCompare.equal(string1, string2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_rightSideDifferentString_shouldReturnFalse() {
        String string1 = "test1";
        String string2 = "test2";
        boolean result = coreOperationCompare.equal(string1, string2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqual_bothSidesString_shouldReturnTrue() {
        String string1 = "test";
        String string2 = new String("test");
        boolean result = coreOperationCompare.equal(string1, string2);
        Assert.assertTrue(result);
    }

    private Pointer createPointer(Object value) {
        return new Pointer(value);
    }
}