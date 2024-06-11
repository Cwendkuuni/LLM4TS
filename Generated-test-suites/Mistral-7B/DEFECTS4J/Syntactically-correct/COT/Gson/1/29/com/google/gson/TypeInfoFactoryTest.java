import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoImpl;
import com.google.gson.ParameterizedTypeImpl;
import com.google.gson.GenericArrayTypeImpl;
import com.google.gson.TypeVariable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type arrayType = int[].class.getTypeParameters()[0];
    TypeInfoArray typeInfoArray = TypeInfoFactory.TypeInfoForArray(arrayType);
    assertEquals(TypeInfo.ARRAY, typeInfoArray.type);
    assertEquals(int.class, typeInfoArray.componentType);
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException {
    Class<Foo> clazz = Foo.class;
    Field field = clazz.getDeclaredField("bar");
    Type fieldType = field.getGenericType();
    Type actualType = TypeInfoFactory.getActualType(fieldType, clazz.getTypeParameters()[0], clazz);
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, clazz.getTypeParameters()[0]);
    assertEquals(actualType.getTypeName(), typeInfo.typeName);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForFieldWithInvalidParentType() throws NoSuchFieldException {
    Class<Foo> clazz = Foo.class;
    Field field = clazz.getDeclaredField("bar");
    Type fieldType = field.getGenericType();
    Type actualType = TypeInfoFactory.getActualType(fieldType, clazz, Object.class);
  }

  @Test
  public void testGetActualTypeForClass() {
    Type actualType = TypeInfoFactory.getActualType(Integer.TYPE, null, Integer.class);
    assertEquals(Integer.class, actualType);
  }

  @Test
  public void testGetActualTypeForParameterizedType() {
    TypeParameterizedType type = new TypeParameterizedType() {
      @Override
      public Type getRawType() {
        return List.class;
      }

      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] { Integer.class };
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };
    Type actualType = TypeInfoFactory.getActualType(type, null, Object.class);
    assertTrue(actualType instanceof ParameterizedTypeImpl);
    assertEquals(List.class, ((ParameterizedTypeImpl) actualType).getRawType());
    assertEquals(Integer.class, ((ParameterizedTypeImpl) actualType).getActualTypeArguments()[0]);
  }

  @Test
  public void testGetActualTypeForGenericArrayType() {
    Type genericArrayType = int[].class.getTypeParameters()[0];
    Type actualType = TypeInfoFactory.getActualType(genericArrayType, null, Object.class);
    assertTrue(actualType instanceof GenericArrayTypeImpl);
    assertEquals(int.class, ((GenericArrayTypeImpl) actualType).getComponentType());
  }

  @Test
  public void testGetActualTypeForTypeVariable() {
    TypeVariable<String> typeVariable = String.class.getTypeParameters()[0];
    Type actualType = TypeInfoFactory.getActualType(typeVariable, new Type[] { String.class }, String[].class);
    assertEquals(String.class, actualType);
  }

  @Test
  public void testGetActualTypeForWildcardType() {
    Type wildcardType = new WildcardType() {
      @Override
      public Type[] getUpperBounds() {
        return new Type[] { Object.class };
      }
    };
    Type actualType = TypeInfoFactory.getActualType(wildcardType, null, Object[].class);
    assertTrue(actualType instanceof TypeVariable<?>);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetActualTypeForInvalidType() {
    Type actualType = TypeInfoFactory.getActualType(new Object() {}, null, Object.class);
  }

  @Test
  public void testExtractRealTypes() {
    Type type = new TypeParameterizedType() {
      @Override
      public Type getRawType() {
        return List.class;
      }

      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] { Integer.class, String.class };
      }

      @Override
      public Type getOwnerType() {
        return null;
      }
    };
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(type.getActualTypeArguments(), null, Object.class);
    assertEquals(actualTypes.length, 2);
    assertEquals(Integer.class, actualTypes[0]);
    assertEquals(String.class, actualTypes[1]);
  }

  @Test
  public void testGetIndex() {
    TypeVariable<String>[] typeVariables = new TypeVariable<String>[] { new TypeVariable<String>(), new TypeVariable<String>() };
    TypeVariable<String> typeVariable = new TypeVariable<String>() {};
    int index = TypeInfoFactory.getIndex(typeVariables, typeVariable);
    assertEquals(index, 1);
  }

  static class Foo {
    TypeVariable<String> bar;
  }
}