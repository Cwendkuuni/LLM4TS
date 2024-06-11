import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.mockito.Answer;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.internal.InvocationContainerImpl;
import org.mockito.internal.stubbing.AnswerStub;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Serializable;

import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

public class ReturnsDeepStubsTest {

    @org.junit.jupiter.api.BeforeEach
    public void setUp() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs mockito = mock(ReturnsDeepStubs.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(mockito.answer(eq(invocation))).thenReturn(new Object());
        Assert.assertEquals(new Object(), mockito.answer(invocation));
    }

    @Test
    public void testAnswer_NoMatchingInvocation() {
        ReturnsDeepStubs mockito = mock(ReturnsDeepStubs.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        AnswerStub answerStub = new AnswerStub() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                throw new IllegalStateException("No matching invocation found");
            }
        };
        Answer answer = answerStub;
        when(mockito.answer(eq(invocation))).then(answer);
        try {
            mockito.answer(invocation);
            fail("Expected exception when no matching invocation was found");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testAnswer_MatchingInvocation() throws Throwable {
        ReturnsDeepStubs mockito = mock(ReturnsDeepStubs.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mock = mock(Object.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = new StubbedInvocationMatcher(invocation, eq(mock));
        InvocationContainerImpl container = new InvocationContainerImpl(stubbedInvocationMatcher, new AnswerStub() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return "Test";
            }
        });
        when(mockito.answer(eq(invocation))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return mockito.getMock(invocationOnMock, stubbedInvocationMatcher.getReturnTypeMetadata());
            }
        });
        when(mockito.getMock(eq(container), any(GenericMetadataSupport.class))).thenReturn(mock);
        Assert.assertEquals(mock, mockito.answer(invocation));
    }

    @Test
    public void testAnswer_NestedGenericInvocation() throws Throwable {
        ReturnsDeepStubs mockito = mock(ReturnsDeepStubs.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class<?> genericReturnType = Object.class;
        Class<?> returnType = Map.class;
        Class<?> mapKeyType = Number.class;
        Class<?> mapValueType = Set.class;
        Class<?> setNumberType = Number.class;

        Object mapMock = mock(Map.class, withSettings()
                .defaultAnswer(new ReturnsDeepStubs()));
        Object setMock = mock(Set.class, withSettings()
                .defaultAnswer(new ReturnsDeepStubs()));
        Object numberMock = mock(Number.class, withSettings()
                .defaultAnswer(new ReturnsDeepStubs()));

        Object testObject = new Object() {
            @Override
            public Map.Entry<Number, Set<Number>> entrySet() {
                return new AbstractMap.SimpleEntry<>(numberMock, setMock);
            }

            @Override
            public Iterator<Map.Entry<Number, Set<Number>>> iterator() {
                return new AbstractList.AbstractIterator<Map.Entry<Number, Set<Number>>>() {
                    @Override
                    protected Map.Entry<Number, Set<Number>> computeNext() {
                        return new AbstractMap.SimpleImmutableEntry<>(numberMock, setMock.iterator().next());
                    }
                };
            }
        };

        when(mockito.answer(eq(invocation))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return mockito.getMock(invocationOnMock, getReturnTypeMetadata(invocationOnMock));
            }
        });

        when(mockito.getMock(eq(invocation), any(GenericMetadataSupport.class)))
                .thenAnswer(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                        return testObject;
                    }
                });

        when(mockito.getMock(eq(testObject), eq(mapKeyType.getTypeParameters()[0].getType())))
                .thenReturn(numberMock);

        when(mockito.getMock(eq(testObject), eq(mapValueType.getTypeParameters()[0].getType())))
                .thenReturn(setMock);

        when(mockito.getMock(eq(testObject), eq(mapReturnType.getTypeParameters()[0].getType())))
                .thenReturn(mapMock);

        when(mockito.getMock(eq(testObject), eq(mapValueType.getTypeParameters()[1].getType())))
                .thenReturn(setMock);

        when(mockito.getMock(eq(testObject), eq(setNumberType)))
                .thenReturn(numberMock);

        when(mockito.getMock(eq(testObject), eq(setNumberType.getTypeParameters()[0].getType())))
                .thenReturn(numberMock);

        Map.Entry<Number, Set<Number>> entry = mockito.answer(invocation);
        Assert.assertEquals(numberMock, entry.getKey());
        Assert.assertEquals(setMock, entry.getValue());
    }

    @Test
    public void testCreateNewDeepStubMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Object mock = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);
        Assert.assertNotNull(mock);
        Assert.assertFalse(mockitoCore.isTypeMockable(mock.getClass()));
    }

    @Test
    public void testGetMock_NoMatchingInvocation() throws Throwable {
        ReturnsDeepStubs mockito = mock(ReturnsDeepStubs.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(mockito.getMock(eq(invocation), any(GenericMetadataSupport.class))).thenReturn(null);
        try {
            mockito.getMock(invocation, mock(GenericMetadataSupport.class));
            fail("Expected null when no matching invocation was found");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testGetMock_MatchingInvocation() throws Throwable {
        ReturnsDeepStubs mockito = mock(ReturnsDeepStubs.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mock = mock(Object.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = new StubbedInvocationMatcher(invocation, eq(mock));
        InvocationContainerImpl container = new InvocationContainerImpl(stubbedInvocationMatcher, new AnswerStub() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return "Test";
            }
        });
        when(mockito.getMock(eq(container), any(GenericMetadataSupport.class))).thenReturn(mock);
        Assert.assertEquals(mock, mockito.getMock(invocation, stubbedInvocationMatcher.getReturnTypeMetadata()));
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        ReturnsDeepStubs mockito = mock(ReturnsDeepStubs.class);
        Object mock = mock(Object.class);
        InvocationContainerImpl container = new InvocationContainerImpl(null, new AnswerStub() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return "Test";
            }
        });
        when(mockito.recordDeepStubMock(eq(mock), eq(container))).thenReturn(mock);
        Answer answer = mockito.getMockAnswer();
        Assert.assertEquals(new AnswerStub() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return mock;
            }
        }, answer);
    }

    private GenericMetadataSupport getReturnTypeMetadata(InvocationOnMock invocation) {
        ReturnsDeepStubs returnsDeepStubs = mock(ReturnsDeepStubs.class);
        Object mock = mock(Object.class);
        MockSettings mockSettings = new MockSettings();
        when(mockitoCore.mock(any(Class.class), withSettings(mockSettings)))
                .thenReturn(mock);
        when(returnsDeepStubs.actualParameterizedType(mock)).thenReturn(mockSettings.getTypeToMock());
        return mockSettings.getTypeToMock();
    }
}

@ExtendWith(MockitoExtension.class)
class AnswerStub implements Answer<Object> {
    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        return null;
    }
}

class ReturnsEmptyValues {
    public Object returnValueFor(Class<?> clazz) {
        return null;
    }
}