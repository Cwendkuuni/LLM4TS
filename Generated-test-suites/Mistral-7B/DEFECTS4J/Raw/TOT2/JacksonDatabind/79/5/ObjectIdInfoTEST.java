import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.jupiter.api.*;

class ObjectIdInfoTest {

    @Test
    void testObjectIdInfoWithThreeArguments() {
        PropertyName name = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator);
        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    void testObjectIdInfoWithFourArguments() {
        PropertyName name = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, true);
        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    void testObjectIdInfoWithFiveArguments() {
        PropertyName name = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, true, SimpleObjectIdResolver.class);
        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertTrue(objectIdInfo.getAlwaysAsId());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    }

    @Test
    void testWithAlwaysAsIdMethod() {
        PropertyName name = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator);
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertTrue(newObjectIdInfo.getAlwaysAsId());
        assertNotSame(objectIdInfo, newObjectIdInfo);
    }

    @Test
    void testGetterMethods() {
        PropertyName name = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, true, SimpleObjectIdResolver.class);
        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    void testToStringMethod() {
        PropertyName name = new PropertyName("testProperty");
        Class<String> scope = String.class;
        Class<CustomObjectIdGenerator> generator = CustomObjectIdGenerator.class;
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, generator, true, SimpleObjectIdResolver.class);
        String expectedToString = "ObjectIdInfo: propName=testProperty, scope=class java.lang.String, generatorType=class com.example.CustomObjectIdGenerator, alwaysAsId=true";
        assertEquals(expectedToString, objectIdInfo.toString());
    }
}