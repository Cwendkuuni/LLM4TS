import static org.junit.Assert.*;
import org.junit.Test;

public class BlockTest {

  @Test
  public void testConstructorWithCoordinates() {
    Scheme scheme = new Scheme(5, 5);
    Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
    assertNotNull(block);
  }

  @Test
  public void testConstructorWithCharPositions() {
    Scheme scheme = new Scheme(5, 5);
    CharPosition p1 = new CharPosition(1, 1);
    CharPosition p2 = new CharPosition(3, 3);
    Block block = new ConcreteBlock(scheme, p1, p2);
    assertNotNull(block);
  }

  @Test
  public void testConstructorWithScheme() {
    Scheme scheme = new Scheme(5, 5);
    Block block = new ConcreteBlock(scheme);
    assertNotNull(block);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithInvalidCoordinates() {
    Scheme scheme = new Scheme(5, 5);
    Block block = new ConcreteBlock(scheme, 6, 6, 3, 3);
  }

  @Test
  public void testSubblock() {
    Scheme scheme = new Scheme(5, 5);
    Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
    Block subblock = block.subblock(0, 1, 2, 2);
    assertNotNull(subblock);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCharAtWithInvalidPosition() {
    Scheme scheme = new Scheme(5, 5);
    Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
    char c = block.charAt(5, 5);
  }

  @Test
  public void testTextAt() {
    Scheme scheme = new Scheme(5, 5);
    Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
    String text = block.textAt(0, 2, 2);
    assertEquals("tes", text);
  }

  @Test
  public void testContainsOnlyOneWord() {
    Scheme scheme = new Scheme(5, 5);
    Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
    boolean containsOneWord = block.containsOnlyOneWord();
    assertFalse(containsOneWord);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testContainsOnlyOneWordWithTrivialBlock() {
    Scheme scheme = new Scheme(1, 1);
    Block block = new ConcreteBlock(scheme);
    boolean containsOneWord = block.containsOnlyOneWord();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testContainsOnlyOneWordWithEmptyBlock() {
    Scheme scheme = new Scheme(5, 5);
    Block block = new ConcreteBlock(scheme, 1, 4, 3, 4);
    boolean containsOneWord = block.containsOnlyOneWord();
  }

  @Test
  public void testExtractSequentialSubblocks() {
    Scheme scheme = new Scheme(5, 5);
    Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
    List<Block> sequentialSubblocks = block.extractSequentialSubblocks();
    assertNotNull(sequentialSubblocks);
  }

  @Test
  public void testExtractParallelSubblocks() {
    Scheme scheme = new Scheme(5, 5);
    Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
    List<Block> parallelSubblocks = block.extractParallelSubblocks();
    assertNotNull(parallelSubblocks);
  }

  @Test
  public void testToString() {
    Scheme scheme = new Scheme(5, 5);
    Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
    String blockString = block.toString();
    assertNotNull(blockString);
  }

  @Test
  public void testGetUpperLine() {
    Scheme scheme = new Scheme(5, 5);
    Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
    String upperLine = block.getUpperLine();
    assertNotNull(upperLine);
  }

}