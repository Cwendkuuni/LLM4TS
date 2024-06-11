import static org.junit.Assert.*;
import org.junit.*;

public class BlockTest {
    private Scheme scheme;
    private Block block1;
    private Block block2;
    
    @Before
    public void setUp() {
        scheme = new Scheme(10, 10);
        block1 = new ConcreteBlock(scheme, 0, 0, 5, 5);
        block2 = new ConcreteBlock(scheme, 3, 3, 8, 8);
    }
    
    @Test
    public void testBlockConstructorWithCoordinates() {
        // Typical use case
        assertEquals(0, block1.x1);
        assertEquals(0, block1.y1);
        assertEquals(5, block1.x2);
        assertEquals(5, block1.y2);
        assertEquals(scheme, block1.scheme);
        
        // Edge case: x1, y1, x2, y2 are all maximum values allowed by scheme
        Block block = new ConcreteBlock(scheme, scheme.x2-1, scheme.y2-1, scheme.x2, scheme.y2);
        assertEquals(scheme.x2-1, block.x1);
        assertEquals(scheme.y2-1, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
        
        // Error scenario: x1 > x2
        try {
            Block block = new ConcreteBlock(scheme, 5, 5, 3, 3);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 5,5, 3,3.", e.getMessage());
        }
        
        // Error scenario: y1 > y2
        try {
            Block block = new ConcreteBlock(scheme, 0, 5, 5, 3);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 0,5, 5,3.", e.getMessage());
        }
        
        // Error scenario: x1 < 0
        try {
            Block block = new ConcreteBlock(scheme, -1, 0, 5, 5);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: -1,0, 5,5.", e.getMessage());
        }
        
        // Error scenario: y1 < 0
        try {
            Block block = new ConcreteBlock(scheme, 0, -1, 5, 5);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 0,-1, 5,5.", e.getMessage());
        }
        
        // Error scenario: x2 > scheme.x2
        try {
            Block block = new ConcreteBlock(scheme, 0, 0, scheme.x2 + 1, 5);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 0,0, 11,5.", e.getMessage());
        }
        
        // Error scenario: y2 > scheme.y2
        try {
            Block block = new ConcreteBlock(scheme, 0, 0, 5, scheme.y2 + 1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 0,0, 5,11.", e.getMessage());
        }
    }
    
    @Test
    public void testBlockConstructorWithPositions() {
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(5, 5);
        Block block = new ConcreteBlock(scheme, p1, p2);
        
        // Typical use case
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
        
        // Error scenario: p1.y > p2.y
        try {
            p1 = new CharPosition(0, 5);
            p2 = new CharPosition(5, 0);
            Block block = new ConcreteBlock(scheme, p1, p2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 0,5, 5,0.", e.getMessage());
        }
    }
    
    @Test
    public void testBlockConstructorWithScheme() {
        Block block = new ConcreteBlock(scheme);
        
        // Typical use case
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testSubblock() {
        Block subblock = block1.subblock(2, 2, 4, 4);
        
        // Typical use case
        assertEquals(2, subblock.x1);
        assertEquals(2, subblock.y1);
        assertEquals(4, subblock.x2);
        assertEquals(4, subblock.y2);
        assertEquals(scheme, subblock.scheme);
        
        // Error scenario: p1.y > p2.y
        try {
            subblock = block1.subblock(2, 4, 4, 2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 2,4, 4,2.", e.getMessage());
        }
    }
    
    @Test
    public void testCharAt() {
        // Typical use case
        assertEquals('#', block1.charAt(0, 0));
        assertEquals('#', block1.charAt(5, 5));
        assertEquals(' ', block1.charAt(1, 1));
        assertEquals(' ', block1.charAt(4, 4));
        
        // Edge case: x is minimum value
        assertEquals('#', block1.charAt(block1.x1, 0));
        
        // Edge case: y is minimum value
        assertEquals('#', block1.charAt(0, block1.y1));
        
        // Error scenario: x is greater than scheme.x2
        try {
            block1.charAt(block1.x2 + 1, 0);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("char position (6,0) out of block.\nBlock(0,0, 5,5):\n ######\n #      #\n #      #\n #      #\n #      #\n #      #\n ######", e.getMessage());
        }
        
        // Error scenario: y is greater than scheme.y2
        try {
            block1.charAt(0, block1.y2 + 1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("char position (0,6) out of block.\nBlock(0,0, 5,5):\n ######\n #      #\n #      #\n #      #\n #      #\n #      #\n ######", e.getMessage());
        }
    }
    
    @Test
    public void testTextAt() {
        // Typical use case
        assertEquals("######", block1.textAt(0, 6, 0));
        assertEquals("#      ", block1.textAt(0, 1, 1));
        
        // Edge case: x1 is minimum value
        assertEquals(" #", block1.textAt(block1.x1, 3, 0));
        
        // Error scenario: x2 is greater than scheme.x2
        try {
            block1.textAt(block1.x1, block1.x2 + 1, 0);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coodinates.", e.getMessage());
        }
        
        // Error scenario: y is greater than scheme.y2
        try {
            block1.textAt(0, 6, block1.y2 + 1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coodinates.", e.getMessage());
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_TypicalUseCase() {
        block1.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_EmptyBlock() {
        block2.containsOnlyOneWord();
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        List<Block> sequentialSubblocks = block1.extractSequentialSubblocks();
        
        // Typical use case
        assertEquals(1, sequentialSubblocks.size());
        assertEquals(block1, sequentialSubblocks.get(0));
        
        // Edge case: block contains empty lines
        sequentialSubblocks = block2.extractSequentialSubblocks();
        assertEquals(1, sequentialSubblocks.size());
        assertEquals(block2, sequentialSubblocks.get(0));
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        List<Block> parallelSubblocks = block1.extractParallelSubblocks();
        
        // Typical use case
        assertEquals(1, parallelSubblocks.size());
        assertEquals(block1, parallelSubblocks.get(0));
        
        // Edge case: block contains empty lines
        parallelSubblocks = block2.extractParallelSubblocks();
        assertEquals(1, parallelSubblocks.size());
        assertEquals(block2, parallelSubblocks.get(0));
    }
    
    @Test
    public void testToString() {
        String expected = "Block(0,0, 5,5):\n" +
                          " ######\n" +
                          " #      #\n" +
                          " #      #\n" +
                          " #      #\n" +
                          " #      #\n" +
                          " #      #\n" +
                          " ######";
        assertEquals(expected, block1.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        // Typical use case
        assertEquals(" ######", block1.getUpperLine());
        
        // Edge case: y1 is minimum value
        assertEquals(" ######", block1.getUpperLine());
    }
}