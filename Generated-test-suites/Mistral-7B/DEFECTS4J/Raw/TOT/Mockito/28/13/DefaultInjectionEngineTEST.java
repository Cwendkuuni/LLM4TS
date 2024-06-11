package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoCreationException;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Object testObject;

 @Before
 public void setUp() {
 testObject = new TestObject();
 injectionEngine = new DefaultInjectionEngine();
 }

 @Test
 public void testInjectMocksOnFields() {
 // given
 Set<Field> injectMocksFields = Arrays.asList(testObject.getClass().getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(org.mockito.InjectMocks.class))
 .collect(Collectors.toSet());
 Set<Object> mocks = new HashSet<>();
 mocks.add(mock(Dependency1.class));
 mocks.add(mock(Dependency2.class));

 // when
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testObject);

 // then
 for (Field field : injectMocksFields) {
 Object fieldValue = Whitebox.getInternalState(testObject, field.getName());
 assertEquals(mocks.iterator().next(), fieldValue);
 mocks.remove(fieldValue);
 }
 assertEquals(0, mocks.size());
 }

 @Test(expected = MockitoCreationException.class)
 public void testInjectMocksOnFields_noMockCandidate() {
 // given
 Set<Field> injectMocksFields = Arrays.asList(testObject.getClass().getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(org.mockito.InjectMocks.class))
 .collect(Collectors.toSet());
 Set<Object> mocks = new HashSet<>();

 // when
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testObject);

 // then
 for (Field field : injectMocksFields) {
 // no mock candidate found, test fails
 }
 }

 @Test
 public void testInjectMocksOnFields_multipleMockCandidates() {
 // given
 Set<Field> injectMocksFields = Arrays.asList(testObject.getClass().getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(org.mockito.InjectMocks.class))
 .collect(Collectors.toSet());
 Set<Object> mocks = new HashSet<>();
 mocks.add(mock(Dependency1.class));
 mocks.add(mock(Dependency2.class));

 // when
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testObject);

 // then
 for (Field field : injectMocksFields) {
 Object fieldValue = Whitebox.getInternalState(testObject, field.getName());
 // multiple mock candidates, user will then provide dependencies
 }
 }
}

class TestObject {
 @InjectMocks
 private Dependency1 dependency1;
 @InjectMocks
 private Dependency2 dependency2;
}

interface Dependency1 {}
interface Dependency2 {}