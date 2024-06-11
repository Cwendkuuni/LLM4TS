import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ReferenceTypeTest {

 @Test
 public void testConstruct() {
 JavaType originalType = new TypeReference<String[]>() {}.getType();
 ReferenceType referenceType = ReferenceType.construct(String[].class, originalType, null, null);
 assertEquals(String[].class, referenceType.getRawClass());
 assertEquals(originalType, referenceType.getContentType());
 }

 @Test
 public void testGetReferencedType() {
 JavaType originalType = new TypeReference<String[]>() {}.getType();
 ReferenceType referenceType = new ReferenceType(String[].class, originalType, null, null, false);
 assertEquals(originalType, referenceType.getReferencedType());
 }

 @Test
 public void testIsReferenceType() {
 JavaType originalType = new TypeReference<String[]>() {}.getType();
 ReferenceType referenceType = new ReferenceType(String[].class, originalType, null, null, false);
 assertTrue(referenceType.isReferenceType());
 }

 @Test
 public void testWithTypeHandler() {
 JavaType originalType = new TypeReference<String[]>() {}.getType();
 Object typeHandler = new Object() {};
 ReferenceType referenceType = new ReferenceType(String[].class, originalType, null, null, false);
 ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
 assertNotEquals(referenceType, newReferenceType);
 assertEquals(originalType, newReferenceType.getContentType());
 assertEquals(typeHandler, newReferenceType.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 JavaType originalType = new TypeReference<String[]>() {}.getType();
 JavaType newContentTypeHandler = new TypeReference<Integer[]>() {}.getType();
 ReferenceType referenceType = new ReferenceType(String[].class, originalType, null, null, false);
 ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentTypeHandler);
 assertNotEquals(referenceType, newReferenceType);
 assertEquals(originalType, newReferenceType.getContentType());
 assertEquals(newContentTypeHandler, newReferenceType.getContentTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 JavaType originalType = new TypeReference<String[]>() {}.getType();
 Object valueHandler = new Object() {};
 ReferenceType referenceType = new ReferenceType(String[].class, originalType, null, null, false);
 ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
 assertNotEquals(referenceType, newReferenceType);
 assertEquals(originalType, newReferenceType.getContentType());
 assertEquals(valueHandler, newReferenceType.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 JavaType originalType = new TypeReference<String[]>() {}.getType();
 Object contentValueHandler = new Object() {};
 ReferenceType referenceType = new ReferenceType(String[].class, originalType, null, null, false);
 ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);
 assertNotEquals(referenceType, newReferenceType);
 assertEquals(originalType, newReferenceType.getContentType());
 assertEquals(contentValueHandler, newReferenceType.getContentValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 JavaType originalType = new TypeReference<String[]>() {}.getType();
 ReferenceType referenceType = new ReferenceType(String[].class, originalType, null, null, false);
 ReferenceType newReferenceType = referenceType.withStaticTyping();
 assertNotEquals(referenceType, newReferenceType);
 assertTrue(newReferenceType.isStatic());
 }

 @Test
 public void testNarrow() {
 JavaType originalType = new TypeReference<String[]>() {}.getType();
 JavaType narrowedType = new TypeReference<String[][]>() {}.getType();
 ReferenceType referenceType = new ReferenceType(String[].class, originalType, null, null, false);
 JavaType narrowedReferenceType = referenceType._narrow(String[][].class);
 assertEquals(narrowedType, narrowedReferenceType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNarrowInvalidSubclass() {
 JavaType originalType = new TypeReference<String[]>() {}.getType();
 JavaType invalidSubclass = new TypeReference<Integer[]>() {}.getType();
 ReferenceType referenceType = new ReferenceType(String[].class, originalType, null, null, false);
 JavaType narrowedReferenceType = referenceType._narrow(Integer[].class);
 }

 @Test
 public void testBuildCanonicalName() {
 Class<String[]> arrayClass = String[].class;
 JavaType originalType = new TypeReference<String[]>() {}.getType();
 ReferenceType referenceType = new ReferenceType(arrayClass, originalType, null, null, false);
 String canonicalName = referenceType.buildCanonicalName();
 assertEquals("[Ljava.lang.String;Ljava/lang/reflect/Type;@1", canonicalName);
 }
}