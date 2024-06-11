import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstruction() {
        PropertyName propertyName = new PropertyName("id");
        Class<ObjectIdGenerator<Object>> generator = Mockito.mock(ObjectIdGenerator.class);
        Class<ObjectIdResolver> resolver = Mockito.mock(ObjectIdResolver.class);
        Class<String> scope = String.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.jsontype.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
        assertSame(propertyName, objectIdInfo.getPropertyName());
        assertSame(scope, objectIdInfo.getScope());
        assertSame(generator, objectIdInfo.getGeneratorType());
        assertSame(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionWithAlwaysAsIdTrue() {
        PropertyName propertyName = new PropertyName("id");
        Class<ObjectIdGenerator<Object>> generator = Mockito.mock(ObjectIdGenerator.class);
        Class<ObjectIdResolver> resolver = Mockito.mock(ObjectIdResolver.class);
        Class<String> scope = String.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, true, resolver);
        assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.jsontype.ObjectIdGenerator, alwaysAsId=true", objectIdInfo.toString());
        assertSame(propertyName, objectIdInfo.getPropertyName());
        assertSame(scope, objectIdInfo.getScope());
        assertSame(generator, objectIdInfo.getGeneratorType());
        assertSame(resolver, objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionWithNullPropertyName() {
        Class<ObjectIdGenerator<Object>> generator = Mockito.mock(ObjectIdGenerator.class);
        Class<ObjectIdResolver> resolver = Mockito.mock(ObjectIdResolver.class);
        Class<String> scope = String.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, scope, generator, false, resolver);
        assertNull(objectIdInfo);
    }

    @Test
    public void testObjectIdInfoConstructionWithDefaultResolver() {
        PropertyName propertyName = new PropertyName("id");
        Class<ObjectIdGenerator<Object>> generator = Mockito.mock(ObjectIdGenerator.class);
        Class<String> scope = String.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator);
        assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.jsontype.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
        assertSame(propertyName, objectIdInfo.getPropertyName());
        assertSame(scope, objectIdInfo.getScope());
        assertSame(generator, objectIdInfo.getGeneratorType());
        assertSame(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoConstructionWithCustomResolver() {
        PropertyName propertyName = new PropertyName("id");
        Class<ObjectIdGenerator<Object>> generator = Mockito.mock(ObjectIdGenerator.class);
        Class<MyCustomObjectIdResolver> resolver = Mockito.mock(MyCustomObjectIdResolver.class);
        Class<String> scope = String.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.jsontype.ObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
        assertSame(propertyName, objectIdInfo.getPropertyName());
        assertSame(scope, objectIdInfo.getScope());
        assertSame(generator, objectIdInfo.getGeneratorType());
        assertSame(resolver, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        PropertyName propertyName = new PropertyName("id");
        Class<ObjectIdGenerator<Object>> generator = Mockito.mock(ObjectIdGenerator.class);
        Class<ObjectIdResolver> resolver = Mockito.mock(ObjectIdResolver.class);
        Class<String> scope = String.class;

        ObjectIdInfo objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, false, resolver);
        ObjectIdInfo objectIdInfoWithAlwaysAsId = objectIdInfo.withAlwaysAsId(true);
        assertEquals("ObjectIdInfo: propName=id, scope=class java.lang.String, generatorType=class com.fasterxml.jackson.databind.jsontype.ObjectIdGenerator, alwaysAsId=true", objectIdInfoWithAlwaysAsId.toString());
        assertTrue(objectIdInfoWithAlwaysAsId.getAlwaysAsId());
    }
}

class MyCustomObjectIdResolver implements ObjectIdResolver {
    // Implement your custom ObjectIdResolver here
}