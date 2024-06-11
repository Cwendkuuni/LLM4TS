
package jigl.image.ops.levelOps;

import jigl.image.*;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;
import jigl.image.ops.levelOps.Clip;
import jigl.image.types.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClipTest {
    private Clip clip;

    @Before
    public void setUp() {
        clip = new Clip(0, 0);
    }

    @Test
    public void testApply_withGrayImage() {
        // Create a sample GrayImage for testing
        GrayImage image = new ByteGrayImage(3, 3);
        image.fill(127);

        // Apply the Clip operation on the GrayImage
        Image result = clip.apply(image);

        // Check if the values are clipped correctly
        assertEquals(0, result.get(0, 0));
        assertEquals(0, result.get(2, 2));
        assertEquals(127, result.get(1, 1));
    }

    @Test
    public void testApply_withGrayImageAndROI() {
        // Create a sample GrayImage for testing
        GrayImage image = new ByteGrayImage(3, 3);
        image.fill(127);

        // Apply the Clip operation on the GrayImage with ROI
        ROI roi = new ROI(1, 1, 2, 2);
        Image result = clip.apply(image, roi);

        // Check if the values are clipped correctly within the ROI
        assertEquals(0, result.get(1, 1));
        assertEquals(0, result.get(2, 2));
        assertEquals(127, result.get(0, 0));
    }

    @Test
    public void testApply_withRealGrayImage() {
        // Create a sample RealGrayImage for testing
        RealGrayImage image = new RealGrayImage(3, 3);
        image.fill(0.5f);

        // Apply the Clip operation on the RealGrayImage
        Image result = clip.apply(image);

        // Check if the values are clipped correctly
        assertEquals(0, result.get(0, 0));
        assertEquals(0, result.get(2, 2));
        assertEquals(0.5f, result.get(1, 1));
    }

    @Test
    public void testApply_withRealGrayImageAndROI() {
        // Create a sample RealGrayImage for testing
        RealGrayImage image = new RealGrayImage(3, 3);
        image.fill(0.5f);

        // Apply the Clip operation on the RealGrayImage with ROI
        ROI roi = new ROI(1, 1, 2, 2);
        Image result = clip.apply(image, roi);

        // Check if the values are clipped correctly within the ROI
        assertEquals(0, result.get(1, 1));
        assertEquals(0, result.get(2, 2));
        assertEquals(0.5f, result.get(0, 0));
    }

    @Test
    public void testApply_withColorImage() {
        // Create a sample ColorImage for testing
        ColorImage image = new RGBColorImage(3, 3);
        image.fill(127);

        // Apply the Clip operation on the ColorImage
        Image result = clip.apply(image);

        // Check if the values in all planes are clipped correctly
        assertEquals(0, result.get(0, 0, 0));
        assertEquals(0, result.get(2, 2, 2));
        assertEquals(127, result.get(1, 1, 1));
    }

    @Test
    public void testApply_withColorImageAndROI() {
        // Create a sample ColorImage for testing
        ColorImage image = new RGBColorImage(3, 3);
        image.fill(127);

        // Apply the Clip operation on the ColorImage with ROI
        ROI roi = new ROI(1, 1, 2, 2);
        Image result = clip.apply(image, roi);

        // Check if the values in all planes are clipped correctly within the ROI
        assertEquals(0, result.get(1, 1, 1));
        assertEquals(0, result.get(2, 2, 2));
        assertEquals(127, result.get(0, 0, 0));
    }

    @Test
    public void testApply_withRealColorImage() {
        // Create a sample RealColorImage for testing
        RealColorImage image = new RealRGBColorImage(3, 3);
        image.fill(0.5f);

        // Apply the Clip operation on the RealColorImage
        Image result = clip.apply(image);

        // Check if the values in all planes are clipped correctly
        assertEquals(0, result.get(0, 0, 0));
        assertEquals(0, result.get(2, 2, 2));
        assertEquals(0.5f, result.get(1, 1, 1));
    }

    @Test
    public void testApply_withRealColorImageAndROI() {
        // Create a sample RealColorImage for testing
        RealColorImage image = new RealRGBColorImage(3, 3);
        image.fill(0.5f);

        // Apply the Clip operation on the RealColorImage with ROI
        ROI roi = new ROI(1, 1, 2, 2);
        Image result = clip.apply(image, roi);

        // Check if the values in all planes are clipped correctly within the ROI
        assertEquals(0, result.get(1, 1, 1));
        assertEquals(0, result.get(2, 2, 2));
        assertEquals(0.5f, result.get(0, 0, 0));
    }

    // Additional tests can be added to cover more scenarios

    @Test
    public void testMain() {
        // Test the main method with sample input and output files
        String[] argv = {"0.1", "0.5", "/path/to/inputfile", "/path/to/outputfile"};
        Clip.main(argv);
        // Check if the output file is generated successfully
        // Additional assertions can be added to check the contents of the output file
        assertTrue(new File(argv[3]).exists());
    }
}
