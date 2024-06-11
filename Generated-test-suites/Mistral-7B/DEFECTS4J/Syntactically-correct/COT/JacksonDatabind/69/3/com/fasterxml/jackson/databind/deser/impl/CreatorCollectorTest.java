package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.base.AnnotatedWithParams;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.DeserializationConfigIntrospector;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

public class CreatorCollectorTest {

 private CreatorCollector collector;
 private BeanDescription beanDesc;
 private DeserializationConfig config;
 private ObjectMapper mapper;

 @Before
 public void setUp() throws Exception {
 beanDesc = mock(BeanDescription.class);
 config = mock(DeserializationConfig.class);
 mapper = new ObjectMapper();
 mapper.setConfig(config);
 collector = new CreatorCollector(beanDesc, config);
 }

 @Test
 public void testDefaultCreator() {
 final JavaType javaType = mapper.getTypeFactory().constructType(List.class);
 collector.setDefaultCreator(createAnnotatedWithParams(javaType));
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 assertNotNull(inst);
 }

 @Test(expected = IllegalStateException.class)
 public void testNoDefaultCreator() {
 collector = new CreatorCollector(beanDesc, config);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 assertNull(inst);
 }

 @Test
 public void testStringCreator() throws IOException {
 final String name = "John Doe";
 final JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 final AnnotatedWithParams creator = createAnnotatedWithParams(String.class, new Object[]{"John Doe"});
 collector.addStringCreator(creator, false);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 assertEquals(name, inst.instantiateValue(config, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateStringCreator() throws IOException {
 final JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 final AnnotatedWithParams creator = createAnnotatedWithParams(String.class, new Object[]{"John Doe"});
 collector.addStringCreator(creator, false);
 collector.addStringCreator(creator, false);
 collector.constructValueInstantiator(config);
 }

 @Test
 public void testIntCreator() throws IOException {
 final int value = 42;
 final JavaType javaType = mapper.getTypeFactory().constructType(int.class);
 final AnnotatedWithParams creator = createAnnotatedWithParams(int.class, new Object[]{value});
 collector.addIntCreator(creator, false);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 assertEquals(value, inst.instantiateValue(config, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateIntCreator() throws IOException {
 final JavaType javaType = mapper.getTypeFactory().constructType(int.class);
 final AnnotatedWithParams creator = createAnnotatedWithParams(int.class, new Object[]{42});
 collector.addIntCreator(creator, false);
 collector.addIntCreator(creator, false);
 collector.constructValueInstantiator(config);
 }

 @Test
 public void testLongCreator() throws IOException {
 final long value = 42L;
 final JavaType javaType = mapper.getTypeFactory().constructType(long.class);
 final AnnotatedWithParams creator = createAnnotatedWithParams(long.class, new Object[]{value});
 collector.addLongCreator(creator, false);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 assertEquals(value, inst.instantiateValue(config, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateLongCreator() throws IOException {
 final JavaType javaType = mapper.getTypeFactory().constructType(long.class);
 final AnnotatedWithParams creator = createAnnotatedWithParams(long.class, new Object[]{42L});
 collector.addLongCreator(creator, false);
 collector.addLongCreator(creator, false);
 collector.constructValueInstantiator(config);
 }

 @Test
 public void testDoubleCreator() throws IOException {
 final double value = 42.0;
 final JavaType javaType = mapper.getTypeFactory().constructType(double.class);
 final AnnotatedWithParams creator = createAnnotatedWithParams(double.class, new Object[]{value});
 collector.addDoubleCreator(creator, false);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 assertEquals(value, inst.instantiateValue(config, null), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateDoubleCreator() throws IOException {
 final JavaType javaType = mapper.getTypeFactory().constructType(double.class);
 final AnnotatedWithParams creator = createAnnotatedWithParams(double.class, new Object[]{42.0});
 collector.addDoubleCreator(creator, false);
 collector.addDoubleCreator(creator, false);
 collector.constructValueInstantiator(config);
 }

 @Test
 public void testBooleanCreator() throws IOException {
 final boolean value = true;
 final JavaType javaType = mapper.getTypeFactory().constructType(boolean.class);
 final AnnotatedWithParams creator = createAnnotatedWithParams(boolean.class, new Object[]{value});
 collector.addBooleanCreator(creator, false);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 assertEquals(value, inst.instantiateValue(config, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateBooleanCreator() throws IOException {
 final JavaType javaType = mapper.getTypeFactory().constructType(boolean.class);
 final AnnotatedWithParams creator = createAnnotatedWithParams(boolean.class, new Object[]{true});
 collector.addBooleanCreator(creator, false);
 collector.addBooleanCreator(creator, false);
 collector.constructValueInstantiator(config);
 }

 @Test
 public void testDelegatingCreator() throws IOException {
 final JavaType javaType = mapper.getTypeFactory().constructType(String[].class);
 final Class<?> delegateType = String[].class;
 final Method method = String.class.getMethod("valueOf", new Class[]{String.class});
 final AnnotatedWithParams creator = createAnnotatedWithParams(method, new Object[]{"value"});
 final SettableBeanProperty[] injectables = new SettableBeanProperty[]{new SettableBeanProperty("values", null)};
 collector.addDelegatingCreator(creator, false, injectables);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 assertEquals("[value]", inst.instantiateValue(config, new Object[]{"value"}));
 }

 @Test
 public void testArrayDelegateCreator() throws IOException {
 final JavaType javaType = mapper.getTypeFactory().constructType(int[].class);
 final JavaType arrayDelegateType = mapper.getTypeFactory().constructType(int[].class);
 final Method method = int[].class.getMethod("newInstance", new Class[0]);
 final AnnotatedWithParams creator = createAnnotatedWithParams(method, new Object[0]);
 final SettableBeanProperty[] injectables = new SettableBeanProperty[]{new SettableBeanProperty("values", null)};
 collector.addArrayDelegateCreator(creator, false, injectables);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 final int[] expectedValues = new int[]{1, 2, 3};
 assertEquals(expectedValues, inst.instantiateValue(config, expectedValues.clone()));
 }

 @Test
 public void testPropertyBasedCreator() throws IOException {
 final JavaType javaType = mapper.getTypeFactory().constructType(Foo.class);
 final JavaType creatorType = FooCreator.class;
 final Method method = FooCreator.class.getMethod("createFoo", new Class[0]);
 final BeanPropertyDefinition propertyDef = new BeanPropertyDefinition("name", String.class, null, null, NopAnnotationIntrospector.instance);
 final SettableBeanProperty[] properties = new SettableBeanProperty[]{new SettableBeanProperty("name", null), new SettableBeanProperty("value", null)};
 final FooCreator creator = new FooCreator();
 final AnnotatedWithParams creatorParams = new AnnotatedWithParams(method, creator, new Object[0], new Object[0], new Object[]{propertyDef});
 collector.addPropertyCreator(creatorParams, false, properties);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 assertEquals(new Foo("value"), inst.instantiateValue(config, new Object[0]));
 }

 @Test
 public void testExplicitCreator() throws IOException {
 final JavaType javaType = mapper.getTypeFactory().constructType(Foo.class);
 final JavaType creatorType = FooCreatorExplicit.class;
 final Method method = FooCreatorExplicit.class.getMethod("createFooExplicit", new Class[0]);
 final AnnotatedWithParams creatorParams = new AnnotatedWithParams(method, null, new Object[0], new Object[0], null);
 collector.addPropertyCreator(creatorParams, true, new SettableBeanProperty[0]);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 assertEquals(new Foo("explicit"), inst.instantiateValue(config, new Object[0]));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateExplicitPropertyCreator() throws IOException {
 final JavaType javaType = mapper.getTypeFactory().constructType(Foo.class);
 final JavaType creatorType = FooCreatorExplicit.class;
 final Method method = FooCreatorExplicit.class.getMethod("createFooExplicit", new Class[0]);
 final AnnotatedWithParams creatorParams = new AnnotatedWithParams(method, null, new Object[0], new Object[0], null);
 collector.addPropertyCreator(creatorParams, true, new SettableBeanProperty[0]);
 collector.addPropertyCreator(creatorParams, true, new SettableBeanProperty[0]);
 collector.constructValueInstantiator(config);
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidCreatorType() throws IOException {
 final JavaType javaType = mapper.getTypeFactory().constructType(Foo.class);
 final JavaType creatorType = Integer.TYPE;
 final Method method = Object.class.getMethod("toString", new Class[0]);
 final AnnotatedWithParams creatorParams = new AnnotatedWithParams(method, null, new Object[0], new Object[0], null);
 collector.addPropertyCreator(creatorParams, true, new SettableBeanProperty[0]);
 collector.constructValueInstantiator(config);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullCreator() throws IOException {
 final JavaType javaType = mapper.getTypeFactory().constructType(Foo.class);
 final Method method = null;
 final AnnotatedWithParams creatorParams = new AnnotatedWithParams(method, null, new Object[0], new Object[0], null);
 collector.addPropertyCreator(creatorParams, true, new SettableBeanProperty[0]);
 collector.constructValueInstantiator(config);
 }

 @Test
 public void testCanFixAccess() throws Exception {
 final Member member = mock(Member.class);
 final boolean canFixAccess = true;
 collector = new CreatorCollector(beanDesc, config);
 collector._canFixAccess = canFixAccess;
 collector._forceAccess = false;
 collector._fixAccess(member);
 assertTrue(ClassUtil.isAccessible(member));
 }

 @Test
 public void testCantFixAccess() throws Exception {
 final Member member = mock(Member.class);
 final boolean canFixAccess = false;
 collector = new CreatorCollector(beanDesc, config);
 collector._canFixAccess = canFixAccess;
 collector._forceAccess = false;
 collector._fixAccess(member);
 assertFalse(ClassUtil.isAccessible(member));
 }

 @Test
 public void testForceAccess() throws Exception {
 final Member member = mock(Member.class);
 final boolean forceAccess = true;
 collector = new CreatorCollector(beanDesc, config);
 collector._canFixAccess = false;
 collector._forceAccess = forceAccess;
 collector._fixAccess(member);
 assertTrue(ClassUtil.isAccessible(member));
 }

 private AnnotatedWithParams createAnnotatedWithParams(Class<?> clazz, Object... args) {
 return new AnnotatedWithParams(createTestMethod(clazz, "create", new Class[0]), null, new Object[0], null, null);
 }

 private AnnotatedWithParams createAnnotatedWithParams(Method method, Object... args) {
 return new AnnotatedWithParams(method, null, new Object[0], null, null);
 }

 private Method createTestMethod(Class<?> clazz, String name, Class<?>... parameterTypes) throws NoSuchMethodException {
 return clazz.getDeclaredMethod(name, parameterTypes);
 }

 private static class Foo {
 private final String name;

 public Foo(String name) {
 this.name = name;
 }

 public String getName() {
 return name;
 }
 }

 private static class FooCreator {
 public static Foo createFoo(String[] values) {
 return new Foo(values[0]);
 }
 }

 private static class FooCreatorExplicit {
 @Creator(asEnum = true)
 public static Foo createFooExplicit() {
 return new Foo("explicit");
 }
 }
}