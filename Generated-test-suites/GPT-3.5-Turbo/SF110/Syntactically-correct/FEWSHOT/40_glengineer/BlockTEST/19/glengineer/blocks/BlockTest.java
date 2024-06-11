package glengineer.blocks;

import glengineer.positions.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BlockTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorThrowsExceptionForInvalidBlockDefinition() {
        Scheme scheme = new Scheme();
        new Block(scheme, -1, 0, 2, 3);
    }

    @Test
    public void testConstructorSetsValuesForValidBlockDefinition() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 2, 3);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(2, block.x2);
        assertEquals(3, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testConstructorSetsValuesForValidCharPosition() {
        Scheme scheme = new Scheme();
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(2, 3);
        Block block = new Block(scheme, p1, p2);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(2, block.x2);
        assertEquals(3, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testConstructorSetsValuesForCompleteScheme() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testSubblockReturnsCorrectBlock() {
        Block block = new Block(new Scheme(), 0, 0, 2, 2);
        Block subblock = block.subblock(1, 1, 2, 2);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(2, subblock.x2);
        assertEquals(2, subblock.y2);
    }

    @Test
    public void testCharAtReturnsCorrectCharacter() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def", "ghi"};
        Block block = new Block(scheme, 0, 0, 3, 3);
        assertEquals('a', block.charAt(0, 0));
        assertEquals('d', block.charAt(0, 1));
        assertEquals('g', block.charAt(0, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAtThrowsExceptionForOutOfBoundsPosition() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def", "ghi"};
        Block block = new Block(scheme, 0, 0, 3, 3);
        block.charAt(5, 5);
    }

    @Test
    public void testTextAtReturnsCorrectSubstring() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def", "ghi"};
        Block block = new Block(scheme, 0, 0, 3, 3);
        assertEquals("ab", block.textAt(0, 2, 0));
        assertEquals("de", block.textAt(0, 2, 1));
        assertEquals("gh", block.textAt(0, 2, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAtThrowsExceptionForImproperCoordinates() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def", "ghi"};
        Block block = new Block(scheme, 0, 0, 3, 3);
        block.textAt(2, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordThrowsExceptionForTrivialBlock() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 0, 0);
        block.containsOnlyOneWord();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordThrowsExceptionForEmptyBlock() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 1, 1);
        block.containsOnlyOneWord();
    }

    @Test
    public void testContainsOnlyOneWordReturnsTrueForBlockWithOneWord() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def", "abc"};
        Block block = new Block(scheme, 0, 0, 3, 3);
        assertTrue(block.containsOnlyOneWord());
    }

    @Test
    public void testContainsOnlyOneWordReturnsFalseForBlockWithMultipleWords() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def", "ghi"};
        Block block = new Block(scheme, 0, 0, 3, 3);
        assertFalse(block.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocksReturnsEmptyListForTrivialBlock() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 0, 0);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertTrue(subblocks.isEmpty());
    }

    @Test
    public void testExtractParallelSubblocksReturnsEmptyListForTrivialBlock() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 0, 0);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertTrue(subblocks.isEmpty());
    }

    @Test
    public void testToStringReturnsCorrectStringRepresentationForBlock() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def", "ghi"};
        Block block = new Block(scheme, 0, 0, 3, 3);
        String expectedString = "Block(0,0, 3,3):\n" +
                " #######\n" +
                " # abc #\n" +
                " # def #\n" +
                " # ghi #\n" +
                " #######";
        assertEquals(expectedString, block.toString());
    }

    @Test
    public void testGetUpperLineReturnsCorrectSubstring() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[]{"abc", "def", "ghi"};
        Block block = new Block(scheme, 0, 0, 3, 3);
        assertEquals("abc", block.getUpperLine());
    }
}