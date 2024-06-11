package org.apache.commons.lang3.test;

 import org.apache.commons.lang3.SerializationUtils;
 import org.apache.commons.lang3.serialization.ClassLoaderAwareObjectInputStream;
 import org.apache.commons.lang3.serialization.SerializationException;
 import org.junit.Assert;
 import org.junit.Test;
 import org.junit.runner.RunWith;
 import org.junit.runners.Parameterized;
 import org.junit.runners.Parameterized.Parameters;

 import java.io.ByteArrayInputStream;
 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.io.ObjectInputStream;
 import java.io.ObjectOutputStream;
 import java.io.OutputStream;
 import java.io.Serializable;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.Collection;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;

 @RunWith(Parameterized.class)
 public class SerializationUtilsTest {

 private static final long serialVersionUID = 1L;

 @Parameters(name = "{index}: testCase{0}")
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][]{
 // Test cases for clone method
 new Object[]{" clone(Object) method, valid object", new Object(){}} ,
 new Object[]{" clone(Object) method, null object", null},
 new Object[]{" clone(Object) method, empty Object[]", new Object[0]},
 new Object[]{" clone(Object) method, Object[] with one element", new Object[]{1}},
 new Object[]{" clone(Object) method, Object[] with multiple elements", new Object[]{1, 2, 3}},
 // Test cases for serialize method
 new Object[]{" serialize(Serializable) method, valid Serializable object", new Object(){}} ,
 new Object[]{" serialize(Serializable) method, null Serializable object", null},
 new Object[]{" serialize(Serializable) method, empty Serializable object", new String[]{}},
 new Object[]{" serialize(Serializable) method, Serializable object with simple data types", new Integer(1)},
 new Object[]{" serialize(Serializable) method, Serializable object with complex data types", new HashMap<String, Integer>()},
 // Test cases for deserialize method
 new Object[]{" deserialize(InputStream) method, valid InputStream", new ByteArrayInputStream(SerializationUtils.serialize(new Object(){}))},
 new Object[]{" deserialize(InputStream) method, empty InputStream", null},
 new Object[]{" deserialize(InputStream) method, null InputStream", null},
 new Object[]{" deserialize(InputStream) method, InputStream with invalid data", new ByteArrayInputStream(new byte[0])},
 new Object[]{" deserialize(InputStream) method, InputStream with ClassNotFoundException", new ByteArrayInputStream(new byte[]{})},
 new Object[]{" deserialize(InputStream) method, InputStream with IOException", new ByteArrayInputStream(new byte[]{})},
 new Object[]{" deserialize(byte[]) method, valid byte[]", new byte[]{}},
 new Object[]{" deserialize(byte[]) method, empty byte[]", null},
 new Object[]{" deserialize(byte[]) method, null byte[]", null},
 new Object[]{" deserialize(byte[]) method, byte[] with invalid data", new byte[]{}}
 });
 }

 @Test
 public void testSerializationUtils() {
 // Test clone method
 Object original = new Object(){};
 Object cloned = SerializationUtils.clone(original);
 Assert.assertNotSame(original, cloned);
 Assert.assertEquals(original, cloned);

 // Test serialize method
 Serializable objectToSerialize = new Object(){};
 byte[] serializedData = SerializationUtils.serialize(objectToSerialize);
 Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(serializedData));
 Assert.assertEquals(objectToSerialize, deserializedObject);

 // Test deserialize method
 // Test ClassLoaderAwareObjectInputStream
 ObjectInputStream inputStream = new ClassLoaderAwareObjectInputStream(new ByteArrayInputStream(serializedData), SerializationUtils.class.getClassLoader());
 Object deserializedObjectFromStream = inputStream.readObject();
 Assert.assertEquals(objectToSerialize, deserializedObjectFromStream);
 }

 @Test(expected = SerializationException.class)
 public void testSerializationException() throws SerializationException {
 // Test serialize method with null input
 SerializationUtils.serialize(null);

 // Test deserialize method with empty InputStream
 SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0]));

 // Test deserialize method with null InputStream
 SerializationUtils.deserialize(null);

 // Test deserialize method with InputStream with invalid data
 SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0]));

 // Test deserialize method with InputStream with ClassNotFoundException
 SerializationUtils.deserialize(new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5}));

 // Test deserialize method with InputStream with IOException
 SerializationUtils.deserialize(new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5}));
 }

 }