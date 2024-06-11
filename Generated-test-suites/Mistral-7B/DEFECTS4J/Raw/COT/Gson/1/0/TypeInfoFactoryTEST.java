import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoFactory;
import com.google.gson.TypeVariable;
import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class TypeInfoFactoryTest {

 private TypeInfoFactory typeInfoFactory;
 private Type type;
 private Class<?> clazz;

 @Parameters
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][]{
 // Add test cases with different types here
 });
 }

 public TypeInfoFactoryTest(Type type, Class<?> clazz) {
 this.type = type;
 this.clazz = clazz;
 }

 @Test
 public void testTypeInfoForArray() {
 TypeInfoArray expectedTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
 TypeInfo actualTypeInfo = typeInfoFactory.getTypeInfoForArray(type);
 assertEquals("Unexpected TypeInfo for Array", expectedTypeInfo, actualTypeInfo);
 }

 @Test
 public void testTypeInfoForField() throws NoSuchFieldException, IllegalAccessException {
 Field field = clazz.getDeclaredField("fieldName"); // Replace with actual field name
 Type expectedTypeInfo = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());
 TypeInfo actualTypeInfo = typeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());
 assertEquals("Unexpected TypeInfo for Field", expectedTypeInfo, actualTypeInfo);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetActualTypeWithIllegalArgument() {
 typeInfoFactory.getActualType(new Object(), null, null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetActualTypeWithUnsupportedOperation() {
 typeInfoFactory = new TypeInfoFactory(); // Instantiate the class, which is not allowed
 typeInfoFactory.getActualType(null, null, null);
 }

 @Test
 public void testGetActualTypeWithClass() {
 Type expectedType = TypeToken.of(clazz).getType();
 Type actualType = typeInfoFactory.getActualType(type, null, clazz);
 assertEquals("Unexpected Type", expectedType, actualType);
 }

 @Test
 public void testGetActualTypeWithParameterizedType() {
 TypeParameterized typeParameterized = TypeToken.getParameterized(List.class, String.class).getType();
 Type parentType = TypeToken.get(List.class).getType();
 Type[] expectedTypeArguments = new Type[]{String.class};
 Type rawParentClass = List.class;
 Type[] actualTypeArguments = typeInfoFactory.extractRealTypes(typeParameterized.getActualTypeArguments(), parentType, rawParentClass);
 assertEquals("Unexpected number of TypeArguments", expectedTypeArguments.length, actualTypeArguments.length);
 for (int i = 0; i < expectedTypeArguments.length; i++) {
 assertEquals("Unexpected TypeArgument", expectedTypeArguments[i], actualTypeArguments[i]);
 }
 }

 @Test
 public void testGetActualTypeWithGenericArrayType() {
 TypeGenericArray typeGenericArray = TypeToken.getParameterized(Character[].class).getType();
 Type componentType = new TypeToken<Character>(){}.getType();
 Type expectedType = new GenericArrayTypeImpl(componentType);
 Type actualType = typeInfoFactory.getActualType(typeGenericArray.getComponentType(), null, null);
 assertEquals("Unexpected Type for Generic Array", expectedType, actualType);
 }

 @Test
 public void testGetActualTypeWithTypeVariable() {
 TypeVariable<Integer> typeVariable = new TypeVariable<Integer>("T") {};
 TypeVariable<?>[] classTypeVariables = clazz.getTypeParameters();
 ParameterizedType objParameterizedType = new TypeToken<List<Integer>>(){}.getType();
 int index = TypeInfoFactory.getIndex(classTypeVariables, typeVariable);
 Type expectedType = TypeToken.of(Integer.class).getType();
 Type actualType = (Type) Arrays.asList(objParameterizedType.getActualTypeArguments())
 .get(index);
 assertEquals("Unexpected Type for TypeVariable", expectedType, actualType);
 }

 @Test
 public void testGetActualTypeWithWildcardType() {
 TypeWildcard superclass = new TypeToken<List<?>>(){}.getType();
 Type expectedType = TypeToken.of(List.class).getType();
 Type actualType = typeInfoFactory.getActualType(superclass.getUpperBounds()[0], null, null);
 assertEquals("Unexpected Type for WildcardType", expectedType, actualType);
 }

}