package org.mockito.internal.configuration. DefaultInjectionEngineTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.configuration.injection.MockCandidate;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.assertThat;
import static org.mockito.MockitoAnnotations.getFieldsAnnotatedWith;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.mockito.MockitoAnnotations.spyAnnotatedClass;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Set<Object> mocks;
 private Object testClassInstance;
 private Set<Field> injectMocksFields;

 @Before
 public void setup() {
 injectionEngine = new DefaultInjectionEngine();
 mocks = new HashSet<>();
 testClassInstance = new Object() {};
 injectMocksFields = getFieldsAnnotatedWith(InjectMocks.class, testClassInstance.getClass());
 }

 @Test
 public void testInjectMocksOnFields_noMocks_noInject() {
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 verifyNoMoreInteractions(mocks);
 }

 @Test
 public void testInjectMocksOnFields_mocksAvailable_inject() {
 MockCandidate mockCandidate = mock(String.class);
 mocks.add(mockCandidate);
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 for (Field field : injectMocksFields) {
 MockCandidate expectedMock = mockCandidateFilter(field, mockCandidate.getClass());
 assertThat(field.get(testClassInstance)).isEqualTo(expectedMock);
 }
 verifyNoMoreInteractions(mocks);
 }

 @Test
 public void testInjectMocksOnFields_noUniqueMockCandidate_injectByName() {
 NameBasedCandidateFilter nameBasedCandidateFilter = new NameBasedCandidateFilter(new FinalMockCandidateFilter(new TypeBasedCandidateFilter(new NameBasedCandidateFilter())));
 injectionEngine = new DefaultInjectionEngine() {
 @Override
 public MockCandidateFilter mockCandidateFilter() {
 return nameBasedCandidateFilter;
 }
 };
 MockCandidate mockCandidate1 = mock(String.class);
 MockCandidate mockCandidate2 = mock(String.class);
 mocks.addAll(Arrays.asList(mockCandidate1, mockCandidate2));
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 for (Field field : injectMocksFields) {
 String fieldName = field.getName();
 MockCandidate expectedMock = mockCandidateFilter(field, String.class);
 assertThat(field.get(testClassInstance)).isEqualTo(expectedMock);
 }
 verifyNoMoreInteractions(mockCandidate1);
 verifyNoMoreInteractions(mockCandidate2);
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFields_noMockCandidate_fail() {
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 }

 @Test
 public void testInjectMockCandidate_injectByPropertySetter() {
 Class<?> awaitingInjectionClazz = String[].class;
 Field mockField = new Field("mockField", String.class) {
 @Override
 public int getModifiers() {
 return Modifier.PRIVATE | Modifier.FINAL;
 }
 };
 testClassInstance.getClass().getDeclaredFields()[0].setAccessible(true);
 testClassInstance.getClass().getDeclaredFields()[0].set(testClassInstance, mockField);
 mockField.setAccessible(true);
 MockCandidate mockCandidate = mock(String.class);
 mocks.add(mockCandidate);
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClassInstance);
 assertThat(mockField.get(testClassInstance)).isEqualTo(mockCandidate);
 }

 @Test
 public void testInjectMockCandidate_injectByFieldInjection() {
 Class<?> awaitingInjectionClazz = String[].class;
 Field mockField = new Field("mockField", String.class) {
 @Override
 public int getModifiers() {
 return Modifier.PRIVATE | Modifier.FINAL;
 }
 };
 testClassInstance.getClass().getDeclaredFields()[0].setAccessible(true);
 testClassInstance.getClass().getDeclaredFields()[0].set(testClassInstance, mockField);
 mockField.setAccessible(true);
 MockCandidate mockCandidate = mock(String.class);
 mocks.add(mockCandidate);
 Field fieldToInject = new Field("fieldToInject", String.class) {
 @Override
 public int getModifiers() {
 return Modifier.PRIVATE | Modifier.FINAL;
 }
 };
 fieldToInject.setAccessible(true);
 fieldToInject.set(testClassInstance, mockCandidate);
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClassInstance);
 assertThat(fieldToInject.get(testClassInstance)).isEqualTo(mockCandidate);
 }

 @Test
 public void testInjectMockCandidate_noMockCandidate_noFail() {
 Class<?> awaitingInjectionClazz = String[].class;
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClassInstance);
 }

 private MockCandidate mockCandidateFilter(Field field, Class<?> fieldType) {
 // Implement this method to filter mock candidate based on the field and fieldType
 //...
 }

 @Test
 public void testOrderedInstanceFieldsFrom_superTypeFirst() {
 Class<?> awaitingInjectionClazz = Object.class;
 Class<?> subType = String.class;
 Field field1 = new Field("field1", subType) {
 @Override
 public int getModifiers() {
 return Modifier.PRIVATE | Modifier.FINAL;
 }
 };
 Field field2 = new Field("field2", awaitingInjectionClazz) {
 @Override
 public int getModifiers() {
 return Modifier.PRIVATE | Modifier.FINAL;
 }
 };
 awaitingInjectionClazz.getDeclaredFields()[0].setAccessible(true);
 awaitingInjectionClazz.getDeclaredFields()[1].setAccessible(true);
 Field[] orderedInstanceFieldsFrom = injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);
 List<Field> expectedOrderedInstanceFields = Arrays.asList(field2, field1);
 Collections.sort(expectedOrderedInstanceFields, new Comparator<Field>() {
 @Override
 public int compare(Field field1, Field field2) {
 Class<?> field1Type = field1.getType();
 Class<?> field2Type = field2.getType();
 if (field1Type.isAssignableFrom(field2Type)) {
 return 1;
 }
 if (field2Type.isAssignableFrom(field1Type)) {
 return -1;
 }
 return 0;
 }
 });
 Arrays.sort(orderedInstanceFieldsFrom, new Comparator<Field>() {
 @Override
 public int compare(Field field1, Field field2) {
 Class<?> field1Type = field1.getType();
 Class<?> field2Type = field2.getType();
 if (field1Type.isAssignableFrom(field2Type)) {
 return 1;
 }
 if (field2Type.isAssignableFrom(field1Type)) {
 return -1;
 }
 return 0;
 }
 });
 assertEquals(expectedOrderedInstanceFields, Arrays.asList(orderedInstanceFieldsFrom));
 }
}