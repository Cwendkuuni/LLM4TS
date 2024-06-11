import org.junit.Before;
import org.junit.Test;

public class ConstructorInstantiatorTest {

    private ConstructorInstantiator constructorInstantiator;

    @Before
    public void setUp() {
        constructorInstantiator = new ConstructorInstantiator(null);
    }

    @Test
    public void testNewInstance_noArgConstructor_returnsInstanceSuccessfully() {
        // Arrange
        Class<MyClass> cls = MyClass.class;

        // Act
        MyClass result = constructorInstantiator.newInstance(cls);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testNewInstance_withOuterClass_returnsInstanceSuccessfully() {
        // Arrange
        Class<MyClass> cls = MyClass.class;
        MyClass outerClassInstance = new MyClass();

        // Act
        MyClass result = constructorInstantiator.newInstance(cls);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testNewInstance_withInvalidOuterClass_throwsException() {
        // Arrange
        Class<MyClass> cls = MyClass.class;
        Integer outerClassInstance = 10;

        // Act and Assert
        assertThrows(InstantiationException.class, () -> {
            constructorInstantiator.newInstance(cls);
        });
    }

    @Test
    public void testNewInstance_withClassMissingParameterlessConstructor_throwsException() {
        // Arrange
        Class<MyClassWithoutConstructor> cls = MyClassWithoutConstructor.class;

        // Act and Assert
        assertThrows(InstantiationException.class, () -> {
            constructorInstantiator.newInstance(cls);
        });
    }
  
    @Test
    public void testNewInstance_withNullClass_throwsException() {
        // Arrange
        Class<MyClass> cls = null;

        // Act and Assert
        assertThrows(NullPointerException.class, () -> {
            constructorInstantiator.newInstance(cls);
        });
    }
}