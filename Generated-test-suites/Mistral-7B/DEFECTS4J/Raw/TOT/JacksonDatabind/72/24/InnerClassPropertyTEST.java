package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Arrays;

public class InnerClassPropertyTest {

    @Test
    public void testDelegateProperty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());

        // Define a simple class with an inner class
        class OuterClass {
            private InnerClass inner = new InnerClass();

            @JsonDeserialize(using = InnerClassProperty.InnerClassDeserializer.class)
            static class InnerClass {
            }
        }

        // Define a custom deserializer for the inner class
        static class InnerClassPropertyTestInnerClassDeserializer
                extends JsonDeserializer<InnerClass> {
            @Override
            public InnerClass deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                return new InnerClass();
            }
        }

        // Register the custom deserializer
        mapper.registerDeserializer(InnerClass.class, new InnerClassPropertyTestInnerClassDeserializer());

        // Serialize and deserialize the outer class
        String json = mapper.writeValueAsString(new OuterClass());
        OuterClass deserialized = mapper.readValue(json, OuterClass.class);
        assertNotNull(deserialized.inner);

        // Check that the delegate property is used after value construction
        assertEquals("Delegate property value is not as expected", deserialized.inner, deserialized._delegate.getBean());
    }

    @Test
    public void testTransientConstructor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Define a class with a transient constructor
        class MyClassWithTransientConstructor {
            private InnerClassProperty innerClassProperty;

            @JsonDeserialize(using = InnerClassProperty.InnerClassPropertyDeserializer.class)
            static class InnerClass {
            }

            public MyClassWithTransientConstructor() {
                // This constructor is marked as transient, so it should not be used for deserialization
            }

            public MyClassWithTransientConstructor(InnerClass innerClass) {
                this.innerClassProperty = new InnerClassProperty(null, new AnnotatedConstructor(MyClassWithTransientConstructor.class, null, null, null));
                this.innerClassProperty._delegate = new InnerClassProperty(null, null);
                this.innerClassProperty._delegate.setName(new PropertyName("innerClass"));
                this.innerClassProperty._delegate.setValueDeserializer(
                        new JsonDeserializer<InnerClass>() {
                            @Override
                            public InnerClass deserialize(JsonParser p, DeserializationContext ctxt)
                                    throws IOException {
                                return new InnerClass();
                            }
                        });
                this.innerClassProperty._creator = null;
            }
        }

        // Serialize and deserialize the class
        String json = mapper.writeValueAsString(new MyClassWithTransientConstructor(new InnerClass()));
        MyClassWithTransientConstructor deserialized = mapper.readValue(json, MyClassWithTransientConstructor.class);
        assertNotNull(deserialized.innerClassProperty.getBean());

        // Check that the transient constructor is not used during deserialization
        assertNull(deserialized._creator);
    }

    @Test
    public void testJdkSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Define a class with a non-static inner class
        class MyClassWithInnerClass {
            private InnerClassProperty innerClassProperty;

            static class InnerClass {
            }

            public MyClassWithInnerClass() {
            }

            public MyClassWithInnerClass(InnerClass innerClass) {
                this.innerClassProperty = new InnerClassProperty(null, new AnnotatedConstructor(MyClassWithInnerClass.class, null, null, null));
                this.innerClassProperty._delegate = new InnerClassProperty(null, null);
                this.innerClassProperty._delegate.setName(new PropertyName("innerClass"));
                this.innerClassProperty._delegate.setValueDeserializer(
                        new JsonDeserializer<InnerClass>() {
                            @Override
                            public InnerClass deserialize(JsonParser p, DeserializationContext ctxt)
                                    throws IOException {
                                return new InnerClass();
                            }
                        });
                this.innerClassProperty._creator = null;
            }
        }

        // Serialize the class
        MyClassWithInnerClass obj = new MyClassWithInnerClass();
        String json = mapper.writeValueAsString(obj);

        // Write the serialized object to a file
        mapper.writeValue(new File("serialized.json"), obj);

        // Read the serialized object back from the file
        ObjectMapper deserializer = new ObjectMapper();
        MyClassWithInnerClass deserialized = deserializer.readValue(new File("serialized.json"), MyClassWithInnerClass.class);
        assertNotNull(deserialized.innerClassProperty.getBean());

        // Check that the object can be deserialized using the special constructor
        Constructor<?> constructor = deserialized.innerClassProperty._creator;
        assertNotNull(constructor);

        // Check that the object can be serialized and deserialized using the default serialization mechanism
        String json2 = mapper.writeValueAsString(deserialized);
        MyClassWithInnerClass deserialized2 = mapper.readValue(json2, MyClassWithInnerClass.class);
        assertNotNull(deserialized2.innerClassProperty.getBean());

        // Check that the object can be serialized and deserialized using the custom deserializer
        mapper.setSerializationInclusion(SerializationFeature.FIND_PROPERTIES_WITH_JSON_ANNOTATIONS, false);
        String json3 = mapper.writeValueAsString(deserialized);
        MyClassWithInnerClass deserialized3 = mapper.readValue(json3, MyClassWithInnerClass.class);
        assertNotNull(deserialized3.innerClassProperty.getBean());
    }

    static class InnerClassPropertyTestInnerClassDeserializer
            extends JsonDeserializer<InnerClass> {
        @Override
        public InnerClass deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            return new InnerClass();
        }
    }
}