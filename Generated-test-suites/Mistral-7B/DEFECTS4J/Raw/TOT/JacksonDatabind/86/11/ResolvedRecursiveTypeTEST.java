import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 private JavaType createResolvedRecursiveType(Class<?> erasedType) {
 return new ResolvedRecursiveType(erasedType, null);
 }

 @Test
 public void testSettingReference() {
 ResolvedRecursiveType type1 = createResolvedRecursiveType(Object.class);
 JavaType referenceType = createResolvedRecursiveType(String.class);
 type1.setReference(referenceType);
 ResolvedRecursiveType type2 = createResolvedRecursiveType(Object.class);
 type2.setReference(referenceType);

 // Test equality
 assertTrue(type1.equals(type2));
 // Test self-referenced type
 assertEquals(referenceType, type1.getSelfReferencedType());
 // Test getting signatures
 StringBuilder sb1 = new StringBuilder(100);
 type1.getGenericSignature(sb1);
 assertEquals("java.lang.Object<* extends java.lang.Object>, java.lang.Object", sb1.toString());
 StringBuilder sb2 = new StringBuilder(100);
 type2.getGenericSignature(sb2);
 assertEquals("java.lang.Object<* extends java.lang.Object>, java.lang.Object", sb2.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testSettingReferenceTwice() {
 ResolvedRecursiveType type = createResolvedRecursiveType(Object.class);
 JavaType referenceType = createResolvedRecursiveType(String.class);
 type.setReference(referenceType);
 type.setReference(referenceType);
 }
}