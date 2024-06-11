import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeInfo;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;

import static com.google.common.truth.Truth.assertThat;

public class TypeInfoFactoryTest {

  private TypeInfoFactory typeInfoFactory;
  private Type testType;

  @Before
  public void setup() {
    typeInfoFactory = new TypeInfoFactory();
    testType = new TypeToken<int[][][]>() {}.getType();
  }

  @Test
  public void testTypeInfoForArray() {
    TypeInfoArray typeInfoArray = typeInfoFactory.TypeInfoArray.getTypeInfoForArray(testType);
    assertThat(typeInfoArray.getRawType()).isInstanceOf(int[].class);
    assertThat(typeInfoArray.getComponentType()).isInstanceOf(int[].class);
    assertThat(typeInfoArray.getDeepness()).isEqualTo(3);
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException {
    Class<?> testClass = new TestClass<>();
    Field field = testClass.getDeclaredField("integerField");
    Type type = field.getGenericType();
    TypeInfo typeInfo = typeInfoFactory.TypeInfo.getTypeInfoForField(field, testClass.getGenericType());
    assertThat(typeInfo.getType()).isInstanceOf(int.class);
  }

  @Test
  public void testGetActualType() throws NoSuchFieldException {
    Class<?> testClass = new TestClass<>();
    Field field = testClass.getDeclaredField("integerField");
    Type type = field.getGenericType();
    Type actualType = typeInfoFactory.getTypeInfoFactory().getActualType(type, testClass.getGenericType(), testClass);
    assertThat(actualType).isInstanceOf(int.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualTypeForUnsupportedType() throws NoSuchFieldException {
    Class<?> testClass = new TestClass<>();
    Field field = testClass.getDeclaredField("unsupportedField");
    Type type = field.getGenericType();
    typeInfoFactory.getTypeInfoFactory().getActualType(type, testClass.getGenericType(), testClass);
  }

  @Test
  public void testExtractRealTypes() {
    Type testType = new TypeToken<ArrayList<ArrayList<List<Integer>>>>() {}.getType();
    Type[] realTypes = typeInfoFactory.getTypeInfoFactory()
            .extractRealTypes(testType.getActualTypeArguments(), testType, ArrayList.class);
    Type[] expectedTypes = {List.class, List.class, Integer.class};
    assertThat(Arrays.asList(realTypes)).isEqualTo(Arrays.asList(expectedTypes));
  }

  @Test
  public void testGetIndex() {
    TypeVariable<Integer> testTypeVariable = new TestTypeVariable<>();
    Type[] types = new TypeVariable[]{new TestTypeVariable1<Integer>(), testTypeVariable, new TestTypeVariable2<Integer>()};
    int index = typeInfoFactory.getTypeInfoFactory().getIndex(types, testTypeVariable);
    assertThat(index).isEqualTo(1);
  }
}

class TestClass<T> {
  private int integerField;
  private T unsupportedField;
}

class TestTypeVariable<T> extends TypeVariable<T> {}

class TestTypeVariable1<T> extends TypeVariable<T> {}

class TestTypeVariable2<T> extends TypeVariable<T> {}