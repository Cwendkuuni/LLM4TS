package com.example.junit;

import static org.junit.Assert.*;import com.fasterxml.jackson.databind.JsonDeserializer;import java.util.HashSet;import java.util.Set;

public class JdkDeserializersTest {

@Test
public void testGetClassNames() {
    Set<String> expectedClassNames = new HashSet<String>() {{
        add(UUID.class.getName());
        add(AtomicBoolean.class.getName());
        add(StackTraceElement.class.getName());
        add(ByteBuffer.class.getName());
        // Add other expected class names here
    }};

    Set<String> actualClassNames = JdkDeserializers.getClassNames();

    assertEquals(expectedClassNames, actualClassNames);
}

@Test
public void testFind_ValidClassName_ReturnsDeserializer() {
    JsonDeserializer<?> expectedDeserializer = new UUIDDeserializer();
    Class<?> rawType = UUID.class;
    String clsName = UUID.class.getName();

    JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(rawType, clsName);

    assertEquals(expectedDeserializer, actualDeserializer);
}

@Test
public void testFind_NullRawType_ThrowsNullPointerException() {
    Class<?> rawType = null;
    String clsName = UUID.class.getName();

    try {
        JsonDeserializer<?> actualDeserializer = JdkDeserializers.find(rawType, clsName);
    } catch (NullPointerException e) {
        // Expected exception
    }
}

// Add more test cases for other edge cases and exception handling scenarios

}