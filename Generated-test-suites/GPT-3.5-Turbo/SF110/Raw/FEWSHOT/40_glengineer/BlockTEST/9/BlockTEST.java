package glengineer.blocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BlockTest {
    
    @Test
    public void testBlockConstructor1() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 1, 1);
        assertNotNull(block);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor2() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, -1, 0, 1, 1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor3() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, -1, 1, 1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor4() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 1, 1, 0, 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor5() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 2, 1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor6() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 1, 2);
    }
    
    @Test
    public void testBlockConstructor7() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme);
        assertNotNull(block);
    }
    
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 2, 2);
        Block subblock = block.subblock(1, 1, 2, 2);
        assertEquals(1, subblock.getX1());
        assertEquals(1, subblock.getY1());
        assertEquals(2, subblock.getX2());
        assertEquals(2, subblock.getY2());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAt1() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme);
        block.charAt(0, 1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAt2() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 1, 1);
        block.charAt(1, 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAt() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 1, 1);
        block.textAt(0, 2, 0);
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 1, 1);
        assertFalse(block.containsOnlyOneWord());
        
        block = new BlockImpl(scheme, 0, 0, 1, 0);
        assertTrue(block.containsOnlyOneWord());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 1, 1);
        block.extractSequentialSubblocks();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 1, 1);
        block.extractParallelSubblocks();
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 1, 1);
        String expected = "Block(0,0, 1,1):\n # \n # \n";
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 1, 1);
        String upperLine = block.getUpperLine();
        assertEquals("#", upperLine);
    }
}