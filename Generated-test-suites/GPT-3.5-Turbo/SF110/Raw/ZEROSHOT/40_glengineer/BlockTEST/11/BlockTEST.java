package glengineer.blocks;

import org.junit.*;
import static org.junit.Assert.*;
import glengineer.positions.*;

public class BlockTest {

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor1() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, -1, 0, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor2() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 0, -1, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor3() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 0, 0, 5, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor4() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 0, 0, 0, 5);
    }

    @Test
    public void testBlockConstructor5() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 0, 0, 0, 0);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(0, block.x2);
        assertEquals(0, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructor6() {
        Scheme scheme = new Scheme(5, 5);
        CharPosition p1 = new CharPosition(2, 2);
        CharPosition p2 = new CharPosition(4, 4);
        Block block = new Block(scheme, p1, p2);
        assertEquals(2, block.x1);
        assertEquals(2, block.y1);
        assertEquals(4, block.x2);
        assertEquals(4, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructor7() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAt1() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme);
        block.charAt(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAt2() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme);
        block.charAt(0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAt3() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme);
        block.charAt(6, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAt4() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme);
        block.charAt(0, 6);
    }

    @Test
    public void testCharAt5() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[2] = "abcde";
        Block block = new Block(scheme);
        assertEquals('a', block.charAt(0, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAt1() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme);
        block.textAt(-1, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAt2() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme);
        block.textAt(0, 6, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAt3() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme);
        block.textAt(0, 0, -1);
    }

    @Test
    public void testTextAt4() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[2] = "abcde";
        Block block = new Block(scheme);
        assertEquals("abc", block.textAt(0, 3, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord1() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme);
        block.containsOnlyOneWord();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord2() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme);
        block.y2 = 5;
        block.containsOnlyOneWord();
    }

    @Test
    public void testContainsOnlyOneWord3() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[0] = "abcdef";
        Block block = new Block(scheme);
        assertTrue(block.containsOnlyOneWord());
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[0] = "abcdef";
        Block block = new Block(scheme);
        assertEquals("Block(0,0, 5,5):\n" +
                " ############\n" +
                " # abcdef #\n" +
                " ############", block.toString());
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[0] = "abcdef";
        Block block = new Block(scheme);
        assertEquals("abcdef", block.getUpperLine());
    }
}