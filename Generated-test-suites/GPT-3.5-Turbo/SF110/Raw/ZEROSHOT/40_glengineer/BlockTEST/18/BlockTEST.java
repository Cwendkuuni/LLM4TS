import static org.junit.Assert.*;
import org.junit.Test;

public class BlockTest {
    
    @Test
    public void testConstructor() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 5, 5);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testConstructorWithCharPositions() {
        Scheme scheme = new Scheme();
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(5, 5);
        Block block = new Block(scheme, p1, p2);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testConstructorWithScheme() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorThrowsException() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, -1, 0, 5, 5);
    }
    
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 5, 5) {
            @Override
            public Block subblock(int p0, int p1, int p2, int p3) {
                return null;
            }
        };
        assertNull(block.subblock(1, 2, 3, 4));
    }
    
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcdef";
        scheme.lines[1] = "ghijkl";
        Block block = new Block(scheme, 0, 0, 5, 5) {
            @Override
            public Block subblock(int p0, int p1, int p2, int p3) {
                return null;
            }
        };
        assertEquals('a', block.charAt(0, 0));
        assertEquals('b', block.charAt(1, 0));
        assertEquals('c', block.charAt(2, 0));
        assertEquals('d', block.charAt(3, 0));
        assertEquals('e', block.charAt(4, 0));
        assertEquals('f', block.charAt(5, 0));
        assertEquals('g', block.charAt(0, 1));
        assertEquals('h', block.charAt(1, 1));
        assertEquals('i', block.charAt(2, 1));
        assertEquals('j', block.charAt(3, 1));
        assertEquals('k', block.charAt(4, 1));
        assertEquals('l', block.charAt(5, 1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAtThrowsException() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 5, 5) {
            @Override
            public Block subblock(int p0, int p1, int p2, int p3) {
                return null;
            }
        };
        block.charAt(6, 0);
    }
    
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcdef";
        scheme.lines[1] = "ghijkl";
        Block block = new Block(scheme, 0, 0, 5, 5) {
            @Override
            public Block subblock(int p0, int p1, int p2, int p3) {
                return null;
            }
        };
        assertEquals("abc", block.textAt(0, 3, 0));
        assertEquals("def", block.textAt(3, 6, 0));
        assertEquals("ghi", block.textAt(0, 3, 1));
        assertEquals("jkl", block.textAt(3, 6, 1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAtThrowsException() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 5, 5) {
            @Override
            public Block subblock(int p0, int p1, int p2, int p3) {
                return null;
            }
        };
        block.textAt(0, 6, 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordThrowsException() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 5, 5) {
            @Override
            public Block subblock(int p0, int p1, int p2, int p3) {
                return null;
            }
        };
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 5, 5) {
            @Override
            public Block subblock(int p0, int p1, int p2, int p3) {
                return null;
            }
            
            @Override
            public List<Block> extractSequentialSubblocks() {
                return null;
            }
        };
        assertNull(block.extractSequentialSubblocks());
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 5, 5) {
            @Override
            public Block subblock(int p0, int p1, int p2, int p3) {
                return null;
            }
            
            @Override
            public List<Block> extractParallelSubblocks() {
                return null;
            }
        };
        assertNull(block.extractParallelSubblocks());
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcdef";
        scheme.lines[1] = "ghijkl";
        Block block = new Block(scheme, 0, 0, 5, 5) {
            @Override
            public Block subblock(int p0, int p1, int p2, int p3) {
                return null;
            }
        };
        String expected = "Block(0,0, 5,5):\n"
                        + " #######\n"
                        + " # abc #\n"
                        + " # def #\n"
                        + " # ghi #\n"
                        + " # jkl #\n"
                        + " #######";
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcdef";
        scheme.lines[1] = "ghijkl";
        Block block = new Block(scheme, 0, 0, 5, 5) {
            @Override
            public Block subblock(int p0, int p1, int p2, int p3) {
                return null;
            }
        };
        assertEquals("abc", block.getUpperLine());
    }
}