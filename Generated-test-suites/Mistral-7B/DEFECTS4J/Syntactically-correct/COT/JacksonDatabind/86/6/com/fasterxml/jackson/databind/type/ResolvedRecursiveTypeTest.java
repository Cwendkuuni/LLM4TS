import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 private JavaType createTestType(Class<?> erasedType) {
 return new ResolvedRecursiveType(erasedType, new TypeBindings());
 }

 @Test
 public void testConstructor() {
 ResolvedRecursiveType type = createTestType(String.class);
 assertNull(type.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReference_multipleCalls() {
 ResolvedRecursiveType type = createTestType(String.class);
 JavaType ref = new JavaType(Integer.class);
 type.setReference(ref);
 type.setReference(new JavaType(Long.class));
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType type = createTestType(String.class);
 JavaType ref = new JavaType(Integer.class);
 type.setReference(ref);
 assertEquals(ref, type.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = createTestType(String.class);
 JavaType ref = new JavaType(Integer.class);
 type.setReference(ref);
 StringBuilder sb = new StringBuilder();
 type.getGenericSignature(sb);
 String expected = "[recursive type; " + ref.getRawClass().getName() + "]";
 assertEquals(expected, sb.toString());
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = createTestType(String.class);
 JavaType ref = new JavaType(Integer.class);
 type.setReference(ref);
 StringBuilder sb = new StringBuilder();
 type.getErasedSignature(sb);
 assertNull(sb.toString());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = createTestType(String.class);
 JavaType ref1 = new JavaType(Integer.class);
 type1.setReference(ref1);
 ResolvedRecursiveType type2 = createTestType(String.class);
 JavaType ref2 = new JavaType(Integer.class);
 type2.setReference(ref2);
 assertFalse(type1.equals(type2));
 ResolvedRecursiveType type3 = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertFalse(type1.equals(type3));
 ResolvedRecursiveType type4 = new ResolvedRecursiveType(String.class, new TypeBindings());
 type4.setReference(ref1);
 assertTrue(type1.equals(type4));
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType type = createTestType(String.class);
 JavaType ref = new JavaType(Integer.class);
 type.setReference(ref);
 String expected = "[recursive type; [" + ref.getRawClass().getName() + "]]";
 assertEquals(expected, type.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReference_null() {
 ResolvedRecursiveType type = createTestType(String.class);
 type.setReference(null);
 }
}