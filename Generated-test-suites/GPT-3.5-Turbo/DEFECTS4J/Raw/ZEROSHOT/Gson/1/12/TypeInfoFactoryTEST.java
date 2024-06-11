import com.google.gson.TypeInfoArray;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    Type type = new Type() {
      @Override
      public String getTypeName() {
        return "java.lang.Integer[]";
      }
    };

    TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);

    Assert.assertEquals("java.lang.Integer[]", typeInfoArray.getType().getTypeName());
  }

  @Test
  public void testGetTypeInfoForField() throws NoSuchFieldException {
    Field f = TypeInfoFactory.class.getDeclaredField("typeDefiningF");
    Type typeDefiningF = new Type() {
      @Override
      public String getTypeName() {
        return "java.lang.String";
      }
    };

    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(f, typeDefiningF);

    Assert.assertEquals("java.lang.String", typeInfo.getActualType().getTypeName());
  }

  @Test
  public void testGetActualTypeClass() {
    Class<?> typeToEvaluate = String.class;

    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, null, null);

    Assert.assertEquals(String.class, actualType);
  }

  @Test
  public void testGetActualTypeParameterizedType() {
    ParameterizedType typeToEvaluate = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[]{String.class};
      }

      @Override
      public Type getOwnerType() {
        return null;
      }

      @Override
      public Type getRawType() {
        return null;
      }
    };
    Type parentType = new Type() {
      @Override
      public String getTypeName() {
        return "java.lang.Integer";
      }
    };
    Class<?> rawParentClass = Integer.class;

    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);

    Assert.assertEquals(String.class, actualType);
  }

  @Test
  public void testGetActualTypeGenericArrayType() {
    GenericArrayType typeToEvaluate = new GenericArrayType() {
      @Override
      public Type getGenericComponentType() {
        return String.class;
      }
    };
    Type parentType = new Type() {
      @Override
      public String getTypeName() {
        return "java.lang.Integer";
      }
    };
    Class<?> rawParentClass = Integer.class;

    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);

    Assert.assertEquals(String[].class, actualType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualTypeTypeVariable() {
    TypeVariable<?> typeToEvaluate = new TypeVariable<>() {
      @Override
      public String getName() {
        return "T";
      }

      @Override
      public Type[] getBounds() {
        return new Type[]{String.class};
      }

      @Override
      public AnnotatedType[] getAnnotatedBounds() {
        return new AnnotatedType[0];
      }

      @Override
      public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return null;
      }

      @Override
      public Annotation[] getAnnotations() {
        return new Annotation[0];
      }

      @Override
      public Annotation[] getDeclaredAnnotations() {
        return new Annotation[0];
      }

      @Override
      public String toString() {
        return "T";
      }
    };
    ParameterizedType parentType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[]{Integer.class};
      }

      @Override
      public Type getOwnerType() {
        return null;
      }

      @Override
      public Type getRawType() {
        return String.class;
      }
    };
    Class<?> rawParentClass = String.class;

    TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
  }

  @Test
  public void testGetActualTypeWildcardType() {
    WildcardType typeToEvaluate = new WildcardType() {
      @Override
      public Type[] getUpperBounds() {
        return new Type[]{String.class};
      }

      @Override
      public Type[] getLowerBounds() {
        return new Type[0];
      }

      @Override
      public AnnotatedType[] getAnnotatedUpperBounds() {
        return new AnnotatedType[0];
      }

      @Override
      public AnnotatedType[] getAnnotatedLowerBounds() {
        return new AnnotatedType[0];
      }
    };
    Type parentType = new Type() {
      @Override
      public String getTypeName() {
        return "java.lang.Integer";
      }
    };
    Class<?> rawParentClass = Integer.class;

    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);

    Assert.assertEquals(String.class, actualType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetActualTypeInvalidType() {
    Type typeToEvaluate = new Type() {
      @Override
      public String getTypeName() {
        return "invalid type";
      }
    };
    Type parentType = new Type() {
      @Override
      public String getTypeName() {
        return "java.lang.Integer";
      }
    };
    Class<?> rawParentClass = Integer.class;

    TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
  }

  @Test
  public void testExtractRealTypes() {
    Type[] actualTypeArguments = new Type[]{
        new Type() {
          @Override
          public String getTypeName() {
            return "java.lang.String";
          }
        },
        new Type() {
          @Override
          public String getTypeName() {
            return "java.lang.Integer";
          }
        }
    };
    Type parentType = new Type() {
      @Override
      public String getTypeName() {
        return "java.lang.Double";
      }
    };
    Class<?> rawParentClass = Double.class;

    Type[] retTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);

    Assert.assertEquals("java.lang.String", retTypes[0].getTypeName());
    Assert.assertEquals("java.lang.Integer", retTypes[1].getTypeName());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetIndexNotFound() {
    TypeVariable<?>[] types = new TypeVariable<?>[]{
        new TypeVariable<>() {
          @Override
          public String getName() {
            return "T";
          }

          @Override
          public Type[] getBounds() {
            return new Type[]{String.class};
          }

          @Override
          public AnnotatedType[] getAnnotatedBounds() {
            return new AnnotatedType[0];
          }

          @Override
          public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
            return null;
          }

          @Override
          public Annotation[] getAnnotations() {
            return new Annotation[0];
          }

          @Override
          public Annotation[] getDeclaredAnnotations() {
            return new Annotation[0];
          }

          @Override
          public String toString() {
            return "T";
          }
        }
    };
    TypeVariable<?> type = new TypeVariable<>() {
      @Override
      public String getName() {
        return "U";
      }

      @Override
      public Type[] getBounds() {
        return new Type[]{Double.class};
      }

      @Override
      public AnnotatedType[] getAnnotatedBounds() {
        return new AnnotatedType[0];
      }

      @Override
      public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return null;
      }

      @Override
      public Annotation[] getAnnotations() {
        return new Annotation[0];
      }

      @Override
      public Annotation[] getDeclaredAnnotations() {
        return new Annotation[0];
      }

      @Override
      public String toString() {
        return "U";
      }
    };

    TypeInfoFactory.getIndex(types, type);
  }

  @Test
  public void testGetIndexFound() {
    TypeVariable<?>[] types = new TypeVariable<?>[]{
        new TypeVariable<>() {
          @Override
          public String getName() {
            return "T";
          }

          @Override
          public Type[] getBounds() {
            return new Type[]{String.class};
          }

          @Override
          public AnnotatedType[] getAnnotatedBounds() {
            return new AnnotatedType[0];
          }

          @Override
          public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
            return null;
          }

          @Override
          public Annotation[] getAnnotations() {
            return new Annotation[0];
          }

          @Override
          public Annotation[] getDeclaredAnnotations() {
            return new Annotation[0];
          }

          @Override
          public String toString() {
            return "T";
          }
        },
        new TypeVariable<>() {
          @Override
          public String getName() {
            return "U";
          }

          @Override
          public Type[] getBounds() {
            return new Type[]{Double.class};
          }

          @Override
          public AnnotatedType[] getAnnotatedBounds() {
            return new AnnotatedType[0];
          }

          @Override
          public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
            return null;
          }

          @Override
          public Annotation[] getAnnotations() {
            return new Annotation[0];
          }

          @Override
          public Annotation[] getDeclaredAnnotations() {
            return new Annotation[0];
          }

          @Override
          public String toString() {
            return "U";
          }
        }
    };
    TypeVariable<?> type = new TypeVariable<>() {
      @Override
      public String getName() {
        return "U";
      }

      @Override
      public Type[] getBounds() {
        return new Type[]{Double.class};
      }

      @Override
      public AnnotatedType[] getAnnotatedBounds() {
        return new AnnotatedType[0];
      }

      @Override
      public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return null;
      }

      @Override
      public Annotation[] getAnnotations() {
        return new Annotation[0];
      }

      @Override
      public Annotation[] getDeclaredAnnotations() {
        return new Annotation[0];
      }

      @Override
      public String toString() {
        return "U";
      }
    };

    int index = TypeInfoFactory.getIndex(types, type);

    Assert.assertEquals(1, index);
  }
}