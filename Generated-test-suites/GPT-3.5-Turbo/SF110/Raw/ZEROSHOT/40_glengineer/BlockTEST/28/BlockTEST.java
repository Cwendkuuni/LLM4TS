import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {

    private Scheme scheme;
    private Block block;

    @Before
    public void setUp() {
        scheme = new Scheme();
        block = new ConcreteBlock(scheme, 0, 0, 1, 1);
    }

    @Test
    public void testSubblock() {
        Block subblock = block.subblock(0, 0, 0, 0);
        assertEquals(0, subblock.x1);
        assertEquals(0, subblock.y1);
        assertEquals(0, subblock.x2);
        assertEquals(0, subblock.y2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAt_invalidPosition() {
        block.charAt(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAt_invalidCoordinates() {
        block.textAt(0, 2, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_trivialBlock() {
        block.containsOnlyOneWord();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_emptyBlock() {
        Block emptyBlock = new ConcreteBlock(scheme, 0, 0, 0, 0);
        emptyBlock.containsOnlyOneWord();
    }

    @Test
    public void testToString() {
        String expected = "Block(0,0, 1,1):\n" +
                          " #### \n" +
                          " #   # \n" +
                          " #### ";
        assertEquals(expected, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        String expected = " ";
        assertEquals(expected, block.getUpperLine());
    }

    // ... add more test cases for other methods ...
}