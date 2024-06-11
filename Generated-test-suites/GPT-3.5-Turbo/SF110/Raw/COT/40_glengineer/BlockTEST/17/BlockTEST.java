
import org.junit.*;
import static org.junit.Assert.*;

public class BlockTest {
  
  @Test
  public void testConstructorWithArguments() {
    Scheme scheme = new Scheme();
    Block block = new Block(scheme, 0, 0, 2, 2);
    assertEquals("Constructor with arguments: x1 = 0, y1 = 0, x2 = 2, y2 = 2", block.toString());
  }
  
  @Test
  public void testConstructorWithPositions() {
    Scheme scheme = new Scheme();
    CharPosition p1 = new CharPosition(0, 0);
    CharPosition p2 = new CharPosition(2, 2);
    Block block = new Block(scheme, p1, p2);
    assertEquals("Constructor with positions: x1 = 0, y1 = 0, x2 = 2, y2 = 2", block.toString());
  }
  
  @Test
  public void testDefaultConstructor() {
    Scheme scheme = new Scheme();
    Block block = new Block(scheme);
    assertEquals("Default constructor: x1 = 0, y1 = 0, x2 = scheme.x2, y2 = scheme.y2", block.toString());
  }
  
  @Test
  public void testSubblock() {
    Scheme scheme = new Scheme();
    Block block = new Block(scheme);
    Block subblock = block.subblock(0, 0, 2, 2);
    assertEquals("Subblock: p0 = 0, p1 = 0, p2 = 2, p3 = 2", subblock.toString());
  }
  
  @Test
  public void testCharAt() {
    Scheme scheme = new Scheme();
    Block block = new Block(scheme);
    char character = block.charAt(0, 0);
    assertEquals(' ', character);
  }
  
  @Test
  public void testTextAt() {
    Scheme scheme = new Scheme();
    Block block = new Block(scheme);
    String text = block.textAt(0, 2, 0);
    assertEquals("  ", text);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testContainsOnlyOneWord() {
    Scheme scheme = new Scheme();
    Block block = new Block(scheme);
    block.containsOnlyOneWord();
  }
  
  @Test
  public void testExtractSequentialSubblocks() {
    Scheme scheme = new Scheme();
    Block block = new Block(scheme);
    List<Block> subblocks = block.extractSequentialSubblocks();
    assertEquals(0, subblocks.size());
  }
  
  @Test
  public void testExtractParallelSubblocks() {
    Scheme scheme = new Scheme();
    Block block = new Block(scheme);
    List<Block> subblocks = block.extractParallelSubblocks();
    assertEquals(0, subblocks.size());
  }
  
  @Test
  public void testToString() {
    Scheme scheme = new Scheme();
    Block block = new Block(scheme);
    String blockString = block.toString();
    assertTrue(blockString.contains("Block(0,0, scheme.x2, scheme.y2):"));
  }
  
  @Test
  public void testGetUpperLine() {
    Scheme scheme = new Scheme();
    Block block = new Block(scheme);
    String upperLine = block.getUpperLine();
    assertEquals("  ", upperLine);
  }
  
}

