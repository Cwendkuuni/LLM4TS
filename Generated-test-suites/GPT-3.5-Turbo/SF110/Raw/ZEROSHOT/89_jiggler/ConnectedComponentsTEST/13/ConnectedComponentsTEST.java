import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.Image;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {
    
    ConnectedComponents connectedComponents;
    BinaryImage binaryImage;
    
    
    @Before
    public void setUp() {
        binaryImage = new BinaryImage(5, 5);
        connectedComponents = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, BinaryImage.BLACK);
    }
    
    @Test
    public void testApply() {
        GrayImage resultImage = connectedComponents.apply(binaryImage);
        
        // Test assertions
        assertNotNull(resultImage);
    }
    
    @Test
    public void testConstructor() {
        try {
            ConnectedComponents connectedComponents = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, BinaryImage.BLACK);
            
            // Test assertions
            assertNotNull(connectedComponents);
            assertEquals(ConnectedComponents.FOUR_CONNECTED, connectedComponents.getNeighbors());
            assertEquals(BinaryImage.BLACK, connectedComponents.getBackground());
            
        } catch (InvalidArgumentException e) {
            fail("Exception thrown");
        }
    }
    
    // Additional test methods
    
    @Test(expected = InvalidArgumentException.class)
    public void testInvalidConstructor() throws InvalidArgumentException {
        ConnectedComponents connectedComponents = new ConnectedComponents(3, 2);
        fail("Should throw exception");
    }
    
    @Test
    public void testSetAndGetNeighbors() {
        connectedComponents.setNeighbors(ConnectedComponents.EIGHT_CONNECTED);
        
        // Test assertions
        assertEquals(ConnectedComponents.EIGHT_CONNECTED, connectedComponents.getNeighbors());
    }
    
    @Test
    public void testSetAndGetBackground() {
        connectedComponents.setBackground(BinaryImage.WHITE);
        
        // Test assertions
        assertEquals(BinaryImage.WHITE, connectedComponents.getBackground());
    }

}