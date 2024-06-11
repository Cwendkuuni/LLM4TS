import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ObjectIdInfoTest {

    private ObjectIdInfo objectIdInfo;

    @Before
    public void setup() {
        PropertyName propertyName = new PropertyName("test");
        Class<?> scope = Object.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;

        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
    }

    @Test
    public void testGetPropertyName() {
        PropertyName propertyName = objectIdInfo.getPropertyName();
        assertEquals("test", propertyName.getSimpleName());
    }

    @Test
    public void testGetScope() {
        Class<?> scope = objectIdInfo.getScope();
        assertEquals(Object.class, scope);
    }

    @Test
    public void testGetGeneratorType() {
        Class<? extends ObjectIdGenerator<?>> generator = objectIdInfo.getGeneratorType();
        assertEquals(ObjectIdGenerator.class, generator);
    }

    @Test
    public void testGetResolverType() {
        Class<? extends ObjectIdResolver> resolver = objectIdInfo.getResolverType();
        assertEquals(SimpleObjectIdResolver.class, resolver);
    }

    @Test
    public void testGetAlwaysAsId() {
        boolean alwaysAsId = objectIdInfo.getAlwaysAsId();
        assertEquals(false, alwaysAsId);
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertEquals(true, newObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        String expected = "ObjectIdInfo: " +
                "propName=test, " +
                "scope=java.lang.Object, " +
                "generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, " +
                "alwaysAsId=false";
        String actual = objectIdInfo.toString();
        assertEquals(expected, actual);
    }
}