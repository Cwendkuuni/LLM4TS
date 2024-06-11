package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.exceptions.base.MockitoException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine defaultInjectionEngine;
 private Set<Object> mocks;
 private Object testClassInstance;
 private Field testField;
 private Object fieldToBeInjected;
 private Object superClassField;
 private Class<?> testClass;

 @Mock
 private Object mockA;
 @Mock
 private Object mockB;
 @Mock
 private Object mockC;

 @Before
 public void setUp() {
 testClass = getClass();
 testField = testClass.getDeclaredField("testField");
 testField.setAccessible(true);
 mocks = new HashSet<>();
 mocks.add(mockA);
 mocks.add(mockB);
 mocks.add(mockC);
 testClassInstance = new Object() {};
 defaultInjectionEngine = new DefaultInjectionEngine();
 }

 @Test
 public void testInjectMocksOnFields() {
 Set<Field> injectMocksFields = new HashSet<>();
 injectMocksFields.add(testField);
 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 // testField should have mockA injected
 assertSame(mockA, testField.get(testClassInstance));

 verifyNoMoreInteractions(mockB);
 verifyNoMoreInteractions(mockC);
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFieldsException() {
 Set<Field> injectMocksFields = new HashSet<>();
 injectMocksFields.add(testField);
 try {
 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 } catch (MockitoException e) {
 // expected exception
 assertNotNull(e.getMessage());
 }
 }

 @Test
 public void testInjectMockCandidate() {
 Class<?> awaitingInjectionClazz = Object.class.getSuperclass();
 while (!Object.class.equals(awaitingInjectionClazz)) {
 Field field = getFieldWithMockCandidate(awaitingInjectionClazz, mocks);
 if (field != null) {
 mockCandidateFilter(field, mocks);
 break;
 }
 awaitingInjectionClazz = awaitingInjectionClazz.getSuperclass();
 }

 verifyZeroInteractions(mockA);
 verifyZeroInteractions(mockB);
 verifyZeroInteractions(mockC);
 }

 private void mockCandidateFilter(Field field, Set<Object> mocks) {
 if (mocks.contains(field.getType())) {
 when(field.getType()).thenReturn(mockA);
 }
 }

 private Field getFieldWithMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks) {
 Field[] fields = orderedInstanceFieldsFrom(awaitingInjectionClazz);
 for (Field field : fields) {
 Object candidate = mockCandidateFilter(field, mocks);
 if (candidate != null) {
 return field;
 }
 }
 return null;
 }

 private Object mockCandidateFilter(Field field, Set<Object> mocks) {
 if (mocks.contains(field.getType())) {
 return field.getType();
 }
 return null;
 }

 private Field[] orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
 Field[] fields = awaitingInjectionClazz.getDeclaredFields();
 Arrays.sort(fields, new Comparator<Field>() {
 @Override
 public int compare(Field f1, Field f2) {
 Class<?> f1Type = f1.getType();
 Class<?> f2Type = f2.getType();
 return f1Type.isAssignableFrom(f2Type) ? 1 : (f2Type.isAssignableFrom(f1Type) ? -1 : 0);
 }
 });
 return fields;
 }
}