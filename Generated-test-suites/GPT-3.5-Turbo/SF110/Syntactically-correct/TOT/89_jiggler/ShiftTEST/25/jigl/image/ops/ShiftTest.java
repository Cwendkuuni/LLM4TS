import static org.junit.Assert.*;
import org.junit.*;
import jigl.image.*;
import jigl.image.ops.*;

public class ShiftTest {
    private GrayImage grayImage;
    private RealGrayImage realGrayImage;
    private ColorImage colorImage;
    private RealColorImage realColorImage;
    
    @Before
    public void setUp() {
        // Initialize test images
        int[][] grayImageData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        grayImage = new GrayImage(grayImageData);
        
        float[][] realGrayImageData = {{1.0f, 2.0f, 3.0f}, {4.0f, 5.0f, 6.0f}, {7.0f, 8.0f, 9.0f}};
        realGrayImage = new RealGrayImage(realGrayImageData);
        
        int[][][] colorImageData = {{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, {{10, 11, 12}, {13, 14, 15}, {16, 17, 18}}, {{19, 20, 21}, {22, 23, 24}, {25, 26, 27}}};
        colorImage = new ColorImage(colorImageData);
        
        float[][][] realColorImageData = {{{1.0f, 2.0f, 3.0f}, {4.0f, 5.0f, 6.0f}, {7.0f, 8.0f, 9.0f}}, {{10.0f, 11.0f, 12.0f}, {13.0f, 14.0f, 15.0f}, {16.0f, 17.0f, 18.0f}}, {{19.0f, 20.0f, 21.0f}, {22.0f, 23.0f, 24.0f}, {25.0f, 26.0f, 27.0f}}};
        realColorImage = new RealColorImage(realColorImageData);
    }
    
    @Test
    public void testGrayImageApplyWrap() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        GrayImage result = shift.apply(grayImage);
        
        // Check if wrap is working correctly
        int[][] expectedData = {{9, 7, 8}, {3, 1, 2}, {6, 4, 5}};
        assertArrayEquals(expectedData, result.getData());
    }
    
    @Test
    public void testGrayImageApplyNoWrap() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        GrayImage result = shift.apply(grayImage);
        
        // Check if no wrap is working correctly
        int[][] expectedData = {{0, 0, 0}, {0, 1, 2}, {0, 4, 5}};
        assertArrayEquals(expectedData, result.getData());
    }
    
    @Test
    public void testRealGrayImageApplyWrap() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealGrayImage result = shift.apply(realGrayImage);
        
        // Check if wrap is working correctly
        float[][] expectedData = {{9.0f, 7.0f, 8.0f}, {3.0f, 1.0f, 2.0f}, {6.0f, 4.0f, 5.0f}};
        assertArrayEquals(expectedData, result.getData());
    }
    
    @Test
    public void testRealGrayImageApplyNoWrap() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealGrayImage result = shift.apply(realGrayImage);
        
        // Check if no wrap is working correctly
        float[][] expectedData = {{0.0f, 0.0f, 0.0f}, {0.0f, 1.0f, 2.0f}, {0.0f, 4.0f, 5.0f}};
        assertArrayEquals(expectedData, result.getData());
    }
    
    @Test
    public void testColorImageApplyWrap() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        ColorImage result = shift.apply(colorImage);
        
        // Check if wrap is working correctly for each color plane
        int[][][] expectedData = {
            {{9, 7, 8}, {3, 1, 2}, {6, 4, 5}},
            {{18, 16, 17}, {12, 10, 11}, {15, 13, 14}},
            {{27, 25, 26}, {21, 19, 20}, {24, 22, 23}}
        };
        assertArrayEquals(expectedData, result.getData());
    }
    
    @Test
    public void testColorImageApplyNoWrap() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        ColorImage result = shift.apply(colorImage);
        
        // Check if no wrap is working correctly for each color plane
        int[][][] expectedData = {
            {{0, 0, 0}, {0, 1, 2}, {0, 4, 5}},
            {{0, 0, 0}, {0, 10, 11}, {0, 13, 14}},
            {{0, 0, 0}, {0, 19, 20}, {0, 22, 23}}
        };
        assertArrayEquals(expectedData, result.getData());
    }
    
    @Test
    public void testRealColorImageApplyWrap() {
        Shift shift = new Shift(1, 1, Shift.WRAP);
        RealColorImage result = shift.apply(realColorImage);
        
        // Check if wrap is working correctly for each color plane
        float[][][] expectedData = {
            {{9.0f, 7.0f, 8.0f}, {3.0f, 1.0f, 2.0f}, {6.0f, 4.0f, 5.0f}},
            {{18.0f, 16.0f, 17.0f}, {12.0f, 10.0f, 11.0f}, {15.0f, 13.0f, 14.0f}},
            {{27.0f, 25.0f, 26.0f}, {21.0f, 19.0f, 20.0f}, {24.0f, 22.0f, 23.0f}}
        };
        assertArrayEquals(expectedData, result.getData());
    }
    
    @Test
    public void testRealColorImageApplyNoWrap() {
        Shift shift = new Shift(1, 1, Shift.NOWRAP);
        RealColorImage result = shift.apply(realColorImage);
        
        // Check if no wrap is working correctly for each color plane
        float[][][] expectedData = {
            {{0.0f, 0.0f, 0.0f}, {0.0f, 1.0f, 2.0f}, {0.0f, 4.0f, 5.0f}},
            {{0.0f, 0.0f, 0.0f}, {0.0f, 10.0f, 11.0f}, {0.0f, 13.0f, 14.0f}},
            {{0.0f, 0.0f, 0.0f}, {0.0f, 19.0f, 20.0f}, {0.0f, 22.0f, 23.0f}}
        };
        assertArrayEquals(expectedData, result.getData());
    }
}