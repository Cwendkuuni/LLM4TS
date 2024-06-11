import org.junit.*;
import static org.junit.Assert.*;

public class TypeBindingsTest {
  
  @Test
  public void testCreateWithNoTypes() {
    TypeBindings tb = TypeBindings.create(Class.class, null);
    assertEquals(0, tb.size());
  }
  
  @Test
  public void testCreateWithOneType() {
    JavaType type = new JavaType();
    TypeBindings tb = TypeBindings.create(Class.class, type);
    assertEquals(1, tb.size());
    assertEquals(type, tb.getBoundType(0));
  }
  
  @Test
  public void testCreateWithTwoTypes() {
    JavaType type1 = new JavaType();
    JavaType type2 = new JavaType();
    TypeBindings tb = TypeBindings.create(Class.class, type1, type2);
    assertEquals(2, tb.size());
    assertEquals(type1, tb.getBoundType(0));
    assertEquals(type2, tb.getBoundType(1));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithInvalidNumberOfTypeParameters() {
    JavaType type = new JavaType();
    TypeBindings.create(Class.class, type, type, type);
  }
  
  @Test
  public void testFindBoundType() {
    JavaType type = new JavaType();
    TypeBindings tb = TypeBindings.create(Class.class, type);
    assertEquals(type, tb.findBoundType("T"));
  }
  
  @Test
  public void testFindBoundTypeWithRecursiveType() {
    JavaType type = new JavaType();
    TypeBindings tb = TypeBindings.create(Class.class, type);
    JavaType recursiveType = new JavaType();
    ResolvedRecursiveType rrt = new ResolvedRecursiveType(recursiveType);
    tb = tb.withUnboundVariable("T");
    assertEquals(recursiveType, tb.findBoundType("T"));
  }
  
  @Test
  public void testFindBoundTypeWithUnboundVariable() {
    JavaType type = new JavaType();
    TypeBindings tb = TypeBindings.create(Class.class, type);
    tb = tb.withUnboundVariable("T");
    assertNull(tb.findBoundType("T"));
    assertTrue(tb.hasUnbound("T"));
  }
}