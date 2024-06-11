import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 SimpleType simpleType = new SimpleType(String.class);
 assertEquals(String.class, simpleType.getRawClass());
 assertNull(simpleType.getSuperClass());
 assertNull(simpleType.getSuperInterfaces());
 }

 @Test
 public void testSimpleTypeCopyConstructor() {
 SimpleType simpleType1 = new SimpleType(String.class);
 SimpleType simpleType2 = new SimpleType(simpleType1);
 assertEquals(simpleType1, simpleType2);
 }

 @Test
 public void testSimpleTypeWithBindings() {
 SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings());
 assertEquals(String.class, simpleType.getRawClass());
 assertNull(simpleType.getSuperClass());
 assertNull(simpleType.getSuperInterfaces());

 SimpleType simpleTypeWithBindings = new SimpleType(String.class, TypeBindings.emptyBindings(),
 new SimpleType(Integer.class), null, null, null, false);
 assertNotEquals(simpleType, simpleTypeWithBindings);
 assertEquals(String.class, simpleTypeWithBindings.getRawClass());
 assertNotNull(simpleTypeWithBindings.getSuperClass());
 assertArrayEquals(new JavaType[] { new SimpleType(Integer.class) }, simpleTypeWithBindings.getSuperInterfaces());
 }

 @Test
 public void testSimpleTypeWithValueHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType simpleTypeWithValueHandler = new SimpleType(simpleType, null, null, null, "valueHandler", null, false);
 assertNotEquals(simpleType, simpleTypeWithValueHandler);
 assertSame(simpleType.getRawClass(), simpleTypeWithValueHandler.getRawClass());
 assertEquals("valueHandler", simpleTypeWithValueHandler.getValueHandler());
 }

 @Test
 public void testSimpleTypeWithTypeHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType simpleTypeWithTypeHandler = new SimpleType(simpleType, null, null, null, null, "typeHandler", false);
 assertNotEquals(simpleType, simpleTypeWithTypeHandler);
 assertSame(simpleType.getRawClass(), simpleTypeWithTypeHandler.getRawClass());
 assertEquals("typeHandler", simpleTypeWithTypeHandler.getTypeHandler());
 }

 @Test
 public void testSimpleTypeWithStaticTyping() {
 SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false);
 SimpleType simpleTypeWithStaticTyping = new SimpleType(simpleType, null, null, null, null, null, true);
 assertNotEquals(simpleType, simpleTypeWithStaticTyping);
 assertSame(simpleType.getRawClass(), simpleTypeWithStaticTyping.getRawClass());
 assertTrue(simpleTypeWithStaticTyping.isStaticTyping());
 }

 @Test
 public void testSimpleTypeNarrowing() {
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType simpleTypeNarrowed = simpleType.narrow(String[].class);
 assertNotEquals(simpleType, simpleTypeNarrowed);
 assertSame(String[].class, simpleTypeNarrowed.getRawClass());
 assertSame(simpleType.getRawClass(), simpleTypeNarrowed.getRawClass().getComponentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeWithInvalidInput() {
 SimpleType simpleType = new SimpleType(String.class);
 simpleType.narrow(Map.class);
 }
}