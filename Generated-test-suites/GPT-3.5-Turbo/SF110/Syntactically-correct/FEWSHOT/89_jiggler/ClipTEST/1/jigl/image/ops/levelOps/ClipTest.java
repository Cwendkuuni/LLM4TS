package jigl.image.ops.levelOps;


import jigl.image.*;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;
import jigl.image.ops.levelOps.Clip;
import jigl.image.types.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClipTest {

    //Gray Image tests
    @Test
    public void testApply_GrayImage() {
        GrayImage image = new GrayImage(2, 2);
        image.set(0, 0, 0);
        image.set(0, 1, 1);
        image.set(1, 0, 2);
        image.set(1, 1, 3);

        Clip clip = new Clip(1, 2);
        Image result = clip.apply(image);
        assertTrue(result instanceof GrayImage);
        assertEquals(2, result.get(0, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(2, result.get(1, 0));
        assertEquals(2, result.get(1, 1));
    }

    @Test
    public void testApply_GrayImage_ROI() {
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 0);
        image.set(0, 1, 1);
        image.set(0, 2, 2);
        image.set(1, 0, 3);
        image.set(1, 1, 4);
        image.set(1, 2, 5);
        image.set(2, 0, 6);
        image.set(2, 1, 7);
        image.set(2, 2, 8);

        ROI roi = new ROI(1, 1, 2, 2);

        Clip clip = new Clip(2, 5);
        Image result = clip.apply(image, roi);
        assertTrue(result instanceof GrayImage);
        assertEquals(4, result.get(1, 1));
        assertEquals(4, result.get(1, 2));
        assertEquals(5, result.get(2, 1));
        assertEquals(5, result.get(2, 2));
    }

    //RealGray Image tests
    @Test
    public void testApply_RealGrayImage() {
        RealGrayImage image = new RealGrayImage(2, 2);
        image.set(0, 0, 0.0f);
        image.set(0, 1, 0.5f);
        image.set(1, 0, 1.0f);
        image.set(1, 1, 1.5f);

        Clip clip = new Clip(0.5f, 1.0f);
        Image result = clip.apply(image);
        assertTrue(result instanceof RealGrayImage);
        assertEquals(0.5f, result.get(0, 0));
        assertEquals(0.5f, result.get(0, 1));
        assertEquals(1.0f, result.get(1, 0));
        assertEquals(1.0f, result.get(1, 1));
    }

    @Test
    public void testApply_RealGrayImage_ROI() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 0.1f);
        image.set(0, 1, 0.2f);
        image.set(0, 2, 0.3f);
        image.set(1, 0, 0.4f);
        image.set(1, 1, 0.5f);
        image.set(1, 2, 0.6f);
        image.set(2, 0, 0.7f);
        image.set(2, 1, 0.8f);
        image.set(2, 2, 0.9f);

        ROI roi = new ROI(1, 1, 2, 2);

        Clip clip = new Clip(0.5f, 0.8f);
        Image result = clip.apply(image, roi);
        assertTrue(result instanceof RealGrayImage);
        assertEquals(0.5f, result.get(1, 1));
        assertEquals(0.5f, result.get(1, 2));
        assertEquals(0.8f, result.get(2, 1));
        assertEquals(0.8f, result.get(2, 2));
    }

    //Color Image tests
    @Test
    public void testApply_ColorImage() {
        ColorImage image = new ColorImage(2, 2);
        image.set(0, 0, new RGB(0, 0, 0));
        image.set(0, 1, new RGB(255, 255, 255));
        image.set(1, 0, new RGB(128, 128, 128));
        image.set(1, 1, new RGB(64, 64, 64));

        Clip clip = new Clip(100, 200);
        Image result = clip.apply(image);
        assertTrue(result instanceof ColorImage);
        assertEquals(new RGB(100, 100, 100), result.get(0, 0));
        assertEquals(new RGB(200, 200, 200), result.get(0, 1));
        assertEquals(new RGB(128, 128, 128), result.get(1, 0));
        assertEquals(new RGB(100, 100, 100), result.get(1, 1));
    }

    @Test
    public void testApply_ColorImage_ROI() {
        ColorImage image = new ColorImage(3, 3);
        image.set(0, 0, new RGB(0, 0, 0));
        image.set(0, 1, new RGB(255, 255, 255));
        image.set(0, 2, new RGB(128, 128, 128));
        image.set(1, 0, new RGB(64, 64, 64));
        image.set(1, 1, new RGB(192, 192, 192));
        image.set(1, 2, new RGB(32, 32, 32));
        image.set(2, 0, new RGB(128, 128, 128));
        image.set(2, 1, new RGB(64, 64, 64));
        image.set(2, 2, new RGB(192, 192, 192));

        ROI roi = new ROI(1, 1, 2, 2);

        Clip clip = new Clip(100, 200);
        Image result = clip.apply(image, roi);
        assertTrue(result instanceof ColorImage);
        assertEquals(new RGB(100, 100, 100), result.get(1, 1));
        assertEquals(new RGB(100, 100, 100), result.get(1, 2));
        assertEquals(new RGB(200, 200, 200), result.get(2, 1));
        assertEquals(new RGB(200, 200, 200), result.get(2, 2));
    }
    
    @Test
    public void testMain() {
        String[] argv = {"0.5", "0.8", "inputfile", "outputfile"};
        
        assertTrue(Clip.main(argv));
        
        // add assertions for input/output files
    }
}