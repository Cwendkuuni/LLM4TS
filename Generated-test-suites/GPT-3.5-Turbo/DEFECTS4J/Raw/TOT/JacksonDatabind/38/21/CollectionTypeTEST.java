import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

  @Test
  public void testConstruct() {
    // Typical use case
    CollectionType collectionType1 = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
    assertEquals(ArrayList.class, collectionType1.getRawClass());
    assertEquals(ArrayList.class, collectionType1.getClass());

    // Edge case
    CollectionType collectionType2 = CollectionType.construct(LinkedList.class, new TypeBindings(), null, null, Integer.class);
    assertEquals(LinkedList.class, collectionType2.getRawClass());
    assertEquals(LinkedList.class, collectionType2.getClass());

    // Error scenario
    CollectionType collectionType3 = CollectionType.construct(null, new TypeBindings(), null, null, Object.class);
    assertNull(collectionType3);
  }

  @Test
  public void testWithContentType() {
    // Typical use case
    CollectionType collectionType1 = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
    CollectionType newCollectionType1 = collectionType1.withContentType(Integer.class);
    assertEquals(Integer.class, newCollectionType1.getContentType().getRawClass());

    // Edge case
    CollectionType collectionType2 = CollectionType.construct(LinkedList.class, new TypeBindings(), null, null, Double.class);
    CollectionType newCollectionType2 = collectionType2.withContentType(Long.class);
    assertEquals(Long.class, newCollectionType2.getContentType().getRawClass());

    // Error scenario
    CollectionType collectionType3 = CollectionType.construct(HashSet.class, new TypeBindings(), null, null, Float.class);
    CollectionType newCollectionType3 = collectionType3.withContentType(null);
    assertNull(newCollectionType3);
  }

  @Test
  public void testWithTypeHandler() {
    // Typical use case
    CollectionType collectionType1 = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
    CollectionType newCollectionType1 = collectionType1.withTypeHandler(new CustomHandler());
    assertEquals(new CustomHandler().getClass(), newCollectionType1.getTypeHandler().getClass());

    // Edge case
    CollectionType collectionType2 = CollectionType.construct(LinkedList.class, new TypeBindings(), null, null, Double.class);
    CollectionType newCollectionType2 = collectionType2.withTypeHandler(new CustomHandler());
    assertEquals(new CustomHandler().getClass(), newCollectionType2.getTypeHandler().getClass());

    // Error scenario
    CollectionType collectionType3 = CollectionType.construct(HashSet.class, new TypeBindings(), null, null, Float.class);
    CollectionType newCollectionType3 = collectionType3.withTypeHandler(null);
    assertNull(newCollectionType3.getTypeHandler());
  }

  @Test
  public void testWithContentTypeHandler() {
    // Typical use case
    CollectionType collectionType1 = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
    CollectionType newCollectionType1 = collectionType1.withContentTypeHandler(new CustomHandler());
    assertEquals(new CustomHandler().getClass(), newCollectionType1.getContentType().getTypeHandler().getClass());

    // Edge case
    CollectionType collectionType2 = CollectionType.construct(LinkedList.class, new TypeBindings(), null, null, Double.class);
    CollectionType newCollectionType2 = collectionType2.withContentTypeHandler(new CustomHandler());
    assertEquals(new CustomHandler().getClass(), newCollectionType2.getContentType().getTypeHandler().getClass());

    // Error scenario
    CollectionType collectionType3 = CollectionType.construct(HashSet.class, new TypeBindings(), null, null, Float.class);
    CollectionType newCollectionType3 = collectionType3.withContentTypeHandler(null);
    assertNull(newCollectionType3.getContentType().getTypeHandler());
  }

  @Test
  public void testWithContentValueHandler() {
    // Typical use case
    CollectionType collectionType1 = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
    CollectionType newCollectionType1 = collectionType1.withContentValueHandler(new CustomHandler());
    assertEquals(new CustomHandler().getClass(), newCollectionType1.getContentType().getValueHandler().getClass());

    // Edge case
    CollectionType collectionType2 = CollectionType.construct(LinkedList.class, new TypeBindings(), null, null, Double.class);
    CollectionType newCollectionType2 = collectionType2.withContentValueHandler(new CustomHandler());
    assertEquals(new CustomHandler().getClass(), newCollectionType2.getContentType().getValueHandler().getClass());

    // Error scenario
    CollectionType collectionType3 = CollectionType.construct(HashSet.class, new TypeBindings(), null, null, Float.class);
    CollectionType newCollectionType3 = collectionType3.withContentValueHandler(null);
    assertNull(newCollectionType3.getContentType().getValueHandler());
  }

  @Test
  public void testWithStaticTyping() {
    // Typical use case
    CollectionType collectionType1 = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
    CollectionType newCollectionType1 = collectionType1.withStaticTyping();
    assertTrue(newCollectionType1.isStaticTyping());

    // Edge case
    CollectionType collectionType2 = CollectionType.construct(LinkedList.class, new TypeBindings(), null, null, Double.class);
    CollectionType newCollectionType2 = collectionType2.withStaticTyping();
    assertTrue(newCollectionType2.isStaticTyping());

    // Error scenario
    CollectionType collectionType3 = CollectionType.construct(HashSet.class, new TypeBindings(), null, null, Float.class);
    CollectionType newCollectionType3 = collectionType3.withStaticTyping();
    assertFalse(newCollectionType3.isStaticTyping());
  }

  @Test
  public void testRefine() {
    // Typical use case
    CollectionType collectionType1 = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
    CollectionType newCollectionType1 = collectionType1.refine(LinkedList.class, new TypeBindings(), null, null);
    assertEquals(LinkedList.class, newCollectionType1.getRawClass());

    // Edge case
    CollectionType collectionType2 = CollectionType.construct(LinkedList.class, new TypeBindings(), null, null, Double.class);
    CollectionType newCollectionType2 = collectionType2.refine(ArrayList.class, new TypeBindings(), null, null);
    assertEquals(ArrayList.class, newCollectionType2.getRawClass());

    // Error scenario
    CollectionType collectionType3 = CollectionType.construct(HashSet.class, new TypeBindings(), null, null, Float.class);
    CollectionType newCollectionType3 = collectionType3.refine(null, new TypeBindings(), null, null);
    assertNull(newCollectionType3);
  }

  @Test
  public void testToString() {
    // Typical use case
    CollectionType collectionType1 = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
    assertEquals("[collection type; class java.util.ArrayList, contains class java.lang.String]", collectionType1.toString());

    // Edge case
    CollectionType collectionType2 = CollectionType.construct(LinkedList.class, new TypeBindings(), null, null, Double.class);
    assertEquals("[collection type; class java.util.LinkedList, contains class java.lang.Double]", collectionType2.toString());

    // Error scenario
    CollectionType collectionType3 = CollectionType.construct(HashSet.class, new TypeBindings(), null, null, Float.class);
    assertEquals("[collection type; class java.util.HashSet, contains class java.lang.Float]", collectionType3.toString());
  }
}