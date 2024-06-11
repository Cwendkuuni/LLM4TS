import org.mockito.Answer;
import org.mockito.InvocationOnMock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.MockitoAnswer;
import org.mockito.internal.InvocationContainerImpl;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.stubbing.AnswerInvocation;
import org.mockito.internal.stubbing.InvocationMetadata;
import org.mockito.internal.stubbing.Stubber;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;

import java.io.IOException;

public class ReturnsDeepStubsTest {

    @org.junit.Test
    public void testAnswerMethod() {
        Answer answer = Mockito.mock(Answer.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object result = returnsDeepStubs.answer(invocation);

        org.junit.Assert.assertNotNull(result);
    }

    @org.junit.Test
    public void testDeepStubMethod() throws Throwable {
        Answer answer = Mockito.mock(Answer.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object result = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);

        org.junit.Assert.assertSame(answer, result);
    }

    @org.junit.Test
    public void testNewDeepStubMockMethod() {
        Class<?> mockType = Mockito.class;
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object deepStubMock = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);

        org.junit.Assert.assertInstanceOf(mockType, deepStubMock);
    }

    @org.junit.Test
    public void testWithSettingsUsingMethod() {
        Class<?> mockType = Mockito.class;
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        org.junit.Assert.assertInstanceOf(MockSettings.class, mockSettings);
    }

    @org.junit.Test
    public void testRecordDeepStubAnswerMethod() throws Throwable {
        Object mock = Mockito.mock(Object.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);

        org.junit.Assert.assertSame(mock, result);
    }

    @org.junit.Test
    public void testActualParameterizedTypeMethod() {
        Object mock = Mockito.mock(Object.class);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        org.junit.Assert.assertNotNull(result);
    }

    @org.junit.Test
    public void testDeepStubsAnswerUsingMethod() {
        Class<?> mockType = Mockito.class;
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        ReturnsDeepStubs returnsDeepStubsAnswerUsing = returnsDeepStubs.deepStubsAnswerUsing(returnTypeGenericMetadata);

        org.junit.Assert.assertInstanceOf(ReturnsDeepStubs.class, returnsDeepStubsAnswerUsing);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testAnswerNullArgument() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = null;

        returnsDeepStubs.answer(invocation);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAnswerWrongReturnType() {
        Answer answer = Mockito.mock(Answer.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object result = returnsDeepStubs.answer(invocation);

        org.junit.Assert.assertNotSame(answer.getClass(), result.getClass());
    }

    @org.junit.Test(expected = IOException.class)
    public void testWriteReplaceIOException() throws IOException {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        returnsDeepStubs.writeReplace();
    }

    @org.junit.Test(expected = InstantiationException.class)
    public void testNewMockException() {
        Class<?> mockType = Mockito.class;
        GenericMetadataSupport returnTypeGenericMetadata = null;

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object deepStubMock = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testRecordDeepStubAnswerException() throws Throwable {
        Object mock = Mockito.mock(Object.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        returnsDeepStubs.recordDeepStubAnswer(mock, container);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Mockito.doThrow(new RuntimeException()).when(container).addAnswer(Mockito.any(), Mockito.eq(false));

        ReturnsDeepStubs newReturnsDeepStubs = new ReturnsDeepStubs();
        Object result = newReturnsDeepStubs.recordDeepStubAnswer(mock, container);
    }

}