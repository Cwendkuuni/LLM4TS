package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Member;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollector_DefaultCreator() {
        BeanDescription beanDesc = new BeanDescription(MyClassWithDefaultConstructor.class);
        MapperConfig<Object> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        AnnotatedWithParams defaultCreator = new AnnotatedWithParams(MyClassWithDefaultConstructor.class, null);
        collector.setDefaultCreator(defaultCreator);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        assertNotNull(instantiator);
    }

    @Test
    public void testCreatorCollector_StringCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(MyClassWithStringConstructor.class);
        MapperConfig<Object> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        AnnotatedWithParams stringCreator = new AnnotatedWithParams(MyClassWithStringConstructor.class, new String[]{"str"});
        collector.addStringCreator(stringCreator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        assertNotNull(instantiator);
    }

    @Test
    public void testCreatorCollector_IntCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(MyClassWithIntConstructor.class);
        MapperConfig<Object> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        AnnotatedWithParams intCreator = new AnnotatedWithParams(MyClassWithIntConstructor.class, new int[0]);
        collector.addIntCreator(intCreator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        assertNotNull(instantiator);
    }

    @Test
    public void testCreatorCollector_LongCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(MyClassWithLongConstructor.class);
        MapperConfig<Object> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        AnnotatedWithParams longCreator = new AnnotatedWithParams(MyClassWithLongConstructor.class, new long[0]);
        collector.addLongCreator(longCreator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        assertNotNull(instantiator);
    }

    @Test
    public void testCreatorCollector_DoubleCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(MyClassWithDoubleConstructor.class);
        MapperConfig<Object> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        AnnotatedWithParams doubleCreator = new AnnotatedWithParams(MyClassWithDoubleConstructor.class, new double[0]);
        collector.addDoubleCreator(doubleCreator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        assertNotNull(instantiator);
    }

    @Test
    public void testCreatorCollector_BooleanCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(MyClassWithBooleanConstructor.class);
        MapperConfig<Object> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        AnnotatedWithParams booleanCreator = new AnnotatedWithParams(MyClassWithBooleanConstructor.class, new boolean[0]);
        collector.addBooleanCreator(booleanCreator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        assertNotNull(instantiator);
    }

    @Test
    public void testCreatorCollector_DelegatingCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(MyClassWithDelegatingConstructor.class);
        MapperConfig<Object> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        AnnotatedWithParams delegatingCreator = new AnnotatedWithParams(MyClassWithDelegatingConstructor.class, new Object[1]);
        SettableBeanProperty[] injectables = {new SettableBeanProperty("delegate", String.class)};
        collector.addDelegatingCreator(delegatingCreator, true, injectables);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        assertNotNull(instantiator);
    }

    @Test
    public void testCreatorCollector_PropertyBasedCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(MyClassWithPropertyBasedConstructor.class);
        MapperConfig<Object> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        AnnotatedWithParams propertyBasedCreator = new AnnotatedWithParams(MyClassWithPropertyBasedConstructor.class, new Object[0]);
        SettableBeanProperty[] properties = {new SettableBeanProperty("property1", String.class), new SettableBeanProperty("property2", Integer.class)};
        collector.addPropertyCreator(propertyBasedCreator, true, properties);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        assertNotNull(instantiator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatorCollector_DuplicateCreator() throws IOException {
        BeanDescription beanDesc = new BeanDescription(MyClassWithDuplicateCreators.class);
        MapperConfig<Object> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        AnnotatedWithParams stringCreator = new AnnotatedWithParams(MyClassWithDuplicateCreators.class, new String[0]);
        collector.addStringCreator(stringCreator, true);
        collector.addStringCreator(stringCreator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testCreatorCollector_DeprecatedMethods() throws IOException {
        BeanDescription beanDesc = new BeanDescription(MyClassWithDeprecatedMethods.class);
        MapperConfig<Object> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        AnnotatedWithParams stringCreator = new AnnotatedWithParams(MyClassWithDeprecatedMethods.class, new String[0]);
        collector.addStringCreator(stringCreator);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        assertNotNull(instantiator);
    }
}

class MyClassWithDefaultConstructor {
    public MyClassWithDefaultConstructor() {}
}

class MyClassWithStringConstructor {
    public MyClassWithStringConstructor(String str) {}
}

class MyClassWithIntConstructor {
    public MyClassWithIntConstructor(int i) {}
}

class MyClassWithLongConstructor {
    public MyClassWithLongConstructor(long l) {}
}

class MyClassWithDoubleConstructor {
    public MyClassWithDoubleConstructor(double d) {}
}

class MyClassWithBooleanConstructor {
    public MyClassWithBooleanConstructor(boolean b) {}
}

class MyClassWithDelegatingConstructor {
    public MyClassWithDelegatingConstructor(String delegate) {}
}

class MyClassWithPropertyBasedConstructor {
    public MyClassWithPropertyBasedConstructor(String property1, int property2) {}
}

class MyClassWithDuplicateCreators {
    @JsonCreator
    public static MyClassWithDuplicateCreators create(@JsonProperty("str") String str, @JsonProperty("str2") String str2) {
        return new MyClassWithDuplicateCreators(str, str2);
    }

    public MyClassWithDuplicateCreators(String str, String str2) {}
}

class MyClassWithDeprecatedMethods {
    @JsonCreator
    @Deprecated
    public static MyClassWithDeprecatedMethods create(@JsonProperty("str") String str) {
        return new MyClassWithDeprecatedMethods(str);
    }

    public MyClassWithDeprecatedMethods(String str) {}

    @Deprecated
    public void setStringCreator(AnnotatedWithParams creator) {
        // not used in this test
    }
}