package glengineer.blocks;

import glengineer.positions.*;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class BlockTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidCoordinates() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 2, 2, 1, 1);
    }
    
    @Test
    public void testConstructorWithValidCoordinates() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 1, 1, 2, 2);
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(2, block.getX2());
        assertEquals(2, block.getY2());
        assertEquals(scheme, block.getScheme());
    }
    
    @Test
    public void testConstructorWithCharPosition() {
        Scheme scheme = new Scheme();
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(2, 2);
        Block block = new ConcreteBlock(scheme, p1, p2);
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(2, block.getX2());
        assertEquals(2, block.getY2());
        assertEquals(scheme, block.getScheme());
    }
    
    @Test
    public void testConstructorWithScheme() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme);
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(scheme.getX2(), block.getX2());
        assertEquals(scheme.getY2(), block.getY2());
        assertEquals(scheme, block.getScheme());
    }
    
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        Block subblock = block.subblock(2, 2, 3, 3);
        assertEquals(2, subblock.getX1());
        assertEquals(2, subblock.getY1());
        assertEquals(3, subblock.getX2());
        assertEquals(3, subblock.getY2());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAtOutOfBounds() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 1, 1, 2, 2);
        block.charAt(3, 3);
    }
    
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def"};
        Block block = new ConcreteBlock(scheme, 1, 1, 2, 2);
        assertEquals('c', block.charAt(2, 1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAtOutOfBounds() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 1, 1, 2, 2);
        block.textAt(1, 4, 1);
    }
    
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def"};
        Block block = new ConcreteBlock(scheme, 1, 1, 2, 2);
        assertEquals("bc", block.textAt(1, 3, 1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithTrivialBlock() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 1, 1, 1, 1);
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithEmptyBlock() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 1, 1, 2, 2);
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def"};
        Block block = new ConcreteBlock(scheme, 1, 1, 2, 2);
        assertTrue(block.containsOnlyOneWord());
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 1, 1, 2, 2);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(1, subblocks.size());
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 1, 1, 2, 2);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(1, subblocks.size());
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def"};
        Block block = new ConcreteBlock(scheme, 1, 1, 2, 2);
        assertEquals("Block(1,1, 2,2):\n####\n# b #\n# c #\n####", block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def"};
        Block block = new ConcreteBlock(scheme, 1, 1, 2, 2);
        assertEquals("bc", block.getUpperLine());
    }
    
}