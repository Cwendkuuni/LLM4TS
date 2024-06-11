package glengineer.blocks;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import glengineer.positions.*;
import java.util.ArrayList;

public class BlockTest {
    @Test
    public void testBlockConstructorWithValidParams() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(2, block.getX2());
        assertEquals(2, block.getY2());
        
        assertEquals(scheme, block.getScheme());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidParams() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 12, 12);
    }
    
    @Test
    public void testBlockConstructorWithCharPositionParams() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(2, 2);
        Block block = new ConcreteBlock(scheme, p1, p2);
        
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(2, block.getX2());
        assertEquals(2, block.getY2());
        
        assertEquals(scheme, block.getScheme());
    }
    
    @Test
    public void testBlockConstructorWithSchemeParam() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme);
        
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(10, block.getX2());
        assertEquals(10, block.getY2());
        
        assertEquals(scheme, block.getScheme());
    }
    
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 4, 4);
        Block subblock = block.subblock(1, 1, 3, 3);
        
        assertEquals(1, subblock.getX1());
        assertEquals(1, subblock.getY1());
        assertEquals(3, subblock.getX2());
        assertEquals(3, subblock.getY2());
        
        assertEquals(scheme, subblock.getScheme());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubblockWithInvalidParams() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 4, 4);
        Block subblock = block.subblock(5, 5, 7, 7);
    }
    
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[0] = "#####";
        scheme.lines[1] = "#H#H#";
        scheme.lines[2] = "#e#e#";
        scheme.lines[3] = "#l#l#";
        scheme.lines[4] = "#####";
        
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        
        assertEquals('#', block.charAt(1, 1));
        assertEquals('H', block.charAt(1, 2));
        assertEquals('e', block.charAt(2, 2));
        assertEquals('l', block.charAt(3, 3));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAtWithInvalidPosition() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[0] = "#####";
        scheme.lines[1] = "#H#H#";
        scheme.lines[2] = "#e#e#";
        scheme.lines[3] = "#l#l#";
        scheme.lines[4] = "#####";
        
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        
        block.charAt(4, 4);
    }
    
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[0] = "#####";
        scheme.lines[1] = "#H#H#";
        scheme.lines[2] = "#e#e#";
        scheme.lines[3] = "#l#l#";
        scheme.lines[4] = "#####";
        
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        
        assertEquals("#H", block.textAt(1, 2, 1));
        assertEquals("e#", block.textAt(2, 3, 2));
        assertEquals("#l", block.textAt(3, 4, 3));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAtWithInvalidCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[0] = "#####";
        scheme.lines[1] = "#H#H#";
        scheme.lines[2] = "#e#e#";
        scheme.lines[3] = "#l#l#";
        scheme.lines[4] = "#####";
        
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        
        block.textAt(4, 5, 1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithTrivialBlock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 0, 0, 0, 0);
        
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithEmptyBlock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 0);
        
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testContainsOnlyOneWordWithNonEmptyBlock() {
        Scheme scheme1 = new Scheme(5, 5);
        scheme1.lines[0] = "#####";
        scheme1.lines[1] = "#H#H#";
        scheme1.lines[2] = "#e#e#";
        scheme1.lines[3] = "#####";
        scheme1.lines[4] = "#####";
        
        Block block1 = new ConcreteBlock(scheme1, 1, 1, 3, 2);
        assertTrue(block1.containsOnlyOneWord());
        
        Scheme scheme2 = new Scheme(5, 5);
        scheme2.lines[0] = "#####";
        scheme2.lines[1] = "#H#H#";
        scheme2.lines[2] = "#e#e#";
        scheme2.lines[3] = "#l#l#";
        scheme2.lines[4] = "#####";
        
        Block block2 = new ConcreteBlock(scheme2, 1, 1, 3, 3);
        assertFalse(block2.containsOnlyOneWord());
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[0] = "#####";
        scheme.lines[1] = "#H#e#";
        scheme.lines[2] = "#ell#";
        scheme.lines[3] = "#ell#";
        scheme.lines[4] = "#####";
        
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(3, subblocks.size());
        
        assertEquals("H", subblocks.get(0).getUpperLine());
        assertEquals("ell", subblocks.get(1).getUpperLine());
        assertEquals("ell", subblocks.get(2).getUpperLine());
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[0] = "#####";
        scheme.lines[1] = "#H#e#";
        scheme.lines[2] = "#ell#";
        scheme.lines[3] = "#ell#";
        scheme.lines[4] = "#####";
        
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(2, subblocks.size());
        
        assertEquals("#e#", subblocks.get(0).getUpperLine());
        assertEquals("#e#", subblocks.get(1).getUpperLine());
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[0] = "#####";
        scheme.lines[1] = "#H#e#";
        scheme.lines[2] = "#ell#";
        scheme.lines[3] = "#ell#";
        scheme.lines[4] = "#####";
        
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        
        String expected = "Block(1,1, 3,3):\n" +
                          " #############\n" +
                          " # H # e # # #\n" +
                          " # e l l # # #\n" +
                          " # e l l # # #\n" +
                          " #############";
                          
        assertEquals(expected, block.toString());
    }
    
    private class ConcreteBlock extends Block {
        public ConcreteBlock(Scheme scheme, int x1, int y1, int x2, int y2) {
            super(scheme, x1, y1, x2, y2);
        }
        
        public ConcreteBlock(Scheme scheme, CharPosition p1, CharPosition p2) {
            super(scheme, p1, p2);
        }
        
        public ConcreteBlock(Scheme scheme) {
            super(scheme);
        }
        
        public Block subblock(int p0, int p1, int p2, int p3) {
            return null;
        }
        
        public List<Block> extractSequentialSubblocks() {
            return new ArrayList<Block>();
        }
        
        public List<Block> extractParallelSubblocks() {
            return new ArrayList<Block>();
        }
    }
}