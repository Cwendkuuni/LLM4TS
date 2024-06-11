import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.ByteArrayInputStream;
import org.apache.commons.lang3.ByteArrayOutputStream;
import org.apache.commons.lang3.ObjectInputStream;
import org.apache.commons.lang3.ObjectOutputStream;
import org.apache.commons.lang3.ClassLoaderAwareObjectInputStream;
import org.apache.commons.lang3.ClassLoader;
import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectStreamClass;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testClone() {
        Object original = new HashMap<>();
        Object cloned = SerializationUtils.clone(original);
        Assert.assertNotSame(original, cloned);
        Assert.assertEquals(original, cloned);
    }

    @Test
    public void testSerializeNullObject() {
        Assert.assertThrows(IllegalArgumentException.class, () -> SerializationUtils.serialize(null, new ByteArrayOutputStream()));
    }

    @Test
    public void testSerializeNullOutputStream() {
        Assert.assertThrows(IllegalArgumentException.class, () -> SerializationUtils.serialize(new Object(), null));
    }

    @Test
    public void testDeserializeNullInputStream() {
        Assert.assertThrows(IllegalArgumentException.class, () -> SerializationUtils.deserialize(null));
    }

    @Test
    public void testDeserializeNullObject() {
        byte[] objectData = SerializationUtils.serialize(new Object());
        Assert.assertThrows(NullPointerException.class, () -> SerializationUtils.deserialize(null));
    }

    @Test
    public void testDeserializeWrongClass() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        SerializationUtils.serialize(new Object(), baos);

        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        ClassLoaderAwareObjectInputStream ois = new ClassLoaderAwareObjectInputStream(inputStream, null);
        Assert.assertThrows(ClassNotFoundException.class, () -> ois.readObject());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeepCloneComplexObjectGraph() {
        Object original = new HashMap<>();
        original.put(new HashMap<>(), new HashMap<>());

        Object cloned = SerializationUtils.clone(original);
        Assert.assertNotSame(original, cloned);
        Assert.assertEquals(original, cloned);
    }

    @Test
    public void testSerializeManagedFinally() throws IOException {
        Serializable obj = () -> {
            try {
                SerializationUtils.serialize(new Object(), new ByteArrayOutputStream());
            } finally {
                throw new RuntimeException("Managed exception");
            }};

        Assert.assertThrows(RuntimeException.class, () -> SerializationUtils.serialize(obj, new ByteArrayOutputStream()));
    }

    @Test
    public void testSerializeManagedIOException() throws IOException {
        Serializable obj = new Object() {
            @Override
            public void writeObject(ObjectOutputStream out) throws IOException {
                throw new IOException("Managed IOException");
            }
        };

        Assert.assertThrows(IOException.class, () -> SerializationUtils.serialize(obj, new ByteArrayOutputStream()));
    }

    @Test
    public void testDeserializeManagedFinally() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        SerializationUtils.serialize(new Object() {
            @Override
            protected void finalize() throws Throwable {
                throw new RuntimeException("Managed exception");
            }
        }, baos);

        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        Assert.assertThrows(RuntimeException.class, () -> SerializationUtils.deserialize(inputStream));
    }

    @Test
    public void testDeserializeManagedIOException() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        SerializationUtils.serialize(new Object() {
            @Override
            public void writeObject(ObjectOutputStream out) throws IOException {
                throw new IOException("Managed IOException");
            }
        }, baos);

        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        Assert.assertThrows(IOException.class, () -> SerializationUtils.deserialize(inputStream));
    }

    @Test
    public void testDeserializeWrongClassLoader() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        SerializationUtils.serialize(new Object(), baos);

        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        ClassLoader wrongClassLoader = new ClassLoader() {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                throw new ClassNotFoundException("Wrong class loader");
            }
        };
        ClassLoaderAwareObjectInputStream ois = new ClassLoaderAwareObjectInputStream(inputStream, wrongClassLoader);
        Assert.assertThrows(ClassNotFoundException.class, () -> ois.readObject());
    }

}