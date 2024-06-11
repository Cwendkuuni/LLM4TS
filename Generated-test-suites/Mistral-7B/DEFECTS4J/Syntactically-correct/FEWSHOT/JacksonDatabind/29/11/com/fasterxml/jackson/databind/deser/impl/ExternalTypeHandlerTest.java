package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String json = "{\"foo\":\"intType\",\"bar\":[1,2,3]}";
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimpleBean().getClass().getDeclaredField("foo"), new IntTypeDeserializer())
                .addExternal(new SimpleBean().getClass().getDeclaredField("bar"), new ListTypeDeserializer(Integer.class))
                .build());
        SimpleBean bean = mapper.readValue(new ByteArrayInputStream(json.getBytes()), SimpleBean.class, externalTypeHandler);
        assertEquals(2, bean.getFoo());
        assertEquals(1, bean.getBar().get(0));
        assertEquals(2, bean.getBar().get(1));
        assertEquals(3, bean.getBar().get(2));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        String json = "{\"foo\":1,\"bar\":[1,2,3]}";
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimpleBean().getClass().getDeclaredField("foo"), new IntDeserializer())
                .addExternal(new SimpleBean().getClass().getDeclaredField("bar"), new ListDeserializer(Integer.class))
                .build());
        SimpleBean bean = mapper.readValue(new ByteArrayInputStream(json.getBytes()), SimpleBean.class, externalTypeHandler);
        assertEquals(1, bean.getFoo());
        assertEquals(1, bean.getBar().get(0));
        assertEquals(2, bean.getBar().get(1));
        assertEquals(3, bean.getBar().get(2));
    }

    @Test
    public void testComplete() throws IOException {
        String json = "{\"foo\":null,\"bar\":[1,2,3]}";
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimpleBean().getClass().getDeclaredField("foo"), new NullTypeDeserializer())
                .addExternal(new SimpleBean().getClass().getDeclaredField("bar"), new ListTypeDeserializer(Integer.class))
                .build());
        SimpleBean bean = mapper.readValue(new ByteArrayInputStream(json.getBytes()), SimpleBean.class, externalTypeHandler);
        assertNull(bean.getFoo());
        assertEquals(1, bean.getBar().get(0));
        assertEquals(2, bean.getBar().get(1));
        assertEquals(3, bean.getBar().get(2));
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        String json = "{\"foo\":1,\"bar\":[1,2,3],"
                + "\"creator\":{\"creatorField\":[\"value1\",\"value2\"]}}";
        ObjectMapper mapper = new ObjectMapper();
        SimpleBeanCreator creator = new SimpleBeanCreator();
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimpleBean().getClass().getDeclaredField("foo"), new IntDeserializer())
                .addExternal(new SimpleBean().getClass().getDeclaredField("bar"), new ListDeserializer(Integer.class))
                .build());
        SimpleBean bean = mapper.readValue(new ByteArrayInputStream(json.getBytes()), SimpleBean.class, externalTypeHandler, creator);
        assertEquals(1, bean.getFoo());
        assertEquals(1, bean.getBar().get(0));
        assertEquals(2, bean.getBar().get(1));
        assertEquals(3, bean.getBar().get(2));
        assertEquals("value1", bean.getCreator().getCreatorField().get(0));
        assertEquals("value2", bean.getCreator().getCreatorField().get(1));
    }

    @Test
    public void testMissingTypeId() throws IOException {
        String json = "{\"foo\":null,\"bar\":[1,2,3]}";
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimpleBean().getClass().getDeclaredField("foo"), new NullTypeDeserializer())
                .addExternal(new SimpleBean().getClass().getDeclaredField("bar"), new ListTypeDeserializer(Integer.class))
                .build());
        try {
            SimpleBean bean = mapper.readValue(new ByteArrayInputStream(json.getBytes()), SimpleBean.class, externalTypeHandler);
            fail();
        } catch (MappingException e) {
            assertEquals("Missing property 'foo' for external type id 'null'", e.getMessage());
        }
    }

    @Test
    public void testMissingProperty() throws IOException {
        String json = "{\"foo\":1,\"bar\":null}";
        ObjectMapper mapper = new ObjectMapper();
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimpleBean().getClass().getDeclaredField("foo"), new IntDeserializer())
                .addExternal(new SimpleBean().getClass().getDeclaredField("bar"), new NullTypeDeserializer())
                .build());
        try {
            SimpleBean bean = mapper.readValue(new ByteArrayInputStream(json.getBytes()), SimpleBean.class, externalTypeHandler);
            fail();
        } catch (MappingException e) {
            assertEquals("Missing property 'bar' for external type id 'null'", e.getMessage());
        }
    }

    private static class SimpleBean {
        private int foo;
        private List<Integer> bar;
        private Creator creator;

        public int getFoo() {
            return foo;
        }

        public void setFoo(int foo) {
            this.foo = foo;
        }

        public List<Integer> getBar() {
            return bar;
        }

        public void setBar(List<Integer> bar) {
            this.bar = bar;
        }

        public Creator getCreator() {
            return creator;
        }

        public void setCreator(Creator creator) {
            this.creator = creator;
        }

        private static class Creator {
            private List<String> creatorField;

            public List<String> getCreatorField() {
                return creatorField;
            }

            public void setCreatorField(List<String> creatorField) {
                this.creatorField = creatorField;
            }
        }

        public static class SimpleBeanCreator implements PropertyBasedCreator<SimpleBean> {
            @Override
            public SimpleBean createFromProperties(DeserializationContext ctxt, PropertyValueBuffer valueBuffer) throws IOException {
                SimpleBean bean = new SimpleBean();
                bean.setFoo(valueBuffer.nextIntValue());
                bean.setBar(valueBuffer.nextValue(new ArrayList<>()));
                bean.setCreator(new SimpleBean.Creator());
                return bean;
            }
        }
    }

    private static class IntDeserializer extends StdScalarDeserializer {
        public IntDeserializer() {
            this(null);
        }

        public IntDeserializer(Class<?> vc) {
            super(Int.class, vc);
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsInt();
        }
    }

    private static class IntTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserializeTypedFromObject(DeserializationContext ctxt, Object bean) throws IOException {
            return ctxt.findValueToValueConverter(Int.class).convertFromText(ctxt, null, null);
        }
    }

    private static class ListDeserializer<T> extends BaseDeserializerMapper.BaseDeserializer {
        private final Class<T> _valueClass;

        public ListDeserializer(Class<T> vc) {
            this(vc, null);
        }

        public ListDeserializer(Class<T> vc, TypeDeserializer nullValueHandler) {
            this(vc, nullValueHandler, null);
        }

        public ListDeserializer(Class<T> vc, TypeDeserializer nullValueHandler, JsonDeserializer<List<?>> contentDeserializer) {
            super(Collections.emptyList().getClass(), vc, null);
            _valueClass = vc;
            _nullValueHandler = nullValueHandler;
            _contentDeserializer = contentDeserializer;
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (p.isExpectedStartArrayToken()) {
                TokenBuffer buffer = new TokenBuffer(p);
                Object result = new ArrayList<>();
                if (_contentDeserializer != null) {
                    for (JsonToken t = p.nextToken(); t != null && !p.isExpectedEndArrayToken(); t = p.nextToken()) {
                        if (!p.isCurrentLocationMarshallingEnabled()) {
                            throw ctxt.mappingException("Not handling null values when reading an array");
                        }
                        Object value;
                        if (_nullValueHandler != null) {
                            value = _nullValueHandler.deserializeTypedFromObject(ctxt, null);
                        } else {
                            value = _contentDeserializer.deserialize(p, ctxt);
                        }
                        result.add(value);
                    }
                    p.nextToken(JsonToken.END_ARRAY);
                    return result;
                } else {
                    ArrayNode node = p.getCodec().readTree(p);
                    for (JsonNode element : node) {
                        result.add(ctxt.findValueToValueConverter(_valueClass).convertFromText(ctxt, null, element));
                    }
                    return result;
                }
            } else {
                throw ctxt.mappingException("Not starting with array token: " + p.getCurrentName());
            }
        }
    }

    private static class NullTypeDeserializer extends StdScalarDeserializer {
        public NullTypeDeserializer() {
            this(null);
        }

        public NullTypeDeserializer(Class<?> vc) {
            super(Null.class, vc);
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
}