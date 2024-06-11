import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.Cloneable;
import org.apache.commons.lang3.SerializationException;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class SerializationUtilsTest {

    @Test
    public void testClone() throws SerializationException, CloneNotSupportedException {
        Cloneable obj = new CloneableImplementation();
        SerializationUtils<CloneableImplementation> serialUtils = new SerializationUtils<>();
        Cloneable clonedObj = serialUtils.clone(obj);
        assertNotNull(clonedObj);
        assertEquals(obj.toString(), clonedObj.toString());
    }

    @Test
    public void testSerializeAndDeserializeObject() throws SerializationException, IOException {
        Object obj = new ObjectImplementation();
        byte[] objectData = SerializationUtils.serialize(obj);
        Object deserializedObj = SerializationUtils.deserialize(objectData);
        assertEquals(obj.toString(), deserializedObj.toString());
    }

    @Test(expected = SerializationException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeNullInput() {
        SerializationUtils.deserialize(null);
    }

    // Add more test cases for edge cases and exceptions as needed
}

class ObjectImplementation implements Serializable {
    // Implement the class here
}

class CloneableImplementation implements Cloneable {
    // Implement the class here
}

