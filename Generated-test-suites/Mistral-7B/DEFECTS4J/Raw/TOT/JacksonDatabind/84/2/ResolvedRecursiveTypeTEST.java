import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetAndGetSelfReferencedType() {
 JavaType javaType1 = new ClassType(String.class);
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(Integer.class, null);
 recursiveType1.setReference(javaType1);
 JavaType expectedJavaType = javaType1;
 JavaType actualJavaType = recursiveType1.getSelfReferencedType();
 assertEquals("Self-referenced type should match the set reference", expectedJavaType, actualJavaType);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetSelfReferencedTypeTwice() {
 JavaType javaType1 = new ClassType(String.class);
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(Integer.class, null);
 recursiveType1.setReference(javaType1);
 recursiveType1.setReference(javaType1); // This should throw an exception
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Integer[].class, null);
 StringBuilder expected = new StringBuilder("java.lang.Object[][]");
 StringBuilder actual = recursiveType.getGenericSignature(new StringBuilder());
 assertEquals("Generic signature should match the expected one", expected.toString(), actual.toString());
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Integer[].class, null);
 StringBuilder expected = new StringBuilder("java.lang.Object[][]");
 StringBuilder actual = recursiveType.getErasedSignature(new StringBuilder());
 assertEquals("Erased signature should match the expected one", expected.toString(), actual.toString());
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Integer.class, null);
 JavaType javaType = new ClassType(String.class);
 ResolvedRecursiveType expected = new ResolvedRecursiveType(Integer.class, null);
 ResolvedRecursiveType actual = recursiveType.withContentType(javaType);
 assertEquals("Instance should be the same after withContentType call", expected, actual);
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Integer.class, null);
 boolean actual = recursiveType.isContainerType();
 boolean expected = false;
 assertEquals("isContainerType should return false", expected, actual);
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Integer.class, null);
 String expected = "[recursive type; UNRESOLVED]";
 String actual = recursiveType.toString();
 assertEquals("toString should match the expected one", expected, actual);

 JavaType javaType1 = new ClassType(String.class);
 recursiveType.setReference(javaType1);
 expected = "[recursive type; " + javaType1.getRawClass().getName() + "]";
 actual = recursiveType.toString();
 assertEquals("toString should match the expected one after setting self-referenced type", expected, actual);
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(Integer.class, null);
 ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(Integer.class, null);
 assertFalse("Two different instances should not be equal", recursiveType1.equals(recursiveType2));

 JavaType javaType1 = new ClassType(String.class);
 recursiveType1.setReference(javaType1);
 ResolvedRecursiveType recursiveType3 = new ResolvedRecursiveType(Integer.class, null);
 recursiveType3.setReference(javaType1);
 assertTrue("Two identical instances should be equal", recursiveType1.equals(recursiveType3));
 }
}