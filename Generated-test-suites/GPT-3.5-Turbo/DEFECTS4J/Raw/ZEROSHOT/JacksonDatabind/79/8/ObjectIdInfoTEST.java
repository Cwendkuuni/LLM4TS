import org.junit.Test;

public class ObjectIdInfoTest {

    @Test
    public void testWithAlwaysAsId() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("test"), String.class, null);
        assert !info.getAlwaysAsId();
        ObjectIdInfo updatedInfo = info.withAlwaysAsId(true);
        assert updatedInfo.getAlwaysAsId();
    }

    @Test
    public void testGetPropertyName() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("test"), String.class, null);
        assert info.getPropertyName().getSimpleName().equals("test");
    }

    @Test
    public void testGetScope() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("test"), String.class, null);
        assert info.getScope().getSimpleName().equals("String");
    }

    @Test
    public void testGetGeneratorType() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("test"), String.class, null);
        assert info.getGeneratorType() == null;
    }

    @Test
    public void testGetResolverType() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("test"), String.class, null);
        assert info.getResolverType().getSimpleName().equals("SimpleObjectIdResolver");
    }

    @Test
    public void testGetAlwaysAsId() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("test"), String.class, null);
        assert !info.getAlwaysAsId();
    }

    @Test
    public void testToString() {
        ObjectIdInfo info = new ObjectIdInfo(new PropertyName("test"), String.class, null);
        String expectedString = "ObjectIdInfo: propName=test, scope=String, generatorType=null, alwaysAsId=false";
        assert info.toString().equals(expectedString);
    }

}