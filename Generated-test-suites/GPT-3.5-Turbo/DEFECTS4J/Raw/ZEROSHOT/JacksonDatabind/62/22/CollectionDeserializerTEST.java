import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

public class CollectionDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(null, null, null, null);
        Collection<Object> result = new ArrayList<Object>();
        collectionDeserializer.deserialize(null, null, result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(null, null, null, null);
        Object result = collectionDeserializer.deserializeWithType(null, null, null);
        assertNull(result);
    }
}