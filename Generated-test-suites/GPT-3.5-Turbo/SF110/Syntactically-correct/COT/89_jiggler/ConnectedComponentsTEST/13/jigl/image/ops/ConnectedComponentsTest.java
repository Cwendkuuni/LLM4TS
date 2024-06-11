package jigl.image.ops;

import jigl.image.*;
import jigl.image.exceptions.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {
    
    // Test apply method with basic functionality
    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 0);
        image.set(0, 1, 1);
        image.set(0, 2, 1);
        image.set(1, 0, 1);
        image.set(1, 1, 0);
        image.set(1, 2, 0);
        image.set(2, 0, 0);
        image.set(2, 1, 1);
        image.set(2, 2, 1);
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        
        assertEquals(1, result.get(0, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(0, result.get(0, 2));
        assertEquals(0, result.get(1, 0));
        assertEquals(2, result.get(1, 1));
        assertEquals(2, result.get(1, 2));
        assertEquals(3, result.get(2, 0));
        assertEquals(0, result.get(2, 1));
        assertEquals(0, result.get(2, 2));
    }
    
    // Test constructor with valid values
    @Test
    public void testConstructorValidValues() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.getNeighbors());
        assertEquals(0, cc.getBackground());
    }
    
    // Test constructor with invalid number of neighbors
    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidNeighbors() {
        ConnectedComponents cc = new ConnectedComponents(2, 0);
    }
    
    // Test constructor with invalid background color
    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidBackgroundColor() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
    }
    
    // Test apply method with background color not found
    @Test
    public void testApplyBackgroundColorNotFound() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 1);
        image.set(0, 2, 1);
        image.set(1, 0, 1);
        image.set(1, 1, 1);
        image.set(1, 2, 1);
        image.set(2, 0, 1);
        image.set(2, 1, 1);
        image.set(2, 2, 1);
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        
        for (int i = 0; i < result.X(); i++) {
            for (int j = 0; j < result.Y(); j++) {
                assertEquals(0, result.get(i, j));
            }
        }
    }
    
    // Test apply method with single pixel image
    @Test
    public void testApplySinglePixelImage() {
        BinaryImage image = new BinaryImage(1, 1);
        image.set(0, 0, 1);
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        
        assertEquals(1, result.get(0, 0));
    }
    
    // Test apply method with empty image
    @Test
    public void testApplyEmptyImage() {
        BinaryImage image = new BinaryImage(0, 0);
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        
        assertEquals(0, result.X());
        assertEquals(0, result.Y());
    }
}

