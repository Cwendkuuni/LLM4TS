import static org.junit.Assert.*;
import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoImpl;
import com.google.gson.TypeVariableImpl;
import com.google.gson.ParameterizedTypeImpl;
import com.google.gson.GenericArrayTypeImpl;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class TypeInfoFactoryTest {

 private static final TypeInfoFactory TYPE_INFO_FACTORY = new TypeInfoFactory();

 @Test
 public void testTypeInfoForArray() {
 Type type = new int[10].getClass();
 TypeInfoArray infoArray = TYPE_INFO_FACTORY.getTypeInfoForArray(type);
 assertEquals(TypeInfoArray.class, infoArray.getClass());
 assertEquals(int[].class, infoArray.getType());
 }

 @Test
 public void testTypeInfoForField_ClassType() {
 try {
 Field field = Integer.class.getDeclaredField("MIN_VALUE");
 Type typeDefiningF = field.getDeclaringClass();
 Type type = field.getType();
 TypeInfo typeInfo = TYPE_INFO_FACTORY.getTypeInfoForField(field, typeDefiningF);
 assertEquals(TypeInfo.class, typeInfo.getClass());
 assertEquals(int.class, typeInfo.getType());
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testTypeInfoForField_ParameterizedType() {
 try {
 Class<List<Integer>> clazz = List.class.getTypeName().asSubType(new TypeToken<List<Integer>>() {}.getType());
 Field field = clazz.getDeclaredField("size");
 Type typeDefiningF = field.getDeclaringClass();
 Type type = field.getGenericType();
 TypeInfo typeInfo = TYPE_INFO_FACTORY.getTypeInfoForField(field, typeDefiningF);
 assertEquals(TypeInfoImpl.class, typeInfo.getClass());
 assertEquals(int.class, typeInfo.getType());
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testTypeInfoForField_GenericArrayType() {
 try {
 Field field = String[].class.getDeclaredField("length");
 Type typeDefiningF = field.getDeclaringClass();
 Type type = field.getGenericType();
 TypeInfo typeInfo = TYPE_INFO_FACTORY.getTypeInfoForField(field, typeDefiningF);
 assertEquals(GenericArrayTypeImpl.class, typeInfo.getClass());
 assertEquals(String[].class, typeInfo.getType());
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testTypeInfoForField_TypeVariable() {
 try {
 Class<List<Integer>> clazz = List.class.getTypeName().asSubType(new TypeToken<List<Integer>>() {}.getType());
 Field field = clazz.getDeclaredField("size");
 Type typeDefiningF = field.getDeclaringClass();
 Type type = field.getGenericType();
 TypeVariable<?> typeVariable = (TypeVariable<?>) type.getComponentType();
 TypeInfo typeInfo = TYPE_INFO_FACTORY.getTypeInfoForField(field, typeDefiningF);
 assertEquals(TypeVariableImpl.class, typeInfo.getClass());
 assertEquals(Integer.TYPE, typeVariable);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testTypeInfoForField_UnsupportedType() {
 try {
 Field field = Integer.class.getDeclaredField("toString");
 Type typeDefiningF = field.getDeclaringClass();
 Type type = field.getGenericType();
 TYPE_INFO_FACTORY.getTypeInfoForField(field, typeDefiningF);
 fail("Should have thrown UnsupportedOperationException");
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testGetActualType_ClassType() {
 Type type = Integer.TYPE;
 Type actualType = TYPE_INFO_FACTORY.getActualType(type, null, null);
 assertEquals(Integer.TYPE, actualType);
 }

 @Test
 public void testGetActualType_ParameterizedType() {
 Type type = new TypeToken<List<Integer>>() {}.getType();
 Type actualType = TYPE_INFO_FACTORY.getActualType(type, null, null);
 assertEquals(List.class, actualType);
 }

 @Test
 public void testGetActualType_GenericArrayType() {
 Type type = new Type[] { String.class }.getClass();
 Type actualType = TYPE_INFO_FACTORY.getActualType(type, null, null);
 assertEquals(String[].class, actualType);
 }

 @Test
 public void testGetActualType_TypeVariable() {
 Type type = new TypeVariable<Integer>("i") {}.getType();
 Type actualType = TYPE_INFO_FACTORY.getActualType(type, null, null);
 assertEquals(Integer.TYPE, actualType);
 }

 @Test
 public void testGetActualType_WildcardType() {
 Type type = new Type[] { Collection.class }.getClass();
 Type actualType = TYPE_INFO_FACTORY.getActualType(new WildcardType(null, type), null, null);
 assertEquals(Collection.class, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetActualType_UnexpectedType() {
 Type type = new Object() {}.getClass();
 TYPE_INFO_FACTORY.getActualType(type, null, null);
 fail("Should have thrown IllegalArgumentException");
 }

 @Test
 public void testExtractRealTypes_ClassType() {
 Type type = new int[10].getClass();
 Type[] actualTypes = TYPE_INFO_FACTORY.extractRealTypes(new Type[] { type }, null, null);
 assertEquals(1, actualTypes.length);
 assertEquals(int[].class, actualTypes[0]);
 }

 @Test
 public void testExtractRealTypes_ParameterizedType() {
 Type type = new TypeToken<List<List<Integer>>>() {}.getType();
 Type[] actualTypes = TYPE_INFO_FACTORY.extractRealTypes(type.getActualTypeArguments(), null, null);
 assertEquals(2, actualTypes.length);
 assertEquals(List.class, actualTypes[0]);
 assertEquals(List.class, actualTypes[1]);
 }
}