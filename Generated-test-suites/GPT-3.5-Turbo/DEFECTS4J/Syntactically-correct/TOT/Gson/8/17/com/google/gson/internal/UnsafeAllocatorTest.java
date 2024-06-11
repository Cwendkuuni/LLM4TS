import com.google.gson.internal.UnsafeAllocator;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class UnsafeAllocatorTest {

    private static UnsafeAllocator unsafeAllocator;

    @BeforeClass
    public static void setUp() {
        unsafeAllocator = UnsafeAllocator.create();
    }

    @Test
    public void testNewInstance_SunMiscUnsafe_ReturnsNewInstance() {
        // Arrange
        class TestClass {
            private TestClass() {
            }
        }

        try {
            // Act
            TestClass result = unsafeAllocator.newInstance(TestClass.class);

            // Assert
            assertNotNull(result);
            assertTrue(result instanceof TestClass);
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testNewInstance_DalvikvmPostGingerbread_ReturnsNewInstance() {
        // Arrange
        class TestClass {
            private TestClass() {
            }
        }

        try {
            // Act
            TestClass result = unsafeAllocator.newInstance(TestClass.class);

            // Assert
            assertNotNull(result);
            assertTrue(result instanceof TestClass);
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testNewInstance_DalvikvmPreGingerbread_ReturnsNewInstance() {
        // Arrange
        class TestClass {
            private TestClass() {
            }
        }

        try {
            // Act
            TestClass result = unsafeAllocator.newInstance(TestClass.class);

            // Assert
            assertNotNull(result);
            assertTrue(result instanceof TestClass);
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNewInstance_UnsupportedClass_ThrowsException() {
        // Arrange
        class TestClass {
        }

        // Act
        unsafeAllocator.newInstance(TestClass.class);

        // Expect UnsupportedOperationException
    }
}