
package de.beiri22.stringincrementor;

import static org.junit.Assert.*;
import org.junit.Test;

public class StringIncrementorTest {
  
  @Test
  public void testDiff() {
    // Test case 1
    String a1 = "abc";
    String b1 = "def";
    RelativeString result1 = StringIncrementor.diff(a1, b1);
    assertEquals(result1.getLinks().size(), 0);
    assertEquals(result1.getAbsolute(), b1);
    
    // Test case 2
    String a2 = "abcd";
    String b2 = "abdd";
    RelativeString result2 = StringIncrementor.diff(a2, b2);
    assertEquals(result2.getLinks().size(), 1);
    StringLink link2 = result2.getLinks().get(0);
    assertEquals(link2.getPosOrig(), 2);
    assertEquals(link2.getPosNew(), 2);
    assertEquals(link2.getLen(), 2);
    assertEquals(result2.getAbsolute(), "d");
    
    // Test case 3
    String a3 = "abcde";
    String b3 = "abde";
    RelativeString result3 = StringIncrementor.diff(a3, b3);
    assertEquals(result3.getLinks().size(), 1);
    StringLink link3 = result3.getLinks().get(0);
    assertEquals(link3.getPosOrig(), 2);
    assertEquals(link3.getPosNew(), 2);
    assertEquals(link3.getLen(), 1);
    assertEquals(result3.getAbsolute(), "e");
  }
  
  @Test
  public void testDiffWithVerbose() {
    // Test case 1
    String a1 = "abc";
    String b1 = "def";
    RelativeString result1 = StringIncrementor.diff(a1, b1, true);
    assertEquals(result1.getLinks().size(), 0);
    assertEquals(result1.getAbsolute(), b1);
    
    // Test case 2
    String a2 = "abcd";
    String b2 = "abdd";
    RelativeString result2 = StringIncrementor.diff(a2, b2, true);
    assertEquals(result2.getLinks().size(), 1);
    StringLink link2 = result2.getLinks().get(0);
    assertEquals(link2.getPosOrig(), 2);
    assertEquals(link2.getPosNew(), 2);
    assertEquals(link2.getLen(), 2);
    assertEquals(result2.getAbsolute(), "d");
    
    // Test case 3
    String a3 = "abcde";
    String b3 = "abde";
    RelativeString result3 = StringIncrementor.diff(a3, b3, true);
    assertEquals(result3.getLinks().size(), 1);
    StringLink link3 = result3.getLinks().get(0);
    assertEquals(link3.getPosOrig(), 2);
    assertEquals(link3.getPosNew(), 2);
    assertEquals(link3.getLen(), 1);
    assertEquals(result3.getAbsolute(), "e");
  }
  
  @Test
  public void testPatch() {
    // Test case 1
    String a1 = "abc";
    RelativeString r1 = new RelativeString();
    r1.setAbsolute("def");
    String result1 = StringIncrementor.patch(a1, r1);
    assertEquals(result1, "def");
    
    // Test case 2
    String a2 = "abcd";
    RelativeString r2 = new RelativeString();
    r2.addLink(new StringLink(2, 2, 2));
    r2.setAbsolute("d");
    String result2 = StringIncrementor.patch(a2, r2);
    assertEquals(result2, "abdd");
    
    // Test case 3
    String a3 = "abcde";
    RelativeString r3 = new RelativeString();
    r3.addLink(new StringLink(2, 2, 1));
    r3.setAbsolute("e");
    String result3 = StringIncrementor.patch(a3, r3);
    assertEquals(result3, "abde");
  }

  @Test
  public void testPatchWithVerbose() {
    // Test case 1
    String a1 = "abc";
    RelativeString r1 = new RelativeString();
    r1.setAbsolute("def");
    String result1 = StringIncrementor.patch(a1, r1, true);
    assertEquals(result1, "def");
    
    // Test case 2
    String a2 = "abcd";
    RelativeString r2 = new RelativeString();
    r2.addLink(new StringLink(2, 2, 2));
    r2.setAbsolute("d");
    String result2 = StringIncrementor.patch(a2, r2, true);
    assertEquals(result2, "abdd");
    
    // Test case 3
    String a3 = "abcde";
    RelativeString r3 = new RelativeString();
    r3.addLink(new StringLink(2, 2, 1));
    r3.setAbsolute("e");
    String result3 = StringIncrementor.patch(a3, r3, true);
    assertEquals(result3, "abde");
  }
  
}
