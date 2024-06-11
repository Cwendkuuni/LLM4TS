package glengineer.blocks;

import glengineer.blocks.*;
import glengineer.positions.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {

    @Test
    public void testBlock1() {
        // Test case for Block constructor with x, y coordinates
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        assertNotNull(block);
    }

    @Test
    public void testBlock2() {
        // Test case for Block constructor with CharPosition objects
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(5, 5);
        Block block = new ConcreteBlock(scheme, p1, p2);
        assertNotNull(block);
    }

    @Test
    public void testSubblock() {
        // Test case for subblock method
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        Block subblock = block.subblock(2, 2, 4, 4);
        assertNotNull(subblock);
    }

    @Test
    public void testContainsOnlyOneWord() {
        // Test case for containsOnlyOneWord method
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        boolean result = block.containsOnlyOneWord();
        assertTrue(result);
    }

    // Add more test cases for the rest of the methods in the Block class

}