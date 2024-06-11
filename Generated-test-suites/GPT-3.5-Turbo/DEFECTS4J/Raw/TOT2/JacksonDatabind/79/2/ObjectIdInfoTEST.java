import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructor() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), String.class, MyObjectIdGenerator.class, MyObjectIdResolver.class);

        assertEquals("id", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(MyObjectIdResolver.class, objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), String.class, MyObjectIdGenerator.class);
        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);

        assertTrue(newObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("id"), String.class, MyObjectIdGenerator.class, MyObjectIdResolver.class);

        assertEquals("ObjectIdInfo: propName=id, scope=java.lang.String, generatorType=com.example.MyObjectIdGenerator, alwaysAsId=false", objectIdInfo.toString());
    }

    @Test
    public void testDeprecatedConstructor() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("id", String.class, MyObjectIdGenerator.class);

        assertEquals("id", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

}