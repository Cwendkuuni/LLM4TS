import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetReference() {
 JavaType javaType1 = new TypeBase(String.class, null, null, null, 0, null, null, false);
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(String[].class, null);
 JavaType javaType2 = new TypeBase(Integer.class, null, null, null, 0, null, null, false);
 ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(Integer[].class, null);

 recursiveType1.setReference(recursiveType2);
 JavaType expected = recursiveType2;
 JavaType actual = recursiveType1.getSelfReferencedType();
 assertEquals("Setting reference should set the _referencedType correctly.", expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 JavaType javaType1 = new TypeBase(String.class, null, null, null, 0, null, null, false);
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(String[].class, null);
 JavaType javaType2 = new TypeBase(Integer.class, null, null, null, 0, null, null, false);
 ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(Integer[].class, null);

 recursiveType1.setReference(recursiveType2);
 recursiveType1.setReference(recursiveType2);
 }
}