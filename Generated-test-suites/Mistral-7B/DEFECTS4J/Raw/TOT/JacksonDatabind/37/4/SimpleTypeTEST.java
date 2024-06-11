import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimpleTypeTest {

 @Test
 public void testConstructorWithClass() {
 Class<?> typeClass = String.class;
 SimpleType simpleType = new SimpleType(typeClass);
 assertEquals(typeClass, simpleType.getRawClass());
 }

 @Test
 public void testConstructorWithTypeBindingsAndClass() {
 Class<?> typeClass = String.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 SimpleType simpleType = new SimpleType(typeClass, bindings);
 assertEquals(typeClass, simpleType.getRawClass());
 }

 @Test
 public void testConstructorWithTypeBindingsAndSuperClassAndSuperInterfaces() {
 Class<?> typeClass = String.class;
 TypeBindings bindings = TypeBindings.emptyBindings();
 JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
 JavaType[] superInterfaces = new JavaType[] { TypeFactory.defaultInstance().constructType(Comparable.class) };
 SimpleType simpleType = new SimpleType(typeClass, bindings, superClass, superInterfaces);
 assertEquals(typeClass, simpleType.getRawClass());
 assertNotNull(simpleType.getSuperClass());
 assertEquals(superClass, simpleType.getSuperClass());
 assertNotNull(simpleType.getSuperInterfaces());
 assertEquals(superInterfaces.length, simpleType.getSuperInterfaces().length);
 }

 @Test
 public void testCopyConstructor() {
 SimpleType original = new SimpleType(String.class);
 SimpleType copy = new SimpleType(original);
 assertEquals(original, copy);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructUnsafeWithArray() {
 try {
 Class<?> typeClass = int[].class;
 SimpleType simpleType = SimpleType.constructUnsafe(typeClass);
 } catch (IllegalArgumentException e) {
 assertEquals("Can not construct SimpleType for an array (class: [I)", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithMap() {
 try {
 Class<?> typeClass = HashMap.class;
 SimpleType simpleType = SimpleType.construct(typeClass);
 } catch (IllegalArgumentException e) {
 assertEquals("Can not construct SimpleType for a Map (class: java.util.HashMap)", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithCollection() {
 try {
 Class<?> typeClass = ArrayList.class;
 SimpleType simpleType = SimpleType.construct(typeClass);
 } catch (IllegalArgumentException e) {
 assertEquals("Can not construct SimpleType for a Collection (class: java.util.ArrayList)", e.getMessage());
 }
 }

 @Test
 public void testNarrow() {
 SimpleType stringType = new SimpleType(String.class);
 SimpleType stringSubclassType = new SimpleType(StringBuilder.class);
 SimpleType narrowedType = stringType.narrow(StringBuilder.class);
 assertEquals(stringSubclassType, narrowedType);
 }

 @Test
 public void testWitContentType() {
 try {
 SimpleType simpleType = new SimpleType(String.class);
 simpleType.withContentType(IntegerType.IntType);
 } catch (IllegalArgumentException e) {
 assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
 }
 }

 @Test
 public void testWithTypeHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 Object handler = new Object();
 SimpleType handlerType = simpleType.withTypeHandler(handler);
 assertNotNull(handlerType.getTypeHandler());
 assertEquals(handler, handlerType.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 try {
 SimpleType simpleType = new SimpleType(String.class);
 simpleType.withContentTypeHandler(new Object());
 } catch (IllegalArgumentException e) {
 assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
 }
 }

 @Test
 public void testWithValueHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 Object handler = new Object();
 SimpleType handlerType = simpleType.withValueHandler(handler);
 assertNotNull(handlerType.getValueHandler());
 assertEquals(handler, handlerType.getValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType staticType = simpleType.withStaticTyping();
 assertTrue(staticType.isStatic());
 }

 @Test
 public void testRefine() {
 try {
 SimpleType simpleType = new SimpleType(String.class);
 Class<?> refinedType = Integer.class;
 JavaType[] superInterfaces = new JavaType[] { TypeFactory.defaultInstance().constructType(Comparable.class) };
 JavaType refined = simpleType.refine(refinedType, null, null, superInterfaces);
 assertNull(refined);
 } catch (IllegalArgumentException e) {
 assertEquals("Simple types means something not-specialized, so: null", e.getMessage());
 }
 }
}