package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

    @Test
    public void testGetMockWithMatchingInvocationAndReturnType() {
        // given
        InterfaceFirst interfaceFirst = mock(InterfaceFirst.class, new Answers.ReturnsDeepStubs());
        Class<? extends InterfaceFirst> interfaceFirstType = (Class<? extends InterfaceFirst>) interfaceFirst.getClass();
        Class<? extends List<Number>> listType = List.class.cast(interfaceFirst.entrySet().iterator().next().getValue().iterator().next().getClass());
        Class<? extends Number> numberType = Number.class;

        // when
        Object mockList = interfaceFirst.entrySet().iterator().next().getValue().iterator().next();
        Object mockNumber = (Object) mockList;

        // then
        Object returnedMock = new ReturnsDeepStubs().getMock(
                new InvocationOnMock(interfaceFirst, "entrySet", new Class[0]),
                new GenericMetadataSupport() {
                    @Override
                    public Class<?> rawType() {
                        return List.class;
                    }

                    @Override
                    public Class<?>[] rawExtraInterfaces() {
                        return new Class[0];
                    }

                    @Override
                    public Type getType() {
                        return null;
                    }
                });

        verify(returnedMock).equals(mockList);
        verify(returnedMock).getClass().isAssignableFrom(listType);

        Object returnedNumber = new ReturnsDeepStubs().getMock(
                new InvocationOnMock(mockList, "iterator", new Class[0]),
                new GenericMetadataSupport() {
                    @Override
                    public Class<?> rawType() {
                        return Iterator.class;
                    }

                    @Override
                    public Class<?>[] rawExtraInterfaces() {
                        return new Class[] {Number.class};
                    }

                    @Override
                    public Type getType() {
                        return null;
                    }
                });

        verify(returnedNumber).equals(mockNumber);
        verify(returnedNumber).getClass().isAssignableFrom(numberType);
    }

    @Test
    public void testGetMockWithMismatchedInvocation() {
        // given
        InterfaceFirst interfaceFirst = mock(InterfaceFirst.class, new Answers.ReturnsDeepStubs());

        // when, then
        assertNull(new ReturnsDeepStubs().getMock(
                new InvocationOnMock(interfaceFirst, "toString", new Class[0]),
                new GenericMetadataSupport() {
                    @Override
                    public Class<?> rawType() {
                        return String.class;
                    }

                    @Override
                    public Class<?>[] rawExtraInterfaces() {
                        return new Class[0];
                    }

                    @Override
                    public Type getType() {
                        return null;
                    }
                }));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetMockWithMismatchedReturnType() {
        // given
        InterfaceFirst interfaceFirst = mock(InterfaceFirst.class, new Answers.ReturnsDeepStubs());

        // when
        new ReturnsDeepStubs().getMock(
                new InvocationOnMock(interfaceFirst, "toString", new Class[0]),
                new GenericMetadataSupport() {
                    @Override
                    public Class<?> rawType() {
                        return Integer.class;
                    }

                    @Override
                    public Class<?>[] rawExtraInterfaces() {
                        return new Class[0];
                    }

                    @Override
                    public Type getType() {
                        return null;
                    }
                });

        // then - should throw an exception
    }

    @Test
    public void testGetMockWithMatchingInvocationButDifferentGenericTypes() {
        // given
        InterfaceFirst interfaceFirst = mock(InterfaceFirst.class, new Answers.ReturnsDeepStubs());
        InterfaceSecond interfaceSecond = mock(InterfaceSecond.class, new Answers.ReturnsDeepStubs());

        // when
        Object mockList1 = interfaceFirst.entrySet().iterator().next().getValue().iterator().next();
        Object mockList2 = interfaceSecond.entrySet().iterator().next().getValue().iterator().next();

        // then
        assertNotEquals(mockList1, mockList2);
        Object mockList1DeepStub = new ReturnsDeepStubs().getMock(
                new InvocationOnMock(interfaceFirst, "entrySet", new Class[0]),
                new GenericMetadataSupport() {
                    @Override
                    public Class<?> rawType() {
                        return List.class;
                    }

                    @Override
                    public Class<?>[] rawExtraInterfaces() {
                        return new Class[0];
                    }

                    @Override
                    public Type getType() {
                        return null;
                    }
                });

        Object mockList2DeepStub = new ReturnsDeepStubs().getMock(
                new InvocationOnMock(interfaceSecond, "entrySet", new Class[0]),
                new GenericMetadataSupport() {
                    @Override
                    public Class<?> rawType() {
                        return List.class;
                    }

                    @Override
                    public Class<?>[] rawExtraInterfaces() {
                        return new Class[0];
                    }

                    @Override
                    public Type getType() {
                        return null;
                    }
                });

        assertNotEquals(mockList1DeepStub, mockList2DeepStub);
        assertEquals(mockList1DeepStub.getClass(), mockList1.getClass());
        assertEquals(mockList2DeepStub.getClass(), mockList2.getClass());
    }

    interface InterfaceFirst extends Map<Integer, Set<Number>> {}
    interface InterfaceSecond extends Map<String, Set<Integer>> {}
}