import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructor() {
        ObjectIdInfo objIdInfo = new ObjectIdInfo(new PropertyName("test"), String.class, ObjectIdGenerator.class, ObjectIdResolver.class);
        assertNotNull(objIdInfo);
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo objIdInfo = new ObjectIdInfo(new PropertyName("test"), String.class, ObjectIdGenerator.class, ObjectIdResolver.class);
        ObjectIdInfo newObjIdInfo = objIdInfo.withAlwaysAsId(true);
        assertTrue(newObjIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        ObjectIdInfo objIdInfo = new ObjectIdInfo(new PropertyName("test"), String.class, ObjectIdGenerator.class, ObjectIdResolver.class);
        String result = objIdInfo.toString();
        assertEquals("ObjectIdInfo: propName=test, scope=java.lang.String, generatorType=com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false", result);
    }
}