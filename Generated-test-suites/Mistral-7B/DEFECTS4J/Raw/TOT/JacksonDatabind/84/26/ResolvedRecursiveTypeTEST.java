import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 // Test case for the constructor
 @Test
 public void testResolvedRecursiveTypeConstructor() {
 Class<Integer[]> erasedType = Integer[].class;
 TypeBindings bindings = new TypeBindings();
 ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
 assertNotNull(type);
 assertEquals(Integer[].class, type.getRawClass());
 assertNull(type.getSelfReferencedType());
 }

 // Test case for the setReference method
 @Test
 public void testSetReference() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 JavaType javaTypeRef = TypeFactory.defaultInstance().constructType(Integer.class);
 ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
 type.setReference(javaTypeRef);
 JavaType selfReferencedType = type.getSelfReferencedType();
 assertNotNull(selfReferencedType);
 assertEquals(Integer.class, selfReferencedType.getRawClass());
 }

 // Test case for getSelfReferencedType method
 @Test
 public void testGetSelfReferencedType() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 JavaType javaTypeRef = TypeFactory.defaultInstance().constructType(Integer.class);
 ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
 type.setReference(javaTypeRef);
 JavaType selfReferencedType = type.getSelfReferencedType();
 assertNotNull(selfReferencedType);
 assertEquals(Integer.class, selfReferencedType.getRawClass());
 }

 // Test case for equals method
 @Test
 public void testEquals() {
 Class<String> erasedType1 = String.class;
 TypeBindings bindings1 = new TypeBindings();
 JavaType javaTypeRef1 = TypeFactory.defaultInstance().constructType(Integer.class);
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType1, bindings1);
 type1.setReference(javaTypeRef1);
 Class<String> erasedType2 = String.class;
 TypeBindings bindings2 = new TypeBindings();
 JavaType javaTypeRef2 = TypeFactory.defaultInstance().constructType(Integer.class);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType2, bindings2);
 type2.setReference(javaTypeRef2);
 ResolvedRecursiveType sameType = new ResolvedRecursiveType(erasedType1, bindings1);
 sameType.setReference(javaTypeRef1);
 assertTrue(type1.equals(sameType));
 assertTrue(type1.equals(type2));
 ResolvedRecursiveType differentType = new ResolvedRecursiveType(Long[].class, bindings1);
 assertFalse(type1.equals(differentType));
 }

 // Test case for toString method
 @Test
 public void testToString() {
 Class<String> erasedType = String.class;
 TypeBindings bindings = new TypeBindings();
 JavaType javaTypeRef = TypeFactory.defaultInstance().constructType(Integer.class);
 ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
 type.setReference(javaTypeRef);
 String toString = type.toString();
 assertTrue(toString.contains("[recursive type; "));
 assertTrue(toString.contains("Integer"));
 }
}