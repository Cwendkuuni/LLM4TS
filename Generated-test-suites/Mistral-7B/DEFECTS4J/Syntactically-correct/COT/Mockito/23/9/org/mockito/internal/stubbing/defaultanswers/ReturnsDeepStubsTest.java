import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;

 @Before
 public void setup() {
 returnsDeepStubs = new ReturnsDeepStubs();
 }

 @Test
 public void testAnswer_NonMockableType() throws Throwable {
 Object nonMockableType = new Object() {};
 InvocationOnMock invocation = Mockito.mock(Object.class).invocation(new Object()[0], nonMockableType.getClass());

 Object result = returnsDeepStubs.answer(invocation);

 assertEquals(new ReturnsEmptyValues().returnValueFor(nonMockableType.getClass()), result);
 }

 @Test
 public void testAnswer_MockableType() throws Throwable {
 Class<Interface> interfaceType = Interface.class;
 Interface mock = mock(interfaceType, Answers.RETURNS_DEEP_STUBS);

 Object result = returnsDeepStubs.answer(Mockito.argumentCaptor(interfaceType).capture(invocation -> invocation.getArguments()[0]));

 assertNotNull(result);
 verify(mock).answer(invocation.getArguments()[0]);
 }

 @Test(expected = Throwable.class)
 public void testAnswer_ThrowsException() throws Throwable {
 Class<ThrowableClass> throwableClass = ThrowableClass.class;
 Class<Interface> interfaceType = Interface.class;
 Interface mock = mock(interfaceType, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock invocation = Mockito.mock(Object.class)
 .invocation(new Object()[0], Arrays.asList(mock, mock(throwableClass)).toArray(new Object[0]));

 returnsDeepStubs.answer(invocation);
 }

 @Test
 public void testGetMock_NoMatchingStubbedInvocations() throws Throwable {
 Interface mock = mock(Interface.class, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock invocation = Mockito.mock(Object.class)
 .invocation(new Object()[0], Interface.class);

 Object result = returnsDeepStubs.getMock(invocation, Mockito.type(Interface.class));

 assertNotNull(result);
 assertEquals(mock, result);
 }

 @Test
 public void testGetMock_MatchingStubbedInvocation() throws Throwable {
 Interface mock = mock(Interface.class, Answers.RETURNS_DEEP_STUBS);
 Interface stub = mock(Interface.class);
 InvocationOnMock invocation = Mockito.mock(Object.class)
 .invocation(new Object()[0], Interface.class);
 Mockito.doAnswer(answers(stub)).when(mock).answer(Mockito.argThat(invocationMatcher -> invocationMatcher.matches(invocation)));

 Object result = returnsDeepStubs.getMock(invocation, Mockito.type(Interface.class));

 assertNotNull(result);
 assertEquals(stub, result);
 }

 @Test
 public void testCreateNewDeepStubMock_EmptyExtraInterfaces() throws Throwable {
 Interface emptyInterface = Interface.class;
 Object result = returnsDeepStubs.createNewDeepStubMock(Mockito.type(Interface.class.getGenericInterfaces()[0]));

 assertEquals(emptyInterface, result.getClass().getInterfaces()[0]);
 }

 @Test
 public void testCreateNewDeepStubMock_ExtraInterfaces() throws Throwable {
 Interface extraInterface = InterfaceWithExtraInterface.class;
 Object result = returnsDeepStubs.createNewDeepStubMock(Mockito.type(InterfaceWithExtraInterface.class.getGenericInterfaces()[0]));

 assertEquals(extraInterface, result.getClass().getInterfaces()[0]);
 assertEquals(ExtraInterface.class, result.getClass().getInterfaces()[1]);
 }

 @Test
 public void testRecordDeepStubMock() throws Throwable {
 Interface mock = mock(Interface.class, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock invocation = Mockito.mock(Object.class)
 .invocation(new Object()[0], Interface.class);
 Object deepStub = mock(Interface.class);
 returnsDeepStubs.recordDeepStubMock(deepStub, Mockito.mock(InvocationContainerImpl.class));

 verify(mock).answer(invocation.getArguments()[0]);
 assertEquals(deepStub, mock.answer(invocation.getArguments()[0]));
 }
}