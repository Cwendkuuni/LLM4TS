import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.PropertyName;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructor1() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, null, SimpleObjectIdResolver.class);
        assertEquals("name", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    }

    @Test
    public void testObjectIdInfoConstructor2() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("name", String.class, null, SimpleObjectIdResolver.class);
        assertEquals("name", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    }

    @Test
    public void testObjectIdInfoConstructor3() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, null);
        assertEquals("name", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertNull(objectIdInfo.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, null, SimpleObjectIdResolver.class);
        assertFalse(objectIdInfo.getAlwaysAsId());
        
        objectIdInfo = objectIdInfo.withAlwaysAsId(true);
        assertTrue(objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, null, SimpleObjectIdResolver.class);
        String expectedString = "ObjectIdInfo: propName=name, scope=java.lang.String, generatorType=null, alwaysAsId=false";
        assertEquals(expectedString, objectIdInfo.toString());
    }
}