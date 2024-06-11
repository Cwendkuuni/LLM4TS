import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

   @Test
   public void testConstruct() {
      // Typical use case
      CollectionType colType = CollectionType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.class);
      assertNotNull(colType);

      // Edge case
      CollectionType colType2 = CollectionType.construct(java.util.List.class, null, null, null, java.lang.Integer.class);
      assertNotNull(colType2);

      // Error scenario
      CollectionType colType3 = CollectionType.construct(null, null, null, null, null);
      assertNull(colType3);
   }

   @Test
   public void test_withContentType() {
      // Typical use case
      CollectionType colType = CollectionType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.class);
      CollectionType updatedType = colType.withContentType(java.lang.String.class);
      assertNotEquals(colType, updatedType);
      assertEquals(java.lang.String.class, updatedType.getContentType());

      // Edge case
      CollectionType colType2 = CollectionType.construct(java.util.List.class, null, null, null, java.lang.Integer.class);
      CollectionType updatedType2 = colType2.withContentType(java.lang.String.class);
      assertNotEquals(colType2, updatedType2);
      assertEquals(java.lang.String.class, updatedType2.getContentType());

      // Error scenario
      CollectionType colType3 = CollectionType.construct(java.util.HashSet.class, null, null, null, java.lang.Integer.class);
      CollectionType updatedType3 = colType3.withContentType(null);
      assertNull(updatedType3.getContentType());
   }

   @Test
   public void test_withContentTypeHandler() {
      // Typical use case
      CollectionType colType = CollectionType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.class);
      CollectionType updatedType = colType.withContentTypeHandler(new MyTypeHandler());
      assertNotEquals(colType, updatedType);
      assertEquals(new MyTypeHandler(), updatedType.getContentTypeHandler());

      // Edge case
      CollectionType colType2 = CollectionType.construct(java.util.List.class, null, null, null, java.lang.Integer.class);
      CollectionType updatedType2 = colType2.withContentTypeHandler(new MyTypeHandler());
      assertNotEquals(colType2, updatedType2);
      assertEquals(new MyTypeHandler(), updatedType2.getContentTypeHandler());

      // Error scenario
      CollectionType colType3 = CollectionType.construct(java.util.HashSet.class, null, null, null, java.lang.Integer.class);
      CollectionType updatedType3 = colType3.withContentTypeHandler(null);
      assertNull(updatedType3.getContentTypeHandler());
   }

   @Test
   public void test_withValueHandler() {
      // Typical use case
      CollectionType colType = CollectionType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.class);
      CollectionType updatedType = colType.withValueHandler(new MyValueHandler());
      assertNotEquals(colType, updatedType);
      assertEquals(new MyValueHandler(), updatedType.getValueHandler());

      // Edge case
      CollectionType colType2 = CollectionType.construct(java.util.List.class, null, null, null, java.lang.Integer.class);
      CollectionType updatedType2 = colType2.withValueHandler(new MyValueHandler());
      assertNotEquals(colType2, updatedType2);
      assertEquals(new MyValueHandler(), updatedType2.getValueHandler());

      // Error scenario
      CollectionType colType3 = CollectionType.construct(java.util.HashSet.class, null, null, null, java.lang.Integer.class);
      CollectionType updatedType3 = colType3.withValueHandler(null);
      assertNull(updatedType3.getValueHandler());
   }

   @Test
   public void test_withContentValueHandler() {
      // Typical use case
      CollectionType colType = CollectionType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.class);
      CollectionType updatedType = colType.withContentValueHandler(new MyValueHandler());
      assertNotEquals(colType, updatedType);
      assertEquals(new MyValueHandler(), updatedType.getContentValueHandler());

      // Edge case
      CollectionType colType2 = CollectionType.construct(java.util.List.class, null, null, null, java.lang.Integer.class);
      CollectionType updatedType2 = colType2.withContentValueHandler(new MyValueHandler());
      assertNotEquals(colType2, updatedType2);
      assertEquals(new MyValueHandler(), updatedType2.getContentValueHandler());

      // Error scenario
      CollectionType colType3 = CollectionType.construct(java.util.HashSet.class, null, null, null, java.lang.Integer.class);
      CollectionType updatedType3 = colType3.withContentValueHandler(null);
      assertNull(updatedType3.getContentValueHandler());
   }

   @Test
   public void test_withStaticTyping() {
      // Typical use case
      CollectionType colType = CollectionType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.class);
      CollectionType updatedType = colType.withStaticTyping();
      assertNotEquals(colType, updatedType);
      assertTrue(updatedType.isStaticTyping());

      // Edge case
      CollectionType colType2 = CollectionType.construct(java.util.List.class, null, null, null, java.lang.Integer.class);
      CollectionType updatedType2 = colType2.withStaticTyping();
      assertNotEquals(colType2, updatedType2);
      assertTrue(updatedType2.isStaticTyping());

      // Error scenario
      CollectionType colType3 = CollectionType.construct(java.util.HashSet.class, null, null, null, java.lang.Integer.class);
      CollectionType updatedType3 = colType3.withStaticTyping();
      assertNotEquals(colType3, updatedType3);
      assertTrue(updatedType3.isStaticTyping());
   }

   @Test
   public void test_refine() {
      // Typical use case
      CollectionType colType = CollectionType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.class);
      CollectionType updatedType = colType.refine(java.util.List.class, null, null, null);
      assertNotEquals(colType, updatedType);
      assertEquals(java.util.List.class, updatedType.getRawClass());

      // Edge case
      CollectionType colType2 = CollectionType.construct(java.util.List.class, null, null, null, java.lang.Integer.class);
      CollectionType updatedType2 = colType2.refine(java.util.ArrayList.class, null, null, null);
      assertNotEquals(colType2, updatedType2);
      assertEquals(java.util.ArrayList.class, updatedType2.getRawClass());

      // Error scenario
      CollectionType colType3 = CollectionType.construct(java.util.HashSet.class, null, null, null, java.lang.Integer.class);
      CollectionType updatedType3 = colType3.refine(java.util.TreeSet.class, null, null, null);
      assertNotEquals(colType3, updatedType3);
      assertEquals(java.util.TreeSet.class, updatedType3.getRawClass());
   }

   @Test
   public void testToString() {
      CollectionType colType = CollectionType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.class);
      assertEquals("[collection type; class java.util.ArrayList, contains class java.lang.Integer]", colType.toString());

      CollectionType colType2 = CollectionType.construct(java.util.List.class, null, null, null, java.lang.Integer.class);
      assertEquals("[collection type; class java.util.List, contains class java.lang.Integer]", colType2.toString());

      CollectionType colType3 = CollectionType.construct(java.util.HashSet.class, null, null, null, java.lang.Integer.class);
      assertEquals("[collection type; class java.util.HashSet, contains class java.lang.Integer]", colType3.toString());
   }
}