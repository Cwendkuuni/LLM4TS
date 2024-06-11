import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.MockSettings;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;
import static org.mockito.Mockito.withSettingsExitStrategy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsDeepStubsTest {

 private static class TestClassUnderTest {

 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }

 @SuppressWarnings("unchecked")
 public static GenericsNest<Object> createTestObject() {
 return (GenericsNest<Object>) mock(GenericsNest.class, new ReturnsDeepStubs());
 }
 }

 @Test
 public void testBasicFunctionality() {
 GenericsNest<Object> mock = TestClassUnderTest.createTestObject();
 Object nestedMock = mock.entrySet().iterator().next().getValue().iterator().next();

 assertEquals(nestedMock.getClass(), mock.getClass());
 assertEquals(nestedMock, mock.entrySet().iterator().next().getValue().iterator().next());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnableToMockNonMockableType() {
 GenericsNest<String> mock = TestClassUnderTest.createTestObject();
 try {
 mock.entrySet().clear();
 } catch (UnsupportedOperationException e) {
 throw e;
 }
 }

 @Test
 public void testDeepStubbingWithNestedGenerics() throws Exception {
 GenericsNest<Map<Number, GenericsNest<Number>>> mock = TestClassUnderTest.createTestObject();

 Map<Number, GenericsNest<Number>> map = mock.entrySet().iterator().next();
 GenericsNest<Number> nestedMap = map.values().iterator().next();

 Object nestedNestedMock = nestedMap.entrySet().iterator().next().getValue().iterator().next();

 verify(mock).entrySet().iterator().next();
 verify(nestedMap).entrySet().iterator().next();
 verify(nestedNestedMock).iterator().next();

 verifyNoMoreInteractions(mock, nestedMap, nestedNestedMock);
 }

 @Test(expected = IOException.class)
 public void testSerializationExceptionHandling() throws Exception {
 GenericsNest<Object> mock = TestClassUnderTest.createTestObject();
 when(mock.entrySet().iterator().next()).thenThrow(new IOException());

 GenericsNest<Object> deepStub = TestClassUnderTest.createTestObject();
 deepStub.entrySet().iterator().next().getValue().iterator().next();

 verifyZeroInteractions(mock);
 }

 @Test
 public void testSequenceOfInvocations() throws Exception {
 GenericsNest<Object> mock = TestClassUnderTest.createTestObject();
 Object deepStub1 = TestClassUnderTest.createTestObject();
 Object deepStub2 = TestClassUnderTest.createTestObject();

 when(mock.entrySet().iterator().next().getValue().iterator().next())
 .thenReturn(deepStub1)
 .thenReturn(deepStub2);

 assertEquals(deepStub1, mock.entrySet().iterator().next().getValue().iterator().next());
 assertEquals(deepStub2, mock.entrySet().iterator().next().getValue().iterator().next());
 }

 @Test
 public void testMultipleInvocationsToSameStub() throws Exception {
 GenericsNest<Object> mock = TestClassUnderTest.createTestObject();
 Object deepStub = TestClassUnderTest.createTestObject();

 when(mock.entrySet().iterator().next().getValue().iterator().next())
 .thenReturn(deepStub)
 .thenReturn(deepStub);

 assertEquals(deepStub, mock.entrySet().iterator().next().getValue().iterator().next());
 assertEquals(deepStub, mock.entrySet().iterator().next().getValue().iterator().next());
 }

 @Test
 public void testRecordAnswerToInvocationContainer() throws Exception {
 GenericsNest<Object> mock = TestClassUnderTest.createTestObject();
 Object deepStub = TestClassUnderTest.createTestObject();
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class, withSettingsExitStrategy());

 mockitoCore().mock(InvocationContainerImpl.class).andReturn(container);
 when(container.getInvocationForStubbing()).thenReturn(new InvocationOnMock(mock, new Object[0], new Object[0], 0L, false, null, null, null, null, null, null, null, null, null));

 mockitoCore().addAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return deepStub;
 }
 }, false);

 assertEquals(deepStub, mock.entrySet().iterator().next().getValue().iterator().next());
 }

 private MockitoCore mockitoCore() {
 return Mockito.mock(MockitoCore.class, withSettings());
 }
}