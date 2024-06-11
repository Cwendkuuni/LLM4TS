package glengineer.blocks;

import glengineer.positions.*;
import java.util.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class BlockTest {

    @Test
    public void testConstructor1() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(2, block.x2);
        assertEquals(2, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testConstructor2() {
        Scheme scheme = new Scheme();
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(2, 2);
        Block block = new ConcreteBlock(scheme, p1, p2);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(2, block.x2);
        assertEquals(2, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testConstructor3() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        Block subblock = block.subblock(1, 1, 2, 2);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(2, subblock.x2);
        assertEquals(2, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }

    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def", "ghi"};
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        assertEquals('a', block.charAt(0, 0));
        assertEquals('b', block.charAt(1, 0));
        assertEquals('c', block.charAt(2, 0));
        assertEquals('d', block.charAt(0, 1));
        assertEquals('e', block.charAt(1, 1));
        assertEquals('f', block.charAt(2, 1));
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def", "ghi"};
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        assertEquals("ab", block.textAt(0, 2, 0));
        assertEquals("bc", block.textAt(1, 3, 0));
        assertEquals("de", block.textAt(0, 2, 1));
        assertEquals("ef", block.textAt(1, 3, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord1() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme);
        block.containsOnlyOneWord();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord2() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        block.containsOnlyOneWord();
    }

    @Test
    public void testContainsOnlyOneWord3() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def", "ghi"};
        Block block = new ConcreteBlock(scheme, 0, 0, 3, 1);
        assertFalse(block.containsOnlyOneWord());
    }

    @Test
    public void testContainsOnlyOneWord4() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "abc", "ghi"};
        Block block = new ConcreteBlock(scheme, 0, 0, 3, 2);
        assertFalse(block.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def", "ghi"};
        Block block = new ConcreteBlock(scheme, 0, 0, 3, 2);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(2, subblocks.size());
        assertEquals("ab\n", subblocks.get(0).toString());
        assertEquals("de\n", subblocks.get(1).toString());
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "abc", "ghi"};
        Block block = new ConcreteBlock(scheme, 0, 0, 3, 2);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(2, subblocks.size());
        assertEquals("ae\n", subblocks.get(0).toString());
        assertEquals("bd\n", subblocks.get(1).toString());
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def", "ghi"};
        Block block = new ConcreteBlock(scheme, 0, 0, 3, 2);
        String expected = "Block(0,0, 3,2):\n" +
                          " ####\n" +
                          " # abc #\n" +
                          " # def #\n" +
                          " ####";
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def", "ghi"};
        Block block = new ConcreteBlock(scheme, 0, 0, 3, 2);
        assertEquals("abc", block.getUpperLine());
    }
}