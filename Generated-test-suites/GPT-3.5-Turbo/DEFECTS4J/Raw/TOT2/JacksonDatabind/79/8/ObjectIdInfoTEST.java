import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testObjectIdInfoConstructor() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, MyObjectIdGenerator.class, MyObjectIdResolver.class);

        assertEquals("name", objectIdInfo.getPropertyName().getSimpleName());
        assertEquals(String.class, objectIdInfo.getScope());
        assertEquals(MyObjectIdGenerator.class, objectIdInfo.getGeneratorType());
        assertEquals(MyObjectIdResolver.class, objectIdInfo.getResolverType());
        assertEquals(false, objectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoWithAlwaysAsIdTrue() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, MyObjectIdGenerator.class, MyObjectIdResolver.class);

        ObjectIdInfo newObjectIdInfo = objectIdInfo.withAlwaysAsId(true);

        assertEquals(true, newObjectIdInfo.getAlwaysAsId());
    }

    @Test
    public void testObjectIdInfoToString() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, MyObjectIdGenerator.class, MyObjectIdResolver.class);

        String expectedToString = "ObjectIdInfo: propName=name, scope=java.lang.String, generatorType=com.example.MyObjectIdGenerator, alwaysAsId=false";
        assertEquals(expectedToString, objectIdInfo.toString());
    }

    @Test
    public void testObjectIdInfoConstructorWithDefaultResolver() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("name"), String.class, MyObjectIdGenerator.class);

        assertEquals(SimpleObjectIdResolver.class, objectIdInfo.getResolverType());
    }

    @Test
    public void testObjectIdInfoConstructorWithStringName() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo("name", String.class, MyObjectIdGenerator.class);

        assertEquals("name", objectIdInfo.getPropertyName().getSimpleName());
    }
}