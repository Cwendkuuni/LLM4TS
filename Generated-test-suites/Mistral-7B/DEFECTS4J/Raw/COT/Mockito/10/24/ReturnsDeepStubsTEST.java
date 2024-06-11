import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.lang.reflect.GenericMetadataSupport;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsDeepStubsTest {

 private static final Class<?> RETURNS_DEEP_STUBS_CLASS = ReturnsDeepStubs.class;

 @Test
 public void testAnswerMethodWithSimpleType() throws Exception {
 Mock<Object> mock = mock(Object.class, with(Answers.RETURNS_DEEP_STUBS));
 Object answer = new ReturnsDeepStubs().answer(mock.verify(mock).someMethod());
 assertSame(mock.mock, answer);
 }

 @Test
 public void testAnswerMethodWithGenericType() throws Exception {
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }

 GenericsNest<Object> mock = mock(GenericsNest.class, with(Answers.RETURNS_DEEP_STUBS));
 Object number = ((Set<Number>) mock.entrySet().iterator().next()).iterator().next();
 Object answer = new ReturnsDeepStubs().answer(mock.verify(mock).someMethod());
 assertSame(mock.mock, answer);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswerMethodWithUnmockableType() throws Exception {
 try {
 Mock<IOException.class> mock = mock(IOException.class, with(Answers.RETURNS_DEEP_STUBS));
 } catch (Exception e) {
 throw new UnsupportedOperationException("IOException is not mockable: " + e.getMessage(), e);
 }
 }

 @Test
 public void testDeepStubMethod() throws Exception {
 Object mock = mock(Object.class, withSettings().serializable());
 InvocationOnMock invocation = mock.verify(mock).someMethod();
 Object answer = new ReturnsDeepStubs().deepStub(invocation, new GenericMetadataSupport() {
 @Override
 public Class<?> rawType() {
 return Object.class;
 }
 });
 assertSame(mock, answer);
 }

 @Test
 public void testNewDeepStubMockMethod() throws Exception {
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport() {
 @Override
 public Class<?> rawType() {
 return Map.class;
 }

 @Override
 public Class<?>[] rawExtraInterfaces() {
 return new Class[] { Set.class };
 }
 };

 Object mock = new ReturnsDeepStubs().newDeepStubMock(returnTypeGenericMetadata);
 assertInstanceOf(Map.class, mock);
 }

 @Test
 public void testRecordDeepStubAnswerMethod() throws Exception {
 Object mock = mock(Object.class, withSettings().serializable());
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
 Object answer = new ReturnsDeepStubs().recordDeepStubAnswer(mock, container);
 assertSame(mock, answer);
 verify(container).addAnswer(argThat(a -> a instanceof DeeplyStubbedAnswer), false);
 }

 @Test
 public void testDeeplyStubbedAnswerMethod() throws Exception {
 Object mock = mock(Object.class, withSettings().serializable());
 InvocationOnMock invocation = mock.verify(mock).someMethod();
 DeeplyStubbedAnswer answer = spy(new DeeplyStubbedAnswer(mock));
 answer.answer = mock;
 doAnswer(answer).when(mock).someMethod();
 Object result = mock.someMethod();
 assertSame(mock, result);
 }

 @Test
 public void testActualParameterizedTypeMethodReturnsExpectedType() throws Exception {
 Object mock = mock(Object.class, withSettings().serializable());
 Object returnTypeGenericMetadata = new ReturnsDeepStubs().actualParameterizedType(mock);
 assertEquals(Object.class, returnTypeGenericMetadata.rawType());
 }

 @Test(expected = IllegalStateException.class)
 public void testActualParameterizedTypeMethodThrowsExceptionForNonSerializableMock() throws Exception {
 Object mock = mock(Object.class);
 Object returnTypeGenericMetadata = new ReturnsDeepStubs().actualParameterizedType(mock);
 fail("Should not be able to obtain actualParameterizedType for non-serializable mock");
 }

 @Test
 public void testWithSettingsUsingMethodReturnsExpectedSettings() throws Exception {
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport() {
 @Override
 public Class<?> rawType() {
 return Map.class;
 }

 @Override
 public Class<?>[] rawExtraInterfaces() {
 return new Class[] { Set.class };
 }
 };

 MockSettings settings = new ReturnsDeepStubs().withSettingsUsing(returnTypeGenericMetadata);
 assertTrue(settings.isSerializable());
 assertEquals(Map.class, settings.getTypeToMock());
 assertEquals(Arrays.asList(Set.class), settings.getExtraInterfaces());
 }
}