import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {

    @Test
    public void testConstructorWithResolver() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("id"), Object.class, ObjectIdGenerator.class, SimpleObjectIdResolver.class);
        assertEquals("id", info.getPropertyName().getSimpleName());
        assertEquals(Object.class, info.getScope());
        assertEquals(ObjectIdGenerator.class, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithoutResolver() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("id"), Object.class, ObjectIdGenerator.class);
        assertEquals("id", info.getPropertyName().getSimpleName());
        assertEquals(Object.class, info.getScope());
        assertEquals(ObjectIdGenerator.class, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }
    
    @Test
    public void testConstructorWithStringName() {
        ObjectIdInfo info = new ObjectIdInfo("id", Object.class, ObjectIdGenerator.class);
        assertEquals("id", info.getPropertyName().getSimpleName());
        assertEquals(Object.class, info.getScope());
        assertEquals(ObjectIdGenerator.class, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }
    
    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("id"), Object.class, ObjectIdGenerator.class);
        info = info.withAlwaysAsId(true);
        assertTrue(info.getAlwaysAsId());
    }
    
    @Test
    public void testToString() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("id"), Object.class, ObjectIdGenerator.class);
        String expectedString = "ObjectIdInfo: propName=id, scope=java.lang.Object, generatorType=class com.fasterxml.jackson.annotation.ObjectIdGenerator, alwaysAsId=false";
        assertEquals(expectedString, info.toString());
    }
}