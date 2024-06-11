import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;

public class ShiftTest {

    private Shift shift;
    private GrayImage grayImage;
    private RealGrayImage realGrayImage;
    private ColorImage colorImage;
    private RealColorImage realColorImage;

    @Before
    public void setUp() {
        shift = new Shift(2, 3, 1);
        grayImage = new GrayImage(5, 5);
        realGrayImage = new RealGrayImage(5, 5);
        colorImage = new ColorImage(5, 5);
        realColorImage = new RealColorImage(5, 5);
    }

    @Test
    public void testApplyWithGrayImage() {
        GrayImage result = shift.apply(grayImage);
        // perform assertions here
    }

    @Test
    public void testApplyWithRealGrayImage() {
        RealGrayImage result = shift.apply(realGrayImage);
        // perform assertions here
    }

    @Test
    public void testApplyWithColorImage() {
        ColorImage result = shift.apply(colorImage);
        // perform assertions here
    }

    @Test
    public void testApplyWithRealColorImage() {
        RealColorImage result = shift.apply(realColorImage);
        // perform assertions here
    }

}