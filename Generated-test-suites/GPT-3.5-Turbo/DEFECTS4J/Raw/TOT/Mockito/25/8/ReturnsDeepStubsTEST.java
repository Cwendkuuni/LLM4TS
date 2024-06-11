import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

    @Test
    public void testAnswer_WhenInvocationMatchesContainer_ThenAnswerIsReturned() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        GenericMetadataSupport metadataSupport = Mockito.mock(GenericMetadataSupport.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);

        Mockito.when(invocation.getMock()).thenReturn(invocation);
        Mockito.when(invocation.getMethod()).thenReturn(null);
        Mockito.when(metadataSupport.rawType()).thenReturn(null);
        Mockito.when(container.getInvocationForStubbing()).thenReturn(invocation);
        Mockito.when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        Mockito.when(stubbedInvocationMatcher.getInvocation()).thenReturn(null);
        Mockito.when(stubbedInvocationMatcher.answer(invocation)).thenReturn(null);
        Mockito.when(returnsDeepStubs.actualParameterizedType(invocation.getMock())).thenReturn(metadataSupport);
        Mockito.when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(Mockito.mock(MockUtil.MockHandler.class));

        Object result = returnsDeepStubs.answer(invocation);

        Mockito.verify(invocation, Mockito.times(1)).getMock();
        Mockito.verify(invocation, Mockito.times(1)).getMethod();
        Mockito.verify(metadataSupport, Mockito.times(1)).rawType();
        Mockito.verify(container, Mockito.times(1)).getInvocationForStubbing();
        Mockito.verify(container, Mockito.times(1)).getStubbedInvocations();
        Mockito.verify(stubbedInvocationMatcher, Mockito.times(1)).getInvocation();
        Mockito.verify(stubbedInvocationMatcher, Mockito.times(1)).answer(invocation);

        assertEquals(null, result);
    }

    @Test
    public void testAnswer_WhenInvocationDoesNotMatchContainer_ThenRecordDeepStubMockIsCalled() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        GenericMetadataSupport metadataSupport = Mockito.mock(GenericMetadataSupport.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        Mockito.mock(StubbedInvocationMatcher.class);

        Mockito.when(invocation.getMock()).thenReturn(invocation);
        Mockito.when(invocation.getMethod()).thenReturn(null);
        Mockito.when(metadataSupport.rawType()).thenReturn(null);
        Mockito.when(container.getInvocationForStubbing()).thenReturn(invocation);
        Mockito.when(container.getStubbedInvocations()).thenReturn(Collections.emptyList());
        Mockito.when(returnsDeepStubs.actualParameterizedType(invocation.getMock())).thenReturn(metadataSupport);
        Mockito.when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(Mockito.mock(MockUtil.MockHandler.class));

        Object result = returnsDeepStubs.answer(invocation);

        Mockito.verify(invocation, Mockito.times(1)).getMock();
        Mockito.verify(invocation, Mockito.times(1)).getMethod();
        Mockito.verify(metadataSupport, Mockito.times(1)).rawType();
        Mockito.verify(container, Mockito.times(1)).getInvocationForStubbing();
        Mockito.verify(container, Mockito.times(1)).getStubbedInvocations();
        Mockito.verify(container, Mockito.times(1)).addAnswer(Mockito.any(Answer.class), Mockito.anyBoolean());

        assertTrue(result instanceof Object);
    }

    @Test
    public void testGetMock_WhenInvocationMatchesContainer_ThenAnswerIsReturned() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        GenericMetadataSupport metadataSupport = Mockito.mock(GenericMetadataSupport.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);

        Mockito.when(invocation.getMock()).thenReturn(invocation);
        Mockito.when(invocation.getMethod()).thenReturn(null);
        Mockito.when(metadataSupport.rawType()).thenReturn(null);
        Mockito.when(container.getInvocationForStubbing()).thenReturn(invocation);
        Mockito.when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        Mockito.when(stubbedInvocationMatcher.getInvocation()).thenReturn(null);
        Mockito.when(stubbedInvocationMatcher.answer(invocation)).thenReturn(null);
        Mockito.when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(Mockito.mock(MockUtil.MockHandler.class));

        Object result = returnsDeepStubs.getMock(invocation);

        Mockito.verify(invocation, Mockito.times(1)).getMock();
        Mockito.verify(invocation, Mockito.times(1)).getMethod();
        Mockito.verify(metadataSupport, Mockito.times(1)).rawType();
        Mockito.verify(container, Mockito.times(1)).getInvocationForStubbing();
        Mockito.verify(container, Mockito.times(1)).getStubbedInvocations();
        Mockito.verify(stubbedInvocationMatcher, Mockito.times(1)).getInvocation();
        Mockito.verify(stubbedInvocationMatcher, Mockito.times(1)).answer(invocation);

        assertEquals(null, result);
    }

    @Test
    public void testGetMock_WhenInvocationDoesNotMatchContainer_ThenRecordDeepStubMockIsCalled() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        GenericMetadataSupport metadataSupport = Mockito.mock(GenericMetadataSupport.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        Mockito.mock(StubbedInvocationMatcher.class);

        Mockito.when(invocation.getMock()).thenReturn(invocation);
        Mockito.when(invocation.getMethod()).thenReturn(null);
        Mockito.when(metadataSupport.rawType()).thenReturn(null);
        Mockito.when(container.getInvocationForStubbing()).thenReturn(invocation);
        Mockito.when(container.getStubbedInvocations()).thenReturn(Collections.emptyList());
        Mockito.when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(Mockito.mock(MockUtil.MockHandler.class));

        Object result = returnsDeepStubs.getMock(invocation);

        Mockito.verify(invocation, Mockito.times(1)).getMock();
        Mockito.verify(invocation, Mockito.times(1)).getMethod();
        Mockito.verify(metadataSupport, Mockito.times(1)).rawType();
        Mockito.verify(container, Mockito.times(1)).getInvocationForStubbing();
        Mockito.verify(container, Mockito.times(1)).getStubbedInvocations();
        Mockito.verify(container, Mockito.times(1)).addAnswer(Mockito.any(Answer.class), Mockito.anyBoolean());

        assertTrue(result instanceof Object);
    }

    @Test
    public void testRecordDeepStubMock_WhenClzIsNull_ThenMockIsReturned() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        
        Mockito.when(invocation.getMethod()).thenReturn(null);
        Mockito.when(invocation.getMock()).thenReturn(invocation);
        Mockito.when(container.addAnswer(Mockito.any(Answer.class), Mockito.anyBoolean())).thenReturn(null);

        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);

        Mockito.verify(invocation, Mockito.times(1)).getMethod();
        Mockito.verify(invocation, Mockito.times(1)).getMock();
        Mockito.verify(container, Mockito.times(1)).addAnswer(Mockito.any(Answer.class), Mockito.anyBoolean());

        assertTrue(result instanceof Object);
    }

    @Test
    public void testActualParameterizedType_WhenMockIsNotNull_ThenGenericMetadataSupportIsReturned() {
        Object mock = Mockito.mock(Object.class);
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        MockUtil.MockHandler mockHandler = Mockito.mock(MockUtil.MockHandler.class);
        GenericMetadataSupport genericMetadataSupport = Mockito.mock(GenericMetadataSupport.class);
        MockCreationValidator mockCreationValidator = Mockito.mock(MockCreationValidator.class);
        CreationSettings creationSettings = Mockito.mock(CreationSettings.class);
        Mockito.when(creationSettings.getTypeToMock()).thenReturn(null);
        Mockito.when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        Mockito.when(mockHandler.getMockSettings()).thenReturn(creationSettings);
        Mockito.when(mockCreationValidator.isTypeMockable(null)).thenReturn(true);
        Mockito.when(genericMetadataSupport.inferFrom(null)).thenReturn(null);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        returnsDeepStubs.actualParameterizedType(mock);

        Mockito.verify(mockUtil, Mockito.times(1)).getMockHandler(mock);
        Mockito.verify(mockHandler, Mockito.times(1)).getMockSettings();
        Mockito.verify(mockCreationValidator, Mockito.times(1)).isTypeMockable(null);
        Mockito.verify(genericMetadataSupport, Mockito.times(1)).inferFrom(null);
    }

    @Test
    public void testActualParameterizedType_WhenMockIsNull_ThenGenericMetadataSupportIsReturned() {
        Object mock = Mockito.mock(Object.class);
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        MockUtil.MockHandler mockHandler = Mockito.mock(MockUtil.MockHandler.class);
        GenericMetadataSupport genericMetadataSupport = Mockito.mock(GenericMetadataSupport.class);
        MockCreationValidator mockCreationValidator = Mockito.mock(MockCreationValidator.class);
        CreationSettings creationSettings = Mockito.mock(CreationSettings.class);
        Mockito.when(creationSettings.getTypeToMock()).thenReturn(new Object());
        Mockito.when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        Mockito.when(mockHandler.getMockSettings()).thenReturn(creationSettings);
        Mockito.when(mockCreationValidator.isTypeMockable(new Object())).thenReturn(true);
        Mockito.when(genericMetadataSupport.inferFrom(new Object())).thenReturn(null);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        returnsDeepStubs.actualParameterizedType(mock);

        Mockito.verify(mockUtil, Mockito.times(1)).getMockHandler(mock);
        Mockito.verify(mockHandler, Mockito.times(1)).getMockSettings();
        Mockito.verify(mockCreationValidator, Mockito.times(1)).isTypeMockable(new Object());
        Mockito.verify(genericMetadataSupport, Mockito.times(1)).inferFrom(null);
    }
}