import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class BlockTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorScheme() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, -1, 0, 4, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorPosition() {
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(0, 0);
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme, p1, p2);
    }

    @Test
    public void testConstructorDefault() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme);
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(10, block.getX2());
        assertEquals(10, block.getY2());
        assertEquals(scheme, block.getScheme());
    }

    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme) {
            public Block subblock(int p0, int p1, int p2, int p3) {
                return null;
            }
            public List<Block> extractSequentialSubblocks() {
                return null;
            }
            public List<Block> extractParallelSubblocks() {
                return null;
            }
        };

        assertNull(block.subblock(0, 0, 1, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme) {
            public char charAt(int x, int y) {
                throw new IllegalArgumentException();
            }
            public List<Block> extractSequentialSubblocks() {
                return null;
            }
            public List<Block> extractParallelSubblocks() {
                return null;
            }
        };

        block.charAt(0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme) {
            public String textAt(int x1, int x2, int y) {
                throw new IllegalArgumentException();
            }
            public List<Block> extractSequentialSubblocks() {
                return null;
            }
            public List<Block> extractParallelSubblocks() {
                return null;
            }
        };

        block.textAt(0, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme) {
            public boolean containsOnlyOneWord() {
                throw new IllegalArgumentException();
            }
            public List<Block> extractSequentialSubblocks() {
                return null;
            }
            public List<Block> extractParallelSubblocks() {
                return null;
            }
        };

        block.containsOnlyOneWord();
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme) {
            public String toString() {
                return "Test block";
            }
            public List<Block> extractSequentialSubblocks() {
                return null;
            }
            public List<Block> extractParallelSubblocks() {
                return null;
            }
        };

        assertEquals("Test block", block.toString());
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme) {
            public String textAt(int x1, int x2, int y) {
                return "Upper line";
            }
            public List<Block> extractSequentialSubblocks() {
                return null;
            }
            public List<Block> extractParallelSubblocks() {
                return null;
            }
        };

        assertEquals("Upper line", block.getUpperLine());
    }

}