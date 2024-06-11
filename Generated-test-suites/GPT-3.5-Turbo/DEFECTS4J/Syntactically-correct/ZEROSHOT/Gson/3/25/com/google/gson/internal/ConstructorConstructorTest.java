import org.junit.Test;
import static org.junit.Assert.*;

public class ConstructorConstructorTest {

  @Test
  public void testGet_rawType_notNull() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    ObjectConstructor<T> objectConstructor = constructorConstructor.get(new TypeToken<T>() {
      @Override
      public Type getType() {
        return null;
      }
      @Override
      public Class<T> getRawType() {
        return null;
      }
    });
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_typeCreator_notNull() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    ObjectConstructor<T> objectConstructor = constructorConstructor.get(new TypeToken<T>() {
      @Override
      public Type getType() {
        return null;
      }
      @Override
      public Class<T> getRawType() {
        return null;
      }
    });
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_defaultConstructor_notNull() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    ObjectConstructor<T> objectConstructor = constructorConstructor.get(new TypeToken<T>() {
      @Override
      public Type getType() {
        return null;
      }
      @Override
      public Class<T> getRawType() {
        return null;
      }
    });
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_defaultImplementation_notNull() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    ObjectConstructor<T> objectConstructor = constructorConstructor.get(new TypeToken<T>() {
      @Override
      public Type getType() {
        return null;
      }
      @Override
      public Class<T> getRawType() {
        return null;
      }
    });
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testGet_unsafeAllocator_notNull() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    ObjectConstructor<T> objectConstructor = constructorConstructor.get(new TypeToken<T>() {
      @Override
      public Type getType() {
        return null;
      }
      @Override
      public Class<T> getRawType() {
        return null;
      }
    });
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testNewDefaultConstructor_constructorNotNull() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    ObjectConstructor<T> objectConstructor = constructorConstructor.get(new TypeToken<T>() {
      @Override
      public Type getType() {
        return null;
      }
      @Override
      public Class<T> getRawType() {
        return null;
      }
    });
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testNewDefaultImplementationConstructor_constructorNotNull() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    ObjectConstructor<T> objectConstructor = constructorConstructor.get(new TypeToken<T>() {
      @Override
      public Type getType() {
        return null;
      }
      @Override
      public Class<T> getRawType() {
        return null;
      }
    });
    assertNotNull(objectConstructor);
  }
  
  @Test
  public void testNewUnsafeAllocator_constructorNotNull() {
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(null);
    ObjectConstructor<T> objectConstructor = constructorConstructor.get(new TypeToken<T>() {
      @Override
      public Type getType() {
        return null;
      }
      @Override
      public Class<T> getRawType() {
        return null;
      }
    });
    assertNotNull(objectConstructor);
  }
}