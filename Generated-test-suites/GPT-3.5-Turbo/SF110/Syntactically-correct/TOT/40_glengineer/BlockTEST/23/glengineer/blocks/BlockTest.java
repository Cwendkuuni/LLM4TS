import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {

    private Scheme scheme;
    private Block block;

    @Before
    public void setUp() {
        scheme = new Scheme(5, 5); // Create a sample Scheme
        block = new ConcreteBlock(scheme, 0, 0, 2, 2); // Create a sample Block object
    }

    @Test
    public void testBlockConstructorWithValidCoordinates() {
        // Test valid coordinates
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(2, block.x2);
        assertEquals(2, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidCoordinates() {
        // Test invalid coordinates
        new ConcreteBlock(scheme, 2, 2, 0, 0);
    }

    @Test
    public void testSubblock() {
        // Test subblock method
        Block subblock = block.subblock(1, 1, 2, 2);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(2, subblock.x2);
        assertEquals(2, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }

    @Test
    public void testCharAt() {
        // Test charAt method with valid coordinates
        assertEquals('A', block.charAt(0, 0));
        assertEquals('B', block.charAt(1, 0));
        assertEquals('C', block.charAt(0, 1));
        assertEquals('D', block.charAt(1, 1));

        // Test charAt method with invalid coordinates
        try {
            block.charAt(2, 2);
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("char position (2,2) out of block.\n" + block.toString(), e.getMessage());
        }
    }

    @Test
    public void testTextAt() {
        // Test textAt method with valid coordinates
        assertEquals("AB", block.textAt(0, 2, 0));
        assertEquals("CD", block.textAt(0, 2, 1));

        // Test textAt method with invalid coordinates
        try {
            block.textAt(0, 3, 0);
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coordinates.", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithTrivialBlock() {
        // Test containsOnlyOneWord method with trivial block
        block.containsOnlyOneWord();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithEmptyBlock() {
        // Test containsOnlyOneWord method with empty block
        Block emptyBlock = new ConcreteBlock(scheme, 0, 0, 0, 0);
        emptyBlock.containsOnlyOneWord();
    }

    @Test
    public void testContainsOnlyOneWordWithNonEmptyBlock() {
        // Test containsOnlyOneWord method with non-empty block
        Block nonEmptyBlock = new ConcreteBlock(scheme, 0, 0, 3, 0);
        assertFalse(nonEmptyBlock.containsOnlyOneWord());

        nonEmptyBlock = new ConcreteBlock(scheme, 0, 0, 2, 0);
        assertTrue(nonEmptyBlock.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        // Test extractSequentialSubblocks method
        assertEquals(1, block.extractSequentialSubblocks().size());

        // Test extractSequentialSubblocks method with larger block
        Block largerBlock = new ConcreteBlock(scheme, 0, 0, 3, 3);
        assertEquals(4, largerBlock.extractSequentialSubblocks().size());
    }

    @Test
    public void testExtractParallelSubblocks() {
        // Test extractParallelSubblocks method
        assertEquals(1, block.extractParallelSubblocks().size());

        // Test extractParallelSubblocks method with larger block
        Block largerBlock = new ConcreteBlock(scheme, 0, 0, 3, 3);
        assertEquals(4, largerBlock.extractParallelSubblocks().size());
    }

    @Test
    public void testToString() {
        // Test toString method
        String expected = "Block(0,0, 2,2):\n" +
                          " #### \n" +
                          " # AB #\n" +
                          " # CD #\n" +
                          " #### ";
        assertEquals(expected, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        // Test getUpperLine method
        assertEquals("AB", block.getUpperLine());
    }
}