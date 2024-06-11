import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.gson.Preconditions;
import com.google.gson.TypeUtils;
import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeToken.Builder;
import com.google.gson.reflect.TypeToken.Parameterized;

@RunWith(Parameterized.class)
public class TypeInfoFactoryTest {

  private Type type;
  private Class<?> rawClass;

  public TypeInfoFactoryTest(Type type, Class<?> rawClass) {
    this.type = type;
    this.rawClass = rawClass;
  }

  @Parameters
  public static Iterable<Object[]> data() {
    return new Iterator<Object[]>() {
      @Override
      public Object[] next() {
        Type type = TypeToken.get(int[].class).getType();
        Class<?> rawClass = int[].class;
        return new Object[] { type, rawClass };
      }
    }.asIterable();
  }

  @Test
  public void testTypeInfoForArray() {
    TypeInfoArray info = TypeInfoFactory.getTypeInfoForArray(type);
    assertNotNull(info);
    assertTrue(info instanceof TypeInfoArray);
  }

  @Test
  public void testTypeInfoForField() throws Exception {
    Builder builder = new TypeToken<Integer[]>() {}.where(new TypeNameFilter() {
      @Override
      public boolean matches(Type type, Class<?> rawType, Field field) {
        return TypeUtils.toRawClass(field.getType()).isAssignableFrom(rawType);
      }
    });

    Field field = rawClass.getField("foo");
    Type type = field.getGenericType();
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, type);
    assertNotNull(info);
    assertTrue(info instanceof TypeInfo);

    Type expectedType = builder.getType();
    assertEquals(expectedType, info.getType());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testUnsupportedFieldType() throws Exception {
    Field field = rawClass.getField("foo");
    field.setAccessible(true);
    Type type = new TypeToken<Object>() {}.getType();
    TypeInfoFactory.getTypeInfoForField(field, type);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUnknownType() {
    TypeInfoFactory.getTypeInfoForField(null, type);
  }
}

class TypeNameFilter implements TypeNameFilter {
  @Override
  public boolean matches(Type type, Class<?> rawType, Field field) {
    return TypeUtils.toRawClass(type).isAssignableFrom(rawType);
  }
}