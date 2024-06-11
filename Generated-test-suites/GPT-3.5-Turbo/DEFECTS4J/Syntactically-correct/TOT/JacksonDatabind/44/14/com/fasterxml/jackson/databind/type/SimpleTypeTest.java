import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

   @Test
   public void testConstructUnsafe() {
       SimpleType simpleType = SimpleType.constructUnsafe(String.class);
       assertEquals("com.fasterxml.jackson.databind.type.SimpleType", simpleType.getClass().getName());
       assertEquals("Simple type, class java.lang.String", simpleType.toString());
       assertFalse(simpleType.isContainerType());
       assertEquals("com.fasterxml.jackson.databind.JavaType", simpleType.getClass().getSuperclass().getName());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructUnsafeForMap() {
       SimpleType.constructUnsafe(Map.class);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructUnsafeForCollection() {
       SimpleType.constructUnsafe(Collection.class);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructUnsafeForArray() {
       SimpleType.constructUnsafe(String[].class);
   }

   @Test
   public void testConstruct() {
       SimpleType simpleType = SimpleType.construct(String.class);
       assertEquals("com.fasterxml.jackson.databind.type.SimpleType", simpleType.getClass().getName());
       assertEquals("Simple type, class java.lang.String", simpleType.toString());
       assertFalse(simpleType.isContainerType());
       assertEquals("com.fasterxml.jackson.databind.JavaType", simpleType.getClass().getSuperclass().getName());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructForMap() {
       SimpleType.construct(Map.class);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructForCollection() {
       SimpleType.construct(Collection.class);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructForArray() {
       SimpleType.construct(String[].class);
   }

   @Test
   public void testNarrow() {
       SimpleType baseType = SimpleType.construct(String.class);
       SimpleType narrowType = baseType._narrow(Object.class);
       assertEquals("com.fasterxml.jackson.databind.type.SimpleType", narrowType.getClass().getName());
       assertEquals("Simple type, class java.lang.Object", narrowType.toString());
       assertFalse(narrowType.isContainerType());
       assertEquals("com.fasterxml.jackson.databind.JavaType", narrowType.getClass().getSuperclass().getName());
   }

   @Test
   public void testWithContentType() {
       SimpleType simpleType = SimpleType.construct(String.class);
       assertFalse(simpleType.withContentType(String.class));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testWithContentTypeHandler() {
       SimpleType simpleType = SimpleType.construct(String.class);
       simpleType.withContentTypeHandler(Object.class);
   }

   @Test
   public void testWithValueHandler() {
       SimpleType simpleType = SimpleType.construct(String.class);
       assertFalse(simpleType.withValueHandler(Object.class));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testWithContentValueHandler() {
       SimpleType simpleType = SimpleType.construct(String.class);
       simpleType.withContentValueHandler(Object.class);
   }

   @Test
   public void testWithStaticTyping() {
       SimpleType simpleType = SimpleType.construct(String.class);
       assertFalse(simpleType.withStaticTyping());
   }

   @Test
   public void testRefine() {
       SimpleType simpleType = SimpleType.construct(String.class);
       assertNull(simpleType.refine(Object.class, null, null, null));
   }

   @Test
   public void testBuildCanonicalName() {
       SimpleType simpleType = SimpleType.constructUnsafe(String.class);
       assertEquals("java.lang.String", simpleType.buildCanonicalName());
   }

   @Test
   public void testIsContainerType() {
       SimpleType simpleType = SimpleType.constructUnsafe(String.class);
       assertFalse(simpleType.isContainerType());
   }

   @Test
   public void testGetErasedSignature() {
       SimpleType simpleType = SimpleType.constructUnsafe(String.class);
       StringBuilder sb = new StringBuilder();
       assertEquals("final class java.lang.String", simpleType.getErasedSignature(sb).toString());
   }

   @Test
   public void testGetGenericSignature() {
       SimpleType simpleType = SimpleType.constructUnsafe(String.class);
       StringBuilder sb = new StringBuilder();
       assertEquals("final class java.lang.String;", simpleType.getGenericSignature(sb).toString());
   }

   @Test
   public void testEquals() {
       SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
       SimpleType simpleType2 = SimpleType.constructUnsafe(String.class);
       assertTrue(simpleType1.equals(simpleType2));
   }

   @Test
   public void testEqualsWithDifferentBindings() {
       SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
       SimpleType simpleType2 = SimpleType.construct(String.class);
       assertFalse(simpleType1.equals(simpleType2));
   }

   @Test
   public void testEqualsWithDifferentClass() {
       SimpleType simpleType1 = SimpleType.constructUnsafe(String.class);
       SimpleType simpleType2 = SimpleType.construct(Object.class);
       assertFalse(simpleType1.equals(simpleType2));
   }

   @Test
   public void testEqualsWithNull() {
       SimpleType simpleType = SimpleType.constructUnsafe(String.class);
       assertFalse(simpleType.equals(null));
   }

   @Test
   public void testToString() {
       SimpleType simpleType = SimpleType.constructUnsafe(String.class);
       assertEquals("[simple type, class java.lang.String]", simpleType.toString());
   }
}