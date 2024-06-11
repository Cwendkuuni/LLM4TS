import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.type.*;

public class SimpleTypeTest {

  @Test
  public void testConstructUnsafe() {
    SimpleType type = SimpleType.constructUnsafe(String.class);
    assertEquals(SimpleType.class, type.getClass());
    assertEquals(String.class, type.getRawClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithMap() {
    SimpleType.construct(Map.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithCollection() {
    SimpleType.construct(Collection.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructWithArray() {
    SimpleType.construct(Object[].class);
  }

  // Custom test case for SimpleType._narrow()
  
  @Test
  public void testNarrowSubclass() {
    SimpleType base = SimpleType.constructUnsafe(CharSequence.class);
    SimpleType narrow = (SimpleType) base._narrow(String.class);
    assertEquals(SimpleType.class, narrow.getClass());
    assertEquals(String.class, narrow.getRawClass());
  }

  @Test
  public void testNarrowSameClass() {
    SimpleType base = SimpleType.constructUnsafe(CharSequence.class);
    SimpleType narrow = (SimpleType) base._narrow(CharSequence.class);
    assertEquals(base, narrow);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNarrowInvalidSubclass() {
    SimpleType base = SimpleType.constructUnsafe(CharSequence.class);
    base._narrow(Number.class);
  }

  // Custom test case for SimpleType.getErasedSignature()
  
  @Test
  public void testGetErasedSignature() {
    SimpleType type = SimpleType.constructUnsafe(String.class);
    StringBuilder sb = new StringBuilder();
    type.getErasedSignature(sb);
    assertEquals("Ljava/lang/String;", sb.toString());
  }

  // Custom test case for SimpleType.getGenericSignature()
  
  @Test
  public void testGetGenericSignature() {
    SimpleType type = SimpleType.constructUnsafe(List.class);
    StringBuilder sb = new StringBuilder();
    type.getGenericSignature(sb);
    assertEquals("Ljava/util/List<T>;", sb.toString());
  }

  // Custom test case for SimpleType.isContainerType()
  
  @Test
  public void testIsContainerType() {
    SimpleType type = SimpleType.constructUnsafe(String.class);
    assertFalse(type.isContainerType());
  }

  // Custom test case for SimpleType.toString()
  
  @Test
  public void testToString() {
    SimpleType type = SimpleType.constructUnsafe(Integer.class);
    assertEquals("[simple type, class java.lang.Integer]", type.toString());
  }

  // Custom test case for SimpleType.equals()
  
  @Test
  public void testEqualsSameClass() {
    SimpleType type1 = SimpleType.constructUnsafe(String.class);
    SimpleType type2 = SimpleType.constructUnsafe(String.class);
    assertTrue(type1.equals(type2));
  }

  @Test
  public void testEqualsDifferentClass() {
    SimpleType type1 = SimpleType.constructUnsafe(String.class);
    SimpleType type2 = SimpleType.constructUnsafe(Integer.class);
    assertFalse(type1.equals(type2));
  }

}