package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;
import static org.junit.Assert.*;

public class POJONodeTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testPOJONodeSerialization() throws JsonProcessingException {
        POJONode node = new POJONode(new String("Hello, World!"));
        String json = mapper.writeValueAsString(node);
        assertEquals("{\"@class\":\"com.fasterxml.jackson.databind.node.POJONode\",\"_value\":\"Hello, World!\"}", json);

        POJONode deserializedNode = mapper.readValue(json, POJONode.class);
        assertEquals("Hello, World!", deserializedNode.getPojo());
    }

    @Test
    public void testPOJONodeSerializationWithJsonSerializableObject() throws JsonProcessingException {
        TestJsonSerializable testObj = new TestJsonSerializable();
        POJONode node = new POJONode(testObj);
        String json = mapper.writeValueAsString(node);
        assertEquals("{\"@class\":\"com.fasterxml.jackson.databind.node.POJONode\",\"_value\":{\"name\":\"Test Object\",\"age\":30}}", json);

        POJONode deserializedNode = mapper.readValue(json, POJONode.class);
        TestJsonSerializable deserializedObj = (TestJsonSerializable) deserializedNode.getPojo();
        assertEquals("Test Object", deserializedObj.getName());
        assertEquals(30, deserializedObj.getAge());
    }

    @Test
    public void testBinaryValue() throws IOException {
        byte[] binaryData = {1, 2, 3, 4};
        POJONode node = new POJONode(binaryData);
        byte[] result = node.binaryValue();
        assertArrayEquals(binaryData, result);
    }

    @Test
    public void testToString() {
        POJONode node = new POJONode(new String("Hello, World!"));
        assertEquals("{\"@class\":\"com.fasterxml.jackson.databind.node.POJONode\",\"_value\":\"Hello, World!\"}", node.toString());
    }

    @Test(expected = IOException.class)
    public void testBinaryValueWithNonBinaryValue() throws IOException {
        POJONode node = new POJONode(new String("Hello, World!"));
        node.binaryValue();
    }

    @Test
    public void testEquals() {
        POJONode node1 = new POJONode(new String("Hello, World!"));
        POJONode node2 = new POJONode(new String("Hello, World!"));
        POJONode node3 = new POJONode(new Integer(42));

        assertTrue(node1.equals(node1));
        assertTrue(node1.equals(node2));
        assertFalse(node1.equals(node3));
    }

    @Test
    public void testHashCode() {
        POJONode node1 = new POJONode(new String("Hello, World!"));
        POJONode node2 = new POJONode(new String("Hello, World!"));
        POJONode node3 = new POJONode(new Integer(42));

        int hashCode1 = node1.hashCode();
        int hashCode2 = node2.hashCode();
        int hashCode3 = node3.hashCode();

        assertNotSame(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
    }
}

class TestJsonSerializable {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestJsonSerializable)) return false;
        TestJsonSerializable that = (TestJsonSerializable) o;
        return getName().equals(that.getName()) && getAge() == that.getAge();
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getAge();
        return result;
    }

    @Override
    public String toString() {
        return "TestJsonSerializable{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}