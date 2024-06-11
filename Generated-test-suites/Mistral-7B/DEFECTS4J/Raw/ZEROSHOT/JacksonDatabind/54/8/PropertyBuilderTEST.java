package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSerialize;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;
    private ObjectNode testObjectNode;

    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        JavaClass beanClass = JavaClass.of(TestClass.class);
        BeanDescription beanDesc = Introspector.findBeanDescription(beanClass, config);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        testObjectNode = mapper.createObjectNode();
    }

    @Test
    public void testBuildWriter() throws Exception {
        // Test case for default values
        TestClass testClass = new TestClass();
        PropertyWriter testPropertyWriter = propertyBuilder.buildWriter(null, null,
                TypeFactory.defaultInstance().constructType(TestClass.class), null, null, null, null, false);

        assertNotNull(testPropertyWriter);

        // Test case for JsonSerialize annotation
        TestClassWithSerialize testClassWithSerialize = new TestClassWithSerialize();
        PropertyWriter testPropertyWriterWithSerialize = propertyBuilder.buildWriter(null, null,
                TypeFactory.defaultInstance().constructType(TestClassWithSerialize.class),
                new TestClassWithSerializeSerializer(), null, null, null, false);

        assertNotNull(testPropertyWriterWithSerialize);

        // Test case for JsonInclude annotation
        TestClassWithInclude testClassWithInclude = new TestClassWithInclude();
        PropertyWriter testPropertyWriterWithInclude = propertyBuilder.buildWriter(null, null,
                TypeFactory.defaultInstance().constructType(TestClassWithInclude.class), null, null, null, null, false);

        assertNotNull(testPropertyWriterWithInclude);

        // Test case for JsonInclude and JsonSerialize annotations
        TestClassWithIncludeAndSerialize testClassWithIncludeAndSerialize = new TestClassWithIncludeAndSerialize();
        PropertyWriter testPropertyWriterWithIncludeAndSerialize = propertyBuilder.buildWriter(null, null,
                TypeFactory.defaultInstance().constructType(TestClassWithIncludeAndSerialize.class), null, null, null, null, false);

        assertNotNull(testPropertyWriterWithIncludeAndSerialize);

        // Test case for JsonInclude annotation and default value
        TestClassWithIncludeAndDefault testClassWithIncludeAndDefault = new TestClassWithIncludeAndDefault();
        PropertyWriter testPropertyWriterWithIncludeAndDefault = propertyBuilder.buildWriter(null, null,
                TypeFactory.defaultInstance().constructType(TestClassWithIncludeAndDefault.class), null, null, null, null, false);

        assertNotNull(testPropertyWriterWithIncludeAndDefault);
    }

    // Test classes used for testing
    static class TestClass {
        private int intValue;
        private String stringValue;
    }

    static class TestClassWithSerialize {
        @JsonSerialize(using = TestClassWithSerializeSerializer.class)
        private int intValue;
        private String stringValue;
    }

    static class TestClassWithInclude {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private int intValue;
        private String stringValue;
    }

    static class TestClassWithIncludeAndSerialize {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        @JsonSerialize(using = TestClassWithSerializeSerializer.class)
        private int intValue;
        private String stringValue;
    }

    static class TestClassWithIncludeAndDefault {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private int intValue;
        private String stringValue;

        public TestClassWithIncludeAndDefault() {
            intValue = 1;
        }
    }

    static class TestClassWithSerializeSerializer extends StdSerializer<TestClassWithSerialize> {
        public TestClassWithSerializeSerializer() {
            this(null);
        }

        public TestClassWithSerializeSerializer(Class<TestClassWithSerialize> t) {
            this(null);
        }

        public TestClassWithSerializeSerializer(Class<TestClassWithSerialize> t,
                ConfigurableJsonSerializer.ValueInserter vip) {
            super(t, vip);
        }

        public TestClassWithSerializeSerializer(Class<TestClassWithSerialize> t,
                ConfigurableJsonSerializer.ValueSetter vs) {
            super(t, vs);
        }

        @Override
        public void serialize(TestClassWithSerialize src, JsonGenerator jgen, SerializerProvider provider)
                throws IOException {
            jgen.writeNumber(src.intValue * 2);
        }
    }
}