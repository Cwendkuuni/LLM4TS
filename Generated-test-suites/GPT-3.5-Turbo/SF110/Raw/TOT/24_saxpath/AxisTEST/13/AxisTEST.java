import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {
  
  @Test
  public void testLookup_1() {
    int expected = Axis.CHILD;
    int actual = Axis.lookup("child");
    assertEquals(expected, actual);
  }
  
  @Test
  public void testLookup_2() {
    int expected = Axis.DESCENDANT;
    int actual = Axis.lookup("descendant");
    assertEquals(expected, actual);
  }
  
  @Test
  public void testLookup_3() {
    int expected = Axis.PARENT;
    int actual = Axis.lookup("parent");
    assertEquals(expected, actual);
  }
  
  @Test
  public void testLookup_4() {
    int expected = Axis.ANCESTOR;
    int actual = Axis.lookup("ancestor");
    assertEquals(expected, actual);
  }
  
  @Test
  public void testLookup_5() {
    int expected = Axis.FOLLOWING_SIBLING;
    int actual = Axis.lookup("following-sibling");
    assertEquals(expected, actual);
  }
  
  @Test
  public void testLookup_6() {
    int expected = Axis.PRECEDING_SIBLING;
    int actual = Axis.lookup("preceding-sibling");
    assertEquals(expected, actual);
  }
  
  @Test
  public void testLookup_7() {
    int expected = Axis.FOLLOWING;
    int actual = Axis.lookup("following");
    assertEquals(expected, actual);
  }
  
  @Test
  public void testLookup_8() {
    int expected = Axis.PRECEDING;
    int actual = Axis.lookup("preceding");
    assertEquals(expected, actual);
  }
  
  @Test
  public void testLookup_9() {
    int expected = Axis.ATTRIBUTE;
    int actual = Axis.lookup("attribute");
    assertEquals(expected, actual);
  }
  
  @Test
  public void testLookup_10() {
    int expected = Axis.NAMESPACE;
    int actual = Axis.lookup("namespace");
    assertEquals(expected, actual);
  }
  
  @Test
  public void testLookup_11() {
    int expected = Axis.SELF;
    int actual = Axis.lookup("self");
    assertEquals(expected, actual);
  }
  
  @Test
  public void testLookup_12() {
    int expected = Axis.DESCENDANT_OR_SELF;
    int actual = Axis.lookup("descendant-or-self");
    assertEquals(expected, actual);
  }
  
  @Test
  public void testLookup_13() {
    int expected = Axis.ANCESTOR_OR_SELF;
    int actual = Axis.lookup("ancestor-or-self");
    assertEquals(expected, actual);
  }
  
  @Test
  public void testLookup_InvalidAxisName() {
    int expected = Axis.INVALID_AXIS;
    int actual = Axis.lookup("invalidAxisName");
    assertEquals(expected, actual);
  }
  
  @Test
  public void testLookup_NullAxisName() {
    int expected = Axis.INVALID_AXIS;
    int actual = Axis.lookup(null);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testLookup_EmptyAxisName() {
    int expected = Axis.INVALID_AXIS;
    int actual = Axis.lookup("");
    assertEquals(expected, actual);
  }
  
  @Test
  public void testLookup_PassInvalidAxisNumber() {
    String expected = null;
    String actual = Axis.lookup(14);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testLookup_PassZeroAxisNumber() {
    String expected = null;
    String actual = Axis.lookup(0);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testLookup_PassNegativeAxisNumber() {
    String expected = null;
    String actual = Axis.lookup(-1);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testLookup_PassValidAxisNumber() {
    String expected = "child";
    String actual = Axis.lookup(1);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testLookup_PassValidAxisNumberOutOfBound() {
    String expected = null;
    String actual = Axis.lookup(14);
    assertEquals(expected, actual);
  }
  
}