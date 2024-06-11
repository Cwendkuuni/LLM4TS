import com.fasterxml.jackson.core.SerializationConfig;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;

    @Before
    public void setup() {
        config = new SerializationConfig();
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.putProperty("name", "John Doe");
        node.putProperty("age", 30);
        beanDesc = new BeanDescription(new ClassName("TestBean"), node);
    }

    @Test
    public void testBuilder() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotations annotations = propertyBuilder.getClassAnnotations();
        Assert.assertEquals(annotations.getClassAnnotations().size(), 0);

        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        Assert.assertNotNull(writer);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        AnnotatedMember member = new AnnotatedMember(new ClassName("TestBean"), "name");
        JavaType declaredType = TypeFactory.unknownType();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType serializationType = propertyBuilder.findSerializationType(member, false, declaredType);
        Assert.assertEquals(serializationType.getRawClass(), String.class);
    }

    @Test
    public void testGetClassAnnotations() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotations annotations = propertyBuilder.getClassAnnotations();
        JsonSerialize annotation = annotations.findAnnotation(JsonSerialize.class.getName());
        Assert.assertNotNull(annotation);
    }

    @Test
    public void testGetDefaultBean() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNotNull(defaultBean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultBeanWithNull() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
        Object defaultBean = propertyBuilder.getDefaultBean();
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        String name = "name";
        Object defaultBean = propertyBuilder.getDefaultBean();
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, null, beanDesc.getType());
        Assert.assertEquals(defaultValue, "");

        String value = "Test";
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put(name, value);
        beanDesc = new BeanDescription(new ClassName("TestBean"), node);
        defaultValue = propertyBuilder.getPropertyDefaultValue(name, null, beanDesc.getType());
        Assert.assertEquals(defaultValue, value);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType type = TypeFactory.unknownType();
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        if (type.isPrimitiveClick()) {
            Assert.assertEquals(defaultValue, 0);
        } else if (type.isReferenceType()) {
            Assert.assertEquals(defaultValue, BeanPropertyWriter.MARKER_FOR_EMPTY);
        } else if (type.isContainerType()) {
            Assert.assertEquals(defaultValue, JsonInclude.Include.NON_EMPTY);
        } else {
            Assert.assertEquals(defaultValue, "");
        }
    }

    @Test
    public void testFindSerializationTyping() {
        Annotated a = new AnnotatedMember(new ClassName("TestBean"), "name");
        JsonSerialize.Typing typing = new PropertyBuilder(config, beanDesc).findSerializationTyping(a);
        Assert.assertEquals(typing, JsonSerialize.Typing.DEFAULT_TYPING);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(new ClassName("TestBean"), "name");
        JavaType declaredType = beanDesc.getType();
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        Boolean defaultUseStaticTyping = false;

        BeanPropertyWriter writer = propertyBuilder.buildWriter(provider, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        Assert.assertNotNull(writer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWriterWithNullDeclaredType() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(new ClassName("TestBean"), "name");
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        Boolean defaultUseStaticTyping = false;

        BeanPropertyWriter writer = propertyBuilder.buildWriter(provider, propDef, null, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
    }

    @Test
    public void testRefineSerializationType() throws JsonMappingException {
        Annotated a = new AnnotatedMember(new ClassName("TestBean"), "name");
        JavaType declaredType = TypeFactory.type(String.class, beanDesc.getType());
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType serializationType = propertyBuilder.findSerializationType(a, false, declaredType);
        Assert.assertEquals(serializationType.getRawClass(), String.class);
    }

    @Test
    public void testRefineSerializationTypeWithNullDeclaredType() throws JsonMappingException {
        Annotated a = new AnnotatedMember(new ClassName("TestBean"), "name");
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType serializationType = propertyBuilder.findSerializationType(a, false, null);
        Assert.assertNull(serializationType);
    }

    @Test
    public void testUnwrappingWriter() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        AnnotatedMember member = new AnnotatedMember(new ClassName("TestBean"), "name");
        NameTransformer unwrapper = new NameTransformer() {
            @Override
            public String transform(String name, JsonSerializer<?> serializer) {
                return name.toUpperCase();
            }
        };
        BeanPropertyWriter writer = propertyBuilder.new BeanPropertyWriter(new BeanPropertyDefinition(new ClassName("TestBean"), "name"), member, beanDesc.getClassAnnotations(), beanDesc.getType(), null, null, null, false);
        BeanPropertyWriter unwrappingWriter = propertyBuilder.unwrappingWriter(writer, unwrapper);
        Assert.assertNotNull(unwrappingWriter);
    }
}