import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

 private JavaType createSimpleType(Class<?> cls) {
 return new SimpleType(cls, TypeBindings.emptyBindings());
 }

 @Test
 public void testReferenceTypeConstructor() {
 Class<?> cls = String.class;
 JavaType refType = createSimpleType(Integer.class);
 ReferenceType referenceType = ReferenceType.construct(cls, TypeBindings.emptyBindings(), null, null, refType);

 Assert.assertEquals(cls, referenceType.getRawClass());
 Assert.assertEquals(refType, referenceType.getContentType());
 }

 @Test
 public void testReferenceTypeUpgradeFrom() {
 Class<?> cls = String.class;
 JavaType baseType = createSimpleType(Integer.class);
 JavaType refType = createSimpleType(Double.class);
 ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

 Assert.assertEquals(cls, referenceType.getRawClass());
 Assert.assertEquals(refType, referenceType.getContentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReferenceTypeUpgradeFromNullRefType() {
 Class<?> cls = String.class;
 JavaType baseType = createSimpleType(Integer.class);
 ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReferenceTypeUpgradeFromNonTypeBase() {
 Class<?> cls = String.class;
 JavaType baseType = createSimpleType(Integer.class);
 JavaType nonTypeBase = new Object() {};
 ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, nonTypeBase);
 }

 @Test
 public void testReferenceTypeWithContentType() {
 Class<?> cls = String.class;
 JavaType refType = createSimpleType(Integer.class);
 JavaType contentType = createSimpleType(Long.class);
 ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);

 ReferenceType newReferenceType = referenceType.withContentType(contentType);

 Assert.assertEquals(cls, newReferenceType.getRawClass());
 Assert.assertEquals(contentType, newReferenceType.getContentType());
 }

 @Test
 public void testReferenceTypeWithTypeHandler() {
 Class<?> cls = String.class;
 JavaType refType = createSimpleType(Integer.class);
 Object typeHandler = new Object() {};
 ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);

 ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);

 Assert.assertEquals(cls, newReferenceType.getRawClass());
 Assert.assertEquals(refType, newReferenceType.getContentType());
 Assert.assertEquals(typeHandler, newReferenceType.getTypeHandler());
 }

 @Test
 public void testReferenceTypeWithContentTypeHandler() {
 Class<?> cls = String.class;
 JavaType refType = createSimpleType(Integer.class);
 Object contentTypeHandler = new Object() {};
 Object typeHandler = new Object() {};
 ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);

 ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandler);

 Assert.assertEquals(cls, newReferenceType.getRawClass());
 Assert.assertEquals(refType, newReferenceType.getContentType());
 Assert.assertEquals(contentTypeHandler, newReferenceType.getContentTypeHandler());
 }

 @Test
 public void testReferenceTypeWithValueHandler() {
 Class<?> cls = String.class;
 JavaType refType = createSimpleType(Integer.class);
 Object valueHandler = new Object() {};
 ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);

 ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);

 Assert.assertEquals(cls, newReferenceType.getRawClass());
 Assert.assertEquals(refType, newReferenceType.getContentType());
 Assert.assertEquals(valueHandler, newReferenceType.getValueHandler());
 }

 @Test
 public void testReferenceTypeWithContentValueHandler() {
 Class<?> cls = String.class;
 JavaType refType = createSimpleType(Integer.class);
 Object contentValueHandler = new Object() {};
 Object valueHandler = new Object() {};
 ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);

 ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);

 Assert.assertEquals(cls, newReferenceType.getRawClass());
 Assert.assertEquals(refType, newReferenceType.getContentType());
 Assert.assertEquals(contentValueHandler, newReferenceType.getContentValueHandler());
 Assert.assertEquals(valueHandler, newReferenceType.getValueHandler());
 }

 @Test
 public void testReferenceTypeWithStaticTyping() {
 Class<?> cls = String.class;
 JavaType refType = createSimpleType(Integer.class);
 ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);

 ReferenceType newReferenceType = referenceType.withStaticTyping();

 Assert.assertEquals(cls, newReferenceType.getRawClass());
 Assert.assertEquals(refType, newReferenceType.getContentType());
 Assert.assertTrue(newReferenceType.isStatic());
 }

 @Test
 public void testReferenceTypeRefine() {
 Class<?> rawType = String.class;
 JavaType refType = createSimpleType(Integer.class);
 JavaType refinedType = createSimpleType(Long.class);
 ReferenceType referenceType = new ReferenceType(rawType, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);

 JavaType newRefinedType = referenceType.refine(rawType, TypeBindings.emptyBindings(), null, null);

 Assert.assertEquals(rawType, newRefinedType.getRawClass());
 Assert.assertEquals(refType, newRefinedType.getContentType());
 Assert.assertEquals(refinedType, newRefinedType.getContentType());
 }

 @Test
 public void testReferenceTypeBuildCanonicalName() {
 Class<?> cls = String.class;
 JavaType refType = createSimpleType(Integer.class);
 ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);

 String canonicalName = referenceType.buildCanonicalName();

 Assert.assertEquals("java.lang.String<class com.fasterxml.jackson.databind.type.SimpleType@5d5d6d6d>", canonicalName);
 }

 @Test
 public void testReferenceTypeGetErasedSignature() {
 Class<?> cls = String.class;
 JavaType refType = createSimpleType(Integer.class);
 ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
 StringBuilder sb = new StringBuilder();
 referenceType.getErasedSignature(sb);

 Assert.assertEquals("String", sb.toString());
 }

 @Test
 public void testReferenceTypeGetGenericSignature() {
 Class<?> cls = String.class;
 JavaType refType = createSimpleType(Integer.class);
 StringBuilder sb = new StringBuilder();
 referenceType.getGenericSignature(sb);

 Assert.assertEquals("String<T extends Object>", sb.toString());
 }

 @Test
 public void testReferenceTypeGetContentType() {
 Class<?> cls = String.class;
 JavaType refType = createSimpleType(Integer.class);
 ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);

 Assert.assertEquals(refType, referenceType.getContentType());
 }

 @Test
 public void testReferenceTypeIsReferenceType() {
 Class<?> cls = String.class;
 JavaType refType = createSimpleType(Integer.class);
 ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);

 Assert.assertTrue(referenceType.isReferenceType());
 }

 @Test
 public void testReferenceTypeHasContentType() {
 Class<?> cls = String.class;
 JavaType refType = createSimpleType(Integer.class);
 ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);

 Assert.assertTrue(referenceType.hasContentType());
 }
}