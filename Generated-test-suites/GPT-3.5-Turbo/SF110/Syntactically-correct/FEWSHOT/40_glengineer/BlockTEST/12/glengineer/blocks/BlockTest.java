package glengineer.blocks;

import glengineer.positions.*;
import java.util.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class BlockTest {

    @Test
    public void testConstructor1() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 1, 2, 3, 4);
        
        assertEquals(1, block.x1);
        assertEquals(2, block.y1);
        assertEquals(3, block.x2);
        assertEquals(4, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testConstructor2() {
        Scheme scheme = new Scheme();
        CharPosition p1 = new CharPosition(1, 2);
        CharPosition p2 = new CharPosition(3, 4);
        Block block = new Block(scheme, p1, p2);
        
        assertEquals(1, block.x1);
        assertEquals(2, block.y1);
        assertEquals(3, block.x2);
        assertEquals(4, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testConstructor3() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme);
        
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 1, 2, 3, 4);
        Block subBlock = block.subblock(2, 3, 5, 6);
        
        assertEquals(2, subBlock.x1);
        assertEquals(3, subBlock.y1);
        assertEquals(5, subBlock.x2);
        assertEquals(6, subBlock.y2);
        assertEquals(scheme, subBlock.scheme);
    }
    
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def"};
        Block block = new Block(scheme, 0, 0, 3, 2);
        
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
        scheme.lines = new String[]{"abcdef", "ghijkl"};
        Block block = new Block(scheme, 0, 0, 6, 2);
        
        assertEquals("abc", block.textAt(0, 3, 0));
        assertEquals("ghi", block.textAt(0, 3, 1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_trivialBlock() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme);
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_emptyBlock() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def"};
        Block block = new Block(scheme, 0, 2, 3, 2);
        block.containsOnlyOneWord();
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "abc"};
        Block block = new Block(scheme, 0, 1, 3, 1);
        
        assertTrue(block.containsOnlyOneWord());
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def"};
        Block block = new Block(scheme, 0, 0, 3, 2);
        
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(1, subblocks.size());
        Block subBlock = subblocks.get(0);
        assertEquals(0, subBlock.x1);
        assertEquals(0, subBlock.y1);
        assertEquals(3, subBlock.x2);
        assertEquals(2, subBlock.y2);
        assertEquals(scheme, subBlock.scheme);
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def"};
        Block block = new Block(scheme, 0, 0, 3, 2);
        
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(1, subblocks.size());
        Block subBlock = subblocks.get(0);
        assertEquals(0, subBlock.x1);
        assertEquals(0, subBlock.y1);
        assertEquals(3, subBlock.x2);
        assertEquals(2, subBlock.y2);
        assertEquals(scheme, subBlock.scheme);
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def"};
        Block block = new Block(scheme, 0, 0, 3, 2);
        
        String expected = "Block(0,0, 3,2):\n" +
                          " ##############\n" +
                          " # abc        #\n" +
                          " # def        #\n" +
                          " ##############";
                          
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def"};
        Block block = new Block(scheme, 0, 0, 3, 2);
        
        assertEquals("abc", block.getUpperLine());
    }

}