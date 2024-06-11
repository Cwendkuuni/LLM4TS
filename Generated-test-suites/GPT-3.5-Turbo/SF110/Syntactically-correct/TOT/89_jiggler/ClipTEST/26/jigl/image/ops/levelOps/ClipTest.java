
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClipTest {

    private Clip clip;
    private GrayImage grayImage;
    private RealGrayImage realGrayImage;
    private ColorImage colorImage;
    private RealColorImage realColorImage;

    @Before
    public void setup() {
        clip = new Clip(0, 255);
        grayImage = new GrayImage(3, 3);
        realGrayImage = new RealGrayImage(3, 3);
        colorImage = new ColorImage(3, 3);
        realColorImage = new RealColorImage(3, 3);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grayImage.set(i, j, i + j);
                realGrayImage.set(i, j, i + j);
                colorImage.set(i, j, i + j, i + j, i + j);
                realColorImage.set(i, j, i + j, i + j, i + j);
            }
        }
    }

    @Test
    public void testApply_GrayImage() {
        GrayImage result = (GrayImage) clip.apply(grayImage);

        assertEquals(0, result.get(0, 0));
        assertEquals(1, result.get(1, 0));
        assertEquals(2, result.get(2, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(2, result.get(1, 1));
        assertEquals(3, result.get(2, 1));
        assertEquals(2, result.get(0, 2));
        assertEquals(3, result.get(1, 2));
        assertEquals(4, result.get(2, 2));
    }

    @Test
    public void testApply_GrayImageWithROI() {
        ROI roi = new ROI(1, 1, 2, 2);
        GrayImage result = (GrayImage) clip.apply(grayImage, roi);

        assertEquals(1, result.get(1, 1));
        assertEquals(2, result.get(2, 1));
        assertEquals(2, result.get(1, 2));
        assertEquals(3, result.get(2, 2));
    }

    @Test
    public void testApply_RealGrayImage() {
        RealGrayImage result = (RealGrayImage) clip.apply(realGrayImage);

        assertEquals(0.0f, result.get(0, 0), 0.0001);
        assertEquals(1.0f, result.get(1, 0), 0.0001);
        assertEquals(2.0f, result.get(2, 0), 0.0001);
        assertEquals(1.0f, result.get(0, 1), 0.0001);
        assertEquals(2.0f, result.get(1, 1), 0.0001);
        assertEquals(3.0f, result.get(2, 1), 0.0001);
        assertEquals(2.0f, result.get(0, 2), 0.0001);
        assertEquals(3.0f, result.get(1, 2), 0.0001);
        assertEquals(4.0f, result.get(2, 2), 0.0001);
    }

    @Test
    public void testApply_RealGrayImageWithROI() {
        ROI roi = new ROI(1, 1, 2, 2);
        RealGrayImage result = (RealGrayImage) clip.apply(realGrayImage, roi);

        assertEquals(1.0f, result.get(1, 1), 0.0001);
        assertEquals(2.0f, result.get(2, 1), 0.0001);
        assertEquals(2.0f, result.get(1, 2), 0.0001);
        assertEquals(3.0f, result.get(2, 2), 0.0001);
    }

    @Test
    public void testApply_ColorImage() {
        ColorImage result = (ColorImage) clip.apply(colorImage);

        assertEquals(0, result.get(0, 0, 0));
        assertEquals(1, result.get(1, 0, 0));
        assertEquals(2, result.get(2, 0, 0));
        assertEquals(1, result.get(0, 1, 0));
        assertEquals(2, result.get(1, 1, 0));
        assertEquals(3, result.get(2, 1, 0));
        assertEquals(2, result.get(0, 2, 0));
        assertEquals(3, result.get(1, 2, 0));
        assertEquals(4, result.get(2, 2, 0));

        assertEquals(0, result.get(0, 0, 1));
        assertEquals(1, result.get(1, 0, 1));
        assertEquals(2, result.get(2, 0, 1));
        assertEquals(1, result.get(0, 1, 1));
        assertEquals(2, result.get(1, 1, 1));
        assertEquals(3, result.get(2, 1, 1));
        assertEquals(2, result.get(0, 2, 1));
        assertEquals(3, result.get(1, 2, 1));
        assertEquals(4, result.get(2, 2, 1));

        assertEquals(0, result.get(0, 0, 2));
        assertEquals(1, result.get(1, 0, 2));
        assertEquals(2, result.get(2, 0, 2));
        assertEquals(1, result.get(0, 1, 2));
        assertEquals(2, result.get(1, 1, 2));
        assertEquals(3, result.get(2, 1, 2));
        assertEquals(2, result.get(0, 2, 2));
        assertEquals(3, result.get(1, 2, 2));
        assertEquals(4, result.get(2, 2, 2));
    }

    @Test
    public void testApply_ColorImageWithROI() {
        ROI roi = new ROI(1, 1, 2, 2);
        ColorImage result = (ColorImage) clip.apply(colorImage, roi);

        assertEquals(1, result.get(1, 1, 0));
        assertEquals(2, result.get(2, 1, 0));
        assertEquals(2, result.get(1, 2, 0));
        assertEquals(3, result.get(2, 2, 0));

        assertEquals(1, result.get(1, 1, 1));
        assertEquals(2, result.get(2, 1, 1));
        assertEquals(2, result.get(1, 2, 1));
        assertEquals(3, result.get(2, 2, 1));

        assertEquals(1, result.get(1, 1, 2));
        assertEquals(2, result.get(2, 1, 2));
        assertEquals(2, result.get(1, 2, 2));
        assertEquals(3, result.get(2, 2, 2));
    }

    @Test
    public void testApply_RealColorImage() {
        RealColorImage result = (RealColorImage) clip.apply(realColorImage);

        assertEquals(0.0f, result.get(0, 0, 0), 0.0001);
        assertEquals(1.0f, result.get(1, 0, 0), 0.0001);
        assertEquals(2.0f, result.get(2, 0, 0), 0.0001);
        assertEquals(1.0f, result.get(0, 1, 0), 0.0001);
        assertEquals(2.0f, result.get(1, 1, 0), 0.0001);
        assertEquals(3.0f, result.get(2, 1, 0), 0.0001);
        assertEquals(2.0f, result.get(0, 2, 0), 0.0001);
        assertEquals(3.0f, result.get(1, 2, 0), 0.0001);
        assertEquals(4.0f, result.get(2, 2, 0), 0.0001);

        assertEquals(0.0f, result.get(0, 0, 1), 0.0001);
        assertEquals(1.0f, result.get(1, 0, 1), 0.0001);
        assertEquals(2.0f, result.get(2, 0, 1), 0.0001);
        assertEquals(1.0f, result.get(0, 1, 1), 0.0001);
        assertEquals(2.0f, result.get(1, 1, 1), 0.0001);
        assertEquals(3.0f, result.get(2, 1, 1), 0.0001);
        assertEquals(2.0f, result.get(0, 2, 1), 0.0001);
        assertEquals(3.0f, result.get(1, 2, 1), 0.0001);
        assertEquals(4.0f, result.get(2, 2, 1), 0.0001);

        assertEquals(0.0f, result.get(0, 0, 2), 0.0001);
        assertEquals(1.0f, result.get(1, 0, 2), 0.0001);
        assertEquals(2.0f, result.get(2, 0, 2), 0.0001);
        assertEquals(1.0f, result.get(0, 1, 2), 0.0001);
        assertEquals(2.0f, result.get(1, 1, 2), 0.0001);
        assertEquals(3.0f, result.get(2, 1, 2), 0.0001);
        assertEquals(2.0f, result.get(0, 2, 2), 0.0001);
        assertEquals(3.0f, result.get(1, 2, 2), 0.0001);
        assertEquals(4.0f, result.get(2, 2, 2), 0.0001);
    }

    @Test
    public void testApply_RealColorImageWithROI() {
        ROI roi = new ROI(1, 1, 2, 2);
        RealColorImage result = (RealColorImage) clip.apply(realColorImage, roi);

        assertEquals(1.0f, result.get(1, 1, 0), 0.0001);
        assertEquals(2.0f, result.get(2, 1, 0), 0.0001);
        assertEquals(2.0f, result.get(1, 2, 0), 0.0001);
        assertEquals(3.0f, result.get(2, 2, 0), 0.0001);

        assertEquals(1.0f, result.get(1, 1, 1), 0.0001);
        assertEquals(2.0f, result.get(2, 1, 1), 0.0001);
        assertEquals(2.0f, result.get(1, 2, 1), 0.0001);
        assertEquals(3.0f, result.get(2, 2, 1), 0.0001);

        assertEquals(1.0f, result.get(1, 1, 2), 0.0001);
        assertEquals(2.0f, result.get(2, 1, 2), 0.0001);
        assertEquals(2.0f, result.get(1, 2, 2), 0.0001);
        assertEquals(3.0f, result.get(2, 2, 2), 0.0001);
    }

    // Add more test cases for edge cases and error scenarios if necessary

}
