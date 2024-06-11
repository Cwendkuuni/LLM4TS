package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SerializationUtilsTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testClone() throws Exception {
        Object original = new HashMap<>();
        Object cloned = SerializationUtils.clone(original);

        assertEquals(original, cloned);
        assertNotSame(original, cloned);

        // test cloning an array
        Object originalArray = new Object[] { 1, 2, 3 };
        Object clonedArray = SerializationUtils.clone(originalArray);
        assertArrayEquals((Object[]) originalArray, (Object[]) clonedArray);
        assertNotSame(originalArray, clonedArray);

        // test cloning a null object
        Object nullOriginal = null;
        Object nullCloned = SerializationUtils.clone(nullOriginal);
        assertNull(nullCloned);
    }

    @Test
    public void testSerializeAndDeserialize() throws Exception {
        Object obj = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(obj);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        assertEquals(obj, deserialized);
    }

    @Test(expected = SerializationException.class)
    public void testSerializationFailure() throws Exception {
        Object obj = new Object() {
            private void writeObject(ObjectOutputStream out) throws IOException {
                throw new IOException("Simulated serialization failure");
            }
        };
        SerializationUtils.serialize(obj, new ByteArrayOutputStream(512));
    }

    @Test
    public void testStreamCorruptedException() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(512);
        ObjectOutputStream out = new ObjectOutputStream(bos);

        try {
            out.writeObject(new HashMap<>());
            out.writeInt(-1); // write an invalid length
            out.close();

            InputStream is = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(is);
            in.readObject();
            fail("Expected StreamCorruptedException");

        } catch (StreamCorruptedException e) {
            // Expected
        } finally {
            // Ensure stream resources are closed
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

}