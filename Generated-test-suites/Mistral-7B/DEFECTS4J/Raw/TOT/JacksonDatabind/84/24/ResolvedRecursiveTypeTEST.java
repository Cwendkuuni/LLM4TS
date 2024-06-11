import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetReferenceAndGetSelfReferencedType() {
 JavaType javaType1 = new ClassType(String.class);
 JavaType javaType2 = new ClassType(ResolvedRecursiveType.class);
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(String.class, null);
 ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);

 recursiveType1.setReference(javaType2);
 recursiveType2.setReference(recursiveType1);

 assertEquals("Expected self-referenced type to match", recursiveType1, recursiveType2.getSelfReferencedType());
 assertEquals("Expected self-referenced type to be of the correct class", javaType2.getClass(), recursiveType1.getSelfReferencedType().getClass());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceThrowsExceptionWhenAlreadySet() {
 JavaType javaType1 = new ClassType(String.class);
 JavaType javaType2 = new ClassType(ResolvedRecursiveType.class);
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(String.class, null);
 ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);

 recursiveType1.setReference(javaType2);
 recursiveType2.setReference(javaType2); // should throw an exception
 }
}