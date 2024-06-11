import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import static com.fasterxml.jackson.databind.type.TypeFactory.*;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

 @Test
 public void testConstruct() {
 Class<String> stringClass = String.class;
 JavaType javaType = typeOf(int[].class);
 ReferenceType referenceType = ReferenceType.construct(stringClass, javaType, null, null);
 assertEquals(String.class, referenceType.getRawClass());
 assertEquals(int[].class, referenceType.getContentType().getRawClass());
 }

 @Test
 public void testSetTypeHandler() {
 Class<String> stringClass = String.class;
 JavaType javaType = typeOf(int[].class);
 Object handler = new Object();
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
 assertEquals(handler, newReferenceType.getTypeHandler());
 }

 @Test
 public void testSetContentTypeHandler() {
 Class<String> stringClass = String.class;
 JavaType javaType = typeOf(int[].class);
 JavaType newContentType = typeOf(long[].class);
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentType);
 assertEquals(newContentType, newReferenceType.getContentType());
 }

 @Test
 public void testSetValueHandler() {
 Class<String> stringClass = String.class;
 JavaType javaType = typeOf(int[].class);
 Object handler = new Object();
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 ReferenceType newReferenceType = referenceType.withValueHandler(handler);
 assertEquals(handler, newReferenceType.getValueHandler());
 }

 @Test
 public void testSetContentValueHandler() {
 Class<String> stringClass = String.class;
 JavaType javaType = typeOf(int[].class);
 Object handler = new Object();
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 ReferenceType newReferenceType = referenceType.withContentValueHandler(handler);
 assertEquals(handler, newReferenceType.getContentValueHandler());
 }

 @Test
 public void testSetStaticTyping() {
 Class<String> stringClass = String.class;
 JavaType javaType = typeOf(int[].class);
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 ReferenceType newReferenceType = referenceType.withStaticTyping();
 assertTrue(newReferenceType.isStatic());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTypeHandler() {
 Class<String> stringClass = String.class;
 JavaType javaType = typeOf(int[].class);
 Object handler = new Object();
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 referenceType.withTypeHandler("not an object");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidContentTypeHandler() {
 Class<String> stringClass = String.class;
 JavaType javaType = typeOf(int[].class);
 JavaType newContentType = typeOf(long[].class);
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 referenceType.withContentTypeHandler(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidValueHandler() {
 Class<String> stringClass = String.class;
 JavaType javaType = typeOf(int[].class);
 Object handler = new Object();
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 referenceType.withValueHandler(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidContentValueHandler() {
 Class<String> stringClass = String.class;
 JavaType javaType = typeOf(int[].class);
 Object handler = new Object();
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 referenceType.withContentValueHandler(null);
 }

 @Test
 public void testGetReferencedType() {
 Class<String> stringClass = String.class;
 JavaType javaType = typeOf(int[].class);
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 assertEquals(javaType, referenceType.getReferencedType());
 }

 @Test
 public void testIsReferenceType() {
 Class<String> stringClass = String.class;
 JavaType javaType = typeOf(int[].class);
 ReferenceType referenceType = new ReferenceType(stringClass, javaType, null, null, false);
 assertTrue(referenceType.isReferenceType());
 }
}