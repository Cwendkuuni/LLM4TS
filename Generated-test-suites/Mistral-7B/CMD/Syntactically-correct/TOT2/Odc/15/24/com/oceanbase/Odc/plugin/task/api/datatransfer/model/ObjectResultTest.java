import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objResult = new ObjectResult();
        // Add assertions here
    }

    @Test
    public void testObjectResultFromObjectStatus() {
        ObjectStatus objStatus = new ObjectStatus("TestSchema", "TestName", ObjectStatus.Status.SUCCESS, new AtomicLong(1), new AtomicLong(1));
        ObjectResult objResult = ObjectResult.of(objStatus);
        // Add assertions here
    }

    // Add test cases for other methods

    // ...
}