package de.beiri22.stringincrementor.helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {
  
  @Test
  public void testCount() {
    IndexedString indexedString = new IndexedString("abcabcabc");
    assertEquals(3, indexedString.count('a'));
    assertEquals(3, indexedString.count('b'));
    assertEquals(3, indexedString.count('c'));
  }
  
  @Test
  public void testIndexOf() {
    IndexedString indexedString = new IndexedString("abcabcabc");
    assertEquals(0, indexedString.indexOf(new char[] {'a', 'b', 'c'}));
    assertEquals(3, indexedString.indexOf(new char[] {'a', 'b', 'c'}, 1));
    assertEquals(6, indexedString.indexOf(new char[] {'a', 'b', 'c'}, 2));
    assertEquals(-1, indexedString.indexOf(new char[] {'b', 'a', 'c'}));
  }
  
  @Test
  public void testIndexOf_EmptyString() {
    IndexedString indexedString = new IndexedString("");
    assertEquals(-1, indexedString.indexOf(new char[] {'a', 'b', 'c'}));
  }
  
  @Test
  public void testIndexOf_FirstCharNotPresent() {
    IndexedString indexedString = new IndexedString("abcabcabc");
    assertEquals(-1, indexedString.indexOf(new char[] {'x', 'b', 'c'}));
  }
  
  @Test
  public void testIndexOf_IncompleteMatch() {
    IndexedString indexedString = new IndexedString("abcabcabc");
    assertEquals(-1, indexedString.indexOf(new char[] {'a', 'b'}));
    assertEquals(-1, indexedString.indexOf(new char[] {'a', 'b', 'c', 'd'}));
  }
  
}