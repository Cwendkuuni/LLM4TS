package glengineer.blocks;

import glengineer.positions.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BlockTest {

    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 1, 1, 3, 3);
        
        Block subblock = block.subblock(2, 2, 3, 3);
        
        assertNotNull(subblock);
        assertTrue(subblock instanceof TestBlock);
        
        assertEquals(2, subblock.x1);
        assertEquals(2, subblock.y1);
        assertEquals(3, subblock.x2);
        assertEquals(3, subblock.y2);
    }

    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 1, 1, 3, 3);
        
        assertEquals('x', block.charAt(1, 1));
        assertEquals('x', block.charAt(2, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAt_invalidPosition() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 1, 1, 3, 3);
        
        block.charAt(4, 4);
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 1, 1, 3, 3);
        
        assertEquals("xxx", block.textAt(1, 4, 1));
        assertEquals("xxx", block.textAt(2, 5, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAt_invalidCoordinates() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 1, 1, 3, 3);
        
        block.textAt(4, 5, 1);
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme();
        // Test for containsOnlyOneWord true
        Block block1 = new TestBlock(scheme, 1, 1, 3, 3);
        assertTrue(block1.containsOnlyOneWord());
        
        // Test for containsOnlyOneWord false
        Block block2 = new TestBlock(scheme, 1, 1, 2, 2);
        assertFalse(block2.containsOnlyOneWord());
        
        // Test for trivial block
        Block block3 = new TestBlock(scheme);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, block3::containsOnlyOneWord);
        assertEquals("cannot search in a trivial block", exception.getMessage());
        
        // Test for empty block
        Block block4 = new TestBlock(scheme, 1, 1, 1, 1);
        exception = assertThrows(IllegalArgumentException.class, block4::containsOnlyOneWord);
        assertEquals("cannot verify unicity of an word in an empty block", exception.getMessage());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 1, 1, 3, 3);
        
        List<Block> subblocks = block.extractSequentialSubblocks();
        
        assertNotNull(subblocks);
        assertEquals(1, subblocks.size());
        
        Block subblock = subblocks.get(0);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(3, subblock.x2);
        assertEquals(3, subblock.y2);
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 1, 1, 3, 3);
        
        List<Block> subblocks = block.extractParallelSubblocks();
        
        assertNotNull(subblocks);
        assertEquals(1, subblocks.size());
        
        Block subblock = subblocks.get(0);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(3, subblock.x2);
        assertEquals(3, subblock.y2);
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 1, 1, 3, 3);
        
        String expected = "Block(1,1, 3,3):\n" +
                          " ####\n" +
                          " #xxx#\n" +
                          " #xxx#\n" +
                          " #xxx#\n" +
                          " ####";
        
        assertEquals(expected, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 1, 1, 3, 3);
        
        assertEquals("xxx", block.getUpperLine());
    }
}

class TestBlock extends Block {
    
    public TestBlock(Scheme scheme, int x1, int y1, int x2, int y2) {
        super(scheme, x1, y1, x2, y2);
    }
    
    public TestBlock(Scheme scheme) {
        super(scheme);
    }
    
    @Override
    public Block subblock(int p0, int p1, int p2, int p3) {
        return new TestBlock(scheme, p0, p1, p2, p3);
    }
    
    @Override
    public List<Block> extractSequentialSubblocks() {
        List<Block> subblocks = new ArrayList<>();
        subblocks.add(this);
        return subblocks;
    }
    
    @Override
    public List<Block> extractParallelSubblocks() {
        List<Block> subblocks = new ArrayList<>();
        subblocks.add(this);
        return subblocks;
    }
    
    @Override
    public char charAt(int x, int y) {
        return 'x';
    }
    
    @Override
    public String textAt(int x1, int x2, int y) {
        return "xxx";
    }
    
    @Override
    public String toString() {
        return "TestBlock";
    }
    
    @Override
    public String getUpperLine() {
        return "xxx";
    }
}