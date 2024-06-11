
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {
  
  private JsonAdapterAnnotationTypeAdapterFactory factory;
  private Gson gson;
  
  @Before
  public void setup() {
    factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
    gson = new Gson();
  }
  
  @Test
  public void testCreate_NoAnnotation_ReturnsNull() {
    TypeAdapter<?> adapter = factory.create(gson, TypeToken.get(String.class));
    assertNull(adapter);
  }
  
  @Test
  public void testCreate_TypeAdapterAnnotation_ReturnsTypeAdapter() {
    TypeAdapter<?> adapter = factory.create(gson, TypeToken.get(MyClass.class));
    assertNotNull(adapter);
    assertTrue(adapter instanceof MyTypeAdapter);
  }
  
  @Test
  public void testCreate_TypeAdapterFactoryAnnotation_ReturnsTypeAdapter() {
    TypeAdapter<?> adapter = factory.create(gson, TypeToken.get(AnotherClass.class));
    assertNotNull(adapter);
    assertTrue(adapter instanceof AnotherTypeAdapter);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetNullSafe_NullValue_ThrowsIllegalArgumentException() {
    factory.getTypeAdapter(new ConstructorConstructor(), gson, TypeToken.get(String.class), null);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetNullSafe_InvalidValue_ThrowsIllegalArgumentException() {
    @JsonAdapter(Integer.class)
    class InvalidClass {}
    factory.getTypeAdapter(new ConstructorConstructor(), gson, TypeToken.get(InvalidClass.class), new JsonAdapter() {
      @Override
      public Class<?> value() {
        return Integer.class;
      }
    });
  }
  
  @Test
  public void testGetNullSafe_TypeAdapterValue_ReturnsTypeAdapter() {
    @JsonAdapter(MyTypeAdapter.class)
    class ValidClass {}
    TypeAdapter<?> adapter = factory.getTypeAdapter(new ConstructorConstructor(), gson, TypeToken.get(ValidClass.class), new JsonAdapter() {
      @Override
      public Class<?> value() {
        return MyTypeAdapter.class;
      }
    });
    assertNotNull(adapter);
    assertTrue(adapter instanceof MyTypeAdapter);
  }
  
  @Test
  public void testGetNullSafe_TypeAdapterFactoryValue_ReturnsTypeAdapter() {
    @JsonAdapter(AnotherTypeAdapterFactory.class)
    class ValidClass {}
    TypeAdapter<?> adapter = factory.getTypeAdapter(new ConstructorConstructor(), gson, TypeToken.get(ValidClass.class), new JsonAdapter() {
      @Override
      public Class<?> value() {
        return AnotherTypeAdapterFactory.class;
      }
    });
    assertNotNull(adapter);
    assertTrue(adapter instanceof AnotherTypeAdapter);
  }
  
  @Test
  public void testGetNullSafe_TypeAdapterValue_WithNullSafe_ReturnsNullSafeTypeAdapter() {
    @JsonAdapter(MyTypeAdapter.class)
    class ValidClass {}
    TypeAdapter<?> adapter = factory.getTypeAdapter(new ConstructorConstructor(), gson, TypeToken.get(ValidClass.class), new JsonAdapter() {
      @Override
      public Class<?> value() {
        return MyTypeAdapter.class;
      }
      @Override
      public boolean nullSafe() {
        return true;
      }
    });
    assertNotNull(adapter);
    assertTrue(adapter instanceof MyTypeAdapter);
    assertTrue(adapter.nullSafe());
  }
  
  @Test
  public void testGetNullSafe_TypeAdapterFactoryValue_WithNullSafe_ReturnsNullSafeTypeAdapter() {
    @JsonAdapter(AnotherTypeAdapterFactory.class)
    class ValidClass {}
    TypeAdapter<?> adapter = factory.getTypeAdapter(new ConstructorConstructor(), gson, TypeToken.get(ValidClass.class), new JsonAdapter() {
      @Override
      public Class<?> value() {
        return AnotherTypeAdapterFactory.class;
      }
      @Override
      public boolean nullSafe() {
        return true;
      }
    });
    assertNotNull(adapter);
    assertTrue(adapter instanceof AnotherTypeAdapter);
    assertTrue(adapter.nullSafe());
  }
}
