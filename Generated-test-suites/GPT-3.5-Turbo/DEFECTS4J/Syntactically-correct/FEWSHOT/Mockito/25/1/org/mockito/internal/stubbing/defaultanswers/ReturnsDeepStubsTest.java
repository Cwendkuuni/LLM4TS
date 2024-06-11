import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_withNonMockableType_returnsDelegateReturnValue() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MockUtil mockUtil = mock(MockUtil.class);
        MockCreationValidator mockCreationValidator = mock(MockCreationValidator.class);
        ReturnsEmptyValues returnsEmptyValues = mock(ReturnsEmptyValues.class);

        mockUtil.getMockHandler(invocation.getMock());
        mockCreationValidator.isTypeMockable(invocation.getRawType());
        returnsEmptyValues.returnValueFor(invocation.getRawType());

        // Verify the delegate method calls
        verify(mockUtil).getMockHandler(invocation.getMock());
        verify(mockCreationValidator).isTypeMockable(invocation.getRawType());
        verify(returnsEmptyValues).returnValueFor(invocation.getRawType());

        Object result = returnsDeepStubs.answer(invocation);

        assertEquals(returnsEmptyValue, result);
    }

    @Test
    public void testAnswer_withMockableType_returnsMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MockUtil mockUtil = mock(MockUtil.class);
        MockCreationValidator mockCreationValidator = mock(MockCreationValidator.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        CreationSettings creationSettings = mock(CreationSettings.class);
        InternalMockHandler<Object> internalMockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        Answer<Object> answer = mock(Answer.class);
        ReturnsDeepStubs returnsDeepStubsMock = mock(ReturnsDeepStubs.class);
        ReturnsEmptyValues returnsEmptyValues = mock(ReturnsEmptyValues.class);
        Object delegateReturnValue = new Object();

        mockUtil.getMockHandler(invocation.getMock());
        mockCreationValidator.isTypeMockable(invocation.getRawType());
        genericMetadataSupport.rawType();
        new MockUtil().getMockHandler(invocation.getMock());
        invocationContainerImpl.getStubbedInvocations();
        returnsEmptyValues.returnValueFor(invocation.getRawType());
        invocationContainerImpl.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation());
        stubbedInvocationMatcher.answer(invocation);
        returnsDeepStubsMock.recordDeepStubMock(invocation, invocationContainerImpl);
        internalMockHandler.getInvocationContainer();
        returnsEmptyValues.returnValueFor(invocation.getRawType());
        internalMockHandler.getInvocationContainer().addAnswer(answer, false);

        // Verify the method calls and returns
        verify(mockUtil).getMockHandler(invocation.getMock());
        verify(mockCreationValidator).isTypeMockable(invocation.getRawType());
        verify(genericMetadataSupport).rawType();
        verify(internalMockHandler).getInvocationContainer();
        verify(invocationContainerImpl).getStubbedInvocations();
        verify(returnsEmptyValues).returnValueFor(invocation.getRawType());
        verify(invocationContainerImpl.getInvocationForStubbing()).matches(stubbedInvocationMatcher.getInvocation());
        verify(stubbedInvocationMatcher).answer(invocation);
        verify(returnsDeepStubsMock).recordDeepStubMock(invocation, invocationContainerImpl);
        verify(internalMockHandler).getInvocationContainer();
        verify(returnsEmptyValues).returnValueFor(invocation.getRawType());
        verify(internalMockHandler.getInvocationContainer()).addAnswer(answer, false);

        Object result = returnsDeepStubs.answer(invocation);

        assertSame(mock, result);
    }
  
}