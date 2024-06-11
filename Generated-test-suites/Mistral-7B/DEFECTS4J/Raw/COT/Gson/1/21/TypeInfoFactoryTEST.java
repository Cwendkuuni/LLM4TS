import static org.junit.Assert.*;
import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoImpl;
import com.google.gson.TypeInfoVariable;
import com.google.gson.TypeVariable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypeInfoFactoryTest {

 private static final Class<TypeInfoFactory> INFO_FACTORY_CLASS = TypeInfoFactory.class;

 @org.junit.Test
 public void testTypeInfoForArray() {
 Type type = new int[3].getClass();
 TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
 assertInstanceOf(TypeInfoArray.class, typeInfoArray);
 }

 @org.junit.Test
 public void testTypeInfoForField_SimpleType() throws NoSuchFieldException {
 Class<Integer> integerClass = Integer.class;
 Field field = integerClass.getDeclaredField("value");
 Type typeDefiningF = integerClass;
 Type type = field.getGenericType();
 TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
 assertInstanceOf(TypeInfoVariable.class, typeInfo);
 }

 @org.junit.Test
 public void testTypeInfoForField_ParameterizedType() throws NoSuchFieldException {
 Class<List<Integer>> listClass = List.class.getTypeParameters()[0].getRawType();
 Field listField = List.class.getDeclaredField("list");
 Type typeDefiningF = List.class;
 Type type = listField.getGenericType();
 Type parentType = List.class;
 Class<?> rawParentClass = List.class;
 TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(listField, typeDefiningF, parentType);
 assertInstanceOf(TypeInfoImpl.class, typeInfo);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testTypeInfoForField_IllegalArgumentException() throws NoSuchFieldException {
 Class<List<List<Integer>>> listListClass = List.class.getTypeParameters()[0].getType();
 Field listListField = List.class.getDeclaredField("list");
 Type typeDefiningF = List.class;
 Type type = listListField.getGenericType();
 Type parentType = List.class;
 Class<?> rawParentClass = List.class;
 TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(listListField, typeDefiningF, parentType);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testTypeInfoForField_UnsupportedOperationException() throws NoSuchFieldException {
 TypeVariable<Integer> integerTypeVariable = TypeVariable.of("I");
 Field field = new Object() {
 int i;
 }.getClass().getDeclaredField("i");
 Type typeDefiningF = Object.class;
 Type type = field.getGenericType();
 Type parentType = Object.class;
 Class<?> rawParentClass = Object.class;
 TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF, parentType);
 }

 @org.junit.Test
 public void testGetActualType_Class() {
 Type typeToEvaluate = Integer.TYPE;
 Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, null, null);
 assertEquals(Integer.TYPE, actualType);
 }

 @org.junit.Test
 public void testGetActualType_ParameterizedType() {
 ParameterizedType castedType = new ArrayList<Integer>().getClass().getGenericType();
 Type owner = castedType.getOwnerType();
 Type[] actualTypeParameters = TypeInfoFactory.extractRealTypes(castedType.getActualTypeArguments(), null, null);
 Type rawType = castedType.getRawType();
 TypeInfo actualTypeInfo = new TypeInfoImpl(rawType, actualTypeParameters, owner);
 Type expectedType = new TypeInfoImpl(ArrayList.class, new TypeInfoVariable[0], Object.class);
 assertEquals(expectedType, actualTypeInfo);
 }

 @org.junit.Test
 public void testGetActualType_GenericArrayType() {
 GenericArrayType castedType = new int[3].getClass();
 Type componentType = castedType.getGenericComponentType();
 Type actualType = TypeInfoFactory.getActualType(componentType, null, null);
 Type expectedType = Integer.TYPE;
 assertEquals(expectedType, actualType);
 }

 @org.junit.Test
 public void testGetActualType_TypeVariable() {
 TypeVariable<Integer> integerTypeVariable = TypeVariable.of("I");
 TypeVariable<?>[] classTypeVariables = Integer.class.getTypeParameters();
 ParameterizedType objParameterizedType = new ArrayList<Integer>().getClass().getTypeParameters()[0].getType();
 int indexOfActualTypeArgument = TypeInfoFactory.getIndex(classTypeVariables, integerTypeVariable);
 Type actualTypeArgument = (Type) objParameterizedType.getActualTypeArguments()[indexOfActualTypeArgument];
 Type expectedType = Integer.TYPE;
 assertEquals(expectedType, actualTypeArgument);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testGetActualType_WildcardType() {
 WildcardType castedType = new ArrayList<List<?>>().getType();
 Type upperBounds = castedType.getUpperBounds()[0];
 Type actualType = TypeInfoFactory.getActualType(upperBounds, null, null);
 }
}