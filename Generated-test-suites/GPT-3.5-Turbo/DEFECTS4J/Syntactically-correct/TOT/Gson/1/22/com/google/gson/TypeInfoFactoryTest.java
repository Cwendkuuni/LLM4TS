import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class TypeInfoFactoryTest {

  private Field field;
  private Type type;

  @Before
  public void setUp() throws Exception {
    field = TypeInfoFactory.class.getDeclaredField("typeInfo");
    type = field.getGenericType();
  }

  @Test
  public void getTypeInfoForArray_typicalCase() {
    TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
    assertNotNull(typeInfoArray);
    assertEquals(type, typeInfoArray.getType());
  }

  @Test
  public void getTypeInfoForArray_edgeCase_emptyArray() {
    TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(new Object[0].getClass());
    assertNotNull(typeInfoArray);
    assertEquals(Object[].class, typeInfoArray.getType());
  }

  @Test
  public void getTypeInfoForField_typicalCase() {
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, TypeInfoFactory.class);
    assertNotNull(typeInfo);
    assertEquals(type, typeInfo.getType());
  }

  @Test
  public void getTypeInfoForField_typicalCase_typeVariable() {
    Field typeVariableField = TypeInfoFactory.class.getDeclaredField("typeVariableField");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(typeVariableField, TypeInfoFactory.class);
    assertNotNull(typeInfo);
    assertEquals(TypeVariable.class, typeInfo.getType());
  }

  @Test
  public void getTypeInfoForField_edgeCase_primitiveType() {
    Field primitiveField = TypeInfoFactory.class.getDeclaredField("primitiveField");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(primitiveField, TypeInfoFactory.class);
    assertNotNull(typeInfo);
    assertEquals(primitiveField.getGenericType(), typeInfo.getType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void getTypeInfoForField_errorCase_invalidType() {
    Field invalidField = TypeInfoFactory.class.getDeclaredField("invalidField");
    TypeInfoFactory.getTypeInfoForField(invalidField, TypeInfoFactory.class);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void getTypeInfoForField_errorCase_missingTypeToken() {
    Field missingTypeTokenField = TypeInfoFactory.class.getDeclaredField("missingTypeTokenField");
    TypeInfoFactory.getTypeInfoForField(missingTypeTokenField, TypeInfoFactory.class);
  }
  
}