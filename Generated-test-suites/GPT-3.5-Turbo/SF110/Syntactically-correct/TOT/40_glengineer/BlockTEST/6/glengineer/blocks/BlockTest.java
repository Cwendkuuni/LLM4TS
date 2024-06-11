import static org.junit.Assert.*;
import org.junit.*;
import glengineer.blocks.*;

public class BlockTest {

    @Test
    public void testBlock_Constructor_ShouldThrowIllegalArgumentException_WhenX1IsNegative() {
        Scheme scheme = new Scheme(5, 5);
        
        try {
            Block block = new ConcreteBlock(scheme, -1, 0, 2, 2);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: -1,0, 2,2.", e.getMessage());
        }
    }
    
    @Test
    public void testBlock_Constructor_ShouldThrowIllegalArgumentException_WhenY1IsNegative() {
        Scheme scheme = new Scheme(5, 5);
        
        try {
            Block block = new ConcreteBlock(scheme, 0, -1, 2, 2);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 0,-1, 2,2.", e.getMessage());
        }
    }
    
    @Test
    public void testBlock_Constructor_ShouldThrowIllegalArgumentException_WhenX1IsGreaterThanX2() {
        Scheme scheme = new Scheme(5, 5);
        
        try {
            Block block = new ConcreteBlock(scheme, 3, 0, 2, 2);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 3,0, 2,2.", e.getMessage());
        }
    }
    
    @Test
    public void testBlock_Constructor_ShouldThrowIllegalArgumentException_WhenY1IsGreaterThanY2() {
        Scheme scheme = new Scheme(5, 5);
        
        try {
            Block block = new ConcreteBlock(scheme, 0, 3, 2, 2);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 0,3, 2,2.", e.getMessage());
        }
    }
    
    @Test
    public void testBlock_Constructor_ShouldThrowIllegalArgumentException_WhenX2IsGreaterThanSchemeX2() {
        Scheme scheme = new Scheme(5, 5);
        
        try {
            Block block = new ConcreteBlock(scheme, 0, 0, 6, 2);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 0,0, 6,2.", e.getMessage());
        }
    }
    
    @Test
    public void testBlock_Constructor_ShouldThrowIllegalArgumentException_WhenY2IsGreaterThanSchemeY2() {
        Scheme scheme = new Scheme(5, 5);
        
        try {
            Block block = new ConcreteBlock(scheme, 0, 0, 2, 6);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 0,0, 2,6.", e.getMessage());
        }
    }
    
    @Test
    public void testBlock_CharAt_ShouldThrowIllegalArgumentException_WhenXIsOutOfRange() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        
        try {
            char c = block.charAt(3, 1);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("char position (3, 1) out of block.\n" + block.toString(), e.getMessage());
        }
    }
    
    @Test
    public void testBlock_CharAt_ShouldThrowIllegalArgumentException_WhenYIsOutOfRange() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        
        try {
            char c = block.charAt(1, 3);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("char position (1, 3) out of block.\n" + block.toString(), e.getMessage());
        }
    }
    
    @Test
    public void testBlock_TextAt_ShouldThrowIllegalArgumentException_WhenX1IsOutOfRange() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 0, 0, 3, 2);
        
        try {
            String text = block.textAt(4, 6, 1);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coodinates.", e.getMessage());
        }
    }
    
    @Test
    public void testBlock_TextAt_ShouldThrowIllegalArgumentException_WhenX2IsOutOfRange() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        
        try {
            String text = block.textAt(0, 4, 1);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coodinates.", e.getMessage());
        }
    }
    
    @Test
    public void testBlock_TextAt_ShouldThrowIllegalArgumentException_WhenYIsOutOfRange() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 3);
        
        try {
            String text = block.textAt(0, 2, 4);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coodinates.", e.getMessage());
        }
    }
    
    @Test
    public void testBlock_ContainsOnlyOneWord_ShouldThrowIllegalArgumentException_WhenBlockIsTrivial() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme);
        
        try {
            boolean containsOnlyOneWord = block.containsOnlyOneWord();
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("cannot search in a trivial block", e.getMessage());
        }
    }
    
    @Test
    public void testBlock_ContainsOnlyOneWord_ShouldThrowIllegalArgumentException_WhenFirstWordIsAtBottom() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        
        try {
            boolean containsOnlyOneWord = block.containsOnlyOneWord();
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("cannot verify unicity of an word in an empty block", e.getMessage());
        }
    }
    
    @Test
    public void testBlock_ExtractSequentialSubblocks_ShouldReturnSubblocksInSequentialOrder() {
        Scheme scheme = new Scheme(6, 6);
        Block block = new ConcreteBlock(scheme, 0, 0, 6, 6);
        
        List<Block> subblocks = block.extractSequentialSubblocks();
        
        assertEquals(36, subblocks.size());
        
        // Check the order of subblocks
        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 6; j++) {
                Block subblock = subblocks.get((i-1)*6 + (j-1));
                assertEquals(i, subblock.x1);
                assertEquals(j, subblock.y1);
                assertEquals(6, subblock.x2);
                assertEquals(6, subblock.y2);
            }
        }
    }
    
    @Test
    public void testBlock_ExtractParallelSubblocks_ShouldReturnSubblocksInParallelOrder() {
        Scheme scheme = new Scheme(6, 6);
        Block block = new ConcreteBlock(scheme, 0, 0, 6, 6);
        
        List<Block> subblocks = block.extractParallelSubblocks();
        
        assertEquals(36, subblocks.size());
        
        // Check the order of subblocks
        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 6; j++) {
                Block subblock = subblocks.get((j-1)*6 + (i-1));
                assertEquals(i, subblock.x1);
                assertEquals(j, subblock.y1);
                assertEquals(6, subblock.x2);
                assertEquals(6, subblock.y2);
            }
        }
    }
    
}