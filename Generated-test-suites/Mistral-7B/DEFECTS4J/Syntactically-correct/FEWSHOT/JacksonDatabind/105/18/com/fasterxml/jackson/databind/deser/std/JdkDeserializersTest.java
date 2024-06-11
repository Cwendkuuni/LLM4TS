package com.fasterxml.jackson.databind.deser.std;

import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.FromStringDeserializer.DeserializerInfo;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.FromStringDeserializerBase;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer.FromStringDeserializerWithParams;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.junit.Assert.*;

public class JdkDeserializersTest {

    private static Set<String> _classNames;
    static {
        // note: can skip primitive types; other ways to check them:
        Class<?>[] types = new Class<?>[] {
                UUID.class,
                AtomicBoolean.class,
                StackTraceElement.class,
                ByteBuffer.class
        };
        _classNames = new HashSet<>();
        for (Class<?> cls : types) { _classNames.add(cls.getName()); }
        for (Class<?> cls : FromStringDeserializer.types()) { _classNames.add(cls.getName()); }
    }

    @BeforeClass
    public static void setup() {
        JsonFactory factory = new JsonFactory();
        AnnotationIntrospector introspector = new StandardAnnotationIntrospector();
        ObjectMapper mapper = new ObjectMapper(factory);
        mapper.setAnnotationIntrospector(introspector);
        mapper.findValueDeserializer(UUID.class); // force initialization
        mapper.findValueDeserializer(StackTraceElement.class);
        mapper.findValueDeserializer(AtomicBoolean.class);
        mapper.findValueDeserializer(ByteBuffer.class);
    }

    @Test
    public void testFindNull() {
        assertNull(JdkDeserializers.find(null, null));
    }

    @Test
    public void testFindNotRegisteredType() {
        assertNull(JdkDeserializers.find(String.class, "com.foo.Foo"));
    }

    @Test
    public void testFindUUID() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        assertNotNull(deserializer);
        assertEquals(UUIDDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindAtomicBoolean() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        assertNotNull(deserializer);
        assertEquals(AtomicBooleanDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindStackTraceElement() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        assertNotNull(deserializer);
        assertEquals(StackTraceElementDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindByteBuffer() {
        JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        assertNotNull(deserializer);
        assertEquals(ByteBufferDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindFromStringDeserializer() {
        Class<?> type = Object.class;
        String clsName = ObjectNode.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(type, clsName);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof FromStringDeserializerWithParams);
        DeserializerInfo info = ((FromStringDeserializerWithParams) deserializer).info();
        assertEquals(ObjectNode.class, info.getParameterType(0).getRawClass());
    }

}