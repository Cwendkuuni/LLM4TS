import com.google.gson.TypeInfoFactory;
import com.google.gson.TypeInfo;
import com.google.gson.util.TypeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class TypeInfoFactoryTest {

 @Test
 public void testTypeInfoForArray() {
 Type intArrayType = int[].class;
 TypeInfo expectedTypeInfo = new TypeInfoArray(intArrayType);
 TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(intArrayType);
 assertEquals(expectedTypeInfo, actualTypeInfo);
 }

 @Test
 public void testTypeInfoForField() throws NoSuchFieldException, IllegalAccessException {
 Class<?> ListClass = java.util.List.class;
 Type elementType = java.lang.Integer.TYPE;
 Field field = ListClass.getField("list");
 Type fieldType = field.getGenericType();
 Type typeDefiningF = ListClass;
 Type expectedTypeInfoType = new TypeInfo(elementType);
 TypeInfo actualTypeInfoType = TypeInfoFactory.getTypeInfoForField(field, fieldType, typeDefiningF);
 assertEquals(expectedTypeInfoType, actualTypeInfoType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidType() {
 Type intType = int.class;
 try {
 TypeInfoFactory.getTypeInfoForArray(intType);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGenericArrayType() {
 Type intArrayType = int[].class;
 Type expectedTypeInfo = TypeInfoFactory.getTypeInfoForArray(intArrayType);
 TypeInfo actualTypeInfo = new TypeInfoArray(intArrayType);
 assertEquals(expectedTypeInfo, actualTypeInfo);
 }

 @Test
 public void testTypeVariable() throws NoSuchFieldException, IllegalAccessException {
 Class<?> FooClass = new Class<Foo>() {
 public static class Foo<A> {
 A a;
 }
 }.getDeclaredComponentType();
 Type variableType = FooClass.getTypeParameters()[0];
 Type definingClassType = FooClass;
 Type expectedTypeInfo = new TypeInfo(java.lang.Integer.TYPE);
 TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(
 new Field("a", new TypeVariable<Integer>() {}, FooClass), null, definingClassType);
 assertEquals(expectedTypeInfo, actualTypeInfo);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testMissingParameterizedType() throws NoSuchFieldException, IllegalAccessException {
 Class<?> FooClass = new Class<Foo>() {
 public static class Foo<A> {
 A a;
 }
 }.getDeclaredComponentType();
 Type variableType = FooClass.getTypeParameters()[0];
 Field field = FooClass.getDeclaredField("a");
 Type fieldType = field.getGenericType();
 try {
 TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, fieldType, FooClass);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }
}