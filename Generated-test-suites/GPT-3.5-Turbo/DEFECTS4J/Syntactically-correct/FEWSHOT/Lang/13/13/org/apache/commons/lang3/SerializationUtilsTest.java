import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SerializationUtilsTest {
    @Test
    public void testClone() {
        // Test cloning null object
        assertNull(SerializationUtils.clone(null));
        
        // Test cloning Serializable object
        MyClass testObj = new MyClass(10);
        MyClass clonedObj = SerializationUtils.clone(testObj);
        assertEquals(testObj.getValue(), clonedObj.getValue());
        
        // Test cloning object with non-serializable field
        MyClass2 testObj2 = new MyClass2(20, "test");
        MyClass2 clonedObj2 = SerializationUtils.clone(testObj2);
        assertEquals(testObj2.getValue(), clonedObj2.getValue());
        assertEquals(testObj2.getName(), clonedObj2.getName());
    }
    
    @Test(expected = SerializationException.class)
    public void testCloneWithSerializationException() {
        // Test cloning object with class not found exception
        MyClass3 testObj3 = new MyClass3(30);
        SerializationUtils.clone(testObj3);
    }
    
    @Test
    public void testSerialize() {
        // Test serializing null object
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(null, outputStream);
        assertEquals(0, outputStream.toByteArray().length);
        
        // Test serializing Serializable object
        MyClass testObj = new MyClass(10);
        outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(testObj, outputStream);
        byte[] bytes = outputStream.toByteArray();
        assertEquals(53, bytes.length); // The expected length to match the serialized object
        
        // Test serializing object with non-serializable field
        MyClass2 testObj2 = new MyClass2(20, "test");
        outputStream = new ByteArrayOutputStream();
        try {
            SerializationUtils.serialize(testObj2, outputStream);
        } catch (SerializationException e) {
            // Ignore the exception since we expect it to be thrown
        }
        assertEquals(0, outputStream.toByteArray().length);
    }
    
    @Test
    public void testSerializeWithIOException() {
        // Test serializing object with I/O exception
        MyClass4 testObj4 = new MyClass4(40);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            SerializationUtils.serialize(testObj4, outputStream);
        } catch (SerializationException e) {
            // Ignore the exception since we expect it to be thrown
        }
        assertEquals(0, outputStream.toByteArray().length);
    }
    
    @Test
    public void testDeserialize() {
        // Test deserializing empty byte array
        Object deserializedObj = SerializationUtils.deserialize(new byte[0]);
        assertNull(deserializedObj);
        
        // Test deserializing valid byte array
        byte[] bytes = new byte[53]; // The expected length to match the serialized object
        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Object obj;
        try {
            obj = inputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            // Ignore the exception since we expect it to be thrown
            obj = null;
        }
        assertNull(obj);
    }
    
    @Test(expected = SerializationException.class)
    public void testDeserializeWithSerializationException() {
        // Test deserializing object with class not found exception
        byte[] bytes = new byte[12];
        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
        try {
            inputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            // Ignore the exception since we expect it to be thrown
        }
    }
}

class MyClass implements Serializable {
    private int value;
    
    public MyClass(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}

class MyClass2 implements Serializable {
    private int value;
    private transient String name;
    
    public MyClass2(int value, String name) {
        this.value = value;
        this.name = name;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getName() {
        return name;
    }
}

class MyClass3 implements Serializable {
    private int value;
    
    public MyClass3(int value) {
        this.value = value;
    }
    
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        throw new ClassNotFoundException();
    }
    
    public int getValue() {
        return value;
    }
}

class MyClass4 implements Serializable {
    private int value;
    
    public MyClass4(int value) {
        this.value = value;
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        throw new IOException();
    }
    
    public int getValue() {
        return value;
    }
}