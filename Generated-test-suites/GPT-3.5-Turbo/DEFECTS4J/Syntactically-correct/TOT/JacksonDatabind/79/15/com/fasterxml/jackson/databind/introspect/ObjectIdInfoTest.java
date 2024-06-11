import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    private ObjectIdInfo objectIdInfo;

    @Before
    public void setup() {
        objectIdInfo = new ObjectIdInfo(null, null, null, null);
    }

    @Test
    public void testConstructor1() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(null, null, null, null);
        assertNotNull(objectIdInfo);
        assertNull(objectIdInfo.getPropertyName());
        assertNull(objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructor2() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("propName"), String.class, ObjectIdGenerator.class, true);
        assertNotNull(objectIdInfo);
        assertEquals(new PropertyName("propName"), objectIdInfo.getPropertyName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testConstructor3() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("propName", String.class, ObjectIdGenerator.class);
        assertNotNull(objectIdInfo);
        assertEquals(new PropertyName("propName"), objectIdInfo.getPropertyName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(ObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertNull(objectIdInfo.getResolverType());
        assertFalse(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo updatedObjectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertFalse(objectIdInfo.getAlwaysAsId());
        assertTrue(updatedObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testEquals() {
        ObjectIdInfo objectIdInfo1 = new ObjectIdInfo("propName", String.class, ObjectIdGenerator.class);
        ObjectIdInfo objectIdInfo2 = new ObjectIdInfo("propName", String.class, ObjectIdGenerator.class);
        assertEquals(objectIdInfo1, objectIdInfo2);
    }

    @Test
    public void testNotEquals() {
        ObjectIdInfo objectIdInfo1 = new ObjectIdInfo("propName", String.class, ObjectIdGenerator.class);
        ObjectIdInfo objectIdInfo2 = new ObjectIdInfo("propName", Integer.class, ObjectIdGenerator.class);
        assertNotEquals(objectIdInfo1, objectIdInfo2);
    }

    @Test
    public void testToString() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("propName", String.class, ObjectIdGenerator.class);
        String expected = "ObjectIdInfo: propName=propName, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";
        assertEquals(expected, objectIdInfo.toString());
    }

}