import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.internal.UnsafeAllocator;

public class UnsafeAllocatorTest {

  private UnsafeAllocator unsafeAllocator;

  @Before
  public void setup() {
    unsafeAllocator = UnsafeAllocator.create();
  }

  @Test
  public void testCreate() {
    assertNotNull(unsafeAllocator);
  }

  @Test
  public void testNewInstance_SimpleClass() {
    try {
      // Arrange
      class SimpleClass {
        public SimpleClass() {
          throw new RuntimeException("Constructor should not be called");
        }
      }

      // Act
      SimpleClass instance = unsafeAllocator.newInstance(SimpleClass.class);

      // Assert
      assertNotNull(instance);

    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testNewInstance_ClassWithInterface() {
    try {
      // Arrange
      interface MyInterface {
      }

      class ClassWithInterface implements MyInterface {
        public ClassWithInterface() {
          throw new RuntimeException("Constructor should not be called");
        }
      }

      // Act
      ClassWithInterface instance = unsafeAllocator.newInstance(ClassWithInterface.class);

      // Assert
      assertNotNull(instance);

    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testNewInstance_AbstractClass() {
    try {
      // Arrange
      abstract class AbstractClass {
      }

      // Act
      AbstractClass instance = unsafeAllocator.newInstance(AbstractClass.class);

      // Assert
      assertNotNull(instance);

    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_ClassWithNoConstructor() throws Exception {
    // Arrange
    class ClassWithNoConstructor {
    }

    // Act
    unsafeAllocator.newInstance(ClassWithNoConstructor.class);
  }

  @Test
  public void testNewInstance_ClassWithPrivateConstructor() {
    try {
      // Arrange
      class ClassWithPrivateConstructor {
        private ClassWithPrivateConstructor() {
          throw new RuntimeException("Constructor should not be called");
        }
      }

      // Act
      ClassWithPrivateConstructor instance = unsafeAllocator.newInstance(ClassWithPrivateConstructor.class);

      // Assert
      assertNotNull(instance);

    } catch (Exception e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

}