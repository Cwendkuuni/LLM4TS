
import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {

    // Expert 1 Test Cases
    @Test
    public void testBlockConstructorWithValidParameters() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        assertNotNull(block);
    }

    @Test
    public void testCharAtWithValidCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        assertEquals('a', block.charAt(1, 1));
    }

    // Expert 2 Test Cases
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithTrivialBlock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme);
        block.containsOnlyOneWord();
    }

    @Test
    public void testSubblockWithValidParameters() {
        Scheme scheme = new Scheme(8, 8);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        Block subblock = block.subblock(2, 2, 4, 4);
        assertEquals(2, subblock.x1);
        assertEquals(2, subblock.y1);
        assertEquals(4, subblock.x2);
        assertEquals(4, subblock.y2);
    }

    // Expert 3 Test Cases
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertNotNull(subblocks);
        assertEquals(0, subblocks.size());
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        String upperLine = block.getUpperLine();
        assertEquals("bcd", upperLine);
    }

}