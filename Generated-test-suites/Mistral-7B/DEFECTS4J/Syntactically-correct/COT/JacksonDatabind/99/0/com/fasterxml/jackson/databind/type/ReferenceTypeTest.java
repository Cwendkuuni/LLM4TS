import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.databind.JavaType;

@RunWith(JUnit4.class)
public class ReferenceTypeTest {

 private JavaType simpleType;
 private JavaType referenceType;
 private JavaType baseType;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 simpleType = JavaType.construct(String.class);
 referenceType = JavaType.construct(String[].class);
 baseType = JavaType.construct(String.class);
 }

 @Test
 public void testConstructorWithClassSuperClassAndReferenceType() {
 ReferenceType actual = ReferenceType.construct(String[].class, null,
 JavaType.construct(Object.class), null, referenceType);
 JavaType expected = referenceType;
 assertEquals(expected, actual.getContentType());
 }

 @Test
 public void testConstructorWithClassBindingsSuperClassSuperInterfacesReferenceType() {
 ReferenceType actual = ReferenceType.construct(String[].class, null,
 JavaType.construct(Object.class), null, null, referenceType);
 JavaType expected = referenceType;
 assertEquals(expected, actual.getContentType());
 }

 @Test
 public void testConstructorWithClassBindingsSuperClassSuperInterfacesReferenceTypeAnchorType() {
 ReferenceType actual = ReferenceType.construct(String[].class, null,
 JavaType.construct(Object.class), null, null, referenceType, null);
 JavaType expected = referenceType;
 JavaType anchorTypeExpected = actual.getContentType();
 assertEquals(expected, actual.getContentType());
 assertEquals(referenceType, anchorTypeExpected);
 }

 @Test
 public void testConstructorWithTypeBaseReferenceType() {
 ReferenceType actual = new ReferenceType((TypeBase) baseType, referenceType);
 JavaType expected = referenceType;
 assertEquals(expected, actual.getContentType());
 }

 @Test
 public void testUpgradeFromJavaTypeJavaType() {
 JavaType actual = ReferenceType.upgradeFrom(simpleType, referenceType);
 JavaType expected = referenceType;
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromJavaTypeNull() {
 ReferenceType.upgradeFrom(simpleType, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromJavaTypeWrongType() {
 ReferenceType.upgradeFrom(JavaType.construct(Integer.class), referenceType);
 }

 @Test
 public void testWithContentType() {
 JavaType javaType = JavaType.construct(Integer.class);
 ReferenceType actual = this.actual;
 JavaType expected = new ReferenceType(actual._class, actual._bindings,
 actual._superClass, actual._superInterfaces, javaType, actual._anchorType,
 actual._valueHandler, actual._typeHandler, actual._asStatic);
 assertEquals(expected, actual.withContentType(javaType));
 }

 @Test
 public void testWithTypeHandler() {
 Object handler = new Object();
 ReferenceType actual = this.actual;
 JavaType javaType = JavaType.construct(Integer.class);
 JavaType expected = new ReferenceType(actual._class, actual._bindings,
 actual._superClass, actual._superInterfaces, javaType, actual._anchorType,
 handler, actual._typeHandler, actual._asStatic);
 assertEquals(expected, actual.withTypeHandler(handler));
 }

 @Test
 public void testWithContentTypeHandler() {
 Object handler = new Object();
 JavaType javaType = JavaType.construct(Integer.class);
 ReferenceType actual = this.actual;
 JavaType expected = new ReferenceType(actual._class, actual._bindings,
 actual._superClass, actual._superInterfaces, javaType, actual._anchorType,
 actual._valueHandler, handler, actual._asStatic);
 assertEquals(expected, actual.withContentTypeHandler(handler));
 }

 @Test
 public void testWithValueHandler() {
 Object handler = new Object();
 ReferenceType actual = this.actual;
 JavaType javaType = JavaType.construct(Integer.class);
 JavaType expected = new ReferenceType(actual._class, actual._bindings,
 javaType, actual._superInterfaces,
 actual._referencedType, actual._anchorType, handler, actual._typeHandler, actual._asStatic);
 assertEquals(expected, actual.withValueHandler(handler));
 }

 @Test
 public void testWithContentValueHandler() {
 Object handler = new Object();
 JavaType javaType = JavaType.construct(Integer.class);
 ReferenceType actual = this.actual;
 JavaType expected = new ReferenceType(actual._class, actual._bindings,
 javaType, actual._superInterfaces, actual._referencedType, actual._anchorType,
 actual._valueHandler, handler, actual._asStatic);
 assertEquals(expected, actual.withContentValueHandler(handler));
 }

 @Test
 public void testWithStaticTyping() {
 ReferenceType actual = this.actual;
 JavaType expected = new ReferenceType(actual._class, actual._bindings,
 actual._superClass, actual._superInterfaces, actual._referencedType, actual._anchorType,
 actual._valueHandler, actual._typeHandler, true);
 assertTrue(expected.isStaticTyping());
 }

 @Test
 public void testRefine() {
 JavaType javaType = JavaType.construct(Integer[].class);
 ReferenceType expected = new ReferenceType(javaType.getRawClass(),
 _bindings, _superClass, _superInterfaces, _referencedType, _anchorType,
 _valueHandler, _typeHandler, _asStatic);
 JavaType actual = actual.refine(javaType.getRawClass(), _bindings, _superClass, _superInterfaces);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetContentType() {
 JavaType actual = this.actual.getContentType();
 JavaType expected = referenceType;
 assertEquals(expected, actual);
 }

 @Test
 public void testGetReferencedType() {
 JavaType actual = this.actual.getReferencedType();
 JavaType expected = referenceType;
 assertEquals(expected, actual);
 }

 @Test
 public void testHasContentType() {
 boolean actual = this.actual.hasContentType();
 boolean expected = true;
 assertEquals(expected, actual);
 }

 @Test
 public void testIsReferenceType() {
 boolean actual = this.actual.isReferenceType();
 boolean expected = true;
 assertEquals(expected, actual);
 }

 @Test
 public void testGetErasedSignature() {
 StringBuilder actual = new StringBuilder();
 StringBuilder expected = new StringBuilder();
 this.actual.getErasedSignature(expected);
 String expectedString = "[Ljava.lang.Object;";
 expected.append(expectedString);
 StringBuilder actualString = new StringBuilder(actual.toString());
 assertEquals(expectedString, actualString.toString());
 }

 @Test
 public void testGetGenericSignature() {
 StringBuilder actual = new StringBuilder();
 StringBuilder expected = new StringBuilder();
 this.actual.getGenericSignature(expected);
 String expectedString = "[Ljava.lang.Object;<T>T[]>";
 expected.append(expectedString);
 StringBuilder actualString = new StringBuilder(actual.toString());
 assertEquals(expectedString, actualString.toString());
 }

 @Test
 public void testGetAnchorType() {
 JavaType actual = this.actual.getAnchorType();
 JavaType expected = referenceType;
 assertSame(expected, actual);
 }

 @Test
 public void testIsAnchorType() {
 boolean actual = this.actual.isAnchorType();
 boolean expected = false;
 assertFalse(actual);
 }

 @Test
 public void testToString() {
 String actual = this.actual.toString();
 String expected = "[reference type, class [Ljava.lang.Object;<:Ljava.lang.String;>, content type class java.lang.String]";
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 Object other = new ReferenceType(this.actual._class, this.actual._bindings,
 this.actual._superClass, this.actual._superInterfaces, this.actual._referencedType,
 this.actual._anchorType, this.actual._valueHandler, this.actual._typeHandler, this.actual._asStatic);
 boolean actual = this.actual.equals(other);
 boolean expected = true;
 assertEquals(expected, actual);
 }

 @Test
 public void testGetClass() {
 Class<?> actual = this.actual.getClass();
 Class<?> expected = ReferenceType.class;
 assertEquals(expected, actual);
 }
}