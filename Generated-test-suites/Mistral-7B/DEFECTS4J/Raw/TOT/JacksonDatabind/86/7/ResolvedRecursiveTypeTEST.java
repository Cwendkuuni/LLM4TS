import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetReference() {
 JavaType javaType1 = new ClassType(String.class); // Arrange
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(String[].class, null); // Arrange
 recursiveType1.setReference(javaType1); // Act
 JavaType expected = javaType1; // Assert
 assertSame(expected, recursiveType1.getSelfReferencedType());
 }

 @Test (expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 JavaType javaType1 = new ClassType(String.class); // Arrange
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(String[].class, null); // Arrange
 recursiveType1.setReference(javaType1); // Act & Assert
 recursiveType1.setReference(null); // Act - this should throw an exception
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null); // Arrange
 JavaType expected = null; // Assert
 assertSame(expected, recursiveType.getSelfReferencedType());

 JavaType javaType1 = new ClassType(String.class); // Arrange
 recursiveType.setReference(javaType1); // Act
 JavaType actual = recursiveType.getSelfReferencedType(); // Act & Assert
 assertSame(javaType1, actual);
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null); // Arrange
 StringBuilder expected = new StringBuilder("[recursive type; "); // Assert
 expected.append("UNRESOLVED"); // Arrange for unresolved type
 StringBuilder actual = new StringBuilder(); // Act
 recursiveType.getGenericSignature(actual); // Act
 assertSame(expected.toString(), actual.toString());

 JavaType javaType1 = new ClassType(String.class); // Arrange
 recursiveType.setReference(javaType1); // Act
 expected.append(javaType1.getRawClass().getName()); // Arrange for resolved type
 actual.setLength(0); // Reset for next test
 recursiveType.getGenericSignature(actual); // Act
 assertSame(expected.toString(), actual.toString());
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null); // Arrange
 StringBuilder expected = new StringBuilder("[recursive type; "); // Assert
 expected.append("UNRESOLVED"); // Arrange for unresolved type
 StringBuilder actual = new StringBuilder(); // Act
 recursiveType.getErasedSignature(actual); // Act
 assertSame(expected.toString(), actual.toString());

 JavaType javaType1 = new ClassType(String.class); // Arrange
 recursiveType.setReference(javaType1); // Act
 expected.setLength(0); // Reset for next test
 expected.append("[");
 expected.append(javaType1.getErasedSignature(new StringBuilder()).toString());
 expected.append("]"); // Arrange for resolved type
 actual.setLength(0); // Reset for next test
 recursiveType.getErasedSignature(actual); // Act
 assertSame(expected.toString(), actual.toString());
 }

 @Test
 public void testWithContentType() {
 JavaType javaType1 = new ClassType(String.class); // Arrange
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null); // Arrange
 JavaType expected = recursiveType; // Assert
 recursiveType = recursiveType.withContentType(javaType1); // Act
 assertSame(expected, recursiveType);
 JavaType actual = recursiveType.getSelfReferencedType(); // Act & Assert
 assertSame(javaType1, actual);
 }

 @Test
 public void testWithTypeHandler() {
 Object handler = new Object(); // Arrange
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null); // Arrange
 JavaType expected = recursiveType; // Assert
 recursiveType = recursiveType.withTypeHandler(handler); // Act
 assertSame(expected, recursiveType);
 }

 @Test
 public void testWithContentTypeHandler() {
 Object handler = new Object(); // Arrange
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null); // Arrange
 JavaType expected = recursiveType; // Assert
 recursiveType = recursiveType.withContentTypeHandler(handler); // Act
 assertSame(expected, recursiveType);
 }

 @Test
 public void testWithValueHandler() {
 Object handler = new Object(); // Arrange
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null); // Arrange
 JavaType expected = recursiveType; // Assert
 recursiveType = recursiveType.withValueHandler(handler); // Act
 assertSame(expected, recursiveType);
 }

 @Test
 public void testWithContentValueHandler() {
 Object handler = new Object(); // Arrange
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null); // Arrange
 JavaType expected = recursiveType; // Assert
 recursiveType = recursiveType.withContentValueHandler(handler); // Act
 assertSame(expected, recursiveType);
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null); // Arrange
 JavaType expected = recursiveType; // Assert
 recursiveType = recursiveType.withStaticTyping(); // Act
 assertSame(expected, recursiveType);
 }

 @Test
 public void testNarrow() {
 Class<?> subclass = String[].class; // Arrange
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(Object.class, null); // Arrange
 JavaType expected = recursiveType; // Assert
 recursiveType = recursiveType.withErasedType(subclass); // Act
 JavaType actual = recursiveType.narrow(subclass); // Act & Assert
 assertNull(actual);
 }

 @Test
 public void testRefine() {
 Class<?> rawType = Object.class; // Arrange
 TypeBindings bindings = null; // Arrange
 JavaType superClass = null; // Arrange
 JavaType[] superInterfaces = null; // Arrange
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null); // Arrange
 JavaType expected = null; // Assert
 JavaType actual = recursiveType.refine(rawType, bindings, superClass, superInterfaces); // Act & Assert
 assertNull(actual);
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null); // Arrange
 boolean expected = false; // Assert
 boolean actual = recursiveType.isContainerType(); // Act & Assert
 assertSame(expected, actual);
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String[].class, null); // Arrange
 StringBuilder expected = new StringBuilder("[recursive type; "); // Assert
 expected.append("UNRESOLVED"); // Arrange for unresolved type
 StringBuilder actual = new StringBuilder(); // Act
 recursiveType.toString(actual); // Act
 assertSame(expected.toString(), actual.toString());

 JavaType javaType1 = new ClassType(String.class); // Arrange
 recursiveType.setReference(javaType1); // Act
 expected.setLength(0); // Reset for next test
 expected.append("[recursive type; ");
 expected.append(javaType1.getRawClass().getName()); // Arrange for resolved type
 actual.setLength(0); // Reset for next test
 recursiveType.toString(actual); // Act
 assertSame(expected.toString(), actual.toString());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(String[].class, null); // Arrange
 ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(String[].class, null); // Arrange
 JavaType javaType1 = new ClassType(String.class); // Arrange
 JavaType javaType2 = new ClassType(String.class); // Arrange
 boolean expected = false; // Assert
 boolean actual = recursiveType1.equals(recursiveType2); // Act & Assert
 assertSame(expected, actual);

 recursiveType2.setReference(javaType1); // Act
 expected = false; // Reset for next test
 actual = recursiveType1.equals(recursiveType2); // Act & Assert
 assertSame(expected, actual);

 recursiveType1.setReference(javaType1); // Act
 expected = true; // Reset for next test
 actual = recursiveType1.equals(recursiveType2); // Act & Assert
 assertSame(expected, actual);

 JavaType recursiveType3 = new ResolvedRecursiveType(Integer[].class, null); // Arrange
 expected = false; // Assert
 actual = recursiveType1.equals(recursiveType3); // Act & Assert
 assertSame(expected, actual);
 }
}