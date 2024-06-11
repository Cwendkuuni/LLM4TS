package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.mock.MockComponentImplementation;
import org.mockito.internal.mock.MockSettings;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.AnswerWithProvider;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerWithReturnType() {
        MockSettings mockSettings = mockSettings();
        GivenMock mock = givenMock(mockSettings.getTypeToMock());

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object deepStub = returnsDeepStubs.answer(mockArg(eq(mock)));

        assertSame(mock, deepStub);
    }

    @Test
    public void testAnswerWithReturnTypeAndGenericTypes() {
        MockSettings mockSettings = mockSettingsWithMockedTypes(Map.class, Number.class, Set.class);
        GivenMock<Map<String, Set<Number>>> mapMock = givenMock(Map.class, mockSettings);
        GivenMock<Set<Number>> setMock = givenMock(Set.class, mockSettingsWithMockedTypes(Number.class));

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object deepStub = returnsDeepStubs.answer(mockArg(eq(mapMock)).argThat(arg -> arg.get("key").equals("value")));

        assertSame(setMock.get(argThat(arg -> arg.intValue() == 1)), deepStub);
    }

    @Test
    public void testAnswerWithReturnTypeAndNestedGenericTypes() {
        MockSettings mockSettings = mockSettingsWithMockedTypes(Map.class, Map.class, Map.class, Number.class, Set.class);
        GivenMock<Map<String, Map<String, Map<String, Number>>>> outerMapMock = givenMock(Map.class, mockSettings);
        GivenMock<Map<String, Map<String, Number>>> innerMapMock = givenMock(Map.class, mockSettingsWithMockedTypes(Map.class, Number.class));
        GivenMock<Map<String, Number>> innerInnerMapMock = givenMock(Map.class, mockSettingsWithMockedTypes(String.class, Number.class));

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object deepStub = returnsDeepStubs.answer(mockArg(eq(outerMapMock)).argThat(arg -> arg.get("key1").get("key2").get("key3").equals("value")));

        assertSame(innerInnerMapMock.get("key3"), deepStub);
    }

    @Test
    public void testAnswerWithReturnTypeAndCustomAnswer() {
        MockSettings mockSettings = mockSettings();
        GivenMock<Map<String, Set<Number>>> mapMock = givenMock(Map.class, mockSettings);
        GivenMock<Set<Number>> setMock = givenMock(Set.class, mockSettings);

        Answer<Set<Number>> answer = AnswerWithProvider.answerFromProvider(() -> setMock);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        returnsDeepStubs = returnsDeepStubs.thenAnswer(answer);

        Object deepStub = returnsDeepStubs.answer(mockArg(eq(mapMock)));

        assertSame(setMock, deepStub);
    }

    @Test
    public void testAnswerWithReturnTypeAndCustomAnswerWithNestedTypes() {
        MockSettings mockSettings = mockSettingsWithMockedTypes(Map.class, Map.class, Map.class, Number.class, Set.class);
        GivenMock<Map<String, Map<String, Map<String, Number>>>> outerMapMock = givenMock(Map.class, mockSettings);
        GivenMock<Map<String, Map<String, Number>>> innerMapMock = givenMock(Map.class, mockSettingsWithMockedTypes(Map.class, Number.class));
        GivenMock<Map<String, Number>> innerInnerMapMock = givenMock(Map.class, mockSettingsWithMockedTypes(String.class, Number.class));

        Answer<Map<String, Number>> answer = AnswerWithProvider.answerFromProvider(() -> innerInnerMapMock);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        returnsDeepStubs = returnsDeepStubs.thenAnswer(answer);

        Object deepStub = returnsDeepStubs.answer(mockArg(eq(outerMapMock)).argThat(arg -> arg.get("key1").get("key2").get("key3").equals("value")));

        assertSame(innerInnerMapMock, deepStub);
    }

    private GivenMock<Object> givenMock(Class<?> clazz, MockSettings mockSettings) {
        Object mock = mock(clazz, withSettings(mockSettings));
        MockitoAnnotations.initMocks(this);
        return mockito(mock);
    }

    private GivenMock<Object> givenMock(Class<?> clazz, MockSettings mockSettings, Class<?>[] extraInterfaces) {
        Object mock = mock(clazz, withSettings(mockSettings).extraInterfaces(extraInterfaces));
        MockitoAnnotations.initMocks(this);
        return mockito(mock);
    }

    private GivenMock<Object> givenMock(Class<?> clazz, MockSettings mockSettings, Class<?>[] extraInterfaces, Answer<Object> answer) {
        Object mock = mock(clazz, withSettings(mockSettings).extraInterfaces(extraInterfaces).defaultAnswer(answer));
        MockitoAnnotations.initMocks(this);
        return mockito(mock);
    }

    private MockSettings mockSettings() {
        return withSettings();
    }

    private MockSettings mockSettingsWithMockedTypes(Class<?>... types) {
        return withSettings().defaultAnswer(new ReturnsDeepStubs());
    }

    private InvocationOnMock mockArg(Matcher<? super Object> matcher) {
        return argThat(matcher);
    }

    private GivenMock<Object> mockito(Object mock) {
        return mockito(mock, null);
    }

    private GivenMock<Object> mockito(Object mock, Answer<Object> answer) {
        return new GivenMock<Object>() {
            @Override
            public Object getMock() {
                return mock;
            }

            @Override
            public Answer<Object> getAnswer() {
                return answer;
            }
        };
    }

    private static class GivenMock<T> {
        private T mock;
        private Answer<Object> answer;

        public T getMock() {
            return mock;
        }

        public Answer<Object> getAnswer() {
            return answer;
        }
    }
}