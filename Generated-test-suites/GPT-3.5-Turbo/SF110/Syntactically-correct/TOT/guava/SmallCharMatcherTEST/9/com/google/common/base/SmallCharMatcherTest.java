import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

  private SmallCharMatcher smallCharMatcher;

  // Test case for the constructor
  @Before
  public void setUp() {
    char[] table = new char[3];
    long filter = 1L;
    boolean containsZero = true;
    String description = "Test Description";
    smallCharMatcher = new SmallCharMatcher(table, filter, containsZero, description);
  }

  // Test case for smear method
  @Test
  public void testSmear() {
    int result = SmallCharMatcher.smear(10);
    assertEquals(123456, result);
  }

  // Test case for checkFilter method
  @Test
  public void testCheckFilter() {
    boolean result = smallCharMatcher.checkFilter(2);
    assertFalse(result);
  }

  // Test case for chooseTableSize method with setSize = 1
  @Test
  public void testChooseTableSize_withSetSize1() {
    int result = SmallCharMatcher.chooseTableSize(1);
    assertEquals(2, result);
  }

  // Test case for chooseTableSize method with setSize > 1
  @Test
  public void testChooseTableSize_withSetSizeGreaterThan1() {
    int result = SmallCharMatcher.chooseTableSize(5);
    assertEquals(8, result);
  }

  // Test case for from method
  @Test
  public void testFrom() {
    BitSet chars = new BitSet(3);
    chars.set(0);
    chars.set(1);
    chars.set(2);
    String description = "Test Description";
    CharMatcher result = SmallCharMatcher.from(chars, description);
    assertNotNull(result);
  }

  // Test case for matches method when c is not contained in the filter
  @Test
  public void testMatches_whenCNotContainedInFilter() {
    boolean result = smallCharMatcher.matches('a');
    assertFalse(result);
  }

  // Test case for matches method when c is contained in the filter but not in the table
  @Test
  public void testMatches_whenCContainedInFilterButNotInTable() {
    boolean result = smallCharMatcher.matches('b');
    assertFalse(result);
  }

  // Test case for matches method when c is contained in the table
  @Test
  public void testMatches_whenCContainedInTable() {
    boolean result = smallCharMatcher.matches('\0');
    assertTrue(result);
  }

  // Test case for matches method when c is contained in the table but not at the starting index
  @Test
  public void testMatches_whenCContainedInTableButNotAtStartingIndex() {
    smallCharMatcher = new SmallCharMatcher(new char[]{'a', 'b', '\0'}, 1L, true, "Test Description");
    boolean result = smallCharMatcher.matches('b');
    assertTrue(result);
  }

  // Test case for matches method when c is not contained in the table
  @Test
  public void testMatches_whenCNotContainedInTable() {
    smallCharMatcher = new SmallCharMatcher(new char[]{'a', 'b', '\0'}, 1L, true, "Test Description");
    boolean result = smallCharMatcher.matches('c');
    assertFalse(result);
  }

  // Test case for setBits method when containsZero is true
  @Test
  public void testSetBits_whenContainsZeroTrue() {
    smallCharMatcher = new SmallCharMatcher(new char[]{'a', 'b', '\0'}, 1L, true, "Test Description");
    BitSet table = new BitSet();
    smallCharMatcher.setBits(table);
    assertTrue(table.get(0));
  }

  // Test case for setBits method when containsZero is false
  @Test
  public void testSetBits_whenContainsZeroFalse() {
    smallCharMatcher = new SmallCharMatcher(new char[]{'a', 'b', '\0'}, 1L, false, "Test Description");
    BitSet table = new BitSet();
    smallCharMatcher.setBits(table);
    assertFalse(table.get(0));
  }

  // Test case for setBits method when table contains characters other than '\0'
  @Test
  public void testSetBits_whenTableContainsCharacters() {
    smallCharMatcher = new SmallCharMatcher(new char[]{'a', 'b', '\0'}, 1L, true, "Test Description");
    BitSet table = new BitSet();
    smallCharMatcher.setBits(table);
    assertTrue(table.get('a'));
    assertTrue(table.get('b'));
    assertFalse(table.get('c'));
  }
}