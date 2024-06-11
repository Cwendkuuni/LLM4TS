import static org.junit.Assert.*;
import static java.lang.reflect.Modifier.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.GenericArrayType;

import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[3].getClass(); // primitive array
    TypeInfoArray infoArray = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals(TypeInfo.ARRAY, infoArray.getType());
    assertEquals(int.class, infoArray.getComponentType());

    Type typeList = new ArrayList<Integer>().getClass(); // array list
    TypeInfoArray infoList = TypeInfoFactory.getTypeInfoForArray(typeList);
    assertEquals(TypeInfo.COLLECTION, infoArray.getType());
    assertEquals(Integer.class, infoArray.getComponentType());
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException, IllegalAccessException {
    Class<ExampleClass> exampleClass = ExampleClass.class;
    int fieldIndex = 0;
    Field field = exampleClass.getField("field" + fieldIndex);
    Type fieldType = field.getType();
    Type typeDefiningF = exampleClass;

    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
    assertEquals(TypeInfo.FIELD, info.getType());
    assertEquals(fieldType, info.getTypeValue());
  }

  @Test
  public void testGetActualType() throws InstantiationException, IllegalAccessException, NoSuchFieldException {
    Class<ExampleClass> exampleClass = ExampleClass.class;
    int fieldIndex = 0;
    Field field = exampleClass.getField("field" + fieldIndex);
    Type fieldType = field.getType();
    Type typeDefiningF = exampleClass;
    Type actualType = TypeInfoFactory.getActualType(fieldType, parentType, rawParentClass);
    assertEquals(int.class, actualType);

    TypeVariable<Integer> typeVariable = TypeVariable.of(Integer.TYPE);
    TypeParameterized typeParameterized = new TypeParameterized(int[].class, typeVariable);
    Type parentType = typeParameterized.getType();
    Type rawParentClass = int[].class;

    // Test with a TypeVariable
    Type info = TypeInfoFactory.getActualType(typeVariable, parentType, rawParentClass);
    assertEquals(int.class, info);
  }
}