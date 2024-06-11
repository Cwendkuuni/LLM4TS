import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Class<?> rawType = Mockito.mock(Class.class);

        Mockito.when(invocation.getMock()).thenReturn(returnTypeGenericMetadata);
        Mockito.when(returnTypeGenericMetadata.resolveGenericReturnType(invocation.getMethod())).thenReturn(rawType);

        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        InternalMockHandler<Object> handler = Mockito.mock(InternalMockHandler.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);

        Mockito.when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        Mockito.when(handler.getInvocationContainer()).thenReturn(container);
        Mockito.when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcher});
        Mockito.when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        Mockito.when(stubbedInvocationMatcher.answer(invocation)).thenReturn(new Object());

        MockCreationValidator mockCreationValidator = Mockito.mock(MockCreationValidator.class);
        Mockito.when(mockCreationValidator.isTypeMockable(rawType)).thenReturn(true);

        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        Mockito.whenNew(ReturnsEmptyValues.class).withNoArguments().thenReturn(returnsEmptyValues);
        Mockito.when(returnsEmptyValues.returnValueFor(rawType)).thenReturn(new Object());

        ReturnsDeepStubs spy = Mockito.spy(returnsDeepStubs);
        Mockito.doReturn(mockCreationValidator).when(spy).getMockValidator();

        spy.answer(invocation);

        Mockito.verify(invocation, Mockito.times(2)).getMock();
        Mockito.verify(returnTypeGenericMetadata).resolveGenericReturnType(invocation.getMethod());
        Mockito.verify(mockUtil).getMockHandler(invocation.getMock());
        Mockito.verify(handler).getInvocationContainer();
        Mockito.verify(container).getStubbedInvocations();
        Mockito.verify(container).getInvocationForStubbing();
        Mockito.verify(stubbedInvocationMatcher).answer(invocation);
        Mockito.verify(mockCreationValidator).isTypeMockable(rawType);
        Mockito.verifyNew(ReturnsEmptyValues.class).withNoArguments();
        Mockito.verify(returnsEmptyValues).returnValueFor(rawType);
    }

    @Test
    public void testGetMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        InternalMockHandler<Object> handler = Mockito.mock(InternalMockHandler.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);

        Mockito.when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        Mockito.when(handler.getInvocationContainer()).thenReturn(container);
        Mockito.when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcher});
        Mockito.when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        Mockito.when(stubbedInvocationMatcher.answer(invocation)).thenReturn(new Object());

        ReturnsDeepStubs spy = Mockito.spy(returnsDeepStubs);
        Mockito.doReturn(mockUtil).when(spy).getMockUtil();

        spy.getMock(invocation);

        Mockito.verify(invocation).getMock();
        Mockito.verify(mockUtil).getMockHandler(invocation.getMock());
        Mockito.verify(handler).getInvocationContainer();
        Mockito.verify(container).getStubbedInvocations();
        Mockito.verify(container).getInvocationForStubbing();
        Mockito.verify(stubbedInvocationMatcher).answer(invocation);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = Mockito.mock(InternalMockHandler.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        Class<?> clz = Mockito.mock(Class.class);
        Object mock = Mockito.mock(Object.class);
        Answer<Object> answer = Mockito.mock(Answer.class);

        Mockito.when(invocation.getMethod()).thenReturn(null);
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(clz);

        Mockito.whenNew(MockUtil.class).withNoArguments().thenReturn(Mockito.mock(MockUtil.class));
        Mockito.whenNew(MockUtil.class).withNoArguments().thenReturn(Mockito.mock(MockUtil.class));
        Mockito.whenMock(handler.getInvocationContainer()).thenReturn(container);
        Mockito.when(org.mockito.Mockito.mock(clz, returnsDeepStubs)).thenReturn(mock);

        MockUtil mockUtil = new MockUtil();
        Mockito.whenNew(MockUtil.class).withNoArguments().thenReturn(mockUtil);
        Mockito.when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);

        Mockito.doAnswer(answer).when(container).addAnswer(answer, false);

        ReturnsDeepStubs spy = Mockito.spy(returnsDeepStubs);
        Mockito.doReturn(mockUtil).when(spy).getMockUtil();

        spy.recordDeepStubMock(invocation, container);

        Mockito.verify(invocation, Mockito.times(2)).getMethod();
        Mockito.verify(invocation, Mockito.times(2)).getMethod().getReturnType();
        Mockito.verify(container).addAnswer(answer, false);
        Mockito.verifyNew(MockUtil.class, Mockito.times(2)).withNoArguments();
        Mockito.verify(mockUtil).getMockHandler(invocation.getMock());
        Mockito.verify(handler).getInvocationContainer();
        Mockito.verifyNew(MockUtil.class).withNoArguments();
        Mockito.verify(org.mockito.Mockito.class).mock(clz, returnsDeepStubs);
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Object mock = Mockito.mock(CreationSettings.class);
        CreationSettings creationSettings = Mockito.mock(CreationSettings.class);
        GenericMetadataSupport genericMetadataSupport = Mockito.mock(GenericMetadataSupport.class);

        Mockito.when(mockSettings.getTypeToMock()).thenReturn(creationSettings);
        Mockito.when(org.mockito.Mockito.mock(CreationSettings.class)).thenReturn(mock);
        Mockito.when(org.mockito.Mockito.mock(GenericMetadataSupport.class)).thenReturn(genericMetadataSupport);

        MockUtil mockUtil = new MockUtil();
        Mockito.whenNew(MockUtil.class).withNoArguments().thenReturn(mockUtil);
        Mockito.when(mockUtil.getMockHandler(mock)).thenReturn(handler);
        Mockito.when(handler.getMockSettings()).thenReturn(mockSettings);

        ReturnsDeepStubs returnsDeepStubsSpy = Mockito.spy(returnsDeepStubs);
        Mockito.doReturn(mockUtil).when(returnsDeepStubsSpy).getMockUtil();

        GenericMetadataSupport actualParameterizedType = returnsDeepStubsSpy.actualParameterizedType(mock);

        Mockito.verify(mockSettings).getTypeToMock();
        Mockito.verify(handler).getMockSettings();
        Mockito.verify(mockUtil).getMockHandler(mock);

        Assertions.assertEquals(genericMetadataSupport, actualParameterizedType);
    }
}