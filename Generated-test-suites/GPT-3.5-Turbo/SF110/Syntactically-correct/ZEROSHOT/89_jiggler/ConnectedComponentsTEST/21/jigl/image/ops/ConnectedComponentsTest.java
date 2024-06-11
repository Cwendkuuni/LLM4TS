import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {
    
    private ConnectedComponents cc;
    
    @Before
    public void setUp() throws InvalidArgumentException {
        cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
    }
    
    @Test
    public void testApply() {
        // Test case 1: all background
        BinaryImage input1 = new BinaryImage(5, 5);
        GrayImage expected1 = new GrayImage(5, 5);
        
        GrayImage result1 = cc.apply(input1);
        
        assertEquals(expected1, result1);
        
        // Test case 2: single object
        BinaryImage input2 = new BinaryImage(5, 5);
        input2.set(2, 2, 1);
        GrayImage expected2 = new GrayImage(5, 5);
        expected2.set(2, 2, 1);
        
        GrayImage result2 = cc.apply(input2);
        
        assertEquals(expected2, result2);
        
        // Test case 3: multiple objects
        BinaryImage input3 = new BinaryImage(5, 5);
        input3.set(1, 1, 1);
        input3.set(3, 3, 1);
        GrayImage expected3 = new GrayImage(5, 5);
        expected3.set(1, 1, 2);
        expected3.set(3, 3, 3);
        
        GrayImage result3 = cc.apply(input3);
        
        assertEquals(expected3, result3);
    }
}