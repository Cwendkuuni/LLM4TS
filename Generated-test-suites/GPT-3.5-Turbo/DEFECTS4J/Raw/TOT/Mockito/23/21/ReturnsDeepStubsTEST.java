import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_withMockTypeThatIsNotMockable_returnsDelegateReturnValue() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn("someString");

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assert(result != null);
        assert(result instanceof String);
        assert(result.equals("someString"));
    }

    @Test
    public void testAnswer_withMockTypeThatIsMockableAndInvocationMatches_returnsReturnValueFromStubbedInvocation() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn("someString");

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assert(result != null);
        assert(result instanceof String);
        assert(result.equals("someString"));
    }

    @Test
    public void testAnswer_withMockTypeThatIsMockableAndInvocationDoesNotMatch_returnsNewDeepStubMock() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn("someString");

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assert(result != null);
        assert(result instanceof String);
        assert(result.equals("someString"));
    }

    @Test
    public void testCreateNewDeepStubMock_withReturnTypeGenericMetadata_returnsNewMockObject() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Act
        Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

        // Assert
        assert(result != null);
        assert(result.getClass().isInstance(returnTypeGenericMetadata.rawType()));
    }

    @Test
    public void testWithSettingsUsing_withReturnTypeGenericMetadataAndExtraInterfaces_returnsMockSettingsWithExtraInterfaces() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Act
        MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        // Assert
        assert(result != null);
        assert(result.extraInterfaces().equals(returnTypeGenericMetadata.rawExtraInterfaces()));
    }
  
    @Test
    public void testReturnsDeepStubsAnswerUsing_withReturnTypeGenericMetadata_returnsReturnsDeepStubsAnswer() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Act
        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        // Assert
        assert(result != null);
        assert(result.actualParameterizedType(null).equals(returnTypeGenericMetadata));
    }

}