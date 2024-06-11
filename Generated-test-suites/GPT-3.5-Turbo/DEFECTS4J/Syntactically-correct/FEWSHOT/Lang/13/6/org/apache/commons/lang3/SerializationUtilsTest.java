package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils.ClassLoaderAwareObjectInputStream;
import org.junit.Test;

public class SerializationUtilsTest {

	@Test
	public void testClone() {
		String str = "Hello";
		String clonedStr = SerializationUtils.clone(str);
		assertEquals(str, clonedStr);
	}

	@Test
	public void testSerializeAndDeserialize() {
		String str = "Hello";
		byte[] serializedData = SerializationUtils.serialize(str);
		String deserializedStr = SerializationUtils.deserialize(serializedData);
		assertEquals(str, deserializedStr);
	}

	@Test
	public void testSerialize() {
		String str = "Hello";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SerializationUtils.serialize(str, baos);
		byte[] serializedData = baos.toByteArray();
		String deserializedStr = SerializationUtils.deserialize(serializedData);
		assertEquals(str, deserializedStr);
	}

	@Test
	public void testDeserialize() {
		String str = "Hello";
		byte[] serializedData = serializeString(str);
		String deserializedStr = SerializationUtils.deserialize(serializedData);
		assertEquals(str, deserializedStr);
	}

	@Test
	public void testClassLoaderAwareObjectInputStream() throws IOException, ClassNotFoundException {
		ClassLoaderAwareObjectInputStream in = createObjectInputStream();
		Object deserializedData = in.readObject();
		assertEquals("org.apache.commons.lang3.SerializationUtils$ClassLoaderAwareObjectInputStream", deserializedData.getClass().getName());
	}

	private byte[] serializeString(String str) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(baos);
			out.writeObject(str);
			return baos.toByteArray();
		} catch (IOException ex) {
			throw new SerializationException(ex);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) { // NOPMD
				// ignore close exception
			}
		}
	}

	private ClassLoaderAwareObjectInputStream createObjectInputStream() throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(new byte[0]);
		ClassLoaderAwareObjectInputStream in = new ClassLoaderAwareObjectInputStream(bais, getClass().getClassLoader());
		return in;
	}

}