import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import glengineer.blocks.*;
import glengineer.positions.*;

public class BlockTest {

    private Scheme scheme;
    
    @Before
    public void setUp() {
        scheme = new Scheme(10, 10);
    }
    
    @Test
    public void testBlockConstructor1() {
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
    }
    
    @Test
    public void testBlockConstructor2() {
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(5, 5);
        Block block = new ConcreteBlock(scheme, p1, p2);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
    }
    
    @Test
    public void testBlockConstructor3() {
        Block block = new ConcreteBlock(scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorInvalidDefinition() {
        new ConcreteBlock(scheme, -1, 0, 5, 5);
    }
    
    @Test
    public void testCharAt() {
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        assertEquals('A', block.charAt(0, 0));
        assertEquals('B', block.charAt(1, 0));
        assertEquals('C', block.charAt(2, 0));
        assertEquals('D', block.charAt(3, 0));
        assertEquals('E', block.charAt(4, 0));
        assertEquals('F', block.charAt(5, 0));
        assertEquals('G', block.charAt(0, 1));
        assertEquals('H', block.charAt(1, 1));
        assertEquals('I', block.charAt(2, 1));
        assertEquals('J', block.charAt(3, 1));
        assertEquals('K', block.charAt(4, 1));
        assertEquals('L', block.charAt(5, 1));
        assertEquals('M', block.charAt(0, 2));
        assertEquals('N', block.charAt(1, 2));
        assertEquals('O', block.charAt(2, 2));
        assertEquals('P', block.charAt(3, 2));
        assertEquals('Q', block.charAt(4, 2));
        assertEquals('R', block.charAt(5, 2));
        assertEquals('S', block.charAt(0, 3));
        assertEquals('T', block.charAt(1, 3));
        assertEquals('U', block.charAt(2, 3));
        assertEquals('V', block.charAt(3, 3));
        assertEquals('W', block.charAt(4, 3));
        assertEquals('X', block.charAt(5, 3));
        assertEquals('Y', block.charAt(0, 4));
        assertEquals('Z', block.charAt(1, 4));
        assertEquals('A', block.charAt(2, 4));
        assertEquals('B', block.charAt(3, 4));
        assertEquals('C', block.charAt(4, 4));
        assertEquals('D', block.charAt(5, 4));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAtOutOfBounds() {
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        block.charAt(6, 5);
    }
    
    @Test
    public void testTextAt() {
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        assertEquals("ABCDEF", block.textAt(0, 6, 0));
        assertEquals("GHIJKL", block.textAt(0, 6, 1));
        assertEquals("MNOPQR", block.textAt(0, 6, 2));
        assertEquals("STUVWX", block.textAt(0, 6, 3));
        assertEquals("YZABCD", block.textAt(0, 6, 4));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAtInvalidCoordinates() {
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        block.textAt(5, 4, 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordInTrivialBlock() {
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordInEmptyBlock() {
        Block block = new ConcreteBlock(scheme, 0, 0, 1, 0);
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        assertFalse(block.containsOnlyOneWord());
        
        block = new ConcreteBlock(scheme, 0, 0, 1, 0);
        assertTrue(block.containsOnlyOneWord());
        
        block = new ConcreteBlock(scheme, 2, 2, 2, 2);
        assertTrue(block.containsOnlyOneWord());
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        assertEquals(6, block.extractSequentialSubblocks().size());
        
        block = new ConcreteBlock(scheme, 0, 0, 1, 1);
        assertEquals(1, block.extractSequentialSubblocks().size());
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        assertEquals(6, block.extractParallelSubblocks().size());
        
        block = new ConcreteBlock(scheme, 0, 0, 1, 1);
        assertEquals(1, block.extractParallelSubblocks().size());
    }
    
    @Test
    public void testToString() {
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        String expected = "Block(0,0, 5,5):\n"
                        + " #######\n"
                        + " # ABC #\n"
                        + " # DEF #\n"
                        + " # GHI #\n"
                        + " # JKL #\n"
                        + " # MNO #\n"
                        + " #######";
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Block block = new ConcreteBlock(scheme, 1, 1, 4, 4);
        assertEquals("DEF", block.getUpperLine());
        
        block = new ConcreteBlock(scheme, 0, 0, 2, 0);
        assertEquals("ABC", block.getUpperLine());
    }
}