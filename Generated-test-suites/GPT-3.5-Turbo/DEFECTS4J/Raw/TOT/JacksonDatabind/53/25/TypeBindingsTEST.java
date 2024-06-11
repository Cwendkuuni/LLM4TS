import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class TypeBindingsTest {
  
  @Test
  public void create_emptyBindings_returnsEmpty() {
    TypeBindings bindings = TypeBindings.emptyBindings();
    assertTrue(bindings.isEmpty());
    assertEquals(0, bindings.size());
  }
  
  @Test
  public void create_singleTypeParameter_returnsCorrectBindings() {
    Class<?> erasedType = LinkedList.class;
    JavaType typeArg1 = TypeFactory.defaultInstance().constructType(String.class);
    
    TypeBindings bindings = TypeBindings.create(erasedType, typeArg1);
    assertFalse(bindings.isEmpty());
    assertEquals(1, bindings.size());
    assertEquals("E", bindings.getBoundName(0));
    assertEquals(typeArg1, bindings.getBoundType(0));
  }
  
  @Test
  public void create_multipleTypeParameters_returnsCorrectBindings() {
    Class<?> erasedType = HashMap.class;
    JavaType typeArg1 = TypeFactory.defaultInstance().constructType(String.class);
    JavaType typeArg2 = TypeFactory.defaultInstance().constructType(Integer.class);
    
    TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
    assertFalse(bindings.isEmpty());
    assertEquals(2, bindings.size());
    assertEquals("K", bindings.getBoundName(0));
    assertEquals(typeArg1, bindings.getBoundType(0));
    assertEquals("V", bindings.getBoundName(1));
    assertEquals(typeArg2, bindings.getBoundType(1));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void create_unmatchedTypeParameters_throwsException() {
    Class<?> erasedType = LinkedList.class;
    JavaType typeArg1 = TypeFactory.defaultInstance().constructType(Integer.class);
    JavaType typeArg2 = TypeFactory.defaultInstance().constructType(Double.class);
    
    TypeBindings.create(erasedType, typeArg1, typeArg2);
  }
  
  @Test
  public void withUnboundVariable_addsUnboundVariable() {
    Class<?> erasedType = LinkedList.class;
    JavaType typeArg1 = TypeFactory.defaultInstance().constructType(String.class);
    
    TypeBindings bindings = TypeBindings.create(erasedType, typeArg1);
    TypeBindings modifiedBindings = bindings.withUnboundVariable("T");
    
    assertFalse(modifiedBindings.isEmpty());
    assertEquals(1, modifiedBindings.size());
    assertEquals("E", modifiedBindings.getBoundName(0));
    assertEquals(typeArg1, modifiedBindings.getBoundType(0));
    assertTrue(modifiedBindings.hasUnbound("T"));
  }
  
  @Test
  public void findBoundType_withMatchingName_returnsBoundType() {
    Class<?> erasedType = LinkedList.class;
    JavaType typeArg1 = TypeFactory.defaultInstance().constructType(String.class);
    
    TypeBindings bindings = TypeBindings.create(erasedType, typeArg1);
    JavaType boundType = bindings.findBoundType("E");
    
    assertNotNull(boundType);
    assertEquals(typeArg1, boundType);
  }
  
  @Test
  public void findBoundType_withNonMatchingName_returnsNull() {
    Class<?> erasedType = LinkedList.class;
    JavaType typeArg1 = TypeFactory.defaultInstance().constructType(String.class);
    
    TypeBindings bindings = TypeBindings.create(erasedType, typeArg1);
    JavaType boundType = bindings.findBoundType("T");
    
    assertNull(boundType);
  }

}