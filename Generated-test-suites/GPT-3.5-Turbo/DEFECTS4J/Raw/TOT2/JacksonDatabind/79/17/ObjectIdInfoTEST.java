import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testConstructor1() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("name"), String.class, MyObjectIdGenerator.class, MyObjectIdResolver.class);
        assertEquals("name", info.getPropertyName().getSimpleName());
        assertEquals(String.class, info.getScope());
        assertEquals(MyObjectIdGenerator.class, info.getGeneratorType());
        assertEquals(MyObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testConstructor2() {
        ObjectIdInfo info = new ObjectIdInfo("name", Integer.class, MyObjectIdGenerator.class);
        assertEquals("name", info.getPropertyName().getSimpleName());
        assertEquals(Integer.class, info.getScope());
        assertEquals(MyObjectIdGenerator.class, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertFalse(info.getAlwaysAsId());
    }

    @Test
    public void testConstructor3() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("name"), Long.class, MyObjectIdGenerator.class, true);
        assertEquals("name", info.getPropertyName().getSimpleName());
        assertEquals(Long.class, info.getScope());
        assertEquals(MyObjectIdGenerator.class, info.getGeneratorType());
        assertEquals(SimpleObjectIdResolver.class, info.getResolverType());
        assertTrue(info.getAlwaysAsId());
    }

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("name"), Double.class, MyObjectIdGenerator.class, MyObjectIdResolver.class);
        ObjectIdInfo newInfo = info.withAlwaysAsId(true);
        assertTrue(newInfo.getAlwaysAsId());
    }

    @Test
    public void testToString() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("name"), Float.class, MyObjectIdGenerator.class, MyObjectIdResolver.class);
        String expected = "ObjectIdInfo: propName=name, scope=java.lang.Float, generatorType=MyObjectIdGenerator, alwaysAsId=false";
        assertEquals(expected, info.toString());
    }
}