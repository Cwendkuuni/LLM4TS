import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.classinfo.ClassId;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 Class<String> stringClass = String.class;
 SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
 assertEquals(String.class, simpleType.getRawClass());
 assertNull(simpleType.getSuperClass());
 assertNull(simpleType.getSuperInterfaces());
 }

 @Test
 public void testSimpleTypeWithSuperClass() {
 Class<String> stringClass = String.class;
 Class<Number> numberClass = Number.class;
 JavaType numberType = TypeFactory.defaultInstance().constructType(numberClass);
 SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), numberType, null);
 assertEquals(String.class, simpleType.getRawClass());
 assertEquals(numberType, simpleType.getSuperClass());
 assertNull(simpleType.getSuperInterfaces());
 }

 @Test
 public void testSimpleTypeWithSuperInterfaces() {
 Class<String> stringClass = String.class;
 Class<Comparable<String>> comparableStringClass = Comparable.class.getGenericType();
 SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null,
 (JavaType[]) new JavaType[] { comparableStringClass });
 assertEquals(String.class, simpleType.getRawClass());
 assertNull(simpleType.getSuperClass());
 assertEquals(comparableStringClass, simpleType.getSuperInterfaces()[0]);
 }

 @Test
 public void testSimpleTypeCopyConstructor() {
 SimpleType original = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
 SimpleType copy = new SimpleType(original);
 assertEquals(original.getRawClass(), copy.getRawClass());
 assertEquals(original.getSuperClass(), copy.getSuperClass());
 assertEquals(original.getSuperInterfaces(), copy.getSuperInterfaces());
 }

 @Test
 public void testSimpleTypeWithValueHandler() {
 Class<String> stringClass = String.class;
 SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, new StringValueHandler(), false);
 assertNull(simpleType.getValueHandler());
 simpleType = new SimpleType(simpleType, null, null, null, null, new StringValueHandler(), false);
 assertEquals(simpleType.getValueHandler(), new StringValueHandler());
 }

 @Test
 public void testSimpleTypeWithTypeHandler() {
 Class<String> stringClass = String.class;
 SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, null, false);
 assertNull(simpleType.getTypeHandler());
 simpleType = new SimpleType(simpleType, null, null, null, null, null, new StringTypeHandler());
 assertEquals(simpleType.getTypeHandler(), new StringTypeHandler());
 }

 @Test
 public void testSimpleTypeWithStaticTyping() {
 Class<String> stringClass = String.class;
 SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, null, false);
 assertFalse(simpleType.isStatic());
 simpleType = new SimpleType(simpleType, null, null, null, null, null, true);
 assertTrue(simpleType.isStatic());
 }

 @Test
 public void testSimpleTypeWithNullArguments() {
 try {
 new SimpleType(null);
 fail("Should not be able to construct a SimpleType with a null class argument");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 new SimpleType(null, null);
 fail("Should not be able to construct a SimpleType with null bindings argument");
 } catch (IllegalArgumentException e) {
 // expected
 }
}

 @Test
 public void testSimpleTypeWithInvalidSuperclassArgument() {
 Class<String> stringClass = String.class;
 try {
 new SimpleType(stringClass, TypeBindings.emptyBindings(), new Object());
 fail("Should not be able to construct a SimpleType with an invalid superclass argument");
 } catch (IllegalArgumentException e) {
 // expected
 }
}

 @Test
 public void testSimpleTypeWithInvalidSuperinterfaceArgument() {
 Class<String> stringClass = String.class;
 Class<Comparable<String>> comparableStringClass = Comparable.class.getGenericType();
 try {
 new SimpleType(stringClass, TypeBindings.emptyBindings(), null, new JavaType[] { new ClassId.UnknownType() });
 fail("Should not be able to construct a SimpleType with an invalid superinterface argument");
 } catch (IllegalArgumentException e) {
 // expected
 }
}

 @Test
 public void testSimpleTypeWithInvalidValueHandlerArgument() {
 Class<String> stringClass = String.class;
 try {
 new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, new Object(), null, false);
 fail("Should not be able to construct a SimpleType with an invalid value handler argument");
 } catch (IllegalArgumentException e) {
 // expected
 }
}

 @Test
 public void testSimpleTypeWithInvalidTypeHandlerArgument() {
 Class<String> stringClass = String.class;
 try {
 new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, new Object(), false);
 fail("Should not be able to construct a SimpleType with an invalid type handler argument");
 } catch (IllegalArgumentException e) {
 // expected
 }
}

 @Test
 public void testSimpleTypeWithInvalidAsStaticArgument() {
 Class<String> stringClass = String.class;
 try {
 new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, null, "invalid");
 fail("Should not be able to construct a SimpleType with an invalid asStatic argument");
 } catch (IllegalArgumentException e) {
 // expected
 }
}
}