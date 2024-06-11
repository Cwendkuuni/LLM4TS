import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

@RunWith(PowerMockRunner.class)
public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mock;

 @Before
 public void setUp() throws Exception {
 mock = mock(Object.class, new ReturnsDeepStubs());
 returnsDeepStubs = Whitebox.getInternalState(mock, ReturnsDeepStubs.class);
 }

 @Test
 public void testAnswerWithMatchingInvocation() throws Exception {
 // Test case for answer method with matching invocation
 InvocationOnMock invocation = PowerMockito.mock(InvocationOnMock.class);
 when(returnsDeepStubs.answer(invocation)).thenReturn("matching answer");

 Object result = returnsDeepStubs.answer(invocation);
 verify(returnsDeepStubs).answer(invocation);
 assertEquals("matching answer", result);
 }

 @Test(expected = Throwable.class)
 public void testAnswerWithNonMatchingInvocation() throws Exception {
 // Test case for answer method with non-matching invocation
 InvocationOnMock nonMatchingInvocation = PowerMockito.mock(InvocationOnMock.class);
 when(returnsDeepStubs.answer(nonMatchingInvocation)).thenThrow(new RuntimeException());

 try {
 returnsDeepStubs.answer(nonMatchingInvocation);
 } catch (RuntimeException e) {
 // Expected exception
 assertEquals("RuntimeException", e.getClass().getName());
 }
 }

 @Test
 public void testDeepStub() throws Exception {
 // Test case for deepStub method
 InvocationOnMock invocation = PowerMockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);

 when(returnsDeepStubs.deepStub(Mockito.eq(invocation), Mockito.eq(returnTypeGenericMetadata))).thenReturn("deep stub answer");

 Object result = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);
 verify(returnsDeepStubs).deepStub(invocation, returnTypeGenericMetadata);
 assertEquals("deep stub answer", result);
 }

 @Test
 public void testNewDeepStubMock() throws Exception {
 // Test case for newDeepStubMock method
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 Class<?> rawType = Map.class;

 when(returnsDeepStubs.actualParameterizedType(mock)).thenReturn(returnTypeGenericMetadata);
 when(returnTypeGenericMetadata.rawType()).thenReturn(rawType);
 when(mockitoCore().isTypeMockable(rawType)).thenReturn(true);

 Object mock = mockitoCore().mock(rawType, withSettings());
 Object deepStubMock = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);

 verify(mockitoCore()).mock(rawType, withSettings());
 assertEquals(mock, deepStubMock);
 }

 @Test
 public void testWithSettingsUsing() throws Exception {
 // Test case for withSettingsUsing method
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

 MockSettings mockSettings = Mockito.mock(MockSettings.class);
 when(mockSettings.serializable()).thenReturn(true);
 when(mockSettings.defaultAnswer(Answers.RETURNS_DEEP_STUBS)).thenReturn(returnsDeepStubs);
 when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
 when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{List.class});

 when(returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata)).thenReturn(mockSettings);

 MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
 assertEquals(mockSettings, result);
 }

 @Test
 public void testRecordDeepStubAnswer() throws Exception {
 // Test case for recordDeepStubAnswer method
 InvocationContainerImpl container = PowerMockito.mock(InvocationContainerImpl.class);
 Object mock = PowerMockito.mock(Object.class);

 when(mockitoCore().mock(Mockito.any(Class.class), Mockito.any(MockSettings.class))).thenReturn(mock);
 when(container.addAnswer(Mockito.eq(new DeeplyStubbedAnswer(mock)), Mockito.eq(false))).thenReturn(mock);

 Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);
 verify(container).addAnswer(Mockito.eq(new DeeplyStubbedAnswer(mock)), Mockito.eq(false));
 assertEquals(mock, result);
 }

 @Test
 public void testActualParameterizedType() throws Exception {
 // Test case for actualParameterizedType method
 Object mock = PowerMockito.mock(Map.class);
 CreationSettings mockSettings = PowerMockito.mock(CreationSettings.class);
 when(mockitoCore().mock(Mockito.any(Class.class), Mockito.any(MockSettings.class))).thenReturn(mock);
 when(new MockUtil().getMockHandler(mock)).thenReturn(mockSettings);
 when(mockSettings.getTypeToMock()).thenReturn(Map.class);
 when(mockSettings.hasRawExtraInterfaces()).thenReturn(false);

 Class<?> actualParameterizedType = returnsDeepStubs.actualParameterizedType(mock);
 assertEquals(Map.class, actualParameterizedType);
 }

 @Test
 public void testReturnsDeepStubsSerializationFallback() throws Exception {
 // Test case for ReturnsDeepStubsSerializationFallback class
 ReturnsDeepStubs returnsDeepStubs = mock(ReturnsDeepStubs.class);
 Object deepStubsSerializationFallback = Whitebox.getInternalState(returnsDeepStubs, ReturnsDeepStubsSerializationFallback.class);

 when(deepStubsSerializationFallback.actualParameterizedType(mock)).thenReturn(null);
 when(mockitoCore().mock(Mockito.any(Class.class), Mockito.any(MockSettings.class))).thenReturn(mock);

 Object result = returnsDeepStubs.answer(PowerMockito.mock(InvocationOnMock.class));
 assertEquals(Mockito.RETURNS_DEEP_STUBS, result);
 }

 @Test
 public void testDeeplyStubbedAnswer() throws Exception {
 // Test case for DeeplyStubbedAnswer class
 Object mock = PowerMockito.mock(Object.class);
 Object deeplyStubbedAnswer = PowerMockito.mock(DeeplyStubbedAnswer.class);
 when(deeplyStubbedAnswer.mock).thenReturn(mock);

 when(mockitoCore().mock(Mockito.any(Class.class), Mockito.any(MockSettings.class))).thenReturn(mock);
 when(returnsDeepStubs.recordDeepStubAnswer(Mockito.eq(mock), Mockito.eq(null))).thenReturn(deeplyStubbedAnswer);

 Object result = returnsDeepStubs.answer(PowerMockito.mock(InvocationOnMock.class));
 assertEquals(mock, result);
 }

 @Test
 public void testMockitoCore() throws Exception {
 // Test case for mockitoCore method
 when(mockitoCore().mock(Mockito.any(Class.class), Mockito.any(MockSettings.class))).thenReturn(mock);

 Object result = returnsDeepStubs.answer(PowerMockito.mock(InvocationOnMock.class));
 assertEquals(mock, result);
 }

 @Test
 public void testDelegate() throws Exception {
 // Test case for delegate method
 when(delegate().returnValueFor(String.class)).thenReturn("delegate answer");

 Object result = returnsDeepStubs.answer(PowerMockito.mock(InvocationOnMock.class));
 assertEquals("delegate answer", result);
 }

 @Test
 public void testGenericMetadataSupportResolvesGenericReturnType() throws Exception {
 // Test case for genericMetadataSupportResolvesGenericReturnType method
 Type returnType = new HashMap<Integer, Integer>().getClass().getGenericSuperclass();
 Type[] types = ((ParameterizedType) returnType).getActualTypeArguments();
 GenericMetadataSupport genericMetadataSupport = Mockito.mock(GenericMetadataSupport.class);
 when(genericMetadataSupport.resolveGenericReturnType(Mockito.mock(Method.class))).thenReturn(types[1]);
 when(mockitoCore().isTypeMockable(types[1].getClass())).thenReturn(false);

 Object answer = returnsDeepStubs.answer(PowerMockito.mock(InvocationOnMock.class));
 assertEquals(ReturnsDeepStubs.class, answer.getClass());
 }
}