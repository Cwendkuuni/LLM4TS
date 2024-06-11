import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import glengineer.blocks.Block;
import glengineer.positions.CharPosition;
import glengineer.positions.HWordPosition;
import glengineer.positions.Scheme;
import glengineer.positions.WordPosition;
import java.util.Arrays;
import java.util.List;

public class BlockTest {
  
  private Scheme scheme;
  private Block block;
  
  @Before
  public void setUp() {
    scheme = new Scheme();
    block = new Block(scheme, 0, 0, 5, 5);
  }
  
  @Test
  public void testBlockConstructor() {
    assertNotNull(block);
    assertEquals(0, block.x1);
    assertEquals(0, block.y1);
    assertEquals(5, block.x2);
    assertEquals(5, block.y2);
    assertEquals(scheme, block.scheme);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testBlockConstructorInvalidArgs() {
    Block invalidBlock = new Block(scheme, -1, -1, 5, 5);
  }
  
  @Test
  public void testBlockConstructorWithCharPositions() {
    CharPosition p1 = new CharPosition(0, 0);
    CharPosition p2 = new CharPosition(5, 5);
    Block block = new Block(scheme, p1, p2);
    
    assertNotNull(block);
    assertEquals(0, block.x1);
    assertEquals(0, block.y1);
    assertEquals(5, block.x2);
    assertEquals(5, block.y2);
    assertEquals(scheme, block.scheme);
  }
  
  @Test
  public void testDefaultBlockConstructor() {
    Block defaultBlock = new Block(scheme);
    
    assertNotNull(defaultBlock);
    assertEquals(0, defaultBlock.x1);
    assertEquals(0, defaultBlock.y1);
    assertEquals(scheme.x2, defaultBlock.x2);
    assertEquals(scheme.y2, defaultBlock.y2);
    assertEquals(scheme, defaultBlock.scheme);
  }
  
  @Test
  public void testSubblock() {
    Block subblock = block.subblock(1, 1, 4, 4);
    
    assertNotNull(subblock);
    assertEquals(1, subblock.x1);
    assertEquals(1, subblock.y1);
    assertEquals(4, subblock.x2);
    assertEquals(4, subblock.y2);
    assertEquals(scheme, subblock.scheme);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCharAtWithInvalidArgs() {
    block.charAt(-1, -1);
  }
  
  @Test
  public void testCharAt() {
    char ch = block.charAt(0, 0);
    
    assertEquals(scheme.lines[0].charAt(0), ch);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testTextAtWithInvalidArgs() {
    block.textAt(6, 10, 0);
  }
  
  @Test
  public void testTextAt() {
    String text = block.textAt(0, 4, 0);
    
    assertEquals(scheme.lines[0].substring(0, 4), text);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testContainsOnlyOneWordWithTrivialBlock() {
    Block trivialBlock = new Block(scheme);
    trivialBlock.containsOnlyOneWord();
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testContainsOnlyOneWordWithEmptyBlock() {
    Block emptyBlock = new Block(scheme, 0, 5, 0, 5);
    emptyBlock.containsOnlyOneWord();
  }
  
  @Test
  public void testContainsOnlyOneWord() {
    boolean result = block.containsOnlyOneWord();
    assertFalse(result);
  }
  
  @Test
  public void testToString() {
    String result = block.toString();
    
    String expected = "Block(0,0, 5,5):\n";
    int frameWidth = 5 + 4;
    char[] line = new char[frameWidth];
    Arrays.fill(line, '#');
    String lineString = " " + new String(line);
    expected = expected.concat(lineString + "\n");
    for (int y = 0; y < 5; ++y) {
      expected = expected.concat(" # " + scheme.textAt(0, 5, y) + " #\n");
    }
    expected = expected.concat(lineString);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testGetUpperLine() {
    String upperLine = block.getUpperLine();
    String expected = scheme.textAt(0, 5, 0);
    
    assertEquals(expected, upperLine);
  }
  
  @Test
  public void testExtractSequentialSubblocks() {
    List<Block> subblocks = block.extractSequentialSubblocks();
    
    assertNotNull(subblocks);
    assertTrue(subblocks.isEmpty());
  }
  
  @Test
  public void testExtractParallelSubblocks() {
    List<Block> subblocks = block.extractParallelSubblocks();
    
    assertNotNull(subblocks);
    assertTrue(subblocks.isEmpty());
  }
}