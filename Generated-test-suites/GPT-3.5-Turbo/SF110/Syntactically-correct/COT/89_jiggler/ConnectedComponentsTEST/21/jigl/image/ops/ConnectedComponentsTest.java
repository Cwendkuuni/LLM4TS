import jigl.image.ops.*;
import jigl.image.types.*;
import jigl.image.exceptions.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(3, 3); //change the dimensions according to the test case
        image.set(0,0,1);
        image.set(0,1,1);
        image.set(0,2,0);
        image.set(1,0,0);
        image.set(1,1,1);
        image.set(1,2,1);
        image.set(2,0,1);
        image.set(2,1,0);
        image.set(2,2,1);
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        
        //Assert statements to check if the result is as expected
        assertEquals(1, result.get(0,0));
        assertEquals(1, result.get(0,1));
        assertEquals(0, result.get(0,2));
        assertEquals(0, result.get(1,0));
        assertEquals(1, result.get(1,1));
        assertEquals(1, result.get(1,2));
        assertEquals(2, result.get(2,0)); //connected component labeled as 2
        assertEquals(0, result.get(2,1));
        assertEquals(3, result.get(2,2)); //connected component labeled as 3
    }

    @Test
    public void testConstructorFourConnected() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.getNeighbors());
        } catch (InvalidArgumentException e) {
            fail("InvalidArgumentException thrown");
        }
    }
    
    @Test
    public void testConstructorEightConnected() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
            assertEquals(ConnectedComponents.EIGHT_CONNECTED, cc.getNeighbors());
        } catch (InvalidArgumentException e) {
            fail("InvalidArgumentException thrown");
        }
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidNeighbors() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(2, 0);
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidBackgroundColor() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
    }

    @Test
    public void testApplyEmptyImage() {
        BinaryImage image = new BinaryImage(3, 3);
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        
        //Assert statements to check if the result is as expected
        for (int x = 0; x < result.X(); x++) {
            for (int y = 0; y < result.Y(); y++) {
                assertEquals(0, result.get(x, y));
            }
        }
    }

    @Test
    public void testApplyOneConnectedComponent() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0,0,1);
        image.set(0,1,1);
        image.set(0,2,1);
        image.set(1,0,1);
        image.set(1,1,1);
        image.set(1,2,1);
        image.set(2,0,1);
        image.set(2,1,1);
        image.set(2,2,1);
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        
        //Assert statements to check if the result is as expected
        for (int x = 0; x < result.X(); x++) {
            for (int y = 0; y < result.Y(); y++) {
                assertEquals(1, result.get(x, y));
            }
        }
    }

    @Test
    public void testApplyInvalidNeighbors() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0,0,1);
        image.set(0,1,1);
        image.set(0,2,0);
        image.set(1,0,0);
        image.set(1,1,1);
        image.set(1,2,1);
        image.set(2,0,1);
        image.set(2,1,0);
        image.set(2,2,1);
        
        ConnectedComponents cc = new ConnectedComponents(2, 0);
        
        try {
            GrayImage result = cc.apply(image);
            fail("InvalidArgumentException not thrown");
        } catch (InvalidArgumentException e) {
            //expected
        }
    }

    @Test
    public void testApplyInvalidBackgroundColor() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0,0,1);
        image.set(0,1,1);
        image.set(0,2,0);
        image.set(1,0,0);
        image.set(1,1,1);
        image.set(1,2,1);
        image.set(2,0,1);
        image.set(2,1,0);
        image.set(2,2,1);
        
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
            GrayImage result = cc.apply(image);
            fail("InvalidArgumentException not thrown");
        } catch (InvalidArgumentException e) {
            //expected
        }
    }

}