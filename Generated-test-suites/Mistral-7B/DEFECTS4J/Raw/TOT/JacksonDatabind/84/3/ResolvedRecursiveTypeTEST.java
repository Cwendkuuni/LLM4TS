import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetReference() {
 JavaType refType = new TypeFactory().constructType(String.class);
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null);
 recursiveType.setReference(refType);
 JavaType actualType = recursiveType.getSelfReferencedType();
 assertEquals(refType, actualType);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 JavaType refType = new TypeFactory().constructType(String.class);
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null);
 recursiveType.setReference(refType);
 recursiveType.setReference(new TypeFactory().constructType(Integer.class));
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null);
 assertNull(recursiveType.getSelfReferencedType());

 JavaType refType = new TypeFactory().constructType(String.class);
 recursiveType.setReference(refType);
 assertEquals(refType, recursiveType.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null);
 StringBuilder actualSignature = new StringBuilder();
 recursiveType.getGenericSignature(actualSignature);
 String expectedSignature = "[Ljava.lang.Object;"; // Erased type signature for String[]
 assertEquals(expectedSignature, actualSignature.toString());

 JavaType refType = new TypeFactory().constructType(String.class);
 recursiveType.setReference(refType);
 recursiveType.getGenericSignature(actualSignature);
 String refTypeSignature = "[L" + refType.getRawClass().getName() + ';'; // Erased type signature for the given JavaType
 assertEquals(refTypeSignature, actualSignature.toString());
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null);
 StringBuilder actualSignature = new StringBuilder();
 recursiveType.getErasedSignature(actualSignature);
 assertNull(actualSignature.toString());

 JavaType refType = new TypeFactory().constructType(String.class);
 recursiveType.setReference(refType);
 recursiveType.getErasedSignature(actualSignature);
 assertEquals(refType.getRawClass().getName(), actualSignature.toString());
 }

 // Add more test cases for other methods if needed
}