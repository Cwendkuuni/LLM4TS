package org.mockito.internal.configuration.DefaultInjectionEngine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Object testObject;
 private Set<Object> mocks;

 @Before
 public void setUp() {
 injectionEngine = new DefaultInjectionEngine();
 testObject = new TestClass();
 mocks = new HashSet<>();
 }

 @Test
 public void testInjectMocks_EmptyFields() {
 injectionEngine.injectMocksOnFields(new HashSet<>(), mocks, testObject);
 assertNull(testObject);
 }

 @Test
 public void testInjectMocks_SimpleInjectMocks() {
 Field injectMocksField = Whitebox.getInternalState(testObject, "injectMocksField");
 mock(injectMocksField.getType()).when(injectMocksField).thenReturn(Mockito.mock(injectMocksField.getType()));
 mocks.add(Mockito.mock(injectMocksField.getType()));
 injectionEngine.injectMocksOnFields(new HashSet<Field>(){ { add(injectMocksField); }}.toArray(new Field[0]), mocks, testObject);
 assertNotNull(injectMocksField.get(testObject));
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocks_MultipleCandidates_Type() {
 Field injectMocksField1 = Whitebox.getInternalState(testObject, "injectMocksField1");
 Field injectMocksField2 = Whitebox.getInternalState(testObject, "injectMocksField2");
 mock(injectMocksField1.getType()).when(injectMocksField1).thenReturn(Mockito.mock(injectMocksField1.getType()));
 mock(injectMocksField2.getType()).when(injectMocksField2).thenReturn(Mockito.mock(injectMocksField2.getType()));
 mocks.addAll(Arrays.asList(Mockito.mock(injectMocksField1.getType()), Mockito.mock(injectMocksField2.getType())));
 injectionEngine.injectMocksOnFields(new HashSet<Field>(){ { add(injectMocksField1); add(injectMocksField2); }}.toArray(new Field[0]), mocks, testObject);
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocks_MultipleCandidates_Name() {
 Field injectMocksField1 = Whitebox.getInternalState(testObject, "injectMocksField1");
 Field injectMocksField2 = Whitebox.getInternalState(testObject, "injectMocksField2");
 injectMocksField1.setName("injectMocksFieldCommonName");
 injectionMocksField1.setAccessible(true);
 injectionMocksField2.setAccessible(true);
 mock(injectMocksField1.getType()).when(injectMocksField1).thenReturn(Mockito.mock(injectMocksField1.getType()));
 mock(injectMocksField2.getType()).when(injectMocksField2).thenReturn(Mockito.mock(injectMocksField2.getType()));
 mocks.addAll(Arrays.asList(Mockito.mock(injectMocksField1.getType()), Mockito.mock(injectMocksField2.getType())));
 injectionEngine.injectMocksOnFields(new HashSet<Field>(){ { add(injectMocksField1); add(injectMocksField2); }}.toArray(new Field[0]), mocks, testObject);
 }

 @Test
 public void testInjectMocks_MultipleCandidates_NoFailure() {
 Field injectMocksField1 = Whitebox.getInternalState(testObject, "injectMocksField1");
 Field injectMocksField2 = Whitebox.getInternalState(testObject, "injectMocksField2");
 mock(injectMocksField1.getType()).when(injectMocksField1).thenReturn(Mockito.mock(injectMocksField1.getType()));
 mock(injectMocksField2.getType()).when(injectMocksField2).thenReturn(Mockito.mock(injectMocksField2.getType()));
 mocks.addAll(Arrays.asList(Mockito.mock(injectMocksField1.getType()), Mockito.mock(injectMocksField2.getType())));
 injectionEngine.injectMocksOnFields(new HashSet<Field>(){ { add(injectMocksField1); add(injectMocksField2); }}.toArray(new Field[0]), mocks, testObject);
 assertFalse(mocks.contains(injectMocksField1.get(testObject)));
 }

 @Test
 public void testInjectMocks_SuperClass() {
 Field injectMocksField = Whitebox.getInternalState(testObject, "injectMocksField");
 mock(injectMocksField.getType()).when(injectMocksField).thenReturn(Mockito.mock(injectMocksField.getType()));
 mocks.add(Mockito.mock(injectMocksField.getType()));
 Class<?> superClass = testObject.getClass().getSuperclass();
 injectionEngine.injectMockCandidate(superClass, mocks, testObject);
 assertNotNull(injectMocksField.get(testObject));
 }

 @Test
 public void testInjectMocks_MultipleSuperClasses() {
 Field injectMocksField = Whitebox.getInternalState(testObject, "injectMocksField");
 mock(injectMocksField.getType()).when(injectMocksField).thenReturn(Mockito.mock(injectMocksField.getType()));
 mocks.add(Mockito.mock(injectMocksField.getType()));
 Class<?> superClass1 = testObject.getClass().getSuperclass();
 Class<?> superClass2 = superClass1.getSuperclass();
 injectionEngine.injectMockCandidate(superClass1, mocks, testObject);
 injectionEngine.injectMockCandidate(superClass2, mocks, testObject);
 assertNotNull(injectMocksField.get(testObject));
 }

 @Test
 public void testOrderedInstanceFieldsFrom() {
 Class<?> testClass = TestClass.class;
 Field[] fields = injectionEngine.orderedInstanceFieldsFrom(testClass);
 Field field1 = Whitebox.getInternalState(testClass, "field1");
 Field field2 = Whitebox.getInternalState(testClass, "field2");
 Field field3 = Whitebox.getInternalState(testClass, "field3");

 assertNotNull(fields);
 assertArrayEquals(new Field[] { field3, field2, field1 }, fields);
 }
}

class TestClass {
 private TestField1 injectMocksField1;
 private TestField2 injectMocksField2;
 private TestField3 injectMocksField3;

 @InjectMocks
 private TestClassAnother injectMocksFieldAnother;

 public TestField1 getInjectMocksField1() {
 return injectMocksField1;
 }

 public void setInjectMocksField1(TestField1 injectMocksField1) {
 this.injectMocksField1 = injectMocksField1;
 }

 public TestField2 getInjectMocksField2() {
 return injectMocksField2;
 }

 public void setInjectMocksField2(TestField2 injectMocksField2) {
 this.injectMocksField2 = injectMocksField2;
 }

 public TestClassAnother getInjectMocksFieldAnother() {
 return injectMocksFieldAnother;
 }

 public void setInjectMocksFieldAnother(TestClassAnother injectMocksFieldAnother) {
 this.injectMocksFieldAnother = injectMocksFieldAnother;
 }

 public static class TestField1 {
 public TestField1() {}
 }

 public static class TestField2 {
 public TestField2() {}
 }

 public static class TestField3 extends TestField2 {
 public TestField3() {}
 }

 public static class TestClassAnother {
 public TestClassAnother() {}
 }
}