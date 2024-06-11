package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testCreateDefaultCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig<?> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        JavaType type = JavaType.construct(String[].class);
        collector.setDefaultCreator(new StdValueInstantiator.StdCreatorAnnotatedParameter(type));

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        assertInstanceOf(Vanilla.class, instantiator);
    }

    @Test
    public void testCreateStringCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig<?> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        JavaType stringType = JavaType.construct(String.class);
        AnnotatedWithParams stringCreator = new StdValueInstantiator.StdCreatorAnnotatedParameter(stringType);
        collector.addStringCreator(stringCreator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        instantiator.configureFromStringCreator(stringCreator);
    }

    @Test
    public void testCreateIntCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig<?> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        JavaType intType = JavaType.construct(Integer.class);
        AnnotatedWithParams intCreator = new StdValueInstantiator.StdCreatorAnnotatedParameter(intType, new Integer(42));
        collector.addIntCreator(intCreator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        instantiator.configureFromIntCreator(intCreator);
    }

    @Test
    public void testCreateLongCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig<?> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        JavaType longType = JavaType.construct(Long.class);
        AnnotatedWithParams longCreator = new StdValueInstantiator.StdCreatorAnnotatedParameter(longType, new Long(42L));
        collector.addLongCreator(longCreator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        instantiator.configureFromLongCreator(longCreator);
    }

    @Test
    public void testCreateDoubleCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig<?> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        JavaType doubleType = JavaType.construct(Double.class);
        AnnotatedWithParams doubleCreator = new StdValueInstantiator.StdCreatorAnnotatedParameter(doubleType, new Double(42.0));
        collector.addDoubleCreator(doubleCreator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        instantiator.configureFromDoubleCreator(doubleCreator);
    }

    @Test
    public void testCreateBooleanCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig<?> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        JavaType booleanType = JavaType.construct(Boolean.class);
        AnnotatedWithParams booleanCreator = new StdValueInstantiator.StdCreatorAnnotatedParameter(booleanType, new Boolean(true));
        collector.addBooleanCreator(booleanCreator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        instantiator.configureFromBooleanCreator(booleanCreator);
    }

    @Test
    public void testCreateDelegateCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig<?> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        JavaType delegateType = JavaType.construct(String.class);
        JavaType[] delegateArgsTypes = {Integer.class, Float.class};
        AnnotatedWithParams delegateCreator = new CreatorProperty.StdCreatorAnnotatedMethod(SomeClassWithDelegateCreator.class, "delegateCreator", delegateType, delegateArgsTypes);
        SettableBeanProperty[] delegateArgs = new SettableBeanProperty[2];
        delegateArgs[0] = new SettableBeanProperty.Std("intArg", Integer.class);
        delegateArgs[1] = new SettableBeanProperty.Std("floatArg", Float.class);
        collector.addDelegatingCreator(delegateCreator, true, delegateArgs);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        instantiator.configureFromArraySettings(null, null, delegateArgs);
        instantiator.configureFromObjectSettings(null, delegateCreator, delegateType, delegateArgs, null, null);
        instantiator.configureFromStringCreator(null);
        instantiator.configureFromIntCreator(null);
        instantiator.configureFromLongCreator(null);
        instantiator.configureFromDoubleCreator(null);
        instantiator.configureFromBooleanCreator(null);
        instantiator.configureFromObjectCreator(null, "defaultCreator", null, null);
    }

    @Test
    public void testCreatePropertyBasedCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig<?> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        JavaType propertyBasedType = JavaType.construct(PropertyBasedClass.class);
        JavaType[] propertyBasedArgsTypes = {String.class, Integer.class};
        AnnotatedWithParams propertyBasedCreator = new CreatorProperty.StdCreatorAnnotatedMethod(PropertyBasedClass.class, "propertyBasedCreator", propertyBasedType, propertyBasedArgsTypes);
        SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[2];
        propertyBasedArgs[0] = new SettableBeanProperty.Std("name", String.class);
        propertyBasedArgs[1] = new SettableBeanProperty.Std("age", Integer.class);
        collector.addPropertyCreator(propertyBasedCreator, true, propertyBasedArgs);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
        instantiator.configureFromArraySettings(null, null, null);
        instantiator.configureFromObjectSettings(null, null, null, null, null, null);
        instantiator.configureFromStringCreator(null);
        instantiator.configureFromIntCreator(null);
        instantiator.configureFromLongCreator(null);
        instantiator.configureFromDoubleCreator(null);
        instantiator.configureFromBooleanCreator(null);
        instantiator.configureFromObjectCreator(null, "defaultCreator", null, null);
        instantiator.configureFromPropertyBasedCreator(propertyBasedCreator, propertyBasedArgs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDuplicateCreators() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig<?> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        JavaType stringType = JavaType.construct(String.class);
        AnnotatedWithParams stringCreator = new StdValueInstantiator.StdCreatorAnnotatedParameter(stringType);
        collector.addStringCreator(stringCreator, true);
        collector.addStringCreator(stringCreator, true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDuplicateCreatorsWithInjection() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig<?> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        JavaType stringType = JavaType.construct(String.class);
        AnnotatedWithParams stringCreator = new StdValueInstantiator.StdCreatorAnnotatedParameter(stringType);
        SettableBeanProperty[] stringArgs = new SettableBeanProperty[1];
        stringArgs[0] = new SettableBeanProperty.Std("arg", String.class);
        collector.addStringCreator(stringCreator, true);
        collector.addDelegatingCreator(stringCreator, true, stringArgs);

        ValueInstantiator instantiator = collector.constructValueInstantiator(null);
    }
}

class SomeClassWithDelegateCreator {
    @JsonCreator
    public static String delegateCreator(String str, int intArg, float floatArg) {
        return str + "_" + intArg + "_" + floatArg;
    }
}

class PropertyBasedClass {
    @JsonCreator
    public static PropertyBasedClass propertyBasedCreator(@JsonProperty("name") String name, @JsonProperty("age") int age) {
        return new PropertyBasedClass(name, age);
    }

    private final String _name;
    private final int _age;

    public PropertyBasedClass(String name, int age) {
        this._name = name;
        this._age = age;
    }
}