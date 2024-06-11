package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter.MarkerForEmpty;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ConverterUtil;
import com.fasterxml.jackson.databind.util.NameTransformer;

import org.junit.Before;
import org.junit.Test;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        BeanDescription.Standard builder = new BeanDescription.Standard();
        builder.setBeanClass(String.class);
        builder.setClassAnnotations(node);
        beanDesc = builder.buildDescription();
        config = mapper.getSerializationConfig();
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testGetClassAnnotations() {
        Object[] annotations = propertyBuilder.getClassAnnotations().toArray();
        assertNotNull(annotations);
        assertNotNull(annotations[0]);
    }

    @Test
    public void testBuildWriter_Null() {
        assertNull(propertyBuilder.buildWriter(null, null, null, null, null, null, null, false));
    }

    @Test
    public void testBuildWriter_Empty() {
        SerializerProvider prov = new SerializerProvider();
        SerializedString ser = new SerializedString("test");
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("test", null, null, null, null, null, null, null);
        JavaType declaredType = TypeFactory.unknownType();
        assertNull(propertyBuilder.buildWriter(prov, propDef, declaredType, null, null, null, null, false));
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        Annotated a = new AnnotatedClass("com.fasterxml.jackson.databind.ser.PropertyBuilder") {
            @JsonInclude(JsonInclude.Include.NON_DEFAULT)
            @JsonSerialize(using = Converter.class)
            Class<?> clazz = String[].class;
        };
        assertNull(propertyBuilder.findSerializationType(a, false, TypeFactory.type(int.class)));
        JavaType secondary = TypeFactory.type(String[].class);
        propertyBuilder.findSerializationType(a, true, TypeFactory.type(int.class));
        assertNull(propertyBuilder.findSerializationType(a, true, secondary));
        assertNull(propertyBuilder.findSerializationType(a, false, secondary));
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        String defaultBean = "default";
        propertyBuilder._defaultBean = defaultBean;

        Object value = propertyBuilder.getPropertyDefaultValue("test", null, TypeFactory.type(String.class));
        assertNull(value);

        propertyBuilder._defaultBean = new ObjectNode(null);
        value = propertyBuilder.getPropertyDefaultValue("test", null, TypeFactory.type(ObjectNode.class));
        assertNotNull(value);
    }

    @Test
    public void testGetDefaultValue() {
        assertNull(propertyBuilder.getDefaultValue(TypeFactory.type(int.class)));
        assertNull(propertyBuilder.getDefaultValue(TypeFactory.type(String.class)));
        assertNull(propertyBuilder.getDefaultValue(TypeFactory.type(Object.class)));
        assertNull(propertyBuilder.getDefaultValue(TypeFactory.collectionType(int[].class)));
        assertNull(propertyBuilder.getDefaultValue(TypeFactory.collectionType(List.class)));
        assertNull(propertyBuilder.getDefaultValue(TypeFactory.mapType(Map.class, String.class, Integer.class)));
        assertNull(propertyBuilder.getDefaultValue(TypeFactory.referenceType(String.class)));

        assertNotNull(propertyBuilder.getDefaultValue(TypeFactory.valueType(MarkerForEmpty.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_Exception() throws Exception {
        propertyBuilder._defaultBean = null;
        propertyBuilder.getPropertyDefaultValue("test", null, null);
    }

    @Test
    public void testDefaultBean() {
        Object value = propertyBuilder.getDefaultBean();
        assertNull(value);

        Object defaultBean = new ObjectNode(null);
        propertyBuilder._defaultBean = defaultBean;

        value = propertyBuilder.getDefaultBean();
        assertNotNull(value);

        propertyBuilder._defaultBean = null;
    }

    @Test
    public void testFindNullSerializer() throws Exception {
        Converter converter = ConverterUtil.getSerializer(String.class, String.class);
        BeanPropertyWriter.MarkerForNull nullMarker = BeanPropertyWriter.MarkerForNull.getInstance();
        Object serDef = new JsonSerializer<String>(converter) {
            @Override public void serializeValues(SerializerProvider prov, Object bean, JsonSerializer<Object> ser) throws IOException, JsonProcessingException {
                ser.serializeValue(nullMarker, prov);
            }
        };
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        assertNotNull(bpw);
        Object ser = bpw.getNullSerializer();
        assertNotNull(ser);
        assertSame(serDef, ser);
    }

    @Test
    public void testUnwrappingWriter() throws Exception {
        NameTransformer unwrapper = new NameTransformer() {
            @Override public String transform(String name, JsonSerializer<?> serializers) {
                return "transformed_"+name;
            }
        };
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        BeanPropertyWriter unwrapped = bpw.unwrappingWriter(unwrapper);
        assertNotNull(unwrapped);
        assertNotSame(bpw, unwrapped);
    }
}