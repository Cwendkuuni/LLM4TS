import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;
import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        String original = "Hello World";
        String cloned = SerializationUtils.clone(original);
        Assert.assertEquals(original, cloned);
    }

    @Test
    public void testSerializeDeserializeString() {
        String original = "Hello World";
        byte[] serialized = SerializationUtils.serialize(original);
        String deserialized = (String) SerializationUtils.deserialize(serialized);
        Assert.assertEquals(original, deserialized);
    }

    @Test
    public void testSerializeDeserializeMap() {
        Map<String, Integer> original = new HashMap<>();
        original.put("One", 1);
        original.put("Two", 2);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(original, outputStream);

        Map<String, Integer> deserialized = (Map<String, Integer>) SerializationUtils.deserialize(outputStream.toByteArray());
        Assert.assertEquals(original, deserialized);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    @Test(expected = SerializationException.class)
    public void testSerializeDeserializeException() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            SerializationUtils.serialize(new Exception("Test"), outputStream);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            Object obj = SerializationUtils.deserialize(inputStream);
            Assert.fail();
        } catch (SerializationException e) {
            //Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    @Test(expected = IOException.class)
    public void testDeserializeIOException() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            SerializationUtils.deserialize(inputStream);
            Assert.fail();
        } catch (IOException e) {
            //Expected
        }
    }

    @Test(expected = ClassNotFoundException.class)
    public void testDeserializeClassNotFoundException() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            OutputStream os = new ByteArrayOutputStream(512);
            ObjectOutputStream out = new ObjectOutputStream(os);
            out.writeObject(new byte[0]);
            out.close();
            Object obj = SerializationUtils.deserialize(new ByteArrayInputStream(outputStream.toByteArray()));
            Assert.fail();
        } catch (IOException e) {
            //Expected
        } catch (ClassNotFoundException ex) {
            //Expected
        }
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeInvalidClass() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            OutputStream os = new ByteArrayOutputStream(512);
            ObjectOutputStream out = new ObjectOutputStream(os);
            out.writeObject("InvalidClass");
            out.close();
            Object obj = SerializationUtils.deserialize(new ByteArrayInputStream(outputStream.toByteArray()));
            Assert.fail();
        } catch (IOException e) {
            //Expected
        } catch (ClassNotFoundException ex) {
            //Expected
        } catch (SerializationException ex) {
            //Expected
        }
    }

}