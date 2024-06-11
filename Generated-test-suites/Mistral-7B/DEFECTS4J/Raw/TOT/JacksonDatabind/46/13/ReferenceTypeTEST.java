import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReferenceTypeTest {

 @Test
 public void testConstruct() {
 Class<String> stringClass = String.class;
 JavaType intType = SimpleType.construct(int.class);
 ReferenceType referenceType = ReferenceType.construct(stringClass, intType, null, null);
 assertEquals(stringClass, referenceType.getRawClass());
 assertEquals(intType.getClass(), referenceType.getContentType().getClass());
 }

 @Test
 public void testGetReferencedType() {
 Class<String> stringClass = String.class;
 JavaType intType = SimpleType.construct(int.class);
 ReferenceType referenceType = ReferenceType.construct(stringClass, intType, null, null);
 assertNotNull(referenceType.getReferencedType());
 assertEquals(intType, referenceType.getReferencedType());
 }

 @Test
 public void testIsReferenceType() {
 Class<String> stringClass = String.class;
 JavaType intType = SimpleType.construct(int.class);
 ReferenceType referenceType = ReferenceType.construct(stringClass, intType, null, null);
 assertTrue(referenceType.isReferenceType());
 }

 @Test
 public void testWithTypeHandler() {
 Class<String> stringClass = String.class;
 JavaType intType = SimpleType.construct(int.class);
 Object handler = new Object();
 ReferenceType referenceType = ReferenceType.construct(stringClass, intType, null, null);
 JavaType newType = referenceType.withTypeHandler(handler);
 assertNotSame(referenceType, newType);
 assertEquals(intType, newType.getContentType());
 assertEquals(handler, newType.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<String> stringClass = String.class;
 JavaType intType = SimpleType.construct(int.class);
 JavaType textType = SimpleType.construct(String.class);
 Object handler = new Object();
 ReferenceType referenceType = ReferenceType.construct(stringClass, intType, null, null);
 JavaType newType = referenceType.withContentTypeHandler(handler);
 assertNotSame(referenceType, newType);
 assertEquals(intType, newType.getContentType());
 assertEquals(handler, newType.getContentTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 Class<String> stringClass = String.class;
 JavaType intType = SimpleType.construct(int.class);
 Object handler = new Object();
 ReferenceType referenceType = ReferenceType.construct(stringClass, intType, null, null);
 JavaType newType = referenceType.withValueHandler(handler);
 assertNotSame(referenceType, newType);
 assertEquals(intType, newType.getContentType());
 assertEquals(handler, newType.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<String> stringClass = String.class;
 JavaType intType = SimpleType.construct(int.class);
 Object handler = new Object();
 ReferenceType referenceType = ReferenceType.construct(stringClass, intType, null, null);
 JavaType newType = referenceType.withContentValueHandler(handler);
 assertNotSame(referenceType, newType);
 assertEquals(intType, newType.getContentType());
 assertEquals(handler, newType.getValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 Class<String> stringClass = String.class;
 JavaType intType = SimpleType.construct(int.class);
 ReferenceType referenceType = ReferenceType.construct(stringClass, intType, null, null);
 JavaType newType = referenceType.withStaticTyping();
 assertTrue(newType.isStatic());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidWithTypeHandler() {
 Class<String> stringClass = String.class;
 JavaType intType = SimpleType.construct(int.class);
 Object handler = null;
 ReferenceType referenceType = ReferenceType.construct(stringClass, intType, null, null);
 JavaType newType = referenceType.withTypeHandler(handler);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidWithContentTypeHandler() {
 Class<String> stringClass = String.class;
 JavaType intType = SimpleType.construct(int.class);
 JavaType textType = SimpleType.construct(String.class);
 Object handler = null;
 ReferenceType referenceType = ReferenceType.construct(stringClass, intType, null, null);
 JavaType newType = referenceType.withContentTypeHandler(handler);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidWithValueHandler() {
 Class<String> stringClass = String.class;
 JavaType intType = SimpleType.construct(int.class);
 Object handler = null;
 ReferenceType referenceType = ReferenceType.construct(stringClass, intType, null, null);
 JavaType newType = referenceType.withValueHandler(handler);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidWithContentValueHandler() {
 Class<String> stringClass = String.class;
 JavaType intType = SimpleType.construct(int.class);
 Object handler = null;
 ReferenceType referenceType = ReferenceType.construct(stringClass, intType, null, null);
 JavaType newType = referenceType.withContentValueHandler(handler);
 }

 @Test
 public void testGetCanonicalName() {
 Class<String> stringClass = String.class;
 JavaType intType = SimpleType.construct(int.class);
 ReferenceType referenceType = ReferenceType.construct(stringClass, intType, null, null);
 assertEquals("com.fasterxml.jackson.databind.type.ReferenceType<java.lang.String, class int>", referenceType.getCanonicalName());
 }
}