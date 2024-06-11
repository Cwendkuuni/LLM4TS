
package glengineer.blocks;

import glengineer.positions.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BlockTest {

    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        Block subblock = block.subblock(1, 1, 2, 2);

        Assert.assertEquals(1, subblock.getX1());
        Assert.assertEquals(1, subblock.getY1());
        Assert.assertEquals(2, subblock.getX2());
        Assert.assertEquals(2, subblock.getY2());
    }

    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] {"abc", "def"};
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);

        Assert.assertEquals('a', block.charAt(0, 0));
        Assert.assertEquals('b', block.charAt(1, 0));
        Assert.assertEquals('c', block.charAt(2, 0));
        Assert.assertEquals('d', block.charAt(0, 1));
        Assert.assertEquals('e', block.charAt(1, 1));
        Assert.assertEquals('f', block.charAt(2, 1));
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] {"abc", "def"};
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);

        Assert.assertEquals("ab", block.textAt(0, 2, 0));
        Assert.assertEquals("de", block.textAt(0, 2, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_TrivialBlock_ThrowsException() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme);

        block.containsOnlyOneWord();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_EmptyBlock_ThrowsException() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 1, 1, 1, 1);

        block.containsOnlyOneWord();
    }

    @Test
    public void testContainsOnlyOneWord_ValidBlock_ReturnsTrue() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] {"abc", "abc"};
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);

        boolean result = block.containsOnlyOneWord();

        Assert.assertTrue(result);
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] {"abc", "def", "ghi"};
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);

        List<Block> subblocks = block.extractSequentialSubblocks();

        Assert.assertEquals(3, subblocks.size());
        Assert.assertEquals("abc", subblocks.get(0).toString());
        Assert.assertEquals("def", subblocks.get(1).toString());
        Assert.assertEquals("ghi", subblocks.get(2).toString());
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] {"abc", "def", "ghi"};
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);

        List<Block> subblocks = block.extractParallelSubblocks();

        Assert.assertEquals(3, subblocks.size());
        Assert.assertEquals("adg", subblocks.get(0).toString());
        Assert.assertEquals("beh", subblocks.get(1).toString());
        Assert.assertEquals("cfi", subblocks.get(2).toString());
    }

    private class ConcreteBlock extends Block {

        public ConcreteBlock(Scheme scheme, int x1, int y1, int x2, int y2) {
            super(scheme, x1, y1, x2, y2);
        }

        @Override
        public Block subblock(int p0, int p1, int p2, int p3) {
            return new ConcreteBlock(scheme, p0, p1, p2, p3);
        }

        @Override
        public List<Block> extractSequentialSubblocks() {
            List<Block> subblocks = new ArrayList<>();
            for (int y = this.y1; y <= this.y2; y++) {
                Block subblock = new ConcreteBlock(scheme, this.x1, y, this.x2, y);
                subblocks.add(subblock);
            }
            return subblocks;
        }

        @Override
        public List<Block> extractParallelSubblocks() {
            List<Block> subblocks = new ArrayList<>();
            for (int x = this.x1; x <= this.x2; x++) {
                Block subblock = new ConcreteBlock(scheme, x, this.y1, x, this.y2);
                subblocks.add(subblock);
            }
            return subblocks;
        }
    }
}
