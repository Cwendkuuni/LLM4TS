import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SimpleType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ReferenceTypeTest {

 @Test
 public void testReferenceTypeConstruction() {
 Class<?> cls = String.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superClass = SimpleType.construct(int[].class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = SimpleType.construct(String.class);
 JavaType anchorType = null;

 // Test constructor with all parameters
 ReferenceType referenceType1 = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);
 assertEquals(cls, referenceType1.getClass());
 assertEquals(refType, referenceType1.getReferencedType());
 assertNull(referenceType1.getAnchorType());

 // Test constructor with no anchorType
 JavaType anchorType2 = referenceType1;
 ReferenceType referenceType2 = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
 assertEquals(cls, referenceType2.getClass());
 assertEquals(refType, referenceType2.getReferencedType());
 assertNotNull(referenceType2.getAnchorType());
 assertEquals(referenceType1, referenceType2.getAnchorType());

 // Test constructor upgrading from SimpleType
 JavaType baseType = SimpleType.construct(int[].class);
 JavaType refdType = SimpleType.construct(String.class);
 ReferenceType referenceType3 = ReferenceType.upgradeFrom(baseType, refdType);
 assertEquals(baseType, referenceType3._base);
 assertEquals(refdType, referenceType3.getReferencedType());
 assertNotNull(referenceType3.getAnchorType());
 assertEquals(referenceType3, referenceType3.getAnchorType());
 }

 @Test
 public void testWithContentType() {
 Class<?> cls = String.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superClass = SimpleType.construct(int[].class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = SimpleType.construct(String.class);
 JavaType anchorType = null;

 ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);
 JavaType contentType = SimpleType.construct(Integer.TYPE);
 ReferenceType newReferenceType = referenceType.withContentType(contentType);
 assertEquals(referenceType, newReferenceType._anchorType);
 assertEquals(contentType, newReferenceType.getReferencedType());
 }

 @Test
 public void testFactoryMethod() {
 Class<?> cls = String.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superClass = SimpleType.construct(int[].class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = SimpleType.construct(String.class);

 JavaType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType);
 assertEquals(cls, referenceType.getClass());
 assertEquals(refType, referenceType.getReferencedType());
 assertNull(referenceType.getAnchorType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFactoryMethodWithNullRefdType() {
 Class<?> cls = String.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superClass = SimpleType.construct(int[].class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = null;
 JavaType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromInvalidBaseType() {
 JavaType baseType = SimpleType.construct(String.class);
 JavaType refdType = SimpleType.construct(Integer.TYPE);
 ReferenceType.upgradeFrom(baseType, refdType); // this will throw an exception
 }

 @Test
 public void testToString() {
 Class<?> cls = String.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superClass = SimpleType.construct(int[].class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = SimpleType.construct(Integer.TYPE);
 JavaType anchorType = null;

 ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);
 assertEquals("[reference type, class java.lang.String<>]", referenceType.toString());
 }

 @Test
 public void testEquals() {
 Class<?> cls = String.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superClass = SimpleType.construct(int[].class);
 JavaType[] superInts = new JavaType[0];
 JavaType refType = SimpleType.construct(String.class);
 JavaType anchorType = null;

 ReferenceType referenceType1 = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);
 ReferenceType referenceType2 = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);
 assertTrue(referenceType1.equals(referenceType2));

 ReferenceType referenceType3 = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, true);
 assertFalse(referenceType1.equals(referenceType3));

 JavaType refType2 = SimpleType.construct(Integer.TYPE);
 ReferenceType referenceType4 = new ReferenceType(cls, bindings, superClass, superInts, refType2, anchorType, null, null, false);
 assertFalse(referenceType1.equals(referenceType4));
 }
}