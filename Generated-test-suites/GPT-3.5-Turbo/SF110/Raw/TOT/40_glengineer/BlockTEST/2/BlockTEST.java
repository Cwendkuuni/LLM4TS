package glengineer.blocks;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

public class BlockTest {

  @Test
  public void testBlockConstructor1() {
    Scheme scheme = new Scheme(10, 10);
    Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
    assertEquals(0, block.x1);
    assertEquals(0, block.y1);
    assertEquals(5, block.x2);
    assertEquals(5, block.y2);
    assertEquals(scheme, block.scheme);
  }

  @Test
  public void testBlockConstructor2() {
    Scheme scheme = new Scheme(10, 10);
    CharPosition p1 = new CharPosition(0, 0);
    CharPosition p2 = new CharPosition(5, 5);
    Block block = new ConcreteBlock(scheme, p1, p2);
    assertEquals(0, block.x1);
    assertEquals(0, block.y1);
    assertEquals(5, block.x2);
    assertEquals(5, block.y2);
    assertEquals(scheme, block.scheme);
  }

  @Test
  public void testBlockConstructor3() {
    Scheme scheme = new Scheme(10, 10);
    Block block = new ConcreteBlock(scheme);
    assertEquals(0, block.x1);
    assertEquals(0, block.y1);
    assertEquals(10, block.x2);
    assertEquals(10, block.y2);
    assertEquals(scheme, block.scheme);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBlockConstructorInvalid1() {
    Scheme scheme = new Scheme(10, 10);
    Block block = new ConcreteBlock(scheme, -1, 0, 5, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBlockConstructorInvalid2() {
    Scheme scheme = new Scheme(10, 10);
    Block block = new ConcreteBlock(scheme, 0, -1, 5, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBlockConstructorInvalid3() {
    Scheme scheme = new Scheme(10, 10);
    Block block = new ConcreteBlock(scheme, 0, 0, 11, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBlockConstructorInvalid4() {
    Scheme scheme = new Scheme(10, 10);
    Block block = new ConcreteBlock(scheme, 0, 0, 5, 11);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCharAtInvalid() {
    Scheme scheme = new Scheme(10, 10);
    Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
    char c = block.charAt(6, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTextAtInvalid() {
    Scheme scheme = new Scheme(10, 10);
    Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
    String text = block.textAt(0, 6, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testContainsOnlyOneWordTrivial() {
    Scheme scheme = new Scheme(10, 10);
    Block block = new ConcreteBlock(scheme, 0, 0, 1, 1);
    block.containsOnlyOneWord();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testContainsOnlyOneWordEmpty() {
    Scheme scheme = new Scheme(10, 10);
    Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
    block.containsOnlyOneWord();
  }

  @Test
  public void testExtractSequentialSubblocksEmpty() {
    Scheme scheme = new Scheme(10, 10);
    Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
    List<Block> subblocks = block.extractSequentialSubblocks();
    assertTrue(subblocks.isEmpty());
  }

  @Test
  public void testExtractParallelSubblocksEmpty() {
    Scheme scheme = new Scheme(10, 10);
    Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
    List<Block> subblocks = block.extractParallelSubblocks();
    assertTrue(subblocks.isEmpty());
  }
}

class ConcreteBlock extends Block {
  public ConcreteBlock(Scheme scheme, int x1, int y1, int x2, int y2) {
    super(scheme, x1, y1, x2, y2);
  }

  @Override
  public Block subblock(int p0, int p1, int p2, int p3) {
    return null; // Not implemented for test purposes only
  }

  public boolean isTrivial() {
    return true; // Not implemented for test purposes only
  }

  public HWordPosition firstWord_Horizontally() {
    return null; // Not implemented for test purposes only
  }

  public WordPosition lastWord_Horizontally() {
    return null; // Not implemented for test purposes only
  }

  @Override
  public List<Block> extractSequentialSubblocks() {
    return null; // Not implemented for test purposes only
  }

  @Override
  public List<Block> extractParallelSubblocks() {
    return null; // Not implemented for test purposes only
  }
}