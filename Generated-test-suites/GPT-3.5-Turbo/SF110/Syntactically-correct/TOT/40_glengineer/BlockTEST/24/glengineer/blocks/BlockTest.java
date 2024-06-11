import org.junit.*;
import static org.junit.Assert.*;

public class BlockTest {

    private Scheme scheme;
    private Block block;
    
    @Before
    public void setUp() {
        scheme = new Scheme();
        scheme.setLines(new String[]{"abcdefg", "hijklmn", "opqrstu", "vwxyzAB"});
        block = new ConcreteBlock(scheme, 0, 0, 4, 3);
    }
    
    @Test
    public void testBlockConstructor1() {
        assertNotNull(block);
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(4, block.getX2());
        assertEquals(3, block.getY2());
    }
    
    @Test
    public void testBlockConstructor2() {
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 2);
        block = new ConcreteBlock(scheme, p1, p2);
        
        assertNotNull(block);
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(3, block.getX2());
        assertEquals(2, block.getY2());
    }
    
    @Test
    public void testBlockConstructor3() {
        block = new ConcreteBlock(scheme);
        
        assertNotNull(block);
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(scheme.x2, block.getX2());
        assertEquals(scheme.y2, block.getY2());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorInvalid() {
        block = new ConcreteBlock(scheme, -1, 0, 4, 3);
    }
    
    @Test
    public void testCharAt() {
        assertEquals('d', block.charAt(3, 0));
        assertEquals('o', block.charAt(0, 2));
        try {
            block.charAt(7, 3);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("char position (7,3) out of block.", e.getMessage());
        }
    }
    
    @Test
    public void testTextAt() {
        assertEquals("ef", block.textAt(4, 6, 0));
        assertEquals("jk", block.textAt(0, 2, 1));
        try {
            block.textAt(5, 8, 2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coordinates.", e.getMessage());
        }
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        assertFalse(block.containsOnlyOneWord());
        
        block = new ConcreteTrivialBlock(scheme, 0, 0, 0, 0);
        try {
            block.containsOnlyOneWord();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("cannot search in a trivial block", e.getMessage());
        }
        
        block = new ConcreteBlock(scheme, 0, 3, 4, 3);
        try {
            block.containsOnlyOneWord();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("cannot verify unicity of a word in an empty block", e.getMessage());
        }
        
        block = new ConcreteBlock(scheme, 1, 0, 1, 3);
        assertTrue(block.containsOnlyOneWord());
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        // implementation and test case required
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        // implementation and test case required
    }
    
    @Test
    public void testToString() {
        // implementation and test case required
    }
    
    @Test
    public void testGetUpperLine() {
        assertEquals("abcdefg", block.getUpperLine());
        
        scheme.setLines(new String[]{"123", "456", "789", "0AB"});
        block = new ConcreteBlock(scheme, 0, 2, 4, 3);
        assertEquals("789", block.getUpperLine());
    }
}