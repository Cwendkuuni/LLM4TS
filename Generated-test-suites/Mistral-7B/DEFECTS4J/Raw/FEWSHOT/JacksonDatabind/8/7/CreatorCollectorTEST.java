package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.CreatorTestBase;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class CreatorCollectorTest extends CreatorTestBase {

    public void testCreatorCollector() throws Exception {
        BeanDescription beanDesc = buildTestBeanDescription(MyClass.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        JavaType type = beanDesc.getType();
        assertEquals(type, collector._beanDesc.getType());

        // default creator
        Method defaultConstructor = MyClass.class.getDeclaredConstructor();
        AnnotatedMethod defaultAnnotated = introspect(defaultConstructor);
        collector.setDefaultCreator(defaultAnnotated);

        // string creator
        AnnotatedMethod stringCreator = MyClass.class.getDeclaredMethod("stringCreator");
        JsonCreator stringCreatorAnnotation = stringCreator.getAnnotation(JsonCreator.class);
        AnnotatedWithParams stringCreatorParams = new AnnotatedWithParams(introspect(stringCreator), stringCreatorAnnotation, null);
        collector.addStringCreator(stringCreatorParams, false);

        // int creator
        AnnotatedField intField = findAnnotationedField(MyClass.class, "intField");
        intField.setAnnotation(new JsonProperty("intField") {});
        AnnotatedMethod intCreator = findAnnotationedMethod(MyClass.class, "intCreator", int.class);
        JsonCreator intCreatorAnnotation = intCreator.getAnnotation(JsonCreator.class);
        AnnotatedWithParams intCreatorParams = new AnnotatedWithParams(intCreator, intCreatorAnnotation, new JavaType[]{type});
        collector.addIntCreator(intCreatorParams, false);

        // long creator
        JavaType longType = context.constructType(Long.class);
        AnnotatedMethod longCreator = MyClass.class.getDeclaredMethod("longCreator", longType);
        JsonCreator longCreatorAnnotation = longCreator.getAnnotation(JsonCreator.class);
        AnnotatedWithParams longCreatorParams = new AnnotatedWithParams(introspect(longCreator), longCreatorAnnotation, longType);
        collector.addLongCreator(longCreatorParams, false);

        // double creator
        JavaType doubleType = context.constructType(Double.class);
        AnnotatedMethod doubleCreator = MyClass.class.getDeclaredMethod("doubleCreator", doubleType);
        JsonCreator doubleCreatorAnnotation = doubleCreator.getAnnotation(JsonCreator.class);
        AnnotatedWithParams doubleCreatorParams = new AnnotatedWithParams(introspect(doubleCreator), doubleCreatorAnnotation, doubleType);
        collector.addDoubleCreator(doubleCreatorParams, false);

        // boolean creator
        AnnotatedField booleanField = findAnnotationedField(MyClass.class, "booleanField");
        booleanField.setAnnotation(new JsonProperty("booleanField") {});
        AnnotatedMethod booleanCreator = MyClass.class.getDeclaredMethod("booleanCreator");
        JsonCreator booleanCreatorAnnotation = booleanCreator.getAnnotation(JsonCreator.class);
        AnnotatedWithParams booleanCreatorParams = new AnnotatedWithParams(introspect(booleanCreator), booleanCreatorAnnotation, null);
        collector.addBooleanCreator(booleanCreatorParams, false);

        // delegate creator
        JavaType delegateType = context.constructType(MyDelegate.class);
        AnnotatedMethod delegateCreator = MyClass.class.getDeclaredMethod("delegateCreator", delegateType);
        JsonCreator delegateCreatorAnnotation = delegateCreator.getAnnotation(JsonCreator.class);
        CreatorProperty[] delegateArgs = new CreatorProperty[2];
        delegateArgs[0] = buildCreatorProperty(introspect(MyClass.class.getDeclaredField("i")), null);
        delegateArgs[1] = buildCreatorProperty(introspect(MyClass.class.getDeclaredField("s")), null);
        AnnotatedWithParams delegateCreatorParams = new AnnotatedWithParams(introspect(delegateCreator), delegateCreatorAnnotation, delegateType, delegateArgs);
        collector.addDelegatingCreator(delegateCreatorParams, false, delegateArgs);

        // property-based creator
        AnnotatedMethod propertyCreator = MyClass.class.getDeclaredMethod("propertyCreator");
        JsonCreator propertyCreatorAnnotation = propertyCreator.getAnnotation(JsonCreator.class);
        CreatorProperty[] propertyBasedArgs = new CreatorProperty[3];
        propertyBasedArgs[0] = buildCreatorProperty(introspect(MyClass.class.getDeclaredField("i")), null);
        propertyBasedArgs[1] = buildCreatorProperty(introspect(MyClass.class.getDeclaredField("s")), null);
        propertyBasedArgs[2] = buildCreatorProperty(introspect(MyClass.class.getDeclaredField("b")), null);
        AnnotatedWithParams propertyCreatorParams = new AnnotatedWithParams(introspect(propertyCreator), propertyCreatorAnnotation, null, propertyBasedArgs);
        collector.addPropertyCreator(propertyCreatorParams, false, propertyBasedArgs);

        ValueInstantiator inst = collector.constructValueInstantiator(config);

        // test vanilla case
        Object created1 = inst.createValue(context, null);
        assertEquals(MyClass.class, created1.getClass());

        // test string creator
        Object created2 = inst.createValue(context, "string");
        assertEquals("string", ((MyClass) created2).s);

        // test int creator
        Object created3 = inst.createValue(context, 42);
        assertEquals(42, ((MyClass) created3).i);

        // test long creator
        Object created4 = inst.createValue(context, 42L);
        assertEquals(42L, ((MyClass) created4).l);

        // test double creator
        Object created5 = inst.createValue(context, 3.1415d);
        assertEquals(3.1415d, ((MyClass) created5).d);

        // test boolean creator
        Object created6 = inst.createValue(context, true);
        assertEquals(true, ((MyClass) created6).b);

        // test delegate creator
        Object delegate = new MyDelegate(42, "foo");
        Object created7 = inst.createValue(context, delegate);
        assertEquals(42, ((MyClass) created7).i);
        assertEquals("foo", ((MyClass) created7).s);

        // test property-based creator
        Object created8 = inst.createValue(context, 42, "foo", true);
        assertEquals(42, ((MyClass) created8).i);
        assertEquals("foo", ((MyClass) created8).s);
        assertEquals(true, ((MyClass) created8).b);
    }

    private static CreatorProperty buildCreatorProperty(AnnotatedMember member, Annotation ann) {
        return new CreatorProperty(member, ann, null);
    }

    private static AnnotatedMember findAnnotationedField(Class<?> clazz, String name) throws Exception {
        BeanDescription beanDesc = buildTestBeanDescription(clazz);
        BeanPropertyDefinition propDef = beanDesc.findBeanPropertyDefinition(name);
        if (propDef == null) {
            throw new IllegalArgumentException("No field \""+name+"\" in class "+clazz);
        }
        return introspect(propDef.getAnnotated());
    }

    private static AnnotatedMethod findAnnotationedMethod(Class<?> clazz, String name, Class<?>... paramTypes) throws Exception {
        return introspect(clazz.getDeclaredMethod(name, paramTypes));
    }

    private static BeanDescription buildTestBeanDescription(Class<?> clazz) throws Exception {
        return new BeanDescription(new NopAnnotationIntrospector(), new TypeBindings(), new ObjectMapper(), clazz);
    }
}

class MyClass {
    @JsonCreator
    public MyClass() {}

    @JsonProperty("s")
    private String s;

    @JsonProperty("i")
    private int i;

    @JsonProperty("l")
    private long l;

    @JsonProperty("d")
    private double d;

    @JsonProperty("b")
    private boolean b;

    @JsonCreator
    public static MyClass stringCreator(@JsonProperty("s") String s) {
        return new MyClass().init(s);
    }

    @JsonCreator
    public static MyClass intCreator(@JsonProperty("i") int i) {
        return new MyClass().init(i);
    }

    @JsonCreator
    public static MyClass longCreator(@JsonProperty("l") long l) {
        return new MyClass().init(l);
    }

    @JsonCreator
    public static MyClass doubleCreator(@JsonProperty("d") double d) {
        return new MyClass().init(d);
    }

    @JsonCreator
    public static MyClass booleanCreator(@JsonProperty("b") boolean b) {
        return new MyClass().init(b);
    }

    @JsonCreator
    public static MyClass delegateCreator(@JsonProperty("i") int i, @JsonProperty("s") String s) {
        return new MyClass(i, s);
    }

    @JsonCreator
    public static MyClass propertyCreator(@JsonProperty("i") int i, @JsonProperty("s") String s, @JsonProperty("b") boolean b) {
        return new MyClass(i, s, b);
    }

    private MyClass(int i, String s, boolean b) {
        this.i = i;
        this.s = s;
        this.b = b;
    }

    private MyClass init(Object value) {
        if (value instanceof Number) {
            int i = ((Number) value).intValue();
            this.i = i;
            this.s = ""+i;
            this.b = i > 0;
        } else {
            this.s = (String) value;
            this.i = 0;
            this.b = false;
        }
        return this;
    }
}

class MyDelegate {
    private int i;
    private String s;

    public MyDelegate(int i, String s) {
        this.i = i;
        this.s = s;
    }
}