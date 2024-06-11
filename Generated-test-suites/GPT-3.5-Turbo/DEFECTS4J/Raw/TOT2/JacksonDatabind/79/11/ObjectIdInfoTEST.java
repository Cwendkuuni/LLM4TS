import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.PropertyName;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructor1() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("test"), String.class, ObjectIdGenerator.class, ObjectIdResolver.class);
        assertNotNull(objectIdInfo);
        assertEquals("test", objectIdInfo.getPropertyName().toString());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(ObjectIdResolver.class, objectIdInfo.getResolverType());
    }

    @Test
    public void testObjectIdInfoConstructor2() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("test", String.class, ObjectIdGenerator.class);
        assertNotNull(objectIdInfo);
        assertEquals("test", objectIdInfo.getPropertyName().toString());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
    }

    @Test
    public void testObjectIdInfoConstructor3() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("test"), String.class, ObjectIdGenerator.class, false);
        assertNotNull(objectIdInfo);
        assertEquals("test", objectIdInfo.getPropertyName().toString());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("test"), String.class, ObjectIdGenerator.class, ObjectIdResolver.class);
        ObjectIdInfo updatedInfo = objectIdInfo.withAlwaysAsId(true);
        assertNotNull(updatedInfo);
        assertTrue(updatedInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("test"), String.class, ObjectIdGenerator.class, false);
        String expectedString = "ObjectIdInfo: propName=test, scope=java.lang.String, generatorType=com.fasterxml.jackson.databind.introspect.ObjectIdGenerator, alwaysAsId=false";
        assertEquals(expectedString, objectIdInfo.toString());
    }
}