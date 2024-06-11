import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PropertyBuilderTest {

    private ObjectNode jsonNode;
    private ObjectMapper objectMapper;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.objectMapper = objectMapper;
        ObjectNode rootNode = objectMapper.createObjectNode();
        this.jsonNode = rootNode;
        this.propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(),
                new BeanDescription(MyClass.class));
    }

    @Test
    public void testBuilder_GivenClassAndBeanDescription_ShouldReturnWriter() throws JsonProcessingException {
        // Given
        MyClass myClass = new MyClass();

        // When
        BeanPropertyWriter writer = propertyBuilder.buildWriter(objectMapper.getSerializationConfig(),
                null, MyClass.class, null, null, null, null, false);

        // Then
        Assert.assertNotNull(writer);
    }

    @Test
    public void testFindSerializationType_GivenAnnotatedAndUseStaticTypingFalse_ShouldReturnNull() {
        // Given
        Annotated a = new Annotated() {
            @Override
            public Class<?> getRawType() {
                return String.class;
            }

            @Override
            public JavaType getType() {
                return null;
            }
        };

        // When
        JavaType result = propertyBuilder.findSerializationType(a, false, null);

        // Then
        Assert.assertNull(result);
    }

    @Test
    public void testFindSerializationType_GivenAnnotatedAndUseStaticTypingTrue_ShouldReturnType() {
        // Given
        MyClass myClass = new MyClass();
        BeanDescription beanDescription = new BeanDescription(MyClass.class);
        Annotated a = new Annotated() {
            @Override
            public Class<?> getRawType() {
                return MyClass.class;
            }

            @Override
            public JavaType getType() {
                return objectMapper.getTypeFactory().constructType(MyClass.class);
            }
        };

        // When
        JavaType result = propertyBuilder.findSerializationType(a, true, null);

        // Then
        Assert.assertEquals(MyClass.class, result.getRawClass());
    }

    @Test
    public void testGetDefaultBean_GivenBeanDesc_ShouldReturnInstance() throws JsonProcessingException {
        // Given

        // When
        Object defaultBean = propertyBuilder.getDefaultBean();

        // Then
        Assert.assertNotNull(defaultBean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultBean_GivenNullBeanDesc_ShouldThrowIllegalArgumentException() {
        // Given
        propertyBuilder = new PropertyBuilder(null, null);

        // When
        // Then
        propertyBuilder.getDefaultBean();
    }

    @Test
    public void testBuildWriter_GivenNullProperties_ShouldReturnNullWriter() throws JsonProcessingException {
        // Given
        MyClass myClass = new MyClass();

        // When
        BeanPropertyWriter writer = propertyBuilder.buildWriter(null, null, MyClass.class, null, null, null, null, false);

        // Then
        Assert.assertNull(writer);
    }

    @Test
    public void testBuildWriter_GivenValidProperties_ShouldReturnWriter() throws JsonProcessingException {
        // Given
        MyClass myClass = new MyClass();
        PropertyWriter writer = propertyBuilder.buildWriter(objectMapper.getSerializationConfig(), null, MyClass.class, null, null, null, null, false);

        // When
        jsonNode.set("property1", "value1");
        jsonNode.set("property2", 123);
        jsonNode.set("property3", Arrays.asList(1, 2, 3));

        ObjectWriter writer1 = objectMapper.writerWithView(MyClass.class);
        ObjectNode result = writer1.writeValueAsTree(myClass);

        // Then
        Assert.assertEquals(result.toString(), jsonNode.toString());
    }

    @Test
    public void testBuildWriter_GivenPropertiesWithNullValues_ShouldReturnWriterWithNullSuppression() throws JsonProcessingException {
        // Given
        MyClass myClass = new MyClass();
        myClass.setProperty1(null);

        // When
        PropertyWriter writer = propertyBuilders.buildWriter(objectMapper.getSerializationConfig(), null, MyClass.class, null, null, null, null, false);

        // Then
        Assert.assertFalse(writer.isSuppressNulls());

        // When - SuppressNulls
        PropertyWriter writerWithNullSuppression = propertyBuilders.buildWriter(objectMapper.getSerializationConfig(), null, MyClass.class, null, null, null, null, true);

        // Then
        Assert.assertTrue(writerWithNullSuppression.isSuppressNulls());
    }

    @Test(expected = JsonMappingException.class)
    public void testFindSerializationType_GivenInvalidType_ShouldThrowJsonMappingException() throws JsonProcessingException {
        // Given
        Annotated a = new Annotated() {
            @Override
            public Class<?> getRawType() {
                return String.class;
            }

            @Override
            public JavaType getType() {
                return objectMapper.getTypeFactory().constructType("InvalidType");
            }
        };

        // When
        JavaType result = propertyBuilder.findSerializationType(a, false, null);

        // Then
        Assert.fail();
    }

    @Test
    public void testGetDefaultValue_GivenJavaType_ShouldReturnDefaultValue() {
        // Given

        // When
        Object defaultValue = propertyBuilder.getDefaultValue(new TypeReference<Integer>() {});

        // Then
        Assert.assertEquals(0, defaultValue);
    }

    @Test
    public void testGetDefaultValue_GivenJavaTypeWithNoDefaultValue_ShouldReturnNull() {
        // Given

        // When
        Object defaultValue = propertyBuilder.getDefaultValue(new TypeReference<MyClass>() {});

        // Then
        Assert.assertNull(defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_GivenValidPropertyName_ShouldReturnDefaultValue() throws JsonProcessingException {
        // Given
        MyClass myClass = new MyClass();
        propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), new BeanDescription(MyClass.class));

        // When
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("property1", null, MyClass.class.getType());

        // Then
        Assert.assertEquals("defaultValue1", defaultValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_GivenInvalidPropertyName_ShouldThrowIllegalArgumentException() throws JsonProcessingException {
        // Given
        propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), new BeanDescription(MyClass.class));

        // When
        // Then
        propertyBuilder.getPropertyDefaultValue("invalidPropertyName", null, MyClass.class.getType());
    }

    @Test
    public void testGetPropertyDefaultValue_GivenPropertyHasDefaultConstructor_ShouldReturnInstance() throws JsonProcessingException {
        // Given
        MyClass myClass = new MyClass();
        propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), new BeanDescription(MyClass.class));

        // When
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("property2", null, MyClass.class.getType());

        // Then
        Assert.assertNotNull(defaultValue);
        Assert.assertEquals(myClass.getClass(), defaultValue.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_GivenPropertyHasNoDefaultConstructor_ShouldThrowIllegalArgumentException() throws JsonProcessingException {
        // Given
        MyClass myClass = new MyClass();
        propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), new BeanDescription(MyClass.class));

        // When
        // Then
        propertyBuilder.getPropertyDefaultValue("property3", null, MyClass.class.getType());
    }

    @Test
    public void testGetPropertyDefaultValue_GivenPropertyHasNoDefaultValue_ShouldReturnNull() throws JsonProcessingException {
        // Given
        MyClass myClass = new MyClass();
        propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), new BeanDescription(MyClass.class));

        // When
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("property3", null, MyClass.class.getType());

        // Then
        Assert.assertNull(defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_GivenPropertyHasDefaultValue_ShouldReturnValue() throws JsonProcessingException {
        // Given
        MyClass myClass = new MyClass();
        myClass.setProperty3(new Property3());
        propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), new BeanDescription(MyClass.class));

        // When
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("property3", null, MyClass.class.getType());

        // Then
        Assert.assertNotNull(defaultValue);
        Assert.assertEquals(myClass.getProperty3(), defaultValue);
    }

    @Test
    public void testGetDefaultBean_GivenBeanHasDefaultInstance_ShouldReturnInstance() throws JsonProcessingException {
        // Given
        propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), new BeanDescription(MyClass.class));

        // When
        Object defaultBean = propertyBuilder.getDefaultBean();

        // Then
        Assert.assertNotNull(defaultBean);
        Assert.assertEquals(MyClass.class, defaultBean.getClass());
    }

    @Test
    public void testGetDefaultBean_GivenBeanHasNoDefaultInstance_ShouldReturnNull() throws JsonProcessingException {
        // Given
        MyClass myClass = new MyClass();
        propertyBuilder = new PropertyBuilder(objectMapper.getSerializationConfig(), new BeanDescription(MyClass.class));

        // When
        Object defaultBean = propertyBuilder.getDefaultBean();

        // Then
        Assert.assertNull(defaultBean);
    }

    private static class Annotated {
        private Object _value;

        public Annotated() {
            _value = new Object();
        }

        public JavaType getType() {
            return null;
        }

        public Class<?> getRawType() {
            return getClass();
        }
    }

    private static class MyClass {
        private String property1 = "defaultValue1";
        private Integer property2 = 123;
        private List<Integer> property3 = Arrays.asList(1, 2, 3);
        private Property3 property4;

        public String getProperty1() {
            return property1;
        }

        public void setProperty1(String property1) {
            this.property1 = property1;
        }

        public Integer getProperty2() {
            return property2;
        }

        public void setProperty2(Integer property2) {
            this.property2 = property2;
        }

        public List<Integer> getProperty3() {
            return property3;
        }

        public void setProperty3(List<Integer> property3) {
            this.property3 = property3;
        }

        public Property3 getProperty4() {
            return property4;
        }

        public void setProperty4(Property3 property4) {
            this.property4 = property4;
        }

        public static class Property3 {
        }
    }
}