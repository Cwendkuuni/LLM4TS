package de.beiri22.stringincrementor.helper;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {
  
  private IndexedString indexedString;
  
  @Before
  public void setUp() {
    indexedString = new IndexedString("test string");
  }
  
  @Test
  public void testCount() {
    assertEquals(1, indexedString.count('t'));
    assertEquals(2, indexedString.count('s'));
    assertEquals(1, indexedString.count('r'));
    assertEquals(0, indexedString.count('a'));
  }
  
  @Test
  public void testIndexOf() {
    assertEquals(0, indexedString.indexOf("test".toCharArray()));
    assertEquals(5, indexedString.indexOf("string".toCharArray()));
    assertEquals(-1, indexedString.indexOf("abc".toCharArray()));
  }
  
  @Test
  public void testIndexOfEmptyTarget() {
    assertEquals(-1, indexedString.indexOf("".toCharArray()));
  }
  
  @Test
  public void testIndexOfTargetLongerThanString() {
    assertEquals(-1, indexedString.indexOf("longerstringthantarget".toCharArray()));
  }
  
  @Test
  public void testIndexOfTargetWithMultipleOccurrences() {
    assertEquals(2, indexedString.indexOf("s".toCharArray()));
    assertEquals(8, indexedString.indexOf("t".toCharArray()));
  }
  
  @Test
  public void testIndexOfTargetNotPresent() {
    assertEquals(-1, indexedString.indexOf("abc".toCharArray()));
  }
  
}

