import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectIdInfoTest {
    @Test
    public void testGetPropertyName() {
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(new PropertyName("testProperty"), Object.class, ObjectIdGenerator.class, false);
        PropertyName expected = new PropertyName("testProperty");
        PropertyName actual = objectIdInfo.getPropertyName();
        assertEquals(expected, actual);
    }
}