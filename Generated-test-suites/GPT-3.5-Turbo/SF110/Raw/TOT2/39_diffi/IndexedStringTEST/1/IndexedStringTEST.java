package de.beiri22.stringincrementor.helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

  @Test
  public void testCount() {
    // Typical use case
    IndexedString indexedString = new IndexedString("hello");
    assertEquals(2, indexedString.count('l'));

    // Edge case: Empty string
    indexedString = new IndexedString("");
    assertEquals(0, indexedString.count('a'));

    // Edge case: Character not present in string
    indexedString = new IndexedString("hello");
    assertEquals(0, indexedString.count('z'));
  }

  @Test
  public void testIndexOf() {
    IndexedString indexedString = new IndexedString("hello");
    char[] target = {'l', 'o'};
    
    // Typical use case
    assertEquals(3, indexedString.indexOf(target));
    
    // Edge case: Target not found
    char[] targetNotFound = {'o', 'l'};
    assertEquals(-1, indexedString.indexOf(targetNotFound));

    // Error scenario: Target array is empty
    char[] emptyTarget = {};
    assertEquals(-1, indexedString.indexOf(emptyTarget));
  }

}