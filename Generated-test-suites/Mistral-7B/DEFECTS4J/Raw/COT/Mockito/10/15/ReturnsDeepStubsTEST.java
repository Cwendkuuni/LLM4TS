import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericMetadataSupport;
import java.lang.reflect.Type;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer_ReturnsExistingMock() {
        // given
        Interface mock = mock(Interface.class, new ReturnsDeepStubs());

        // when
        Object result = mock.method();

        // then
        assertSame(mock, result);
    }

    @Test
    public void testDeepStub_ReturnsNewMockForDifferentInvocation() {
        // given
        Interface mock1 = mock(Interface.class, new ReturnsDeepStubs());
        Interface mock2 = mock(Interface.class, new ReturnsDeepStubs());

        // when
        Object result1 = mock1.method();
        Object result2 = mock2.method();

        // then
        assertNotSame(result1, result2);
        assertSame(mock1, result1);
        assertSame(mock2, result2);
    }

    @Test(expected = IllegalStateException.class)
    public void testAnswer_ThrowsExceptionForNonMockableType() {
        // given
        Class<String> stringClass = String.class;
        Answer<Object> answer = new ReturnsDeepStubs();

        // when
        InvocationOnMock invocation = Mockito.mockinvocation(String.class);

        // then
        answer.answer(invocation);
    }

    @Test
    public void testAnswer_DeferToDelegateForNonGenericReturnType() {
        // given
        Interface mock = mock(Interface.class, new ReturnsDeepStubs());
        Type returnType = mock.getClass().getMethod("method").getGenericReturnType();
        Class<?> rawType = (Class<?>) returnType.getComponentType();

        // when
        Answer<Object> answer = new ReturnsDeepStubs();
        Object result = answer.answer(Mockito.mockinvocation(rawType));

        // then
        assertSame(ReturnsDeepStubs.delegate().returnValueFor(rawType), result);
    }

    @Test
    public void testAnswer_RecordsStubbedAnswerForSameInvocation() {
        // given
        Interface mock = mock(Interface.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = (InvocationContainerImpl) Mockito.getMockHandler(mock).getInvocationContainer();

        // when
        mock.method();
        Object answer = mock.method();

        // then
        assertSame(answer, container.getAnswers().get(0).answer(Mockito.mockinvocation(Interface.class)));
    }

    @Test
    public void testAnswer_ReturnsNewMockForDifferentInvocationWithSameReturnType() {
        // given
        Interface mock1 = mock(Interface.class, new ReturnsDeepStubs());
        Interface mock2 = mock(Interface.class, new ReturnsDeepStubs());
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.actualParameterizedType(mock1);

        // when
        Object result1 = mock1.method();
        Object result2 = mock2.method();

        // then
        assertNotSame(result1, result2);
        assertSame(mock1, result1);
        assertSame(mock2, result2);

        // when
        Object result1Again = mock1.method();

        // then
        assertSame(result1, result1Again);
    }

    @Test
    public void testNewDeepStubMock_ReturnsNewMockWithContextualGenericsMetadata() {
        // given
        Class<Interface> interfaceType = Interface.class;
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport() {
            @Override
            public Type getRawType() {
                return interfaceType;
            }

            @Override
            public Type[] getComponentTypes() {
                return new Type[0];
            }

            @Override
            public Type[] getRawExtraInterfaces() {
                return new Type[0];
            }
        };

        // when
        Object mock = new ReturnsDeepStubs().newDeepStubMock(returnTypeGenericMetadata);

        // then
        assertInstanceOf(Interface.class, mock);
        assertTrue(returnTypeGenericMetadata.isTypeMockable(Interface.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testNewDeepStubMock_ThrowsExceptionForNonMockableType() {
        // given
        Class<String> stringClass = String.class;
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport() {
            @Override
            public Type getRawType() {
                return stringClass;
            }

            @Override
            public Type[] getComponentTypes() {
                return new Type[0];
            }

            @Override
            public Type[] getRawExtraInterfaces() {
                return new Type[0];
            }
        };

        // when
        new ReturnsDeepStubs().newDeepStubMock(returnTypeGenericMetadata);

        // then
        fail("Expected IllegalStateException for non-mockable type");
    }

    @Test
    public void testRecordDeepStubAnswer_AddsAnswerToContainerForSameInvocation() {
        // given
        Interface mock = mock(Interface.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = (InvocationContainerImpl) Mockito.getMockHandler(mock).getInvocationContainer();

        // when
        Object answer1 = container.getAnswers().get(0).answer(Mockito.mockinvocation(Interface.class));
        Object answer2 = container.getAnswers().get(1).answer(Mockito.mockinvocation(Interface.class));

        // then
        assertEquals(answer1, answer2);
        assertEquals(mock, answer1);
        assertEquals(mock, answer2);
    }

    @Test
    public void testRecordDeepStubAnswer_AddsNewAnswerToContainerForDifferentInvocation() {
        // given
        Interface mock1 = mock(Interface.class, new ReturnsDeepStubs());
        Interface mock2 = mock(Interface.class, new ReturnsDeepStubs());
        InvocationContainerImpl container1 = (InvocationContainerImpl) Mockito.getMockHandler(mock1).getInvocationContainer();
        InvocationContainerImpl container2 = (InvocationContainerImpl) Mockito.getMockHandler(mock2).getInvocationContainer();

        // when
        Object answer1 = container1.getAnswers().get(0).answer(Mockito.mockinvocation(Interface.class));
        Object answer2 = container2.getAnswers().get(0).answer(Mockito.mockinvocation(Interface.class));

        // then
        assertNotSame(answer1, answer2);
        assertSame(mock1, answer1);
        assertSame(mock2, answer2);
    }
}