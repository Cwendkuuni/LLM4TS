package glengineer.blocks;

import glengineer.positions.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BlockTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlock_IllegalArgument() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, -1, -1, 5, 5);
    }
    
    @Test
    public void testBlock_Constructor1() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 0, 0, 5, 5);
        assertEquals(scheme, block.getScheme());
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(5, block.getX2());
        assertEquals(5, block.getY2());
    }
    
    @Test
    public void testBlock_Constructor2() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(5, 5);
        Block block = new Block(scheme, p1, p2);
        assertEquals(scheme, block.getScheme());
        assertEquals(p1.getX(), block.getX1());
        assertEquals(p1.getY(), block.getY1());
        assertEquals(p2.getX(), block.getX2());
        assertEquals(p2.getY(), block.getY2());
    }
    
    @Test
    public void testBlock_Constructor3() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme);
        assertEquals(scheme, block.getScheme());
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(scheme.getX2(), block.getX2());
        assertEquals(scheme.getY2(), block.getY2());
    }
    
    @Test
    public void testBlock_subblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 0, 0, 5, 5);
        
        Block subblock = block.subblock(1, 1, 4, 4);
        assertEquals(1, subblock.getX1());
        assertEquals(1, subblock.getY1());
        assertEquals(4, subblock.getX2());
        assertEquals(4, subblock.getY2());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlock_charAt_OutOfBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 0, 0, 5, 5);
        
        block.charAt(6, 6);
    }
    
    @Test
    public void testBlock_charAt() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "Hello World";
        Block block = new Block(scheme, 0, 0, 5, 1);
        
        char ch = block.charAt(0, 0);
        assertEquals('H', ch);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlock_textAt_ImproperCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 0, 0, 5, 5);
        
        block.textAt(0, 6, 0);
    }
    
    @Test
    public void testBlock_textAt() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "Hello World";
        Block block = new Block(scheme, 0, 0, 5, 0);
        
        String text = block.textAt(0, 5, 0);
        assertEquals("Hello", text);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlock_containsOnlyOneWord_TrivialBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 0, 0, 0, 0);
        
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlock_containsOnlyOneWord_EmptyBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, 0, 0, 5, 5);
        
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testBlock_containsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "Hello World";
        Block block = new Block(scheme, 0, 0, 5, 0);
        
        boolean result = block.containsOnlyOneWord();
        assertTrue(result);
    }
    
    @Test
    public void testBlock_extractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "Hello World";
        Block block = new Block(scheme, 0, 0, 5, 0);
        
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(1, subblocks.size());
    }
    
    @Test
    public void testBlock_extractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "Hello World";
        Block block = new Block(scheme, 0, 0, 5, 0);
        
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(1, subblocks.size());
    }
    
    @Test
    public void testBlock_toString() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "Hello World";
        Block block = new Block(scheme, 0, 0, 5, 0);
        
        String expected = "Block(0,0, 5,0):\n" +
                        " #######\n" +
                        " # Hello #\n" +
                        " #######";
        
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testBlock_getUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "Hello World";
        Block block = new Block(scheme, 0, 0, 5, 0);
        
        String upperLine = block.getUpperLine();
        assertEquals("Hello", upperLine);
    }
    
}