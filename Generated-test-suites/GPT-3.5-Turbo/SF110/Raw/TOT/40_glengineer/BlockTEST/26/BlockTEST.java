import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import glengineer.blocks.*;

public class BlockTest {
  
  private Scheme scheme;
  private Block block;
  
  @Before
  public void setUp() {
    // Initialize Scheme and Block for testing
    scheme = new Scheme(5, 5);
    block = new ConcreteBlock(scheme, 1, 1, 4, 4);
  }
  
  @Test
  public void testBlockConstructor_WithValidCoordinates_ShouldCreateBlock() {
    assertEquals(1, block.x1);
    assertEquals(1, block.y1);
    assertEquals(4, block.x2);
    assertEquals(4, block.y2);
    assertEquals(scheme, block.scheme);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testBlockConstructor_WithInvalidCoordinates_ShouldThrowException() {
    new ConcreteBlock(scheme, 5, 5, 1, 1);
  }
  
  @Test
  public void testBlockConstructor_WithCharPositions_ShouldCreateBlock() {
    CharPosition p1 = new CharPosition(1, 1);
    CharPosition p2 = new CharPosition(4, 4);
    block = new ConcreteBlock(scheme, p1, p2);
    
    assertEquals(1, block.x1);
    assertEquals(1, block.y1);
    assertEquals(4, block.x2);
    assertEquals(4, block.y2);
    assertEquals(scheme, block.scheme);
  }
  
  @Test
  public void testBlockConstructor_WithoutCoordinates_ShouldCreateBlock() {
    block = new ConcreteBlock(scheme);
    
    assertEquals(0, block.x1);
    assertEquals(0, block.y1);
    assertEquals(5, block.x2);
    assertEquals(5, block.y2);
    assertEquals(scheme, block.scheme);
  }
  
  @Test
  public void testSubblock_WithValidCoordinates_ShouldReturnSubblock() {
    Block subblock = block.subblock(2, 2, 3, 3);
    
    assertEquals(2, subblock.x1);
    assertEquals(2, subblock.y1);
    assertEquals(3, subblock.x2);
    assertEquals(3, subblock.y2);
    assertEquals(scheme, subblock.scheme);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSubblock_WithInvalidCoordinates_ShouldThrowException() {
    block.subblock(5, 5, 6, 6);
  }
  
  @Test
  public void testCharAt_WithValidPosition_ShouldReturnCharacter() {
    char result = block.charAt(2, 2);
    assertEquals(' ', result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCharAt_WithInvalidPosition_ShouldThrowException() {
    block.charAt(6, 6);
  }
  
  @Test
  public void testTextAt_WithValidRange_ShouldReturnSubstring() {
    String result = block.textAt(1, 4, 2);
    assertEquals("   ", result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testTextAt_WithInvalidRange_ShouldThrowException() {
    block.textAt(1, 6, 2);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testContainsOnlyOneWord_WithTrivialBlock_ShouldThrowException() {
    block = new ConcreteTrivialBlock(scheme);
    block.containsOnlyOneWord();
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testContainsOnlyOneWord_WithEmptyBlock_ShouldThrowException() {
    block = new ConcreteEmptyBlock(scheme);
    block.containsOnlyOneWord();
  }
  
  @Test
  public void testContainsOnlyOneWord_WithNonEmptyBlock_ShouldReturnTrue() {
    assertTrue(block.containsOnlyOneWord());
  }
  
  @Test
  public void testExtractSequentialSubblocks_ShouldReturnListOfSubblocks() {
    // Assuming that extractSequentialSubblocks() returns a list of ConcreteBlock objects
    
    // Testing with a block containing only one subblock
    block = new ConcreteBlock(scheme, 1, 1, 3, 3);
    List<Block> subblocks = block.extractSequentialSubblocks();
    assertEquals(1, subblocks.size());
    assertEquals(1, subblocks.get(0).x1);
    assertEquals(1, subblocks.get(0).y1);
    assertEquals(3, subblocks.get(0).x2);
    assertEquals(3, subblocks.get(0).y2);
    
    // Testing with a block containing multiple subblocks
    block = new ConcreteBlock(scheme);
    subblocks = block.extractSequentialSubblocks();
    assertEquals(1, subblocks.size());
    assertEquals(0, subblocks.get(0).x1);
    assertEquals(0, subblocks.get(0).y1);
    assertEquals(5, subblocks.get(0).x2);
    assertEquals(5, subblocks.get(0).y2);
  }
  
  @Test
  public void testExtractParallelSubblocks_ShouldReturnListOfSubblocks() {
    // Assuming that extractParallelSubblocks() returns a list of ConcreteBlock objects
    
    // Testing with a block containing only one subblock
    block = new ConcreteBlock(scheme, 1, 1, 3, 3);
    List<Block> subblocks = block.extractParallelSubblocks();
    assertEquals(1, subblocks.size());
    assertEquals(1, subblocks.get(0).x1);
    assertEquals(1, subblocks.get(0).y1);
    assertEquals(3, subblocks.get(0).x2);
    assertEquals(3, subblocks.get(0).y2);
    
    // Testing with a block containing multiple subblocks
    block = new ConcreteBlock(scheme);
    subblocks = block.extractParallelSubblocks();
    assertEquals(1, subblocks.size());
    assertEquals(0, subblocks.get(0).x1);
    assertEquals(0, subblocks.get(0).y1);
    assertEquals(5, subblocks.get(0).x2);
    assertEquals(5, subblocks.get(0).y2);
  }
  
  @Test
  public void testToString_ShouldReturnStringRepresentationOfBlock() {
    String expected = "Block(1,1, 4,4):\n" +
                      " #### \n" +
                      " #    \n" +
                      " #    \n" +
                      " #    \n" +
                      " ####";
    String result = block.toString();
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testGetUpperLine_ShouldReturnUpperLineOfBlock() {
    String result = block.getUpperLine();
    assertEquals("    ", result);
  }
  
}