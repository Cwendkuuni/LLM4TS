package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertInstanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.util.reflection.GenericMetadataSupport.inferFrom;

import org.junit.Test;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.AnswerCalledWith;

import java.io.Serializable;
import java.lang.reflect.Method;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());

        Method method = mock.getClass().getMethod("answer", InvocationOnMock.class);
        Answer<Object> answer = new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "answer";
            }
        };

        assertInstanceOf(Answer.class, mock.answer(new InvocationOnMock(mock, method)));
    }

    @Test
    public void testGetMock() throws Exception {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = new MockUtil().getMockHandler(mock).getInvocationContainer();

        Class<?> clz = Object.class;
        Method method = clz.getMethod("getMock", InvocationOnMock.class);

        // Case 1: Invocation matches the recorded stub
        StubbedInvocationMatcher stubbedInvocationMatcher1 = new StubbedInvocationMatcher(new AnswerCalledWith(mock), method);
        container.addStubbedInvocation(stubbedInvocationMatcher1);
        Object stub = ReturnsDeepStubs.class.newInstance();
        Object answer1 = stub.getMock(new InvocationOnMock(mock, method));
        assertInstanceOf(mock.getClass(), answer1);

        // Case 2: Invocation does not match any recorded stub
        Object answer2 = ReturnsDeepStubs.class.newInstance().getMock(new InvocationOnMock(mock, method));
        assertInstanceOf(new ReturnsEmptyValues().getClass(), answer2);

        // Case 3: Invocation does not match the recorded stub and the generic type is not mockable
        clz = DeepStubsTest.Nested.class;
        method = clz.getMethod("getNested");
        StubbedInvocationMatcher stubbedInvocationMatcher2 = new StubbedInvocationMatcher(new AnswerCalledWith(mock), method);
        container.addStubbedInvocation(stubbedInvocationMatcher2);
        Object stub2 = ReturnsDeepStubs.class.newInstance();
        Object answer3 = stub2.getMock(new InvocationOnMock(mock, method));
        assertInstanceOf(NewInstance.class, answer3);

        // Case 4: Invocation matches the deep stub
        InvocationContainerImpl container2 = new MockUtil().getMockHandler(new NewInstance()).getInvocationContainer();
        Object deepMock = mock(DeepStubsTest.Deep.class, new ReturnsDeepStubs());
        container2.addAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return deepMock;
            }
        }, false);
        Object answer4 = ReturnsDeepStubs.class.newInstance().getMock(new InvocationOnMock(mock, method));
        assertInstanceOf(deepMock.getClass(), answer4);
    }

    static class Deep {
        Deep getDeep() {
            return this;
        }
    }

    static class Nested<T> {
        T getNested() {
            return null;
        }
    }

    static class NewInstance {
        NewInstance getNewInstance() {
            return this;
        }
    }
}