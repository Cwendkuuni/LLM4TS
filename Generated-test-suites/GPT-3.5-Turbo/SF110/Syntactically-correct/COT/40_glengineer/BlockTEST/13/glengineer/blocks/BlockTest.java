import glengineer.blocks.*;
import glengineer.positions.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {

    // Test block creation with valid parameters
    @Test
    public void testBlockCreation1() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        assertNotNull(block);
    }

    // Test block creation with invalid parameters
    @Test(expected=IllegalArgumentException.class)
    public void testBlockCreation2() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 5, 5, 2, 2);
    }

    // Test block creation with valid CharPosition parameters
    @Test
    public void testBlockCreation3() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 3);
        Block block = new ConcreteBlock(scheme, p1, p2);
        assertNotNull(block);
    }

    // Test block creation with scheme parameter only
    @Test
    public void testBlockCreation4() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme);
        assertNotNull(block);
    }

    // Test subblock method
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        Block subblock = block.subblock(2, 2, 3, 3);
        assertNotNull(subblock);
    }

    // Test charAt method with valid coordinates
    @Test
    public void testCharAt1() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        char c = block.charAt(2, 2);
        assertEquals('-', c);
    }

    // Test charAt method with invalid coordinates
    @Test(expected=IllegalArgumentException.class)
    public void testCharAt2() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme);
        char c = block.charAt(11, 11);
    }

    // Test textAt method with valid coordinates
    @Test
    public void testTextAt1() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        String text = block.textAt(1, 3, 2);
        assertEquals("--", text);
    }

    // Test textAt method with invalid coordinates
    @Test(expected=IllegalArgumentException.class)
    public void testTextAt2() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme);
        String text = block.textAt(2, 1, 1);
    }

    // Test containsOnlyOneWord method with a non-trivial block
    @Test
    public void testContainsOnlyOneWord1() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        boolean containsOnlyOneWord = block.containsOnlyOneWord();
        assertTrue(containsOnlyOneWord);
    }

    // Test containsOnlyOneWord method with a trivial block
    @Test(expected=IllegalArgumentException.class)
    public void testContainsOnlyOneWord2() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 2, 2, 2, 2);
        boolean containsOnlyOneWord = block.containsOnlyOneWord();
    }

    // Test extractSequentialSubblocks method
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 4, 4);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertNotNull(subblocks);
    }

    // Test extractParallelSubblocks method
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 4, 4);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertNotNull(subblocks);
    }

    // Test toString method
    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        String str = block.toString();
        assertNotNull(str);
    }

    // Test getUpperLine method
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        String line = block.getUpperLine();
        assertNotNull(line);
    }
}

