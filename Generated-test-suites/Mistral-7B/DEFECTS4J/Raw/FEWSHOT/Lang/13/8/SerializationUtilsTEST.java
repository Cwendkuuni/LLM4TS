package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SerializationUtilsTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testClone() throws IOException, ClassNotFoundException {
        // Test cloning a simple class
        Object original = new HashMap<>();
        Object cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);

        // Test cloning a nested class
        class NestedClass implements Serializable {
            private int id = 1;
            private String name = "Nested Class";

            public NestedClass(int id, String name) {
                this.id = id;
                this.name = name;
            }
        }

        NestedClass originalNested = new NestedClass(1, "Nested Class");
        NestedClass clonedNested = SerializationUtils.clone(originalNested);
        assertSame(originalNested, clonedNested);
        assertEquals(originalNested.getId(), clonedNested.getId());
        assertEquals(originalNested.getName(), clonedNested.getName());

        // Test cloning a complex object graph
        class ComplexObject implements Serializable {
            private int id = 1;
            private String name = "Complex Object";
            private ComplexObject nested = new ComplexObject();

            public ComplexObject(int id, String name, ComplexObject nested) {
                this.id = id;
                this.name = name;
                this.nested = nested;
            }
        }

        ComplexObject originalComplex = new ComplexObject(1, "Complex Object", new ComplexObject(2, "Complex Object"));
        ComplexObject clonedComplex = SerializationUtils.clone(originalComplex);
        assertEquals(originalComplex, clonedComplex);
        assertEquals(originalComplex.getId(), clonedComplex.getId());
        assertEquals(originalComplex.getName(), clonedComplex.getName());
        assertSame(originalComplex.getNested(), clonedComplex.getNested());
    }

    @Test
    public void testSerializeDeserializeObject() throws IOException {
        Object original = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(original);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        assertEquals(original, deserialized);
    }

    @Test
    public void testSerializeDeserializeObjectStream() throws IOException, ClassNotFoundException {
        Object original = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(original);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        assertEquals(original, deserialized);

        // Test with custom ClassLoader
        Object customObject = new Object() {
            static final long serialVersionUID = 1L;
        };
        byte[] serializedCustom = SerializationUtils.serialize(customObject);
        Object deserializedCustom = SerializationUtils.deserialize(new ByteArrayInputStream(serializedCustom), CustomClassLoader.class.getClassLoader());
        assertSame(customObject, deserializedCustom);
    }

    @Test
    public void testSerializeDeserializeObjectStreamWithNullInput() {
        assertNull(SerializationUtils.deserialize(null));
    }

    static class CustomClassLoader extends ClassLoader {
        public CustomClassLoader() {
            super(SerializationUtils.class.getClassLoader());
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            return super.findClass(name);
        }
    }

}