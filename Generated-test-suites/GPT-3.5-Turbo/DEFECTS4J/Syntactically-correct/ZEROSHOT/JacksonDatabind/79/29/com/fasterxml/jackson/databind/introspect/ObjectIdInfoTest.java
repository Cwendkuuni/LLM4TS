import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    private ObjectIdInfo objectIdInfo;

    @Before
    public void setUp() {
        PropertyName propertyName = new PropertyName("testProperty");
        Class<?> scope = String.class;
        Class<? extends ObjectIdGenerator<?>> generator = ObjectIdGenerator.class;
        Class<? extends ObjectIdResolver> resolver = SimpleObjectIdResolver.class;
        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
    }

    @Test
    public void testWithAlwaysAsId() {
        boolean alwaysAsId = true;
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(alwaysAsId);
        assertTrue(newObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testGetPropertyName() {
        PropertyName expectedPropertyName = new PropertyName("testProperty");
        assertEquals(expectedPropertyName, objectIdInfo.getPropertyName());
    }

    @Test
    public void testGetScope() {
        Class<?> expectedScope = String.class;
        assertEquals(expectedScope, objectIdInfo.getScope());
    }

    @Test
    public void testGetGeneratorType() {
        Class<? extends ObjectIdGenerator<?>> expectedGeneratorType = ObjectIdGenerator.class;
        assertEquals(expectedGeneratorType, objectIdInfo.getGeneratorType());
    }

    @Test
    public void testGetResolverType() {
        Class<? extends ObjectIdResolver> expectedResolverType = SimpleObjectIdResolver.class;
        assertEquals(expectedResolverType, objectIdInfo.getResolverType());
    }

    @Test
    public void testGetAlwaysAsId() {
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        String expectedString = "ObjectIdInfo: propName=testProperty, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";
        assertEquals(expectedString, objectIdInfo.toString());
    }
}