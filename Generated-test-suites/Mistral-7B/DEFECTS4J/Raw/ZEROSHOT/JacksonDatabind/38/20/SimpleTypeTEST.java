import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 Class<?> simpleClass = String.class;
 SimpleType simpleType1 = new SimpleType(simpleClass); // constructor 1
 SimpleType simpleType2 = new SimpleType(simpleClass, TypeBindings.emptyBindings(), null, null); // constructor 2
 SimpleType simpleType3 = new SimpleType(simpleType1); // copy constructor

 assertEquals(simpleClass, simpleType1.getClass());
 assertEquals(simpleType1, simpleType3);
 }

 @Test
 public void testInvalidTypeConstruction() {
 Class<?> mapClass = HashMap.class;
 Class<?> collectionClass = ArrayList.class;
 Class<?> arrayClass = String[].class;

 assertNull(SimpleType.construct(mapClass));
 assertNull(SimpleType.construct(collectionClass));
 assertNull(SimpleType.construct(arrayClass));
 }

 @Test
 public void testUnsafeConstruction() {
 Class<?> simpleClass = Integer.class;
 SimpleType simpleType = SimpleType.constructUnsafe(simpleClass);
 assertEquals(simpleClass, simpleType.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeprecatedConstructMethod() {
 Class<?> simpleClass = Integer.class;
 SimpleType simpleType = SimpleType.construct(simpleClass);
 }

 @Test
 public void testNarrowMethod() {
 SimpleType simpleType = new SimpleType(String.class);
 Class<?> stringSubclass = StringBuilder.class;
 SimpleType narrowedType = simpleType._narrow(stringSubclass);
 assertEquals(stringSubclass, narrowedType.getClass());
 }

 @Test
 public void testWithTypeHandlerMethod() {
 SimpleType simpleType = new SimpleType(String.class);
 Object handler = new Object();
 SimpleType typeWithHandler = simpleType.withTypeHandler(handler);
 assertNotSame(simpleType, typeWithHandler);
 assertEquals(handler, typeWithHandler.getTypeHandler());
 }

 @Test
 public void testRefineMethod() {
 SimpleType simpleType = new SimpleType(String[].class);
 assertNull(simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null));
 }

 @Test
 public void testBuildCanonicalNameMethod() {
 SimpleType simpleType = new SimpleType(String.class);
 assertEquals("java.lang.String", simpleType.buildCanonicalName());
 }

 @Test
 public void testIsContainerTypeMethod() {
 SimpleType simpleType = new SimpleType(String.class);
 assertFalse(simpleType.isContainerType());
 }

 @Test
 public void testGetErasedSignatureMethod() {
 SimpleType simpleType = new SimpleType(String.class);
 StringBuilder builder = new StringBuilder();
 simpleType.getErasedSignature(builder);
 assertEquals("java.lang.String", builder.toString());
 }

 @Test
 public void testGetGenericSignatureMethod() {
 SimpleType simpleType = new SimpleType(List.class);
 StringBuilder builder = new StringBuilder();
 simpleType.getGenericSignature(builder);
 assertEquals("java.util.List<T>", builder.toString());
 }
}