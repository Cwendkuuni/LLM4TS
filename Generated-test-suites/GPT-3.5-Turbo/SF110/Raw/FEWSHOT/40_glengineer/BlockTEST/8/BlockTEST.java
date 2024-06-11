package glengineer.blocks;

import glengineer.positions.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {
    @Test
    public void testConstructor1() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 0, 0, 5, 5);
        
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testConstructor2() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(5, 5);
        Block block = new BlockImpl(scheme, p1, p2);

        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testConstructor3() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme);
        
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(10, block.x2);
        assertEquals(10, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 0, 0, 5, 5);
        Block subblock = block.subblock(1, 1, 4, 4);
        
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(4, subblock.x2);
        assertEquals(4, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }
    
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 0, 0, 5, 5);
        
        scheme.lines = new String[]{"1234567890",
                                     "abcdefghij",
                                     "ABCDEFGHIJ",
                                     "0987654321"};
                                     
        assertEquals('1', block.charAt(0, 0));
        assertEquals('6', block.charAt(5, 1));
        assertEquals('A', block.charAt(0, 2));
        assertEquals('0', block.charAt(5, 3));
    }
    
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 0, 0, 5, 5);
        
        scheme.lines = new String[]{"1234567890",
                                     "abcdefghij",
                                     "ABCDEFGHIJ",
                                     "0987654321"};
                                     
        assertEquals("123", block.textAt(0, 3, 0));
        assertEquals("fgh", block.textAt(1, 4, 1));
        assertEquals("CDE", block.textAt(2, 5, 2));
        assertEquals("21", block.textAt(3, 5, 3));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordException() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 0, 0, 0, 0);
        
        block.containsOnlyOneWord();
    }
    
    // Write test cases for other methods...
}