package jigl.image.ops;

import jigl.image.*;
import jigl.image.exceptions.*;
import junit.framework.*;

public class ShiftTest extends TestCase {

    public ShiftTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of apply method, of class Shift.
     */
    public void testApply_GrayImage() throws RangeCheckError {
        System.out.println("apply");
        GrayImage image = null;
        Shift instance = new Shift(0, 0, 0);
        GrayImage expResult = null;
        GrayImage result = instance.apply(image);
        assertEquals(expResult.size(), result.size());
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of apply method, of class Shift.
     */
    public void testApply_RealGrayImage() throws RangeCheckError {
        System.out.println("apply");
        RealGrayImage image = null;
        Shift instance = new Shift(0, 0, 0);
        RealGrayImage expResult = null;
        RealGrayImage result = instance.apply(image);
        assertEquals(expResult.size(), result.size());
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of apply method, of class Shift.
     */
    public void testApply_ColorImage() throws RangeCheckError {
        System.out.println("apply");
        ColorImage image = null;
        Shift instance = new Shift(0, 0, 0);
        ColorImage expResult = null;
        ColorImage result = instance.apply(image);
        assertEquals(expResult.size(), result.size());
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of apply method, of class Shift.
     */
    public void testApply_RealColorImage() throws RangeCheckError {
        System.out.println("apply");
        RealColorImage image = null;
        Shift instance = new Shift(0, 0, 0);
        RealColorImage expResult = null;
        RealColorImage result = instance.apply(image);
        assertEquals(expResult.size(), result.size());
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}