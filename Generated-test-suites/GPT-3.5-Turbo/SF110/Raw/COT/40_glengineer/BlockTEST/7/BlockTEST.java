
import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {
    
    @Test
    public void testBlockConstructorWithValidParameters() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(2, block.x2);
        assertEquals(2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidParameters() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, -1, 0, 2, 2);
    }
    
    @Test
    public void testBlockSubblock() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 4, 4);
        Block subblock = block.subblock(1, 1, 3, 3);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(3, subblock.x2);
        assertEquals(3, subblock.y2);
    }
    
    @Test
    public void testBlockCharAt() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] { "abc", "def", "ghi" };
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        assertEquals('a', block.charAt(0, 0));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockCharAtWithInvalidCoordinates() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] { "abc", "def", "ghi" };
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        block.charAt(3, 0);
    }
    
    @Test
    public void testBlockTextAt() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] { "abc", "def", "ghi" };
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        assertEquals("ab", block.textAt(0, 2, 0));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockTextAtWithInvalidCoordinates() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] { "abc", "def", "ghi" };
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        block.textAt(3, 4, 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockContainsOnlyOneWordWithTrivialBlock() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteTrivialBlock(scheme);
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockContainsOnlyOneWordWithEmptyBlock() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testBlockToString() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] { "abc", "def", "ghi" };
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        String expected = "Block(0,0, 2,2):\n" +
                          " ####\n" + 
                          " # ab #\n" +
                          " # de #\n" +
                          " ####";
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testBlockGetUpperLine() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] { "abc", "def", "ghi" };
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        assertEquals("ab", block.getUpperLine());
    }
}
