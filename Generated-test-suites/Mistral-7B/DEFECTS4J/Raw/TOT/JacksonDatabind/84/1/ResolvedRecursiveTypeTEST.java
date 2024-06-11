import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 @Test
 public void testSetReference() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(RecursiveClass.class, new TypeBindings());
 JavaType refType = new TypeReference<JavaType>() {}.getType();
 type1.setReference(refType);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(RecursiveClass.class, new TypeBindings());
 type2.setReference(type1);

 assertEquals(type1.getSelfReferencedType(), type2.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(RecursiveClass.class, new TypeBindings());
 JavaType refType = new TypeReference<JavaType>() {}.getType();
 type.setReference(refType);
 type.setReference(refType);
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(RecursiveClass.class, new TypeBindings());
 JavaType refType = new TypeReference<JavaType>() {}.getType();
 type.setReference(refType);

 assertNotNull(type.getSelfReferencedType());
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(RecursiveClass.class, new TypeBindings());
 JavaType refType = new TypeReference<JavaType>() {}.getType();
 type.setReference(refType);
 String expected = refType.getRawClass().getName() + "<*>";

 assertEquals(expected, type.getErasedSignature().toString());
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(RecursiveClass.class, new TypeBindings());
 JavaType refType = new TypeReference<JavaType>() {}.getType();
 type.setReference(refType);
 String expected = "<" + refType.getRawClass().getName() + "<*>>";

 assertEquals(expected, type.getGenericSignature().toString());
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(RecursiveClass.class, new TypeBindings());
 JavaType contentType = new TypeReference<JavaType>() {}.getType();

 assertEquals(type, type.withContentType(contentType));
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(RecursiveClass.class, new TypeBindings());
 Object handler = new Object();

 assertEquals(type, type.withTypeHandler(handler));
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(RecursiveClass.class, new TypeBindings());
 Object handler = new Object();

 assertEquals(type, type.withContentTypeHandler(handler));
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(RecursiveClass.class, new TypeBindings());
 Object handler = new Object();

 assertEquals(type, type.withValueHandler(handler));
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(RecursiveClass.class, new TypeBindings());
 Object handler = new Object();

 assertEquals(type, type.withContentValueHandler(handler));
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(RecursiveClass.class, new TypeBindings());

 assertEquals(type, type.withStaticTyping());
 }

 @Test
 public void testNarrow() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(RecursiveClass.class, new TypeBindings());
 TypeBindings bindings = new TypeBindings();
 Class<?> subclass = RecursiveSubclass.class;

 assertNull(type._narrow(subclass));
 }

 @Test
 public void testRefine() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(RecursiveClass.class, new TypeBindings());
 JavaType superClass = new TypeReference<JavaType>() {}.getType();
 JavaType[] superInterfaces = new JavaType[0];
 TypeBindings bindings = new TypeBindings();

 assertNull(type.refine(RecursiveClass.class, bindings, superClass, superInterfaces));
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(RecursiveClass.class, new TypeBindings());

 assertFalse(type.isContainerType());
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(RecursiveClass.class, new TypeBindings());
 JavaType refType = new TypeReference<JavaType>() {}.getType();
 type1.setReference(refType);
 String expected1 = "[recursive type; "
 + refType.getRawClass().getName() + "]";

 ResolvedRecursiveType type2 = new ResolvedRecursiveType(RecursiveClass.class, new TypeBindings());
 type2.setReference(type1);
 String expected2 = "[recursive type; "
 + type1.getRawClass().getName() + "]";

 assertEquals(expected1, type1.toString());
 assertEquals(expected2, type2.toString());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = new ResolvedRecursiveType(RecursiveClass.class, new TypeBindings());
 JavaType refType = new TypeReference<JavaType>() {}.getType();
 type1.setReference(refType);
 ResolvedRecursiveType type2 = new ResolvedRecursiveType(RecursiveClass.class, new TypeBindings());
 type2.setReference(refType);
 ResolvedRecursiveType type3 = new ResolvedRecursiveType(RecursiveClass.class, new TypeBindings());
 JavaType refType2 = new TypeReference<JavaType>() {}.getType();
 type3.setReference(refType2);

 assertTrue(type1.equals(type1));
 assertTrue(type1.equals(type2));
 assertFalse(type1.equals(type3));
 }
}

class RecursiveClass {
}

class RecursiveSubclass extends RecursiveClass {
}