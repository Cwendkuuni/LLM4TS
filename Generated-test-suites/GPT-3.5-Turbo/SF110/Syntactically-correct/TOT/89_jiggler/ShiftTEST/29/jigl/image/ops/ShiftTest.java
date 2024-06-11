import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShiftTest {

    private Shift shift;
    private GrayImage grayImage;
    private RealGrayImage realGrayImage;
    private ColorImage colorImage;
    private RealColorImage realColorImage;

    @BeforeEach
    public void setUp() {
        shift = new Shift(2, 1, 1);
        grayImage = new GrayImage(4, 4);
        realGrayImage = new RealGrayImage(4, 4);
        colorImage = new ColorImage(4, 4);
        realColorImage = new RealColorImage(4, 4);
    }

    @Test
    public void testApplyGrayImageWrap() {
        grayImage.set(0, 0, 5);
        grayImage.set(2, 1, 10);
        GrayImage result = shift.apply(grayImage);
        Assertions.assertEquals(0, result.get(0, 0));
        Assertions.assertEquals(10, result.get(2, 2));
    }

    @Test
    public void testApplyGrayImageNoWrap() {
        grayImage.set(0, 0, 5);
        grayImage.set(2, 1, 10);
        shift = new Shift(5, 2, 0);
        GrayImage result = shift.apply(grayImage);
        Assertions.assertEquals(0, result.get(0, 0));
        Assertions.assertEquals(0, result.get(2, 1));
    }

    @Test
    public void testApplyRealGrayImageWrap() {
        realGrayImage.set(0, 1, 3.5);
        realGrayImage.set(3, 2, 7.2);
        RealGrayImage result = shift.apply(realGrayImage);
        Assertions.assertEquals(3.5, result.get(0, 1));
        Assertions.assertEquals(7.2, result.get(3, 0));
    }

    @Test
    public void testApplyRealGrayImageNoWrap() {
        realGrayImage.set(0, 1, 3.5);
        realGrayImage.set(3, 2, 7.2);
        shift = new Shift(5, 2, 0);
        RealGrayImage result = shift.apply(realGrayImage);
        Assertions.assertEquals(0.0, result.get(0, 1));
        Assertions.assertEquals(0.0, result.get(3, 2));
    }

    @Test
    public void testApplyColorImageWrap() {
        colorImage.set(0, 1, 255, 0, 0);
        colorImage.set(3, 2, 0, 255, 0);
        ColorImage result = shift.apply(colorImage);
        Assertions.assertArrayEquals(new int[]{255, 0, 0}, result.get(0, 1));
        Assertions.assertArrayEquals(new int[]{0, 255, 0}, result.get(3, 0));
    }

    @Test
    public void testApplyColorImageNoWrap() {
        colorImage.set(0, 1, 255, 0, 0);
        colorImage.set(3, 2, 0, 255, 0);
        shift = new Shift(5, 2, 0);
        ColorImage result = shift.apply(colorImage);
        Assertions.assertArrayEquals(new int[]{0, 0, 0}, result.get(0, 1));
        Assertions.assertArrayEquals(new int[]{0, 0, 0}, result.get(3, 2));
    }

    @Test
    public void testApplyRealColorImageWrap() {
        realColorImage.set(0, 1, 1.1f, 0.0f, 0.0f);
        realColorImage.set(3, 2, 0.0f, 2.2f, 0.0f);
        RealColorImage result = shift.apply(realColorImage);
        Assertions.assertArrayEquals(new float[]{1.1f, 0.0f, 0.0f}, result.get(0, 1));
        Assertions.assertArrayEquals(new float[]{0.0f, 2.2f, 0.0f}, result.get(3, 0));
    }

    @Test
    public void testApplyRealColorImageNoWrap() {
        realColorImage.set(0, 1, 1.1f, 0.0f, 0.0f);
        realColorImage.set(3, 2, 0.0f, 2.2f, 0.0f);
        shift = new Shift(5, 2, 0);
        RealColorImage result = shift.apply(realColorImage);
        Assertions.assertArrayEquals(new float[]{0.0f, 0.0f, 0.0f}, result.get(0, 1));
        Assertions.assertArrayEquals(new float[]{0.0f, 0.0f, 0.0f}, result.get(3, 2));
    }

}