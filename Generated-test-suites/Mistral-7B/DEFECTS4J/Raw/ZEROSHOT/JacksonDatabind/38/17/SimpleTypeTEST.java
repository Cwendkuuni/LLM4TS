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

 @Test
 public void testConstruct() {
 SimpleType simpleType = SimpleType.construct(Integer.class);
 assertNotNull(simpleType);
 assertEquals(Integer.class, simpleType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructInvalidType() {
 SimpleType simpleType = SimpleType.construct(Map.class);
 fail("Should not reach this point");
 }

 @Test
 public void testGetErasedSignature() {
 SimpleType simpleType = SimpleType.construct(String.class);
 StringBuilder sb = new StringBuilder();
 simpleType.getErasedSignature(sb);
 assertEquals("java.lang.String", sb.toString());
 }

 @Test
 public void testGetGenericSignature() {
 SimpleType simpleType = SimpleType.construct(List.class);
 StringBuilder sb = new StringBuilder();
 simpleType.getGenericSignature(sb);
 assertEquals("[java.util.List, java.lang.Object]", sb.toString());
 }

 @Test
 public void testGetRawClass() {
 SimpleType simpleType = SimpleType.construct(String.class);
 assertEquals(String.class, simpleType.getRawClass());
 }

 @Test
 public void testIsContainerType() {
 SimpleType simpleType = SimpleType.construct(String.class);
 assertFalse(simpleType.isContainerType());
 }

 @Test
 public void testEquals() {
 SimpleType simpleType1 = SimpleType.construct(String.class);
 SimpleType simpleType2 = SimpleType.construct(String.class);
 assertTrue(simpleType1.equals(simpleType2));
 }

 @Test
 public void testNotEquals() {
 SimpleType simpleType1 = SimpleType.construct(String.class);
 SimpleType simpleType2 = SimpleType.construct(Integer.class);
 assertFalse(simpleType1.equals(simpleType2));
 }

 @Test
 public void testRefine() {
 SimpleType simpleType = SimpleType.construct(String.class);
 JavaType javaType = simpleType.refine(String[].class, null, null, null);
 assertNull(javaType);
 }

 @Test
 public void testNarrow() {
 SimpleType simpleType1 = SimpleType.construct(String.class);
 SimpleType simpleType2 = SimpleType.construct(String[].class);
 JavaType javaType = simpleType1.narrow(String[].class);
 assertSame(simpleType2, javaType);
 }

 @Test
 public void testWithContentType() {
 SimpleType simpleType = SimpleType.construct(String.class);
 JavaType javaType = simpleType.withContentType(IntegerType.INT_TYPE);
 assertNull(javaType);
 }

 @Test
 public void testWithTypeHandler() {
 SimpleType simpleType = SimpleType.construct(String.class);
 simpleType = simpleType.withTypeHandler(null);
 assertNotNull(simpleType.getTypeHandler());
 simpleType = simpleType.withTypeHandler(new Object());
 assertNotNull(simpleType.getTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 SimpleType simpleType = SimpleType.construct(String.class);
 simpleType = simpleType.withValueHandler(null);
 assertNotNull(simpleType.getValueHandler());
 simpleType = simpleType.withValueHandler(new Object());
 assertNotNull(simpleType.getValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 SimpleType simpleType = SimpleType.construct(String.class);
 JavaType javaType = simpleType.withStaticTyping();
 assertTrue(javaType.isStaticTyping());
 }

 @Test
 public void testBuildCanonicalName() {
 SimpleType simpleType = SimpleType.construct(List.class);
 assertEquals("[java.util.List, java.lang.Object]", simpleType.buildCanonicalName());
 }
}