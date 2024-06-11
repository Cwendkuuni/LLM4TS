import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.InvocationOnMock;
import org.mockito.MockSettings;
import org.mockito.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.AnswerWithThrowables;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() {
        // Given
        Object mock = mock(SomeClass.class);
        GenericMetadataSupport returnTypeGenericMetadata = actualParameterizedType(mock);

        // When
        Object result = new ReturnsDeepStubs().answer(mock(AnyInvocationOnMock.class));

        // Then
        assertEquals(mock, result);
    }

    @Test
    public void testGetMock() throws Throwable {
        // Given
        Object mock = mock(SomeClass.class);
        GenericMetadataSupport returnTypeGenericMetadata = actualParameterizedType(mock);

        // When
        Object result = new ReturnsDeepStubs().getMock(mock(AnyInvocationOnMock.class), returnTypeGenericMetadata);

        // Then
        assertEquals(mock, result);
    }

    @Test
    public void testCreateNewDeepStubMock() {
        // Given
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // When
        Object mock = new ReturnsDeepStubs().createNewDeepStubMock(returnTypeGenericMetadata);

        // Then
        assertNotNull(mock);
    }

    @Test
    public void testWithSettingsUsing() {
        // Given
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // When
        MockSettings mockSettings = new ReturnsDeepStubs().withSettingsUsing(returnTypeGenericMetadata);

        // Then
        assertNotNull(mockSettings);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        // Given
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // When
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs() {
            @Override
            protected GenericMetadataSupport actualParameterizedType(Object mock) {
                return returnTypeGenericMetadata;
            }
        };

        // Then
        assertEquals(deepStubs, new ReturnsDeepStubs().returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        // Given
        Object mock = mock(SomeClass.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        // When
        Object deepStubMock = new ReturnsDeepStubs().recordDeepStubMock(mock, container);

        // Then
        assertEquals(mock, deepStubMock);
    }

    @Test(expected = NullPointerException.class)
    public void testNullChecks() {
        new ReturnsDeepStubs().answer(null);
    }

    @Test(expected = MockitoException.class)
    public void testExceptionHandling() {
        // Given
        Class<String> stringClass = String.class;

        // When
        new ReturnsDeepStubs().answer(mock(InvocationOnMock.class, withSettings().extraInterfaces(stringClass)));

        // Then
        // The test should not reach this point as an exception should be thrown before
    }

    @Test
    public void testNestedGenericTypes() {
        // Given
        Class<Map<List<Number>, Number>> mapClass = Map.class.getTypeParameters()[0].getGenericComponentType();
        Class<List<Number>> listClass = List.class.getTypeParameters()[0].getGenericComponentType();
        Class<Number> numberClass = Number.class;

        // Given mocking the map and list classes
        Object mapMock = mock(mapClass, new ReturnsDeepStubs());
        Object listMock = mock(listClass, new ReturnsDeepStubs());

        // When setting up the map with the list and number mocks
        ((Map) mapMock).put(listMock, mock(Number.class, new ReturnsDeepStubs()));

        // When invoking a method on the map mock that returns the number mock
        Object result = ((Map<List<Number>, Number>) mapMock).get(listMock);

        // Then
        assertEquals(number, result);
    }

    @Test
    public void testMultipleMocks() {
        // Given
        Class<Map<List<Number>, Number>> mapClass = Map.class.getTypeParameters()[0].getGenericComponentType();
        Class<List<Number>> listClass = List.class.getTypeParameters()[0].getGenericComponentType();
        Class<Number> numberClass = Number.class;

        // Given mocking the map and list classes
        Object mapMock = mock(mapClass, new ReturnsDeepStubs());
        Object listMock = mock(listClass, new ReturnsDeepStubs());

        // When setting up the map with the list mock
        ((Map) mapMock).put(listMock, mock(Number.class, new ReturnsDeepStubs()));

        // When invoking a method on the map mock that returns the list mock
        Object result = ((Map<List<Number>, Number>) mapMock).get(listMock);

        // Then
        assertEquals(listMock, result);
    }
}

//