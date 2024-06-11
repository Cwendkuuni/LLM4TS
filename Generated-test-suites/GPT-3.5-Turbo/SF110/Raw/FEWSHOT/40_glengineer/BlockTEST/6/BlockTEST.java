import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class BlockTest {
    @Test
    public void testConstructor() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
        
        Block block2 = new ConcreteBlock(scheme, new CharPosition(0, 0), new CharPosition(5, 5));
        assertEquals(0, block2.x1);
        assertEquals(0, block2.y1);
        assertEquals(5, block2.x2);
        assertEquals(5, block2.y2);
        assertEquals(scheme, block2.scheme);
        
        Block block3 = new ConcreteBlock(scheme);
        assertEquals(0, block3.x1);
        assertEquals(0, block3.y1);
        assertEquals(scheme.x2, block3.x2);
        assertEquals(scheme.y2, block3.y2);
        assertEquals(scheme, block3.scheme);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 5, 5, 0, 0);
    }
    
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        Block subblock = block.subblock(1, 1, 3, 3);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(3, subblock.x2);
        assertEquals(3, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }
    
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "1234567890";
        scheme.lines[1] = "abcdefghij";
        scheme.lines[2] = "klmnopqrst";
        Block block = new ConcreteBlock(scheme, 0, 0, 10, 3);
        assertEquals('1', block.charAt(0, 0));
        assertEquals('5', block.charAt(4, 0));
        assertEquals('a', block.charAt(0, 1));
        assertEquals('g', block.charAt(6, 1));
        assertEquals('k', block.charAt(0, 2));
        assertEquals('p', block.charAt(5, 2));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAtOutOfBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        block.charAt(6, 6);
    }
    
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "1234567890";
        scheme.lines[1] = "abcdefghij";
        scheme.lines[2] = "klmnopqrst";
        Block block = new ConcreteBlock(scheme, 0, 0, 10, 3);
        assertEquals("12", block.textAt(0, 2, 0));
        assertEquals("56", block.textAt(4, 6, 0));
        assertEquals("abcde", block.textAt(0, 5, 1));
        assertEquals("ghijk", block.textAt(5, 10, 1));
        assertEquals("klmn", block.textAt(0, 4, 2));
        assertEquals("qrst", block.textAt(5, 9, 2));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAtImproperCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        block.textAt(6, 10, 0);
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "word.";
        scheme.lines[1] = "test";
        Block block1 = new ConcreteBlock(scheme, 0, 0, 5, 1);
        assertTrue(block1.containsOnlyOneWord());
        
        scheme.lines[0] = "word";
        scheme.lines[1] = "word";
        Block block2 = new ConcreteBlock(scheme, 0, 0, 5, 1);
        assertFalse(block2.containsOnlyOneWord());
        
        scheme.lines[0] = "";
        scheme.lines[1] = "";
        Block block3 = new ConcreteBlock(scheme, 0, 0, 0, 1);
        assertFalse(block3.containsOnlyOneWord());
        
        scheme.lines[0] = "word.";
        scheme.lines[1] = "test";
        scheme.lines[2] = "word.";
        Block block4 = new ConcreteBlock(scheme, 0, 0, 5, 2);
        assertFalse(block4.containsOnlyOneWord());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordTrivialBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme);
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "word1 word2 word3";
        scheme.lines[1] = "test1 test2 test3";
        Block block = new ConcreteBlock(scheme, 0, 0, 20, 1);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(3, subblocks.size());
        
        Block subblock1 = subblocks.get(0);
        assertEquals(0, subblock1.x1);
        assertEquals(0, subblock1.y1);
        assertEquals(5, subblock1.x2);
        assertEquals(1, subblock1.y2);
        
        Block subblock2 = subblocks.get(1);
        assertEquals(6, subblock2.x1);
        assertEquals(0, subblock2.y1);
        assertEquals(11, subblock2.x2);
        assertEquals(1, subblock2.y2);
        
        Block subblock3 = subblocks.get(2);
        assertEquals(12, subblock3.x1);
        assertEquals(0, subblock3.y1);
        assertEquals(17, subblock3.x2);
        assertEquals(1, subblock3.y2);
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "word1";
        scheme.lines[1] = "word2";
        scheme.lines[2] = "word3";
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 2);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(3, subblocks.size());
        
        Block subblock1 = subblocks.get(0);
        assertEquals(0, subblock1.x1);
        assertEquals(0, subblock1.y1);
        assertEquals(5, subblock1.x2);
        assertEquals(0, subblock1.y2);
        
        Block subblock2 = subblocks.get(1);
        assertEquals(0, subblock2.x1);
        assertEquals(1, subblock2.y1);
        assertEquals(5, subblock2.x2);
        assertEquals(1, subblock2.y2);
        
        Block subblock3 = subblocks.get(2);
        assertEquals(0, subblock3.x1);
        assertEquals(2, subblock3.y1);
        assertEquals(5, subblock3.x2);
        assertEquals(2, subblock3.y2);
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "word1 word2 word3";
        scheme.lines[1] = "test1 test2 test3";
        Block block = new ConcreteBlock(scheme, 0, 0, 20, 1);
        String expected = "Block(0,0, 20,1):\n"
                        + " ####################\n"
                        + " # word1 word2 word3 #\n"
                        + " # test1 test2 test3 #\n"
                        + " ####################";
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[0] = "word1 word2 word3";
        scheme.lines[1] = "test1 test2 test3";
        Block block = new ConcreteBlock(scheme, 0, 0, 20, 1);
        assertEquals("word1 word2 word3", block.getUpperLine());
    }
}