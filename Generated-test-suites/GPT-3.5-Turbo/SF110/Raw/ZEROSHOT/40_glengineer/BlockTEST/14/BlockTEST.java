package glengineer.blocks;

import glengineer.positions.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalBlockDefinition() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, -1, 0, 2, 3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAtOutOfBounds() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme);
        block.charAt(1, 6);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAtImproperCoordinates() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme);
        block.textAt(2, 5, 3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithTrivialBlock() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme);
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithEmptyBlock() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 4, 0, 4);
        block.containsOnlyOneWord();
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme) {
            @Override
            public Block subblock(int p0, int p1, int p2, int p3) {
                return null; // implementation not required for test
            }

            @Override
            public List<Block> extractSequentialSubblocks() {
                return null; // implementation not required for test
            }

            @Override
            public List<Block> extractParallelSubblocks() {
                return null; // implementation not required for test
            }
        };
        assertNull(block.extractSequentialSubblocks());
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme) {
            @Override
            public Block subblock(int p0, int p1, int p2, int p3) {
                return null; // implementation not required for test
            }

            @Override
            public List<Block> extractSequentialSubblocks() {
                return null; // implementation not required for test
            }

            @Override
            public List<Block> extractParallelSubblocks() {
                return null; // implementation not required for test
            }
        };
        assertNull(block.extractParallelSubblocks());
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme) {
            @Override
            public Block subblock(int p0, int p1, int p2, int p3) {
                return null; // implementation not required for test
            }

            @Override
            public List<Block> extractSequentialSubblocks() {
                return null; // implementation not required for test
            }

            @Override
            public List<Block> extractParallelSubblocks() {
                return null; // implementation not required for test
            }
        };
        assertEquals("Block(0,0, 0,0):\n ####\n", block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme) {
            @Override
            public Block subblock(int p0, int p1, int p2, int p3) {
                return null; // implementation not required for test
            }

            @Override
            public List<Block> extractSequentialSubblocks() {
                return null; // implementation not required for test
            }

            @Override
            public List<Block> extractParallelSubblocks() {
                return null; // implementation not required for test
            }
        };
        assertEquals(" ", block.getUpperLine());
    }
}