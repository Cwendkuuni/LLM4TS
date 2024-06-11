import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.withSettings;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mock;
 private InvocationOnMock invocation;

 @Before
 public void setUp() {
 mock = mock(Object.class, new ReturnsDeepStubs());
 returnsDeepStubs = Mockito.getanswers(mock).getReturnsDeepStubs();
 }

 @Test
 public void testAnswer_NonMockableType() {
 invocation = Mockito.mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(mock);
 when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

 assertNotNull(returnsDeepStubs.answer(invocation));
 verifyZeroInteractions(mock);
 }

 @Test
 public void testAnswer_MockableType() throws Exception {
 invocation = Mockito.mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(mock);
 when(invocation.getMethod()).thenReturn(getClass().getMethod("testMethod", String.class));

 Object deepStub = mock(String.class);
 when(deepStub.toString()).thenReturn("deep stub");

 Object result = returnsDeepStubs.answer(invocation);
 assertEquals("deep stub", result);
 verify(mock).toString();
 }

 @Test(expected = Throwable.class)
 public void testAnswer_ThrowsException() throws Exception {
 invocation = Mockito.mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(mock);
 when(invocation.getMethod()).thenReturn(getClass().getMethod("testMethod", String.class));

 throw new IOException("Exception message");

 try {
 returnsDeepStubs.answer(invocation);
 fail();
 } catch (Throwable e) {
 // expected
 }

 verifyZeroInteractions(mock);
 }

 @Test
 public void testDeepStub() throws Exception {
 mock = mock(String.class);
 when(mock.toString()).thenReturn("mock String");
 when(mock.length()).thenReturn(5);

 Object deepStub = returnsDeepStubs.deepStub(invocation, Mockito.type(String.class));
 assertEquals(mock, deepStub);
 verify(mock).toString();
 verify(mock).length();
 }

 @Test
 public void testNewDeepStubMock() throws Exception {
 mock = mock(Map.class);
 when(mock.entrySet()).thenReturn(Arrays.asList(Mockito.mock(Map.Entry.class)).iterator());
 when(mock.entrySet().iterator().next().getValue()).thenReturn(Mockito.mock(Number.class));

 Object newDeepStubMock = returnsDeepStubs.newDeepStubMock(Mockito.type(Map.class));
 assertEquals(Map.class, newDeepStubMock.getClass());
 verifyZeroInteractions(newDeepStubMock);
 }

 @Test
 public void testWithSettingsUsing() throws Exception {
 GenericDeclaration genericDeclaration = getClass().getDeclaredMethod("testMethod", String.class).getGenericReturnType();
 Type[] genericReturnTypeTypes = genericDeclaration.getGenericReturnType().getTypeArgs();

 mock = mock(Map.class, withSettings().extraInterfaces(new Type[] { genericReturnTypeTypes[0] }));
 when(mock.entrySet()).thenReturn(Arrays.asList(Mockito.mock(Map.Entry.class)).iterator());
 when(mock.entrySet().iterator().next().getValue()).thenReturn(Mockito.mock(Number.class));

 Object newDeepStubMock = returnsDeepStubs.newDeepStubMock(Mockito.type(Map.class));
 assertEquals(Map.class, newDeepStubMock.getClass());
 assertEquals(genericReturnTypeTypes[0], newDeepStubMock.getClass().getGenericSuperclass());
 }

 @Test
 public void testRecordDeepStubAnswer() throws Exception {
 mock = mock(String.class);
 when(mock.toString()).thenReturn("mock String");

 InvocationContainerImpl container = new InvocationContainerImpl(mock, new HashMap<>(), new HashMap<>(), null, null, null, null);
 Object deepStub = mock(String.class);
 when(deepStub.toString()).thenReturn("deep stub");

 returnsDeepStubs.recordDeepStubAnswer(deepStub, container);

 verify(container).addAnswer(ArgumentMatchers.isInstanceOf(DeeplyStubbedAnswer.class), ArgumentMatchers.eq(deepStub));
 verify(deepStub).toString();
 }

 @Test
 public void testActualParameterizedType() throws Exception {
 mock = mock(Map.class);
 CreationSettings mockSettings = new CreationSettings(Map.class, null, null, null, null, null, null, null, null, null);
 when(mockitoCore().mock(Mockito.type(Map.class), withSettings(mockSettings))).thenReturn(mock);

 Type genericReturnType = getClass().getDeclaredMethod("testMethod", Map.class).getGenericReturnType();
 Object typeToMock = returnsDeepStubs.actualParameterizedType(mock);
 assertEquals(mock.getClass(), typeToMock.getClass());
 assertEquals(genericReturnType, typeToMock.getClass().getGenericInterfaces()[0]);
 }

 private MockitoCore mockitoCore() {
 return Mockito.mock(MockitoCore.class);
 }

}