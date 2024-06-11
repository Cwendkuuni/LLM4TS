package glengineer.blocks;

import glengineer.positions.CharPosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class BlockTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidArgumentsThrowsException() {
        Scheme scheme = new Scheme();
        new Block(scheme, -1, -1, 2, 2);
    }

    @Test
    public void testConstructorWithValidArguments() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 2, 2);
        Assert.assertNotNull(block);
        Assert.assertEquals(0, block.getX1());
        Assert.assertEquals(0, block.getY1());
        Assert.assertEquals(2, block.getX2());
        Assert.assertEquals(2, block.getY2());
        Assert.assertEquals(scheme, block.getScheme());
    }

    @Test
    public void testConstructorWithCharPosition() {
        Scheme scheme = new Scheme();
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(2, 2);
        Block block = new Block(scheme, p1, p2);
        Assert.assertNotNull(block);
        Assert.assertEquals(0, block.getX1());
        Assert.assertEquals(0, block.getY1());
        Assert.assertEquals(2, block.getX2());
        Assert.assertEquals(2, block.getY2());
        Assert.assertEquals(scheme, block.getScheme());
    }

    @Test
    public void testConstructorWithoutArguments() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme);
        Assert.assertNotNull(block);
        Assert.assertEquals(0, block.getX1());
        Assert.assertEquals(0, block.getY1());
        Assert.assertEquals(scheme.getX2(), block.getX2());
        Assert.assertEquals(scheme.getY2(), block.getY2());
        Assert.assertEquals(scheme, block.getScheme());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAtWithInvalidCoordinatesThrowsException() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme);
        block.charAt(-1, -1);
    }

    @Test
    public void testCharAtWithValidCoordinates() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcd";
        Block block = new Block(scheme);
        char result = block.charAt(2, 0);
        Assert.assertEquals('c', result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAtWithInvalidSublineCoordinatesThrowsException() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme);
        block.textAt(2, 5, 0);
    }

    @Test
    public void testTextAtWithValidSublineCoordinates() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcd";
        Block block = new Block(scheme);
        String result = block.textAt(1, 3, 0);
        Assert.assertEquals("bc", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordThrowsExceptionIfIsTrivial() {
        Block block = new Block(new Scheme());
        block.containsOnlyOneWord();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordThrowsExceptionIfEmptyBlock() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 0, 0);
        block.containsOnlyOneWord();
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcd";
        Block block = new Block(scheme);
        boolean result = block.containsOnlyOneWord();
        Assert.assertFalse(result);
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcd";
        Block block = new Block(scheme, 0, 0, 2, 2);
        String result = block.toString();
        String expected = "Block(0,0, 2,2):\n" +
                " #######\n" +
                " # ab #\n" +
                " # cd #\n" +
                " #######";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcd";
        Block block = new Block(scheme, 0, 0, 2, 2);
        String result = block.getUpperLine();
        Assert.assertEquals("ab", result);
    }

    @Test
    public void testSubblock() {
        Block block = new ConcreteBlock(new Scheme());
        Block subblock = block.subblock(0, 0, 1, 1);
        Assert.assertNotNull(subblock);
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Block block = new ConcreteBlock(new Scheme());
        List<Block> subblocks = block.extractSequentialSubblocks();
        Assert.assertNotNull(subblocks);
        Assert.assertTrue(subblocks.isEmpty());
    }

    @Test
    public void testExtractParallelSubblocks() {
        Block block = new ConcreteBlock(new Scheme());
        List<Block> subblocks = block.extractParallelSubblocks();
        Assert.assertNotNull(subblocks);
        Assert.assertTrue(subblocks.isEmpty());
    }
}