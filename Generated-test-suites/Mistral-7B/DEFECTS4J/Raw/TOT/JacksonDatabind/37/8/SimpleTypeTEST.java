import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 Class<?> simpleClass = String.class;
 SimpleType simpleType = new SimpleType(simpleClass);
 JavaType obtainedType = TypeFactory.defaultInstance().constructType(simpleType.toString());
 assertEquals(simpleClass, obtainedType.getRawClass());
 }

 @Test
 public void testSimpleTypeBinding() {
 Class<?> simpleClass = String[].class;
 SimpleType simpleType = new SimpleType(simpleClass, TypeBindings.mapType(String.class, Integer.class));
 JavaType obtainedType = TypeFactory.defaultInstance().constructType(simpleType.toString());
 TypeBindings bindings = obtainedType.getTypeBindings();
 assertNotNull(bindings);
 assertEquals(TypeBindings.class, bindings.getClass());
 assertEquals(String.class, bindings.getBindings().get(0).getKey());
 assertEquals(Integer.class, bindings.getBindings().get(0).getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeInvalidConstruction() {
 Class<?> mapClass = Map.class;
 SimpleType simpleType = new SimpleType(mapClass);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeInvalidCopyConstruction() {
 TypeBase invalidBase = new Object();
 SimpleType simpleType = new SimpleType(invalidBase);
 }

 @Test
 public void testSimpleTypeToString() {
 Class<?> simpleClass = String.class;
 SimpleType simpleType = new SimpleType(simpleClass);
 assertEquals("[simple type, class " + simpleClass.getName() + "]", simpleType.toString());
 }

 @Test
 public void testSimpleTypeEquals() {
 Class<?> simpleClass1 = String.class;
 Class<?> simpleClass2 = String.class;
 SimpleType simpleType1 = new SimpleType(simpleClass1);
 SimpleType simpleType2 = new SimpleType(simpleClass2);
 SimpleType simpleType3 = simpleType1;
 SimpleType simpleType4 = new SimpleType(simpleClass1);
 assertTrue(simpleType1.equals(simpleType1));
 assertTrue(simpleType1.equals(simpleType3));
 assertTrue(simpleType1.equals(simpleType4));
 assertFalse(simpleType1.equals(null));
 assertFalse(simpleType1.equals("test"));
 }

 @Test
 public void testSimpleTypeNegativeCases() {
 Class<?> nullClass = null;
 SimpleType simpleType = new SimpleType(nullClass);
 assertNull(simpleType);
 }
}