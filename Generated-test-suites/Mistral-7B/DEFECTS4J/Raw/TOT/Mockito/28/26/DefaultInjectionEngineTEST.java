import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.InjectionPoint;
import org.mockito.internal.configuration.MockCandidateFilter;
import org.mockito.internal.util.reflection.FieldAccessible;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Object testObject;
 private Set<Object> mocks;

 @Before
 public void setUp() {
 mocks = new HashSet<>();
 testObject = new TestClassUnderTest();
 injectionEngine = new DefaultInjectionEngine();
 }

 @Test
 public void testInjectMocksOnFields() {
 // given
 MockitoAnnotations.initMocks(this);
 mocks.add(mock(MockType1.class));
 mocks.add(mock(MockType2.class));
 Field mockField1 = Whitebox.getInternalState(testObject, "field1");
 Field mockField2 = Whitebox.getInternalState(testObject, "field2");
 Set<Field> injectMocksFields = Arrays.asList(mockField1, mockField2).stream().map(Field::classifiable).collect(Collectors.toSet());

 // when
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testObject);

 // then
 assertEquals(mocks.size(), mocks.size()); // no mocks should have been removed
 verify(mockField1).setMock(any(MockType1.class));
 verify(mockField2).setMock(any(MockType2.class));
 }

 @Test
 public void testInjectMocksOnFields_NoMockCandidateFound() {
 // given
 MockitoAnnotations.initMocks(this);
 Set<Field> injectMocksFields = Arrays.asList(new FieldWithNoMockCandidate()).stream().map(Field::classifiable).collect(Collectors.toSet());

 // when
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testObject);

 // then
 verifyNoInteractions(mocks); // no mocks should have been injected
 }

 @Test
 public void testInjectMocksOnFields_MultipleMockCandidatesFound() {
 // given
 MockitoAnnotations.initMocks(this);
 Set<Object> mocksToBeInjected = new HashSet<>(mocks);
 mocksToBeInjected.add(mock(MockType1.class));
 mocksToBeInjected.add(mock(MockType2.class));
 Set<Field> injectMocksFields = Arrays.asList(new FieldWithMultipleMockCandidates()).stream().map(Field::classifiable).collect(Collectors.toSet());

 // when
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testObject);

 // then
 verify(mockType1Mock).someMethod();
 verify(mockType2Mock).someMethod();
 assertEquals(mocks.size() - 2, mocks.size()); // two mocks should have been removed
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFields_ExceptionThrown() {
 // given
 MockitoAnnotations.initMocks(this);
 Set<Field> injectMocksFields = Arrays.asList(new FieldThrowingException()).stream().map(Field::classifiable).collect(Collectors.toSet());

 // when
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testObject);

 // then
 fail("Exception should have been thrown");
 }

 private static class TestClassUnderTest {
 @InjectMocks
 private MockType1 field1;
 @InjectMocks
 private MockType2 field2;
 }

 private static class FieldWithNoMockCandidate {
 @InjectMocks
 private NoMockCandidateType field;
 }

 private static class FieldWithMultipleMockCandidates {
 @InjectMocks
 private MultipleMockCandidateType field;
 }

 private static class FieldThrowingException {
 @InjectMocks
 private ExceptionThrowingType field;
 }

 private static class NoMockCandidateType {
 // empty class
 }

 private static class MultipleMockCandidateType {
 @Mock
 private MockType1 mockType1;
 @Mock
 private MockType2 mockType2;
 }

 private static class ExceptionThrowingType {
 @Mock
 private Throwable exception;
 }
}

class Field {
 public static <T> T classifiable(Field field) {
 return (T) field;
 }
}