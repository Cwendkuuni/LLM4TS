import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithArgs() {
        PropertyName propertyName = new PropertyName("testPropertyName");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithArgsAndAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testPropertyName");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, alwaysAsId, resolver);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructorWithPropertyNameScopeAndGenerator() {
        PropertyName propertyName = new PropertyName("testPropertyName");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    }

    @Test
    public void testConstructorWithStringPropertyNameScopeAndGenerator() {
        String propertyName = "testPropertyName";
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);

        assertEquals(new PropertyName(propertyName), objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    }

    @Test
    public void testWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("testPropertyName");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver)
                .withAlwaysAsId(alwaysAsId);

        assertEquals(propertyName, objectIdInfo.getPropertyName());
        assertEquals(scope, objectIdInfo.getScope());
        assertEquals(generator, objectIdInfo.getGeneratorType());
        assertEquals(resolver, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        PropertyName propertyName = new PropertyName("testPropertyName");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = ObjectIdResolver.class;
        boolean alwaysAsId = true;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver)
                .withAlwaysAsId(alwaysAsId);

        String expectedString = "ObjectIdInfo: propName=testPropertyName, scope=java.lang.Object, " +
                "generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=true";
        assertEquals(expectedString, objectIdInfo.toString());
    }
}