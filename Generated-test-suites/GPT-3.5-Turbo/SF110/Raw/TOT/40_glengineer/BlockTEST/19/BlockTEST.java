import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
import glengineer.blocks.*;
import glengineer.positions.*;

public class BlockTest {

    // Test cases for the constructor with 4 integer parameters
    @Test
    public void testConstructorWithIntegers() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(3, block.x2);
        assertEquals(3, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithIntegers_invalidParameters() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, -1, 1, 3, 3);
    }

    // Test cases for the constructor with CharPosition parameters
    @Test
    public void testConstructorWithCharPositions() {
        Scheme scheme = new Scheme(5, 5);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 3);
        Block block = new ConcreteBlock(scheme, p1, p2);
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(3, block.x2);
        assertEquals(3, block.y2);
        assertEquals(scheme, block.scheme);
    }

    // Test cases for the constructor with only Scheme parameter
    @Test
    public void testConstructorWithScheme() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }

    // Test cases for the subblock method
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        Block subblock = block.subblock(2, 2, 3, 3);
        assertEquals(2, subblock.x1);
        assertEquals(2, subblock.y1);
        assertEquals(3, subblock.x2);
        assertEquals(3, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }

    // Test cases for the charAt method
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[1] = "ABCDE";
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        assertEquals('B', block.charAt(2, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAt_outOfBlock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        block.charAt(4, 2);
    }

    // Test cases for the textAt method
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[2] = "ABCDE";
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        assertEquals("CD", block.textAt(2, 4, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAt_invalidCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[2] = "ABCDE";
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        block.textAt(5, 2, 2);
    }

    // Test cases for the containsOnlyOneWord method
    @Test
    public void testContainsOnlyOneWord_true() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[2] = "ABCDE";
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        assertTrue(block.containsOnlyOneWord());
    }

    @Test
    public void testContainsOnlyOneWord_false() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        assertFalse(block.containsOnlyOneWord());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_emptyBlock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 1, 5, 5, 5);
        block.containsOnlyOneWord();
    }

    // Test cases for the extractSequentialSubblocks method
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[1] = "ABCDE";
        scheme.lines[2] = "FGHIJ";
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(2, subblocks.size());
        Block subblock1 = subblocks.get(0);
        assertEquals(1, subblock1.x1);
        assertEquals(1, subblock1.y1);
        assertEquals(5, subblock1.x2);
        assertEquals(2, subblock1.y2);
        Block subblock2 = subblocks.get(1);
        assertEquals(1, subblock2.x1);
        assertEquals(2, subblock2.y1);
        assertEquals(5, subblock2.x2);
        assertEquals(3, subblock2.y2);
    }

    // Test cases for the extractParallelSubblocks method
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[1] = "ABCDE";
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(1, subblocks.size());
        Block subblock = subblocks.get(0);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(3, subblock.x2);
        assertEquals(3, subblock.y2);
    }

    // Test case for the toString method
    @Test
    public void testToString() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[1] = "ABCDE";
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        String expectedResult = "Block(1,1, 5,5):\n" +
                                 " ####\n" +
                                 " # ABCDE #\n" +
                                 " #\n" +
                                 " ####";
        assertEquals(expectedResult, block.toString());
    }

    // Test case for the getUpperLine method
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[1] = "ABCDE";
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        assertEquals("ABCDE", block.getUpperLine());
    }

}