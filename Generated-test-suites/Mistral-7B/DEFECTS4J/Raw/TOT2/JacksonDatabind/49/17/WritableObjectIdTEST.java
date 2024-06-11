import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class WritableObjectIdTest {
    @Test
    public void testWritableObjectIdConstructor() {
        ObjectIdGenerator<Integer> generator = new ObjectIdGeneratorBase() {
            @Override
            public Integer generateId(SerializerProvider serializerProvider, BeanProperty property) {
                return 1;
            }
        };
        WritableObjectId writableObjectId = new WritableObjectId(generator);
        assertNotNull(writableObjectId.generator);
    }
}