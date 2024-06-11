package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class InnerClassPropertyTest {

    private static class TestBean {
        private InnerClass inner;

        public InnerClass getInner() {
            return inner;
        }

        public void setInner(InnerClass inner) {
            this.inner = inner;
        }
    }

    private static class InnerClass {
        private int value;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    private static class InnerClassPropertySerializer extends StdSerializer<InnerClassProperty> {
        public InnerClassPropertySerializer() { this(null); }
        public InnerClassPropertySerializer(Class<InnerClassProperty> t) { super(t); }

        @Override
        public void serialize(InnerClassProperty property, JsonGenerator jg, SerializerProvider sp) throws IOException {
            jg.writeStartObject();
            jg.writeName("delegate");
            property.getDelegate().serialize(jg, sp);
            jg.writeEndObject();
        }
    }

    @Test
    public void testInnerClassProperty() throws Exception {
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        node.putPOJO("delegate", new BeanPropertyDefinition(new TestProperty(), null));

        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        mapper.registerSerializer(InnerClassProperty.class, new InnerClassPropertySerializer());

        TestBean bean = mapper.readValue(node.toString(), TestBean.class);
        assertNull(bean.getInner());

        InnerClass ic = new InnerClass();
        ic.setValue(42);
        TestBean beanWithInner = new TestBean();
        beanWithInner.setInner(ic);

        ObjectNode nodeForInner = ObjectMapper.getDefaultInstance().createObjectNode();
        nodeForInner.putPOJO("delegate", new BeanPropertyDefinition(new TestProperty(), null));
        nodeForInner.put("value", 42);

        JsonNode innerNode = mapper.readTree(nodeForInner.toString());
        InnerClassProperty innerProperty = mapper.findValue("delegate", innerNode);

        TestBean beanWithInnerSet = mapper.readValue(node.toString(), TestBean.class, new DeserializationConfig.Builder()
                .withValueDeserializer(mapper.findValue("delegate", node)).build());
        assertSame(ic, beanWithInnerSet.getInner());

        TestBean beanWithSettableInnerProperty = mapper.readValue(node.toString(), TestBean.class);
        SettableBeanProperty delegateProperty = BeanDeserializerFactory.findBeanProperty(TestBean.class, "inner");
        delegateProperty.set(beanWithSettableInnerProperty, innerProperty);
        assertSame(ic, beanWithSettableInnerProperty.getInner());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();

        JsonToken currentToken = JsonToken.START_OBJECT;
        JsonParser mockJsonParser = mock(JsonParser.class);
        when(mockJsonParser.getCurrentToken()).thenReturn(currentToken);

        TestBean bean = mapper.readValue(mockJsonParser, TestBean.class);
        assertNull(bean.getInner());

        InnerClass ic = new InnerClass();
        ic.setValue(42);

        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        InnerClassProperty innerProperty = new InnerClassProperty(null, null, ic.getClass().getConstructors()[0], null);
        InnerClassProperty delegateProperty = new SettableBeanProperty(null, null, null, null, null, null);
        InnerClassProperty innerPropertyWithDelegate = new InnerClassProperty(delegateProperty, innerProperty);

        doAnswer(i -> {
            innerProperty.deserializeAndSet(mockJsonParser, null, bean);
            return null;
        }).when(innerProperty).deserializeAndSet(any(JsonParser.class), any(DeserializationContext.class), any(Object.class));

        when(mockJsonParser.nextToken()).thenReturn(currentToken);

        TestBean beanWithInner = mapper.readValue(mockJsonParser, TestBean.class);
        assertSame(ic, beanWithInner.getInner());

        verify(innerProperty).deserializeAndSet(mockJsonParser, null, bean);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();

        JsonToken currentToken = JsonToken.START_OBJECT;
        JsonParser mockJsonParser = mock(JsonParser.class);
        when(mockJsonParser.getCurrentToken()).thenReturn(currentToken);

        TestBean bean = mapper.readValue(mockJsonParser, TestBean.class);
        assertNull(bean.getInner());

        InnerClass ic = new InnerClass();
        ic.setValue(42);

        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        InnerClassProperty innerProperty = new InnerClassProperty(null, null, ic.getClass().getConstructors()[0], null);
        InnerClassProperty delegateProperty = new SettableBeanProperty(null, null, null, null, null, null);
        InnerClassProperty innerPropertyWithDelegate = new InnerClassProperty(delegateProperty, innerProperty);

        doAnswer(i -> {
            bean = innerProperty.deserializeSetAndReturn(mockJsonParser, null, bean);
            return bean;
        }).when(innerProperty).deserializeSetAndReturn(any(JsonParser.class), any(DeserializationContext.class), any(Object.class));

        when(mockJsonParser.nextToken()).thenReturn(currentToken);

        TestBean beanWithInner = mapper.readValue(mockJsonParser, TestBean.class);
        assertSame(ic, beanWithInner.getInner());

        verify(innerProperty).deserializeSetAndReturn(mockJsonParser, null, bean);
    }
}