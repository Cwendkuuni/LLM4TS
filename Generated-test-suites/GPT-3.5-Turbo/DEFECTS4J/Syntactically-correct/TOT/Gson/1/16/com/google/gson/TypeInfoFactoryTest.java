import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TypeInfoFactoryTest {
  
  private Field field;
  private Type type;
  private Type parentType;
  private Class<?> rawParentClass;

  @Before
  public void setUp() throws NoSuchFieldException {
    field = TypeInfoFactory.class.getDeclaredField("field");
    type = TypeInfoFactory.class.getDeclaredMethod("getTypeInfoForField", Field.class, Type.class).getGenericReturnType();
    parentType = TypeInfoFactory.class;
    rawParentClass = TypeInfoFactory.class;
  }

  @Test
  public void testGetTypeInfoForArray() {
    TypeInfoArray arrayType = TypeInfoFactory.getTypeInfoForArray(type);

    assertEquals(type, arrayType.getType());
    assertTrue(arrayType.getType().getClass().isArray());
  }
  
  @Test
  public void testGetTypeInfoForField_whenGenericArrayType() {
    Type genericArrayType = new TypeToken<List<String>[]>() {}.getType();
    TypeInfo fieldType = TypeInfoFactory.getTypeInfoForField(field, genericArrayType);

    assertEquals(genericArrayType, fieldType.getType());
    assertTrue(fieldType.getType().getClass().isArray());
  }

  @Test
  public void testGetTypeInfoForField_whenTypeIsClass() {
    TypeInfo fieldType = TypeInfoFactory.getTypeInfoForField(field, String.class);

    assertEquals(String.class, fieldType.getType());
    assertTrue(fieldType.getType().getClass().isInstance(String.class));
  }

  @Test
  public void testGetTypeInfoForField_whenParameterizedType() {
    Type parameterizedType = new TypeToken<List<String>>() {}.getType();
    TypeInfo fieldType = TypeInfoFactory.getTypeInfoForField(field, parameterizedType);

    assertEquals(parameterizedType, fieldType.getType());
    assertTrue(fieldType.getType() instanceof ParameterizedType);
  }

  @Test
  public void testGetTypeInfoForField_whenTypeVariable() {
    Type typeVariable = new TypeToken<List<String>>() {}.getType();
    TypeInfo fieldType = null;
    try {
      fieldType = TypeInfoFactory.getTypeInfoForField(field, typeVariable);
      fail("Expected UnsupportedOperationException to be thrown.");
    } catch (UnsupportedOperationException e) {
      assertEquals("Expecting parameterized type, got " + typeVariable + ".\n Are you missing the use of TypeToken idiom?\n See "
          + "http://sites.google.com/site/gson/gson-user-guide#TOC-Serializing-and-Deserializing-Gener", e.getMessage());
    } catch (Exception e) {
      fail("Expected UnsupportedOperationException to be thrown.");
    }
  }

  @Test
  public void testGetTypeInfoForField_whenWildcardType() {
    Type wildcardType = new TypeToken<List<?>>() {}.getType();
    TypeInfo fieldType = TypeInfoFactory.getTypeInfoForField(field, wildcardType);

    assertEquals(wildcardType, fieldType.getType());
    assertTrue(fieldType.getType().getClass().isInstance(wildcardType));
  }

  @Test
  public void testGetActualType_whenClassType() {
    Type classType = String.class;
    Type actualType = TypeInfoFactory.getActualType(classType, parentType, rawParentClass);

    assertEquals(classType, actualType);
    assertTrue(actualType.getClass().isInstance(classType));
  }

  @Test
  public void testGetActualType_whenParameterizedType() {
    Type parameterizedType = new TypeToken<List<String>>() {}.getType();
    Type actualType = TypeInfoFactory.getActualType(parameterizedType, parentType, rawParentClass);

    assertEquals(parameterizedType, actualType);
    assertTrue(actualType instanceof ParameterizedType);
  }

  @Test
  public void testGetActualType_whenGenericArrayType() {
    Type genericArrayType = new TypeToken<List<String>[]>() {}.getType();
    Type actualType = TypeInfoFactory.getActualType(genericArrayType, parentType, rawParentClass);

    assertEquals(genericArrayType, actualType);
    assertTrue(actualType.getClass().isArray());
  }

  @Test
  public void testGetActualType_whenTypeVariable() {
    Type typeVariable = new TypeToken<List<String>>() {}.getType();
    Type actualType = null;
    try {
      actualType = TypeInfoFactory.getActualType(typeVariable, parentType, rawParentClass);
      fail("Expected UnsupportedOperationException to be thrown.");
    } catch (UnsupportedOperationException e) {
      assertEquals("Expecting parameterized type, got " + parentType
          + ".\n Are you missing the use of TypeToken idiom?\n See "
          + "http://sites.google.com/site/gson/gson-user-guide#TOC-Serializing-and-Deserializing-Gener", e.getMessage());
    } catch (Exception e) {
      fail("Expected UnsupportedOperationException to be thrown.");
    }
  }

  @Test
  public void testGetActualType_whenWildcardType() {
    Type wildcardType = new TypeToken<List<?>>() {}.getType();
    Type actualType = TypeInfoFactory.getActualType(wildcardType, parentType, rawParentClass);

    assertEquals(wildcardType, actualType);
    assertTrue(actualType.getClass().isInstance(wildcardType));
  }

  @Test
  public void testExtractRealTypes_whenEmptyTypesArray() {
    Type[] actualTypeArguments = new Type[0];
    Type[] realTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);

    assertEquals(0, realTypes.length);
  }

  @Test
  public void testExtractRealTypes_whenNonEmptyTypesArray() {
    Type[] actualTypeArguments = new Type[]{String.class, Integer.class};
    Type[] realTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);

    assertEquals(2, realTypes.length);
    assertTrue(realTypes[0] instanceof Class<?>);
    assertTrue(realTypes[1] instanceof Class<?>);
  }

  @Test
  public void testGetIndex_whenTypeVariablePresent() {
    TypeVariable<?>[] types = rawParentClass.getTypeParameters();
    TypeVariable<?> typeVariable = types[0];
    int index = TypeInfoFactory.getIndex(types, typeVariable);

    assertEquals(0, index);
  }

  @Test
  public void testGetIndex_whenTypeVariableNotPresent() {
    TypeVariable<?>[] types = rawParentClass.getTypeParameters();
    TypeVariable<?> typeVariable = new TypeToken<List<String>>() {}.getType().getClass().getTypeParameters()[0];
    try {
      int index = TypeInfoFactory.getIndex(types, typeVariable);
      fail("Expected IllegalStateException to be thrown.");
    } catch (IllegalStateException e) {
      assertEquals("How can the type variable not be present in the class declaration!", e.getMessage());
    } catch (Exception e) {
      fail("Expected IllegalStateException to be thrown.");
    }
  }

}