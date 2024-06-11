import static org.junit.Assert.*;
import org.junit.Test;

public class ClipTest {

    @Test
    public void testApply_GrayImage_Typical() {
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 5);
        image.set(1, 0, 10);
        image.set(2, 0, 15);
        image.set(0, 1, 20);
        image.set(1, 1, 25);
        image.set(2, 1, 30);
        image.set(0, 2, 35);
        image.set(1, 2, 40);
        image.set(2, 2, 45);
        
        Clip clip = new Clip(10, 30);
        GrayImage result = clip.apply(image);
        
        assertEquals(10, result.get(0, 0));
        assertEquals(10, result.get(1, 0));
        assertEquals(15, result.get(2, 0));
        assertEquals(20, result.get(0, 1));
        assertEquals(25, result.get(1, 1));
        assertEquals(30, result.get(2, 1));
        assertEquals(30, result.get(0, 2));
        assertEquals(30, result.get(1, 2));
        assertEquals(30, result.get(2, 2));
    }
    
    @Test
    public void testApply_GrayImage_Edge() {
        GrayImage image = new GrayImage(1, 1);
        image.set(0, 0, 100);
        
        Clip clip = new Clip(0, 255);
        GrayImage result = clip.apply(image);
        
        assertEquals(100, result.get(0, 0));
    }
    
    @Test
    public void testApply_GrayImage_Error() {
        GrayImage image = null;
        
        Clip clip = new Clip(0, 255);
        GrayImage result = clip.apply(image);
        
        assertNull(result);
    }
    
    @Test
    public void testApply_RealGrayImage_Typical() {
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 5.0);
        image.set(1, 0, 10.0);
        image.set(2, 0, 15.0);
        image.set(0, 1, 20.0);
        image.set(1, 1, 25.0);
        image.set(2, 1, 30.0);
        image.set(0, 2, 35.0);
        image.set(1, 2, 40.0);
        image.set(2, 2, 45.0);
        
        Clip clip = new Clip(10.0f, 30.0f);
        RealGrayImage result = clip.apply(image);
        
        assertEquals(10.0, result.get(0, 0), 0.001);
        assertEquals(10.0, result.get(1, 0), 0.001);
        assertEquals(15.0, result.get(2, 0), 0.001);
        assertEquals(20.0, result.get(0, 1), 0.001);
        assertEquals(25.0, result.get(1, 1), 0.001);
        assertEquals(30.0, result.get(2, 1), 0.001);
        assertEquals(30.0, result.get(0, 2), 0.001);
        assertEquals(30.0, result.get(1, 2), 0.001);
        assertEquals(30.0, result.get(2, 2), 0.001);
    }
    
    @Test
    public void testApply_RealGrayImage_Edge() {
        RealGrayImage image = new RealGrayImage(1, 1);
        image.set(0, 0, 100.0);
        
        Clip clip = new Clip(0.0f, 255.0f);
        RealGrayImage result = clip.apply(image);
        
        assertEquals(100.0, result.get(0, 0), 0.001);
    }
    
    @Test
    public void testApply_RealGrayImage_Error() {
        RealGrayImage image = null;
        
        Clip clip = new Clip(0.0f, 255.0f);
        RealGrayImage result = clip.apply(image);
        
        assertNull(result);
    }
    
    @Test
    public void testApply_ColorImage_Typical() {
        ColorImage image = new ColorImage(2, 2);
        GrayImage plane0 = new GrayImage(2, 2);
        plane0.set(0, 0, 5);
        plane0.set(1, 0, 10);
        plane0.set(0, 1, 15);
        plane0.set(1, 1, 20);
        image.setPlane(0, plane0);
        
        GrayImage plane1 = new GrayImage(2, 2);
        plane1.set(0, 0, 25);
        plane1.set(1, 0, 30);
        plane1.set(0, 1, 35);
        plane1.set(1, 1, 40);
        image.setPlane(1, plane1);
        
        GrayImage plane2 = new GrayImage(2, 2);
        plane2.set(0, 0, 45);
        plane2.set(1, 0, 50);
        plane2.set(0, 1, 55);
        plane2.set(1, 1, 60);
        image.setPlane(2, plane2);
        
        Clip clip = new Clip(10, 30);
        ColorImage result = clip.apply(image);
        
        assertEquals(10, result.plane(0).get(0, 0));
        assertEquals(10, result.plane(0).get(1, 0));
        assertEquals(15, result.plane(0).get(0, 1));
        assertEquals(20, result.plane(0).get(1, 1));
        
        assertEquals(25, result.plane(1).get(0, 0));
        assertEquals(30, result.plane(1).get(1, 0));
        assertEquals(30, result.plane(1).get(0, 1));
        assertEquals(30, result.plane(1).get(1, 1));
        
        assertEquals(30, result.plane(2).get(0, 0));
        assertEquals(30, result.plane(2).get(1, 0));
        assertEquals(30, result.plane(2).get(0, 1));
        assertEquals(30, result.plane(2).get(1, 1));
    }
    
    @Test
    public void testApply_ColorImage_Edge() {
        ColorImage image = new ColorImage(1, 1);
        GrayImage plane0 = new GrayImage(1, 1);
        plane0.set(0, 0, 100);
        image.setPlane(0, plane0);
        
        GrayImage plane1 = new GrayImage(1, 1);
        plane1.set(0, 0, 200);
        image.setPlane(1, plane1);
        
        GrayImage plane2 = new GrayImage(2, 2);
        plane2.set(0, 0, 300);
        image.setPlane(2, plane2);
        
        Clip clip = new Clip(0, 255);
        ColorImage result = clip.apply(image);
        
        assertEquals(100, result.plane(0).get(0, 0));
        assertEquals(200, result.plane(1).get(0, 0));
        assertEquals(255, result.plane(2).get(0, 0));
    }
    
    @Test
    public void testApply_ColorImage_Error() {
        ColorImage image = null;
        
        Clip clip = new Clip(0, 255);
        ColorImage result = clip.apply(image);
        
        assertNull(result);
    }
    
    @Test
    public void testApply_RealColorImage_Typical() {
        RealColorImage image = new RealColorImage(2, 2);
        RealGrayImage plane0 = new RealGrayImage(2, 2);
        plane0.set(0, 0, 5.0);
        plane0.set(1, 0, 10.0);
        plane0.set(0, 1, 15.0);
        plane0.set(1, 1, 20.0);
        image.setPlane(0, plane0);
        
        RealGrayImage plane1 = new RealGrayImage(2, 2);
        plane1.set(0, 0, 25.0);
        plane1.set(1, 0, 30.0);
        plane1.set(0, 1, 35.0);
        plane1.set(1, 1, 40.0);
        image.setPlane(1, plane1);
        
        RealGrayImage plane2 = new RealGrayImage(2, 2);
        plane2.set(0, 0, 45.0);
        plane2.set(1, 0, 50.0);
        plane2.set(0, 1, 55.0);
        plane2.set(1, 1, 60.0);
        image.setPlane(2, plane2);
        
        Clip clip = new Clip(10.0f, 30.0f);
        RealColorImage result = clip.apply(image);
        
        assertEquals(10.0, result.plane(0).get(0, 0), 0.001);
        assertEquals(10.0, result.plane(0).get(1, 0), 0.001);
        assertEquals(15.0, result.plane(0).get(0, 1), 0.001);
        assertEquals(20.0, result.plane(0).get(1, 1), 0.001);
        
        assertEquals(25.0, result.plane(1).get(0, 0), 0.001);
        assertEquals(30.0, result.plane(1).get(1, 0), 0.001);
        assertEquals(30.0, result.plane(1).get(0, 1), 0.001);
        assertEquals(30.0, result.plane(1).get(1, 1), 0.001);
        
        assertEquals(30.0, result.plane(2).get(0, 0), 0.001);
        assertEquals(30.0, result.plane(2).get(1, 0), 0.001);
        assertEquals(30.0, result.plane(2).get(0, 1), 0.001);
        assertEquals(30.0, result.plane(2).get(1, 1), 0.001);
    }
    
    @Test
    public void testApply_RealColorImage_Edge() {
        RealColorImage image = new RealColorImage(1, 1);
        RealGrayImage plane0 = new RealGrayImage(1, 1);
        plane0.set(0, 0, 100.0);
        image.setPlane(0, plane0);
        
        RealGrayImage plane1 = new RealGrayImage(1, 1);
        plane1.set(0, 0, 200.0);
        image.setPlane(1, plane1);
        
        RealGrayImage plane2 = new RealGrayImage(2, 2);
        plane2.set(0, 0, 300.0);
        image.setPlane(2, plane2);
        
        Clip clip = new Clip(0.0f, 255.0f);
        RealColorImage result = clip.apply(image);
        
        assertEquals(100.0, result.plane(0).get(0, 0), 0.001);
        assertEquals(200.0, result.plane(1).get(0, 0), 0.001);
        assertEquals(255.0, result.plane(2).get(0, 0), 0.001);
    }
    
    @Test
    public void testApply_RealColorImage_Error() {
        RealColorImage image = null;
        
        Clip clip = new Clip(0.0f, 255.0f);
        RealColorImage result = clip.apply(image);
        
        assertNull(result);
    }
}