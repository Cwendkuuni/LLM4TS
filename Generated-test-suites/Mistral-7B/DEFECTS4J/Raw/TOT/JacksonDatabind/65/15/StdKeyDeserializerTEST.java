package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class StdKeyDeserializerTest {

    private DeserializationContext context;
    private ObjectMapper mapper;
    private StdKeyDeserializer stringKD;
    private StdKeyDeserializer intKD;
    private StdKeyDeserializer dateKD;
    private StdKeyDeserializer uriKD;

    @Before
    public void setUp() throws Exception {
        context = mock(DeserializationContext.class);
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        stringKD = StdKeyDeserializer.forType(String.class);
        intKD = StdKeyDeserializer.forType(Integer.class);
        dateKD = StdKeyDeserializer.forType(Date.class);
        uriKD = StdKeyDeserializer.forType(URI.class);
    }

    @Test
    public void testStringKD() throws IOException {
        String key = "test";
        assertEquals(key, stringKD.deserializeKey(key, context));
    }

    @Test
    public void testIntKD_ValidInput() throws IOException {
        String key = "123";
        assertEquals(Integer.valueOf(123), intKD.deserializeKey(key, context));
    }

    @Test
    public void testIntKD_InvalidInput() throws IOException {
        String key = "invalid";
        assertNull(intKD.deserializeKey(key, context));
    }

    @Test
    public void testDateKD() throws IOException {
        String key = "2022-01-01";
        java.util.Date date = new Date(0L);
        date.setTime(System.currentTimeMillis());
        assertEquals(date, dateKD.deserializeKey(key, context));
    }

    @Test(expected = IOException.class)
    public void testDateKD_InvalidInput() throws IOException {
        String key = "invalid_date";
        dateKD.deserializeKey(key, context);
    }

    @Test
    public void testURIKD() throws IOException {
        String key = "http://test.com";
        URI uri = new URI(key);
        assertEquals(uri, uriKD.deserializeKey(key, context));
    }

    @Test(expected = IOException.class)
    public void testURIKD_InvalidInput() throws IOException {
        String key = "invalid_uri";
        uriKD.deserializeKey(key, context);
    }

    @Test
    public void testDeserializerOfAnnotatedClass() throws IOException {
        @JsonDeserialize(using = StdKeyDeserializer.class)
        static class TestClass {
            private String key;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }
        }

        AnnotatedClass ac = mapper.getSerializationConfig().findShape(TestClass.class);
        AnnotatedMethod am = ac.findMethod("setKey", TestClass.class);
        StdKeyDeserializer deserializer = (StdKeyDeserializer) am.getAnnotation(JsonDeserialize.class).getDeserializer();

        TestClass testClass = new TestClass();
        String key = "test";
        testClass.setKey(key);

        assertEquals(key, deserializer.deserializeKey(key, context));
    }

    @Test
    public void testDeserializerOfAnnotatedMethod() throws IOException {
        @JsonSerialize(using = ToStringSerializer.class)
        static class TestClass {
            private String key;

            @JsonDeserialize(using = StdKeyDeserializer.class)
            public String getKey() {
                return key;
            }
        }

        AnnotatedMethod am = mapper.getTypeFactory().constructType(TestClass.class).getDeclaredMethod("getKey");
        StdKeyDeserializer deserializer = (StdKeyDeserializer) am.getAnnotation(JsonDeserialize.class).getDeserializer();

        TestClass testClass = new TestClass();
        String key = "test";
        assertEquals(key, deserializer.deserializeKey(key, context));
    }

    @Test
    public void testDelegateDeserializer() throws IOException {
        static class DelegateClass {
            private String key;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }
        }

        class DelegateDeserializer extends StdKeyDeserializer {
            private final JsonDeserializer<DelegateClass> delegate;

            DelegateDeserializer(Class<?> cls, JsonDeserializer<DelegateClass> delegate) {
                super(-1, cls);
                this.delegate = delegate;
            }

            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                DelegateClass delegateClass = delegate.deserialize(ctxt.getParser(), ctxt);
                return delegateClass.getKey();
            }
        }

        AnnotatedClass ac = mapper.getSerializationConfig().findShape(DelegateClass.class);
        AnnotatedMethod am = ac.findMethod("getKey", DelegateClass.class);
        JsonDeserializer<DelegateClass> delegateDeserializer = mapper.findValueDeserializer(ac, am);
        DelegateDeserializer keyDeserializer = new DelegateDeserializer(DelegateClass.class, delegateDeserializer);

        DelegateClass delegateClass = new DelegateClass();
        String key = "test";
        delegateClass.setKey(key);

        assertEquals(key, keyDeserializer.deserializeKey(key, context));
    }
}