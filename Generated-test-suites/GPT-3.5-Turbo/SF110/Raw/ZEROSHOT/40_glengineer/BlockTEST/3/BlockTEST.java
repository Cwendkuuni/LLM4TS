import glengineer.positions.*;
import java.util.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BlockTest {
  
    private Scheme scheme;
    private Block block;
    
    @Before
    public void setUp() {
        scheme = new Scheme(10, 10);
        block = new Block(scheme, 0, 0, 5, 5);
    }
    
    @Test
    public void testConstructorWithSchemeAndCoordinates() {
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testConstructorWithSchemeAndPositions() {
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(5, 5);
        Block block2 = new Block(scheme, p1, p2);
        
        assertEquals(0, block2.x1);
        assertEquals(0, block2.y1);
        assertEquals(5, block2.x2);
        assertEquals(5, block2.y2);
        assertEquals(scheme, block2.scheme);
    }
    
    @Test
    public void testConstructorWithScheme() {
        Block block3 = new Block(scheme);
        
        assertEquals(0, block3.x1);
        assertEquals(0, block3.y1);
        assertEquals(scheme.x2, block3.x2);
        assertEquals(scheme.y2, block3.y2);
        assertEquals(scheme, block3.scheme);
    }
    
    @Test
    public void testSubblock() {
        Block subblock = block.subblock(1, 1, 3, 3);
        
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(3, subblock.x2);
        assertEquals(3, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testCharAtOutOfBounds() {
        block.charAt(10, 10);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testTextAtOutOfBounds() {
        block.textAt(10, 20, 5);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testContainsOnlyOneWordTrivialBlock() {
        Block trivialBlock = new Block(scheme, 0, 0, 0, 0);
        trivialBlock.containsOnlyOneWord();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testContainsOnlyOneWordEmptyBlock() {
        Block emptyBlock = new Block(scheme, 0, 0, 5, 5);
        emptyBlock.containsOnlyOneWord();
    }
    
    @Test
    public void testToString() {
        String expected = "Block(0,0, 5,5):\n" +
                          " ##############\n" +
                          " #              #\n" +
                          " #              #\n" +
                          " #              #\n" +
                          " #              #\n" +
                          " ##############";
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        String upperLine = block.getUpperLine();
        assertEquals(" ", upperLine);
    }
}