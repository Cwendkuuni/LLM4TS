import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsDeepStubsTest {

    @Mock
    private ReturnsDeepStubs returnsDeepStubs;

    @Test
    public void testAnswer_typicalUseCase() throws Throwable {
        // Typical use case: Return a mock for a type that is mockable
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        // Set up the returnTypeGenericMetadata with a mockable type
        GenericMetadataSupport returnTypeGenericMetadata = createMockableGenericMetadataSupport();
        
        // Call the answer method and verify that the mock is returned
        Object result = returnsDeepStubs.answer(invocation);
        assertNotNull(result);
        
        // Verify that the mock was created with the correct mock settings
        verify(returnsDeepStubs.getMockitoCore()).mock(returnTypeGenericMetadata.rawType(),
                returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testAnswer_unmockableType() throws Throwable {
        // Error scenario: Return an empty value for an unmockable type
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        // Set up the returnTypeGenericMetadata with an unmockable type
        GenericMetadataSupport returnTypeGenericMetadata = createUnmockableGenericMetadataSupport();

        // Call the answer method and verify that an empty value is returned
        Object result = returnsDeepStubs.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswer_matchingStubbedInvocation() throws Throwable {
        // Typical use case: Return the stubbed value for a matching invocation
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        // Set up the returnTypeGenericMetadata with a mockable type
        GenericMetadataSupport returnTypeGenericMetadata = createMockableGenericMetadataSupport();
        
        // Set up the container with a stubbed invocation matcher that matches
        InvocationContainerImpl container = new InvocationContainerImpl();
        StubbedInvocationMatcher stubbedInvocationMatcher = createStubbedInvocationMatcher();
        container.addStubbedInvocation(stubbedInvocationMatcher);
        
        // Call the answer method and verify that the stubbed value is returned
        Object result = returnsDeepStubs.answer(invocation);
        assertNotNull(result);
        assertEquals(stubbedInvocationMatcher.answer(invocation), result);
    }

    @Test
    public void testAnswer_nonMatchingStubbedInvocation() throws Throwable {
        // Error scenario: Return a new deep stub mock for a non-matching invocation
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        // Set up the returnTypeGenericMetadata with a mockable type
        GenericMetadataSupport returnTypeGenericMetadata = createMockableGenericMetadataSupport();
        
        // Set up the container with a stubbed invocation matcher that does not match
        InvocationContainerImpl container = new InvocationContainerImpl();
        container.addStubbedInvocation(createStubbedInvocationMatcher());
        
        // Call the answer method and verify that a new deep stub mock is returned
        Object result = returnsDeepStubs.answer(invocation);
        assertNotNull(result);
        verify(returnsDeepStubs.getMockitoCore()).mock(
                returnTypeGenericMetadata.rawType(),
                returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata));
    }

    private GenericMetadataSupport createMockableGenericMetadataSupport() {
        GenericMetadataSupport genericMetadataSupport = Mockito.mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.rawType()).thenReturn(MockitoCore.class);
        when(returnsDeepStubs.getMockitoCore().isTypeMockable(MockitoCore.class)).thenReturn(true);
        return genericMetadataSupport;
    }

    private GenericMetadataSupport createUnmockableGenericMetadataSupport() {
        GenericMetadataSupport genericMetadataSupport = Mockito.mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.rawType()).thenReturn(Object.class);
        when(returnsDeepStubs.getMockitoCore().isTypeMockable(Object.class)).thenReturn(false);
        return genericMetadataSupport;
    }

    private StubbedInvocationMatcher createStubbedInvocationMatcher() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);
        return stubbedInvocationMatcher;
    }
}