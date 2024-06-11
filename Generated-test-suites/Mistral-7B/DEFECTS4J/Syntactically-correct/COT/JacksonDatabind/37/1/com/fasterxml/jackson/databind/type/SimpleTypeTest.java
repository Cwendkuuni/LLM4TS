import org.junit.Assert;
import org.junit.Test;

public class SimpleTypeTest {

 private Class<Object> simpleClass = Object.class;
 private SimpleType simpleType = null;

 @Test
 public void testConstructorWithClass() {
 simpleType = SimpleType.construct(simpleClass);
 Assert.assertEquals(simpleClass, simpleType.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithMap() {
 Class<Map<Integer, String>> mapClass = Map.class.getClassGenType();
 simpleType = SimpleType.construct(mapClass);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithCollection() {
 Class<List<Integer>> listClass = List.class.getClassGenType();
 simpleType = SimpleType.construct(listClass);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithArray() {
 Class<Integer[]> arrayClass = Integer[].class;
 simpleType = SimpleType.construct(arrayClass);
 }

 @Test
 public void testNarrowMethod() {
 Class<Integer> intClass = Integer.class;
 JavaType javaIntType = new SimpleType(intClass)._narrow(intClass);
 Assert.assertEquals(javaIntType, new SimpleType(intClass));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNarrowWithInvalidSubclass() {
 Class<String> stringClass = String.class;
 JavaType javaIntType = new SimpleType(Object.class)._narrow(stringClass);
 }

 @Test
 public void testIsContainerType() {
 Assert.assertFalse(simpleType.isContainerType());
 }

 @Test
 public void testToString() {
 Assert.assertEquals("[simple type, class " + SimpleType.class.getName() + "]", simpleType.toString());
 }

 @Test
 public void testEquals() {
 SimpleType simpleType2 = new SimpleType(simpleClass);
 Assert.assertEquals(simpleType, simpleType2);
 }

 // Edge cases and exception handling:
 @Test
 public void testNullClassArgument() {
 Assert.assertNull(SimpleType.construct(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBinderArgument() {
 simpleType = new SimpleType(simpleClass, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullSuperclassArgument() {
 simpleType = new SimpleType(simpleClass, new TypeBindings(), null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullSuperinterfacesArgument() {
 simpleType = new SimpleType(simpleClass, new TypeBindings(), null, (JavaType[]) null);
 }

 @Test
 public void testValueHandlerSetter() {
 Object handler = new Object();
 SimpleType simpleType3 = new SimpleType(simpleClass, new TypeBindings(), null, null, handler, null, false);
 Assert.assertEquals(handler, simpleType3.getValueHandler());
 }

 @Test(expected = NullPointerException.class)
 public void testValueHandlerNullSetter() {
 Object handler = null;
 SimpleType simpleType3 = new SimpleType(simpleClass, new TypeBindings(), null, null, null, null, false);
 simpleType3.setValueHandler(handler);
 }

 @Test
 public void testTypeHandlerSetter() {
 Object handler = new Object();
 SimpleType simpleType3 = new SimpleType(simpleClass, new TypeBindings(), null, null, null, handler, false);
 Assert.assertEquals(handler, simpleType3.getTypeHandler());
 }

 @Test(expected = NullPointerException.class)
 public void testTypeHandlerNullSetter() {
 Object handler = null;
 SimpleType simpleType3 = new SimpleType(simpleClass, new TypeBindings(), null, null, null, null, false);
 simpleType3.setTypeHandler(handler);
 }

 @Test
 public void testAsStaticSetter() {
 boolean asStatic = true;
 SimpleType simpleType3 = new SimpleType(simpleClass, new TypeBindings(), null, null, null, null, asStatic);
 Assert.assertEquals(asStatic, simpleType3.isStaticTyping());
 }
}