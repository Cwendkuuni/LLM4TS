import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SimpleType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings.TypeSerializerBindings;
import com.fasterxml.jackson.databind.type.TypeModifier;

@RunWith(org.junit.runner.JUnit4.class)
public class ReferenceTypeTest {

 @Test
 public void testReferenceTypeConstruction() {
 SimpleType baseType = mock(SimpleType.class);
 JavaType referencedType = mock(JavaType.class);

 ReferenceType referenceType = new ReferenceType(baseType, referencedType);

 assert(referenceType instanceof ReferenceType);
 assert(referenceType.getReferencedType().equals(referencedType));
 assert(referenceType._anchorType.equals(referenceType));
 }

 @Test
 public void testUpgradeFrom() {
 SimpleType baseType = mock(SimpleType.class);
 JavaType referencedType = mock(JavaType.class);

 ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, referencedType);

 assert(referenceType instanceof ReferenceType);
 assert(referenceType.getReferencedType().equals(referencedType));
 assert(referenceType._anchorType.equals(referenceType));
 }

 @Test
 public void testConstruct() {
 Class<String> cls = String.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = mock(JavaType.class);

 ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType);

 assert(referenceType instanceof ReferenceType);
 assert(referenceType.getReferencedType().equals(refType));
 assert(referenceType._anchorType.equals(referenceType));
 }

 @Test
 public void testWithContentType() {
 Class<String> cls = String.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = mock(JavaType.class);
 JavaType contentType = mock(JavaType.class);

 ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
 ReferenceType newReferenceType = referenceType.withContentType(contentType);

 assert(newReferenceType instanceof ReferenceType);
 assert(newReferenceType.getReferencedType().equals(refType));
 assert(newReferenceType._anchorType.equals(referenceType));
 assert(newReferenceType.getContentType().equals(contentType));
 }

 @Test
 public void testGetContentType() {
 Class<String> cls = String.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = mock(JavaType.class);
 ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);

 assert(referenceType.getContentType() == null);
 assert(referenceType.getReferencedType().equals(refType));

 JavaType contentType = mock(JavaType.class);
 referenceType = referenceType.withContentType(contentType);

 assert(referenceType.getContentType().equals(contentType));
 }

 @Test
 public void testGetReferencedType() {
 Class<String> cls = String.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = mock(JavaType.class);
 ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);

 assert(referenceType.getReferencedType().equals(refType));
 }

 @Test
 public void testHasContentType() {
 Class<String> cls = String.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = mock(JavaType.class);
 ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);

 assert(referenceType.hasContentType());
 }

 @Test
 public void testIsReferenceType() {
 Class<String> cls = String.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = mock(JavaType.class);
 ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);

 assert(referenceType.isReferenceType());
 }

 @Test
 public void testGetErasedSignature() {
 Class<String> cls = String.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = mock(JavaType.class);
 ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
 StringBuilder sb = new StringBuilder();

 assertNull(referenceType.getErasedSignature(sb));
 }

 @Test
 public void testGetGenericSignature() {
 Class<String> cls = String.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = mock(JavaType.class);
 ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
 StringBuilder sb = new StringBuilder();

 referenceType.getGenericSignature(sb);
 String result = sb.toString();

 assertEquals("[Ljava.lang.String;", result);
 }

 @Test
 public void testGetAnchorType() {
 Class<String> cls = String.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = mock(JavaType.class);
 JavaType anchorType = mock(JavaType.class);
 ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);

 assertEquals(anchorType, referenceType.getAnchorType());
 }

 @Test
 public void testIsAnchorType() {
 Class<String> cls = String.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = mock(JavaType.class);
 JavaType anchorType = mock(JavaType.class);
 ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);

 assertFalse(referenceType.isAnchorType());

 JavaType superAnchorType = mock(JavaType.class);
 ReferenceType superReferenceType = new ReferenceType(superAnchorType, refType);
 referenceType = referenceType.withTypeHandler(superReferenceType);

 assertTrue(referenceType.isAnchorType());
 }

 @Test
 public void testToString() {
 Class<String> cls = String.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = mock(JavaType.class);
 ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);

 String result = referenceType.toString();

 assertEquals("[reference type, class java.lang.String, reference type <class 'java.lang.Object'>]", result);
 }

 @Test
 public void testEquals() {
 Class<String> cls = String.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType1 = mock(JavaType.class);
 JavaType refType2 = mock(JavaType.class);
 JavaType contentType = mock(JavaType.class);

 ReferenceType referenceType1 = new ReferenceType(cls, bindings, superClass, superInts, refType1, null, null, null, false);
 ReferenceType referenceType2 = new ReferenceType(cls, bindings, superClass, superInts, refType2, null, null, null, false);
 ReferenceType referenceType3 = new ReferenceType(cls, bindings, superClass, superInts, refType1, null, null, null, false);
 ReferenceType referenceType4 = new ReferenceType(cls, bindings, superClass, superInts, refType1, null, null, null, true);

 assertTrue(referenceType1.equals(referenceType1));
 assertTrue(referenceType1.equals(referenceType3));
 assertFalse(referenceType1.equals(referenceType2));
 assertFalse(referenceType1.equals(contentType));
 assertFalse(referenceType1.equals(null));
 }

 @Test
 public void testNarrow() {
 Class<String> cls = String.class;
 Class<Integer> subClass = Integer.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = mock(JavaType.class);

 ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);

 assertEquals(referenceType.getClass(), ReferenceType.class);
 JavaType narrowType = referenceType._narrow(subClass);

 assertEquals(narrowType.getClass(), ReferenceType.class);
 assertEquals(narrowType.getReferencedType().getClass(), Integer.class);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNarrowUnsupported() {
 Class<String> cls = String.class;
 Class<Integer> subClass = Integer.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = mock(JavaType.class);

 ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);

 JavaType narrowType = referenceType._narrow(Object.class);
 }

 @Test
 public void testConstructWithTypeHandler() {
 Class<String> cls = String.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = mock(JavaType.class);
 Object valueHandler = mock(Object.class);
 Object typeHandler = mock(Object.class);

 ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, valueHandler, typeHandler, false);

 assertEquals(valueHandler, referenceType._valueHandler);
 assertEquals(typeHandler, referenceType._typeHandler);
 }

 @Test
 public void testConstructWithContentTypeHandler() {
 Class<String> cls = String.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = mock(JavaType.class);
 Object contentTypeHandler = mock(Object.class);
 Object valueHandler = mock(Object.class);
 Object typeHandler = mock(Object.class);

 ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, valueHandler, typeHandler, false);
 ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandler);

 assertEquals(contentTypeHandler, newReferenceType._referencedType.getTypeHandler());
 }

 @Test
 public void testConstructWithValueHandler() {
 Class<String> cls = String.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = mock(JavaType.class);
 Object valueHandler = mock(Object.class);
 Object typeHandler = mock(Object.class);

 ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
 ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);

 assertEquals(valueHandler, newReferenceType._valueHandler);
 }

 @Test
 public void testConstructWithContentValueHandler() {
 Class<String> cls = String.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = mock(JavaType.class);
 Object contentValueHandler = mock(Object.class);
 Object valueHandler = mock(Object.class);
 Object typeHandler = mock(Object.class);

 ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
 ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);

 assertEquals(contentValueHandler, newReferenceType._referencedType.getValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 Class<String> cls = String.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = mock(JavaType.class);
 ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);

 ReferenceType newReferenceType = referenceType.withStaticTyping();

 assertTrue(newReferenceType._asStatic);
 }

 @Test
 public void testRefine() {
 Class<String> cls = String.class;
 Class<Number> subClass = Number.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = mock(JavaType.class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = mock(JavaType.class);

 ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
 JavaType refinedType = referenceType.refine(subClass, bindings, superClass, superInts);

 assertEquals(subClass, refinedType.getRawClass());
 }
}