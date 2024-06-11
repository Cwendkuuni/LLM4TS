import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.lang.reflect.GenericMetadataSupport;
import java.lang.reflect.Type;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerWithMatchingInvocation() {
        // Given
        Class<Object> mockType = Mockito.mock(Object.class).getClass();
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(mockType);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // When
        Object result = returnsDeepStubs.answer(createMockInvocation(mockType, returnTypeGenericMetadata));

        // Then
        Assert.assertSame("Expected mock to be returned", result, createMock(mockType, returnTypeGenericMetadata));
        verify(mockType).equals(mockType); // Verify that the mock was used
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAnswerWithUnmockableType() {
        // Given
        Class<String> stringClass = String.class;
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(stringClass);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // When
        returnsDeepStubs.answer(createMockInvocation(String.class, returnTypeGenericMetadata));

        // Then
        // Exception should be thrown
    }

    @Test
    public void testGetMockWithMatchingInvocation() throws Throwable {
        // Given
        Class<Object> mockType = Mockito.mock(Object.class).getClass();
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(mockType);

        Object mock = createMock(mockType, returnTypeGenericMetadata);
        InvocationContainerImpl container = new InvocationContainerImpl();

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // When
        Object result = returnsDeepStubs.getMock(createMockInvocation(mockType, returnTypeGenericMetadata), returnTypeGenericMetadata);

        // Then
        Assert.assertSame("Expected mock to be returned", result, mock);
    }

    @Test(expected = Throwable.class)
    public void testGetMockWithNoMatchingStubbedInvocation() throws Throwable {
        // Given
        Class<Object> mockType = Mockito.mock(Object.class).getClass();
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(mockType);

        Object mock = createMock(mockType, returnTypeGenericMetadata);
        InvocationContainerImpl container = new InvocationContainerImpl();

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // When
        returnsDeepStubs.getMock(createMockInvocation(mockType, returnTypeGenericMetadata), returnTypeGenericMetadata);

        // Then
        // Exception should be thrown
    }

    @Test
    public void testGetMockWithDeepStub() throws Throwable {
        // Given
        Class<Object> mockType = Mockito.mock(Object.class).getClass();
        Class<Object> nestedMockType = Mockito.mock(Object.class).getClass();
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(mockType);
        GenericMetadataSupport nestedReturnTypeGenericMetadata = new GenericMetadataSupport(nestedMockType);

        Object mock = createMock(mockType, returnTypeGenericMetadata);
        Object nestedMock = createMock(nestedMockType, nestedReturnTypeGenericMetadata);

        InvocationContainerImpl container = new InvocationContainerImpl();

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // When
        Object result = returnsDeepStubs.getMock(createMockInvocation(mockType, returnTypeGenericMetadata), returnTypeGenericMetadata);

        // Then
        Assert.assertSame("Expected nested mock to be returned", result, nestedMock);
    }

    @Test
    public void testCreateNewDeepStubMock() {
        // Given
        Class<Object> mockType = Mockito.mock(Object.class).getClass();
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(mockType);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // When
        Object mock = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

        // Then
        Assert.assertNotNull("Expected mock to be created", mock);
    }

    @Test
    public void testWithSettingsUsing() {
        // Given
        Class<Object> mockType = Mockito.mock(Object.class).getClass();
        Type[] rawExtraInterfaces = new Type[0];
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(mockType);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // When
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        // Then
        Assert.assertNotNull("Expected MockSettings to be returned", mockSettings);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        // Given
        Class<Object> mockType = Mockito.mock(Object.class).getClass();
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(mockType);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        ReturnsDeepStubs deepStubs = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        // Then
        Assert.assertSame("Expected ReturnsDeepStubs to be returned", deepStubs, returnsDeepStubs);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        // Given
        Class<Object> mockType = Mockito.mock(Object.class).getClass();
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(mockType);
        InvocationContainerImpl container = new InvocationContainerImpl();

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // When
        Object deepStub = returnsDeepStubs.recordDeepStubMock(createMock(mockType, returnTypeGenericMetadata), container);

        // Then
        Assert.assertSame("Expected mock to be returned", deepStub, createMock(mockType, returnTypeGenericMetadata));
    }

    private InvocationOnMock createMockInvocation(Class<?> mockType, GenericMetadataSupport returnTypeGenericMetadata) {
        return Mockito.mock(InvocationOnMock.class, withSettings().callRealMethods(true).answer(Answers.RETURNS_DEEP_STUBS));
    }

    private Object createMock(Class<?> mockType, GenericMetadataSupport returnTypeGenericMetadata) {
        return Mockito.mock(mockType, withSettings().answer(Answers.RETURNS_DEEP_STUBS));
    }
}