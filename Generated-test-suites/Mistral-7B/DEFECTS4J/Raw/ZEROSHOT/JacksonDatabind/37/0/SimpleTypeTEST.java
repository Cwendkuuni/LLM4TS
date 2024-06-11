import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

@RunWith(JUnit4.class)
public class SimpleTypeTest {

 @Test
 public void testConstructUnsafe() {
 SimpleType simpleType = SimpleType.constructUnsafe(String.class);
 assertNotNull(simpleType);
 assertEquals(String.class, simpleType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructUnsafeWithArray() {
 SimpleType simpleType = SimpleType.constructUnsafe(int[].class);
 fail("Should not reach here");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructUnsafeWithMap() {
 SimpleType simpleType = SimpleType.constructUnsafe(HashMap.class);
 fail("Should not reach here");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructUnsafeWithCollection() {
 SimpleType simpleType = SimpleType.constructUnsafe(ArrayList.class);
 fail("Should not reach here");
 }

 @Test
 public void testConstruct() {
 SimpleType simpleType = SimpleType.construct(String.class);
 assertNotNull(simpleType);
 assertEquals(String.class, simpleType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithArray() {
 SimpleType simpleType = SimpleType.construct(int[].class);
 fail("Should not reach here");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithMap() {
 SimpleType simpleType = SimpleType.construct(HashMap.class);
 fail("Should not reach here");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithCollection() {
 SimpleType simpleType = SimpleType.construct(ArrayList.class);
 fail("Should not reach here");
 }

 @Test
 public void testNarrow() {
 SimpleType stringType = SimpleType.construct(String.class);
 SimpleType stringSubType = SimpleType.construct(StringSubclass.class);
 JavaType narrowedType = stringType._narrow(StringSubclass.class);
 assertEquals(stringSubType, narrowedType);
 }

 @Test
 public void testWithTypeHandler() {
 SimpleType stringType = SimpleType.construct(String.class);
 SimpleType stringTypeWithHandler = stringType.withTypeHandler(new MyTypeHandler());
 assertNotEquals(stringType, stringTypeWithHandler);
 assertEquals(stringType.getRawClass(), stringTypeWithHandler.getRawClass());
 }

 @Test
 public void testWithValueHandler() {
 SimpleType stringType = SimpleType.construct(String.class);
 SimpleType stringTypeWithHandler = stringType.withValueHandler(new MyValueHandler());
 assertNotEquals(stringType, stringTypeWithHandler);
 assertEquals(stringType.getRawClass(), stringTypeWithHandler.getRawClass());
 }

 @Test
 public void testWithStaticTyping() {
 SimpleType stringType = SimpleType.construct(String.class);
 SimpleType stringTypeStatic = stringType.withStaticTyping();
 assertNotEquals(stringType, stringTypeStatic);
 assertEquals(stringType.getRawClass(), stringTypeStatic.getRawClass());
 }

 @Test
 public void testBuildCanonicalName() {
 SimpleType simpleType = SimpleType.construct(String[].class);
 assertEquals("[Ljava.lang.String;", simpleType.buildCanonicalName());
 }

 @Test
 public void testIsContainerType() {
 SimpleType simpleType = SimpleType.construct(String.class);
 assertFalse(simpleType.isContainerType());
 }

 @Test
 public void testGetErasedSignature() {
 SimpleType simpleType = SimpleType.construct(String.class);
 StringBuilder signature = new StringBuilder();
 simpleType.getErasedSignature(signature);
 assertEquals("Ljava/lang/String;", signature.toString());
 }

 @Test
 public void testGetGenericSignature() {
 SimpleType simpleType = SimpleType.construct(List.class);
 StringBuilder signature = new StringBuilder();
 simpleType.getGenericSignature(signature);
 assertEquals("java.util.List<T>", signature.toString());
 }
}