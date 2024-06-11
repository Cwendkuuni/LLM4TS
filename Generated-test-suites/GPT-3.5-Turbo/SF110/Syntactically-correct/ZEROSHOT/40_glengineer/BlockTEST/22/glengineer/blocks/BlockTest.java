import org.junit.*;
import static org.junit.Assert.*;

public class BlockTest {
  
  @Test
  public void testBlockConstructor1() {
    Scheme scheme = new Scheme(10, 10);
    Block block = new Block(scheme, 1, 1, 5, 5);
    assertNotNull(block);
  }
  
  @Test
  public void testBlockConstructor2() {
    Scheme scheme = new Scheme(10, 10);
    CharPosition p1 = new CharPosition(1, 1);
    CharPosition p2 = new CharPosition(5, 5);
    Block block = new Block(scheme, p1, p2);
    assertNotNull(block);
  }
  
  @Test
  public void testBlockConstructor3() {
    Scheme scheme = new Scheme(10, 10);
    Block block = new Block(scheme);
    assertNotNull(block);
  }
  
  @Test
  public void testSubblock() {
    Scheme scheme = new Scheme(10, 10);
    Block block = new Block(scheme, 1, 1, 5, 5);
    Block subblock = block.subblock(2, 2, 4, 4);
    assertNotNull(subblock);
  }
  
  @Test
  public void testCharAt() {
    Scheme scheme = new Scheme(10, 10);
    Block block = new Block(scheme, 1, 1, 5, 5);
    char result = block.charAt(3, 3);
    assertEquals(' ', result);
  }
  
  @Test
  public void testTextAt() {
    Scheme scheme = new Scheme(10, 10);
    Block block = new Block(scheme, 1, 1, 5, 5);
    String result = block.textAt(2, 4, 3);
    assertEquals("   ", result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testContainsOnlyOneWord() {
    Scheme scheme = new Scheme(10, 10);
    Block block = new Block(scheme, 1, 1, 5, 5);
    block.containsOnlyOneWord();
  }
  
  @Test
  public void testExtractSequentialSubblocks() {
    Scheme scheme = new Scheme(10, 10);
    Block block = new Block(scheme, 1, 1, 5, 5);
    List<Block> subblocks = block.extractSequentialSubblocks();
    assertNotNull(subblocks);
  }
  
  @Test
  public void testExtractParallelSubblocks() {
    Scheme scheme = new Scheme(10, 10);
    Block block = new Block(scheme, 1, 1, 5, 5);
    List<Block> subblocks = block.extractParallelSubblocks();
    assertNotNull(subblocks);
  }
  
  @Test
  public void testToString() {
    Scheme scheme = new Scheme(10, 10);
    Block block = new Block(scheme, 1, 1, 5, 5);
    String result = block.toString();
    assertNotNull(result);
  }
  
  @Test
  public void testGetUpperLine() {
    Scheme scheme = new Scheme(10, 10);
    Block block = new Block(scheme, 1, 1, 5, 5);
    String result = block.getUpperLine();
    assertNotNull(result);
  }
}