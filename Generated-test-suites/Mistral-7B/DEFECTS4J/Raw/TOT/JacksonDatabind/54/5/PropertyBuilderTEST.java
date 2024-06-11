package com.fasterxml.jackson.databind.ser;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class PropertyBuilderTest {

    private PropertyBuilder _builder;
    private SerializationConfig _config;
    private ObjectMapper _mapper;

    @Before
    public void setup() throws Exception {
        _config = new SerializationConfig();
        _config.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        _config.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        _mapper = new ObjectMapper(_config);
    }

    @Test
    public void testPropertyBuilder_simple() throws Exception {
        BeanDescription beanDesc = new BeanDescription(SomeClass.class);
        PropertyBuilder builder = new PropertyBuilder(_config, beanDesc);
        SerializerProvider prov = _mapper.getSerializerProvider();
        BeanPropertyWriter writer = builder.buildWriter(prov, new BeanPropertyDefinition(null), null, null, null, null, null, false);
        assertEquals("simpleWriter", writer.getName());
    }

    @Test
    public void testPropertyBuilder_container() throws Exception {
        BeanDescription beanDesc = new BeanDescription(SomeListClass.class);
        PropertyBuilder builder = new PropertyBuilder(_config, beanDesc);
        SerializerProvider prov = _mapper.getSerializerProvider();
        TypeSerializer typeSerializer = _mapper.getTypeSerializer(_config, new TypeReference<List<String>>() {});
        BeanPropertyWriter writer = builder.buildWriter(prov, new BeanPropertyDefinition(null), null, null, null, null, null, false).withContentSerializer(typeSerializer);
        assertEquals("containerWriter", writer.getName());

        ObjectWriter out = _mapper.writerWithView(_config, new SimpleBeanPropertyView());
        assertEquals("[]", out.writeValueAsString(writer));
    }

    @Test
    public void testPropertyBuilder_suppressNulls() throws Exception {
        BeanDescription beanDesc = new BeanDescription(SomeClassWithNulls.class);
        PropertyBuilder builder = new PropertyBuilder(_config, beanDesc);
        SerializerProvider prov = _mapper.getSerializerProvider();
        BeanPropertyWriter writer = builder.buildWriter(prov, new BeanPropertyDefinition(null), null, null, null, null, null, true);

        assertNull(writer.getValueToSuppress());
        assertEquals("SomeClassWithNulls@123", writer.getSerializer().serializeValue(_mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false), null));
        assertEquals("SomeClassWithNulls@456", writer.getSerializer().serializeValue(_mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false), new SomeClassWithNulls()));
    }

    @Test
    public void testPropertyBuilder_useDefaults() throws Exception {
        BeanDescription beanDesc = new BeanDescription(SomeClassWithDefaults.class);
        PropertyBuilder builder = new PropertyBuilder(_config, beanDesc);
        SerializerProvider prov = _mapper.getSerializerProvider();

        Object value = builder.buildWriter(prov, new BeanPropertyDefinition(null), null, null, null, null, null, false).getValueToSuppress();
        assertSame(Integer.class, value.getClass());
        assertEquals(0, value.intValue());

        Object serialized = builder.buildWriter(prov, new BeanPropertyDefinition(null), null, null, null, null, null, false).getSerializer().serializeValue(_mapper, null);
        assertNull(serialized);
    }

    @Test
    public void testPropertyBuilder_nonDefault() throws Exception {
        BeanDescription beanDesc = new BeanDescription(SomeClassWithDefaults.class);
        PropertyBuilder builder = new PropertyBuilder(_config, beanDesc);
        SerializerProvider prov = _mapper.getSerializerProvider();

        Object value = builder.buildWriter(prov, new BeanPropertyDefinition(null), null, null, null, null, null, false).getValueToSuppress();
        assertSame(Integer.class, value.getClass());
        assertEquals(1, value.intValue());

        Object serialized = builder.buildWriter(prov, new BeanPropertyDefinition(null), null, null, null, null, null, false).getSerializer().serializeValue(_mapper, null);
        assertNull(serialized);

        SomeClassWithDefaults instance = new SomeClassWithDefaults(2);
        Object serializedInstance = builder.buildWriter(prov, new BeanPropertyDefinition(null), null, null, null, null, null, false).getSerializer().serializeValue(_mapper, instance);
        assertEquals(2, serializedInstance.intValue());
    }

    @Test
    public void testPropertyBuilder_nonEmpty() throws Exception {
        BeanDescription beanDesc = new BeanDescription(SomeClassWithEmpty.class);
        PropertyBuilder builder = new PropertyBuilder(_config, beanDesc);
        SerializerProvider prov = _mapper.getSerializerProvider();

        Object serialized = builder.buildWriter(prov, new BeanPropertyDefinition(null), null, null, null, null, null, false).getSerializer().serializeValue(_mapper, null);
        assertNull(serialized);

        SomeClassWithEmpty instance = new SomeClassWithEmpty();
        Object serializedInstance = builder.buildWriter(prov, new BeanPropertyDefinition(null), null, null, null, null, null, false).getSerializer().serializeValue(_mapper, instance);
        assertEquals("", serializedInstance);
    }

    @Test
    public void testPropertyBuilder_nonAbsent() throws Exception {
        BeanDescription beanDesc = new BeanDescription(SomeClassWithOptional.class);
        PropertyBuilder builder = new PropertyBuilder(_config, beanDesc);
        SerializerProvider prov = _mapper.getSerializerProvider();

        Object serialized = builder.buildWriter(prov, new BeanPropertyDefinition(null), null, null, null, null, null, false).getSerializer().serializeValue(_mapper, null);
        assertNull(serialized);

        SomeClassWithOptional instance = new SomeClassWithOptional();
        Object serializedInstance = builder.buildWriter(prov, new BeanPropertyDefinition(null), null, null, null, null, null, false).getSerializer().serializeValue(_mapper, instance);
        assertNull(serializedInstance);

        SomeClassWithOptional instanceWithValue = new SomeClassWithOptional("someValue");
        Object serializedInstanceWithValue = builder.buildWriter(prov, new BeanPropertyDefinition(null), null, null, null, null, null, false).getSerializer().serializeValue(_mapper, instanceWithValue);
        assertEquals("someValue", serializedInstanceWithValue);
    }

    @Test
    public void testPropertyBuilder_customNullSerializer() throws Exception {
        BeanDescription beanDesc = new BeanDescription(SomeClassWithNullSerializer.class);
        PropertyBuilder builder = new PropertyBuilder(_config, beanDesc);
        SerializerProvider prov = _mapper.getSerializerProvider();

        Object serialized = builder.buildWriter(prov, new BeanPropertyDefinition(null, new JsonProperty("nullValue")), null, null, null, null, null, false).getSerializer().serializeValue(_mapper, null);
        assertEquals("nullValue", serialized);

        SomeClassWithNullSerializer instance = new SomeClassWithNullSerializer(null);
        Object serializedInstance = builder.buildWriter(prov, new BeanPropertyDefinition(null, new JsonProperty("nullValue")), null, null, null, null, null, false).getSerializer().serializeValue(_mapper, instance);
        assertEquals("nullValue", serializedInstance);

        SomeClassWithNullSerializer instanceWithValue = new SomeClassWithNullSerializer("someValue");
        Object serializedInstanceWithValue = builder.buildWriter(prov, new BeanPropertyDefinition(null, new JsonProperty("nullValue")), null, null, null, null, null, false).getSerializer().serializeValue(_mapper, instanceWithValue);
        assertEquals("someValue", serializedInstanceWithValue);
    }

    static class SomeClass {
        @JsonProperty("simpleWriter")
        public String getSimpleWriter() {
            return "simpleWriter";
        }

        @JsonProperty("simpleWriter")
        public void setSimpleWriter(String value) {
        }
    }

    static class SomeListClass {
        @JsonProperty("containerWriter")
        private List<String> _list;

        @JsonProperty("containerWriter")
        public List<String> getContainerWriter() {
            return _list;
        }

        @JsonProperty("containerWriter")
        public void setContainerWriter(List<String> value) {
            _list = value;
        }
    }

    static class SomeClassWithNulls {
        private Integer _null;

        public SomeClassWithNulls() {
            _null = null;
        }

        public SomeClassWithNulls(Integer value) {
            _null = value;
        }

        @JsonProperty("nulls")
        public Integer getNulls() {
            return _null;
        }

        @JsonProperty("nulls")
        public void setNulls(Integer value) {
            _null = value;
        }
    }

    static class SomeClassWithDefaults {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private Integer _default = 0;

        public SomeClassWithDefaults() {
        }

        public SomeClassWithDefaults(Integer value) {
            _default = value;
        }

        @JsonProperty("defaults")
        public Integer getDefaults() {
            return _default;
        }

        @JsonProperty("defaults")
        public void setDefaults(Integer value) {
            _default = value;
        }
    }

    static class SomeClassWithEmpty {
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private String _empty;

        public SomeClassWithEmpty() {
            _empty = "";
        }

        public SomeClassWithEmpty(String value) {
            _empty = value;
        }

        @JsonProperty("empty")
        public String getEmpty() {
            return _empty;
        }

        @JsonProperty("empty")
        public void setEmpty(String value) {
            _empty = value;
        }
    }

    static class SomeClassWithOptional {
        @JsonInclude(JsonInclude.Include.NON_ABSENT)
        private String _optional;

        public SomeClassWithOptional() {
        }

        public SomeClassWithOptional(String value) {
            _optional = value;
        }

        @JsonProperty("optional")
        public String getOptional() {
            return _optional;
        }

        @JsonProperty("optional")
        public void setOptional(String value) {
            _optional = value;
        }
    }

    static class SomeClassWithNullSerializer {
        private String _nullValue;

        public SomeClassWithNullSerializer() {
        }

        public SomeClassWithNullSerializer(String value) {
            _nullValue = value;
        }

        @JsonProperty("nullValue")
        public String getNullValue() {
            return _nullValue;
        }

        @JsonProperty("nullValue")
        public void setNullValue(String value) {
            _nullValue = value;
        }

        @JsonSerialize(using = SomeNullSerializer.class)
        public String getNullSerializerValue() {
            return null;
        }
    }

    static class SomeNullSerializer extends StdSerializer<Object> {
        public SomeNullSerializer() {
            this(null);
        }

        public SomeNullSerializer(Class<Object> t) {
            this(null);
        }

        public SomeNullSerializer(Class<Object> t, PropertyWriter writer) {
            super(t, writer);
        }

        @Override
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider prov) throws IOException {
            jgen.writeString("nullValue");
        }
    }
}