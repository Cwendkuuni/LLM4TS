import junit.framework.TestCase;

public class ShiftTest extends TestCase {

    public void testApply() {
        // Test case 1
        Shift shift1 = new Shift(2, 2, Shift.NOWRAP);
        GrayImage image1 = new GrayImage(3, 3);
        image1.set(0, 0, 1);
        image1.set(0, 1, 2);
        image1.set(0, 2, 3);
        image1.set(1, 0, 4);
        image1.set(1, 1, 5);
        image1.set(1, 2, 6);
        image1.set(2, 0, 7);
        image1.set(2, 1, 8);
        image1.set(2, 2, 9);
        GrayImage expected1 = new GrayImage(3, 3);
        expected1.set(0, 0, 0);
        expected1.set(0, 1, 0);
        expected1.set(0, 2, 0);
        expected1.set(1, 0, 0);
        expected1.set(1, 1, 1);
        expected1.set(1, 2, 2);
        expected1.set(2, 0, 0);
        expected1.set(2, 1, 4);
        expected1.set(2, 2, 5);
        assertEquals(expected1, shift1.apply(image1));

        // Test case 2
        Shift shift2 = new Shift(-1, -1, Shift.WRAP);
        GrayImage image2 = new GrayImage(3, 3);
        image2.set(0, 0, 1);
        image2.set(0, 1, 2);
        image2.set(0, 2, 3);
        image2.set(1, 0, 4);
        image2.set(1, 1, 5);
        image2.set(1, 2, 6);
        image2.set(2, 0, 7);
        image2.set(2, 1, 8);
        image2.set(2, 2, 9);
        GrayImage expected2 = new GrayImage(3, 3);
        expected2.set(0, 0, 5);
        expected2.set(0, 1, 6);
        expected2.set(0, 2, 3);
        expected2.set(1, 0, 8);
        expected2.set(1, 1, 9);
        expected2.set(1, 2, 6);
        expected2.set(2, 0, 2);
        expected2.set(2, 1, 3);
        expected2.set(2, 2, 5);
        assertEquals(expected2, shift2.apply(image2));
    }

    public void testApplyRealGrayImage() {
        // Test case 1
        Shift shift1 = new Shift(2, 2, Shift.NOWRAP);
        RealGrayImage image1 = new RealGrayImage(3, 3);
        image1.set(0, 0, 1.0f);
        image1.set(0, 1, 2.0f);
        image1.set(0, 2, 3.0f);
        image1.set(1, 0, 4.0f);
        image1.set(1, 1, 5.0f);
        image1.set(1, 2, 6.0f);
        image1.set(2, 0, 7.0f);
        image1.set(2, 1, 8.0f);
        image1.set(2, 2, 9.0f);
        RealGrayImage expected1 = new RealGrayImage(3, 3);
        expected1.set(0, 0, 0.0f);
        expected1.set(0, 1, 0.0f);
        expected1.set(0, 2, 0.0f);
        expected1.set(1, 0, 0.0f);
        expected1.set(1, 1, 1.0f);
        expected1.set(1, 2, 2.0f);
        expected1.set(2, 0, 0.0f);
        expected1.set(2, 1, 4.0f);
        expected1.set(2, 2, 5.0f);
        assertEquals(expected1, shift1.apply(image1));

        // Test case 2
        Shift shift2 = new Shift(-1, -1, Shift.WRAP);
        RealGrayImage image2 = new RealGrayImage(3, 3);
        image2.set(0, 0, 1.0f);
        image2.set(0, 1, 2.0f);
        image2.set(0, 2, 3.0f);
        image2.set(1, 0, 4.0f);
        image2.set(1, 1, 5.0f);
        image2.set(1, 2, 6.0f);
        image2.set(2, 0, 7.0f);
        image2.set(2, 1, 8.0f);
        image2.set(2, 2, 9.0f);
        RealGrayImage expected2 = new RealGrayImage(3, 3);
        expected2.set(0, 0, 5.0f);
        expected2.set(0, 1, 6.0f);
        expected2.set(0, 2, 3.0f);
        expected2.set(1, 0, 8.0f);
        expected2.set(1, 1, 9.0f);
        expected2.set(1, 2, 6.0f);
        expected2.set(2, 0, 2.0f);
        expected2.set(2, 1, 3.0f);
        expected2.set(2, 2, 5.0f);
        assertEquals(expected2, shift2.apply(image2));
    }

    public void testApplyColorImage() {
        // Test case 1
        Shift shift1 = new Shift(2, 2, Shift.NOWRAP);
        ColorImage image1 = new ColorImage(3, 3);
        image1.setPlane(0, new GrayImage(3, 3));
        image1.setPlane(1, new GrayImage(3, 3));
        image1.setPlane(2, new GrayImage(3, 3));
        shift1.apply(image1);

        // Test case 2
        Shift shift2 = new Shift(-1, -1, Shift.WRAP);
        ColorImage image2 = new ColorImage(3, 3);
        image2.setPlane(0, new GrayImage(3, 3));
        image2.setPlane(1, new GrayImage(3, 3));
        image2.setPlane(2, new GrayImage(3, 3));
        shift2.apply(image2);
    }

    public void testApplyRealColorImage() {
        // Test case 1
        Shift shift1 = new Shift(2, 2, Shift.NOWRAP);
        RealColorImage image1 = new RealColorImage(3, 3);
        image1.setPlane(0, new RealGrayImage(3, 3));
        image1.setPlane(1, new RealGrayImage(3, 3));
        image1.setPlane(2, new RealGrayImage(3, 3));
        shift1.apply(image1);

        // Test case 2
        Shift shift2 = new Shift(-1, -1, Shift.WRAP);
        RealColorImage image2 = new RealColorImage(3, 3);
        image2.setPlane(0, new RealGrayImage(3, 3));
        image2.setPlane(1, new RealGrayImage(3, 3));
        image2.setPlane(2, new RealGrayImage(3, 3));
        shift2.apply(image2);
    }
}