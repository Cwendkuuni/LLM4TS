import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.SerializationUtils;
import java.io.Serializable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Typical use case
        TestData testData = new TestData("Test Data");
        TestData clonedData = SerializationUtils.clone(testData);
        assertNotSame(testData, clonedData);
        assertEquals(testData.getValue(), clonedData.getValue());

        // Edge case
        TestData nullData = null;
        TestData clonedNullData = SerializationUtils.clone(nullData);
        assertNull(clonedNullData);
    }

    @Test
    public void testSerialize_Deserialize() {
        // Typical use case
        TestData testData = new TestData("Test Data");
        byte[] serializedData = SerializationUtils.serialize(testData);
        TestData deserializedData = (TestData) SerializationUtils.deserialize(serializedData);
        assertEquals(testData.getValue(), deserializedData.getValue());

        // Edge case
        byte[] emptyData = new byte[0];
        TestData deserializedEmptyData = (TestData) SerializationUtils.deserialize(emptyData);
        assertNull(deserializedEmptyData);

        // Error scenario
        try {
            TestData testData1 = new TestData("Test Data");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.close(); // Simulating closed stream
            SerializationUtils.serialize(testData1, outputStream);
            fail("Expected SerializationException");
        } catch (SerializationException ex) {
            // Expected
        }
    }

    private static class TestData implements Serializable {
        private String value;

        public TestData(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof TestData) {
                TestData other = (TestData) obj;
                return this.value.equals(other.value);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }
    }
}