package org.mockito.internal.configuration. DefaultInjectionEngineTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration. DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine defaultInjectionEngine;
 private Object testObject;

 @Before
 public void setup() throws Exception {
 testObject = new TestClass();
 defaultInjectionEngine = new DefaultInjectionEngine();
 }

 @Test
 public void testInjectMocksOnFields() {
 Set<Field> injectMocksFields = Arrays.asList(new Field[] { testObject.getClass().getDeclaredField("field1"), testObject.getClass().getDeclaredField("field2") }).stream().map(Field::new).collect(Collectors.toSet());
 Set<Object> mocks = new HashSet<>();
 mocks.add(MockitoJUnit.mock(String.class));
 mocks.add(MockitoJUnit.mock(Integer.class));

 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testObject);

 Field field1 = testObject.getClass().getDeclaredField("field1");
 Assert.assertNotNull(field1.get(testObject));

 Field field2 = testObject.getClass().getDeclaredField("field2");
 Assert.assertNotNull(field2.get(testObject));
 }

 @Test(expected = MockitoException.class)
 public void testFailToInjectMock() {
 Set<Field> injectMocksFields = Arrays.asList(new Field[] { testObject.getClass().getDeclaredField("field3") }).stream().map(Field::new).collect(Collectors.toSet());
 Set<Object> mocks = new HashSet<>();
 mocks.add(MockitoJUnit.mock(String.class));
 mocks.add(MockitoJUnit.mock(Integer.class));

 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testObject);
 }

 @Test
 public void testInjectMockCandidate() throws Exception {
 Class<?> awaitingInjectionClazz = String.class;
 Set<Object> mocks = new HashSet<>();
 mocks.add(MockitoJUnit.mock(String.class));
 mocks.add(MockitoJUnit.mock(Integer.class));

 Field field = Whitebox.getInternalState(testObject, "field1");
 Object fieldInstance = field.get(testObject);

 defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

 Assert.assertEquals(MockitoJUnit.mock(String.class), field.get(testObject));
 }

 @Test
 public void testNoMockCandidateFound() throws Exception {
 Class<?> awaitingInjectionClazz = Mockito.class;
 Set<Object> mocks = new HashSet<>();
 mocks.add(MockitoJUnit.mock(String.class));
 mocks.add(MockitoJUnit.mock(Integer.class));

 Field field = Whitebox.getInternalState(testObject, "field1");
 Object fieldInstance = field.get(testObject);

 defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);
 }

 @Test
 public void testMultipleMockCandidateFound() throws Exception {
 Class<?> awaitingInjectionClazz = String.class;
 Set<Object> mocks = new HashSet<>();
 mocks.add(MockitoJUnit.mock(String.class));
 mocks.add(MockitoJUnit.mock(StringBuilder.class));
 mocks.add(MockitoJUnit.mock(Integer.class));

 Field field = Whitebox.getInternalState(testObject, "field1");
 Object fieldInstance = field.get(testObject);

 defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

 Assert.assertEquals(MockitoJUnit.mock(String.class), field.get(testObject));
 }

 private static class TestClass {
 private String field1;
 private Integer field2;
 private Object field3;
 }
}