package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.JsonToken.VALUE_NULL;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.JSONTest;

public class MethodPropertyTest extends JSONTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectCar car = new ObjectCar();
        ObjectMapper mapper = new ObjectMapper();

        AnnotatedMethod am = mock(AnnotatedMethod.class);
        Method m = mock(Method.class);
        when(am.getAnnotated()).thenReturn(m);
        when(m.getName()).thenReturn("setFoo");
        when(m.getParameterTypes()[0]).thenReturn(String.class);

        JsonParser p = createParser("{\"foo\":\"bar\"}", null);
        MethodProperty prop = new MethodProperty(null, car, null, null, am);

        prop.deserializeAndSet(p, null, car);
        assertEquals("bar", car.foo);
    }

    @Test
    public void testDeserializeAndSetNull() throws Exception {
        ObjectCar car = new ObjectCar();
        ObjectMapper mapper = new ObjectMapper();

        AnnotatedMethod am = mock(AnnotatedMethod.class);
        Method m = mock(Method.class);
        when(am.getAnnotated()).thenReturn(m);
        when(m.getName()).thenReturn("setFoo");
        when(m.getParameterTypes()[0]).thenReturn(String.class);

        JsonParser p = createParser("{\"foo\":null}", null);
        MethodProperty prop = new MethodProperty(null, car, null, null, am);

        prop.deserializeAndSet(p, null, car);
        assertNull(car.foo);
    }

    @Test
    public void testDeserializeAndSetNullWithNullProvider() throws Exception {
        ObjectCar car = new ObjectCar();
        ObjectMapper mapper = new ObjectMapper();

        AnnotatedMethod am = mock(AnnotatedMethod.class);
        Method m = mock(Method.class);
        when(am.getAnnotated()).thenReturn(m);
        when(m.getName()).thenReturn("setFoo");
        when(m.getParameterTypes()[0]).thenReturn(String.class);

        NullValueProvider nva = mock(NullValueProvider.class);
        when(nva.getNullValue(null)).thenReturn("nullValue");

        JsonParser p = createParser("{\"foo\":null}", null);
        MethodProperty prop = new MethodProperty(null, car, nva, null, am);

        prop.deserializeAndSet(p, null, car);
        assertEquals("nullValue", car.foo);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectCar car = new ObjectCar();
        ObjectMapper mapper = new ObjectMapper();

        AnnotatedMethod am = mock(AnnotatedMethod.class);
        Method m = mock(Method.class);
        when(am.getAnnotated()).thenReturn(m);
        when(m.getName()).thenReturn("setFoo");
        when(m.getParameterTypes()[0]).thenReturn(String.class);

        String value = "bar";
        when(m.invoke(car, value)).thenReturn(value);

        JsonParser p = createParser("{\"foo\":\"bar\"}", null);
        MethodProperty prop = new MethodProperty(null, car, null, null, am);

        Object result = prop.deserializeSetAndReturn(p, null, car);
        assertEquals(value, result);
    }

    @Test
    public void testDeserializeSetAndReturnNull() throws Exception {
        ObjectCar car = new ObjectCar();
        ObjectMapper mapper = new ObjectMapper();

        AnnotatedMethod am = mock(AnnotatedMethod.class);
        Method m = mock(Method.class);
        when(am.getAnnotated()).thenReturn(m);
        when(m.getName()).thenReturn("setFoo");
        when(m.getParameterTypes()[0]).thenReturn(String.class);

        when(m.invoke(car, null)).thenReturn(null);

        JsonParser p = createParser("{\"foo\":null}", null);
        MethodProperty prop = new MethodProperty(null, car, null, null, am);

        Object result = prop.deserializeSetAndReturn(p, null, car);
        assertNull(result);
    }

    @Test
    public void testDeserializeSetAndReturnNullWithNullProvider() throws Exception {
        ObjectCar car = new ObjectCar();
        ObjectMapper mapper = new ObjectMapper();

        AnnotatedMethod am = mock(AnnotatedMethod.class);
        Method m = mock(Method.class);
        when(am.getAnnotated()).thenReturn(m);
        when(m.getName()).thenReturn("setFoo");
        when(m.getParameterTypes()[0]).thenReturn(String.class);

        NullValueProvider nva = mock(NullValueProvider.class);
        when(nva.getNullValue(null)).thenReturn("nullValue");

        when(m.invoke(car, null)).thenReturn(null);

        JsonParser p = createParser("{\"foo\":null}", null);
        MethodProperty prop = new MethodProperty(null, car, nva, null, am);

        Object result = prop.deserializeSetAndReturn(p, null, car);
        assertEquals("nullValue", result);
    }

    @Test
    public void testSet() throws Exception {
        ObjectCar car = new ObjectCar();

        Method setter = car.getClass().getMethod("setFoo", String.class);

        String value = "bar";
        MethodProperty prop = new MethodProperty(null, car, null, null, null);

        prop.set(car, value);
        assertEquals(value, car.foo);
    }

    @Test
    public void testSetNull() throws Exception {
        ObjectCar car = new ObjectCar();

        Method setter = car.getClass().getMethod("setFoo", String.class);

        MethodProperty prop = new MethodProperty(null, car, null, null, null);

        prop.set(car, null);
        assertNull(car.foo);
    }

    @Test
    public void testSetWithNullProvider() throws Exception {
        ObjectCar car = new ObjectCar();

        Method setter = car.getClass().getMethod("setFoo", String.class);

        NullValueProvider nva = mock(NullValueProvider.class);
        when(nva.getNullValue(null)).thenReturn("nullValue");

        MethodProperty prop = new MethodProperty(null, car, nva, null, null);

        prop.set(car, null);
        assertEquals("nullValue", car.foo);
    }

    @Test
    public void testFixAccess() throws Exception {
        ObjectCar car = new ObjectCar();
        ObjectMapper mapper = new ObjectMapper();

        AnnotatedMethod am = mock(AnnotatedMethod.class);
        when(am.getAnnotated()).thenReturn(car.getClass().getMethod("setFoo", String.class));

        MethodProperty prop = new MethodProperty(null, car, null, null, am);
        prop.fixAccess(mapper.getConfig());

        JsonParser p = createParser("{\"foo\":\"bar\"}", null);
        String value = prop.deserializeAndSet(p, null, car).toString();
        assertEquals("bar", value);
    }

    @Test
    public void testJsonDeserializer() throws Exception {
        ObjectCar car = new ObjectCar();
        ObjectMapper mapper = new ObjectMapper();

        AnnotatedMethod am = mock(AnnotatedMethod.class);
        when(am.getAnnotated()).thenReturn(car.getClass().getMethod("setFoo", String.class));

        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(deserializer.deserialize).thenReturn("bar");
        when(deserializer.deserializeWithType).thenReturn("bar");

        MethodProperty prop = new MethodProperty(null, car, null, null, am, deserializer, null);

        Object result = prop.deserializeSetAndReturn(createParser("{\"foo\":\"bar\"}"), null, car);
        assertEquals("bar", result);
    }

    @Test
    public void testJsonDeserializerWithType() throws Exception {
        ObjectCar car = new ObjectCar();
        ObjectMapper mapper = new ObjectMapper();

        AnnotatedMethod am = mock(AnnotatedMethod.class);
        when(am.getAnnotated()).thenReturn(car.getClass().getMethod("setFoo", String.class));

        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(deserializer.deserializeWithType).thenReturn("bar");

        MethodProperty prop = new MethodProperty(null, car, null, null, am, deserializer, typeDeserializer);

        Object result = prop.deserializeSetAndReturn(createParser("{\"foo\":\"bar\"}"), null, car);
        assertEquals("bar", result);
    }

    @Test
    public void testReadResolve() throws Exception {
        ObjectCar car = new ObjectCar();
        ObjectMapper mapper = new ObjectMapper();

        AnnotatedMethod am = mock(AnnotatedMethod.class);
        Method m = mock(Method.class);
        when(am.getAnnotated()).thenReturn(m);
        when(m.getName()).thenReturn("setFoo");
        when(m.getParameterTypes()[0]).thenReturn(String.class);

        JsonParser p = createParser("{\"foo\":\"bar\"}", null);
        MethodProperty prop = new MethodProperty(null, car, null, null, am);

        Object result = prop.readResolve();
        assertEquals(prop.getClass(), result.getClass());
        MethodProperty resultProp = (MethodProperty) result;
        assertSame(prop._annotated, resultProp._annotated);
        assertSame(prop._setter, resultProp._setter);
        assertSame(prop._skipNulls, resultProp._skipNulls);
    }

    static class ObjectCar {
        String foo;

        @JsonProperty("foo")
        public void setFoo(String value) {
            this.foo = value;
        }
    }
}