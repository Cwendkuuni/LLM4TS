package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.junit.MockitoJUnit;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@MockitoJUnit
public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 @InjectMocks
 private MyClass testClass;
 @Mock
 private MockType mockType;
 @Mock
 private FinalMock mockFinalMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 injectionEngine = new DefaultInjectionEngine();
 }

 @Test
 public void testInjectMocksOnFields() {
 Set<Field> injectMocksFields = Arrays.asList(testClass.getClass().getDeclaredFields()).stream()
 .filter(field -> field.isAnnotationPresent(InjectMocks.class))
 .collect(Collectors.toSet());
 Set<Object> mocks = new HashSet<Object>() {{
 add(mockType);
 add(mockFinalMock);
 }};
 Object testClassInstance = testClass;
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 // Test injection of @InjectMocks field
 verify(testClass).setMock1(mockType);
 // Test injection of field in class hierarchy
 verify(testClass).setMock2(mockFinalMock);
 }

 private void mockCandidateFilterTest(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
 Set<Field> orderedInstanceFields = Arrays.asList(awaitingInjectionClazz.getDeclaredFields()).stream()
 .sorted(new Comparator<Field>() {
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
 }).collect(Collectors.toList());
 DefaultInjectionEngine engine = new DefaultInjectionEngine();
 for (Field field : orderedInstanceFields) {
 engine.mockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();
 }
 }

 @Test
 public void testInjectMockCandidate() {
 Class<?> awaitingInjectionClazz = MyClassThatHasMockInSuperClass.class;
 Set<Object> mocks = new HashSet<Object>() {{
 add(mockType);
 add(mockFinalMock);
 }};
 Object fieldInstance = mock(MyClassThatHasMockInSuperClass.class);
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

 // Test injection of @Mock field
 verify(fieldInstance).setMock1(mockType);
 // Test injection of final mock field
 verify(fieldInstance).setMock2(mockFinalMock);
 }

 @Test(expected = MockitoException.class)
 public void testNoMockFound() {
 Class<?> awaitingInjectionClazz = MyClassThatHasNoMock.class;
 Set<Object> mocks = new HashSet<Object>();
 Object fieldInstance = mock(awaitingInjectionClazz);
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);
 }

 @Test
 public void testOrderedInjection() {
 Class<?> awaitingInjectionClazz = MyClassWithOrderedFields.class;
 Set<Object> mocks = new HashSet<Object>() {{
 add(mockType);
 add(mockFinalMock);
 }};
 Object fieldInstance = mock(awaitingInjectionClazz);
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

 Field[] orderedInstanceFields = awaitingInjectionClazz.getDeclaredFields();
 Arrays.sort(orderedInstanceFields, new Comparator<Field>() {
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
 for (int i = 0; i < orderedInstanceFields.length; i++) {
 Field field = orderedInstanceFields[i];
 DefaultInjectionEngine engine = new DefaultInjectionEngine();
 engine.mockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();
 verify(fieldInstance).setFieldI(mocks.iterator().next());
 }
 }
}

class MyClass {
 @InjectMocks
 private MyClassThatHasMock mock1;
 private MockType mock2;

 public void setMock1(MockType mock) {
 this.mock2 = mock;
 }

 public void setMock2(FinalMock mock) {
 this.mock2 = mock;
 }
}

class MyClassThatHasMockInSuperClass extends MyClass {
 @Mock
 private MockType mock1;
 @Mock
 private FinalMock mock2;

 private MockType mock3;

 public void setMock1(MockType mock) {
 this.mock3 = mock;
 }

 public void setMock2(FinalMock mock) {
 this.mock3 = mock;
 }
}

class MyClassThatHasNoMock {
 @InjectMocks
 private NoMock mock;

 private MockType mock2;

 public void setMock1(MockType mock) {
 this.mock2 = mock;
 }
}

class MyClassWithOrderedFields {
 @InjectMocks
 private MyClassWithOrderedFieldsThatHasMocks mock;

 private MockType mock1;
 private FinalMock mock2;

 public void setFieldI(Object mock) {
 if (i == 0) {
 this.mock1 = (MockType) mock;
 } else {
 this.mock2 = (FinalMock) mock;
 }
 }
}