package glengineer.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import glengineer.blocks.*;
import glengineer.positions.*;

public class BlockTest {

    private Scheme scheme;
    
    @Before
    public void setUp() {
        scheme = new Scheme(10, 10);
    }
    
    @Test
    public void testBlockConstructorWithCoordinates() {
        Block block = new ConcreteBlock(scheme, 1, 2, 3, 4);
        assertNotNull(block);
    }
    
    @Test
    public void testBlockConstructorWithPositions() {
        CharPosition p1 = new CharPosition(1, 2);
        CharPosition p2 = new CharPosition(3, 4);
        Block block = new ConcreteBlock(scheme, p1, p2);
        assertNotNull(block);
    }
   
    @Test
    public void testBlockConstructorWithDefaultCoordinates() {
        Block block = new ConcreteBlock(scheme);
        assertNotNull(block);
    }
    
    @Test
    public void testSubblock() {
        Block block = new ConcreteBlock(scheme, 1, 2, 5, 6);
        Block subblock = block.subblock(2, 3, 4, 5);
        assertNotNull(subblock);
    }
    
    @Test
    public void testCharAtValid() {
        Block block = new ConcreteBlock(scheme, 1, 2, 3, 4);
        assertEquals('a', block.charAt(1, 2));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAtInvalid() {
        Block block = new ConcreteBlock(scheme, 1, 2, 3, 4);
        block.charAt(10, 10);
    }
    
    @Test
    public void testTextAtValid() {
        Block block = new ConcreteBlock(scheme, 1, 2, 5, 6);
        assertEquals("test", block.textAt(1, 5, 2));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAtInvalid() {
        Block block = new ConcreteBlock(scheme, 1, 2, 5, 6);
        block.textAt(6, 10, 2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithTrivialBlock() {
        Block block = new ConcreteBlock(scheme, 1, 2, 1, 2);
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithEmptyBlock() {
        Block block = new ConcreteBlock(scheme, 1, 2, 1, 3);
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Block block = new ConcreteBlock(scheme, 1, 2, 5, 6);
        assertNotNull(block.extractSequentialSubblocks());
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Block block = new ConcreteBlock(scheme, 1, 2, 5, 6);
        assertNotNull(block.extractParallelSubblocks());
    }
    
    @Test
    public void testToString() {
        Block block = new ConcreteBlock(scheme, 1, 2, 5, 6);
        assertNotNull(block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Block block = new ConcreteBlock(scheme, 1, 2, 5, 6);
        assertEquals("test", block.getUpperLine());
    }
}