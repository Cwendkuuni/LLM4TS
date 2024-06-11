import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructor1() {
        PropertyName name = new PropertyName("testName");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen, resolver);
        
        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(gen, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructor2() {
        PropertyName name = new PropertyName("testName");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen);

        assertEquals(name, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(gen, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructor3() {
        String name = "testName";
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen);

        assertEquals(new PropertyName(name), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(gen, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName name = new PropertyName("testName");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen, resolver);

        ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(true);

        assertEquals(name, updatedObjectIdInfo.getPropertyName());
        assertEquals(scope, updatedObjectIdInfo.getScope());
        assertEquals(gen, updatedObjectIdInfo.getGeneratorType());
        assertEquals(resolver, updatedObjectIdInfo.getResolverType());
        assertTrue(updatedObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName name = new PropertyName("testName");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> gen = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(name, scope, gen, resolver);

        String expectedString = "ObjectIdInfo: propName=testName, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";
        assertEquals(expectedString, objectIdInfo.toString());
    }
}