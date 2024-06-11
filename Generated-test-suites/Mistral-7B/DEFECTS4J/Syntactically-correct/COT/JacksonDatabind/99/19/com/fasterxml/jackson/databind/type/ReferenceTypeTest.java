import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

 private JavaType createReferenceType(Class<?> cls, JavaType refType) {
 return ReferenceType.construct(cls, TypeBindings.emptyBindings(), null, null, refType);
 }

 @Test
 public void testReferenceTypeConstructor() {
 Class<?> cls = String.class;
 JavaType refType = JavaType.construct(int.class);
 ReferenceType referenceType = createReferenceType(cls, refType);
 assertNotNull(referenceType);
 assertEquals(cls, referenceType.getRawClass());
 assertEquals(refType, referenceType.getContentType());
 }

 @Test
 public void testReferenceTypeWithContentTypeHandler() {
 Class<?> cls = String.class;
 JavaType refType = JavaType.construct(int.class);
 ReferenceType referenceType = createReferenceType(cls, refType);
 Object handler = new Object();
 JavaType newContentType = referenceType.withContentTypeHandler(handler);
 assertNotSame(referenceType, newContentType);
 assertEquals(cls, newContentType.getRawClass());
 assertEquals(refType, newContentType.getContentType());
 assertEquals(handler, newContentType.getContentTypeHandler());
 }

 @Test
 public void testUpgradeFrom() {
 Class<?> cls = String.class;
 JavaType refType = JavaType.construct(int.class);
 JavaType baseType = JavaType.construct(String.class);
 ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
 assertNotNull(referenceType);
 assertEquals(cls, referenceType.getRawClass());
 assertEquals(refType, referenceType.getContentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromNullRefType() {
 Class<?> cls = String.class;
 JavaType baseType = JavaType.construct(String.class);
 ReferenceType.upgradeFrom(baseType, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromInvalidBaseType() {
 Class<?> cls = String.class;
 JavaType refType = JavaType.construct(int.class);
 JavaType baseType = JavaType.construct(double.class);
 ReferenceType.upgradeFrom(baseType, refType);
 }

 @Test
 public void testWithStaticTyping() {
 Class<?> cls = String.class;
 JavaType refType = JavaType.construct(int.class);
 ReferenceType referenceType = createReferenceType(cls, refType);
 ReferenceType staticReferenceType = referenceType.withStaticTyping();
 assertNotSame(referenceType, staticReferenceType);
 assertTrue(staticReferenceType.isStatic());
 }

 @Test
 public void testIsAnchorType() {
 Class<?> cls = String.class;
 JavaType refType = JavaType.construct(int.class);
 ReferenceType referenceType = createReferenceType(cls, refType);
 assertFalse(referenceType.isAnchorType());
 }

 @Test
 public void testGetAnchorType() {
 Class<?> cls = String.class;
 JavaType refType = JavaType.construct(int.class);
 ReferenceType referenceType = createReferenceType(cls, refType);
 ReferenceType anchorType = new ReferenceType(referenceType, null);
 assertEquals(referenceType, anchorType.getAnchorType());
 }

 @Test
 public void testGetContentType() {
 Class<?> cls = String.class;
 JavaType refType = JavaType.construct(int.class);
 ReferenceType referenceType = createReferenceType(cls, refType);
 assertEquals(refType, referenceType.getContentType());
 }

 @Test
 public void testHasContentType() {
 Class<?> cls = String.class;
 JavaType refType = JavaType.construct(int.class);
 ReferenceType referenceType = createReferenceType(cls, refType);
 assertTrue(referenceType.hasContentType());
 }

 @Test
 public void testIsReferenceType() {
 Class<?> cls = String.class;
 JavaType refType = JavaType.construct(int.class);
 ReferenceType referenceType = createReferenceType(cls, refType);
 assertTrue(referenceType.isReferenceType());
 }

 @Test
 public void testGetErasedSignature() {
 Class<?> cls = String.class;
 JavaType refType = JavaType.construct(int.class);
 ReferenceType referenceType = createReferenceType(cls, refType);
 StringBuilder expected = new StringBuilder("Ljava/lang/String;");
 StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testGetGenericSignature() {
 Class<?> cls = String.class;
 JavaType refType = JavaType.construct(int.class);
 ReferenceType referenceType = createReferenceType(cls, refType);
 StringBuilder expected = new StringBuilder("Ljava/lang/String;<T:Ljava/lang/Integer;>");
 StringBuilder actual = new StringBuilder();
 referenceType.getGenericSignature(actual);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testEquals() {
 Class<?> cls1 = String.class;
 JavaType refType1 = JavaType.construct(int.class);
 Class<?> cls2 = String.class;
 JavaType refType2 = JavaType.construct(int.class);
 JavaType refType3 = JavaType.construct(long.class);
 ReferenceType referenceType1 = createReferenceType(cls1, refType1);
 ReferenceType referenceType2 = createReferenceType(cls2, refType1);
 ReferenceType referenceType3 = createReferenceType(cls1, refType3);
 assertTrue(referenceType1.equals(referenceType1));
 assertTrue(referenceType1.equals(referenceType2));
 assertFalse(referenceType1.equals(referenceType3));
 assertFalse(referenceType1.equals(null));
 }

}