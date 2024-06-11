import static org.junit.Assert.*; 
import org.junit.Test;
import jigl.image.*;
import jigl.image.io.*;
import jigl.image.ops.levelOps.*;
import jigl.image.types.*;
import java.util.ArrayList;

public class ClipTest {
    
    @Test
    public void testApply_GrayImage() {
        Clip clip = new Clip(10, 20);
        GrayImage image = new GrayImage(10, 10);
        // Set some values for testing
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, (x + y) % 30);
            }
        }
        Image result = clip.apply(image);
        
        // Check that values are clipped
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                int value = result.get(x, y);
                assertTrue(value >= 10 && value <= 20);
            }
        }
    }
    
    @Test
    public void testApply_GrayImage_ROI() {
        Clip clip = new Clip(10, 20);
        GrayImage image = new GrayImage(10, 10);
        ROI roi = new ROI(0, 0, 5, 5);
        // Set some values for testing
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, (x + y) % 30);
            }
        }
        Image result = clip.apply(image, roi);
        
        // Check that values are clipped within ROI
        for (int y = 0; y <= roi.ly() - roi.uy(); y++) {
            for (int x = 0; x <= roi.lx() - roi.ux(); x++) {
                int value = result.get(x, y);
                assertTrue(value >= 10 && value <= 20);
            }
        }
        // Check that values outside ROI are unchanged
        for (int y = roi.ly() + 1; y < image.Y(); y++) {
            for (int x = roi.lx() + 1; x < image.X(); x++) {
                int value = result.get(x, y);
                assertEquals(value, image.get(x, y));
            }
        }
    }
    
    @Test
    public void testApply_RealGrayImage() {
        Clip clip = new Clip(10, 20);
        RealGrayImage image = new RealGrayImage(10, 10);
        // Set some values for testing
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, (float)(x + y) % 30);
            }
        }
        Image result = clip.apply(image);
        
        // Check that values are clipped
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                float value = result.get(x, y);
                assertTrue(value >= 10 && value <= 20);
            }
        }
    }
    
    @Test
    public void testApply_RealGrayImage_ROI() {
        Clip clip = new Clip(10, 20);
        RealGrayImage image = new RealGrayImage(10, 10);
        ROI roi = new ROI(0, 0, 5, 5);
        // Set some values for testing
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, (float)(x + y) % 30);
            }
        }
        Image result = clip.apply(image, roi);
        
        // Check that values are clipped within ROI
        for (int y = 0; y <= roi.ly() - roi.uy(); y++) {
            for (int x = 0; x <= roi.lx() - roi.ux(); x++) {
                float value = result.get(x, y);
                assertTrue(value >= 10 && value <= 20);
            }
        }
        // Check that values outside ROI are unchanged
        for (int y = roi.ly() + 1; y < image.Y(); y++) {
            for (int x = roi.lx() + 1; x < image.X(); x++) {
                float value = result.get(x, y);
                assertEquals(value, image.get(x, y), 0.000001);
            }
        }
    }
    
    @Test
    public void testApply_ColorImage() {
        Clip clip = new Clip(10, 20);
        ColorImage image = new ColorImage(10, 10);
        // Set some values for testing
        for (int p = 0; p < 3; p++) {
            for (int y = 0; y < image.Y(); y++) {
                for (int x = 0; x < image.X(); x++) {
                    int value = (x + y + p) % 30;
                    image.set(x, y, p, value);
                }
            }
        }
        Image result = clip.apply(image);
        
        // Check that values are clipped for each plane
        for (int p = 0; p < 3; p++) {
            for (int y = 0; y < image.Y(); y++) {
                for (int x = 0; x < image.X(); x++) {
                    int value = result.get(x, y, p);
                    assertTrue(value >= 10 && value <= 20);
                }
            }
        }
    }
    
    @Test
    public void testApply_ColorImage_ROI() {
        Clip clip = new Clip(10, 20);
        ColorImage image = new ColorImage(10, 10);
        ROI roi = new ROI(0, 0, 5, 5);
        // Set some values for testing
        for (int p = 0; p < 3; p++) {
            for (int y = 0; y < image.Y(); y++) {
                for (int x = 0; x < image.X(); x++) {
                    int value = (x + y + p) % 30;
                    image.set(x, y, p, value);
                }
            }
        }
        Image result = clip.apply(image, roi);
        
        // Check that values are clipped within ROI for each plane
        for (int p = 0; p < 3; p++) {
            for (int y = 0; y <= roi.ly() - roi.uy(); y++) {
                for (int x = 0; x <= roi.lx() - roi.ux(); x++) {
                    int value = result.get(x, y, p);
                    assertTrue(value >= 10 && value <= 20);
                }
            }
        }
        // Check that values outside ROI are unchanged for each plane
        for (int p = 0; p < 3; p++) {
            for (int y = roi.ly() + 1; y < image.Y(); y++) {
                for (int x = roi.lx() + 1; x < image.X(); x++) {
                    int value = result.get(x, y, p);
                    assertEquals(value, image.get(x, y, p));
                }
            }
        }
    }
    
    @Test
    public void testApply_RealColorImage() {
        Clip clip = new Clip(10, 20);
        RealColorImage image = new RealColorImage(10, 10);
        // Set some values for testing
        for (int p = 0; p < 3; p++) {
            for (int y = 0; y < image.Y(); y++) {
                for (int x = 0; x < image.X(); x++) {
                    float value = (float)(x + y + p) % 30;
                    image.set(x, y, p, value);
                }
            }
        }
        Image result = clip.apply(image);
        
        // Check that values are clipped for each plane
        for (int p = 0; p < 3; p++) {
            for (int y = 0; y < image.Y(); y++) {
                for (int x = 0; x < image.X(); x++) {
                    float value = result.get(x, y, p);
                    assertTrue(value >= 10 && value <= 20);
                }
            }
        }
    }
    
    @Test
    public void testApply_RealColorImage_ROI() {
        Clip clip = new Clip(10, 20);
        RealColorImage image = new RealColorImage(10, 10);
        ROI roi = new ROI(0, 0, 5, 5);
        // Set some values for testing
        for (int p = 0; p < 3; p++) {
            for (int y = 0; y < image.Y(); y++) {
                for (int x = 0; x < image.X(); x++) {
                    float value = (float)(x + y + p) % 30;
                    image.set(x, y, p, value);
                }
            }
        }
        Image result = clip.apply(image, roi);
        
        // Check that values are clipped within ROI for each plane
        for (int p = 0; p < 3; p++) {
            for (int y = 0; y <= roi.ly() - roi.uy(); y++) {
                for (int x = 0; x <= roi.lx() - roi.ux(); x++) {
                    float value = result.get(x, y, p);
                    assertTrue(value >= 10 && value <= 20);
                }
            }
        }
        // Check that values outside ROI are unchanged for each plane
        for (int p = 0; p < 3; p++) {
            for (int y = roi.ly() + 1; y < image.Y(); y++) {
                for (int x = roi.lx() + 1; x < image.X(); x++) {
                    float value = result.get(x, y, p);
                    assertEquals(value, image.get(x, y, p), 0.000001);
                }
            }
        }
    }
    
    @Test
    public void testMain() {
        // Test main method with sample inputs
        String[] argv = new String[]{"10.0", "20.0", "input.png", "output.png"};
        Clip.main(argv);
        Clip clip = new Clip(10, 20);
        
        ImageInputStream is = new ImageInputStream("input.png");
        Image image = is.read();
        is.close();
        
        Image result = clip.apply(image);
        
        ImageOutputStream os = new ImageOutputStream("output.png");
        os.write(result);
        os.close();
        
        // Manual inspection of output file required
        // This test only checks that the program executes without errors
    }
}