import org.junit.Test;

public class ShiftTest {

    @Test
    public void testShift() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        // Testing the wrap functionality
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 0, 2);
        image.set(2, 0, 3);
        image.set(0, 1, 4);
        image.set(1, 1, 5);
        image.set(2, 1, 6);
        image.set(0, 2, 7);
        image.set(1, 2, 8);
        image.set(2, 2, 9);
        GrayImage result = shift.apply(image);
        assert(result.get(0, 0) == 9);
        assert(result.get(1, 0) == 7);
        assert(result.get(2, 0) == 8);
        assert(result.get(0, 1) == 3);
        assert(result.get(1, 1) == 1);
        assert(result.get(2, 1) == 2);
        assert(result.get(0, 2) == 6);
        assert(result.get(1, 2) == 4);
        assert(result.get(2, 2) == 5);

        // Testing the non-wrap functionality
        Shift shift2 = new Shift(1, 1, Shift.NOWRAP);
        GrayImage result2 = shift2.apply(image);
        assert(result2.get(0, 0) == 0);
        assert(result2.get(1, 0) == 0);
        assert(result2.get(2, 0) == 0);
        assert(result2.get(0, 1) == 0);
        assert(result2.get(1, 1) == 1);
        assert(result2.get(2, 1) == 0);
        assert(result2.get(0, 2) == 0);
        assert(result2.get(1, 2) == 0);
        assert(result2.get(2, 2) == 0);
    }
}