import static org.junit.Assert.assertEquals;

public class ObjectIdInfoTest {

    @Test
    public void testGetPropertyName() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class, false);
        PropertyName expectedPropertyName = new PropertyName("testProperty");
        PropertyName actualPropertyName = objectIdInfo.getPropertyName();
        assertEquals(expectedPropertyName, actualPropertyName);
    }
}