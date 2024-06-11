import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoImpl;
import com.google.gson.ParameterizedTypeImpl;
import com.google.gson.GenericArrayTypeImpl;
import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class TypeInfoFactoryTest {

 @Test
 public void testTypeInfoForArray() {
 Type type = new int[3].getClass();
 TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
 TypeInfo expectedTypeInfo = new TypeInfoArray(TypeUtils.wrapWithArray(int[].class));
 assertEquals(expectedTypeInfo, actualTypeInfo);
 }

 @Test
 public void testTypeInfoForField() throws NoSuchFieldException, IllegalAccessException {
 Class<?> clazz = new Foo<Integer>().getClass();
 Field field = clazz.getDeclaredField("intField");
 field.setAccessible(true);
 Type type = field.getGenericType();
 Type definingType = Foo.class;
 Type actualType = TypeInfoFactory.getActualType(type, definingType, Foo.class);
 TypeInfo expectedTypeInfo = new TypeInfo(TypeToken.of(Integer.class).getType());
 assertEquals(expectedTypeInfo, TypeInfoFactory.getTypeInfoForField(field, definingType));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testTypeInfoForFieldUnsupported() throws NoSuchFieldException, IllegalAccessException {
 Class<?> clazz = new Foo<List<Integer>>().getClass();
 Field field = clazz.getDeclaredField("rawListField");
 field.setAccessible(true);
 Type type = field.getGenericType();
 Type definingType = Foo.class;
 TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, definingType);
 fail("Expected UnsupportedOperationException but got " + actualTypeInfo);
 }

 @Test
 public void testGetActualTypeArray() {
 Type type = new int[3].getClass();
 Type actualType = TypeInfoFactory.getActualType(type, null, null);
 assertEquals(int[].class, actualType);
 }

 @Test
 public void testGetActualTypeParameterizedType() {
 Type type = new TypeToken<List<Integer>>() {}.getType();
 Type actualType = TypeInfoFactory.getActualType(type, null, null);
 assertEquals(List.class, actualType);
 }

 @Test
 public void testGetActualTypeGenericArrayType() {
 Type type = new TypeToken<int[]>() {}.getType();
 Type actualType = TypeInfoFactory.getActualType(type, null, null);
 assertEquals(int[].class, actualType);
 }

 @Test
 public void testGetActualTypeTypeVariable() {
 Type definingType = new Foo<Integer>().getClass();
 Type type = definingType.getGenericSuperclass();
 TypeVariable<?> typeVariable = definedType.getTypeParameters()[0];
 Type actualType = TypeInfoFactory.getActualType(typeVariable, type, definingType);
 assertEquals(TypeToken.of(Integer.class).getType(), actualType);
 }

 @Test
 public void testGetActualTypeWildcardType() {
 Type type = TypeToken.get(List.class).getType();
 Type wildcardType = TypeToken.getParameterized(List.class, Object.class).getType();
 Type actualType = TypeInfoFactory.getActualType(wildcardType, type, null);
 assertEquals(List.class, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetActualTypeIllegalArgumentException() {
 Type type = new Object()
 .getClass()
 .getGenericSuperclass();
 Type actualType = TypeInfoFactory.getActualType(type, null, null);
 fail("Expected IllegalArgumentException but got " + actualType);
 }
}

class Foo<A> {
 int intField;
 List<A> rawListField;
 A a;
}