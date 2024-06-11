package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({StdKeyDeserializer.class})
public class StdKeyDeserializerTest {

    @InjectMocks
    private StdKeyDeserializer stdKeyDeserializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDeserializeBasicTypes() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Test Boolean
        AnnotatedClass booleanClassAnnotation = mock(AnnotatedClass.class);
        when(booleanClassAnnotation.findMethod(eq("getValue"))).thenReturn(mock(AnnotatedMethod.class));
        doAnswer(invocation -> Boolean.TRUE).when(booleanClassAnnotation).getValue(any());
        when(booleanClassAnnotation.getRawClass()).thenReturn(Boolean.class);
        StdKeyDeserializer booleanDeserializer = PowerMockito.spy(StdKeyDeserializer.forType(Boolean.class));
        assertEquals(Boolean.TRUE, booleanDeserializer.deserializeKey("true", mock(DeserializationContext.class)));
        verify(booleanDeserializer)._parse("true", any());

        // Test Byte
        AnnotatedClass byteClassAnnotation = mock(AnnotatedClass.class);
        when(byteClassAnnotation.findMethod(eq("getValue"))).thenReturn(mock(AnnotatedMethod.class));
        doAnswer(invocation -> Byte.parseByte(invocation.getArgument(0).toString())).when(byteClassAnnotation).getValue(any());
        when(byteClassAnnotation.getRawClass()).thenReturn(Byte.class);
        StdKeyDeserializer byteDeserializer = PowerMockito.spy(StdKeyDeserializer.forType(Byte.class));
        assertEquals((byte) 0, byteDeserializer.deserializeKey("0", mock(DeserializationContext.class)));
        assertEquals((byte) 127, byteDeserializer.deserializeKey("127", mock(DeserializationContext.class)));
        verify(byteDeserializer)._parse("0", any());
        verify(byteDeserializer)._parse("127", any());

        // Test Short
        AnnotatedClass shortClassAnnotation = mock(AnnotatedClass.class);
        when(shortClassAnnotation.findMethod(eq("getValue"))).thenReturn(mock(AnnotatedMethod.class));
        doAnswer(invocation -> Short.parseShort(invocation.getArgument(0).toString())).when(shortClassAnnotation).getValue(any());
        when(shortClassAnnotation.getRawClass()).thenReturn(Short.class);
        StdKeyDeserializer shortDeserializer = PowerMockito.spy(StdKeyDeserializer.forType(Short.class));
        assertEquals((short) 0, shortDeserializer.deserializeKey("0", mock(DeserializationContext.class)));
        assertEquals((short) 255, shortDeserializer.deserializeKey("255", mock(DeserializationContext.class)));
        verify(shortDeserializer)._parse("0", any());
        verify(shortDeserializer)._parse("255", any());

        // Test Char
        AnnotatedClass charClassAnnotation = mock(AnnotatedClass.class);
        when(charClassAnnotation.findMethod(eq("getValue"))).thenReturn(mock(AnnotatedMethod.class));
        doAnswer(invocation -> Character.valueOf(invocation.getArgument(0).charAt(0))).when(charClassAnnotation).getValue(any());
        when(charClassAnnotation.getRawClass()).thenReturn(Character.class);
        StdKeyDeserializer charDeserializer = PowerMockito.spy(StdKeyDeserializer.forType(Character.class));
        assertEquals('A', charDeserializer.deserializeKey("A", mock(DeserializationContext.class)));
        verify(charDeserializer)._parse("A", any());

        // Test Int
        AnnotatedClass intClassAnnotation = mock(AnnotatedClass.class);
        when(intClassAnnotation.findMethod(eq("getValue"))).thenReturn(mock(AnnotatedMethod.class));
        doAnswer(invocation -> Integer.parseInt(invocation.getArgument(0).toString())).when(intClassAnnotation).getValue(any());
        when(intClassAnnotation.getRawClass()).thenReturn(Integer.class);
        StdKeyDeserializer intDeserializer = PowerMockito.spy(StdKeyDeserializer.forType(Integer.class));
        assertEquals(0, intDeserializer.deserializeKey("0", mock(DeserializationContext.class)));
        assertEquals(12345, intDeserializer.deserializeKey("12345", mock(DeserializationContext.class)));
        verify(intDeserializer)._parse("0", any());
        verify(intDeserializer)._parse("12345", any());

        // Test Long
        AnnotatedClass longClassAnnotation = mock(AnnotatedClass.class);
        when(longClassAnnotation.findMethod(eq("getValue"))).thenReturn(mock(AnnotatedMethod.class));
        doAnswer(invocation -> Long.parseLong(invocation.getArgument(0).toString())).when(longClassAnnotation).getValue(any());
        when(longClassAnnotation.getRawClass()).thenReturn(Long.class);
        StdKeyDeserializer longDeserializer = PowerMockito.spy(StdKeyDeserializer.forType(Long.class));
        assertEquals(0L, longDeserializer.deserializeKey("0", mock(DeserializationContext.class)));
        assertEquals(123456789012345L, longDeserializer.deserializeKey("123456789012345", mock(DeserializationContext.class)));
        verify(longDeserializer)._parse("0", any());
        verify(longDeserializer)._parse("123456789012345", any());
    }

    @Test
    public void testDeserializeString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerValueHandlerForClasses(String.class, new ToStringSerializer(String.class));

        String key = "test";
        String deserializedKey = stdKeyDeserializer.deserializeKey(key, mock(DeserializationContext.class));
        assertEquals(key, deserializedKey);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeUnknownType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String unknownTypeKey = "unknownType:123";
        stdKeyDeserializer.deserializeKey(unknownTypeKey, mock(DeserializationContext.class));
    }
}