import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {
  
  @Test
  public void testConstruct() {
    Class<?> rawType = Collection.class;
    TypeBindings bindings = TypeBindings.emptyBindings();
    JavaType superClass = null;
    JavaType[] superInts = null;
    JavaType elemT = JavaType.STRING;
    CollectionLikeType type = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
    assertNotNull(type);
    assertEquals(rawType, type.getRawClass());
    assertEquals(bindings, type.getBindings());
    assertEquals(superClass, type.getSuperClass());
    assertEquals(superInts, type.getInterfaces());
    assertEquals(elemT, type.getContentType());
  }
  
  @Test
  public void testConstructWithInvalidRawType() {
    Class<?> rawType = null;
    TypeBindings bindings = TypeBindings.emptyBindings();
    JavaType superClass = null;
    JavaType[] superInts = null;
    JavaType elemT = JavaType.STRING;
    try {
      CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
      fail("Should throw IllegalArgumentException for invalid raw type");
    } catch (IllegalArgumentException e) {
      assertEquals("Raw type can not be null", e.getMessage());
    }
  }
  
  @Test
  public void testConstructWithInvalidElemT() {
    Class<?> rawType = Collection.class;
    TypeBindings bindings = TypeBindings.emptyBindings();
    JavaType superClass = null;
    JavaType[] superInts = null;
    JavaType elemT = null;
    try {
      CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
      fail("Should throw IllegalArgumentException for invalid elemT");
    } catch (IllegalArgumentException e) {
      assertEquals("Element type can not be null", e.getMessage());
    }
  }
  
  @Test
  public void testUpgradeFrom() {
    JavaType baseType = JavaType.STRING;
    JavaType elementType = JavaType.INTEGER;
    CollectionLikeType type = CollectionLikeType.upgradeFrom(baseType, elementType);
    assertNotNull(type);
    assertEquals(baseType, type.getJavaType());
    assertEquals(elementType, type.getContentType());
  }
  
  @Test
  public void testUpgradeFromWithInvalidBaseType() {
    JavaType baseType = null;
    JavaType elementType = JavaType.INTEGER;
    try {
      CollectionLikeType.upgradeFrom(baseType, elementType);
      fail("Should throw IllegalArgumentException for invalid baseType");
    } catch (IllegalArgumentException e) {
      assertEquals("Can not upgrade from an instance of null", e.getMessage());
    }
  }

  @Test
  public void testIsTrueCollectionType() {
    Class<?> rawType = Collection.class;
    TypeBindings bindings = TypeBindings.emptyBindings();
    JavaType superClass = null;
    JavaType[] superInts = null;
    JavaType elemT = JavaType.STRING;
    CollectionLikeType type = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
    assertTrue(type.isTrueCollectionType());
  }
  
  @Test
  public void testIsTrueCollectionTypeWithNonCollectionClass() {
    Class<?> rawType = String.class;
    TypeBindings bindings = TypeBindings.emptyBindings();
    JavaType superClass = null;
    JavaType[] superInts = null;
    JavaType elemT = JavaType.STRING;
    CollectionLikeType type = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
    assertFalse(type.isTrueCollectionType());
  }

  @Test
  public void testEquals() {
    Class<?> rawType1 = Collection.class;
    TypeBindings bindings1 = TypeBindings.emptyBindings();
    JavaType superClass1 = null;
    JavaType[] superInts1 = null;
    JavaType elemT1 = JavaType.STRING;
    CollectionLikeType type1 = CollectionLikeType.construct(rawType1, bindings1, superClass1, superInts1, elemT1);
    
    Class<?> rawType2 = Collection.class;
    TypeBindings bindings2 = TypeBindings.emptyBindings();
    JavaType superClass2 = null;
    JavaType[] superInts2 = null;
    JavaType elemT2 = JavaType.STRING;
    CollectionLikeType type2 = CollectionLikeType.construct(rawType2, bindings2, superClass2, superInts2, elemT2);
    
    assertTrue(type1.equals(type2));
  }
  
  @Test
  public void testToString() {
    Class<?> rawType = Collection.class;
    TypeBindings bindings = TypeBindings.emptyBindings();
    JavaType superClass = null;
    JavaType[] superInts = null;
    JavaType elemT = JavaType.STRING;
    CollectionLikeType type = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
    assertEquals("[collection-like type; class java.util.Collection, contains " +elemT+"]", type.toString());
  }
}