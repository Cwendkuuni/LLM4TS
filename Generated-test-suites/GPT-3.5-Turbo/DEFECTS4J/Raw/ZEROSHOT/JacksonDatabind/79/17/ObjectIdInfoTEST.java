import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    private ObjectIdInfo objectIdInfo;

    @Before
    public void setUp() {
        objectIdInfo = new ObjectIdInfo(PropertyName.construct("name"), MyClass.class, MyObjectIdGenerator.class, MyObjectIdResolver.class);
    }

    @Test
    public void testConstructor1() {
        assertEquals("name", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(MyClass.class, objectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(MyObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructor2() {
        ObjectIdInfo info = new ObjectIdInfo(PropertyName.construct("name"), MyClass.class, MyObjectIdGenerator.class, MyObjectIdResolver.class);
        assertEquals("name", info.getPropertyName().getSimpleName());
        assertEquals(MyClass.class, info.getScope());
        assertEquals(MyObjectIdGenerator.class, info.getGeneratorType());
        assertEquals(MyObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testConstructor3() {
        ObjectIdInfo info = new ObjectIdInfo("name", MyClass.class, MyObjectIdGenerator.class);
        assertEquals("name", info.getPropertyName().getSimpleName());
        assertEquals(MyClass.class, info.getScope());
        assertEquals(MyObjectIdGenerator.class, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo updatedInfo = objectIdInfo.withAlwaysAsId(true);
        assertTrue(updatedInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        String expected = "ObjectIdInfo: propName=name, scope=com.fasterxml.jackson.databind.introspect.MyClass, generatorType=com.fasterxml.jackson.databind.introspect.MyObjectIdGenerator, alwaysAsId=false";
        assertEquals(expected, objectIdInfo.toString());
    }

    // Additional helper classes for testing

    public static class MyClass {
    }

    public static class MyObjectIdGenerator<T> extends com.fasterxml.jackson.annotation.ObjectIdGenerator<T> {

        @Override
        public T generateId(Object forPojo) {
            return null;
        }

        @Override
        public Class<?> getScope() {
            return null;
        }

        @Override
        public boolean canUseFor(ObjectIdGenerator<?> gen) {
            return false;
        }
    }

    public static class MyObjectIdResolver extends com.fasterxml.jackson.annotation.ObjectIdResolver {

        @Override
        public void bindItem(ObjectIdGenerator.IdKey id, Object pojo) {

        }

        @Override
        public Object resolveId(ObjectIdGenerator.IdKey id) {
            return null;
        }

        @Override
        public ObjectIdResolver newForDeserialization(Object context) {
            return null;
        }

        @Override
        public boolean canUseFor(ObjectIdResolver resolverType) {
            return false;
        }
    }

}