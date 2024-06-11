import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {

    @Test
    public void testConstructorWithSchemeAndCoordinates() {
        // Arrange
        Scheme scheme = new Scheme();
        int x1 = 1;
        int y1 = 2;
        int x2 = 3;
        int y2 = 4;
        
        // Act
        Block block = new BlockImpl(scheme, x1, y1, x2, y2);
        
        // Assert
        assertEquals(x1, block.x1);
        assertEquals(y1, block.y1);
        assertEquals(x2, block.x2);
        assertEquals(y2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testConstructorWithSchemeAndCharPositions() {
        // Arrange
        Scheme scheme = new Scheme();
        CharPosition p1 = new CharPosition(1, 2);
        CharPosition p2 = new CharPosition(3, 4);
        int x1 = p1.x;
        int y1 = p1.y;
        int x2 = p2.x;
        int y2 = p2.y;
        
        // Act
        Block block = new BlockImpl(scheme, p1, p2);
        
        // Assert
        assertEquals(x1, block.x1);
        assertEquals(y1, block.y1);
        assertEquals(x2, block.x2);
        assertEquals(y2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testDefaultConstructor() {
        // Arrange
        Scheme scheme = new Scheme();
        int x1 = 0;
        int y1 = 0;
        int x2 = scheme.x2;
        int y2 = scheme.y2;
        
        // Act
        Block block = new BlockImpl(scheme);
        
        // Assert
        assertEquals(x1, block.x1);
        assertEquals(y1, block.y1);
        assertEquals(x2, block.x2);
        assertEquals(y2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testSubblock() {
        // Arrange
        Block block = new BlockImpl(null, 0, 0, 4, 4);
        
        // Act
        Block subblock = block.subblock(1, 1, 3, 3);
        
        // Assert
        assertNotNull(subblock);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(3, subblock.x2);
        assertEquals(3, subblock.y2);
        assertEquals(block.scheme, subblock.scheme);
    }
    
    // Add more test methods for other methods in the Block class
    
}