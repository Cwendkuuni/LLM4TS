import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.reflect.TypeUtils;
import com.google.gson.reflect.TypeVariableImpl;
import com.google.gson.reflect.WildcardTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    // Test creating a TypeInfoArray for a simple Java Array type.
    Type intType = int[].class;
    TypeInfoArray intTypeInfoArray = TypeInfoFactory.getTypeInfoForArray(intType);
    assertEquals(TypeInfoArray.class, intTypeInfoArray.getClass());
    assertEquals(intType, intTypeInfoArray.getType());

    // Test creating a TypeInfoArray for a generic Java Array type.
    TypeList<Integer> intListType = new TypeToken<List<Integer>>() {}.getType();
    TypeInfoArray<List<Integer>> intListTypeInfoArray =
        TypeInfoFactory.getTypeInfoForArray(intListType);
    assertEquals(TypeInfoArray.class, intListTypeInfoArray.getClass());
    assertEquals(intListType, intListTypeInfoArray.getType());
  }

  @Test
  public void testTypeInfoForField() {
    // Test creating a TypeInfo for a simple field.
    Field field = new Field() {
      public Type getGenericType() {
        return String.class;
      }

      public Type getType() {
        return String.class;
      }

      public Class<?> getDeclaringClass() {
        return Object.class;
      }
    };
    Type parentType = Object.class;
    TypeInfo stringTypeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(TypeInfo.class, stringTypeInfo.getClass());
    assertEquals(String.class, stringTypeInfo.getType());

    // Test creating a TypeInfo for a generic field.
    Field genericField = new Field() {
      public Type getGenericType() {
        return new TypeToken<List<String>>() {}.getType();
      }

      public Type getType() {
        return List.class;
      }

      public Class<?> getDeclaringClass() {
        return Object.class;
      }
    };
    Type parentTypeWithGenericType = new TypeToken<List<String>>() {}.getType();
    TypeInfo listStringTypeInfo = TypeInfoFactory.getTypeInfoForField(genericField, parentTypeWithGenericType);
    assertEquals(TypeInfo.class, listStringTypeInfo.getClass());
    assertEquals(List.class, listStringTypeInfo.getType());
  }

  // Test cases for edge cases and exception handling scenarios will be added here.

  @Test
  public void testNullTypeArgument() {
    // Test creating TypeInfoArray with null Type argument.
    Type nullType = null;
    assertNull(TypeInfoFactory.getTypeInfoForArray(nullType));

    // Test creating TypeInfo with null Field argument and null parentType.
    Field nullField = mock(Field.class);
    when(nullField.getGenericType()).thenReturn(null);
    when(nullField.getType()).thenReturn(null);
    when(nullField.getDeclaringClass()).thenReturn(null);
    assertNull(TypeInfoFactory.getTypeInfoForField(nullField, null));
  }

  @Test
  public void testInvalidType() {
    // Test creating TypeInfoArray with non-array types.
    Type invalidType = String.class;
    assertNotNull(TypeInfoFactory.getTypeInfoForArray(invalidType));
    // This test case will fail as TypeInfoForArray expects an Array type.

    // Test creating TypeInfo with non-field types.
    Type invalidTypeForField = String.class;
    assertNull(TypeInfoFactory.getTypeInfoForField(null, invalidTypeForField));
    // This test case will fail as TypeInfoForField expects a Field type.
  }

  @Test
  public void testTypeVariable() {
    // Test creating TypeInfoArray with generic types containing TypeVariables.
    TypeList<List<A>> listOfAListType = new TypeToken<List<List<A>>>() {}.getType();
    TypeInfoArray<List<List<A>>> listOfAListTypeInfoArray =
        TypeInfoFactory.getTypeInfoForArray(listOfAListType);
    assertEquals(TypeInfoArray.class, listOfAListTypeInfoArray.getClass());
    assertEquals(listOfAListType, listOfAListTypeInfoArray.getType());

    // Test creating TypeInfo with generic fields containing TypeVariables.
    TypeVariable<A> aTypeVariable = new TypeVariable<A>("A") {
      {
        setBound(Integer.class);
      }
    };
    Field genericFieldWithTypeVariable = new Field() {
      public Type getGenericType() {
        return new TypeToken<List<A>>() {}.getType();
      }

      public Type getType() {
        return List.class;
      }

      public Class<?> getDeclaringClass() {
        return Object.class;
      }
    };
    Type parentTypeWithTypeVariable = new TypeToken<List<List<A>>>(){}.getType();
    TypeInfo listStringTypeInfoWithTypeVariable =
        TypeInfoFactory.getTypeInfoForField(genericFieldWithTypeVariable, parentTypeWithTypeVariable);
    assertEquals(TypeInfo.class, listStringTypeInfoWithTypeVariable.getClass());
    assertEquals(List.class, listStringTypeInfoWithTypeVariable.getType());
    assertEquals(Integer.class, listStringTypeInfoWithTypeVariable.getTypeVariable("A").getBound());
  }

  @Test
  public void testWildcardType() {
    // Test creating TypeInfoArray with generic types containing WildcardTypes.
    TypeList<? extends List<Number>> listOfNumberListType =
        new TypeToken<List<? extends List<Number>>>() {}.getType();
    TypeInfoArray<? extends List<List<Number>>> listOfNumberListTypeInfoArray =
        TypeInfoFactory.getTypeInfoForArray(listOfNumberListType);
    assertEquals(TypeInfoArray.class, listOfNumberListTypeInfoArray.getClass());
    assertEquals(listOfNumberListType, listOfNumberListTypeInfoArray.getType());

    // Test creating TypeInfo with generic fields containing WildcardTypes.
    TypeList<? extends List<Number>> wildcardListType =
        new TypeToken<List<? extends List<Number>>>() {}.getType();
    Field genericFieldWithWildcardType = new Field() {
      public Type getGenericType() {
        return wildcardListType;
      }

      public Type getType() {
        return List.class;
      }

      public Class<?> getDeclaringClass() {
        return Object.class;
      }
    };
    Type parentTypeWithWildcardType = new TypeToken<List<List<Number>>>() {}.getType();
    TypeInfo listStringTypeInfoWithWildcardType =
        TypeInfoFactory.getTypeInfoForField(genericFieldWithWildcardType, parentTypeWithWildcardType);
    assertEquals(TypeInfo.class, listStringTypeInfoWithWildcardType.getClass());
    assertEquals(List.class, listStringTypeInfoWithWildcardType.getType());
    assertEquals(Collections.emptyList(), listStringTypeInfoWithWildcardType.getTypeArguments());
  }

  @Test
  public void testCyclicReferences() {
    // Test creating TypeInfoArray with cyclic references in the type hierarchy.
    TypeA typeA = new TypeA();
    TypeB typeB = new TypeB();
    TypeInfoArray<List<TypeA>> listOfATypeInfoArray =
        TypeInfoFactory.getTypeInfoForArray(new TypeToken<List<TypeA>>() {}.getType());
    assertEquals(TypeInfoArray.class, listOfATypeInfoArray.getClass());
    assertEquals(new TypeToken<List<TypeA>>() {}.getType(), listOfATypeInfoArray.getType());
    assertNotNull(listOfATypeInfoArray.getTypeArguments());
    TypeInfo<TypeB> typeBTypeInfo = TypeInfoFactory.getTypeInfoForField(typeB.getFieldA(), typeA.getClass());
    List<TypeInfo> typeInfos = listOfATypeInfoArray.getTypeArguments();
    assertEquals(TypeInfo.class, typeInfos.get(0).getClass());
    assertEquals(typeBTypeInfo, typeInfos.get(0).getType());

    // Test creating TypeInfo with cyclic references in the type hierarchy.
    TypeInfo<TypeA> typeATypeInfo = TypeInfoFactory.getTypeInfoForField(typeA.getFieldB(), typeB.getClass());
    assertEquals(TypeInfo.class, typeATypeInfo.getClass());
    assertEquals(typeA.getClass(), typeATypeInfo.getType());
    TypeInfo<TypeB> typeBTypeInfoFromTypeA = TypeInfoFactory.getTypeInfoForField(typeB.getFieldA(), typeA.getClass());
    assertEquals(typeBTypeInfoFromTypeA, typeATypeInfo.getTypeVariable("B").getBound());
  }

  @Test
  public void testRawAndGenericClasses() {
    // Test creating TypeInfoArray with raw and generic classes.
    Type rawListType = List.class;
    TypeInfoArray<List<String>> listOfStringTypeInfoArray =
        TypeInfoFactory.getTypeInfoForArray(new TypeToken<List<String>>() {}.getType());
    assertEquals(TypeInfoArray.class, listOfStringTypeInfoArray.getClass());
    assertEquals(rawListType, listOfStringTypeInfoArray.getType());

    // Test creating TypeInfo with raw and generic classes.
    Type rawStringType = String.class;
    Field rawStringField = new Field() {
      public Type getGenericType() {
        return null;
      }

      public Type getType() {
        return rawStringType;
      }

      public Class<?> getDeclaringClass() {
        return Object.class;
      }
    };
    Type parentTypeWithRawClass = rawStringType.getClass();
    TypeInfo rawStringTypeInfo = TypeInfoFactory.getTypeInfoForField(rawStringField, parentTypeWithRawClass);
    assertEquals(TypeInfo.class, rawStringTypeInfo.getClass());
    assertEquals(rawStringType, rawStringTypeInfo.getType());

    // Test creating TypeInfo with raw and generic classes for a generic field.
    TypeParameterized<String> genericStringType = new TypeParameterized<String>(String.class) {};
    Field genericStringField = new Field() {
      public Type getGenericType() {
        return new TypeToken<List<String>>() {}.getType();
      }

      public Type getType() {
        return genericStringType;
      }

      public Class<?> getDeclaringClass() {
        return Object.class;
      }
    };
    Type parentTypeWithGenericClass = new TypeToken<List<String>>() {}.getType();
    TypeInfo genericStringTypeInfo = TypeInfoFactory.getTypeInfoForField(genericStringField, parentTypeWithGenericClass);
    assertEquals(TypeInfo.class, genericStringTypeInfo.getClass());
    assertEquals(genericStringType, genericStringTypeInfo.getType());
    assertEquals(String.class, genericStringTypeInfo.getTypeVariable("T").getBound());
  }
}