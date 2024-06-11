import static org.junit.Assert.*;
import org.junit.*;
import glengineer.blocks.*;
import glengineer.positions.*;

public class BlockTest {

    private Scheme scheme;
    private Block block;

    @Before
    public void setUp() {
        scheme = new Scheme(5, 5);
        block = new ConcreteBlock(scheme, 1, 1, 4, 4);
    }

    @Test
    public void testBlockConstructor_ValidCoordinates_Success() {
        assertNotNull(block);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor_InvalidX1_ThrowsIllegalArgumentException() {
        block = new ConcreteBlock(scheme, -1, 1, 4, 4);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor_InvalidY1_ThrowsIllegalArgumentException() {
        block = new ConcreteBlock(scheme, 1, -1, 4, 4);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor_InvalidX2_ThrowsIllegalArgumentException() {
        block = new ConcreteBlock(scheme, 1, 1, 6, 4);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor_InvalidY2_ThrowsIllegalArgumentException() {
        block = new ConcreteBlock(scheme, 1, 1, 4, 6);
    }

    @Test
    public void testBlockConstructor_CharPosition_Success() {
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(4, 4);
        block = new ConcreteBlock(scheme, p1, p2);
        assertNotNull(block);
    }

    @Test
    public void testBlockConstructor_Default_Success() {
        block = new ConcreteBlock(scheme);
        assertNotNull(block);
    }

    @Test
    public void testSubblock_ValidCoordinates_Success() {
        Block subblock = block.subblock(2, 2, 3, 3);
        assertNotNull(subblock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAt_InvalidCoordinates_ThrowsIllegalArgumentException() {
        block.charAt(6, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAt_InvalidX1_ThrowsIllegalArgumentException() {
        block.textAt(5, 6, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAt_InvalidX2_ThrowsIllegalArgumentException() {
        block.textAt(1, 6, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAt_InvalidY_ThrowsIllegalArgumentException() {
        block.textAt(1, 4, 6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_TrivialBlock_ThrowsIllegalArgumentException() {
        Block trivialBlock = new ConcreteBlock(scheme);
        trivialBlock.containsOnlyOneWord();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_EmptyBlock_ThrowsIllegalArgumentException() {
        Block emptyBlock = new ConcreteBlock(scheme, 1, 5, 4, 5);
        emptyBlock.containsOnlyOneWord();
    }

    @Test
    public void testContainsOnlyOneWord_NonEmptyBlockWithOneWord_Success() {
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(4, 1);
        Block oneWordBlock = new ConcreteBlock(scheme, p1, p2);
        assertTrue(oneWordBlock.containsOnlyOneWord());
    }

    // Add test cases for other methods...

}