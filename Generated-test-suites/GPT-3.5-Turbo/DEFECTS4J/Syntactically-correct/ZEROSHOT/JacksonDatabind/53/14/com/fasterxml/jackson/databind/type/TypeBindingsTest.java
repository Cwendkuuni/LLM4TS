import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;

public class TypeBindingsTest {

  @Test
  public void testEmptyBindings() {
    TypeBindings empty = TypeBindings.emptyBindings();
    Assert.assertTrue(empty.isEmpty());
    Assert.assertEquals(0, empty.size());
  }

  @Test
  public void testFindBoundType() {
    TypeBindings typeBindings = new TypeBindings(Arrays.asList("A", "B", "C"), Arrays.asList(new JavaType(), new JavaType(), new JavaType()), null);
    Assert.assertNull(typeBindings.findBoundType("D"));
    Assert.assertNotNull(typeBindings.findBoundType("A"));
    Assert.assertNotNull(typeBindings.findBoundType("B"));
    Assert.assertNotNull(typeBindings.findBoundType("C"));
  }

  @Test
  public void testSize() {
    TypeBindings typeBindings = new TypeBindings(Arrays.asList("A", "B", "C"), Arrays.asList(new JavaType(), new JavaType(), new JavaType()), null);
    Assert.assertEquals(3, typeBindings.size());
  }

  @Test
  public void testGetBoundName() {
    TypeBindings typeBindings = new TypeBindings(Arrays.asList("A", "B", "C"), Arrays.asList(new JavaType(), new JavaType(), new JavaType()), null);
    Assert.assertNull(typeBindings.getBoundName(3));
    Assert.assertNotNull(typeBindings.getBoundName(2));
    Assert.assertNotNull(typeBindings.getBoundName(1));
    Assert.assertNotNull(typeBindings.getBoundName(0));
  }

  @Test
  public void testGetBoundType() {
    TypeBindings typeBindings = new TypeBindings(Arrays.asList("A", "B", "C"), Arrays.asList(new JavaType(), new JavaType(), new JavaType()), null);
    Assert.assertNull(typeBindings.getBoundType(3));
    Assert.assertNotNull(typeBindings.getBoundType(2));
    Assert.assertNotNull(typeBindings.getBoundType(1));
    Assert.assertNotNull(typeBindings.getBoundType(0));
  }

  @Test
  public void testGetTypeParameters() {
    TypeBindings typeBindings = new TypeBindings(Arrays.asList("A", "B", "C"), Arrays.asList(new JavaType(), new JavaType(), new JavaType()), null);
    Assert.assertEquals(3, typeBindings.getTypeParameters().size());
  }

  @Test
  public void testHasUnbound() {
    TypeBindings typeBindings = new TypeBindings(Arrays.asList("A", "B", "C"), Arrays.asList(new JavaType(), new JavaType(), new JavaType()), null);
    Assert.assertFalse(typeBindings.hasUnbound("D"));
    Assert.assertFalse(typeBindings.hasUnbound("E"));
    Assert.assertFalse(typeBindings.hasUnbound("F"));
    Assert.assertFalse(typeBindings.hasUnbound("G"));
  }

  @Test
  public void testWithUnboundVariable() {
    TypeBindings typeBindings = new TypeBindings(Arrays.asList("A", "B", "C"), Arrays.asList(new JavaType(), new JavaType(), new JavaType()), null);
    TypeBindings res = typeBindings.withUnboundVariable("D");
    Assert.assertFalse(res.hasUnbound("D"));
  }

  @Test
  public void testHashCode() {
    TypeBindings typeBindings = new TypeBindings(Arrays.asList("A", "B", "C"), Arrays.asList(new JavaType(), new JavaType(), new JavaType()), null);
    Assert.assertNotNull(typeBindings.hashCode());
  }

  @Test
  public void testEquals() {
    TypeBindings typeBindings = new TypeBindings(Arrays.asList("A", "B", "C"), Arrays.asList(new JavaType(), new JavaType(), new JavaType()), null);
    Assert.assertFalse(typeBindings.equals(null));
    Assert.assertTrue(typeBindings.equals(typeBindings));
  }

  @Test
  public void testTypeParameterArray() {
    TypeBindings typeBindings = new TypeBindings(Arrays.asList("A", "B", "C"), Arrays.asList(new JavaType(), new JavaType(), new JavaType()), null);
    Assert.assertNotNull(typeBindings.typeParameterArray());
  }
}