import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    Type type = new Type() {
        @Override
        public String getTypeName() {
          return "MyArray";
        }
    };
    TypeInfoArray expected = new TypeInfoArray(type);
    TypeInfoArray actual = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetTypeInfoForField() throws NoSuchFieldException {
    Field f = TypeInfoFactory.class.getDeclaredField("typeDefiningF");
    Type typeDefiningF = f.getDeclaringClass();
    TypeInfo expected = new TypeInfo(typeDefiningF);
    TypeInfo actual = TypeInfoFactory.getTypeInfoForField(f, typeDefiningF);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetActualType() {
    Type typeToEvaluate = new Type() {
        @Override
        public String getTypeName() {
          return "ActualType";
        }
    };
    Type parentType = new Type() {
        @Override
        public String getTypeName() {
          return "ParentType";
        }
    };
    Class<?> rawParentClass = TypeInfoFactory.class;

    Type expected = typeToEvaluate;
    Type actual = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testExtractRealTypes() {
    Type[] actualTypeArguments = new Type[] {
        new Type() {
          @Override
          public String getTypeName() {
            return "ActualType1";
          }
        },
        new Type() {
          @Override
          public String getTypeName() {
            return "ActualType2";
          }
        }
    };
    Type parentType = new Type() {
        @Override
        public String getTypeName() {
          return "ParentType";
        }
    };
    Class<?> rawParentClass = TypeInfoFactory.class;
    
    Type[] expected = actualTypeArguments;
    Type[] actual = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
    assertArrayEquals(expected, actual);
  }
  
  @Test
  public void testGetIndex() {
    TypeVariable<?>[] types = new TypeVariable<?>[] {
        new TypeVariable<Class<?>>() {
          @Override
          public String getName() {
            return "Type1";
          }
        },
        new TypeVariable<Class<?>>() {
          @Override
          public String getName() {
            return "Type2";
          }
        }
    };
    TypeVariable<?> type = types[1];
    
    int expected = 1;
    int actual = TypeInfoFactory.getIndex(types, type);
    assertEquals(expected, actual);
  }
}