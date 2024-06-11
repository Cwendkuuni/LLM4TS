package de.beiri22.stringincrementor.helper;

import static org.junit.Assert.*;

import org.junit.Test;

public class IndexedStringTest {
  
  @Test
  public void testCount() {
    IndexedString indexedString = new IndexedString("abca");
    
    assertEquals(2, indexedString.count('a'));
    assertEquals(1, indexedString.count('b'));
    assertEquals(1, indexedString.count('c'));
  }
  
  @Test
  public void testConstructor() {
    IndexedString indexedString = new IndexedString("abacaba");
    
    assertEquals(7, indexedString.values.length);
    
    assertEquals(2, indexedString.index['a'].length);
    assertEquals(4, indexedString.index['b'].length);
    assertEquals(1, indexedString.index['c'].length);
    assertEquals(0, indexedString.index['d'].length);
  }
  
  @Test
  public void testIndexOf() {
    IndexedString indexedString = new IndexedString("abacaba");
    
    assertEquals(0, indexedString.indexOf("ab".toCharArray()));
    assertEquals(2, indexedString.indexOf("ac".toCharArray()));
    assertEquals(4, indexedString.indexOf("ba".toCharArray()));
    assertEquals(-1, indexedString.indexOf("bd".toCharArray()));
  }
}