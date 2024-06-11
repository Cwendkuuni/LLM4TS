package jigl.image.ops.levelOps;

import static org.junit.Assert.assertEquals;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Test;
import jigl.image.*;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;
import jigl.image.types.ColorImage;
import jigl.image.types.FloatImage;
import jigl.image.types.GrayImage;
import jigl.image.types.Image;
import jigl.image.types.RealColorImage;
import jigl.image.types.RealGrayImage;

public class ClipTest {

    @Test
    public void testConstructor_int() {
        int min = 10;
        int max = 20;
        Clip clip = new Clip(min, max);
        
        try {
            Field intMinField = clip.getClass().getDeclaredField("int_min");
            intMinField.setAccessible(true);
            int actualMin = (int) intMinField.get(clip);
            assertEquals(min, actualMin);
            
            Field intMaxField = clip.getClass().getDeclaredField("int_max");
            intMaxField.setAccessible(true);
            int actualMax = (int) intMaxField.get(clip);
            assertEquals(max, actualMax);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConstructor_float() {
        float min = 10.0f;
        float max = 20.0f;
        Clip clip = new Clip(min, max);
        
        try {
            Field floatMinField = clip.getClass().getDeclaredField("float_min");
            floatMinField.setAccessible(true);
            float actualMin = (float) floatMinField.get(clip);
            assertEquals(min, actualMin, 0.001);
            
            Field floatMaxField = clip.getClass().getDeclaredField("float_max");
            floatMaxField.setAccessible(true);
            float actualMax = (float) floatMaxField.get(clip);
            assertEquals(max, actualMax, 0.001);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testApply_GrayImage() {
        Clip clip = new Clip(10, 20);
        GrayImage image = new GrayImage(new int[][]{{5, 15, 25}, {10, 20, 30}});
        
        image = (GrayImage) clip.apply(image);
        
        int[][] expected = {{10, 15, 20}, {10, 20, 20}};
        assertEquals(expected, image.getData());
    }
    
    @Test
    public void testApply_GrayImage_withROI() {
        Clip clip = new Clip(10, 20);
        GrayImage image = new GrayImage(new int[][]{{5, 15, 25}, {10, 20, 30}});
        ROI roi = new ROI(0, 0, 1, 1);
        
        image = (GrayImage) clip.apply(image, roi);
        
        int[][] expected = {{10, 15, 25}, {10, 15, 30}};
        assertEquals(expected, image.getData());
    }

    @Test
    public void testApply_RealGrayImage() {
        Clip clip = new Clip(10.0f, 20.0f);
        RealGrayImage image = new RealGrayImage(new float[][]{{5.0f, 15.0f, 25.0f}, {10.0f, 20.0f, 30.0f}});
        
        image = (RealGrayImage) clip.apply(image);
        
        float[][] expected = {{10.0f, 15.0f, 20.0f}, {10.0f, 20.0f, 20.0f}};
        assertEquals(expected, image.getData());
    }

    @Test
    public void testApply_RealGrayImage_withROI() {
        Clip clip = new Clip(10.0f, 20.0f);
        RealGrayImage image = new RealGrayImage(new float[][]{{5.0f, 15.0f, 25.0f}, {10.0f, 20.0f, 30.0f}});
        ROI roi = new ROI(0, 0, 1, 1);
        
        image = (RealGrayImage) clip.apply(image, roi);
        
        float[][] expected = {{10.0f, 15.0f, 25.0f}, {10.0f, 15.0f, 30.0f}};
        assertEquals(expected, image.getData());
    }

    @Test
    public void testApply_ColorImage() {
        Clip clip = new Clip(10, 20);
        ColorImage image = new ColorImage(new int[][][]{{{5, 15, 25}}, {{10, 20, 30}}});
        
        image = (ColorImage) clip.apply(image);
        
        int[][][] expected = {{{10, 15, 20}}, {{10, 20, 20}}};
        assertEquals(expected, image.getData());
    }

    @Test
    public void testApply_ColorImage_withROI() {
        Clip clip = new Clip(10, 20);
        ColorImage image = new ColorImage(new int[][][]{{{5, 15, 25}}, {{10, 20, 30}}});
        ROI roi = new ROI(0, 0, 0, 0);
        
        image = (ColorImage) clip.apply(image, roi);
        
        int[][][] expected = {{{10, 15, 25}}, {{10, 20, 30}}};
        assertEquals(expected, image.getData());
    }

    @Test
    public void testApply_RealColorImage() {
        Clip clip = new Clip(10.0f, 20.0f);
        RealColorImage image = new RealColorImage(new float[][][]{{{5.0f, 15.0f, 25.0f}}, {{10.0f, 20.0f, 30.0f}}});
        
        image = (RealColorImage) clip.apply(image);
        
        float[][][] expected = {{{10.0f, 15.0f, 20.0f}}, {{10.0f, 20.0f, 20.0f}}};
        assertEquals(expected, image.getData());
    }

    @Test
    public void testApply_RealColorImage_withROI() {
        Clip clip = new Clip(10.0f, 20.0f);
        RealColorImage image = new RealColorImage(new float[][][]{{{5.0f, 15.0f, 25.0f}}, {{10.0f, 20.0f, 30.0f}}});
        ROI roi = new ROI(0, 0, 0, 0);
        
        image = (RealColorImage) clip.apply(image, roi);
        
        float[][][] expected = {{{10.0f, 15.0f, 25.0f}}, {{10.0f, 20.0f, 30.0f}}};
        assertEquals(expected, image.getData());
    }

    @Test
    public void testMain() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        String[] argv = new String[] {"10.0", "20.0", "inputfile", "outputfile"};
        Method mainMethod = Clip.class.getDeclaredMethod("main", String[].class);
        mainMethod.setAccessible(true);
        mainMethod.invoke(null, (Object) argv);
    }

    @Test
    public void testMain_withException() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        String[] argv = new String[] {"10.0", "20.0", "inputfile", "outputfile"};
        Method mainMethod = Clip.class.getDeclaredMethod("main", String[].class);
        mainMethod.setAccessible(true);
        mainMethod.invoke(null, (Object) argv);
    }
}