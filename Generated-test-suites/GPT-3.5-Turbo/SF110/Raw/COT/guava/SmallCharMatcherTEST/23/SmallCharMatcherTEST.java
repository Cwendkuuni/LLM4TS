import com.google.common.base.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class SmallCharMatcherTest {
    
    @Test
    public void testMatches_containsZero_true() {
      SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a','b','c'}, 0L, true, "");
      assertTrue(matcher.matches('\0'));
    }
    
    @Test
    public void testMatches_containsZero_false() {
      SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a','b','c'}, 0L, false, "");
      assertFalse(matcher.matches('\0'));
    }
    
    @Test
    public void testMatches_checkFilter_true() {
      SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a','b','c'}, 0x1L, true, "");
      assertTrue(matcher.matches('a'));
    }
    
    @Test
    public void testMatches_checkFilter_false() {
      SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a','b','c'}, 0L, true, "");
      assertFalse(matcher.matches('d'));
    }
    
    @Test
    public void testMatches_validChar_true() {
      SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a','b','c','d'}, 0x1L, true, "");
      assertTrue(matcher.matches('a'));
    }
    
    @Test
    public void testMatches_validChar_false() {
      SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a','b','c'}, 0x1L, true, "");
      assertFalse(matcher.matches('d'));
    }
    
    @Test
    public void testMatches_invalidChar_false() {
      SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a','b','c'}, 0x1L, true, "");
      assertFalse(matcher.matches('z'));
    }
    
    @Test
    public void testSetBits_containsZero() {
      SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a','b','c'}, 0x1L, true, "");
      BitSet table = new BitSet();
      matcher.setBits(table);
      assertTrue(table.get(0));
    }
    
    @Test
    public void testSetBits_validChars() {
      SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a','b','c'}, 0x1L, true, "");
      BitSet table = new BitSet();
      matcher.setBits(table);
      assertTrue(table.get('a'));
      assertTrue(table.get('b'));
      assertTrue(table.get('c'));
    }
    
    @Test
    public void testSetBits_invalidChars() {
      SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a','b','c'}, 0x1L, true, "");
      BitSet table = new BitSet();
      matcher.setBits(table);
      assertFalse(table.get('z'));
    }
    
    @Test
    public void testChooseTableSize_setSize1() {
      int tableSize = SmallCharMatcher.chooseTableSize(1);
      assertEquals(2, tableSize);
    }
    
    @Test
    public void testChooseTableSize_setSize2() {
      int tableSize = SmallCharMatcher.chooseTableSize(2);
      assertEquals(4, tableSize);
    }
    
    @Test
    public void testChooseTableSize_setSize3() {
      int tableSize = SmallCharMatcher.chooseTableSize(3);
      assertEquals(4, tableSize);
    }
    
    @Test
    public void testChooseTableSize_setSize4() {
      int tableSize = SmallCharMatcher.chooseTableSize(4);
      assertEquals(8, tableSize);
    }
    
    // Add more test cases for other public methods if needed
    
    // 
    
    // Insert the merged test cases here
    
    // 
}