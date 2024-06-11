import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

 @Test
 public void testConstructUnsafe() {
 SimpleType simpleType = SimpleType.constructUnsafe(String.class);
 assertNotNull(simpleType);
 assertEquals(String.class, simpleType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithMap() {
 SimpleType simpleType = SimpleType.construct(Map.class);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithCollection() {
 SimpleType simpleType = SimpleType.construct(List.class);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithArray() {
 SimpleType simpleType = SimpleType.construct(int[].class);
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testNarrow() {
 SimpleType stringType = SimpleType.construct(String.class);
 SimpleType stringSubType = SimpleType.construct(StringBuffer.class);
 SimpleType narrowedType = stringType.narrow(StringBuffer.class);
 assertEquals(stringSubType, narrowedType);
 }

 @Test
 public void testIsContainerType() {
 assertFalse(SimpleType.class.isInstance(new SimpleType(String.class)).isContainerType());
 }

 @Test
 public void testGetErasedSignature() {
 SimpleType stringType = SimpleType.construct(String.class);
 StringBuilder sb = new StringBuilder();
 stringType.getErasedSignature(sb);
 assertEquals("java.lang.String", sb.toString());
 }

 @Test
 public void testGetGenericSignature() {
 SimpleType listType = SimpleType.construct(List.class);
 StringBuilder sb = new StringBuilder();
 listType.getGenericSignature(sb);
 assertEquals("[Ljava.lang.Object;", sb.toString());
 }

 @Test
 public void testWithTypeHandler() {
 SimpleType stringType = SimpleType.construct(String.class);
 JavaType javaType = SimpleType.construct(StringBuffer.class);
 SimpleType typeWithTypeHandler = stringType.withTypeHandler(javaType);
 assertEquals(stringType.getRawClass(), typeWithTypeHandler.getRawClass());
 assertEquals(javaType, typeWithTypeHandler.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 SimpleType stringType = SimpleType.construct(String.class);
 SimpleType typeWithContentTypeHandler = stringType.withContentTypeHandler(StringBuffer.class);
 assertEquals(stringType.getRawClass(), typeWithContentTypeHandler.getRawClass());
 assertNull(typeWithContentTypeHandler.getTypeHandler());
 assertNull(typeWithContentTypeHandler.getValueHandler());
 }

 @Test
 public void testWithValueHandler() {
 SimpleType stringType = SimpleType.construct(String.class);
 Object object = new Object();
 SimpleType typeWithValueHandler = stringType.withValueHandler(object);
 assertEquals(stringType.getRawClass(), typeWithValueHandler.getRawClass());
 assertEquals(object, typeWithValueHandler.getValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 SimpleType stringType = SimpleType.construct(String.class);
 SimpleType typeWithStaticTyping = stringType.withStaticTyping();
 assertTrue(typeWithStaticTyping.isStatic());
 }

 @Test
 public void testRefine() {
 SimpleType stringType = SimpleType.construct(String.class);
 SimpleType refinedType = stringType.refine(StringBuilder.class, null, null, null);
 assertNull(refinedType);
 }

 @Test
 public void testBuildCanonicalName() {
 SimpleType listType = SimpleType.construct(List.class);
 assertEquals("[Ljava.util.List;", listType.buildCanonicalName());

 SimpleType listTypeWithIntGeneric = SimpleType.construct(List.class, new TypeBindings().bind(0, Integer.class));
 assertEquals("[Ljava.util.List;<0>Ljava/lang/Integer;", listTypeWithIntGeneric.buildCanonicalName());
 }

 @Test
 public void testToString() {
 SimpleType stringType = SimpleType.construct(String.class);
 assertEquals("[simple type, class java.lang.String]", stringType.toString());
 }

 @Test
 public void testEquals() {
 SimpleType stringType1 = SimpleType.construct(String.class);
 SimpleType stringType2 = SimpleType.construct(String.class);
 assertTrue(stringType1.equals(stringType2));

 SimpleType stringType3 = SimpleType.construct(StringBuffer.class);
 assertFalse(stringType1.equals(stringType3));
 }
}