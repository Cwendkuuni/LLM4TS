import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {
  
  @Test
  public void testEqual(){
    assertTrue(Objects.equal(null, null));
    assertFalse(Objects.equal(null, "abc"));
    assertFalse(Objects.equal("abc", null));
    assertTrue(Objects.equal("abc", "abc"));
    assertFalse(Objects.equal("abc", "xyz"));
    assertTrue(Objects.equal(123, 123));
    assertFalse(Objects.equal(123, 456));
    assertTrue(Objects.equal(1.23, 1.23));
    assertFalse(Objects.equal(1.23, 4.56));
    assertTrue(Objects.equal(true, true));
    assertFalse(Objects.equal(true, false));
    assertTrue(Objects.equal('a', 'a'));
    assertFalse(Objects.equal('a', 'b'));
  }
  
  @Test
  public void testHashCode(){
    assertEquals(0, Objects.hashCode());
    assertEquals(123, Objects.hashCode(123));
    assertEquals(123456, Objects.hashCode(123, 456));
    assertEquals(1.23f, Objects.hashCode(1.23f), 0);
    assertEquals(1.23f + 4.56f, Objects.hashCode(1.23f, 4.56f), 0);
    assertEquals(true, Objects.hashCode(true));
    assertEquals(false, Objects.hashCode(true, false));
    assertEquals('a', Objects.hashCode('a'));
    assertEquals('a' + 'b', Objects.hashCode('a', 'b'));
  }
  
  @Test
  public void testToStringHelper(){
    assertEquals("Objects{}", Objects.toStringHelper(new Objects()).toString());
    assertEquals("Objects{}", Objects.toStringHelper(Objects.class).toString());
    assertEquals("Objects{}", Objects.toStringHelper("Objects").toString());
    assertEquals("Objects{a=null, b=xyz}", Objects.toStringHelper("Objects").add("a", null).add("b", "xyz").toString());
    assertEquals("Objects{value}", Objects.toStringHelper("Objects").addValue("value").toString());
  }
  
  @Test
  public void testFirstNonNull(){
    assertNull(Objects.firstNonNull(null, null));
    assertEquals("abc", Objects.firstNonNull(null, "abc"));
    assertEquals("xyz", Objects.firstNonNull("xyz", null));
    assertEquals("def", Objects.firstNonNull("def", "abc"));
    assertEquals(123, Objects.firstNonNull(null, 123));
    assertEquals(456, Objects.firstNonNull(456, null));
    assertEquals(789, Objects.firstNonNull(789, 123));
    assertEquals(true, Objects.firstNonNull(null, true));
    assertEquals(false, Objects.firstNonNull(false, null));
    assertEquals(true, Objects.firstNonNull(true, false));
    assertEquals('a', Objects.firstNonNull(null, 'a'));
    assertEquals('b', Objects.firstNonNull('b', null));
    assertEquals('c', Objects.firstNonNull('c', 'd'));
  }

}