import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 SimpleType simpleType = new SimpleType(String.class);
 assertNotNull(simpleType);
 assertEquals(String.class, simpleType.getRawClass());
 }

 @Test
 public void testConstructUnsafe() {
 SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
 assertNotNull(simpleType);
 assertEquals(Integer.class, simpleType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithMap() {
 SimpleType simpleType = SimpleType.construct(Map.class);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithCollection() {
 SimpleType simpleType = SimpleType.construct(ArrayList.class);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithArray() {
 SimpleType simpleType = SimpleType.construct(int[].class);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testNarrow() {
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType narrowedType = simpleType.narrow(Integer.class);
 assertNull(narrowedType);
 }

 @Test
 public void testWithContentType() {
 try {
 SimpleType simpleType = new SimpleType(String.class);
 JavaType contentType = new SimpleModule().addTypeHandler(String.class, new StringTypeHandler()).construct();
 simpleType.withContentType(contentType);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testWithTypeHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 Object typeHandler = new Object() {};
 SimpleType withTypeHandler = simpleType.withTypeHandler(typeHandler);
 assertNotSame(simpleType, withTypeHandler);
 assertEquals(typeHandler, withTypeHandler.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 try {
 SimpleType simpleType = new SimpleType(String.class);
 Object contentTypeHandler = new Object() {};
 simpleType.withContentTypeHandler(contentTypeHandler);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testWithValueHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 Object valueHandler = new Object() {};
 SimpleType withValueHandler = simpleType.withValueHandler(valueHandler);
 assertNotSame(simpleType, withValueHandler);
 assertEquals(valueHandler, withValueHandler.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 try {
 SimpleType simpleType = new SimpleType(String.class);
 Object contentValueHandler = new Object() {};
 simpleType.withContentValueHandler(contentValueHandler);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testWithStaticTyping() {
 SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false);
 SimpleType staticTyping = simpleType.withStaticTyping();
 assertTrue(staticTyping.isStatic());
 }

 @Test
 public void testRefine() {
 SimpleType simpleType = new SimpleType(String.class);
 Class<?> rawType = Integer.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = null;
 JavaType[] superInterfaces = new JavaType[] { new SimpleType(List.class) };
 SimpleType refinedType = simpleType.refine(rawType, bindings, superClass, superInterfaces);
 assertNull(refinedType);
 }

 @Test
 public void testBuildCanonicalName() {
 SimpleType simpleType = new SimpleType(List.class, null, null, null, null, null, false);
 String canonicalName = simpleType.buildCanonicalName();
 assertEquals("[simple type, class java.util.List]", canonicalName);
 }

 @Test
 public void testIsContainerType() {
 SimpleType simpleType = new SimpleType(String.class);
 assertFalse(simpleType.isContainerType());
 }

 @Test
 public void testGetErasedSignature() {
 SimpleType simpleType = new SimpleType(List.class, null, null, null, null, null, false);
 StringBuilder sb = new StringBuilder();
 simpleType.getErasedSignature(sb);
 assertEquals("[java.util.List]", sb.toString());
 }

 @Test
 public void testGetGenericSignature() {
 SimpleType simpleType = new SimpleType(List.class, null, null, null, null, null, false);
 StringBuilder sb = new StringBuilder();
 simpleType.getGenericSignature(sb);
 assertEquals("[java.util.List<T>]", sb.toString());
 }
}