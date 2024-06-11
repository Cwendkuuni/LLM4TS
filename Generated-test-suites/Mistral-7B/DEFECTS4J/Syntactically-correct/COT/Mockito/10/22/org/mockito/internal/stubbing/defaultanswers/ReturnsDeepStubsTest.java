import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.whitebox.Whitebox;

import java.io.Serializable;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(PowerMockRunner.class)
public class ReturnsDeepStubsTest {

 private static final Class<ReturnsDeepStubs> RETURNS_DEEP_STUBS_CLASS = ReturnsDeepStubs.class;

 @Test
 public void testAnswer() throws Exception {
 // Test method: public java.lang.Object answer(org.mockito.internal.stubbing.invocation.InvocationOnMock)
 // Given
 Object mock = mock(Integer.class, Answers.RETURNS_DEEP_STUBS);
 // When
 GenericMetadataSupport returnTypeGenericMetadata = Whitebox.getInternalState(mock, "returnTypeGenericMetadata");
 Class<?> rawType = returnTypeGenericMetadata.rawType();

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object answer = returnsDeepStubs.answer(PowerMockito.mock(InvocationOnMock.class));

 // Then
 assertNotNull(answer);
 // Check if the correct mock was returned
 if (mockitoCore().isTypeMockable(rawType)) {
 verify(mock).answer(PowerMockito.any(InvocationOnMock.class));
 } else {
 verifyZeroInteractions(mock);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testAnswerWithNotMockableType() throws Exception {
 // Test method: public java.lang.Object answer(org.mockito.internal.stubbing.invocation.InvocationOnMock)
 // Given
 Object mock = mock(String.class, Answers.RETURNS_DEEP_Stubs);
 // When & Then
 throw new IllegalStateException("Type String is not mockable with ReturnsDeepStubs answer.");
 }

 @Test
 public void testDeepStub() throws Exception {
 // Test method: private java.lang.Object deepStub(org.mockito.internal.stubbing.invocation.InvocationOnMock, org.mockito.internal.stubbing.util.GenericMetadataSupport)
 // Given
 InvocationOnMock invocationMock = PowerMockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(Object.class);

 PowerMockito.spy(ReturnsDeepStubs.class);
 Whitebox.setInternalState(ReturnsDeepStubs.class, "mockitoCore", mockitoCore());
 Whitebox.setInternalState(ReturnsDeepStubs.class, "delegate", new ReturnsEmptyValues());

 ReturnsDeepStubs returnsDeepStubs = Whitebox.getInternalState(ReturnsDeepStubs.class, "instance");

 // When
 Object deepStub = returnsDeepStubs.deepStub(invocationMock, returnTypeGenericMetadata);

 // Then
 assertEquals(invocationMock, deepStub);
 }

 @Test
 public void testNewDeepStubMock() throws Exception {
 // Test method: private java.lang.Object newDeepStubMock(org.mockito.internal.stubbing.util.GenericMetadataSupport)
 // Given
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(List.class);

 PowerMockito.spy(ReturnsDeepStubs.class);
 Whitebox.setInternalState(ReturnsDeepStubs.class, "mockitoCore", mockitoCore());
 Whitebox.setInternalState(ReturnsDeepStubs.class, "delegate", new ReturnsEmptyValues());

 // When
 Object newDeepStubMock = Whitebox.invokeMethod(ReturnsDeepStubs.class, "newDeepStubMock", returnTypeGenericMetadata);

 // Then
 assertNotNull(newDeepStubMock);
 // Check if the correct mock was created
 GenericDeclaration genericDeclaration = returnTypeGenericMetadata.getTypeToMock().getGenericInterfaces()[0];
 assertEquals(genericDeclaration.getTypeName(), Whitebox.getImplementedInterfaces(newDeepStubMock)[0].getTypeName());
 }

 @Test
 public void testWithSettingsUsing() throws Exception {
 // Test method: private org.mockito.MockSettings withSettingsUsing(org.mockito.internal.stubbing.util.GenericMetadataSupport)
 // Given
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(Map.class);

 PowerMockito.spy(ReturnsDeepStubs.class);
 Whitebox.setInternalState(ReturnsDeepStubs.class, "mockitoCore", mockitoCore());
 Whitebox.setInternalState(ReturnsDeepStubs.class, "delegate", new ReturnsEmptyValues());

 // When
 MockSettings mockSettings = Whitebox.invokeMethod(ReturnsDeepStubs.class, "withSettingsUsing", returnTypeGenericMetadata);

 // Then
 assertNotNull(mockSettings);
 // Check if the correct settings were set
 if (returnTypeGenericMetadata.hasRawExtraInterfaces()) {
 List<Class<?>> extraInterfaces = Arrays.asList(returnTypeGenericMetadata.rawExtraInterfaces());
 assertEquals(extraInterfaces.size(), mockSettings.sizeOfExtraInterfaces());
 for (Class<?> extraInterface : extraInterfaces) {
 assertEquals(extraInterface.getName(), mockSettings.getExtraInterface(extraInterface.getName()).getType().getName());
 }
 }
 }

 @Test
 public void testRecordDeepStubAnswer() throws Exception {
 // Test method: private java.lang.Object recordDeepStubAnswer(java.lang.Object, org.mockito.internal.stubbing.InvocationContainerImpl)
 // Given
 Object mock = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 InvocationContainerImpl container = new InvocationContainerImpl(mock, null, null, null, null, null);

 PowerMockito.spy(ReturnsDeepStubs.class);
 Whitebox.setInternalState(ReturnsDeepStubs.class, "mockitoCore", mockitoCore());
 Whitebox.setInternalState(ReturnsDeepStubs.class, "delegate", new ReturnsEmptyValues());

 // When
 Object recordedAnswer = Whitebox.invokeMethod(ReturnsDeepStubs.class, "recordDeepStubAnswer", mock, container);

 // Then
 assertSame(mock, recordedAnswer);
 }

 @Test
 public void testActualParameterizedType() throws Exception {
 // Test method: private java.lang.reflect.GenericDeclaration actualParameterizedType(java.lang.Object)
 // Given
 Object mock = mock(Map.class, Answers.RETURNS_DEEP_STUBS);

 PowerMockito.spy(ReturnsDeepStubs.class);
 Whitebox.setInternalState(ReturnsDeepStubs.class, "mockitoCore", mockitoCore());
 Whitebox.setInternalState(ReturnsDeepStubs.class, "delegate", new ReturnsEmptyValues());

 // When
 GenericDeclaration actualParameterizedType = Whitebox.invokeMethod(ReturnsDeepStubs.class, "actualParameterizedType", mock);

 // Then
 assertNotNull(actualParameterizedType);
 assertEquals(Map.class, actualParameterizedType.getDeclaringClass());
 }

 @Test
 public void testCreateInstance() throws Exception {
 // Test constructor: public org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs()
 // Given
 PowerMockito.spy(ReturnsDeepStubs.class);
 Whitebox.setInternalState(ReturnsDeepStubs.class, "mockitoCore", mockitoCore());
 Whitebox.setInternalState(ReturnsDeepStubs.class, "delegate", new ReturnsEmptyValues());

 // When
 ReturnsDeepStubs instance = new ReturnsDeepStubs();

 // Then
 assertNotNull(instance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateInstanceWithNullMockitoCore() throws Exception {
 // Test constructor: public org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs()
 // Given
 PowerMockito.spy(ReturnsDeepStubs.class);
 Whitebox.setInternalState(ReturnsDeepStubs.class, "mockitoCore", null);
 Whitebox.setInternalState(ReturnsDeepStubs.class, "delegate", new ReturnsEmptyValues());

 // When & Then
 throw new IllegalArgumentException("mockitoCore cannot be null");
 }

 @Test
 public void testReturnsDeepStubsAnswerUsing() throws Exception {
 // Test method: private org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs returnsDeepStubsAnswerUsing(org.mockito.internal.stubbing.util.GenericMetadataSupport)
 // Given
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(Map.class);

 PowerMockito.spy(ReturnsDeepStubs.class);
 Whitebox.setInternalState(ReturnsDeepStubs.class, "mockitoCore", mockitoCore());
 Whitebox.setInternalState(ReturnsDeepStubs.class, "delegate", new ReturnsEmptyValues());

 // When
 ReturnsDeepStubs returnsDeepStubs = Whitebox.getInternalState(ReturnsDeepStubs.class, "instance");
 Object returnsDeepStubsAnswerUsing = Whitebox.invokeMethod(ReturnsDeepStubs.class, "returnsDeepStubsAnswerUsing", returnTypeGenericMetadata);

 // Then
 assertSame(returnsDeepStubs, returnsDeepStubsAnswerUsing);
 }

 @Test
 public void testGetMockHandler() throws Exception {
 // Test method: private org.mockito.internal.MockHandler getMockHandler(java.lang.Object)
 // Given
 Object mock = mock(Map.class, Answers.RETURNS_DEEP_STUBS);

 PowerMockito.spy(ReturnsDeepStubs.class);
 Whitebox.setInternalState(ReturnsDeepStubs.class, "mockitoCore", mockitoCore());
 Whitebox.setInternalState(ReturnsDeepStubs.class, "delegate", new ReturnsEmptyValues());

 // When
 Object mockHandler = Whitebox.invokeMethod(ReturnsDeepStubs.class, "getMockHandler", mock);

 // Then
 assertNotNull(mockHandler);
 assertEquals(mock, mockHandler.getMock());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetMockHandlerWithNonMock() throws Exception {
 // Test method: private org.mockito.internal.MockHandler getMockHandler(java.lang.Object)
 // Given
 Object nonMock = new Object();

 PowerMockito.spy(ReturnsDeepStubs.class);
 Whitebox.setInternalState(ReturnsDeepStubs.class, "mockitoCore", mockitoCore());
 Whitebox.setInternalState(ReturnsDeepStubs.class, "delegate", new ReturnsEmptyValues());

 // When & Then
 throw new IllegalStateException("getMockHandler can only be called on a mocked object.");
 }

 @Test
 public void testIsTypeMockable() throws Exception {
 // Test method: private boolean isTypeMockable(java.lang.Class<?>)
 // Given
 PowerMockito.spy(ReturnsDeepStubs.class);
 Whitebox.setInternalState(ReturnsDeepStubs.class, "mockitoCore", mockitoCore());
 Whitebox.setInternalState(ReturnsDeepStubs.class, "delegate", new ReturnsEmptyValues());

 // When
 boolean isTypeMockableString = Whitebox.invokeMethod(ReturnsDeepStubs.class, "isTypeMockable", String.class);
 boolean isTypeMockableInteger = Whitebox.invokeMethod(ReturnsDeepStubs.class, "isTypeMockable", Integer.class);
 boolean isTypeMockableMap = Whitebox.invokeMethod(ReturnsDeepStubs.class, "isTypeMockable", Map.class);

 // Then
 assertTrue(isTypeMockableString);
 assertTrue(isTypeMockableInteger);
 assertTrue(isTypeMockableMap);
 }

 @Test
 public void testWriteReplace() throws Exception {
 // Test method: public java.lang.Object writeReplace() throws java.io.IOException
 // Given
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 // When
 Object result = returnsDeepStubs.writeReplace();

 // Then
 assertSame(mockitoCore().RETURNS_DEEP_STUBS, result);
 }
}