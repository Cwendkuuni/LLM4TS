import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 // Test case for setReference method
 @Test
 public void testSetReference() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType refType = new JavaType(int[].class);
 type.setReference(refType);
 assertNotNull(type.getSelfReferencedType());
 assertEquals(refType, type.getSelfReferencedType());
 }

 // Test case for getSelfReferencedType method
 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType refType = new JavaType(int[].class);
 type.setReference(refType);
 assertNotNull(type.getSelfReferencedType());
 assertEquals(refType, type.getSelfReferencedType());
 }

 // Test case for getGenericSignature method
 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String[].class, new TypeBindings());
 StringBuilder sb = new StringBuilder();
 type.getGenericSignature(sb);
 assertTrue(sb.toString().contains("<T>"));
 }

 // Test case for getErasedSignature method
 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String[].class, new TypeBindings());
 StringBuilder sb = new StringBuilder();
 type.getErasedSignature(sb);
 assertEquals("java.lang.String[]", sb.toString());
 }

 // Test case for equals method
 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, new TypeBindings());
 ResolvedRecursiveType type3 = new ResolvedRecursiveType(String[].class, new TypeBindings());
 JavaType refType = new JavaType(int[].class);
 ResolvedRecursiveType type4 = new ResolvedRecursiveType(String.class, new TypeBindings());
 type4.setReference(refType);
 ResolvedRecursiveType type5 = new ResolvedRecursiveType(String.class, new TypeBindings());
 type5.setReference(refType);

 assertTrue(type1.equals(type1));
 assertFalse(type1.equals(null));
 assertFalse(type1.equals(type2));
 assertFalse(type1.equals(type3));
 assertFalse(type1.equals(type4));
 assertFalse(type1.equals(type5));
 }
}