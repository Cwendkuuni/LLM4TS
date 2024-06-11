
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

    private ObjectIdInfo objectIdInfo;

    @Before
    public void setUp() {
        PropertyName propertyName = new PropertyName("name");
        Class<?> scope = String.class;
        Class<?> generator = ObjectIdGenerator.class;
        Class<?> resolver = ObjectIdResolver.class;
        objectIdInfo = new ObjectIdInfo(propertyName, scope, generator, resolver);
    }

    @Test
    public void testGetPropertyName() {
        PropertyName propertyName = objectIdInfo.getPropertyName();
        // add assertion here
    }

    @Test
    public void testGetScope() {
        Class<?> scope = objectIdInfo.getScope();
        // add assertion here
    }

    @Test
    public void testGetGeneratorType() {
        Class<?> generatorType = objectIdInfo.getGeneratorType();
        // add assertion here
    }

    @Test
    public void testGetResolverType() {
        Class<?> resolverType = objectIdInfo.getResolverType();
        // add assertion here
    }

    @Test
    public void testGetAlwaysAsId() {
        boolean alwaysAsId = objectIdInfo.getAlwaysAsId();
        // add assertion here
    }

    @Test
    public void testWithAlwaysAsId() {
        boolean state = true;
        ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(state);
        // add assertion here
    }

    @Test
    public void testToString() {
        String string = objectIdInfo.toString();
        // add assertion here
    }
}
