import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.MockSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;

import java.io.Serializable;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerWithEmptyValuesDelegateForNonMockableType() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        assertSame(mock, delegateAnswer().returnValueFor(Object.class));
    }

    @Test
    public void testAnswerWithReturnedMockForInvocationMatchesStubbedInvocation() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = new InvocationContainerImpl();
        StubbedInvocationMatcher stubbedInvocationMatcher = new StubbedInvocationMatcher(container, any());
        container.addStubbedInvocation(stubbedInvocationMatcher);

        when(mock.foo()).thenAnswer(stubbedInvocationMatcher);

        Object returnedMock = new ReturnsDeepStubs().getMock(mock(InvocationOnMock.class), mock(GenericMetadataSupport.class));
        Object result = returnedMock.answer(mock(InvocationOnMock.class));
        assertEquals(mock, result);
    }

    @Test
    public void testAnswerWithReturnedMockForInvocationDoesNotMatchAnyStubbedInvocation() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = new InvocationContainerImpl();

        Object returnedMock = new ReturnsDeepStubs().getMock(mock(InvocationOnMock.class), mock(GenericMetadataSupport.class));
        Object result = returnedMock.answer(mock(InvocationOnMock.class));
        assertNotSame(mock, result);
    }

    @Test(expected = Throwable.class)
    public void testAnswerWithThrownException() throws Throwable {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        doThrow(new RuntimeException("Test Exception")).answer(mock(InvocationOnMock.class));

        Object result = ((ReturnsDeepStubs) mock).answer(mock(InvocationOnMock.class));
        fail("Expected an exception to be thrown.");
    }

    @Test
    public void testGetMockWithNonMockableType() throws Throwable {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        Class<?> nonMockableType = int[].class;
        assertSame(new ReturnsEmptyValues().returnValueFor(nonMockableType), new ReturnsDeepStubs().getMock(mock, mock(GenericMetadataSupport.class)).answer(mock(InvocationOnMock.class)));
    }

    @Test
    public void testGetMockWithNestedGenericType() throws Throwable {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport() {
            @Override
            public Class<?> rawType() {
                return Map.class;
            }

            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{String.class, Integer.class};
            }

            @Override
            public Collection<Class<?>> getRawExtraInterfaces() {
                return Arrays.asList(Collection.class);
            }
        };

        Object returnedMock = new ReturnsDeepStubs().getMock(mock, returnTypeGenericMetadata);
        assertInstanceOf(Map.class, returnedMock);
        assertInstanceOf(Collection.class, returnedMock);
    }

    @Test
    public void testCreateNewDeepStubMockWithEmptyExtraInterfaces() throws Throwable {
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport() {
            @Override
            public Class<?> rawType() {
                return Map.class;
            }

            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{String.class, Integer.class};
            }
        };

        Object returnedMock = new ReturnsDeepStubs().createNewDeepStubMock(returnTypeGenericMetadata);
        assertInstanceOf(Map.class, returnedMock);
    }

    @Test
    public void testCreateNewDeepStubMockWithExtraInterfaces() throws Throwable {
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport() {
            @Override
            public Class<?> rawType() {
                return Map.class;
            }

            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{String.class, Integer.class};
            }

            @Override
            public Collection<Class<?>> getRawExtraInterfaces() {
                return Arrays.asList(Collection.class, Iterator.class);
            }
        };

        Object returnedMock = new ReturnsDeepStubs().createNewDeepStubMock(returnTypeGenericMetadata);
        assertInstanceOf(Map.class, returnedMock);
        assertInstanceOf(Collection.class, returnedMock);
        assertInstanceOf(Iterator.class, returnedMock.entrySet().iterator());
    }

    private Answer<?> delegateAnswer() {
        return new ReturnsEmptyValues();
    }
}