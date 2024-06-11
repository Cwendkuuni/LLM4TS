package jigl.image.ops.levelOps;

import org.junit.Test;

import jigl.image.*;
import jigl.image.io.*;
import jigl.image.types.*;
import jigl.image.ops.*;
import jigl.image.ops.levelOps.Clip.*;

public class ClipTest {

    @Test
    public void testConstructorInt() {
        Clip clip = new Clip(0, 255);
        assertEquals(0, clip.int_min);
        assertEquals(255, clip.int_max);
    }

    @Test
    public void testConstructorFloat() {
        Clip clip = new Clip(0.0f, 1.0f);
        assertEquals(0.0f, clip.float_min, 0.001);
        assertEquals(1.0f, clip.float_max, 0.001);
    }

    @Test
    public void testApplyGrayImage() {
        GrayImage image = new GrayImage(10, 10);
        Clip clip = new Clip(0, 255);
        Image result = clip.apply(image);
        assertEquals(image, result);
    }

    @Test
    public void testApplyGrayImageWithROI() {
        GrayImage image = new GrayImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        Clip clip = new Clip(0, 255);
        Image result = clip.apply(image, roi);
        assertEquals(image, result);
    }

    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = new RealGrayImage(10, 10);
        Clip clip = new Clip(0.0f, 1.0f);
        Image result = clip.apply(image);
        assertEquals(image, result);
    }

    @Test
    public void testApplyRealGrayImageWithROI() {
        RealGrayImage image = new RealGrayImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        Clip clip = new Clip(0.0f, 1.0f);
        Image result = clip.apply(image, roi);
        assertEquals(image, result);
    }

    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage(10, 10);
        Clip clip = new Clip(0, 255);
        Image result = clip.apply(image);
        assertEquals(image, result);
    }

    @Test
    public void testApplyColorImageWithROI() {
        ColorImage image = new ColorImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        Clip clip = new Clip(0, 255);
        Image result = clip.apply(image, roi);
        assertEquals(image, result);
    }

    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new RealColorImage(10, 10);
        Clip clip = new Clip(0.0f, 1.0f);
        Image result = clip.apply(image);
        assertEquals(image, result);
    }

    @Test
    public void testApplyRealColorImageWithROI() {
        RealColorImage image = new RealColorImage(10, 10);
        ROI roi = new ROI(0, 0, 9, 9);
        Clip clip = new Clip(0.0f, 1.0f);
        Image result = clip.apply(image, roi);
        assertEquals(image, result);
    }
}