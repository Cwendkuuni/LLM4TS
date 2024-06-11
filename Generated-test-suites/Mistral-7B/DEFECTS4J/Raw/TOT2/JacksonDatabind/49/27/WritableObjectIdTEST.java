import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class WritableObjectIdTest {

    @Test
    public void testWritableObjectIdConstruction() {
        ObjectIdGenerator<?> generator = new ObjectIdGeneratorString(); // assuming a valid ObjectIdGenerator implementation
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertNotNull(writableObjectId);
    }
}