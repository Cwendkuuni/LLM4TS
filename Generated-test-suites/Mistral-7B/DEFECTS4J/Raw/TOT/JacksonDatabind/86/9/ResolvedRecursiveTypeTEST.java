import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetReference() {
 JavaType refType = new ClassType(String.class);
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null);

 recursiveType.setReference(refType);
 JavaType selfReferencedType = recursiveType.getSelfReferencedType();

 assertEquals(refType, selfReferencedType);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 JavaType refType = new ClassType(String.class);
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null);

 recursiveType.setReference(refType);
 recursiveType.setReference(new ClassType(Integer.class));
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null);
 JavaType refType = new ClassType(String.class);
 recursiveType.setReference(refType);

 JavaType selfReferencedType = recursiveType.getSelfReferencedType();
 assertEquals(refType, selfReferencedType);
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null);
 StringBuilder sb = new StringBuilder();
 TypeBase.getGenericSignature(recursiveType, sb);

 String expected = "[Ljava.lang.Object;";
 String actual = sb.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null);
 StringBuilder sb = new StringBuilder();
 recursiveType.getErasedSignature(sb);

 String expected = "[Ljava.lang.Object;";
 String actual = sb.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null);
 JavaType contentType = new ArrayType(IntegerType.getInstance(), 5);

 JavaType result = recursiveType.withContentType(contentType);
 assertEquals(recursiveType, result);
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null);
 JavaType refType = new ClassType(String.class);
 recursiveType.setReference(refType);

 String expected = "[recursive type; [Ljava.lang.String;@" + refType.getCanonicalName() + "]";
 String actual = recursiveType.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(String[].class, null);
 JavaType refType = new ClassType(String.class);
 recursiveType1.setReference(refType);

 ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(String[].class, null);
 JavaType refType2 = new ClassType(String.class);
 recursiveType2.setReference(refType2);

 assertNotEquals(recursiveType1, recursiveType2);

 ResolvedRecursiveType recursiveType3 = new ResolvedRecursiveType(String[].class, null);
 recursiveType3.setReference(refType);

 assertEquals(recursiveType1, recursiveType3);
 }
}