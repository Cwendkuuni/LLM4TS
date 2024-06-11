import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class WritableObjectIdTest {

    @Test
    public void testWritableObjectIdConstructor() {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator() {
            @Override
            public Object generateId(Object object) {
                return "GeneratedId";
            }
        };

        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertNotNull(writableObjectId.generator);
    }
}