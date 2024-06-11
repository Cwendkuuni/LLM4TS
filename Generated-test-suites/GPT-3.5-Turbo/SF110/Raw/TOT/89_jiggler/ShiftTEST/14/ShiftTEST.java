
import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {

    @Test
    public void testApply_GrayImage_wrap() {
        GrayImage input = new GrayImage(3, 3);
        for (int i = 0; i < input.X(); i++) {
            for (int j = 0; j < input.Y(); j++) {
                input.set(i, j, i * 3 + j);
            }
        }

        Shift shift = new Shift(1, 1, Shift.WRAP);
        GrayImage expectedOutput = new GrayImage(3, 3);
        for (int i = 0; i < expectedOutput.X(); i++) {
            for (int j = 0; j < expectedOutput.Y(); j++) {
                expectedOutput.set(i, j, (i - 1) * 3 + (j - 1));
            }
        }

        GrayImage output = shift.apply(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testApply_GrayImage_nowrap() {
        GrayImage input = new GrayImage(4, 4);
        for (int i = 0; i < input.X(); i++) {
            for (int j = 0; j < input.Y(); j++) {
                input.set(i, j, i * 4 + j);
            }
        }

        Shift shift = new Shift(-1, 2, Shift.NOWRAP);
        GrayImage expectedOutput = new GrayImage(4, 4);
        for (int i = 0; i < expectedOutput.X(); i++) {
            for (int j = 0; j < expectedOutput.Y(); j++) {
                if (i - 1 >= 0 && j + 2 < expectedOutput.Y()) {
                    expectedOutput.set(i, j, (i - 1) * 4 + (j + 2));
                } else {
                    expectedOutput.set(i, j, 0);
                }
            }
        }

        GrayImage output = shift.apply(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testApply_RealGrayImage_wrap() {
        RealGrayImage input = new RealGrayImage(2, 2);
        for (int i = 0; i < input.X(); i++) {
            for (int j = 0; j < input.Y(); j++) {
                input.set(i, j, (float) (i + 0.5 + j));
            }
        }

        Shift shift = new Shift(1, -1, Shift.WRAP);
        RealGrayImage expectedOutput = new RealGrayImage(2, 2);
        for (int i = 0; i < expectedOutput.X(); i++) {
            for (int j = 0; j < expectedOutput.Y(); j++) {
                expectedOutput.set(i, j, (float) (i + 0.5 + j - 1));
            }
        }

        RealGrayImage output = shift.apply(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testApply_ColorImage_nowrap() {
        ColorImage input = new ColorImage(2, 2);
        GrayImage plane0 = new GrayImage(2, 2);
        GrayImage plane1 = new GrayImage(2, 2);
        GrayImage plane2 = new GrayImage(2, 2);

        for (int i = 0; i < input.X(); i++) {
            for (int j = 0; j < input.Y(); j++) {
                plane0.set(i, j, i * 2 + j);
                plane1.set(i, j, i + j);
                plane2.set(i, j, i * j);
            }
        }

        input.setPlane(0, plane0);
        input.setPlane(1, plane1);
        input.setPlane(2, plane2);

        Shift shift = new Shift(0, 2, Shift.NOWRAP);

        ColorImage expectedOutput = new ColorImage(2, 2);
        GrayImage expectedPlane0 = new GrayImage(2, 2);
        GrayImage expectedPlane1 = new GrayImage(2, 2);
        GrayImage expectedPlane2 = new GrayImage(2, 2);

        for (int i = 0; i < expectedOutput.X(); i++) {
            for (int j = 0; j < expectedOutput.Y(); j++) {
                expectedPlane0.set(i, j, i * 2 + j - 2);
                expectedPlane1.set(i, j, i + j);
                expectedPlane2.set(i, j, i * j);
            }
        }

        expectedOutput.setPlane(0, expectedPlane0);
        expectedOutput.setPlane(1, expectedPlane1);
        expectedOutput.setPlane(2, expectedPlane2);

        ColorImage output = shift.apply(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testApply_RealColorImage_wrap() {
        RealColorImage input = new RealColorImage(3, 3);
        RealGrayImage plane0 = new RealGrayImage(3, 3);
        RealGrayImage plane1 = new RealGrayImage(3, 3);
        RealGrayImage plane2 = new RealGrayImage(3, 3);

        for (int i = 0; i < input.X(); i++) {
            for (int j = 0; j < input.Y(); j++) {
                plane0.set(i, j, (float) (i + 0.5 + j));
                plane1.set(i, j, (float) (i + j + 1.5));
                plane2.set(i, j, (float) (i * j + 0.5));
            }
        }

        input.setPlane(0, plane0);
        input.setPlane(1, plane1);
        input.setPlane(2, plane2);

        Shift shift = new Shift(-2, -3, Shift.WRAP);

        RealColorImage expectedOutput = new RealColorImage(3, 3);
        RealGrayImage expectedPlane0 = new RealGrayImage(3, 3);
        RealGrayImage expectedPlane1 = new RealGrayImage(3, 3);
        RealGrayImage expectedPlane2 = new RealGrayImage(3, 3);

        for (int i = 0; i < expectedOutput.X(); i++) {
            for (int j = 0; j < expectedOutput.Y(); j++) {
                expectedPlane0.set(i, j, (float) (i + 0.5 + j - 2));
                expectedPlane1.set(i, j, (float) (i + j + 1.5 - 3));
                expectedPlane2.set(i, j, (float) (i * j + 0.5 - 3));
            }
        }

        expectedOutput.setPlane(0, expectedPlane0);
        expectedOutput.setPlane(1, expectedPlane1);
        expectedOutput.setPlane(2, expectedPlane2);

        RealColorImage output = shift.apply(input);
        assertEquals(expectedOutput, output);
    }
}

