import static org.junit.Assert.*;
import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoImpl;
import com.google.gson.TypeVariableImpl;
import com.google.gson.ParameterizedTypeImpl;
import com.google.gson.GenericArrayTypeImpl;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

public class TypeInfoFactoryTest {

 private static Type getTestType(Class<?> clazz) {
 return new ParameterizedTypeImpl(clazz, new Type[] { String.class, Integer.class });
 }

 @org.junit.Test
 public void testTypeInfoForArray() {
 Type type = new int[0].getClass();
 TypeInfoArray info = TypeInfoFactory.TypeInfoForArray(type);
 assertEquals(TypeInfo.ARRAY, info.type);
 assertNotNull(info.elementType);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testTypeInfoForArrayNull() {
 TypeInfoFactory.TypeInfoForArray(null);
 }

 @org.junit.Test
 public void testTypeInfoForField() throws NoSuchFieldException {
 Class<TestClass> clazz = TestClass.class;
 Field field = clazz.getField("stringField");
 Type fieldType = field.getGenericType();
 Type actualType = TypeInfoFactory.getActualType(fieldType, clazz, clazz);
 TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());
 assertEquals(actualType, info.type);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testTypeInfoForFieldNull() throws NoSuchFieldException {
 Field field = null;
 TypeInfoFactory.getTypeInfoForField(field, TestClass.class.getGenericSuperclass());
 }

 @org.junit.Test
 public void testGetActualTypeClass() {
 Type type = String.class;
 Type actualType = TypeInfoFactory.getActualType(type, null, null);
 assertEquals(String.class, actualType);
 }

 @org.junit.Test
 public void testGetActualTypeParameterizedType() {
 Type parentType = getTestType(TestClass.class);
 Type typeToEvaluate = new TypeVariableImpl("T");
 Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, TestClass.class);
 assertEquals(String.class, actualType);
 }

 @org.junit.Test
 public void testGetActualTypeGenericArrayType() {
 Type parentType = getTestType(TestClass.class);
 Type componentType = Integer.TYPE;
 Type genericArrayType = new GenericArrayTypeImpl(componentType);
 Type actualType = TypeInfoFactory.getActualType(genericArrayType, parentType, TestClass.class);
 assertEquals(genericArrayType, actualType);
 }

 @org.junit.Test
 public void testGetActualTypeTypeVariable() {
 Class<TestClass> clazz = TestClass.class;
 TypeVariable<Integer> typeVariable = clazz.getTypeParameters().get(0);
 Type actualType = TypeInfoFactory.getActualType(typeVariable, getTestType(clazz), clazz);
 assertEquals(Integer.class, actualType);
 }

 @org.junit.Test
 public void testGetActualTypeWildcardType() {
 Class<TestClass> clazz = TestClass.class;
 Type parentType = getTestType(clazz);
 Type wildcardType = new WildcardType(null, new Type[] { String.class });
 Type actualType = TypeInfoFactory.getActualType(wildcardType, parentType, clazz);
 assertEquals(String.class, actualType);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testGetActualTypeUnsupportedType() {
 Class<TestClass> clazz = TestClass.class;
 Type typeToEvaluate = new Object() {};
 Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, clazz);
 }

 @org.junit.Test
 public void testExtractRealTypes() {
 Type parentType = getTestType(TestClass.class);
 Type[] actualTypeArguments = new Type[] { String.class, Integer.class };
 Type[] realTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, TestClass.class);
 assertArrayEquals(actualTypeArguments, realTypes);
 }

 @org.junit.Test
 public void testGetIndex() {
 TypeVariable<Integer> intTypeVariable = new TypeVariable<>("I", new Type[]{String.class, Integer.class});
 TypeVariable<String> stringTypeVariable = new TypeVariable<>("S", new Type[]{String.class, Integer.class});
 TypeVariable<?>[] typeVariables = new TypeVariable<?>[]{intTypeVariable, stringTypeVariable};
 TypeVariable<?> testTypeVariable = intTypeVariable;
 int index = TypeInfoFactory.getIndex(typeVariables, testTypeVariable);
 assertEquals(0, index);
 }

}

class TestClass extends GenericType<TestClass, String, Integer> {
 String stringField;
 Integer intField;
}