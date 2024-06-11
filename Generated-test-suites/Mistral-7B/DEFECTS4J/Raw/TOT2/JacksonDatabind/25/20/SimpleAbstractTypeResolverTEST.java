import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import org.junit.Assert;
import org.junit.Test;

public class SimpleAbstractTypeResolverTest {
    @Test
    public void testAddMapping() {
        // Arrange
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Act
        Class<Number> numberSuperType = Number.class;
        Class<Integer> numberSubType = Integer.class;
        resolver.addMapping(numberSuperType, numberSubType);

        // Assert
        Assert.assertEquals(numberSubType, resolver._mappings.get(new ClassKey(numberSuperType)));
    }

    @Test
    public void testFindTypeMapping() {
        // Arrange
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        Class<Number> numberSuperType = Number.class;
        Class<Integer> numberSubType = Integer.class;
        resolver.addMapping(numberSuperType, numberSubType);
        JavaType javaType = new ClassType(numberSuperType);

        // Act
        JavaType result = resolver.findTypeMapping(new DeserializationConfig(), javaType);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(numberSubType, result.getRawClass());
    }

    @Test
    public void testResolveAbstractType() {
        // Arrange
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Act and Assert
        Assert.assertNull(resolver.resolveAbstractType(new DeserializationConfig(), new ClassType(Object.class)));
    }
}