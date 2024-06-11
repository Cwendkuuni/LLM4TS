import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {

    private GenericMaster genericMaster;
    private Field genericField;

    @Before
    public void setUp() throws NoSuchFieldException {
        genericMaster = new GenericMaster();
        genericField = GenericMaster.class.getDeclaredField("generic");
    }

    @Test
    public void testGetGenericType_ValidGenericField_ReturnsGenericClass() {
        // Arrange
        ParameterizedType parameterizedType = new ParameterizedType() {
            public Type getRawType() {
                return null;
            }
            public Type getOwnerType() {
                return null;
            }
            public Type[] getActualTypeArguments() {
                return new Type[]{String.class};
            }
        };
        genericField.setGenericType(parameterizedType);

        // Act
        Class<?> result = genericMaster.getGenericType(genericField);

        // Assert
        Assert.assertEquals(String.class, result);
    }

    @Test
    public void testGetGenericType_NullGeneric_ReturnsObjectClass() {
        // Arrange
        genericField.setGenericType(null);

        // Act
        Class<?> result = genericMaster.getGenericType(genericField);

        // Assert
        Assert.assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_NonParameterizedTypeGeneric_ReturnsObjectClass() {
        // Arrange
        genericField.setGenericType(Integer.class);

        // Act
        Class<?> result = genericMaster.getGenericType(genericField);

        // Assert
        Assert.assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_NestedGeneric_ReturnsFirstLevelGenericClass() {
        // Arrange
        ParameterizedType parameterizedType = new ParameterizedType() {
            public Type getRawType() {
                return null;
            }
            public Type getOwnerType() {
                return null;
            }
            public Type[] getActualTypeArguments() {
                return new Type[]{
                        new ParameterizedType() {
                            public Type getRawType() {
                                return null;
                            }
                            public Type getOwnerType() {
                                return null;
                            }
                            public Type[] getActualTypeArguments() {
                                return new Type[]{Double.class};
                            }
                        }
                };
            }
        };
        genericField.setGenericType(parameterizedType);

        // Act
        Class<?> result = genericMaster.getGenericType(genericField);

        // Assert
        Assert.assertEquals(ParameterizedType.class, result);
    }
}